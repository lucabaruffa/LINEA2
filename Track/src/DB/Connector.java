package DB;
//PRELEVA I DATI DELLA POSTAZIONE 10  DAL DATABASE E AGGIORNA I DATI  

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

import javax.swing.JTextField;

import linea2.ArrayBatteryStory;
import linea2.Batteria;
import linea2.Indicatore;
import linea2.LoggerFile;
import linea2.Setting;


public class Connector implements Runnable{

		
		final int SLEEP_TIME = 8000; //8 secondi
	
		public Connection c_mysql;
		public Statement stmt_mysql ;
		private Setting setting;
		private DBConnectionPool pool;
		
		
		private JTextField statoLinea;
		private JTextField timeStatoLinea;
		
		private Indicatore indicatore = new Indicatore();
		
		private static String dax = "";
		private int counter = 0;
		private int counter_fault = 0;
		
		private int numero_di_cicli_senza_batterie = 0;		
		
		private int timeout_query = 10000;
		
		private String tempo_ultima_batteria="01/01/1970 00:00:00";
		
		private static LoggerFile log = new LoggerFile();
		ArrayBatteryStory vettArrayBatt =  new ArrayBatteryStory();
		
		public Connector(Indicatore indica) {
			
			try {
				setting = new Setting();
			} catch (Exception e) {
				log.write("ERRORE CARICAMENTO CONFIGURAZIONE  - MODULO: CONNECTOR");
				e.printStackTrace();
			}
			
			indicatore = indica;
			
		
			SimpleDateFormat ier = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 22:00:00");
			Date date = new Date(System.currentTimeMillis());
			
			dax = ier.format(date);
			LocalDate da  = LocalDate.parse(dax);
			LocalDate ieri = da.minusDays(1); 
			
			
			dax = ieri+" 22:00:00";//getMyDate(""+ieri+" 10:00:00", "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
			
			//inizializzo pool connessioni db
			pool = new DBConnectionPool();
			
			
			Thread t = new Thread(this);
			t.start();
			
		}//fine costruttore
		
			
		
		public void getDataFromDB() {
			
			
			try {
				c_mysql = pool.getConnection();
			  
				stmt_mysql = c_mysql.createStatement(); 
				stmt_mysql.setQueryTimeout(timeout_query);
			  } catch (Exception e) {
				  log.write("ERRORE CREAZIONE STATEMENT CHECK IN CONNECTOR");
		          System.out.println("Errore: " +  e.toString());
		      }//FINE CATCH
			  
			
		  
		   ResultSet rs;
			try {
				
				
				
				System.out.println("Ultima sincronizzazione di connector:" + dax);
				
				
				for(int z=0;z<10;z++) {
					setting.totale_batterie_scartate[z]=0;
					setting.totale_batterie_lavorate[z]=0;
				}
				
				
				rs = stmt_mysql.executeQuery("SELECT * FROM linea5 where postazione = 10 AND data>'"+dax+"' order by data asc");	
				
				int numero_occorrenze = 0;
				while (rs.next()){
					numero_occorrenze += 1;
					try {
						
						String cod_batteria = rs.getString("codice");
						int postazione = rs.getInt("postazione");
						int stato_test = rs.getInt("stato_test");
						String timestamp = rs.getString("data");
						int valore1 = rs.getInt("valore1");
						int valore2 = rs.getInt("valore2");
						
						timestamp = timestamp.substring(0,timestamp.length()-2);
						
						tempo_ultima_batteria = timestamp;
						
					//	System.out.println("Stampo batteria:" + cod_batteria + "  time:"+timestamp + "   postazione:" + postazione);
						
						counter += 1;
						
						dax = timestamp;
						
						timestamp = getMyDate(timestamp, "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
												
						
						Batteria batteria = new Batteria(1,cod_batteria,timestamp,""+stato_test,valore1,valore2);
						batteria.setPostazione(""+postazione);
												
						vettArrayBatt.aggiungiBatteria(batteria);
						
						numero_di_cicli_senza_batterie = 0;
						
						indicatore.setBatteria(cod_batteria);
						
						if (stato_test==1) {
							indicatore.conteggio.setBackground(setting.bianco);
						   	indicatore.stato.setBackground(setting.verde);
							
							indicatore.setStato("OK");
						}
						else {
							counter_fault += 1;
							indicatore.conteggio.setBackground(setting.bianco);
						    indicatore.stato.setBackground(setting.rosso);
						    indicatore.setStato("ESITO KO");
							
						}
						
						indicatore.setTempo(timestamp);
						indicatore.setConteggio(""+counter);
						indicatore.scarto.setText(""+counter_fault);
						
						
					} catch (Exception e) {
						log.write("ERRORE CREAZIONE BATTERIA:  "+ e.toString() + "   modulo:CONNECTOR");
						System.out.print("Errore:" +e.toString());
						e.printStackTrace();
					}
				}//fine while
				
				rs.close();
				
				
				 if (numero_di_cicli_senza_batterie>8) {
			        	//monitor.append("STAZIONE "+nomeStazione+" FERMA !!\n");
			        	indicatore.statoLinea.setText("STOP");
			        	indicatore.statoLinea.setBackground(setting.rosso);
			        	indicatore.tempostatoLinea.setText(""+tempo_ultima_batteria);
			        	//numero_di_cicli_senza_batterie = 0;
			        }else {
			        	indicatore.statoLinea.setText("RUNNING");
			        	indicatore.statoLinea.setBackground(setting.verde);
			        	indicatore.tempostatoLinea.setText("");
			        	
			        }
			        	
			        
			        numero_di_cicli_senza_batterie += 1;
			
				
			
			} catch (SQLException e) {
				log.write("ERRORE IN CHECK. CONNESSIONE. POSSIBILE TIMEOUT: "+e.toString() + " modulo: CONNECTOR");
				e.printStackTrace();
				
				
			} finally {
				if (c_mysql != null) {
	                pool.returnConnection(c_mysql);
	            }
				
				//return risposta;
			}
		    
		  
		
		}//FINE METODO
		
	
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
		
		
		
		@Override
		public void run() {
			while(true) {
				getDataFromDB();
				try {
					Thread.sleep(SLEEP_TIME);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}//fine while
					
		}//fine run
	
	
}//fine classe
