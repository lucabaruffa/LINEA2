package PLC;
import java.awt.Color;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import DB.CheckControl;
import Moka7.IntByRef;
import Moka7.S7;
import Moka7.S7Client;
import View.Scarti;
import linea2.ArrayBatteriePostazione;
import linea2.Batteria;
import linea2.ConfiguratoreLinea;
import linea2.Indicatore;
import linea2.LoggerFile;
import linea2.Setting;

public class readerPLC implements Runnable   {
	
	
	public byte[] Buffer = new byte[65536]; // 64K buffer (maximum for S7400 systems)
	public int Rack = 0; 
	public int Slot = 1; 
    public final S7Client Client = new S7Client();
    public int DataToMove=48; // contiene la dimensione del DB connesso
    public int DataToMove_170=36; // contiene la dimensione del DB connesso
    private int DB = -1; // db
    private int CurrentStatus = S7.S7CpuStatusUnknown;
    private int sleep = Setting.TIMER_SLEEP_READERPLC;  //tempo di ciclo
    private int NUMERO_CICLI_START_STOP = Setting.NUMERO_CICLI_START_STOP; // sleep * NUMERO_CICLI_START_STOP = millisecondi di oltre il quale scatta il microstop
    private boolean running = false;
   
    private int nomeStazione = -1;
    private JProgressBar bufferBatterie;
    private String tempo_ultima_batteria="01/01/1970 00:00:00";
    private Indicatore indicatore;
    private int conteggio = 0;    
    private Setting setting;
    private boolean prima_lettura = true;  //avvio la prima lettura del programma. Ho riavviato il programma
    public GregorianCalendar data = new GregorianCalendar(); 
	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	protected ArrayBatteriePostazione array;
	private CheckControl check;
	private int DIMENSIONI_BUFFER_PLC = Setting.DIMENSIONI_BUFFER_PLC; // buffer PLC
	private boolean riazzera_contatori = true;
	private int numero_di_cicli_senza_batterie = 0;
	
	private boolean segnalato = false, stopped = false; //setStartStop
	
	private GregorianCalendar dax = new GregorianCalendar(); 
	private ConfiguratoreLinea configuratore;
	
	private String timestamp_ultima_batteria = "01/01/2020 00:00:00";
	private String codice_batteria_old="OLD";
	
	private int numero_batterie_riprocessate = 0;
	private int numero_batterie_scartate = 0;
	
	private static LoggerFile log = new LoggerFile();
	
	private Scarti viewer = new Scarti();
	
	private JTextField areaErrore;
	private String stato_batteria_postazione_bilancia2 = "1";
	
	//private ControlloRiempimentoAcido controllopesoacido = new ControlloRiempimentoAcido();
	
	
	public readerPLC() {
		//monitor.append("costruttore");
	}//fine costruttore
	
	public void avvia() {
		running = true;
	}
	
	
	public void stop()
	{
		running = false;
	}
	
	//ritorna true se questa stazione è di controllo, false altrimenti. ULTIMA POSTAZIONE
	private boolean isFinalController()
	{
		if (nomeStazione==Setting.STAZIONE_DI_CONTROLLO_2) return true;
		else return false;
	}
	
	//ritorna true se questa stazione è di controllo, false altrimenti.POSTAZIONE INTERMEDIA (7)
	private boolean isController()
		{
			if (nomeStazione==Setting.STAZIONE_DI_CONTROLLO_1) return true;
			else return false;
		}
	
	
	
	public readerPLC(int db, JProgressBar b, int nome, Indicatore ind, ArrayBatteriePostazione arrayBat) {
		
		//monitor =g;
		nomeStazione = nome;
		DB = db;
		indicatore = ind;
		array = arrayBat;
		
		check = new CheckControl();
		
		try {
			dax.setTime((sdf.parse(tempo_ultima_batteria)));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
    	
		
		try {
			setting = new Setting(); //non reinizializzo setting
		} catch (Exception e) {
			log.write("ERRORE CARICAMENTO CONFIGURAZIONE NEL MODULO readerPLC\n");
			e.printStackTrace();
		}
		
				
		areaErrore = setting.getAreaError();
		//monitor.append("costruttore");
		bufferBatterie = b;
			
		Rack = Integer.parseInt(setting.getRACK());
		Slot = Integer.parseInt(setting.getSLOT());
		
		bufferBatterie.setString("0 BATTERIE IN CODA"); 
		
		try {
			configuratore = new ConfiguratoreLinea();
		}catch(Exception h) {
			
			log.write("Errore reader PLC, caricamento configurazione: "+h.toString());
		}
		
		
		numero_batterie_scartate = setting.totale_batterie_scartate[nomeStazione-1];
		conteggio = setting.totale_batterie_lavorate[nomeStazione-1];
		
		//tento di caricare le informazione dell'ultima stazione
		try {
			if (nomeStazione==Setting.STAZIONE_DI_CONTROLLO_2){
				conteggio = Integer.parseInt(setting.ReadProperties("conteggio_finale",nome));
				numero_batterie_scartate = Integer.parseInt(setting.ReadProperties("numero_batterie_scartate",nome));
				log.write("\nCARICAMENTO PROPERTIES readerPLC. conteggio="+conteggio+" - Scartate="+numero_batterie_scartate+"\n");
				indicatore.setConteggio(""+conteggio);
				indicatore.scarto.setText(""+numero_batterie_scartate);
			}else {
				//conteggio = Integer.parseInt(setting.ReadProperties("conteggio_postazione"+nome));
				indicatore.riprocessato.setText((setting.ReadProperties("numero_batterie_riprocessate_postazione"+nome,nome)));
				numero_batterie_riprocessate = Integer.valueOf(setting.ReadProperties("numero_batterie_riprocessate_postazione"+nome,nome));
			}
			
		}catch(Exception j) {
			log.write("Errore reader PLC, impostazioni properties: "+j.toString() +" - REINIZIALIZZO PROPERTIES per postazione: " + nome);
			//setting.WriteProperties("conteggio_finale", "0","numero_batterie_scartate", "0");
			
		}
		
		
	}//fine costruttore
    
    
    public void HexDump(byte[] Buffer, int Size)
    {
        int r=0;
        String Hex = "";
        
        for (int i=0; i<Size; i++)
        {
            int v = (Buffer[i] & 0x0FF);
            String hv = Integer.toHexString(v);     
            
            if (hv.length()==1)
                hv="0"+hv+" ";
            else
                hv=hv+" ";
            
            Hex=Hex+hv;
            
            r++;
            if (r==16)
            {
            	log.write(Hex+" ");
            	log.write(S7.GetPrintableStringAt(Buffer, i-15, 16)+"\n");
            	System.out.print(Hex+" ");
                System.out.println(S7.GetPrintableStringAt(Buffer, i-15, 16));
                Hex="";
                r=0;
            }
        }
        int L=Hex.length();
        if (L>0)
        {
            while (Hex.length()<49)
                Hex=Hex+" ";
            
            log.write(Hex);
            log.write(S7.GetPrintableStringAt(Buffer, Size-r, r)+"\n");
            System.out.print(Hex);
            System.out.println(S7.GetPrintableStringAt(Buffer, Size-r, r));                       
        }
        else {
            System.out.println();
            log.write("\n");
        }
    }

	
	
	public boolean DBGet()
    {
        IntByRef SizeRead = new IntByRef(0);
        int Result = Client.DBGet(DB, Buffer, SizeRead);        
        if (Result==0)
        {
        	
        	DataToMove = SizeRead.Value; // memorizzo dimensioni del DB
        	//System.out.println("DB "+DB+" - Size read "+DataToMove+" bytes");
        	log.write("DB "+DB+" - DIMENSIONI DB "+DataToMove+" bytes\n");
        	HexDump(Buffer, DataToMove);
        	return true;
        }  else {
        	log.write("DB GET fallita !");
        }
        return false;        
    }
	//fine DBGet
	
	public void DBRead(int start)
    {
		
		int Result = Client.ReadArea(S7.S7AreaDB, DB, start, DataToMove * DIMENSIONI_BUFFER_PLC, Buffer);
		
        if (Result==0)
        {
        	//log.write("\nDBAREA OK.  DB="+DB+"\n" );
        }else
        {
        	log.write("\nreader plc -> PROBLEMA CON READAREA!!!!!!!!! ERRORE =" + Result +". db="+DB);
        }
       
    }      
    
    public void DBWrite(int db_w)
    {
    	
    	int Result = Client.WriteArea(S7.S7AreaDB, db_w, 0, DataToMove_170, Buffer);
        if (Result==0)
        {
        	//log.write("DB "+DB+" succesfully written ");
        }
    	
    }  
    
        
    
    public void ShowStatus()
    {
    	try {
	        IntByRef PlcStatus = new IntByRef(S7.S7CpuStatusUnknown);
	        int Result = Client.GetPlcStatus(PlcStatus);
	        if (Result==0)
	        {
	            System.out.print("PLC Status : ");
	            switch (PlcStatus.Value)
	            {
	                case S7.S7CpuStatusRun :
	                    //monitor.append("RUN");
	                    //statoplc.setText("RUNNING");
	                    //statoplc.setBackground(new Color(50, 168, 82));
	                    break;
	                case S7.S7CpuStatusStop :
	                	//monitor.append("STOP");
	                	//statoplc.setText("STOP");
	                	//statoplc.setBackground(new Color(219, 22, 65));
	                    break;
	                default :    
	                	log.write("Unknown ("+PlcStatus.Value+")");
	                	//statoplc.setText("SCONOSCIUTO");
	            }
	        }//fine if
	        CurrentStatus = PlcStatus.Value;
    	}
	        catch(Exception e) {
	        	log.write("Errore in SHOWSTATUS: " + e.toString());
	        }
        
    }//fine showstatus
	
	
	@Override
	public void run(){
		log.write("AVVIO RUN");
		while(running) {
			//monitor.append("RUNNING\n");
			if(Client.Connected) {
				
				long startTime = System.nanoTime();
				
				
				leggiArrayBatterie();
				ShowStatus();
					
				long endTime = System.nanoTime();
				//monitor.append("\nPostazione "+ nomeStazione + ". Tempo esecuzione = "+ (endTime - startTime)/1000000+" millisecondi");  
			}//fine if
			else {
				log.write("NON CONNESSO! - AVVIO TENTATIVO DI CONNESSIONE");
				connetti();
				
			}
			
			try {
				//monitor.append("slider ="+sleep+"\n");
				Thread.sleep(sleep);
			}catch(Exception e) {
				log.write("\nreader plc Errore inSleep!\n");
			}//fine catch
			
		}//fine while
       
		log.write("- ReaderPLC - Chiusura regolare THread POSTAZIONE "+ nomeStazione);
    
	}//fine run
	
	
	
	
	 public void leggiArrayBatterie() {
	    	
		    int offset_DBAREA = 0;	
	  	    DBRead(0);
	    	int numero_batterie_aggiunte = 0;
	    	
	    	//vado a puntare l'ultima batteria del buffer
	    	//int indirizzo_start = OFFSET_BATTERIA * (Setting.DIMENSIONI_BUFFER_PLC-1) ;  //la prima batteria è quella in lavorazione. Leggo dalla seconda
	    	
	    	Batteria batteria=null;
	    	String stato = "0";
	    	String timestamp = "";
	    	String cod_batteria ="";
	    	
	        for(int i=(DIMENSIONI_BUFFER_PLC-1);i>=0;i--) {
	        	
	        	stato = "-1";
	        	offset_DBAREA = i * DataToMove;       		        	
	        	//indirizzo_start = (OFFSET_BATTERIA * i);
	        	//int offset_DBAREA = 0;
	        	
	        	//DBRead(indirizzo_start);
	        	//monitor.append("INDEX : "+i+"\n");
	        	
	        	cod_batteria = S7.GetStringAt(Buffer, offset_DBAREA ,30).trim();
	        	offset_DBAREA += 30;
	        	
	        	timestamp = (S7.GetDateAt(Buffer, offset_DBAREA )).toString();
	        	timestamp = getMyDate(timestamp, "dd/MM/yyyy HH:mm:ss", "EEE MMM dd HH:mm:ss zzz yyyy");
	        	
	        	try {
					data.setTime(sdf.parse(timestamp));
					//log.write("leggo la data :" + timestamp);
				} catch (ParseException e1) {
					log.write("readerPLC - Errore parsing data 331 : " + e1.toString());
				} 
	        	offset_DBAREA += 8;
	        	
	        	int tmp_stato = S7.GetWordAt(Buffer, offset_DBAREA);//leggo 256 se impostato a 1, 0 se impostato a 0
	        		        		        	
	        	if ((tmp_stato==255) || (tmp_stato==256) ) stato = "1";
	        	if ((tmp_stato==512)) stato = "0";
	        	if (tmp_stato==0) stato = "-1";
	        	
	        		        	
	        	offset_DBAREA += 2;
	        	
	        	int data1 = S7.GetDIntAt(Buffer, offset_DBAREA);
	        	offset_DBAREA += 4;
	        	
	        	//String data1_binary = Integer.toBinaryString(data1);
	        	
	        	int data2 = S7.GetDIntAt(Buffer, offset_DBAREA);
	        	offset_DBAREA += 4;
	        	
	        	
				try {
					batteria = new Batteria(i,cod_batteria,timestamp,""+stato,data1,data2);
					//log.write("Postazione:" +nomeStazione+  "  -> valore1 =" + data1);
					batteria.setPostazione(""+nomeStazione);
					
				} catch (Exception e) {
					log.write("ERRORE CREAZIONE BATTERIA: "+ e.toString());
					e.printStackTrace();
				}
        		
	        	
	        	try {
	        		
	        		//controllo batteria in ingresso. POSIZIONE 0 DELLA PILA
	        		if ((i==0) && (!codice_batteria_old.equals(cod_batteria) && cod_batteria != null)) {
	        			
	        			 		//se il codice letto ha dimensioni maggiori di 12 (codice teoricamente valido)
			        			 if (cod_batteria.length()>12) {
				        				 try { 
				        				 	codice_batteria_old = cod_batteria;		
				    	        			indicatore.setBatteriaZero(cod_batteria);
				    	        			numero_di_cicli_senza_batterie = 0;
				    	        			
				    	        			
				    	        			//nella prima postazione controllo la TIPOLOGIA DELLA BATTERIA
							    	        if (nomeStazione==1) {
							    	        		try {
								    	      				String tipo = cod_batteria.substring(0,2);
								    	       				if (tipo.equals("12")) setting.getTipologiaBatterie().setText("BTX 12");
								    	       				if (tipo.equals("14")) setting.getTipologiaBatterie().setText("BTX 14");
							    	        			}catch(Exception j) {
								    	       				log.write("readerPLC -> errore in getpostazione codifica tipo batteria");
								    	       			}
							    	        }//FINE IF POSTAZIONE 1
				    	        			
				    	        			
				    	        			if (isFinalController()) { //postazioni 7 e 10
				    	        				//log.write("Reader PLc -> postazione 10 arrivo batteria :" + cod_batteria);
								    	        				try {
								    	        					tempo_ultima_batteria = timestamp;
								    	        					//log.write("Reader PLc line444-> - timestamp =" + timestamp);
								    	        					
								    	        					scriviRisultatoScartoPostazione(batteria);
								    	        					
								    	        				}catch(Exception j) {
								    	        					log.write("Reader PLc line445-> - Errore scriviRisultato postazione "+nomeStazione+" :" + j.toString());
								    	        				}
				    	        				
								    	        				//AGGIORNO LA LINEA IN FUNZIONE	
								    	        				try {	
										    	 					SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    	 					Date date3 = new Date(System.currentTimeMillis());
										    	 					//String data_fermo_linea = sdf2.format(date);
										    	 					String dax3 = sdf3.format(date3);
										    	 					Setting.setData_aggiornamento(""+dax3);
										    	 				}catch(Exception k) {
										    	 					log.write("Reader PLC line456.errore dateformat . k:" + k.toString());
										    	 				}
								    	        				
								    	        				
								    	        				
				    	        								int total_batt = Integer.parseInt(indicatore.conteggio.getText());
								    	        				int total_scarto = Integer.parseInt(indicatore.scarto.getText());
								    	        							    	        				
								    	        				int totale_buone = total_batt - total_scarto;
								    	        				
								    	        				float good_percent = ((float)totale_buone / total_batt)*100; 
								    	        				float fake_percent = ((float)total_scarto / total_batt)*100;
								    	        				
								    	        				DecimalFormat df = new DecimalFormat("###.#");
								    	        				
								    	        				setting.getLabelBatterieBuone().setText(""+df.format(good_percent) + " %");
								    	        				setting.getLabelBatterieScartate().setText(""+df.format(fake_percent)+ " %");
				    	        				//}//stazione di controllo 10
				    	        			
				    	        			
				    	        			}//fine if postazione 10 e 7
				    	        			
				    	        			
				    	        			
				        				}catch(Exception j) {
					        				log.write("Reader PLC. Stazione :"+nomeStazione +" batteria: "+ cod_batteria+" old:"+codice_batteria_old+" - Errore lettura posizione buffer 0 : " + j.toString());
					        			}
			        			 			
			    	        		
			        			 }// fine if
			        			 
			        			 
	        		
			        			 //IN ATTESA DELLLA BATTERIA
			        			 else {
					        				 try {
							        				 indicatore.setBatteriaZero("IN ATTESA");
							        				 
							        				 if (isFinalController()){
							        					 areaErrore.setBackground(setting.grigio);
								        		   		 areaErrore.setText(Setting.ATTESA_BATTERIA);
								        		   		 setting.getCodiceBatteriaScartata().setText("IN ATTESA");
								        	   			 setting.getCodiceBatteriaScartata().setBackground(setting.grigio);
							        				 }
					        				 }catch(Exception j) {
				    	        					log.write("Reader PLc line501->   - Errore else areaerrore indicatorew "+nomeStazione+" :" + j.toString());
				    	        				}
						        				 
					        		 }//fine else
	        			
	        			
	        		
				     }//fine i==0	
	        		
	        		
	        		
	        		//else if (data.after(dax) && (i>0)){     //04/01/2021
	        		else if (data.after(dax) && (i>0) && (!isFinalController())){
				    	 
				    					    	
						    	 				try {
						    			        	//top = (array.getOnTop());
						    	        			dax.setTime((sdf.parse(timestamp)));  	
						    	        		}catch(Exception e){
						    	        			
						    	        		}
						    	 				
						    	 				numero_di_cicli_senza_batterie = 0; //resetto il contatore
						    	 				
						    	 				try {	
						    	 					SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    	 					Date date3 = new Date(System.currentTimeMillis());
						    	 					//String data_fermo_linea = sdf2.format(date);
						    	 					String dax3 = sdf3.format(date3);
						    	 					Setting.setData_aggiornamento(""+dax3);
						    	 				}catch(Exception k) {
						    	 					log.write("Reader PLC line532.errore dateformat . k:" + k.toString());
						    	 				}
						    		        						    	 				
						    	 				indicatore.setTempo(timestamp);
					    	        			indicatore.setBatteria(cod_batteria);
					    	        			indicatore.riprocessato.setText(""+numero_batterie_riprocessate);
					    	        			indicatore.scarto.setText(""+numero_batterie_scartate);
					    	        			tempo_ultima_batteria = timestamp;
					    	        			
					    	        try {	
					               		 
	
		
	
							if (check(batteria,i)) {
					               			 
					               			   boolean contiene = false;
					               			 
					               			 	contiene = array.contains(batteria);
					               			 
								                if (!contiene) {
								                	
								                	try {
								                		
								                		//if (nomeStazione.equals("2"))	    								
								    					  //log.write("Reader PLC. Postazione 2 da inseriemtno.cod:"+batteria.getCodiceBatteria()+" Ritorno = "+batteria.getStatoBatteria());
								                		
								                		//SE LA BATTERIA NON E' PRESENTE NELL ARRAY LA INSERISCO
								                		if (nomeStazione==9) {
								                			batteria.setStato(stato_batteria_postazione_bilancia2);
								                			stato_batteria_postazione_bilancia2 = "1";
								                		}
								                			
								                		
								                		array.addBatteriaTop(batteria);
								                		 
								                		
								                		conteggio +=1;
								                		
								                		float v1 = ((float)(data1))/1000;
								                		float v2 = ((float)(data2))/1000;
								                		
								                		indicatore.risultato.setBackground(setting.grigio);
								                		
								                		if (nomeStazione==1) indicatore.risultato.setText(""+data1+" - " + data2);
								                		if (nomeStazione==2) indicatore.risultato.setText(""+data1+" - " + data2);
								                		if (nomeStazione==3) indicatore.risultato.setText(""+data1+" - " + data2);
								                		if (nomeStazione==4) indicatore.risultato.setText(""+data1+" - " + data2);
								                		
								                		if (nomeStazione==5) indicatore.risultato.setText(""+ data1 + " ["+(data1-data2)+" mbar] " +data2);
								                		if (nomeStazione==6) indicatore.risultato.setText(""+ data1 + " ["+(data1-data2)+" mbar] " + data2);
								                		
								                		if (nomeStazione==7) indicatore.risultato.setText(v1 + " [mm] " + v2);
								                		//if (nomeStazione==8) indicatore.risultato.setText(data1 + " [g] " );
								                		//if (nomeStazione==9) indicatore.risultato.setText(data1 + " [g] ");
								                		
								                		
								                		
								                		//if (batteria.getPostazione().equals("1")) log.write("Postazione 1. CODICE BAT: "+batteria.getCodiceBatteria()+" -  Data binary :" + data1_binary );
								                	}catch(Exception b) {
								        				log.write("Reader PLC. Stazione :"+nomeStazione +" - Errore AddBatteriaTop buffer >0 : " + b.toString());
								        			}
								                	
								                }//fine if contains
					               			 		
							                	
							                	numero_batterie_aggiunte +=1;
							              
					               		}//fine if check
					               		
							}catch(Exception j) {
								log.write("Reader PLC. Stazione :"+nomeStazione +" - Errore lettura posizione buffer >0 : " + j.toString());
							}

					                			
				         }//fine data after
				     
				     //fine data after
	        	 
	        	}catch(Exception e) {
	       		log.write("Reader PLC. Stazione :"+nomeStazione +" - Errore in data: " + e.toString());
	            	}
	        	
	        	
	        	
	        }//fine for
	        
	        
	        int ora = LocalDateTime.now().getHour();
	        
	        //log.write("ora:" + ora);
	        
			if ((ora==22)||(ora==6)||(ora==14) && riazzera_contatori) {
				numero_batterie_riprocessate = 0;
				numero_batterie_scartate = 0;
				conteggio = 0;
				riazzera_contatori = false;
				
				try {
					setting.WriteProperties("conteggio_finale", "0","numero_batterie_scartate", "0",nomeStazione);
					setting.WriteProperties("numero_batterie_riprocessate_postazione"+nomeStazione, "0",nomeStazione);
					
				}catch(Exception h) {
					
				}
			}
			if ((ora==23)||(ora==15)||(ora==7)) {
				riazzera_contatori = true;
			}
			
	        
	        if ((numero_di_cicli_senza_batterie>NUMERO_CICLI_START_STOP)) {
	        	//monitor.append("STAZIONE "+nomeStazione+" FERMA !!\n");
	        	indicatore.statoLinea.setText("STOP");
	        	indicatore.statoLinea.setBackground(setting.rosso);
	        	
		        if (!stopped)  {
		        	
		        	//19-05-2021 set startstop non per la stazione 10 (che è solo un controller)
		        	if (((nomeStazione!=Setting.STAZIONE_DI_CONTROLLO_2)))
			        		setStartStop(false,batteria);
			        
			        segnalato = true;
			        	        	
	        		stopped = true;
	        		segnalato = false;
		        }
	        	
	        	//numero_di_cicli_senza_batterie = 0;
	        }else {
	        	indicatore.statoLinea.setText("RUNNING");
	        	indicatore.statoLinea.setBackground(setting.verde);
	        	indicatore.tempostatoLinea.setText("");
	        	
	        	if (!segnalato) {
	        		setStartStop(true,batteria);
	        		segnalato = true;
	        		stopped = false;
	        	}
	        	
	        }//FINE ELSE
	        
	        
	        if (tempo_ultima_batteria.equals("01/01/1970 00:00:00"))
	        	indicatore.tempostatoLinea.setText("-");
	        else
	        	indicatore.tempostatoLinea.setText(""+tempo_ultima_batteria);
	        
	        numero_di_cicli_senza_batterie += 1;
	        
	        prima_lettura = false;
	        //indicatore.setConteggio(""+conteggio);
	   
		    bufferBatterie.setValue(array.totaleBatterie);
	        bufferBatterie.setString(""+array.totaleBatterie+ " BATTERIE IN CODA");
	        	
		   
	    	
	    }//fine leggi Array
	    
	    
	    
	    
	    public void connetti() {
	    	if (setting.getConnectionType().equals("OP"))
			{
				Client.SetConnectionType(S7.OP);
				log.write("TIPO CONNESSIONE: OP\n");
				}
			
	    	if (setting.getConnectionType().equals("PG")) {
				Client.SetConnectionType(S7.PG);
				log.write("TIPO CONNESSIONE: PG\n");
			}
	    	if (setting.getConnectionType().equals("S7_BASIC")) {
				Client.SetConnectionType(S7.S7_BASIC);
				log.write("TIPO CONNESSIONE: S7_BASIC\n");
				}
	    	
	    	log.write("IP:" + setting.getIPPLC()+"\n");
			int Result = Client.ConnectTo(setting.getIPPLC(), Integer.valueOf(setting.getRACK()), Integer.valueOf(setting.getSLOT()));
	    	if (Result==0)
	    	{
	    		
	    		Indicatore.statoplc.setText("OK");
	    		Indicatore.statoplc.setBackground(Setting.verde);
	    		log.write("\nCONNESSIONE RIUSCITA!\n");
	    		//statoplc.setText("CONNESSIONE OK\n");
	            
	    		//monitor.append("PDU negotiated: " + Client.PDULength()+" bytes" );
	    		//System.out.println("Connected to   : " + setting.getIPPLC() + " (Rack=" + Integer.valueOf(RACKtext.getText()) + ", Slot=" + Integer.valueOf(SLOTtext.getText())+ ")");
	           // System.out.println("PDU negotiated : " + Integer.valueOf(SLOTtext.getText()));
	            //statoplc.setText("CONNESSIONE OK!");
	    	}else {
	    		log.write("CONNESSIONE FALLITA! - NUOVO TENTATIVO FRA "+sleep+" SECONDI\n");
	    		indicatore.statoplc.setText("ERRORE");
	    		indicatore.statoplc.setBackground(setting.rosso);
	    		//statoplc.setBackground(new Color(219, 22, 65));
	    	}
	    }//fine connetti
	    
	    
	    
	    static class SortByDate implements Comparator<Batteria> {
	        @Override
	        public int compare(Batteria a, Batteria b) {
	            return b.data.compareTo(a.data);
	        }
	    }//fine sortbydate
	    
	    
	    public static String getMyDate(String myDate, String requiredFormat, String mycurrentFormat) {
	        DateFormat dateFormat = new SimpleDateFormat(requiredFormat);
	        Date date = null;
	        String returnValue = "";
	        try {
	            date = new SimpleDateFormat(mycurrentFormat, Locale.ENGLISH).parse(myDate);
	            returnValue = dateFormat.format(date);
	        } catch (Exception e) {
	            returnValue = myDate;
	        } 
	        return returnValue;
	    }
	    
	    public void setSleep(int s) {
	    	sleep = s;
	    }
	    
	    
	   /* 
	    private void log(String l) {
	    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    	monitor.append(timestamp+" -> Stazione:"+nomeStazione + " - MSG:" + l + "   - modulo readerPLC\n");
	    }
	    */
	    
	    /*
	     * @param batteria è la batteria da controllare
	     * @indice è l'indice della batteria nel buffer del PLC.
	     * @return true se la batteria è da inserire, false altrimenti 
	     * 
	     * il controllo lo faccio solo sulla batteria 0 , ma non la inserisco nel database. Il resto delle batterie nel buffer
	     * non ha più senso dare l'ok al PLC
	     */
	    private boolean check(Batteria batteria, int indice) {
	    	boolean ritorno = false; 
	    	
	    		//monitor.append("INDICE:"+indice+" HO INVIATO IL CODICE BATTERIA: " + batteria.getCodiceBatteria() +" CON POSTAZIONE="+batteria.getPostazione()+"\n");	    					
	    		if (batteria.getCodiceBatteria().length()>12) { //SE IL CODICE è VALIDO
		    		
	    					int tmp = -1;
	    					
	    					//QUESTO CONTROLLO NON SI VERIFICA MAI
	    					if (indice == 0) {	
	    						
	    					}
	    					//indice <>0
	    					else {
	    						//CONTROLLO SU TUTTE LE BATTERIE <> 0
	    					
	    						if (!prima_lettura) {
	    							if (check != null)
		    							try {	
		    								tmp = check.controlloDbBatteria(batteria); //controllo normale con diversi tipi di segnalazione
		    								
		    							}catch(Exception h) {
		    	    						log.write("Reader PLC. Stazione :"+nomeStazione +" - check.control >0: Cod.batteria:"+batteria.getCodiceBatteria()+ " - !primalettura Errore:" + h.toString());
		    		    				}
	    							else log.write("Reader PLC. Stazione :"+nomeStazione +" - check.control >0: Cod.batteria:"+batteria.getCodiceBatteria()+ " - CHECK IS NULL");
	    						}//fine if !primalettura
			    						
	    						else
	    						{
	    							try {
	    								tmp = check.controlloDbBatteriaDoppione(batteria); //cerco solo doppioni. cerco se è presento o meno nel db
	    								//per le bilancie devo verificare differenza peso --- 27/10/2021
	    									    									
	    									//log.write("Reader PLC. Postazione 2. Ritorno = "+tmp);
	    								
	    								//log.write("Reader PLC. Valore controllo  
	    								
	    							}catch(Exception h) {
	    	    						log.write("Reader PLC. Stazione :"+nomeStazione +" - check.control >0: Cod.batteria:"+batteria.getCodiceBatteria()+ " - primalettura Errore:" + h.toString());
	    		    				}
	    						}//fine else
	    						
	    							
	    						try {		
		    						
		    						if (configuratore.getListaAtomoConfigurazione()[nomeStazione-1].scartoabilitato>0)	{
		    							//codice di ritorno dal controllo, codice batteria, postazione
		    							//if (Integer.parseInt(batteria.getPostazione())==2)		    								
	    								
		    							return segnala(tmp,batteria.getCodiceBatteria(),batteria.getPostazione());	//se la batteri non supera il testo, segnalo con
		    							    						
		    						}else
		    						{
		    							//SCARTO DISABILITATO
		    							return segnala(9,batteria.getCodiceBatteria(),batteria.getPostazione());
		    							
		    						}
		    					}catch(Exception h) {
		    					//in realtà non riesco a leggere la configurazione dal PLC. problema DBAREA 
		    						return segnala(9,batteria.getCodiceBatteria(),batteria.getPostazione());
		    					}
    		    				
	    					
	    					}//fine else prima lettura
	    					
	    					
	    					
	    					//ritorno = false;
	    					
	    		}//CODICE CORRETTO
				
				
			
	    	
	    	//con falso non inserisco batterie, con true si
	    	return false;
	    }//fine check
	    
	    
	
	   public boolean segnala(int risposta, String codice_batt, String postazione) {
		   
		   boolean ris = false;

		   switch(risposta) {
		   case -1:
			   //indicatore.batteria.setBackground(setting.bianco);
			   indicatore.conteggio.setBackground(setting.bianco);
			   indicatore.stato.setBackground(setting.arancio); 
			   indicatore.setStato("BYPASS");
			   //conteggio +=1;
			   indicatore.setConteggio(""+conteggio);
			   ris = true;
			   break;
		   case 0:
			   	//indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(setting.bianco);
			   	indicatore.stato.setBackground(setting.verde);
				indicatore.setStato("OK");
				//conteggio +=1;
				indicatore.setConteggio(""+conteggio);
				stato_batteria_postazione_bilancia2 = "1";
				
				//if (postazione.equals("2"))		    								
					//log.write("Reader PLC. Postazione 2 da segnala. Ritorno = "+risposta);
				
				ris = true;
		   break;
		   case 1:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(setting.bianco);
			    indicatore.stato.setBackground(setting.rosso); 
			    indicatore.setStato("RIPROCESS.");
			    numero_batterie_riprocessate +=1;
				//conteggio +=1;
				indicatore.setConteggio(""+conteggio);
				indicatore.riprocessato.setText(""+numero_batterie_riprocessate);
				try {
					setting.WriteProperties("numero_batterie_riprocessate_postazione"+nomeStazione, ""+numero_batterie_riprocessate,nomeStazione);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ris = true;
		   break;
		   // eventuali altri case
		   case 2:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(setting.bianco);
			    indicatore.stato.setBackground(setting.arancio);
			    indicatore.setStato("KO PRECE.");
			    log.write("BATTERIA KO nella stazione precedete. POSTAZIONE ATTUALE N."+postazione+"    CODICE BATTERIA:" + codice_batt);
				//conteggio +=1;
				//numero_batterie_scartate +=1;
				indicatore.setConteggio(""+conteggio);
				ris = true;
		  break;
				 
		   case 3:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(setting.bianco);
			    indicatore.stato.setBackground(setting.rosso); 
			    indicatore.setStato("TIMEOUT");
			    log.write("TIMEOUT DEL DATABASE.  --->  postazione n."+postazione+"  , BATTERIA:"+ codice_batt);
				//conteggio +=1;
				indicatore.setConteggio(""+conteggio);
				ris = true;
		   break;
		   case 4:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(setting.bianco);
			    indicatore.stato.setBackground(setting.rosso); 
			    indicatore.setStato("SALTO P.");
			    log.write("SALTO POSTAZIONE N."+postazione+"   CODICE BATTERIA:" + codice_batt);
			    
				//conteggio +=1;
				indicatore.setConteggio(""+conteggio);
				ris = true;
		   break;
		   case 5:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(setting.bianco);
			    indicatore.stato.setBackground(setting.rosso); 
			    indicatore.setStato("NO LAN");
			    //log.write("NEL CHECK HO RISCONTRATO UN VALORE DI CONTROL. NON DOVREI ESSERE QUI. - POSTAZIONE N."+postazione+"   CODICE BATTERIA:" + codice_batt);
				//conteggio +=1;
				indicatore.setConteggio(""+conteggio);
				ris = true;
		   break;
		   case 6:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(setting.bianco);
			    indicatore.stato.setBackground(setting.arancio); 
			    indicatore.setStato("BUFFER");
				//conteggio +=1;
				indicatore.setConteggio(""+conteggio);
				ris = false;
		   break;
		   case 7:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(setting.bianco);
			    indicatore.stato.setBackground(setting.rosso);
			    indicatore.setStato("ESITO KO");
			    log.write("BATTERIA KO. POSTAZIONE N."+postazione+"    CODICE BATTERIA:" + codice_batt);
				//conteggio +=1;
				numero_batterie_scartate +=1;
				indicatore.setConteggio(""+conteggio);
				ris = true;
		  break;
		   case 8:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(setting.bianco);
			    indicatore.stato.setBackground(setting.rosso);
			    indicatore.setStato("ESITO KO");
			    //log.write("BATTERIA KO. POSTAZIONE N."+postazione+"    CODICE BATTERIA:" + codice_batt);
				//conteggio +=1;
				//numero_batterie_scartate +=1;
				//indicatore.setConteggio(""+conteggio);
				ris = false;
		  break;
		   case 9:
			   	//OK CON BYPASS
				indicatore.conteggio.setBackground(setting.bianco);
			   	indicatore.stato.setBackground(setting.verde);
				indicatore.setStato("OK BYPASS");
				//conteggio +=1;
				indicatore.setConteggio(""+conteggio);
				ris = true;
		   break;
		   case 10:
				indicatore.conteggio.setBackground(setting.bianco);
			   	indicatore.stato.setBackground(setting.rosso);
				indicatore.setStato("ERR DBCONF");
				//conteggio +=1;
				indicatore.setConteggio(""+conteggio);
				ris = true;
		   break;
		   case 11: //KO POSTAZIONE 9 BILANCIA
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(setting.bianco);
			    indicatore.stato.setBackground(setting.rosso);
			    indicatore.setStato("ESITO KO");
			    log.write("BATTERIA KO. POSTAZIONE N."+postazione+"    CODICE BATTERIA:" + codice_batt);
				//conteggio +=1;
				numero_batterie_scartate +=1;
				indicatore.setConteggio(""+conteggio);
				stato_batteria_postazione_bilancia2 = "0";
				ris = true;
		  break;
		   
		   
		   
		   
		   default:
			   //indicatore.batteria.setBackground(setting.bianco);
			   indicatore.conteggio.setBackground(setting.bianco);
			   indicatore.stato.setBackground(setting.rosso); 
			   indicatore.setStato("NET :" + risposta);
			   log.write("NEL CHECK HO RISCONTRATO UN VALORE DI DEFAULT. NON DOVREI ESSERE QUI");
			   //conteggio +=1;
			   indicatore.setConteggio(""+conteggio);
			   ris = true;
		   }
		   
		   return ris;
	   }//fine segnala
	   
	  
	   
	   //per le postazioni 7 e 10
	   //questo metodo è chiamato solo per finalcontroller e controller
	   public void scriviRisultatoScartoPostazione(Batteria batteria) {
		   
		   //log.write("readerplc -> prima di iswaste. POSTAZIONE "+batteria.getPostazione()+". CONTROLLO PER SCARTO AVVIATO COD: BAT: " + batteria.getCodiceBatteria());
		   
		   	int ritorno=-5;
				try {
					ritorno = check.isWaste(batteria);
				} catch (Exception e) {
					log.write("Reader PLC Line1043. Stazione :"+nomeStazione +":  " + e.toString());
				}
		   
		   	//log.write("readerplc line1049 -> dopo di iswaste. RITORNO = "+ritorno);
		   	//if (Integer.parseInt(batteria.getPostazione())==2)
		   	//log.write("POSTAZIONE "+batteria.getPostazione()+". RITORNO IL VALORE = " + ritorno + " con batteria cod:" + batteria.getCodiceBatteria());
		   
		   	if (ritorno > 0) {
		   		
	   			      //-----------------------------------------------------------------------------------------------
			   			if (isFinalController()) {
			   				//log.write("readerplc -> sono nella postazione di controllo ");
			   				
			   				
			   				String nome_postazione_errore = "NON CONOSCIUTA";
			   				
			   				//gli errori <10 sono ko
			   				if (ritorno<Integer.parseInt(setting.getNumeroStazioniAttive())) {
			   					
				   					numero_batterie_scartate +=1;
					   				indicatore.risultato.setBackground(setting.rosso);
					   				
				   					if (ritorno == 1) nome_postazione_errore = "CON. CORTI 1";
					   				if (ritorno == 2) nome_postazione_errore = "PUNTATRICE 1";
					   				if (ritorno == 3) nome_postazione_errore = "PUNTATRICE 2";
					   				if (ritorno == 4) nome_postazione_errore = "CON. CORTI 2";
					   				if (ritorno == 5) nome_postazione_errore = "P. TENUTA 1";
					   				if (ritorno == 6) nome_postazione_errore = "P. TENUTA 2";
					   				if (ritorno == 7) nome_postazione_errore = "ALT. POLARI";
					   				if (ritorno == 8) nome_postazione_errore = "BILANCIA 1";
					   				if (ritorno == 9) nome_postazione_errore = "BILANCIA 2";
				   					
				   					SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				   					Date date = new Date(System.currentTimeMillis());
				   					//System.out.println(formatter.format(date));
				   					viewer.setMessage("----------------------------");
					   				viewer.setMessage("BATTERIA COD. " + batteria.getCodiceBatteria());
					   				viewer.setMessage("SEGNALATA PER KO P. " + nome_postazione_errore );
					   				viewer.setMessage("----------------------------\n");
					   				
					   				areaErrore.setBackground(setting.rosso);
					   				
					   				
					   				areaErrore.setText("BATTERIA " + batteria.getCodiceBatteria() + " - KO POSTAZIONE " + nome_postazione_errore );
					   				Setting.getCodiceBatteriaScartata().setText(batteria.getCodiceBatteria());
					   				Setting.getCodiceBatteriaScartata().setBackground(setting.rosso);
					   				
					   				
					   				
					   			}//FINE IF ritorno <10
			   				
			   				
				   			//è maggiore del numerodelle postazioni, ma comunque inferiore a postazioni *2 cioè sono nel campo del salta postazione	
				   			if (((ritorno>=Integer.parseInt(setting.getNumeroStazioniAttive()))&& (ritorno<(Integer.parseInt(setting.getNumeroStazioniAttive()) * 2 ))) || (ritorno==100) ) {

				   					//NON SEGNO LE BATTERIE SALTO POSTAZIONE
				   					//numero_batterie_scartate +=1;
				   					indicatore.risultato.setBackground(setting.arancio);
				   				
				   					SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					   				Date date = new Date(System.currentTimeMillis());
					   				
					   				int rit = ritorno - Integer.parseInt(setting.getNumeroStazioniAttive());
					   				
					   				if (rit ==1) nome_postazione_errore = "CON. CORTI 1";
					   				if (rit ==2) nome_postazione_errore = "PUNTATRICE 1";
					   				if (rit ==3) nome_postazione_errore = "PUNTATRICE 2";
					   				if (rit ==4) nome_postazione_errore = "CON. CORTI 2";
					   				if (rit ==5) nome_postazione_errore = "P. TENUTA 1";
					   				if (rit ==6) nome_postazione_errore = "P. TENUTA 2";
					   				if (rit ==7) nome_postazione_errore = "ALT. POLARI";
					   				if (rit ==8) nome_postazione_errore = "BILANCIA 1";
					   				if (rit ==9) nome_postazione_errore = "BILANCIA 2";
					   				
					   				//log.write("readerplc 1120-> sono in nritorno fra 10 e 20");
					   				
					   				//MODIFICA SOLO PER LA POSTAZIONE 4. DEVONO ESSERE BLOCCATE ANCGHE SE HANNO AVUTO ESITO OK DOPO RIPROCESSAMENTO
					   				//MODIFICA RICHIESTA DA CAPPAI 14/06/2021
					   				if (ritorno==100) {
					   					nome_postazione_errore = "CON. CORTI 2 - ULTERIORE VERIFICA";
					   					numero_batterie_scartate +=1;
					   				}
					   				
					   				//log.write("readerplc -> sono nuovamente nella postazione di controllo centrale");
					   				
					   				areaErrore.setBackground(setting.arancio);
					   				setting.getCodiceBatteriaScartata().setText(batteria.getCodiceBatteria());
					   				setting.getCodiceBatteriaScartata().setBackground(setting.arancio);
					   				
					   				if (ritorno!=100)
					   					areaErrore.setText("COD." + batteria.getCodiceBatteria() + " - SALTO POSTAZIONE " + nome_postazione_errore);
					   				else
					   					areaErrore.setText("COD." + batteria.getCodiceBatteria() + " - POSTAZIONE " + nome_postazione_errore);
					   				
					   				//System.out.println(formatter.format(date));
					   				viewer.setMessage("-------------------------------");
						   			viewer.setMessage("BATTERIA COD. " + batteria.getCodiceBatteria());
						   			
						   			if (rit<Integer.parseInt(setting.getNumeroStazioniAttive()))
						   				viewer.setMessage("SEGNALATA PER SALTO  " + nome_postazione_errore);
						   			else {
						   				viewer.setMessage("SEGNALATA PER " + nome_postazione_errore);
						   			}
						   				
						   				viewer.setMessage("----------------------------\n");
						   			
						   				//log.write("readerplc 1152-> sono in nritorno fra 10 e 20");
						   			//log.write("readerplc -> sono nuovamente nella postazione di controllo ESCO");
						   			
				   				
				   				}//FINE IF ritorno tra 10 e 20
				   			
				   		
			   			
			   			}//fine isfinalcontroller
			   			//non e' final controller
			   			
			   			if ((ritorno<Integer.parseInt(setting.getNumeroStazioniAttive()))){
			   				indicatore.risultato.setBackground(setting.rosso);
			   				indicatore.risultato.setText("KO P. " + ritorno);
			   			}
			   			
			   			if ((ritorno>=Integer.parseInt(setting.getNumeroStazioniAttive()))&& (ritorno<(Integer.parseInt(setting.getNumeroStazioniAttive())*2)) ){
			   				indicatore.risultato.setBackground(setting.rosso);
			   				indicatore.risultato.setText("SALTO P. " + (ritorno-Integer.parseInt(setting.getNumeroStazioniAttive())));
			   			}
			   			
			   			
			   			
			   			//-----------------------------------------------------------------------------------------
			   			
			   				
			   } //fine controllo >0
			   				
			   			
	   		
	   		if ((ritorno == 0) && isFinalController() ){
	   			
	   			//log.write("readerplc -> RITORNO 0 E POSTAZIONE DI CONTROLLO ");
	   			
	   			indicatore.risultato.setBackground(setting.verde);
	   			indicatore.risultato.setText("BATTERIA CONFORME");
	   			areaErrore.setBackground(setting.verde);
	   			areaErrore.setText("BATTERIA " + batteria.getCodiceBatteria() + " - ESITO OK ");
	   			setting.getCodiceBatteriaScartata().setText(batteria.getCodiceBatteria());
	   			setting.getCodiceBatteriaScartata().setBackground(setting.verde);
	   			
	   		}
	   		
	   		if (ritorno == -1) {
	   			indicatore.risultato.setBackground(setting.rosso);
	   			indicatore.risultato.setText("ERR. LAN");
	   			
	   			//NELLA POSTAZIONE 10, IN CASO DI MANCANZA LAN, SCRIVO
	   			if (Integer.parseInt(batteria.getPostazione()) == Setting.STAZIONE_DI_CONTROLLO_2) {
	   				areaErrore.setBackground(setting.rosso);
	   				areaErrore.setText("ERRORE RETE - DISABILITARE CONTROLLO");
	   			}
	   				
	   		}
	   		
	   		if (ritorno == -2) {
	   			
	   			viewer.setMessage("----------------------------");
   				viewer.setMessage("BATTERIA COD. " + batteria.getCodiceBatteria()+ " POSTAZIONE:" + nomeStazione);
   				viewer.setMessage("SEGNALATA PER NON PRESENZA DEL RECORD NEL DB. " + ritorno );
   				viewer.setMessage("----------------------------\n");
	   			
	   			indicatore.risultato.setBackground(setting.rosso);
	   			indicatore.risultato.setText("NON TROVATA");
	   			
	   		    //NELLA POSTAZIONE 10, IN CASO DI MANCANZA LAN, SCRIVO
	   			if (isFinalController()) {
	   				areaErrore.setBackground(setting.arancio);
	   				areaErrore.setText("BATTERIA " + batteria.getCodiceBatteria() + " - NON TROVATA ");
	   			}
	   		}
	   		
	   		
	   		if (isFinalController()) {
	   			
	   			//log.write("readerplc -> sono nuovamente nella postazione di controllo VERSO FINE");
	   			
	   			conteggio +=1;
	   			indicatore.conteggio.setBackground(setting.bianco);
	   			indicatore.stato.setBackground(setting.verde);
	   			indicatore.setStato("" + ritorno);
	   			
	   			indicatore.setTempo(batteria.gettimestamp());
				indicatore.setBatteria(batteria.getCodiceBatteria());
				
				indicatore.setConteggio(""+conteggio);
				indicatore.scarto.setText(""+numero_batterie_scartate);
				//salvo permanentemente questi valori per tenerli in memoria in caso di chiusura
				
				try {
					setting.WriteProperties("conteggio_finale", ""+conteggio,"numero_batterie_scartate", ""+numero_batterie_scartate,nomeStazione);
					
				}catch(Exception kk) {
					log.write("readerPLC line1235 -> errore scrivirisultato . err: "+ kk.toString());
				}
				
				
	   		}//fineif postazione 10
		   	
		   	
		   	
	   		
	   	   //SCRIVO RISULTATO DEL CONTROLLO SUL DB	
	   	   //CON VALORE INFERIORE A 10 LA BATTERIA VIENE BLOCCATA DAL PLC (DEFINITO DAL PLC)
		   SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		   Date date = new Date(System.currentTimeMillis());
		   //System.out.println(formatter.format(date));
		   
		    //VERIFICA PER LA POSTAZIONE 4 CHE RICHIEDE ULTERIORE CONTROLLO
		   	if (ritorno==100) ritorno = 4;
		   		  
			int indirizzo_start = 0;
			S7.SetWordAt(Buffer, indirizzo_start , ritorno);
	   		indirizzo_start +=2;
	   			   			   		   
	   		S7.SetDateAt(Buffer, indirizzo_start , date);
	   		   
	   		int db = -1;
	   		if (isController()) db = Setting.DB_POSTAZIONE_CONTROLLO1;
	   		if (isFinalController()) db = Setting.DB_POSTAZIONE_CONTROLLO2;
	   		
	   		indirizzo_start +=8;
	   		
	   		String code = batteria.getCodiceBatteria();
	   		//byte[] buf = new byte[26];//code.getBytes();
	   		log.write("readerplc line1277-> mi accingo a scrivere il risultato nel db. Batteria:" + code);
	   		//code = "123456789101";
	   		
	   		for (int i = 0; i < code.length(); i++) {
	        	
	            if (code.charAt(i) <= ' ') {
	               break;
	            }else {
	              
	           // S7.SetShortAt(Buffer, indirizzo_start, Integer.parseInt(String.valueOf(code.charAt(i))));
	            //S7.SetShortAt(Buffer, indirizzo_start, (String.valueOf(code.charAt(i))));
	            S7.SetcCharAt(Buffer, indirizzo_start, Short.parseShort(String.valueOf(code.charAt(i))));
	            //S7.SetcCharAt(Buffer, Pos, Value);
	            	indirizzo_start +=2;	
	            }//fine else
	        }////fine for
	   			
	   		try {
	   			DBWrite(db); //inizio, dimensioni
	   		}catch(Exception h) {
	   			log.write("readerplc -> Errore scrittura PLC per memorizzare scarti ultima postazione. DB="+db+" : ERR=" + h.toString());
	   		}
	   		
	  	   		
	   }//fine public scartoPostazione
	  
	   
	   
	   
	   //per le postazioni 7 e 10
	   public void scriviRisultatoScartoPostazione_bk(Batteria batteria) {
		   
		   //log.write("readerplc -> prima di iswaste. POSTAZIONE "+batteria.getPostazione()+". CONTROLLO PER SCARTO AVVIATO COD: BAT: " + batteria.getCodiceBatteria());
		   
		   	int ritorno = check.isWaste(batteria);
		   
		   	//log.write("readerplc -> dopo di iswaste. RITORNO = "+ritorno);
		   	//if (Integer.parseInt(batteria.getPostazione())==2)
		   		//log.write("POSTAZIONE "+batteria.getPostazione()+". RITORNO IL VALORE = " + ritorno + " con batteria cod:" + batteria.getCodiceBatteria());
		   
		   	if (ritorno > 0) {
		   		
		   		
	   			
			   			if (isFinalController()) {
			   				//log.write("readerplc -> sono nella postazione di controllo ");
			   				numero_batterie_scartate +=1;
			   				indicatore.risultato.setBackground(setting.rosso);
			   				//log.write("readerplc -> ... e ne sono uscito ");
			   				
			   			}
			   			
			   			String nome_postazione_errore = "NON CONOSCIUTA";
			   			
			   			if (ritorno<Integer.parseInt(batteria.getPostazione())) {
			   				if (isFinalController()) {
			   					
			   					SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			   					Date date = new Date(System.currentTimeMillis());
			   					//System.out.println(formatter.format(date));
			   					viewer.setMessage("----------------------------");
				   				viewer.setMessage("BATTERIA COD. " + batteria.getCodiceBatteria());
				   				viewer.setMessage("SEGNALATA PER KO P. " + ritorno );
				   				viewer.setMessage("----------------------------\n");
				   				
				   				areaErrore.setBackground(setting.rosso);
				   				
				   				
				   				if (ritorno ==1) nome_postazione_errore = "CON. CORTI 1";
				   				if (ritorno ==2) nome_postazione_errore = "PUNTATRICE 1";
				   				if (ritorno ==3) nome_postazione_errore = "PUNTATRICE 2";
				   				if (ritorno ==4) nome_postazione_errore = "CON. CORTI 2";
				   				if (ritorno ==5) nome_postazione_errore = "P. TENUTA 1";
				   				if (ritorno ==6) nome_postazione_errore = "P. TENUTA 2";
				   				if (ritorno ==7) nome_postazione_errore = "ALT. POLARI";
				   				if (ritorno ==8) nome_postazione_errore = "BILANCIA 1";
				   				if (ritorno ==9) nome_postazione_errore = "BILANCIA 2";
				   				
				   				areaErrore.setText("BATTERIA " + batteria.getCodiceBatteria() + " - KO POSTAZIONE " + nome_postazione_errore );
				   				Setting.getCodiceBatteriaScartata().setText(batteria.getCodiceBatteria());
				   				Setting.getCodiceBatteriaScartata().setBackground(setting.rosso);
				   			}//FINE IF
			   				
			   				
			   				indicatore.risultato.setBackground(setting.rosso);
			   				indicatore.risultato.setText("KO P. " + ritorno);
			   			}
			   				
			   			//RIPROCESSATE
			   			if ((ritorno + 10) > Integer.parseInt(batteria.getPostazione())) {
			   				//POSTAZIONE 10
			   				
			   				if (isFinalController()) {
			   					
			   					//log.write("readerplc -> sono nuovamente nella postazione di controllo ");
			   					
			   					SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				   				Date date = new Date(System.currentTimeMillis());
				   				
				   				int rit = ritorno - 10;
				   				
				   				if (rit ==1) nome_postazione_errore = "CON. CORTI 1";
				   				if (rit ==2) nome_postazione_errore = "PUNTATRICE 1";
				   				if (rit ==3) nome_postazione_errore = "PUNTATRICE 2";
				   				if (rit ==4) nome_postazione_errore = "CON. CORTI 2";
				   				if (rit ==5) nome_postazione_errore = "P. TENUTA 1";
				   				if (rit ==6) nome_postazione_errore = "P. TENUTA 2";
				   				if (rit ==7) nome_postazione_errore = "ALT. POLARI";
				   				if (rit ==8) nome_postazione_errore = "BILANCIA 1";
				   				if (rit ==9) nome_postazione_errore = "BILANCIA 2";
				   				
				   				//MODIFICA SOLO PER LA POSTAZIONE 4. DEVONO ESSERE BLOCCATE ANCGHE SE HANNO AVUTO ESITO OK DOPO RIPROCESSAMENTO
				   				//MODIFICA RICHIESTA DA CAPPAI 14/06/2021
				   				if (rit==14) nome_postazione_errore = "CON. CORTI 2 - ULTERIORE VERIFICA";
				   				
				   				//log.write("readerplc -> sono nuovamente nella postazione di controllo centrale");
				   				
				   				areaErrore.setBackground(setting.arancio);
				   				setting.getCodiceBatteriaScartata().setText(batteria.getCodiceBatteria());
				   				setting.getCodiceBatteriaScartata().setBackground(setting.arancio);
				   				
				   				if (rit!=14)
				   					areaErrore.setText("COD." + batteria.getCodiceBatteria() + " - SALTO POSTAZIONE " + nome_postazione_errore);
				   				else
				   					areaErrore.setText("COD." + batteria.getCodiceBatteria() + " - POSTAZIONE " + nome_postazione_errore);
				   				
				   				//System.out.println(formatter.format(date));
				   				viewer.setMessage("-------------------------------");
					   			viewer.setMessage("BATTERIA COD. " + batteria.getCodiceBatteria());
					   			if (rit<14)
					   				viewer.setMessage("SEGNALATA PER SALTO  " + nome_postazione_errore);
					   			else
					   				viewer.setMessage("SEGNALATA PER " + nome_postazione_errore);
					   			viewer.setMessage("----------------------------\n");
					   			
					   			log.write("readerplc -> sono nuovamente nella postazione di controllo ESCO");
					   			
				   			}//FINE POSTAZIONE 10
			   				
			   				indicatore.risultato.setBackground(setting.rosso);
			   				indicatore.risultato.setText("SALTO P. " + (ritorno-10));
			   			}//FINE IF RIPROCESSATE
	   				
	   			
		   	} //fine if ritorno > 0
	   		
	   		if ((ritorno == 0) && isFinalController() ){
	   			
	   			//log.write("readerplc -> RITORNO 0 E POSTAZIONE DI CONTROLLO ");
	   			
	   			indicatore.risultato.setBackground(setting.verde);
	   			indicatore.risultato.setText("BATTERIA CONFORME");
	   			areaErrore.setBackground(setting.verde);
	   			areaErrore.setText("BATTERIA " + batteria.getCodiceBatteria() + " - ESITO OK ");
	   			setting.getCodiceBatteriaScartata().setText(batteria.getCodiceBatteria());
	   			setting.getCodiceBatteriaScartata().setBackground(setting.verde);
	   			
	   		}
	   		
	   		if (ritorno == -1) {
	   			indicatore.risultato.setBackground(setting.rosso);
	   			indicatore.risultato.setText("ERR. CONTROLLO");
	   			
	   			//NELLA POSTAZIONE 10, IN CASO DI MANCANZA LAN, SCRIVO
	   			if (Integer.parseInt(batteria.getPostazione()) == Setting.STAZIONE_DI_CONTROLLO_2) {
	   				areaErrore.setBackground(setting.rosso);
	   				areaErrore.setText("ERRORE LAN - DISABILITARE CONTROLLO");
	   			}
	   				
	   		}
	   		
	   		if (ritorno == -2) {
	   			
	   			viewer.setMessage("----------------------------");
   				viewer.setMessage("BATTERIA COD. " + batteria.getCodiceBatteria()+ " POSTAZIONE:" + nomeStazione);
   				viewer.setMessage("SEGNALATA PER NON PRESENZA DEL RECORD NEL DB. " + ritorno );
   				viewer.setMessage("----------------------------\n");
	   			
	   			indicatore.risultato.setBackground(setting.rosso);
	   			indicatore.risultato.setText("NON TROVATA");
	   			
	   		//NELLA POSTAZIONE 10, IN CASO DI MANCANZA LAN, SCRIVO
	   			if (isFinalController()) {
	   				areaErrore.setBackground(setting.arancio);
	   				areaErrore.setText("BATTERIA " + batteria.getCodiceBatteria() + " - NON TROVATA ");
	   			}
	   		}
	   		
	   		
	   		if (isFinalController()) {
	   			
	   			//log.write("readerplc -> sono nuovamente nella postazione di controllo VERSO FINE");
	   			
	   			conteggio +=1;
	   			indicatore.conteggio.setBackground(setting.bianco);
	   			indicatore.stato.setBackground(setting.verde);
	   			indicatore.setStato("" + ritorno);
	   			
	   			indicatore.setTempo(batteria.gettimestamp());
				indicatore.setBatteria(batteria.getCodiceBatteria());
				
				indicatore.setConteggio(""+conteggio);
				indicatore.scarto.setText(""+numero_batterie_scartate);
				//salvo permanentemente questi valori per tenerli in memoria in caso di chiusura
				
				try {
					setting.WriteProperties("conteggio_finale", ""+conteggio,"numero_batterie_scartate", ""+numero_batterie_scartate,nomeStazione);
					//setting.WriteProperties("numero_batterie_scartate", ""+numero_batterie_scartate);
				}catch(Exception kk) {
					log.write("readerPLC line1475 -> errore scrivirisultato . err: "+ kk.toString());
				}
				
				
	   		}//fineif postazione 10
		   	
		   	
		   	
	   		
	   	   //SCRIVO RISULTATO DEL CONTROLLO SUL DB	
		   SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		   Date date = new Date(System.currentTimeMillis());
		   //System.out.println(formatter.format(date));
		   
		    //VERIFICA PER LA POSTAZIONE 4 CHE RICHIEDE ULTERIORE CONTROLLO
		   	if (ritorno==24) ritorno = 4;
		   		  
			int indirizzo_start = 0;
			S7.SetWordAt(Buffer, indirizzo_start , ritorno);
	   		indirizzo_start +=2;
	   			   			   		   
	   		S7.SetDateAt(Buffer, indirizzo_start , date);
	   		   
	   		int db = -1;
	   		if (isController()) db = Setting.DB_POSTAZIONE_CONTROLLO1;
	   		if (isFinalController()) db = Setting.DB_POSTAZIONE_CONTROLLO2;
	   		
	   		indirizzo_start +=8;
	   		
	   		String code = batteria.getCodiceBatteria();
	   		//byte[] buf = new byte[26];//code.getBytes();
	   		
	   		//code = "123456789101";
	   		
	   		for (int i = 0; i < code.length(); i++) {
	        	
	            if (code.charAt(i) <= ' ') {
	               break;
	            }else {
	              
	            S7.SetShortAt(Buffer, indirizzo_start, Integer.parseInt(String.valueOf(code.charAt(i))));
	            //S7.SetcCharAt(Buffer, indirizzo_start, Short.parseShort(String.valueOf(code.charAt(i))));
	               
	            	indirizzo_start +=2;	
	            }//fine else
	        }////fine for
	   			
	   		try {
	   			DBWrite(db); //inizio, dimensioni
	   		}catch(Exception h) {
	   			log.write("readerplc -> Errore scrittura PLC per memorizzare scarti ultima postazione. DB="+db+" : ERR=" + h.toString());
	   		}
	   		
	  	   		
	   }//fine public scartoPostazione
	   
	   
	   //ricevo in ingresso lo start (true) o stop (false)
	   public void setStartStop(boolean start_stop, Batteria batteria) {
			try {
				int ritorno = check.setMicroStop(start_stop,batteria);
				//log.write("readerPLC - AVVIO setStartStop. Ristorno =" + ritorno);
			} catch (IOException e) {
				log.write("readerPLC - ERRORE setStartStop :" + e.toString());
				e.printStackTrace();
			}
	   }//fine start
	   

}//fine classe

