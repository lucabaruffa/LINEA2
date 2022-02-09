package PLC;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JTextField;

import DB.DBCommand;
import Moka7.IntByRef;
import Moka7.S7;
import Moka7.S7Client;
import linea.AtomoConfigurazioneLinea;
import linea.Indicatore;
import linea.LoggerFile;
import linea.SendEmailOffice365;
import linea.Setting;

public class ConfiguratorePLc implements Runnable {

	
	private static Setting setting; 
	public static byte[] Buffer = new byte[65536]; // 64K buffer (maximum for S7400 systems)
    public static final S7Client Client = new S7Client();
    private int DataToMove=46; // contiene la dimensione del DB connesso
    private int DB = 155; // db
    private int CurrentStatus = S7.S7CpuStatusUnknown;
    
    private static int NUMERO_STAZIONI_ATTIVE = 0;
    private static LoggerFile log = new LoggerFile();
    
    public static String LT1_stabilitation_time ="-1";
    public static String LT2_stabilitation_time ="-1";
    public static String LT1_test_time ="-1";
    public static String LT2_test_time ="-1";
    public static String time5 ="-1"; 
    public static int  LT1_setpoint_pressure = -1;
    public static int  LT2_setpoint_pressure = -1;
    public static int  LT1_leak = -1;
    public static int  LT2_leak = -1;
    public static int  pressure5 = -1;
	
    public static float  HP_right_max = -1;
    public static float  HP_right_min = -1;
    public static float  HP_left_max = -1;
    public static float  HP_left_min = -1;
    public static float  measure5 = -1;
    
    public static String data_aggiornamento ="/";
    
    private static boolean running = true;
    private int sleep = 30000;  //1 min tempo di ciclo
    
    private static boolean modificato = false;
        
    private JTextField stato;
    
    private DBCommand comando_db ;
    
    
    
    
    public ConfiguratorePLc()  {
		
    }//fine costruttore
	
	
	public ConfiguratorePLc(JTextField stat)  {			
		stato = stat;
		comando_db = new DBCommand();
	}//fine costruttore
    
    /**
	 * @return the data_aggiornamento
	 */
	public static synchronized String getData_aggiornamento() {
		return data_aggiornamento;
	}

	/**
	 * @param data_aggiornamento the data_aggiornamento to set
	 */
	public static synchronized void setData_aggiornamento(String data_aggiornamento) {
		ConfiguratorePLc.data_aggiornamento = data_aggiornamento;
		
	}


	 
    
    /**
	 * @return the lT1_stabilitation_time
	 */
	public static synchronized String getLT1_stabilitation_time() {
		return LT1_stabilitation_time;
	}

	/**
	 * @param lT1_stabilitation_time the lT1_stabilitation_time to set
	 */
	public static synchronized void setLT1_stabilitation_time(String lT1_stabilitation_time) {
		//log.write("vecchio/nuovo = " + LT1_stabilitation_time + "/" + lT1_stabilitation_time );
		
		if (!lT1_stabilitation_time.equals(LT1_stabilitation_time)) modificato = true;
		
		LT1_stabilitation_time = lT1_stabilitation_time;
		
	}

	/**
	 * @return the lT2_stabilitation_time
	 */
	public static synchronized String getLT2_stabilitation_time() {
		return LT2_stabilitation_time;
	}

	/**
	 * @param lT2_stabilitation_time the lT2_stabilitation_time to set
	 */
	public static synchronized void setLT2_stabilitation_time(String lT2_stabilitation_time) {
		//log.write("vecchio/nuovo = " + LT2_stabilitation_time + "/" + lT2_stabilitation_time );
		if (!lT2_stabilitation_time.equals(LT2_stabilitation_time)) modificato = true;
		
		LT2_stabilitation_time = lT2_stabilitation_time;
	}

	/**
	 * @return the lT1_test_time
	 */
	public static synchronized String getLT1_test_time() {
		return LT1_test_time;
	}

	/**
	 * @param lT1_test_time the lT1_test_time to set
	 */
	public static synchronized void setLT1_test_time(String lT1_test_time) {
		//log.write("vecchio/nuovo = " + LT1_test_time + "/" + lT1_test_time );
		if (!lT1_test_time.equals(LT1_test_time)) modificato = true;
		LT1_test_time = lT1_test_time;
	}

	/**
	 * @return the lT2_test_time
	 */
	public static synchronized String getLT2_test_time() {
		return LT2_test_time;
	}

	/**
	 * @param lT2_test_time the lT2_test_time to set
	 */
	public static synchronized void setLT2_test_time(String lT2_test_time) {
		//log.write("vecchio/nuovo = " + LT2_test_time + "/" + lT2_test_time );
		if (!lT2_test_time.equals(LT2_test_time)) modificato = true;
		LT2_test_time = lT2_test_time;
	}

	/**
	 * @return the time5
	 */
	public static synchronized String getTime5() {
		return time5;
	}

	/**
	 * @param time5 the time5 to set
	 */
	public static synchronized void setTime5(String time5) {
		//log.write("vecchio/nuovo = " + ConfiguratorePLc.time5 + "/" + time5 );
		if (!time5.equals(ConfiguratorePLc.time5)) modificato = true;
		ConfiguratorePLc.time5 = time5;
	}

	/**
	 * @return the lT1_setpoint_pressure
	 */
	public static synchronized int getLT1_setpoint_pressure() {
		return LT1_setpoint_pressure;
	}

	/**
	 * @param lT1_setpoint_pressure the lT1_setpoint_pressure to set
	 */
	public static synchronized void setLT1_setpoint_pressure(int lT1_setpoint_pressure) {
		//log.write("vecchio/nuovo = " + LT1_setpoint_pressure + "/" + lT1_setpoint_pressure );
		if (lT1_setpoint_pressure!=LT1_setpoint_pressure) modificato = true;
		LT1_setpoint_pressure = lT1_setpoint_pressure;
	}

	/**
	 * @return the lT2_setpoint_pressure
	 */
	public static synchronized int getLT2_setpoint_pressure() {
		return LT2_setpoint_pressure;
	}

	/**
	 * @param lT2_setpoint_pressure the lT2_setpoint_pressure to set
	 */
	public static synchronized void setLT2_setpoint_pressure(int lT2_setpoint_pressure) {
		//log.write("lt2 vecchio/nuovo = " + LT2_setpoint_pressure + "/" + lT2_setpoint_pressure );
		if (lT2_setpoint_pressure!=LT2_setpoint_pressure) modificato = true;
		LT2_setpoint_pressure = lT2_setpoint_pressure;
	}

	/**
	 * @return the lT1_leak
	 */
	public static synchronized int getLT1_leak() {
		
		return LT1_leak;
	}

	/**
	 * @param lT1_leak the lT1_leak to set
	 */
	public static synchronized void setLT1_leak(int lT1_leak) {
		//log.write("leak 1 vecchio/nuovo = " + LT1_leak + "/" + lT1_leak );
		if (lT1_leak!=LT1_leak) modificato = true;
		LT1_leak = lT1_leak;
	}

	/**
	 * @return the lT2_leak
	 */
	public static synchronized int getLT2_leak() {
		return LT2_leak;
	}

	/**
	 * @param lT2_leak the lT2_leak to set
	 */
	public static synchronized void setLT2_leak(int lT2_leak) {
		//log.write("vecchio/nuovo = " + LT2_leak + "/" + lT2_leak );
		if (lT2_leak!=LT2_leak) modificato = true;
		LT2_leak = lT2_leak;
	}

	/**
	 * @return the pressure5
	 */
	public static synchronized int getPressure5() {
		return pressure5;
	}

	/**
	 * @param pressure5 the pressure5 to set
	 */
	public static synchronized void setPressure5(int pressure5) {
		//log.write("pressure vecchio/nuovo = " + ConfiguratorePLc.pressure5 + "/" + pressure5 );
		if (pressure5!=ConfiguratorePLc.pressure5) modificato = true;
		ConfiguratorePLc.pressure5 = pressure5;
	}

	/**
	 * @return the hP_right_max
	 */
	public static synchronized float getHP_right_max() {
		return HP_right_max;
	}

	/**
	 * @param hP_right_max the hP_right_max to set
	 */
	public static synchronized void setHP_right_max(float hP_right_max) {
		//log.write("vecchio/nuovo = " + HP_right_max + "/" + hP_right_max );
		if (hP_right_max!=HP_right_max) modificato = true;
		HP_right_max = hP_right_max;
	}

	/**
	 * @return the hP_right_min
	 */
	public static synchronized float getHP_right_min() {
		return HP_right_min;
	}

	/**
	 * @param hP_right_min the hP_right_min to set
	 */
	public static synchronized void setHP_right_min(float hP_right_min) {
		//log.write("hp r min vecchio/nuovo = " + HP_right_min + "/" + hP_right_min );
		if (hP_right_min!=HP_right_min) modificato = true;
		HP_right_min = hP_right_min;
	}

	/**
	 * @return the hP_left_max
	 */
	public static synchronized float getHP_left_max() {
		return HP_left_max;
	}

	/**
	 * @param hP_left_max the hP_left_max to set
	 */
	public static synchronized void setHP_left_max(float hP_left_max) {
		//log.write("hp l max vecchio/nuovo = " + HP_left_max + "/" + hP_left_max );
		if (hP_left_max!=HP_left_max) modificato = true;
		HP_left_max = hP_left_max;
	}

	/**
	 * @return the hP_left_min
	 */
	public static synchronized float getHP_left_min() {
		return HP_left_min;
	}

	/**
	 * @param hP_left_min the hP_left_min to set
	 */
	public static synchronized void setHP_left_min(float hP_left_min) {
		//log.write("hp l min vecchio/nuovo = " + HP_left_min + "/" + hP_left_min );
		if (hP_left_min!=HP_left_min) modificato = true;
		HP_left_min = hP_left_min;
	}

	/**
	 * @return the measure5
	 */
	public static synchronized float getMeasure5() {
		return measure5;
	}

	/**
	 * @param measure5 the measure5 to set
	 */
	public static synchronized void setMeasure5(float measure5) {
		//log.write("measure vecchio/nuovo = " + ConfiguratorePLc.measure5 + "/" + measure5 );
		if (measure5!=ConfiguratorePLc.measure5) modificato = true;
		ConfiguratorePLc.measure5 = measure5;
	}


	
	
	public void inizializza() throws Exception {
		
		try {
			caricaConfigurazione();
		} catch (Exception e) {
			log.write("\n------->Errore caricamento configurazione ConfiguratorePlc <---------------------\n");
			
		}
		
		try {
			setting = new Setting();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		connetti();
		
	}//fine inizializza
	
	public void stop()
	{
		running = false;
	}
	
	
	
	@Override
	public void run(){
		log.write("AVVIO RUN Configuratore PLC");
		while(running) {
			//monitor.append("RUNNING\n");
			if(Client.Connected) {
				
				stato.setBackground(Setting.verde);
				stato.setText("OK");
				
				long startTime = System.nanoTime();
				scanReader();	
				
				//se è stata apportata una modifica alla configurazione
				if (modificato) {
					log.write("\n-----------------> CONFIGURAZIONE DEL PLC MODDIFICATA <---------------------\n");
					try {
						comando_db.setConfigurationChanged();
						try {
							salvaConfigurazione();
						} catch (Exception e) {
							log.write("\n------->Errore salvataggio configurazione ConfiguratorePlc <---------------------\n");
							
						}
					} catch (Exception e) {
						log.write("ConfiguratorePLc -> ERRORE INVIO DB CONFIGURATORE PLC MODIFICATA :" + e.toString());
						e.printStackTrace();
					} 
					modificato = false;
				}
				
				
				
				long endTime = System.nanoTime();
				//monitor.append("\nPostazione "+ nomeStazione + ". Tempo esecuzione = "+ (endTime - startTime)/1000000+" millisecondi");  
			}//fine if
			else {
				log.write("CONFIGURATORE PLC NON CONNESSO! - AVVIO TENTATIVO DI CONNESSIONE");
				stato.setBackground(Setting.rosso);
				stato.setText("KO");
				connetti();	
			}
			
			try {
				//monitor.append("slider ="+sleep+"\n");
				Thread.sleep(sleep);
			}catch(Exception e) {
				log.write("\nConfiguratore plc Errore inSleep!\n");
				stato.setBackground(Setting.rosso);
				stato.setText("KO");
			}//fine catch
			
		}//fine while
       
	
    
	}//fine run
	
	
	
	
	
	public void scanReader()  {
		
		
		try {
				
				int indirizzo_start = 0;
				DBRead(indirizzo_start);
				
				
				ConfiguratorePLc.setLT1_stabilitation_time(""+S7.GetDIntAt(Buffer, 0));
	        	//log.write("\n\n-----------------> CONFIGURATOR LINEA FOR : LT1_stabilitation_time " +LT1_stabilitation_time+" ms");
				ConfiguratorePLc.setLT2_stabilitation_time(""+S7.GetDIntAt(Buffer, 4));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : LT2_stabilitation_time " +LT2_stabilitation_time+" ms ");
				ConfiguratorePLc.setLT1_test_time(""+S7.GetDIntAt(Buffer, 8));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : LT1_test_time " +LT1_test_time+" ms");
				ConfiguratorePLc.setLT2_test_time(""+S7.GetDIntAt(Buffer, 12));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : LT2_test_time " +LT2_test_time+" ms");
				ConfiguratorePLc.setTime5(""+S7.GetDIntAt(Buffer, 16));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : time5 " +time5+" ms");	
				ConfiguratorePLc.setLT1_setpoint_pressure(LT1_setpoint_pressure = S7.GetWordAt(Buffer, 20));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : LT1_setpoint_pressure " +LT1_setpoint_pressure+"");
				ConfiguratorePLc.setLT2_setpoint_pressure(S7.GetWordAt(Buffer, 22));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : LT2_setpoint_pressure " +LT2_setpoint_pressure+"");
				ConfiguratorePLc.setLT1_leak(S7.GetWordAt(Buffer, 24));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : LT1_leak " +LT1_leak+" ");
				ConfiguratorePLc.setLT2_leak(S7.GetWordAt(Buffer, 26));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : LT2_leak " +LT2_leak+" ");
				ConfiguratorePLc.setPressure5(S7.GetWordAt(Buffer, 28));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : pressure5 " +pressure5+"");   	
				ConfiguratorePLc.setHP_right_max(S7.GetFloatAt(Buffer, 30));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : HP_right_max " +HP_right_max+"");
				ConfiguratorePLc.setHP_right_min(S7.GetFloatAt(Buffer, 34));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : HP_right_min " +HP_right_min+"");
				ConfiguratorePLc.setHP_left_max(S7.GetFloatAt(Buffer, 38));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : HP_left_max " +HP_left_max+"");
				ConfiguratorePLc.setHP_left_min(S7.GetFloatAt(Buffer, 42));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : HP_left_min " +HP_left_min+"");
				ConfiguratorePLc.setMeasure5(S7.GetFloatAt(Buffer, 46));
	        	//log.write("-----------------> CONFIGURATOR LINEA FOR : measure5 " +measure5+"  \n\n");
				
				
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat ier = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				ConfiguratorePLc.setData_aggiornamento(ier.format(date));
				
				
			
		}catch(Exception e) {
			log.write("-------------------------->Errore DBAREA:"+e.toString()+"\n");
		}
		
		
		
	}//fine scan
	
	
	
	
	
	public void DBRead(int start)
    {
		//log("\nREADAREA - DB =" + DB +" datatomove="+DataToMove+"\n" );
		int Result = Client.ReadArea(S7.S7AreaDB, DB, start, DataToMove, Buffer);
		
        if (Result==0)
        {
        	//log("\nDBAREA OK.  DB="+DB+"\n" );
        }else
        {
        	log.write("ConfiguratorePLC -> PROBLEMA CON READAREA!!!!!!!!! ERRORE =" + Result +". db="+DB);
        }
       
    }      
    
    public void DBWrite()
    {
    	
    	int Result = Client.WriteArea(S7.S7AreaDB, DB, 0, (NUMERO_STAZIONI_ATTIVE * DataToMove) , Buffer);
        if (Result==0)
        {
        	log.write("DB "+DB+" succesfully written using size reported by DBwrite() - Modulo configuratoreLinea");
        }else
        	log.write("DB "+DB+" errore dbwrite()  - Modulo configuratoreLinea");
    	
    }  
    
        
    
    public void ShowStatus()
    {
    	try {
	        IntByRef PlcStatus = new IntByRef(S7.S7CpuStatusUnknown);
	        int Result = Client.GetPlcStatus(PlcStatus);
	        if (Result==0)
	        {
	            System.out.print("PLC Status : ");
	            switch (PlcStatus.Value)
	            {
	                case S7.S7CpuStatusRun :
	                    //monitor.append("RUN");
	                    //statoplc.setText("RUNNING");
	                    //statoplc.setBackground(new Color(50, 168, 82));
	                    break;
	                case S7.S7CpuStatusStop :
	                	//monitor.append("STOP");
	                	//statoplc.setText("STOP");
	                	//statoplc.setBackground(new Color(219, 22, 65));
	                    break;
	                default :    
	                	//log("Unknown ("+PlcStatus.Value+")");
	                	//statoplc.setText("SCONOSCIUTO");
	            }
	        }//fine if
	        CurrentStatus = PlcStatus.Value;
    	}
	        catch(Exception e) {
	        	log("Errore in SHOWSTATUS: " + e.toString());
	        }
        
    }//fine showstatus
    
    
    private void log(String l) {
    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	log.write(timestamp+":"+ l + "   - modulo ConfiguratorePLC\n");
    }
    
    public void connetti() {
    	if (setting.getConnectionType().equals("OP"))
		{
			Client.SetConnectionType(S7.OP);
			log("TIPO CONNESSIONE: OP\n");
			}
		
    	if (setting.getConnectionType().equals("PG")) {
			Client.SetConnectionType(S7.PG);
			log("TIPO CONNESSIONE: PG\n");
		}
    	if (setting.getConnectionType().equals("S7_BASIC")) {
			Client.SetConnectionType(S7.S7_BASIC);
			log("TIPO CONNESSIONE: S7_BASIC\n");
			}
    	
    	log("IP:" + setting.getIPPLC()+"\n");
		int Result = Client.ConnectTo(setting.getIPPLC(), Integer.valueOf(setting.getRACK()), Integer.valueOf(setting.getSLOT()));
    	if (Result==0)
    	{
    		
    		//indicatore.statoplc.setText("CONNESSO");
    		//indicatore.statoplc.setBackground(setting.verde);
    		log.write("--------------------->CONNESSIONE RIUSCITA! - MODULO: configuratorePLC");
    		//statoplc.setText("CONNESSIONE OK\n");
            
    		//monitor.append("PDU negotiated: " + Client.PDULength()+" bytes" );
    		//System.out.println("Connected to   : " + setting.getIPPLC() + " (Rack=" + Integer.valueOf(RACKtext.getText()) + ", Slot=" + Integer.valueOf(SLOTtext.getText())+ ")");
           // System.out.println("PDU negotiated : " + Integer.valueOf(SLOTtext.getText()));
            //statoplc.setText("CONNESSIONE OK!");
    	}else {
    		log.write("----------------------->CONNESSIONE FALLITA AL PLC! - MODULO: configuratorePLC");
    		//indicatore.statoplc.setText("ERRORE");
    		//indicatore.statoplc.setBackground(setting.rosso);
    		//statoplc.setBackground(new Color(219, 22, 65));
    	}
    }//fine connetti
    
    
    
   
    
    public void setListaAtomoConfigurazione(AtomoConfigurazioneLinea[] lista) {
    	
    	//NUMERO_STAZIONI_ATTIVE = 7; //da caricare dinamicamente
    	
    	log.write("\nSALVO LA CONFIGURAZIONE. ATTIVE:"+NUMERO_STAZIONI_ATTIVE+" - MODULO: configuratoreLinea");
    	
    	
    	int OFFSET_DB_LINEA = 4; //byte
    	int indirizzo_start = 0;
    	
    	try {
    	
    	for(int i=0;i<NUMERO_STAZIONI_ATTIVE;i++) {
    		
    		indirizzo_start = (OFFSET_DB_LINEA * i);
    		AtomoConfigurazioneLinea atomo = lista[i];
    		
    		boolean setbit_scanner = false;
    		boolean setbit_scarto = false;
    		
    		//if (atomo.scartoabilitato<200) atomo.scartoabilitato = 0x11000000;
    		//else atomo.scartoabilitato = 10;
    		
    		    		
    		S7.SetWordAt(Buffer, indirizzo_start , atomo.statoscanner);
    		indirizzo_start +=1;
    		
    		S7.SetWordAt(Buffer, indirizzo_start , atomo.scartoabilitato);
    		indirizzo_start +=1;
    		
    		//spare1
    		S7.SetWordAt(Buffer, indirizzo_start , atomo.spare1);
    		indirizzo_start +=1;
    		//spare2
    		S7.SetWordAt(Buffer, indirizzo_start , atomo.spare2);
    		indirizzo_start +=1;
    		
    		
			
			
    		log.write("HO SCRITTO SUL PLC: statoscanner:"+atomo.statoscanner+" - scartoabilitato:"+atomo.scartoabilitato+" - OFFSET ="+(i+1)+" - Modulo ConfiguratoreLinea");	
			
    		
    	}//fine for
    	
    	DBWrite(); //inizio, dimensioni
    	
    	}catch(Exception e) {
    		log.write("\nErrore DBAREA:"+e.toString()+"  --  inizio:"+indirizzo_start+"   - Modulo: ConfiguratoreLinea");
		}
    	
    	log.write("CONFIGURAZIONE SALVATA - Modulo: ConfiguratoreLinea");
    	
    }//fine scrivi
    
    
    private byte[] intToBytes( final int i ) {
        ByteBuffer bb = ByteBuffer.allocate(4); 
        bb.putInt(i); 
        return bb.array();
    }
    
    
    public void salvaConfigurazione() throws Exception
	{
		
			File configFile = new File("configurazione/plc_status.xml");
			
		    Properties props = new Properties();
		    props.setProperty("data", ConfiguratorePLc.getData_aggiornamento());
		    
		    props.setProperty("LT1_stabilitation_time", ConfiguratorePLc.getLT1_stabilitation_time());
		    props.setProperty("LT2_stabilitation_time", ConfiguratorePLc.getLT2_stabilitation_time());
		    
		    props.setProperty("LT1_test_time", ConfiguratorePLc.getLT1_test_time());
		    props.setProperty("LT2_test_time", ConfiguratorePLc.getLT2_test_time());
		    
		    props.setProperty("Time5", ConfiguratorePLc.getTime5());
		   
		    props.setProperty("LT1_setpoint_pressure", ""+ConfiguratorePLc.getLT1_setpoint_pressure());
		    props.setProperty("LT2_setpoint_pressure", ""+ConfiguratorePLc.getLT2_setpoint_pressure());
		   
		    props.setProperty("LT1_leak", ""+ConfiguratorePLc.getLT1_leak());
		    props.setProperty("LT2_leak", ""+ConfiguratorePLc.getLT2_leak());
		    
		    props.setProperty("Pressure5", ""+ConfiguratorePLc.getPressure5());
		   
		    props.setProperty("HP_right_max", ""+ConfiguratorePLc.getHP_right_max());
		    props.setProperty("HP_right_min", ""+ConfiguratorePLc.getHP_right_min());
		    props.setProperty("HP_left_max", ""+ConfiguratorePLc.getHP_left_max());
		    props.setProperty("HP_left_min", ""+ConfiguratorePLc.getHP_left_min());
		    props.setProperty("Measure5", ""+ConfiguratorePLc.getMeasure5());
		    
					    
		    OutputStream outputStream = new FileOutputStream(configFile);
		    props.storeToXML(outputStream, "CONFIGURAZIONE PARAMETRI LINEA 5");
			outputStream.close();
		
		
	}//fine classe
	

	
	public void caricaConfigurazione() throws Exception
	{
		
		File configFile = new File("configurazione/plc_status.xml");
		InputStream inputStream = new FileInputStream(configFile);
		Properties props = new Properties();
		
		if (!configFile.exists()){
			log.write("\n\n------>CARICA CONFIGURAZIONE NON ESISTE IL FILE LO CREO -----------");
			salvaConfigurazione();
		   }else{
			   try {
				   log.write("\n\n------>ESISTE IL FILE LO APRO -----------");
										
					props.loadFromXML(inputStream);
					
					ConfiguratorePLc.data_aggiornamento = (props.getProperty("data"));
					
					ConfiguratorePLc.LT1_stabilitation_time = (props.getProperty("LT1_stabilitation_time"));
					ConfiguratorePLc.LT2_stabilitation_time = (props.getProperty("LT2_stabilitation_time"));
					
					ConfiguratorePLc.LT1_test_time = (props.getProperty("LT1_test_time"));
					ConfiguratorePLc.LT2_test_time = (props.getProperty("LT2_test_time"));
					
					ConfiguratorePLc.time5 = (props.getProperty("Time5"));
					
					
					ConfiguratorePLc.LT1_setpoint_pressure = Integer.parseInt((props.getProperty("LT1_setpoint_pressure")));
					ConfiguratorePLc.LT2_setpoint_pressure = Integer.parseInt((props.getProperty("LT2_setpoint_pressure")));
					
					
					ConfiguratorePLc.LT1_leak = (Integer.parseInt(props.getProperty("LT1_leak")));	  
					ConfiguratorePLc.LT2_leak = (Integer.parseInt(props.getProperty("LT2_leak")));
					
					ConfiguratorePLc.pressure5 = ((Integer.parseInt(props.getProperty("Pressure5"))));
					
					ConfiguratorePLc.HP_right_max = ((Float.parseFloat(props.getProperty("HP_right_max"))));
					ConfiguratorePLc.HP_right_min = ((Float.parseFloat(props.getProperty("HP_right_min"))));
					ConfiguratorePLc.HP_left_max = ((Float.parseFloat(props.getProperty("HP_left_max"))));
					ConfiguratorePLc.HP_left_min = ((Float.parseFloat(props.getProperty("HP_left_min"))));
					
					ConfiguratorePLc.measure5 = ((Float.parseFloat(props.getProperty("Measure5"))));
					
					
				    inputStream.close();
				    
				    log.write("\n\n------>FINITO DI APRIRE -----------");
				} catch (Exception ex) {
				    System.out.println(ex.toString());
				    log.write("\n\n------>ERRORE APERTURA FILE DI CONFIGURAZIONE due-----------: " + ex.toString());
				
				} 
		   }//fine else creazione
		 
		
	}//fine caricaConfigurazione
    
	
	
}//fine classe

