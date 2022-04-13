package DB;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import org.json.JSONObject;

import PLC.ConfiguratorePLc;
import linea.ArrayBatteryStory;
import linea.Batteria;
import linea.LoggerFile;
import linea.Setting;


public class DBCommand{
	
	//private Indicatore indicatore = new Indicatore();
	public 	Connection c_mysql;
	public Statement stmt_mysql ;
	private Setting setting;
	private DBConnectionPool pool;
	private int timeout_query = 6000;
	GregorianCalendar offset_data = new GregorianCalendar();
	
	private static LoggerFile log = new LoggerFile();
	
	public DBCommand() {
		
		try {
			setting = new Setting(true);
		} catch (Exception e) {
			log.write("ERRORE CARICAMENTO CONFIGURAZIONE IN CHECK");
			e.printStackTrace();
		}
		
				
		//inizializzo pool connessioni db
		pool = new DBConnectionPool();
		
	}//fine costruttore
	
	public DBCommand(boolean tempo) {
		//inizializzo pool connessioni db
		pool = new DBConnectionPool();
		
	}//fine costruttore
	
	
	
	
	
	

public int setConfigurationChanged() throws UnknownHostException, IOException  {
	
	try {
		 c_mysql = pool.getConnection();
		 stmt_mysql = c_mysql.createStatement();
	  } catch (Exception e) {
		  log.write("Errore getConnection: " +  e.toString());
		  if (c_mysql != null) {
              pool.returnConnection(c_mysql);
          }
		  return -1;            
      }
  
	
	
	Date date = new Date(System.currentTimeMillis());
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	try { 
         	            
			String dat =  sdf.format(date);
			
							            				        
			stmt_mysql.executeUpdate("INSERT INTO configuration (data,lt1_stabilitation_time,lt2_stabilitation_time,lt1_test_time,lt2_test_time,time5,lt1_setpoint_pressure,lt2_setpoint_pressure,lt1_leak,lt2_leak,pressure5,hp_right_max,hp_right_min,hp_left_max,hp_left_min,measure5) VALUES " +
				          		  				   " ('"+dat+"',"+ConfiguratorePLc.getLT1_stabilitation_time()+"," +
				          		  				   ConfiguratorePLc.getLT2_stabilitation_time() + "," +
				          		  				   ConfiguratorePLc.getLT1_test_time() + "," +
				          		  				   ConfiguratorePLc.getLT2_test_time() + "," +
				          		  				   ConfiguratorePLc.getTime5() + "," +
				          		  				   ConfiguratorePLc.getLT1_setpoint_pressure() + "," +
				          		  				   ConfiguratorePLc.getLT2_setpoint_pressure() + "," +
				          		  				   ConfiguratorePLc.getLT1_leak() + "," +
				          		  				   ConfiguratorePLc.getLT2_leak() + "," +
				          		  				   ConfiguratorePLc.getPressure5() + "," +
				          		  				   ConfiguratorePLc.getHP_right_max() + "," +
				          		  				   ConfiguratorePLc.getHP_right_min() + "," +
				          		  				   ConfiguratorePLc.getHP_left_max() + "," +
				          		  				   ConfiguratorePLc.getHP_left_min() + "," +
				          		  				   ConfiguratorePLc.getMeasure5() + 				          		  				   
					")");
			     
			return 0;
				            
  } catch (Exception e) {
	          //System.out.println("Errore: " +  e.getMessage());
			  log.write("ERRORE setConfigurationChanged : " +e.toString()+"\n");
			  return -1;
	             
      } finally {
    	  if (c_mysql != null) {
              pool.returnConnection(c_mysql);
          }
      }
	

}//fine trasmetti




public boolean invia_segnalazione(String oggetto,String messaggio,String destinatari) {
	
	
	
	
  
	  try {
		c_mysql = pool.getConnection();
		stmt_mysql = c_mysql.createStatement();
	 
	  } catch (Exception e) {
         
          log.write("Errore statement :"+e.getMessage()+"   Modulo:Segnalazione");
      }//FINE CATCH

	
				            
			try {   
			      stmt_mysql.executeUpdate("INSERT INTO segnalazioni (oggetto,operatore,testo,linea,email) VALUES " +
				           		  				   " ('"+oggetto+"','SISTEMA','"+messaggio+"', '"+Setting.LINEA_GIUSTIFICATIVI+"','"+destinatari+"')");
				            
			 }catch(Exception h) {
				   	 log.write("ERRORE INSERIMENTO db SEGNALAZIONE.   Errore:"+ h.toString());	 
				   	 return false;
			   }          
			 finally {
					  pool.returnConnection(c_mysql);
			  }
				         
				
	return true;
}//fine invia

public boolean invia_segnalazione(String oggetto,int ID,String note, String badge, String desc_fermo) {
	
	
	 String codice_tesserino = "";
	 
	 //potrebbe essere che badge sia  null
	if (badge.length()>3) {
		
		if (badge.substring(0,1).equals("ò")) {
			log.write("Sono in presenza di badge:" + badge);
			codice_tesserino = badge.substring(1,5);
		}else {
			log.write("Probabilmente sono in presenza di nome:"+ badge);
			codice_tesserino = badge;
		}
		
		
	 }else {
		 codice_tesserino = "UTENTE NON VALIDO";
	 }
		 
		 
	 //System.out.println("codice tesserino:" + codice_tesserino);
	  
	  try {
		c_mysql = pool.getConnection();
		stmt_mysql = c_mysql.createStatement();
		//log.write("Fatto giustificativo su ID=" + ID);
	  } catch (Exception e) {
		  log.write("Errore statement :"+e.getMessage()+"   Modulo:DBCommand");
       System.out.println("Errore statement :"+e.getMessage()+"   Modulo:DBCommand");
    }//FINE CATCH

				            
			try {   
			      stmt_mysql.executeUpdate("UPDATE "+Setting.DB_TABLE_STOP_LINEA+" SET motivo_fermo='"+oggetto+"', operatore='"+codice_tesserino+"', note='"+note+"', desc_motivo_fermo='"+desc_fermo+"' WHERE id="+ID);
			      log.write("Fatto giustificativo su ID=" + ID);    
				  System.out.println("fatto aggiornamento su ID=" + ID + " e operatore:" + codice_tesserino);          
			 }catch(Exception h) {
				 System.out.println("ERRORE :"+ h.toString());	 
				   	 return false;
			   }          
			 finally {
					  pool.returnConnection(c_mysql);
			  }
				         
				
	return true;
}//fine invia



//public void inserisciReport(int batt_lavorate,int batt_scartate, int ora) {
public void inserisciReport() {
	try {
		 c_mysql = pool.getConnection();
		 stmt_mysql = c_mysql.createStatement();
	  } catch (Exception e) {
		  log.write("DBCommand Errore getConnection in inserimento Report: " +  e.toString());
		  if (c_mysql != null) {
             pool.returnConnection(c_mysql);
         }
		  return;      
     }
	  
    try {
    	
    	
    	LocalDateTime now = LocalDateTime.now();
    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");		
    	SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
    	int ora = offset_data.get(Calendar.HOUR_OF_DAY);
		
		
		Date date = new Date(System.currentTimeMillis());
		String tempo = (sdf3.format(date)); //data e ora corrente
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, -8);
		date = calendar.getTime();
		
		//System.out.println(sdf2.format(date));
		
		String oggi = (sdf3.format(date));
		
		int turno = -1;
		if ((ora>=6) && (ora<14) ) turno=1;
		if ((ora>=14) && (ora<22) ) turno=2;
		if ((ora>=22) || (ora<6) ) 	turno=3;
		
		//log.write("DBCOMMAND . oggi = " + oggi);
		
		ResultSet rs = stmt_mysql.executeQuery("SELECT sum(minuti_differenza) as tempo FROM stop_linea"+Setting.DB_BATTERIE_NUM_LINEA+" where start>='"+oggi+"' AND turno="+turno);
		
		int minuti_fermo = 0;
		if (rs.next()){
			minuti_fermo = rs.getInt("tempo");
		}	
		
		stmt_mysql.executeUpdate("INSERT INTO report_linea"+Setting.DB_BATTERIE_NUM_LINEA+" (data,turno,num_batterie,num_scarto,minuti_fermo) VALUES ('"+tempo+"',"+turno+","+Setting.PEZZI+","+Setting.SCARTI+","+minuti_fermo+");");
    	
    }catch(Exception j) {
    	log.write("DBCommand . Inserimento dati nel report. Errore: " + j.toString());
    }
    finally {
		  pool.returnConnection(c_mysql);
    }
   
}//fine inserisci report



private String calcola_motivo_fermo(String oggetto) {
	
	return "";
}


}//fine classe
