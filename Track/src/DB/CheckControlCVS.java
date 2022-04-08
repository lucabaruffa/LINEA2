package DB;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.json.JSONObject;

import PLC.ConfiguratorePLc;
import linea.ArrayBatteriePostazione;
import linea.ArrayBatteryStory;
import linea.Batteria;
import linea.CalcoloFermi;
import linea.Giustificativo;
import linea.LoggerFile;
import linea.Setting;


//faccio un controllo sulle batterie presente nel file CSV. 
//faccio un ulteriore controllo sul fermo della linea (per impostare fermo)


public class CheckControlCVS extends TimerTask{
	
	//private Indicatore indicatore = new Indicatore();
	public 	Connection c_mysql;
	public Statement stmt_mysql ;
	private Setting setting;
	private DBConnectionPool pool;
	//private ArrayBatteryStory arrayBatteryStory;
	//private int timeout_query = 6000;
	private static String path = Setting.path;
	private static String dir = Setting.dir;
	
	private static LoggerFile log = new LoggerFile();
	GregorianCalendar offset_data = new GregorianCalendar();
	
	
	
	public CheckControlCVS() {
		
		try {
			setting = new Setting();
		} catch (Exception e) {
			log.write("ERRORE CARICAMENTO CONFIGURAZIONE IN CHECK");
			e.printStackTrace();
		}
		
		//arrayBatteryStory = new ArrayBatteryStory();
				
		//inizializzo pool connessioni db
		pool = new DBConnectionPool();
		
	}//fine costruttore
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("CheckControlCVS chiamato. Running");
		try {
			trasmetti();
		} catch (Exception e) {
			System.out.println("Ho riscontrato errore: " + e.toString());
			e.printStackTrace();
		}
		
	}//fine run
	
		
	public void trasmetti() throws UnknownHostException, IOException  {
		
		
		
		try {
			 c_mysql = pool.getConnection();
			 stmt_mysql = c_mysql.createStatement();
			 
	        //log.write("SENDERDB.   CHIEDO CONNESSIONE");
		  } catch (Exception e) {
			  log.write("CheckControllCVS. Errore getConnection: " +  e.toString());
	          
	          if (c_mysql != null) {
	              pool.returnConnection(c_mysql);
	          }//fine if
	          
	          //se sono qui non ho rte lan. E' inutile proseguire
	          return;
	             
	      }//fine catch
		
		
             
			// itero sul file CVS
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			SimpleDateFormat sdf_solodata = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Date date = new Date(System.currentTimeMillis());
			
			//String data_fermo_linea = sdf2.format(date);
			String dax = sdf.format(date);
			path = dir + Setting.nome_CSV;
			String path_rename = dir + ""+dax+ Setting.nome_CSV_rinominato;
			
			// Scrittura di dati da file
			FileReader fileInput;
			BufferedReader bufferReader;
			
			boolean completato = true;
			
			try {
							// Lettura da file
							fileInput = new FileReader(path);
							bufferReader = new BufferedReader(fileInput);
				 
							String s = bufferReader.readLine();
							while (s != null) {				
								
								s = bufferReader.readLine();
								
								System.out.println("riga letta:<"+s+">\n");
								
								if (s!=null) {
								
										String codice = s.split(",")[0];
										String nomestazione = s.split(",")[1];
										int stato = Integer.parseInt(s.split(",")[2]);
							        	String dat =  s.split(",")[3];
							            int valore1 = Integer.parseInt(s.split(",")[4]);
							            int valore2 = Integer.parseInt(s.split(",")[5]);
							            String dettaglio_errore ="";
							            
							            if (s.split(",").length>6)
							            	dettaglio_errore = s.split(",")[6];
							            
								         try {   
								           //HO DICHIARATO NEL DATABASE UNIQUE(CODICE,POSTAZIONE,DATA)
								           //IN MODO DA EVITARE DUPLICAZIONI
								        	 stmt_mysql.executeUpdate("INSERT INTO "+Setting.TABLE_LINEA+" (codice,postazione,stato_test,data,valore1,valore2,dettaglio) VALUES " +
								           		  				   " ('"+codice+"',"+nomestazione+","+stato+",'"+dat+"',"+valore1+","+valore2+",'"+dettaglio_errore+"') ");
								        	//DOPO INSERIMENTO NEL DB CANCELLO LA BATTERIA IN CODA
								            //arraybatteria.cancellaBatteria(batteria);
								        	 completato = false; 
								        	 
								        	 //System.out.println("riga inserita codice:"+codice+"\n");
								        	 
								         }catch(Exception h) {
								        	 //log.write("CheckControll CVS. La batteria � stata gi� inserita dal buffer  ");	
								        	 System.out.println("riga gi� inserita\n");
								         }//fine catch
								}//fine riga vuota
						         
								
							}//fine while del iterazione
							
							
									 
							bufferReader.close();
							fileInput.close();
							
							if (completato) {
								//se ho sincronizzato tutto potrei cancellare il file. in realta lo rinomino per futuri controlli
								
								System.out.println("E' tutto completo! Rinomino\n");
								path = dir + Setting.nome_CSV;
								File f1 = new File(path);
								File f2 = new File(path_rename);
								
								if(f1.renameTo(f2))
									log.write("CheckControll CVS. E' tutto completo! Rinominato \n");	
									
							}//fine completato
				 
			} catch (IOException e) {
							//log.write("CheckControll CSV. Non c'e' nulla da sincronizzare! ->" + e.toString());
							//System.out.println("CheckControll CSV. Forse non c'e' nulla da sincronizzare! Error :" + e.toString());
			} 
			
			
			//verifico fermo linea
			try {
				
				
				String tempo_fermo = Setting.getData_aggiornamento();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				LocalDateTime second = LocalDateTime.parse(tempo_fermo, formatter);
				
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");				
				String dax2 = sdf2.format(date);
			   
			    long diff = second.until(now, ChronoUnit.MINUTES);
			    			    
			    if (diff>Setting.tempoMaxLineaFerma) {
			    	
			    	//calcolo giustificativo automatico
			    	String giustificativo = CalcoloFermi.calcolaMotivoFermo(diff);
			    	log.write("Checkcontrol csv motivazione evidon: "+giustificativo);
			    	
			    	String fermo[]= giustificativo.split("@");
			    	String fermi = fermo[0];
			    	String descrizione_fermo = fermo[1];
			    	
			    	//log.write("Checkcontrol csv motivazione fermi: "+fermi+"   -> descrizione:"+descrizione_fermo);
			    	
			    	//controllo se esiste il record. Lo aggiorno in caso positivo, altrimenti lo inserisco
			    	ResultSet rs = stmt_mysql.executeQuery("SELECT * FROM "+Setting.DB_TABLE_STOP_LINEA+" where start='"+tempo_fermo+"' AND linea="+Setting.DB_BATTERIE_NUM_LINEA+" limit 1");
			    	if (rs.next()){
			    		int ID = rs.getInt("ID");
			    		String giustificativo_inserito = rs.getString("motivo_fermo");
			    		
			    		if (!giustificativo_inserito.equals("DA GIUSTIFICARE"))
			    			stmt_mysql.executeUpdate("UPDATE "+Setting.DB_TABLE_STOP_LINEA+" SET stop = '"+dax2+"', minuti_differenza="+diff+" WHERE ID="+ID+";");
			    		else
			    			stmt_mysql.executeUpdate("UPDATE "+Setting.DB_TABLE_STOP_LINEA+" SET stop = '"+dax2+"', minuti_differenza="+diff+",motivo_fermo='"+fermi+"',desc_motivo_fermo='"+descrizione_fermo+"' WHERE ID="+ID+";");
			    		
			    		rs.close();
			    		
			    		
			    	}//fine if rs
			    	else {
			    		rs.close();
			    		
			    		int turno = -1;
			    		int ora = -1;
			    		
				    	try {
				    		
					    		offset_data.setTime((sdf_solodata.parse(tempo_fermo)));
					    	    ora = offset_data.get(Calendar.HOUR_OF_DAY);
					    			
								if ((ora>=6) && (ora<14) ) turno=1;
								if ((ora>=14) && (ora<22) ) turno=2;
								if ((ora>=22) || (ora<6) ) 	turno=3;	
									
							} catch (Exception e) {
								log.write("CheckControll CSV. Calcolo del turno  error:" + e.toString());
								System.out.println("CheckControll CSV. Calcolo del turno a error:" + e.toString());
							}
			 
				   		log.write("CheckControll CSV. Calcolo del turno:" + turno + "  -> data di start:" + tempo_fermo + "  -> ora:" + ora );
				   		System.out.println("CheckControll CSV. Calcolo del turno:" + turno);
			    		
			    		stmt_mysql.executeUpdate("INSERT INTO "+Setting.DB_TABLE_STOP_LINEA+" (linea,start,stop,minuti_differenza,motivo_fermo,desc_motivo_fermo,operatore,turno) VALUES ("+Setting.DB_BATTERIE_NUM_LINEA+",'"+tempo_fermo+"','"+dax2+"',"+diff+",'"+fermi+"','"+descrizione_fermo+"','EVIDON',"+turno+");");
			    		
			    	}//fine else
			    	
			    	log.write("CheckControll CSV. Linea ferma da pi� di " + diff + " minuti. Lo segnalo con motivo =" + fermi);
			    	
			    	//stmt_mysql.executeUpdate("INSERT INTO stop_linea5 (linea,start,stop,minuti_differenza) VALUES (5,'"+tempo_fermo+"','"+dax2+"',"+diff+") ON DUPLICATE KEY UPDATE stop = '"+dax2+"', minuti_differenza="+diff+";");
			    }//fine if segnalazione
			    
			    
			    try {
			    	//invio dati per conteggio batterie
			    	new DBCommand().inserisciReport();
			    } catch (Exception e) {
					log.write("CheckControll CSV. Errore inserimento statistiche:" + e.toString());
					System.out.println("CheckControll CSV. Calcolo del turno a error:" + e.toString());
				}
			    
			    
			 
				
			}catch(Exception j) {
				log.write("CheckControll CSV. errore segnalazione linea in stop. Err:" + j.toString());
			}
			
			finally {
				 	  if (c_mysql != null)    pool.returnConnection(c_mysql);
					          
			}
		 
	
	}//fine trasmetti


	
	


}//fine classe
