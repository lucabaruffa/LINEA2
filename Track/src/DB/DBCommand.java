package DB;
import java.util.Date;
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

import org.json.JSONObject;

import PLC.ConfiguratorePLc;
import linea2.ArrayBatteryStory;
import linea2.Batteria;
import linea2.LoggerFile;
import linea2.Setting;


public class DBCommand{
	
	//private Indicatore indicatore = new Indicatore();
	public 	Connection c_mysql;
	public Statement stmt_mysql ;
	private Setting setting;
	private DBConnectionPool pool;
	private int timeout_query = 6000;
	
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

public boolean invia_segnalazione(String oggetto,int ID) {
	
	
	  
	  try {
		c_mysql = pool.getConnection();
		stmt_mysql = c_mysql.createStatement();
		log.write("Fatto giustificativo su ID=" + ID);
	  } catch (Exception e) {
       
       System.out.println("Errore statement :"+e.getMessage()+"   Modulo:DBCommand");
    }//FINE CATCH

	
				            
			try {   
			      stmt_mysql.executeUpdate("UPDATE "+Setting.DB_TABLE_STOP_LINEA+" SET motivo_fermo='"+oggetto+"' WHERE id="+ID);
			      log.write("Fatto giustificativo su ID=" + ID);    
				  System.out.println("fatto aggiornamento su ID=" + ID);          
			 }catch(Exception h) {
				 System.out.println("ERRORE :"+ h.toString());	 
				   	 return false;
			   }          
			 finally {
					  pool.returnConnection(c_mysql);
			  }
				         
				
	return true;
}//fine invia


}//fine classe
