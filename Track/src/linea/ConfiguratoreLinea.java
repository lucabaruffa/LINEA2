package linea2;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.concurrent.CopyOnWriteArrayList;

import Moka7.IntByRef;
import Moka7.S7;
import Moka7.S7Client;

public class ConfiguratoreLinea {

	
	private static Setting setting; 
	public static byte[] Buffer = new byte[65536]; // 64K buffer (maximum for S7400 systems)
    public static final S7Client Client = new S7Client();
    public int DataToMove=4; // contiene la dimensione del DB connesso
    private int DB66 = 101; // db
    private int CurrentStatus = S7.S7CpuStatusUnknown;
    private static AtomoConfigurazioneLinea[] dateList;// = new AtomoConfigurazioneLinea[10];
    private Indicatore indicatore = new Indicatore();
    private static int NUMERO_STAZIONI_ATTIVE = 0;
    private static LoggerFile log = new LoggerFile();
    private int DB_CONF = Setting.DB_CONFIGURAZIONI_ABILITAZIONI_PLC;
	
	public ConfiguratoreLinea() throws Exception {
	
			if (dateList == null) inizializza();
			else {
				connetti();
				scanReader();
			}
					
		
	}//fine costruttore
	
	public void inizializza() throws Exception {
		
		setting = new Setting();
		
		NUMERO_STAZIONI_ATTIVE = Integer.parseInt(setting.getNumeroStazioniAttive());
	
		if (dateList==null)
			dateList = new AtomoConfigurazioneLinea[50];			
				
		connetti();
		
		scanReader();
		
	}//fine inizializza
	
	
	
	public void scanReader()  {
		
		int OFFSET_DB_LINEA = 4; //byte
		int statoscanner = 0; //non abilitato
		int scartoabilitato = 0; //non abilitato
		int spare1 = 0; //non abilitato
		int spare2 = 0; //non abilitato

		
		try {
			for(int i=0;i<NUMERO_STAZIONI_ATTIVE;i++) {
				
				int indirizzo_start = (OFFSET_DB_LINEA * i);
				
				
				DBRead(indirizzo_start);
				
				
				statoscanner = S7.GetWordAt(Buffer, 0);//leggo 256 se impostato a 1, 0 se impostato a 0
	        	if (statoscanner>250) statoscanner = 1; //abilitato
	        	else statoscanner = 0;
	        	
	        	
	        	scartoabilitato = S7.GetWordAt(Buffer, 1);//leggo 256 se impostato a 1, 0 se impostato a 0
	        	if (scartoabilitato >250) scartoabilitato = 1; //abilitato
	        	else scartoabilitato  = 0;
	        	
	        	
	        	spare1 = S7.GetWordAt(Buffer, 2);//leggo 256 se impostato a 1, 0 se impostato a 0
	        	if (spare1 >250) spare1 = 1; //abilitato
	        	else spare1  = 0;
	        	
	        	
	        	spare2 = S7.GetWordAt(Buffer, 3);//leggo 256 se impostato a 1, 0 se impostato a 0
	        	if (spare2 >250) spare2 = 1; //abilitato
	        	else spare2  = 0;
	        	
	        	AtomoConfigurazioneLinea atomo = new AtomoConfigurazioneLinea(statoscanner,scartoabilitato,spare1,spare2);
	        	
	        	//log.write("CONFIGURATOR LINEA FOR : PRIMA DEL CONTROLLO. i=" + i + "  - postazione di controllo 2=" + (Setting.DB_POSTAZIONE_CONTROLLO2 -1));
	        	if (i==(NUMERO_STAZIONI_ATTIVE-1))
	        		dateList[(Setting.STAZIONE_DI_CONTROLLO_2) - 1] = atomo;
	        	else
	        		dateList[i] = atomo;
	        	
	        	log.write("CONFIGURATOR LINEA FOR : statoscanner: "+ i+" -> "+statoscanner +"  - scarto abilitato:"+scartoabilitato+" - spare1:"+spare1+"  - spare2:"+spare2);
	        	
				
			}//fine for
		
		}catch(Exception e) {
			log.write("ConfiguratoreLinea -> Errore DBAREA: "+e.toString()+"\n");
		}
		
		
		
	}//fine scan
	
	
	
	
	
	public void DBRead(int start)
    {
		//log("\nREADAREA - DB =" + DB +" datatomove="+DataToMove+"\n" );
		int Result = Client.ReadArea(S7.S7AreaDB, DB_CONF , start, DataToMove, Buffer);
		
        if (Result==0)
        {
        	//log("\nDBAREA OK.  DB="+DB+"\n" );
        }else
        {
        	log.write("ConfiguratoreLinea ->PROBLEMA CON READAREA!!!!!!!!! ERRORE =" + Result +". db="+DB_CONF);
        }
       
    }      
    
    public void DBWrite()
    {
    	
    	int Result = Client.WriteArea(S7.S7AreaDB, DB_CONF, 0, (NUMERO_STAZIONI_ATTIVE * DataToMove) , Buffer);
        if (Result==0)
        {
        	log.write("ConfiguratoreLinea -> DB "+DB_CONF+" succesfully written using size reported by DBwrite() - Modulo configuratoreLinea");
        }else
        	log.write("ConfiguratoreLinea -> DB "+DB_CONF+" errore dbwrite()  - Modulo configuratoreLinea");
    	
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
    	log.write(timestamp+":"+ l + "   - modulo ConfiguratoreLinea\n");
    }
    
    public void connetti() throws Exception{
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
    		log.write("CONNESSIONE RIUSCITA! - MODULO: configuratoreLinea");
    		//statoplc.setText("CONNESSIONE OK\n");
            
    		//monitor.append("PDU negotiated: " + Client.PDULength()+" bytes" );
    		//System.out.println("Connected to   : " + setting.getIPPLC() + " (Rack=" + Integer.valueOf(RACKtext.getText()) + ", Slot=" + Integer.valueOf(SLOTtext.getText())+ ")");
           // System.out.println("PDU negotiated : " + Integer.valueOf(SLOTtext.getText()));
            //statoplc.setText("CONNESSIONE OK!");
    	}else {
    		log.write("CONNESSIONE FALLITA AL PLC! - MODULO: configuratoreLinea");
    		//indicatore.statoplc.setText("ERRORE");
    		//indicatore.statoplc.setBackground(setting.rosso);
    		//statoplc.setBackground(new Color(219, 22, 65));
    	}
    }//fine connetti
    
    
    
    public AtomoConfigurazioneLinea[] getListaAtomoConfigurazione()
    {
    	return dateList;
    }
    
    public void setListaAtomoConfigurazione(AtomoConfigurazioneLinea[] lista) {
    	
    	//NUMERO_STAZIONI_ATTIVE = 7; //da caricare dinamicamente
    	
    	log.write("\nSALVO LA CONFIGURAZIONE. ATTIVE:"+NUMERO_STAZIONI_ATTIVE+" - MODULO: configuratoreLinea");
    	
    	dateList = lista;
    	int OFFSET_DB_LINEA = 4; //byte
    	int indirizzo_start = 0;
    	
    	try {
    	
    	for(int i=0;i<NUMERO_STAZIONI_ATTIVE;i++) {
    		
    		indirizzo_start = (OFFSET_DB_LINEA * i);
    		
    		
    		AtomoConfigurazioneLinea atomo;
    		
    		if (i==NUMERO_STAZIONI_ATTIVE -1)
    			atomo = lista[Setting.STAZIONE_DI_CONTROLLO_2-1];
    		else
    			atomo = lista[i];
    		
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
    		log.write("\ncONFIGURAZIONE lINEA -> Errore DBAREA:"+e.toString()+"  --  inizio:"+indirizzo_start+"   - Modulo: ConfiguratoreLinea");
		}
    	
    	log.write("CONFIGURAZIONE SALVATA - Modulo: ConfiguratoreLinea");
    	
    }//fine scrivi
    
    
    private byte[] intToBytes( final int i ) {
        ByteBuffer bb = ByteBuffer.allocate(4); 
        bb.putInt(i); 
        return bb.array();
    }
    
	
	
}//fine classe

