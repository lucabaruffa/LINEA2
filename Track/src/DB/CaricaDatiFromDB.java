package DB;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import linea.ArrayBatteryStory;
import linea.Batteria;
import linea.LoggerFile;
import linea.Setting;




public class CaricaDatiFromDB {

	
	//private Indicatore indicatore = new Indicatore();
		public 	Connection c_mysql;
		public Statement stmt_mysql ;
		private Setting setting;
		private DBConnectionPool pool;
		
		private int timeout_query = 3000;
		
		private static LoggerFile log = new LoggerFile();
		ArrayBatteryStory vettArrayBatt =  new ArrayBatteryStory();
		
		public CaricaDatiFromDB() {
			
			try {
				setting = new Setting();
			} catch (Exception e) {
				log.write("ERRORE CARICAMENTO CONFIGURAZIONE  - MODULO: CaricaDatiFromdb");
				e.printStackTrace();
			}
			
			
			int stazioni_attive = Integer.parseInt(setting.getNumeroStazioniAttive());
			
			System.out.println("stazioni attive: " + stazioni_attive);
			
			
			//inizializzo pool connessioni db
			pool = new DBConnectionPool();
			
			
		}//fine costruttore
		
		
		
		
			
		
		public void getDataFromDB() {
			
			
			int risposta = -1;  
			
			try {
				c_mysql = pool.getConnection();
				log.write("CaricaDatiFromDB. GETDATA FROM DB  CHIEDO CONNESSIONE");
			  } catch (Exception e) {  
				log.write("ERRORE CREAZIONE CONNESSIONE CHECK \n");
		      	System.err.println(e.toString());
		           
		      }
		  
			  try { 
				stmt_mysql = c_mysql.createStatement(); 
				stmt_mysql.setQueryTimeout(timeout_query);
			  } catch (Exception e) {
				  log.write("ERRORE CREAZIONE STATEMENT CHECK ");
		          System.out.println("Errore: " +  e.toString());
		      }//FINE CATCH
			  
			
		  
		   ResultSet rs;
			try {
				
				
				//SimpleDateFormat ier = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 22:00:00");
				Date date = new Date(System.currentTimeMillis());
				
				//String dax = ier.format(date);
				//LocalDate da  = LocalDate.parse(dax);
				//LocalDate ieri = da.minusDays(1); 
				
				
				//dax = ieri+" 22:00:00";//getMyDate(""+ieri+" 10:00:00", "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
				//System.out.println("ieri:" + dax);
				
				
				int ora = LocalDateTime.now().getHour();
				 String dax = "";
				 
				 if ((ora>=6) && (ora<14) ) {
					
					 SimpleDateFormat ier = new SimpleDateFormat("yyyy-MM-dd 06:00");
					 dax = ier.format(date);
					
					 
				 }
				 if ((ora>=14) && (ora<22) ) {
					 
					 SimpleDateFormat ier = new SimpleDateFormat("yyyy-MM-dd 14:00");
					 dax = ier.format(date);
					 
				 }
				 if ((ora>=22) && (ora<6) ) {
					 SimpleDateFormat ier = new SimpleDateFormat("yyyy-MM-dd 22:00");
					 if (ora>=22) { 
						 dax = ier.format(date);
					 }
					 	
					 if (ora<6) {
						 date = addDays(date, 1);
						 dax = ier.format(date);
					 }
					 
				 }
				 
				
				
				for(int z=0;z<22;z++) {
					setting.totale_batterie_scartate[z]=0;
					setting.totale_batterie_lavorate[z]=0;
				}
				
				
				//rs = stmt_mysql.executeQuery("SELECT distinct codice, postazione,stato_test,data,valore1,valore2 FROM "+Setting.TABLE_LINEA+" where data>='"+dax+"' AND postazione <10 order by data asc");	
				rs = stmt_mysql.executeQuery("SELECT distinct codice, postazione,stato_test,data,valore1,valore2 FROM "+Setting.TABLE_LINEA+" where data>='"+dax+"'  order by data asc");
				
				
				while (rs.next()){
					
					try {
						
						String cod_batteria = rs.getString("codice");
						int postazione = rs.getInt("postazione");
						int stato_test = rs.getInt("stato_test");
						String timestamp = rs.getString("data");
						int valore1 = rs.getInt("valore1");
						int valore2 = rs.getInt("valore2");
						
						timestamp = timestamp.substring(0,timestamp.length());
						
						//System.out.println("Stampo batteria:" + cod_batteria + "  time:"+timestamp + "   postazione:" + postazione);
						
						timestamp = getMyDate(timestamp, "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
						
						Batteria batteria = new Batteria(1,cod_batteria,timestamp,""+stato_test,valore1,valore2);
						
						batteria.setPostazione(""+postazione);
												
						vettArrayBatt.aggiungiBatteria(batteria);
						
						Setting.totale_batterie_lavorate[postazione-1] +=1;
						
						if ((stato_test==0)) Setting.totale_batterie_scartate[postazione-1] +=1;
						
					} catch (Exception e) {
						log.write("ERRORE CREAZIONE BATTERIA: Modulo CaricadatifromDB "+ e.toString() + "   modulo:CarcaDatiFrom db");
						System.out.print("Errore:" +e.toString());
						e.printStackTrace();
					}
				}//fine while
				
				rs.close();
			
				
			
			} catch (SQLException e) {
				log.write("ERRORE IN CHECK. CONNESSIONE. POSSIBILE TIMEOUT: "+e.toString() + " modulo: caricadatifromdb");
				e.printStackTrace();
				
				
			} finally {
				pool.returnConnection(c_mysql);
				log.write("CaricaDatiFromdb. RESTITUISCO CONNESSIONE");
				//return risposta;
			}
		    
		  
		
		}//FINE METODO
		
		
		
		public static Date addDays(Date date, int days) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.add(Calendar.DATE, days);
					
			return cal.getTime();
		}
		
	
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
	
	
}//fine classe
