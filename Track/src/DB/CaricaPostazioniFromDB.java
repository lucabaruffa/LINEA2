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




public class CaricaPostazioniFromDB {

	
	//private Indicatore indicatore = new Indicatore();
		public 	Connection c_mysql;
		public Statement stmt_mysql ;
		private Setting setting;
		private DBConnectionPool pool;
		
		private int timeout_query = 6000;
		
		private static LoggerFile log = new LoggerFile();
		
		
		public CaricaPostazioniFromDB() {
			
			try {
				setting = new Setting();
			} catch (Exception e) {
				log.write("ERRORE CARICAMENTO CONFIGURAZIONE  - MODULO: CaricaPostazioniFromdb");
				e.printStackTrace();
			}
			
			
						
			//inizializzo pool connessioni db
			pool = new DBConnectionPool();
			
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
				
				System.out.println("carico dati della linea :" + Setting.LINEA_GIUSTIFICATIVI);
					
				rs = stmt_mysql.executeQuery("SELECT * FROM nome_stazioni where linea='"+Setting.LINEA_GIUSTIFICATIVI+"'  order by stazione asc");
				
				
				while (rs.next()){
				
						Setting.nomiPostazioni[(rs.getInt("stazione")+1)] = rs.getString("nome");
						System.out.println("Postazione caricata:" + rs.getString("nome"));
				}//fine while
				
				rs.close();
			
				
			
			} catch (SQLException e) {
				log.write("ERRORE IN getNomiPostazioni. CONNESSIONE. POSSIBILE TIMEOUT: "+e.toString());
				e.printStackTrace();
				
				
			} finally {
				pool.returnConnection(c_mysql);
				log.write("CaricaPostazioniFromdb. RESTITUISCO CONNESSIONE");
				//return risposta;
			}
			
			
		}//fine costruttore
		
	
}//fine classe
