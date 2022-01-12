package DB;
import java.io.IOException;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import linea2.ArrayBatterie;
import linea2.ArrayBatteriePostazione;
import linea2.ArrayBatteryStory;
import linea2.Batteria;
import linea2.LoggerFile;
import linea2.Setting;

public class SenderDB implements Runnable {

	private boolean continua = true;
	//private int t = 1000;
	private JProgressBar progress_bar;
	public 	Connection c_mysql;
	public Statement stmt_mysql ;
	private JTextArea monitor;
	
	private ArrayBatterie array; //array di array di batterie
	
	private Setting setting;
	private int conteggio = 0;
	public JTextField statoplc;
	public JTextField statodb;
	private DBConnectionPool pool;
	
	private ArrayBatteryStory arrayBatteryStory;
	private static LoggerFile log = new LoggerFile();
	
	
	public SenderDB(JTextArea m, JProgressBar b, JTextField splc,  JTextField sdb, ArrayBatterie arrayBat) {
		monitor = m;
		progress_bar = b;
		statoplc = splc;
		statodb = sdb;
		array = arrayBat;
	
		arrayBatteryStory = new ArrayBatteryStory();
		
	
		try {
			setting = new Setting();
		} catch (Exception e) {
			log.write("ERRORE CARICAMENTO CONFIGURAZIONE. Line 49");
			e.printStackTrace();
		}
		
		
	   //inizializzo pool connessioni db
		pool = new DBConnectionPool();
		
		Thread t = new Thread(this);
		t.start();
	
	}//fine costruttore 
	
	
	
	
	
	@Override
	public void run(){
		
		
		log.write("THREAD AVVIATO");
		
		while (true) { 
			if(continua) {
		 
				try {
					//if (c_mysql.isClosed()) connessione();
					trasmetti();
					Thread.sleep(Setting.time_update_sender_db);
				} catch (Exception e) {
					log.write("SenderDB. Errore in trasmetti: " + e.toString());
					e.printStackTrace();
				}  
					
			}//fine if
			
			
		}//fine while
	 }//fine run
	
	
	
	public static String getDate() {
		 return  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
	 }//fine getDate
	
	
public void trasmetti() throws UnknownHostException, IOException  {
	
		try {
			 c_mysql = pool.getConnection();
			 stmt_mysql = c_mysql.createStatement();
			 statodb.setText("OK");
	         statodb.setBackground(setting.verde);
	        //log.write("SENDERDB.   CHIEDO CONNESSIONE");
		  } catch (Exception e) {
			  log.write("SenderDB. Errore getConnection: " +  e.toString());
	          statodb.setText("ERRORE");
	          statodb.setBackground(setting.rosso);
	          if (c_mysql != null) {
	              pool.returnConnection(c_mysql);
	          }//fine if
	             
	      }//fine catch
	  
		 
		
		
		try { 
             
				Iterator<ArrayBatteriePostazione> arraypostazioni = array.getIterator();
								
				int totale = 0;
				
				//ciclo sul buffer
				while (arraypostazioni.hasNext()) {
	              						
					ArrayBatteriePostazione arraybatteria = (ArrayBatteriePostazione)arraypostazioni.next();
					Iterator<Batteria> it = arraybatteria.getIterator();
									
					while(it.hasNext()) {
						
								Batteria batteria = (Batteria)it.next();
					        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					            
					        	String codice = batteria.getCodiceBatteria().trim();
					        	String dat =  sdf.format( batteria.getData());
					            String nomestazione = batteria.getPostazione();
					            int stato = Integer.parseInt(batteria.getStatoBatteria());
					            int valore1 = batteria.getDato1();
					            int valore2 = batteria.getDato2();
					            					            			               
					            //monitor.append("INVIO ---> INDEX:"+batteria.getIndex()+" - POSTAZIONE:"+nomestazione+" - BATTERIA: "+batteria.getCodiceBatteria()+" - TIMESTAMP:"+dat+" - STATO:"+batteria.getStatoBatteria()+" - DATO1:"+batteria.getDato1()+" - DATO2 : "+batteria.getDato2()+ "\n");
					            //sulle due postazioni di controllo corrti, specifico il dettaglio della cella in corto
					            String dettaglio_errore = "";	
					           
					            //cerco di codificare il tipo di errore arrivato dal plc per le stazioni 1 e 4 (controllo corti)
					            if (nomestazione.equals("1") || nomestazione.equals("4")) {
					            	//interpreto il risultato del PLC
					            	dettaglio_errore = valoreScarto(valore1);					            	
					            }//fine if
					            
						        try {   
						           //HO DICHIARATO NEL DATABASE UNIQUE(CODICE,POSTAZIONE,DATA)
						           //IN MODO DA EVITARE DUPLICAZIONI
						        	
						        	//if (nomestazione.equals("2")) log.write("SenderDB. Stato batteria pos2  = " + stato);
						        	
						        	 stmt_mysql.executeUpdate("INSERT INTO linea2 (codice,postazione,stato_test,data,valore1,valore2,dettaglio) VALUES " +
						           		  				   " ('"+codice+"',"+nomestazione+","+stato+",'"+dat+"',"+valore1+","+valore2+",'"+dettaglio_errore+"') ");
						        	//DOPO INSERIMENTO NEL DB CANCELLO LA BATTERIA IN CODA
						            arraybatteria.cancellaBatteria(batteria);
						            
						            
						         }catch(Exception h) {
						        	// log.write("SenderDB. ERRORE INSERIMENTO DB. BATTERIA:"+codice+" - nomestazione:"+nomestazione+" - stato:"+stato+"  - valore1:"+valore1+"  - valore2:"+valore2+"  Errore:"+ h.toString());	 
						        
						        	//SALVATAGGIO SU FILE CVS DEI DATI DELLA BATTERIA NEL CASO DI FALLIMENTO INSERIMENTO. PROBABILE MANCANZA LAN
							         try {
								            String CVS = ""+codice+","+nomestazione+","+stato+","+dat+","+valore1+","+valore2+","+dettaglio_errore;
								            log.writeCSVbatteria(CVS);
								            //DOPO INSERIMENTO NEL FILE CANCELLO LA BATTERIA IN CODA
								            arraybatteria.cancellaBatteria(batteria);
								            
							            }catch(Exception k) {
							            	log.write("SenderDB. ERRORE INSERIMENTO CSV NEL FILE: "+ k.toString());
							            }
						         
						         }//fine catch
						         
						         
					        
					}//fine while dentro
					
					totale += arraybatteria.totaleBatterie;	
					
				} //fine while fuori
					
			
	                
			  } catch (Exception e) {
		          //System.out.println("Errore: " +  e.getMessage());
				  log.write("ERRORE trasmissione : " +e.toString()+"\n");
		         
		      } finally {
		    	  if (c_mysql != null) {
		              pool.returnConnection(c_mysql);
		          }
		    	 // log.write("SENDERDB.   RESTITUISCO CONNESSIONE");
		      }
		
	
	}//fine trasmetti




	
	
	
	private String valoreScarto(int valore) {
		/*
        String dett_guasto = "";
        String stringa_guasto = "";
        
		if (valore>1) {
    		
			//valore -=1;
        	//--------------
        	int []T={1,1,1,1,1,1,1};
        	int i=T.length-1;
        	do{
        		   //resto della divisione per 2 di n nell'i-esimo elemento del //vettore
        		   T[i]=valore%2;
        		   valore=valore/2;
        		   i--;
        	}while(valore!=0);
        	
        	for(i=1;i<T.length;i++) {
        		if (T[i]==0) stringa_guasto +=  " - " + i  ;
        		dett_guasto += T[i] ;
        	}//fine for
		
        	
        	dett_guasto = "GUASTO CELLE " + stringa_guasto;
        	
    	}//fine if valore>1
		return dett_guasto;
		*/
		
		
		Integer Int = Integer.valueOf(valore);   
		if (Int>1) {
			String b = byteToString(Int.byteValue());
			return "GUASTO CELLE " + b;
		}else {
			return "";
		}
			
		
	}
	
	
	
	public static String byteToString(byte b) {
	    byte[] masks = { -128, 64, 32, 16, 8, 4, 2, 1 };
	    StringBuilder builder = new StringBuilder();
	    int count = 9;
	    for (byte m : masks) {
	    	count --;
	        if ((b & m) == m) {
	            builder.append(" - "+count);
	        } else {
	            //builder.append('0');
	        }
	        
	        
	    }//fine for
	    return builder.toString();
	}
	
	
}//fine classe
