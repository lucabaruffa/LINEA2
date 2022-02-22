package DB;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

import linea.Setting;
import linea.greenCode;

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



public ArrayList<String>[] getGiustificativi() {
	
	ArrayList<String> giustificativo1 = new ArrayList<>();
	ArrayList<String> giustificativo2 = new ArrayList<>();
	ArrayList<String> giustificativo3 = new ArrayList<>();
	ArrayList<String> giustificativo4 = new ArrayList<>();
	ArrayList<String> giustificativo5 = new ArrayList<>();
	
	ArrayList<String>[] ritorno = new ArrayList[5];
	
	  
	  try {
		c_mysql = pool.getConnection();
		stmt_mysql = c_mysql.createStatement();
	 
	  } catch (Exception e) {
       
       // log.write("Errore statement :"+e.getMessage()+"   Modulo:Segnalazione");
    }//FINE CATCH

	
	  ResultSet rs;	            
	  try {
		 
		rs = stmt_mysql.executeQuery("Select * from giustificativi where linea='"+Setting.LINEA_GIUSTIFICATIVI+"'  order by idgiustificativi asc");
        
			while (rs.next()) {
				String g1 = rs.getString("giustificativo1");
				String g2 = rs.getString("giustificativo2");
				String g3 = rs.getString("giustificativo3");
				String g4 = rs.getString("giustificativo4");
				String g5 = rs.getString("giustificativo5");
				
				if ((!(g1==null))&&(!g1.equals("")) ) giustificativo1.add(g1);
				if ((!(g2==null)&&(!g2.equals("")))) giustificativo2.add(g2);
				if ((!(g3==null)&&(!g3.equals("")))) giustificativo3.add(g3);
				if ((!(g4==null)&&(!g4.equals("")))) giustificativo4.add(g4);
				if ((!(g5==null)&&(!g5.equals("")))) giustificativo5.add(g5);
				 
			}
		} catch (Exception ex) {
			System.out.println("Errore getGiustificativi:" + ex.toString());
			
    	}        
	  finally {
			
		  		try {
					pool.returnConnection(c_mysql);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  }
	  
	  ritorno[0] = giustificativo1;
	  ritorno[1] = giustificativo2;
	  ritorno[2] = giustificativo3;
	  ritorno[3] = giustificativo4;
	  ritorno[4] = giustificativo5;
	  
	  return ritorno;
			
	
}//fine invia


@Override
public void run() {
	// TODO Auto-generated method stub
	//getTipoGriglia();
}


}//fine classe
