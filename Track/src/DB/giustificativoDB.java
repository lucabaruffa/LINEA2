package DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JTextField;

import linea2.LoggerFile;
import linea2.Setting;

public class giustificativoDB {
	
	public 	Connection c_mysql;
	public Statement stmt_mysql ;
	private Setting setting;
	private DBConnectionPool pool;
	private int timeout_query = 10000;
	private static LoggerFile log = new LoggerFile();
	private int ID = -1;
	private JTextField update;
	long minutidifferenza = 0;

	public giustificativoDB() {
		
		log.write("GiustificativoDB avviato");
		System.out.println("GiustificativoDB avviato");
		
		try {
			setting = new Setting(true);
		} catch (Exception e) {
			//log.write("ERRORE CARICAMENTO CONFIGURAZIONE IN CHECK");
			e.printStackTrace();
		}
		
				
		//inizializzo pool connessioni db
		pool = new DBConnectionPool();
	}
	
	
	public int getFermata() {
		
		System.out.println("GIUSTIFICASTIBVO BD. GETFERMATA CHIAMATA!");
		String giustificativo ="";
		//String orario ="";
				
		minutidifferenza = 0;
		
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		
	  
		  try {
			c_mysql = pool.getConnection();
			stmt_mysql = c_mysql.createStatement();
		 
		  } catch (Exception e) {
	         
	          //System.out.println("Errore statement :"+e.getMessage()+"   Modulo:Segnalazione");
	      }//FINE CATCH

		
		  ResultSet rs;	            
				try {   
				      rs = stmt_mysql.executeQuery("SELECT * FROM "+Setting.DB_TABLE_STOP_LINEA+" where motivo_fermo='DA GIUSTIFICARE'   order by tempo desc limit 1");
				      if (rs.next()){
							 
				    	  ID = rs.getInt("ID"); //ultima_postazione processata
				    	  minutidifferenza = rs.getLong("minuti_differenza");
				    	  giustificativo = rs.getString("motivo_fermo");
				    	  String inizio_fermata = sdf2.format(rs.getTimestamp("start"));
				    	  				    	  
				    	  System.out.println("ID:"+ID+" - ORARIO ATTUALE : "+sdf.format(date) +"  --> differenza:" + minutidifferenza+" - GIUSTIFICATIVO='"+giustificativo+"'");
							
				    	  Setting.data_fermo_linea = inizio_fermata;
				    	  Setting.minuti_fermo_linea = minutidifferenza;
				    	  
						}//fine if
				      else{
				    	  Setting.minuti_fermo_linea = 0;
				    	  Setting.data_fermo_linea = "";
				      }
						
						rs.close();
					            
				 }catch(Exception h) {
					   	 //log.write("ERRORE PRELIEVO TIPOGRIGLIA.   Errore:"+ h.toString());	
					    giustificativo = "ERROR";
					    System.out.println("errore prelievo giustificativoDB " + h.toString());
					   	 
				   }          
				 finally {
						  pool.returnConnection(c_mysql);
				  }
				
							
				
				return ID;
				
				
				
	}//fine invia

	
}//fine classe
