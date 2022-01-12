package DB;

import java.util.TimerTask;
import linea2.Setting;
import linea2.greenCode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;





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
	
		
	

public void getTipoGriglia() {
	
	String tipo_griglia ="";
	String orario ="";
  
	  try {
		c_mysql = pool.getConnection();
		stmt_mysql = c_mysql.createStatement();
	 
	  } catch (Exception e) {
         System.out.println("Errore connessione al database");
         return;
         // log.write("Errore statement :"+e.getMessage()+"   Modulo:Segnalazione");
      }//FINE CATCH

	
	  ResultSet rs;	            
			try {   
			      rs = stmt_mysql.executeQuery("SELECT * FROM tipo_griglia order by tempo desc limit 1");
			      while (rs.next()){
						
			    	  tipo_griglia = rs.getString("descrizione"); //ultima_postazione processata
			    	  orario = rs.getString("tempo");
						
					}//fine while
					
					rs.close();
				            
			 }catch(Exception h) {
				   	 //log.write("ERRORE PRELIEVO TIPOGRIGLIA.   Errore:"+ h.toString());	
				    tipo_griglia = "ERROR";
				    System.out.println("errore prelievo griglia " + h.toString());
				   	 
			   }          
			 finally {
					  pool.returnConnection(c_mysql);
			  }
			
			System.out.println("TIPOLOGIA GRIGLIA PRELEVATA ="+tipo_griglia);	         
			
			//Setting.TIPO_GRIGLIA = tipo_griglia;
			
				try {
					//Setting.txtTipogriglia.setText(tipo_griglia);
					//Setting.txtAggiornamentoGriglia.setText(orario);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	
}//fine invia


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
	getTipoGriglia();
}


}//fine classe
