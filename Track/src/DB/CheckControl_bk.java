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
import linea.ArrayBatteryStory;
import linea.Batteria;
import linea.LoggerFile;
import linea.Setting;


public class CheckControl_bk{
	
	//private Indicatore indicatore = new Indicatore();
	public 	Connection c_mysql;
	public Statement stmt_mysql ;
	private Setting setting;
	private DBConnectionPool pool;
	private ArrayBatteryStory arrayBatteryStory;
	private int timeout_query = Setting.timeout_query;
	
	private static LoggerFile log = new LoggerFile();
	
	public CheckControl_bk() {
		
		try {
			setting = new Setting();
		} catch (Exception e) {
			log.write("ERRORE CARICAMENTO CONFIGURAZIONE IN CHECK");
			e.printStackTrace();
		}
		
		arrayBatteryStory = new ArrayBatteryStory();
		
		
		//inizializzo pool connessioni db
		pool = new DBConnectionPool();
		
	}//fine costruttore
	
	public CheckControl_bk(boolean tempo) {
		
		
		arrayBatteryStory = new ArrayBatteryStory();
		//inizializzo pool connessioni db
		pool = new DBConnectionPool();
		
	}//fine costruttore
	
	
	
	//ATTUALMENTE NON UTILIZZATO
	public boolean checkHTTP(String codice_batt) throws Exception
	{
		
		
				String url = "http://ETPSERVER.FIAMM.DOM/tracciabilita/check/battery/";
		        URL obj = new URL(url);
		        
		        Map<String,Object> params = new LinkedHashMap<>();
		        params.put("codice_batteria", codice_batt);
		        StringBuilder postData = new StringBuilder();
		        for (Map.Entry<String,Object> param : params.entrySet()) {
		            if (postData.length() != 0) postData.append('&');
		            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
		            postData.append('=');
		            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		        }
		        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
		        
		        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		        // optional default is GET
		        con.setRequestMethod("POST");
		        //add request header
		        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		        con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		        con.setDoOutput(true);
		        con.getOutputStream().write(postDataBytes);
		        con.setConnectTimeout(10000);
		        
		        int responseCode = con.getResponseCode();
		        //System.out.println("\nSending 'GET' request to URL : " + url);
		        System.out.println("Response Code : " + responseCode);
		        BufferedReader in = new BufferedReader(
		                new InputStreamReader(con.getInputStream()));
		        String inputLine;
		        StringBuffer response = new StringBuffer();
		        while ((inputLine = in.readLine()) != null) {
		        	response.append(inputLine);
		        }
		        in.close();
		        
		        //print in String
		        System.out.println(response.toString());
		        
		        //Read JSON response and print
		        JSONObject myResponse = new JSONObject(response.toString());
		        
		        String ritorno = myResponse.getString("response");
		        
		       log.write("HO INVIATO IL CODICE BATTERIA: " + codice_batt +" ED HO RICEVUTO:" + ritorno+"\n\n");
		       
		       if (ritorno.equals("true"))
		        	return true;
		       else
		    	   return false;
	}
	
	
	/**
	invia richiesta al servizio web per controllare la presenza della batteria nel db
	@param codice della batteria da controllare
	@param tempo è il timestamp di registrazione della batteri anel PLC
	@return true o false. True la batteria  può essere registrata, False va scartata
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	*/
	public String checkNonBloccante(String codice_batt, String tempo) throws InterruptedException, ExecutionException {
		 ExecutorService executor = Executors.newCachedThreadPool();
		    Future<String> futureCall = executor.submit(new Control());
		    String result = futureCall.get(); // Here the thread will be blocked 
		    // until the result came back.
		    return result;
	
	}//fine metodo check
	
	
	public class Control implements Callable<String> {
	    @Override
	    public String call() throws Exception {
	        
	        return new String("Result");
	    }
	}
	
	
		
	//controllo per le batterie che NON sono nella posizione 0 del BUFFER del PLC.
	//dovrebbe EFFETTUARE TUTTI I CONTROLLI POSSIBILI
	//non viene mai chiamata dalla postazione di controllo finale
public int controlloDbBatteria(Batteria batteria) throws Exception {
		
		String codice = "";
		String postazione = "";
		
	
			codice = batteria.getCodiceBatteria();
			postazione = batteria.getPostazione();
			int risposta = -1;  
			
		
			try {
				c_mysql = pool.getConnection();
				stmt_mysql = c_mysql.createStatement(); 
				stmt_mysql.setQueryTimeout(timeout_query);
				//log.write("CheckControl. BATTERIA: "+codice+ " POSTAZIONE:" + postazione + "  CHIEDO CONNESSIONE");
			  } catch (Exception e) {  
				log.write("CheckControl. ERRORE CREAZIONE CONNESSIONE CHECK \n");
		      	//System.err.println(e.toString());
		      	arrayBatteryStory.aggiungiBatteria(batteria);
		      	if (c_mysql != null) {
	                pool.returnConnection(c_mysql);
	            }
		      	return setting.ERRORE_INDEFINITO;
		           
		      }
		  
			  
			  
				//evito che le batterie saltino la postazione
				int numero_postazione = 0;  
			   
				numero_postazione = Integer.parseInt(postazione);
			  
			   //se esito ko, ritorno senza fare ulteriori controlli
			   if (batteria.getStatoBatteria().equals("0")) {
					arrayBatteryStory.aggiungiBatteria(batteria);
					if (c_mysql != null) {
		                pool.returnConnection(c_mysql);
		            }
					return setting.ESITO_KO_ISTANTANEO;
				}
		 
		  
		   ResultSet rs;
			try {
				
				
				rs = stmt_mysql.executeQuery("SELECT stato_test,postazione FROM linea2 where postazione<"+batteria.getPostazione()+" AND  codice='"+codice+"' order by data asc");
				//rs = stmt_mysql.executeQuery("SELECT stato_test,postazione FROM linea5 where  codice='"+codice+"' order by data asc");	
				int numero_occorrenze = 0;
				int ultima_postazione = 0;
				boolean pass = true;
				while (rs.next()){
					
					ultima_postazione = rs.getInt("postazione"); //ultima_postazione processata
					
					if (rs.getInt("stato_test")==0) {
						pass = false;	
					}
						
					else {
						numero_occorrenze +=1;
						pass = true;
					}
					
				}//fine while
				
				rs.close();
				
				if (!pass)  {
					//pool.returnConnection(c_mysql);
					arrayBatteryStory.aggiungiBatteria(batteria);
					return setting.SCARTO_PER_ESITO; //esito in una stazione precedente
				}
				
				
			
				if (numero_postazione == (numero_occorrenze + 1)) {
					//pool.returnConnection(c_mysql);
					arrayBatteryStory.aggiungiBatteria(batteria);
					return setting.BATTERIA_OK;
				}//fine if
				
							
				if (numero_postazione > (numero_occorrenze + 1)) {
					//pool.returnConnection(c_mysql);
					arrayBatteryStory.aggiungiBatteria(batteria);
					return setting.SCARTO_SALTO_STAZIONE;
					
				}
				  
				if (numero_postazione < (numero_occorrenze + 1)) {
					//pool.returnConnection(c_mysql);
					arrayBatteryStory.aggiungiBatteria(batteria);
					return setting.BATTERIA_GIA_PROCESSATA;
					
				}
			
			} catch (SQLException e) {
				log.write("---------------------> ERRORE IN CHECK. CONNESSIONE. POSSIBILE TIMEOUT: "+e.toString());
				//e.printStackTrace();
				arrayBatteryStory.aggiungiBatteria(batteria);
				return setting.SCARTO_TIMEOUT;
				
			} finally {
				
					//log.write("CheckControl. BATTERIA: "+codice+ " POSTAZIONE:" + postazione + "  RILASCIO CONNESSIONE \n");
					if (c_mysql != null) {
		                pool.returnConnection(c_mysql);
		            }
				
				//return setting.BYPASS;
			}
		    
			return setting.SCARTO_TIMEOUT;
		
		}//FINE METODO
		


	public int controlloDbBatteriaDoppione(Batteria batteria) {
		
		String codice = batteria.getCodiceBatteria();
		String postazione = batteria.getPostazione();
		int risposta = -1;  
		
		try {
			c_mysql = pool.getConnection();
			stmt_mysql = c_mysql.createStatement(); 
			stmt_mysql.setQueryTimeout(timeout_query);
			//log.write("CheckControl. BATTERIA: "+codice+ " POSTAZIONE:" + postazione + "  CHIEDO CONNESSIONE");
		  } catch (Exception e) {
			  if (c_mysql != null) {
	                pool.returnConnection(c_mysql);
	            }
			  log.write("ERRORE CREAZIONE STATEMENT CHECK DOPPIONE");
	          arrayBatteryStory.aggiungiBatteria(batteria);
	          return setting.BYPASS;
	      }//FINE CATCH
		  
		
		  
		
	   ResultSet rs;
		try {
			
			rs = stmt_mysql.executeQuery("SELECT * FROM linea2 where codice='"+codice+"' and postazione ="+ postazione + " order by data asc");
						
			int count =  0;
			while (rs.next()){
					count +=1;	
			}
		
			rs.close();
			
			if (count>0) {
				if (batteria.getStatoBatteria().equals("0")) {
					//arrayBatteryStory.aggiungiBatteria(batteria);  // perchè aggiungo ?   01/12/2020
					//pool.returnConnection(c_mysql);  //lo faccio in finally
					return setting.BATTERIA_KO_GIA_PROCESSATA_BUFFER;
				}
				else {
					//pool.returnConnection(c_mysql);
					return setting.BATTERIA_GIA_PROCESSATA_BUFFER;
				}
					
			}//fine if count
			else {
				//se sto qui la batteria NON è presente nel database in questa stazione
				//se esito ko, ritorno senza fare ulteriori controlli
				
				 if (batteria.getStatoBatteria().equals("0")) {
						arrayBatteryStory.aggiungiBatteria(batteria);
						return setting.ESITO_KO_ISTANTANEO;
				}
				
				
					arrayBatteryStory.aggiungiBatteria(batteria);
					return setting.BATTERIA_OK;
			  
				
			}//fine else
			
		
		} catch (Exception e) {
			log.write("ERRORE CONNESSIONE CHECK DOPPIONE. POSSIBILE TIMEOUT: "+e.toString());
			arrayBatteryStory.aggiungiBatteria(batteria);
			return setting.SCARTO_TIMEOUT;
			
		} finally {
			if (c_mysql != null) {
                pool.returnConnection(c_mysql);
            }
			//log.write("CheckControl. BATTERIA: "+codice+ " POSTAZIONE:" + postazione + "  RESTITUISCO CONNESSIONE\n");
		}
	    
	    //return setting.SCARTO_PER_ESITO;

	}//FINE METODO


	
	
	/**
	 * Metodo utilizzato per le postazioni di controllo
	 * Verifica se la batteria può passare oppure no
	 * Aggiunta modifica per la postazione 7
	 * @param  batteria
	 * @return valore intero che specifica su quale postazione si è verificato errore oppure se è ok. 1-10 ko postazione 1-10. 11-19 salto postazione. 0 ok
	 * @since 1.7
	 */	
public int isWaste(Batteria batteria) {
		
		String codice = batteria.getCodiceBatteria();
		String postazione = batteria.getPostazione();
		int risposta = -1;  
		
		try {
			c_mysql = pool.getConnection();
			stmt_mysql = c_mysql.createStatement(); 
			stmt_mysql.setQueryTimeout(timeout_query);
		  } catch (Exception e) {
			  log.write("ERRORE CREAZIONE STATEMENT CHECK iswaste");
			  if (c_mysql != null) {
	                pool.returnConnection(c_mysql);
	            }
	          return -1;
	      }//FINE CATCH
		  
		
		  
		
	   ResultSet rs;
		try {
			
			//r4 mi occorre per verificare il numero di riprocessamento del controllo corti
			rs = stmt_mysql.executeQuery("SELECT p1,p2,p3,p4,p5,p6,p7,p8,p9,r4 FROM batterie_linea2 where codice='"+codice+"' ");
			
			
			if (rs.next()) {
					
					int iVal = 0;
					iVal = rs.getInt("p1");
				    if (!rs.wasNull()) {
				    	 if (iVal == 0)
				    		return 1;
					}else
					{
						return 11;
					}
				    
						
				    iVal = rs.getInt("p2");
				    if (!rs.wasNull()) {
				    	 if (iVal == 0)
				    		return 2;
					}else
					{
						return 12;
					}
					
					
				    iVal = rs.getInt("p3");
				    if (!rs.wasNull()) {
				    	 if (iVal == 0)
				    		return 3;
					}else
					{
						return 13;
					}
					
					
				    iVal = rs.getInt("p4");
				    if (!rs.wasNull()) {
				    	 if (iVal == 0)
				    		return 4;
					}else
					{
						return 14;
					}
					
					
				    iVal = rs.getInt("p5");
				    if (!rs.wasNull()) {
				    	 if (iVal == 0)
				    		return 5;
					}else
					{
						return 15;
					}
					
					
				    iVal = rs.getInt("p6");
				    if (!rs.wasNull()) {
				    	 if (iVal == 0)
				    		return 6;
					}else
					{
						return 16;
					}
				    
				    if (batteria.getPostazione().equals("7")) {
						 return 0;
					}
					
					
				    iVal = rs.getInt("p7");
				    if (!rs.wasNull()) {
				    	 if (iVal == 0)
				    		return 7;
					}else
					{
						return 17;
					}
					
					
					
					iVal = rs.getInt("p8");
				    if (!rs.wasNull()) {
				    	 if (iVal == 0)
				    		return 8;
					}else
					{
						return 18;
					}
					
					
				    iVal = rs.getInt("p9");
				    if (!rs.wasNull()) {
				    	 if (iVal == 0)
				    		return 9;
					}else
					{
						return 19;
					}
				    
				    
				    //riprocessamenti postazione 4
				    iVal = rs.getInt("r4");
				    if (!rs.wasNull()) {
				    	 if (iVal > 1)
				    		return 100; //ritorno 100 se il secondo controllo corti è stato riprocessato
					}
					
				
					return 0;
			}
			
			rs.close();
			
			return -2;
			
		
		} catch (SQLException e) {
			log.write("CheckControl. Line521 ERRORE CONNESSIONE CHECK iswaste. POSSIBILE TIMEOUT: "+e.toString());
			return -1;
			
		} finally {
			if (c_mysql != null) {
                pool.returnConnection(c_mysql);
            }
		}
	    
	    //return setting.SCARTO_PER_ESITO;

	}//FINE METODO

	
	
//start_stop = true in avvio , false nello stop	
public int setMicroStop(boolean start_stop, Batteria batteria) throws UnknownHostException, IOException  {
	
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
			String nomestazione = batteria.getPostazione();
						
			ResultSet rs;
			int ultimo_id = -1;
			int stato = -1;
			
			
					rs = stmt_mysql.executeQuery("SELECT id,start_stop FROM fermi_linea2 where postazione="+nomestazione+"  ORDER by time desc limit 1 ");
					
					if (rs.next()) {
						//se esiste è stato creato per avvio
						ultimo_id = rs.getInt("id");
						stato = rs.getInt("start_stop");
					}//fine if rs ! null
			
				            
					int ss = 0;
					//se true è in start , se false è in stop
					if (start_stop) {
						ss = 1;
						//Setting.setData_aggiornamento(dat);
					}
					
					if (ultimo_id > 0) {
						//ESISTE UN RECORD
						if (stato==0) {
							//l'ultimo record è in stato di stop
							if(start_stop) 
								//l'ultimo record è in stato di stop e la stazione si è appena partita
								stmt_mysql.executeUpdate("UPDATE fermi_linea2 SET time_stop = '"+ dat + "', start_stop = 1 WHERE ID="+ ultimo_id);
							if(!start_stop) {
								//l'ultimo record è in stato di stop , devo impostare un nuovo stop
								//chiudo il precedente ed avvio il nuovo
								//stmt_mysql.executeUpdate("UPDATE fermi_linea5 SET time_stop = '"+ dat + "',start_stop = 0 WHERE ID="+ ultimo_id);
								stmt_mysql.executeUpdate("INSERT INTO fermi_linea2 (postazione,start_stop,time) VALUES " +
										" ("+nomestazione+",0,'"+dat+"'"+")");
							}//fine start_stop
							
						}//fine stato==1
						if (stato==1) {
							//l'ultimo record è in stato di start
							if(!start_stop) 
								//l'ultimo record è in stato di start e la stazione si è appena avviata
								stmt_mysql.executeUpdate("INSERT INTO fermi_linea2 (postazione,start_stop,time) VALUES " +
										" ("+nomestazione+",0,'"+dat+"'"+")");
						}//fine stato==1
						
						
					}else {
						//NON ESISTE UN RECORD
						if (ss==1)
							//NON CI SONO RECORD PRECEDENTI, INSERISCO LO STOP
							stmt_mysql.executeUpdate("INSERT INTO fermi_linea2 (postazione,start_stop,time) VALUES " +
									" ("+nomestazione+",1,'"+dat+"'"+")");
						else
							//NON CI SONO RECORD PRECEDENTI, INSERISCO LO START
							stmt_mysql.executeUpdate("INSERT INTO fermi_linea2 (postazione,start,time_stop) VALUES " +
									" ("+nomestazione+",0,'"+dat+"'"+")");
					}//fine else ultimo_id<0
						            				        
					
						       
					return 0;
				            
			} catch (Exception e) {
	          //System.out.println("Errore: " +  e.getMessage());
			  log.write("ERRORE trasmissione CheckControl Line625 - StartStop : " +e.toString()+"\n");
			  return -1;
	             
	      } finally {
	    	  if (c_mysql != null) {
	              pool.returnConnection(c_mysql);
	          }
	      }
	

}//fine microstop






}//fine classe
