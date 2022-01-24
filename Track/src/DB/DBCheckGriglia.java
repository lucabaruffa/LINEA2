package DB;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;
import linea2.Setting;
import linea2.greenCode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;





public class DBCheckGriglia extends TimerTask{
	
	//private Indicatore indicatore = new Indicatore();
	public 	Connection c_mysql;
	public Statement stmt_mysql ;
	private Setting setting;
	private DBConnectionPool pool;
	private int timeout_query = 10000;
	
	//private static LoggerFile log = new LoggerFile();
	
	public DBCheckGriglia() {
		
		try {
			setting = new Setting();
		} catch (Exception e) {
			//log.write("ERRORE CARICAMENTO CONFIGURAZIONE IN CHECK");
			e.printStackTrace();
		}
		
				
		//inizializzo pool connessioni db
		pool = new DBConnectionPool();
		
	}//fine costruttore
	
		
	


public void getElencoGreenCode() {
	
	  
	  try {
		c_mysql = pool.getConnection();
		stmt_mysql = c_mysql.createStatement();
	 
	  } catch (Exception e) {
         
         // log.write("Errore statement :"+e.getMessage()+"   Modulo:Segnalazione");
      }//FINE CATCH

	
	  ResultSet rs;	            
	  try {
		 
		  rs = stmt_mysql.executeQuery("Select * from elenco_greencode where linea='"+Setting.LINEA+"' order by codice asc");
          while (rs.next()) {
			String name = rs.getString("descrizione");
			
			if (name.equals("")) {
				
				//combo.addItem("");
				//c//ombo.setVisible(false);
			} else {
				greenCode green = new greenCode(rs.getString("descrizione"),rs.getString("codice"),rs.getString("linea"),rs.getString("cdc"));
				Setting.listaGreenCode.add(green);
				//combo.addItem(rs.getString("descrizione"));
				System.out.println(rs.getString("descrizione"));
				//combo.setVisible(true);
			}
			}
		} catch (Exception ex) {
			System.out.println("Errore getAllElenco:" + ex.toString());
      	}        
	  finally {
			  pool.returnConnection(c_mysql);
	  }
			
	
}//fine invia


@Override
public void run() {
	// TODO Auto-generated method stub
	//getTipoGriglia();
}


}//fine classe
