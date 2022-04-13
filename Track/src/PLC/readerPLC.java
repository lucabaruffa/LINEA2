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
import DB.DBCommand;
import Moka7.IntByRef;
import Moka7.S7;
import Moka7.S7Client;
import View.Scarti;
import linea.ArrayBatteriePostazione;
import linea.ArrayBatteriePostazioneScarto;
import linea.ArrayBatterieScarto;
import linea.Batteria;
import linea.ConfiguratoreLinea;
import linea.Indicatore;
import linea.LoggerFile;
import linea.Setting;

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
    //private int conteggio = 0;    
    private Setting setting;
    private boolean prima_lettura = true;  //avvio la prima lettura del programma. Ho riavviato il programma
    public GregorianCalendar data = new GregorianCalendar(); 
	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	protected ArrayBatteriePostazione array;
	protected ArrayBatteriePostazioneScarto arrayScarto;
	private CheckControl check;
	private int DIMENSIONI_BUFFER_PLC = Setting.DIMENSIONI_BUFFER_PLC; // buffer PLC
	private int numero_di_cicli_senza_batterie = 0;
	
	private boolean segnalato = false, stopped = false; //setStartStop
	
	private GregorianCalendar dax = new GregorianCalendar(); 
	private ConfiguratoreLinea configuratore;
	
	private String timestamp_ultima_batteria = "01/01/2020 00:00:00";
	private String codice_batteria_old="OLD";
	
	private int numero_batterie_riprocessate = 0;
	//private int numero_batterie_scartate = 0;
	
	private static LoggerFile log = new LoggerFile();
	
	private Scarti viewer = new Scarti();
	
	private JTextField areaErrore;
	private String stato_batteria_postazione_bilancia2 = "1";
	
	private String old_stato_batteria="-1";
	
	
	
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
	
	
	
	public readerPLC(int db, JProgressBar b, int nome, Indicatore ind, ArrayBatteriePostazione arrayBat, ArrayBatteriePostazioneScarto arrayBatScarto) {
		
		//monitor =g;
		nomeStazione = nome;
		DB = db;
		indicatore = ind;
		array = arrayBat;
		arrayScarto = arrayBatScarto;
		
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
		bufferBatterie = b;
			
		Rack = Integer.parseInt(setting.getRACK());
		Slot = Integer.parseInt(setting.getSLOT());
		
		bufferBatterie.setString("0 BATTERIE IN CODA"); 
		
		try {
			configuratore = new ConfiguratoreLinea();
		}catch(Exception h) {
			
			log.write("Errore reader PLC, caricamento configurazione: "+h.toString());
		}
		
		
		
		
		//tento di caricare le informazione dell'ultima stazione
		try {
			if (nomeStazione==Setting.STAZIONE_DI_CONTROLLO_2){
				indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				indicatore.scarto.setText(""+Setting.totale_batterie_scartate[nomeStazione-1]);
				
			}
			
			if (nomeStazione == 1) {
				Setting.PEZZI = Integer.parseInt(setting.ReadProperties("conteggio_finale"));
				Setting.SCARTI = Integer.parseInt(setting.ReadProperties("conteggio_scarti"));
				ArrayBatteriePostazioneScarto.totaleBatterie = Setting.SCARTI;
				log.write("ReaderPLC. Avvio read scarti :" + ArrayBatteriePostazioneScarto.totaleBatterie);
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
        	//log.write("DB "+DB+" - DIMENSIONI DB "+DataToMove+" bytes\n");
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
	            //System.out.print("PLC Status : ");
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
	        	
	        	//CREO LA BATTERIA
				try {
					batteria = new Batteria(i,cod_batteria,timestamp,""+stato,data1,data2);
					batteria.setPostazione(""+nomeStazione);
					
				} catch (Exception e) {
					log.write("ERRORE CREAZIONE BATTERIA: "+ e.toString());
					e.printStackTrace();
				}
        		
	        	
	        	try {
	        		
	        		//controllo batteria in ingresso. POSIZIONE 0 DELLA PILA
	        		if ((i==0) && (!codice_batteria_old.equals(cod_batteria) && cod_batteria != null)) {
	        				        			
	        			 		//se il codice letto ha dimensioni maggiori di 20 (codice teoricamente valido)
			        			 if (cod_batteria.length()>20) {
				        				 try { 
				        				 	codice_batteria_old = cod_batteria;		
				    	        			indicatore.setBatteriaZero(cod_batteria);
				    	        			numero_di_cicli_senza_batterie = 0;
				    	        			
				    	        							    	        			
				    	        			//--------- 29-03-2022 -------------------
				    	        			if (!Setting.statiPLC[nomeStazione].RUN) {
				    	        			//-------- 15/02/2022 -------------------- SE è DISABILITATA LA SEGNO COME BYPASS
				    	        						    	        				
				    	        				batteria.setStato("-2");  //LA SEGNO COME BYPASS 
				    							segnala(9,batteria.getCodiceBatteria(),batteria.getPostazione());	//se la batteria non supera il testo, segnalo con  
				    							 if (!array.contains(batteria)) {		
									                	array.addBatteriaTop(batteria);
									             }
				    							 
				    							 indicatore.setTempo(timestamp);
				    							 Setting.totale_batterie_lavorate[nomeStazione-1] += 1; 
				    							 indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				    							
				    						}
				    	        			//--------------------------------
				    	        			
				    	        			if (isFinalController()) { //postazioni di controllo
				    	        				//log.write("Reader PLc -> postazione 10 arrivo batteria :" + cod_batteria);
								    	        				try {
								    	        					tempo_ultima_batteria = timestamp;
								    	        													    	        					
								    	        					scriviRisultatoScartoPostazione(batteria);
								    	        					
								    	        					//-------------------- 07-02-2022-------------------
														             if (!array.contains(batteria)) {		
														                	array.addBatteriaTop(batteria);
														             }
								    	        					//-------------------07-02-2022---------------------
								    	        					
								    	        					
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
								    	        				
								    	        				
								    	        				//AGGIORNO GLI INDICATORI
				    	        								int total_batt = Integer.parseInt(indicatore.conteggio.getText());
								    	        				int total_scarto = Integer.parseInt(indicatore.scarto.getText());
								    	        							    	        				
								    	        				int totale_buone = total_batt - total_scarto;
								    	        				
								    	        				float good_percent = ((float)totale_buone / total_batt)*100; 
								    	        				float fake_percent = ((float)total_scarto / total_batt)*100;
								    	        				
								    	        				DecimalFormat df = new DecimalFormat("###.#");
								    	        				
								    	        				setting.getLabelBatterieBuone().setText(""+df.format(good_percent) + " %");
								    	        				setting.getLabelBatterieScartate().setText(""+df.format(fake_percent)+ " %");
				    	        				//}//stazione di controllo 10
				    	        			
								    	        				
				    	        			
				    	        			}//fine if final controller 
				    	        			
				    	        			
				    	        			
				        				}catch(Exception j) {
					        				log.write("Reader PLC. Stazione :"+nomeStazione +" batteria: "+ cod_batteria+" old:"+codice_batteria_old+" - Errore lettura posizione buffer 0 : " + j.toString());
					        			}
			        			 			
			    	        		
			        			 }// fine if codice letto correttamente
			        			 
			        			 
			        			 //IN ATTESA DELLLA BATTERIA . POSTAZIONE DI CONTROLLO
			        			 else {
					        				 try {
							        				 indicatore.setBatteriaZero("IN ATTESA");
							        				 
							        				 if (isFinalController()){
							        					 areaErrore.setBackground(Setting.grigio);
								        		   		 areaErrore.setText(Setting.ATTESA_BATTERIA);
								        		   		 Setting.getCodiceBatteriaScartata().setText("IN ATTESA");
								        	   			 Setting.getCodiceBatteriaScartata().setBackground(Setting.grigio);
							        				 }
					        				 }catch(Exception j) {
				    	        					log.write("Reader PLc line501->   - Errore else areaerrore indicatorew "+nomeStazione+" :" + j.toString());
				    	        				}
						        				 
					        	 }//fine else
	        			
	        			
			        			 
			        			 	        		
				     }//fine i==0	
	        		
	        		//29-03-2022 AGGIUNGO IL CONTROLLO SULLA POSTAZIONE ABILITATA automaticamente
	        		else if (data.after(dax) && (i>0) && (!isFinalController()) ){
	        			
	        			try {
    			        	//top = (array.getOnTop());
    	        			dax.setTime((sdf.parse(timestamp)));  	
    	        		}catch(Exception e){
    	        			
    	        		}
	        			
	        			if ((i==1)&&(stato.equals("1"))) {
	        				//per la linea 1 la 6 è la prova tenuta
	        				//se sono nella pila posizione 1 ed il risultato è OK
	        				//posso pensare di contare
	        				//se invece è ok, vedo se è già inserita negli scarti.
	        				//in caso la rimuovo tra gli scarti. Vedi:riprocessamento
	        				if (arrayScarto.contains(batteria)&&(cod_batteria.length()>20)) {
	        					arrayScarto.cancellaBatteria(batteria);
	        					setting.WriteProperties("conteggio_finale", ""+	Setting.PEZZI,"conteggio_scarti", ""+Setting.SCARTI,nomeStazione);
	        					log.write("ReaderPLC. RIMUOVO Scarto. Attuali dimensioni :" + ArrayBatteriePostazioneScarto.totaleBatterie +" -> batteria:" + batteria.getCodiceBatteria());
	        				}	
	        				
	        				if ((nomeStazione == Setting.POSTAZIONE_CONTATORE1) || (nomeStazione == Setting.POSTAZIONE_CONTATORE2)) {
	        					Setting.PEZZI +=1;
		        				Setting.Contapezzi.setText(""+Setting.PEZZI);
		        				
	        				}
	        			}
	        			
	        			
	        			//12-04-2022
	        			//se la batteria è KO ->scarto array
	        			if ((i==1)&&(stato.equals("0"))) {
	        				//se la batteria ha esito KO la inserisco nell'array delle batterie scartate
	        				if (!arrayScarto.contains(batteria)&&(cod_batteria.length()>20)) {
	        					arrayScarto.addBatteriaTop(batteria);
	        					setting.WriteProperties("conteggio_finale", ""+	Setting.PEZZI,"conteggio_scarti", ""+Setting.SCARTI,nomeStazione);
		        				log.write("ReaderPLC. Aggiungo Scarto. Attuali dimensioni :" + ArrayBatteriePostazioneScarto.totaleBatterie+" -> batteria:" + batteria.getCodiceBatteria());
	        				}
	        				
	        			}
	        				
	        			
	        			
	        			
	        			
	        			
	        			//29-03-2022
	        			if (Setting.statiPLC[nomeStazione].RUN) {
	        			
	        			//22-02-2022 AGGIUNGO IL CONTROLLO SULLA POSTAZIONE ABILITATA . SE E' IN BYPASS QUI NON ENTRA
	        			
				    	 				    	
						    	 				numero_di_cicli_senza_batterie = 0; //resetto il contatore
						    	 				
						    	 				try {	
						    	 					SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    	 					Date date3 = new Date(System.currentTimeMillis());
						    	 					String dax3 = sdf3.format(date3);
						    	 					Setting.setData_aggiornamento(""+dax3);
						    	 				}catch(Exception k) {
						    	 					log.write("Reader PLC line532.errore dateformat . k:" + k.toString());
						    	 				}
						    		        						    	 				
						    	 				indicatore.setTempo(timestamp);
					    	        			indicatore.setBatteria(cod_batteria);
					    	        			
					    	        			indicatore.scarto.setText(""+Setting.totale_batterie_scartate[nomeStazione-1]);
					    	        			
					    	        
					    	        			tempo_ultima_batteria = timestamp;
					    	        			
					    	        			
					    	        			
					    	        try {	
					               	
					    	        	//questa variabile 
					    	        	//old_stato_batteria="-1";
					    	        	
					    	        	if (check(batteria,i)) {
					               			 
					               			   boolean contiene = false;
					               			//log.write("Reader PLC.DOPO VERIFICA - Batteria:"+batteria.getCodiceBatteria()+ " - Risultato:"+ batteria.getStatoBatteria());
					               			 
					               			 	contiene = array.contains(batteria);
					               			 
								                if (!contiene) {
								                	
								                	try {
								                										                		
								                		array.addBatteriaTop(batteria);
								                		
								                		//conteggio +=1;
								                		Setting.totale_batterie_lavorate[nomeStazione-1] += 1; 
								                		
								                		float v1 = ((float)(data1))/1000;
								                		float v2 = ((float)(data2))/1000;
								                		
								                		indicatore.risultato.setBackground(Setting.grigio);
								                		
								                		if (nomeStazione==1) indicatore.risultato.setText(""+data1+" - " + data2);
								                		if (nomeStazione==2) indicatore.risultato.setText(""+data1+" - " + data2);
								                		if (nomeStazione==3) indicatore.risultato.setText(""+data1+" - " + data2);
								                		if (nomeStazione==4) indicatore.risultato.setText(""+data1+" - " + data2);
								                		
								                		if (nomeStazione==5) indicatore.risultato.setText(""+ data1 + " ["+(data1-data2)+" mbar] " +data2);
								                		if (nomeStazione==6) indicatore.risultato.setText(""+ data1 + " ["+(data1-data2)+" mbar] " + data2);
								                		
								                		if (nomeStazione==7) indicatore.risultato.setText(v1 + " [mm] " + v2);
								                		
								                		
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

					          
					    	        
	        				}//fine Setting.RUN
					    	        
				         }//fine data after
				     
	        		
	        	 
	        	}catch(Exception e) {
	       		log.write("Reader PLC. Stazione :"+nomeStazione +" - Errore in data: " + e.toString());
	            }
	        	
	        	
	        	
	        }//fine for
	        
	        
	        int ora = LocalDateTime.now().getHour();
	              
			if ((((ora==22)||(ora==6)||(ora==14)) && Setting.riazzera_contatori)) {
				
				Setting.riazzera_contatori = false;
				
				Setting.PEZZI = 0;
				Setting.SCARTI = 0;
				
				numero_batterie_riprocessate = 0;
				
				Setting.totale_batterie_scartate[nomeStazione-1] = 0;
				Setting.totale_batterie_lavorate[nomeStazione-1] = 0; 
				
				indicatore.setConteggio("0");
				//indicatore.scarto.setText(""+Setting.totale_batterie_scartate[nomeStazione-1]);
				indicatore.scarto.setText(""+Setting.PEZZI);
				
				arrayScarto.cancellaTutto(); //cancello le batterie nel buffer di scarto
				
			}//fine if
			
			
			
			if ((ora==23)||(ora==15)||(ora==7)) {
				Setting.riazzera_contatori = true;
			}
			
			 //---------------------------
			 if ((numero_di_cicli_senza_batterie>NUMERO_CICLI_START_STOP)) {
		        	
		        	indicatore.statoLinea.setText("STOP");
		        	indicatore.statoLinea.setBackground(Setting.rosso);
		        	
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
		        		indicatore.statoLinea.setBackground(Setting.verde);
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
			 //--------------------------
			
		    Setting.SCARTI = arrayScarto.totaleBatterie;
	        
	        numero_di_cicli_senza_batterie += 1;
	        	   
		    bufferBatterie.setValue(ArrayBatteriePostazione.totaleBatterie);
	        bufferBatterie.setString(""+ArrayBatteriePostazione.totaleBatterie+ " BATTERIE IN CODA");
	        	
	        prima_lettura = false;
	    	
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
	    		Indicatore.statoplc.setText("ERRORE");
	    		Indicatore.statoplc.setBackground(Setting.rosso);
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
	     * @param batteria è la batteria da controllare
	     * @indice è l'indice della batteria nel buffer del PLC.
	     * @return true se la batteria è da inserire, false altrimenti 
	     * 
	     * il controllo lo faccio solo sulla batteria 0 , ma non la inserisco nel database. Il resto delle batterie nel buffer
	     * non ha più senso dare l'ok al PLC
	     */
	    private boolean check(Batteria batteria, int indice) {
	    	//boolean ritorno = false; 
	    	
	    		//monitor.append("INDICE:"+indice+" HO INVIATO IL CODICE BATTERIA: " + batteria.getCodiceBatteria() +" CON POSTAZIONE="+batteria.getPostazione()+"\n");	    					
	    		if (batteria.getCodiceBatteria().length()>12) { //SE IL CODICE è VALIDO
		    		
	    					int tmp = -1;
	    					
	    					//SE NON SONO NELLA PRIMA POSIZIONE
	    					if (indice != 0) {	
	    						//CONTROLLO SU TUTTE LE BATTERIE <> 0
	    					
	    						if (!prima_lettura) 
	    						{
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
	    							}catch(Exception h) {
	    	    						log.write("Reader PLC. Stazione :"+nomeStazione +" - check.control >0: Cod.batteria:"+batteria.getCodiceBatteria()+ " - primalettura Errore:" + h.toString());
	    		    				}
	    						}//fine else
	    						
	    							
	    						try {		
		    						//scarto abilitato
	    							
	    							//MODIFICA DEL 09/03/2022
	    							if(Setting.statiPLC[nomeStazione].RUN) {
		    						//if ((configuratore.getListaAtomoConfigurazione()[nomeStazione-1].scartoabilitato>0)||(configuratore.getListaAtomoConfigurazione()[nomeStazione-1].statoscanner>0)	){
		    							//codice di ritorno dal controllo, codice batteria, postazione 
		    							return segnala(tmp,batteria.getCodiceBatteria(),batteria.getPostazione());	//se la batteri non supera il testo, segnalo con    						
		    						}else
		    						{
		    							//SCARTO DISABILITATO
		    							//MODIFICA DEL 08-02-2022
		    							//voglio memorizzare che la postazione è disabilitata. Lo faccio memorizzando -2
		    							//log.write("Reader PLC. Stazione :"+nomeStazione +" - Batteria:"+batteria.getCodiceBatteria()+ " - Risultato:"+ tmp);
		    							batteria.setStato("-2");
		    							return segnala(9,batteria.getCodiceBatteria(),batteria.getPostazione());
		    							
		    						}
		    					}catch(Exception h) {
		    					//in realtà non riesco a leggere la configurazione dal PLC. problema DBAREA 
		    						return segnala(9,batteria.getCodiceBatteria(),batteria.getPostazione());
		    					}
    		    				
	    					
	    			}//fine else prima lettura
	    					
	    					
	    					
	    		}//CODICE CORRETTO
				
				
			
	    	
	    	//con falso non inserisco batterie, con true si
	    	return false;
	    }//fine check
	    
	    
	
	   public boolean segnala(int risposta, String codice_batt, String postazione) {
		   
		   boolean ris = false;

		   switch(risposta) {
		   case -1:
			   //indicatore.batteria.setBackground(setting.bianco);
			   indicatore.conteggio.setBackground(Setting.bianco);
			   indicatore.stato.setBackground(Setting.arancio); 
			   indicatore.setStato("BYPASS");
			   //conteggio +=1;
			   //indicatore.setConteggio(""+conteggio);
			   indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
			   ris = true;
			   break;
		   case 0:
			   	//indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(Setting.bianco);
			   	indicatore.stato.setBackground(Setting.verde);
				indicatore.setStato("OK");
				//conteggio +=1;
				//indicatore.setConteggio(""+conteggio);
				indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				stato_batteria_postazione_bilancia2 = "1";
				
				//if (postazione.equals("2"))		    								
					//log.write("Reader PLC. Postazione 2 da segnala. Ritorno = "+risposta);
				
				ris = true;
		   break;
		   case 1:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(Setting.bianco);
			    indicatore.stato.setBackground(Setting.rosso); 
			    indicatore.setStato("RIPROCESS.");
			    numero_batterie_riprocessate +=1;
				//conteggio +=1;
			    indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				//indicatore.setConteggio(""+conteggio);
				//indicatore.riprocessato.setText(""+numero_batterie_riprocessate);
				try {
					//setting.WriteProperties("numero_batterie_riprocessate_postazione"+nomeStazione, ""+numero_batterie_riprocessate,nomeStazione);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ris = true;
		   break;
		   // eventuali altri case
		   case 2:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(Setting.bianco);
			    indicatore.stato.setBackground(Setting.arancio);
			    indicatore.setStato("KO PRECE.");
			    log.write("BATTERIA KO nella stazione precedete. POSTAZIONE ATTUALE N."+postazione+"    CODICE BATTERIA:" + codice_batt);
				//conteggio +=1;
				//numero_batterie_scartate +=1;
			    indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
			    //indicatore.setConteggio(""+conteggio);
				ris = true;
		  break;
				 
		   case 3:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(Setting.bianco);
			    indicatore.stato.setBackground(Setting.rosso); 
			    indicatore.setStato("TIMEOUT");
			    log.write("TIMEOUT DEL DATABASE.  --->  postazione n."+postazione+"  , BATTERIA:"+ codice_batt);
				//conteggio +=1;
			    indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				//indicatore.setConteggio(""+conteggio);
				ris = true;
		   break;
		   case 4:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(Setting.bianco);
			    indicatore.stato.setBackground(Setting.rosso); 
			    indicatore.setStato("SALTO P.");
			    log.write("SALTO POSTAZIONE N."+postazione+"   CODICE BATTERIA:" + codice_batt);
			    
				//conteggio +=1;
			    indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				//indicatore.setConteggio(""+conteggio);
				ris = true;
		   break;
		   case 5:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(Setting.bianco);
			    indicatore.stato.setBackground(Setting.rosso); 
			    indicatore.setStato("NO LAN");
			    //log.write("NEL CHECK HO RISCONTRATO UN VALORE DI CONTROL. NON DOVREI ESSERE QUI. - POSTAZIONE N."+postazione+"   CODICE BATTERIA:" + codice_batt);
				//conteggio +=1;
			    indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				//indicatore.setConteggio(""+conteggio);
				ris = true;
		   break;
		   case 6:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(Setting.bianco);
			    indicatore.stato.setBackground(Setting.arancio); 
			    indicatore.setStato("BUFFER");
				//conteggio +=1;
			    indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				//indicatore.setConteggio(""+conteggio);
				ris = false;
		   break;
		   case 7:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(Setting.bianco);
			    indicatore.stato.setBackground(Setting.rosso);
			    indicatore.setStato("ESITO KO");
			    log.write("BATTERIA KO. POSTAZIONE N."+postazione+"    CODICE BATTERIA:" + codice_batt);
				//conteggio +=1;
			    
			    //Setting.totale_batterie_scartate[nomeStazione-1] +=1;
				
			    
			    indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				//indicatore.setConteggio(""+conteggio);
				ris = true;
		  break;
		   case 8:
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(Setting.bianco);
			    indicatore.stato.setBackground(Setting.rosso);
			    indicatore.setStato("ESITO KO");
			    //log.write("BATTERIA KO. POSTAZIONE N."+postazione+"    CODICE BATTERIA:" + codice_batt);
				//conteggio +=1;
				//numero_batterie_scartate +=1;
				//indicatore.setConteggio(""+conteggio);
				ris = false;
		  break;
		   case 9:
			   	//OK CON BYPASS
				//indicatore.conteggio.setBackground(Setting.bianco);
			   	//indicatore.stato.setBackground(Setting.verde);
			   	//indicatore.stato.setBackground(Setting.verde);
			   	indicatore.conteggio.setBackground(Setting.grigio);
			   	indicatore.stato.setBackground(Setting.grigio);
			   	indicatore.batteria.setBackground(Setting.grigio);
			   	indicatore.batteriaZero.setBackground(Setting.grigio);
			   	indicatore.conteggio.setBackground(Setting.grigio);
				indicatore.setStato("BYPASS");
				
				indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				//indicatore.setConteggio(""+conteggio);
				ris = true;
		   break;
		   case 10:
				indicatore.conteggio.setBackground(Setting.bianco);
			   	indicatore.stato.setBackground(Setting.rosso);
				indicatore.setStato("ERR DBCONF");
				//conteggio +=1;
				indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				//indicatore.setConteggio(""+conteggio);
				ris = true;
		   break;
		   case 11: //KO POSTAZIONE 9 BILANCIA
			   //indicatore.batteria.setBackground(setting.bianco);
				indicatore.conteggio.setBackground(Setting.bianco);
			    indicatore.stato.setBackground(Setting.rosso);
			    indicatore.setStato("ESITO KO");
			    log.write("BATTERIA KO. POSTAZIONE N."+postazione+"    CODICE BATTERIA:" + codice_batt);
				//conteggio +=1;
			    //Setting.totale_batterie_scartate[nomeStazione-1] +=1;
				indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				//indicatore.setConteggio(""+conteggio);
				stato_batteria_postazione_bilancia2 = "0";
				ris = true;
		  break;
		   
		   
		   
		   
		   default:
			   //indicatore.batteria.setBackground(setting.bianco);
			   indicatore.conteggio.setBackground(Setting.bianco);
			   indicatore.stato.setBackground(Setting.rosso); 
			   indicatore.setStato("NET :" + risposta);
			   log.write("NEL CHECK HO RISCONTRATO UN VALORE DI DEFAULT. NON DOVREI ESSERE QUI");
			   //conteggio +=1;
			   indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
			   //indicatore.setConteggio(""+conteggio);
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
		   
				   	
		   	//log.write("POSTAZIONE "+batteria.getPostazione()+". RITORNO IL VALORE = " + ritorno + " con batteria cod:" + batteria.getCodiceBatteria());
		   
		   	if (ritorno > 0) {
		   		
	   			      //-----------------------------------------------------------------------------------------------
			   			
			   				
			   				String nome_postazione_errore = "NON CONOSCIUTA";
			   				
			   				//gli errori <10 sono KO
			   				if (ritorno<=10) {
			   					
			   					Setting.totale_batterie_scartate[nomeStazione-1] +=1;
					   				indicatore.risultato.setBackground(Setting.rosso);
					   				
				   					if (ritorno == 1) nome_postazione_errore = "CON. CORTI 1";
					   				if (ritorno == 2) nome_postazione_errore = "PUNTATRICE 1";
					   				if (ritorno == 3) nome_postazione_errore = "PUNTATRICE 2";
					   				if (ritorno == 4) nome_postazione_errore = "CON. CORTI 2";
					   				if (ritorno == 5) nome_postazione_errore = "P. TENUTA 1";
					   				if (ritorno == 6) nome_postazione_errore = "P. TENUTA 2";
					   				if (ritorno == 7) nome_postazione_errore = "ALT. POLARI";
					   				
				   					
				   					SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				   					Date date = new Date(System.currentTimeMillis());
				   					//System.out.println(formatter.format(date));
				   					viewer.setMessage("----------------------------");
					   				viewer.setMessage("BATTERIA COD. " + batteria.getCodiceBatteria());
					   				viewer.setMessage("SEGNALATA PER KO P. " + nome_postazione_errore );
					   				viewer.setMessage("----------------------------\n");
					   				
					   				areaErrore.setBackground(Setting.rosso);
					   				
					   				
					   				areaErrore.setText("BATTERIA " + batteria.getCodiceBatteria() + " - KO POSTAZIONE " + nome_postazione_errore );
					   				Setting.getCodiceBatteriaScartata().setText(batteria.getCodiceBatteria());
					   				Setting.getCodiceBatteriaScartata().setBackground(Setting.rosso);
					   				
					   				//MODIFICA DEL 11_02_2022. DEVO TENERE TRACCIA DELLO STATO RICEVUTO
					   				batteria.setStato("0");
					   				
					   				indicatore.risultato.setBackground(Setting.rosso);
					   				indicatore.risultato.setText("KO P. " + ritorno);
					   				
					   				
					   			}//FINE IF ritorno <10
			   				
			   				
				   			//è maggiore del numerodelle postazioni, ma comunque inferiore a postazioni *2 cioè sono nel campo del salta postazione	
				   			if (((ritorno>10)&& (ritorno<20)) || (ritorno==100) ) {

				   					//NON SEGNO LE BATTERIE SALTO POSTAZIONE
				   					//numero_batterie_scartate +=1;
				   					indicatore.risultato.setBackground(Setting.arancio);
				   				
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
					   				
					   				//log.write("readerplc 1120-> sono in nritorno fra 10 e 20");
					   				
					   									   				
					   				areaErrore.setBackground(Setting.arancio);
					   				Setting.getCodiceBatteriaScartata().setText(batteria.getCodiceBatteria());
					   				Setting.getCodiceBatteriaScartata().setBackground(Setting.arancio);
					   				
					   				if (ritorno!=100)
					   					areaErrore.setText("COD." + batteria.getCodiceBatteria() + " - SALTO POSTAZIONE " + nome_postazione_errore);
					   				else
					   					areaErrore.setText("COD." + batteria.getCodiceBatteria() + " - POSTAZIONE " + nome_postazione_errore);
					   				
					   				//System.out.println(formatter.format(date));
					   				viewer.setMessage("-------------------------------");
						   			viewer.setMessage("BATTERIA COD. " + batteria.getCodiceBatteria());
						   			
						   			if (rit<10)
						   				viewer.setMessage("SEGNALATA PER SALTO  " + nome_postazione_errore);
						   			else {
						   				viewer.setMessage("SEGNALATA PER " + nome_postazione_errore);
						   			}
						   				
						   				viewer.setMessage("----------------------------\n");
						   				
						   			//MODIFICA DEL 11_02_2022. DEVO TENERE TRACCIA DELLO STATO RICEVUTO
						   			batteria.setStato("1");  //IL SALTO DI POSTAZIONE PER ADESSO LO CONSIDERO OK
						   			
						   			indicatore.risultato.setBackground(Setting.rosso);
					   				indicatore.risultato.setText("SALTO P. " + (ritorno-10));
						   			
				   				
				   				}//FINE IF ritorno tra 10 e 20
				   			
				   		
			   			
			   		
			   			//non e' final controller
			   			/*
			   			if ((ritorno<Integer.parseInt(setting.getNumeroStazioniAttive()))){
			   				indicatore.risultato.setBackground(Setting.rosso);
			   				indicatore.risultato.setText("KO P. " + ritorno);
			   				//MODIFICA DEL 11_02_2022. DEVO TENERE TRACCIA DELLO STATO RICEVUTO
				   			batteria.setStato("0");
			   			}
			   			*/
				   			/*
			   			if ((ritorno>=Integer.parseInt(setting.getNumeroStazioniAttive()))&& (ritorno<(Integer.parseInt(setting.getNumeroStazioniAttive())*2)) ){
			   				indicatore.risultato.setBackground(Setting.rosso);
			   				indicatore.risultato.setText("SALTO P. " + (ritorno-Integer.parseInt(setting.getNumeroStazioniAttive())));
			   			//MODIFICA DEL 11_02_2022. DEVO TENERE TRACCIA DELLO STATO RICEVUTO
				   			batteria.setStato("1");
			   			}
			   			*/
			   			
			   			
			   			//-----------------------------------------------------------------------------------------
			   			
			   				
			   } //fine controllo >0
			   				
			   			
	   		
	   		if ((ritorno == 0) && isFinalController() ){
	   			
	   			//log.write("readerplc -> RITORNO 0 E POSTAZIONE DI CONTROLLO ");
	   			
	   			indicatore.risultato.setBackground(Setting.verde);
	   			indicatore.risultato.setText("BATTERIA CONFORME");
	   			areaErrore.setBackground(Setting.verde);
	   			areaErrore.setText("BATTERIA " + batteria.getCodiceBatteria() + " - ESITO OK ");
	   			Setting.getCodiceBatteriaScartata().setText(batteria.getCodiceBatteria());
	   			Setting.getCodiceBatteriaScartata().setBackground(Setting.verde);
	   			
	   			//MODIFICA DEL 11_02_2022. DEVO TENERE TRACCIA DELLO STATO RICEVUTO
	   			batteria.setStato("1");
	   			
	   		}
	   		
	   		if (ritorno == -1) {
	   			indicatore.risultato.setBackground(Setting.rosso);
	   			indicatore.risultato.setText("ERR. LAN");
	   			
	   			//MODIFICA DEL 11_02_2022. DEVO TENERE TRACCIA DELLO STATO RICEVUTO
	   			batteria.setStato("-2"); //IL -2 è IL bypass
	   			
	   			//NELLA POSTAZIONE 10, IN CASO DI MANCANZA LAN, SCRIVO
	   			if (Integer.parseInt(batteria.getPostazione()) == Setting.STAZIONE_DI_CONTROLLO_2) {
	   				areaErrore.setBackground(Setting.rosso);
	   				areaErrore.setText("ERRORE RETE - DISABILITARE CONTROLLO");
	   			}
	   				
	   		}
	   		
	   		if (ritorno == -2) {
	   			
	   			viewer.setMessage("----------------------------");
   				viewer.setMessage("BATTERIA COD. " + batteria.getCodiceBatteria()+ " POSTAZIONE:" + nomeStazione);
   				viewer.setMessage("SEGNALATA PER NON PRESENZA DEL RECORD NEL DB. " + ritorno );
   				viewer.setMessage("----------------------------\n");
	   			
	   			indicatore.risultato.setBackground(Setting.rosso);
	   			indicatore.risultato.setText("NON TROVATA");
	   			
	   		    //NELLA POSTAZIONE 10, IN CASO DI MANCANZA LAN, SCRIVO
	   			if (isFinalController()) {
	   				areaErrore.setBackground(Setting.arancio);
	   				areaErrore.setText("BATTERIA " + batteria.getCodiceBatteria() + " - NON TROVATA ");
	   				
	   			    //MODIFICA DEL 11_02_2022. DEVO TENERE TRACCIA DELLO STATO RICEVUTO
		   			batteria.setStato("-2"); //IL -2 è IL bypass
	   			}
	   		}
	   		
	   		
	   		if (isFinalController()) {
	   			
	   			//log.write("readerplc -> sono nuovamente nella postazione di controllo VERSO FINE");
	   				   			
	   			Setting.totale_batterie_lavorate[nomeStazione-1] +=1;
	   			indicatore.conteggio.setBackground(Setting.bianco);
	   			indicatore.stato.setBackground(Setting.verde);
	   			indicatore.setStato("" + ritorno);
	   			
	   			indicatore.setTempo(batteria.gettimestamp());
				indicatore.setBatteria(batteria.getCodiceBatteria());
				
				indicatore.setConteggio(""+Setting.totale_batterie_lavorate[nomeStazione-1] );
				indicatore.scarto.setText(""+Setting.totale_batterie_scartate[nomeStazione-1]);
				//salvo permanentemente questi valori per tenerli in memoria in caso di chiusura
				
				try {
					//setting.WriteProperties("conteggio_finale", ""+	Setting.totale_batterie_lavorate[nomeStazione-1],"numero_batterie_scartate", ""+Setting.totale_batterie_scartate[nomeStazione-1],nomeStazione);
					
				}catch(Exception kk) {
					log.write("readerPLC line1244 -> errore scrivirisultato . err: "+ kk.toString());
				}
				
				
	   		}//fineif postazione 10
		   	
		   	
		   	
		    //SCRIVO RISULTATO DEL CONTROLLO SUL DB	
		    //CON VALORE INFERIORE A 10 LA BATTERIA VIENE BLOCCATA DAL PLC (DEFINITO DAL PLC)
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
			Date date = new Date(System.currentTimeMillis());
			//System.out.println(formatter.format(date));
		   
		    		   		  
			int indirizzo_start = 0;
			S7.SetWordAt(Buffer, indirizzo_start , ritorno);
	   		indirizzo_start +=2;
	   			   			   		   
	   		S7.SetDateAt(Buffer, indirizzo_start , date);
	   		
	   		indirizzo_start +=8;
	   		
	   		String code = batteria.getCodiceBatteria();
	   			   		
	        S7.SetString(Buffer, indirizzo_start, code);
	           
	        int db = Setting.DB_ESITO_POSTAZIONE_CONTROLLO2;
	        
	   		try {
	   			DBWrite(db); //inizio, dimensioni
	   		}catch(Exception h) {
	   			log.write("readerplc 1276 -> Errore scrittura PLC per memorizzare scarti ultima postazione. DB="+db+" : ERR=" + h.toString());
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

