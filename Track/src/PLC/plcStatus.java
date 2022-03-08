package PLC;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import Moka7.IntByRef;
import Moka7.S7;
import Moka7.S7Client;
import linea.ArrayBatteriePostazione;
import linea.Indicatore;
import linea.LoggerFile;
import linea.Setting;
import linea.StatoPLC;

public class plcStatus  implements Runnable  {
	
	
	public byte[] Buffer = new byte[65536]; // 64K buffer (maximum for S7400 systems)
	public int Rack = 0; 
	public int Slot = 1; 
    public final S7Client Client = new S7Client();
    public int DataToMove=72; // contiene la dimensione del DB connesso
    private int DB = 200; // db stati del plc
    private int CurrentStatus = S7.S7CpuStatusUnknown;
    private int sleep = Setting.TIMER_SLEEP_READERPLC;  //tempo di ciclo
    private Indicatore indicatore;
   
    private Setting setting;
    
    public GregorianCalendar data = new GregorianCalendar(); 
	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	protected ArrayBatteriePostazione array;
	
	private GregorianCalendar dax = new GregorianCalendar(); 
	
	private static LoggerFile log = new LoggerFile();
	
	
	public plcStatus() {
		
		try {
			setting = new Setting(true);
		} catch (Exception e) {
			log.write("PLCStatus -> ERRORE CARICAMENTO CONFIGURAZIONE \n");
			e.printStackTrace();
		}
		
					
		Rack = Integer.parseInt(setting.getRACK());
		Slot = Integer.parseInt(setting.getSLOT());
		DB = Setting.DBPLCSTATUS;
		
		log.write("AVVIO RUN");
		
		//monitor.append("RUNNING\n");
		if(Client.Connected) {		
			//long startTime = System.nanoTime();	
			ShowStatus();				
			//long endTime = System.nanoTime();
			//monitor.append("\nPostazione "+ nomeStazione + ". Tempo esecuzione = "+ (endTime - startTime)/1000000+" millisecondi");  
		}//fine if
		else {
			log.write("plcStatus -> NON CONNESSO! - AVVIO TENTATIVO DI CONNESSIONE");
			connetti();
			
		}
		
		
		
		
		
	}//fine costruttore
    
    
    public void HexDump(byte[] Buffer, int Size)
    {
        int r=0;
        String Hex = "";
        
        for (int i=0; i<Size; i++)
        {
            int v = (Buffer[i] & 0x0FF);
            String hv = Integer.toHexString(v);     
            
            if (hv.length()==1)
                hv="0"+hv+" ";
            else
                hv=hv+" ";
            
            Hex=Hex+hv;
            
            r++;
            if (r==16)
            {
            	log.write(Hex+" ");
            	log.write(S7.GetPrintableStringAt(Buffer, i-15, 16)+"\n");
            	System.out.print(Hex+" ");
                System.out.println(S7.GetPrintableStringAt(Buffer, i-15, 16));
                Hex="";
                r=0;
            }
        }
        int L=Hex.length();
        if (L>0)
        {
            while (Hex.length()<49)
                Hex=Hex+" ";
            
            log.write(Hex);
            log.write(S7.GetPrintableStringAt(Buffer, Size-r, r)+"\n");
            System.out.print(Hex);
            System.out.println(S7.GetPrintableStringAt(Buffer, Size-r, r));                       
        }
        else {
            System.out.println();
            log.write("\n");
        }
    }

	
	
	public boolean DBGet()
    {
        IntByRef SizeRead = new IntByRef(0);
        int Result = Client.DBGet(DB, Buffer, SizeRead);        
        if (Result==0)
        {
        	DataToMove = SizeRead.Value; // memorizzo dimensioni del DB
        	//System.out.println("DB "+DB+" - Size read "+DataToMove+" bytes");
        	log.write("DB "+DB+" - DIMENSIONI DB "+DataToMove+" bytes\n");
        	HexDump(Buffer, DataToMove);
        	return true;
        }  else {
        	log.write("DB GET fallita !");
        }
        return false;        
    }
	//fine DBGet
	
	public void DBRead(int start)
    {
		
		int Result = Client.ReadArea(S7.S7AreaDB, DB, start, DataToMove , Buffer);
		
        if (Result==0)
        {
        	//log.write("\nDBAREA OK.  DB="+DB+"\n" );
        }else
        {
        	log.write("\nplcStatus -> PROBLEMA CON READAREA!!!!!!!!! ERRORE =" + Result +". db="+DB);
        }
       
    }      
    
    public void DBWrite(int db_w)
    {
    	
    	int Result = Client.WriteArea(S7.S7AreaDB, db_w, 0, DataToMove, Buffer);
        if (Result==0)
        {
        	//log.write("DB "+DB+" succesfully written ");
        }
    	
    } //dbwrite 
    
        
    
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
	                	log.write("Unknown ("+PlcStatus.Value+")");
	                	//statoplc.setText("SCONOSCIUTO");
	            }
	        }//fine if
	        CurrentStatus = PlcStatus.Value;
    	}
	        catch(Exception e) {
	        	log.write("Errore in SHOWSTATUS: " + e.toString());
	        }
        
    }//fine showstatus
	
	
	
	
	
	
	 public void readPLCstatus() {
	    	
		 	
		    int offset_DBAREA = 0;	
	  	    DBRead(0);
	        	
	  	    //log.write("plcStatus -> Tento lettura dal plc lo stato di tutti i PLC di campo. ");
	  	    
	  	    // RUN = FALSE NOT RUN  -> TRUE RUN
	  	    //ALLARME = FALSE NON ALLARME -> TRUE ALLARME
	  	    //MANUALE = FALSE AUTOMATICA -> TRUE MANUALE
	  	    
	  	    //PLC1
	        boolean RUN1 = S7.GetBitAt(Buffer, offset_DBAREA, 0);
	        boolean MANUALE1 = S7.GetBitAt(Buffer, offset_DBAREA, 1);
	        boolean ALLARME1 = S7.GetBitAt(Buffer, offset_DBAREA, 2);
	        
	        offset_DBAREA += 20;
	        
	        //PLC2
	        boolean RUN2 = S7.GetBitAt(Buffer, offset_DBAREA, 0);
	        boolean MANUALE2 = S7.GetBitAt(Buffer, offset_DBAREA, 1);
	        boolean ALLARME2 = S7.GetBitAt(Buffer, offset_DBAREA, 2);
	        
	        offset_DBAREA += 20;
	        
	        //PLC3
	        boolean RUN3 = S7.GetBitAt(Buffer, offset_DBAREA, 0);
	        boolean MANUALE3 = S7.GetBitAt(Buffer, offset_DBAREA, 1);
	        boolean ALLARME3 = S7.GetBitAt(Buffer, offset_DBAREA, 2);
	        
	        offset_DBAREA += 20;
	        
	        //PLC4
	        boolean RUN4 = S7.GetBitAt(Buffer, offset_DBAREA, 0);
	        boolean MANUALE4 = S7.GetBitAt(Buffer, offset_DBAREA, 1);
	        boolean ALLARME4 = S7.GetBitAt(Buffer, offset_DBAREA, 2);
	        
	        offset_DBAREA += 20;
	        
	        //PLC5
	        boolean RUN5 = S7.GetBitAt(Buffer, offset_DBAREA, 0);
	        boolean MANUALE5 = S7.GetBitAt(Buffer, offset_DBAREA, 1);
	        boolean ALLARME5 = S7.GetBitAt(Buffer, offset_DBAREA, 2);
	        
	        offset_DBAREA += 20;
	        
	        //PLC6
	        boolean RUN6 = S7.GetBitAt(Buffer, offset_DBAREA, 0);
	        boolean MANUALE6 = S7.GetBitAt(Buffer, offset_DBAREA, 1);
	        boolean ALLARME6 = S7.GetBitAt(Buffer, offset_DBAREA, 2);
	        
	        offset_DBAREA += 20;
	        
	        //PLC7
	        boolean RUN7 = S7.GetBitAt(Buffer, offset_DBAREA, 0);
	        boolean MANUALE7 = S7.GetBitAt(Buffer, offset_DBAREA, 1);
	        boolean ALLARME7 = S7.GetBitAt(Buffer, offset_DBAREA, 2);
	        
	        offset_DBAREA += 20;
	        
	        //PLC8
	        boolean RUN8 = S7.GetBitAt(Buffer, offset_DBAREA, 0);
	        boolean MANUALE8 = S7.GetBitAt(Buffer, offset_DBAREA, 1);
	        boolean ALLARME8 = S7.GetBitAt(Buffer, offset_DBAREA, 2);
	        
	        offset_DBAREA += 20;
	        
	        //PLC9
	        boolean RUN9 = S7.GetBitAt(Buffer, offset_DBAREA, 0);
	        boolean MANUALE9 = S7.GetBitAt(Buffer, offset_DBAREA, 1);
	        boolean ALLARME9 = S7.GetBitAt(Buffer, offset_DBAREA, 2);
	        
	        
	        Setting.statiPLC[1] = new StatoPLC(RUN1,MANUALE1,ALLARME1); 
	        Setting.statiPLC[2] = new StatoPLC(RUN2,MANUALE2,ALLARME2); 
	        Setting.statiPLC[3] = new StatoPLC(RUN3,MANUALE3,ALLARME3); 
	        Setting.statiPLC[4] = new StatoPLC(RUN4,MANUALE4,ALLARME4); 
	        Setting.statiPLC[5] = new StatoPLC(RUN5,MANUALE5,ALLARME5);
	        Setting.statiPLC[6] = new StatoPLC(RUN6,MANUALE6,ALLARME6);
	        Setting.statiPLC[7] = new StatoPLC(RUN7,MANUALE7,ALLARME7);
	        Setting.statiPLC[8] = new StatoPLC(RUN8,MANUALE8,ALLARME8);
	        Setting.statiPLC[9] = new StatoPLC(RUN9,MANUALE9,ALLARME9);
	        
	        Setting.statiPLC[21] = new StatoPLC(true,true,true);
	        
	        log.write("plcStatus -> Stato plc 1. RUN1 =" + RUN1 +"  MANUALE1:" + MANUALE1 + "   ALLARME1:" + ALLARME1);
	        log.write("plcStatus -> Stato plc 1. RUN2 =" + RUN2 +"  MANUALE2:" + MANUALE2 + "   ALLARME2:" + ALLARME2);
	        log.write("plcStatus -> Stato plc 1. RUN3 =" + RUN3 +"  MANUALE3:" + MANUALE3 + "   ALLARME3:" + ALLARME3);
	        log.write("plcStatus -> Stato plc 1. RUN4 =" + RUN4 +"  MANUALE4:" + MANUALE4 + "   ALLARME4:" + ALLARME4);
	        log.write("plcStatus -> Stato plc 1. RUN5 =" + RUN5 +"  MANUALE4:" + MANUALE5 + "   ALLARME4:" + ALLARME5);
	        log.write("plcStatus -> Stato plc 1. RUN6 =" + RUN6 +"  MANUALE4:" + MANUALE6 + "   ALLARME4:" + ALLARME6);
	        log.write("plcStatus -> Stato plc 1. RUN7 =" + RUN7 +"  MANUALE4:" + MANUALE7 + "   ALLARME4:" + ALLARME7);
	        
	        
	    	
	    }//fine leggi Array
	 
	 
	 
	    
	    
	    public void connetti() {
	    	if (setting.getConnectionType().equals("OP"))
			{
				Client.SetConnectionType(S7.OP);
				log.write("TIPO CONNESSIONE: OP\n");
				}
			
	    	if (setting.getConnectionType().equals("PG")) {
				Client.SetConnectionType(S7.PG);
				log.write("TIPO CONNESSIONE: PG\n");
			}
	    	if (setting.getConnectionType().equals("S7_BASIC")) {
				Client.SetConnectionType(S7.S7_BASIC);
				log.write("TIPO CONNESSIONE: S7_BASIC\n");
				}
	    	
	    	log.write("IP:" + setting.getIPPLC()+"\n");
			int Result = Client.ConnectTo(setting.getIPPLC(), Integer.valueOf(setting.getRACK()), Integer.valueOf(setting.getSLOT()));
	    	if (Result==0)
	    	{
	    		
	    	//Indicatore.statoplc.setText("OK");
	    	//Indicatore.statoplc.setBackground(Setting.verde);
	    	log.write("\nCONNESSIONE RIUSCITA AL plcstaus!\n");
	    		//statoplc.setText("CONNESSIONE OK\n");
	            
	    		//monitor.append("PDU negotiated: " + Client.PDULength()+" bytes" );
	    		//System.out.println("Connected to   : " + setting.getIPPLC() + " (Rack=" + Integer.valueOf(RACKtext.getText()) + ", Slot=" + Integer.valueOf(SLOTtext.getText())+ ")");
	           // System.out.println("PDU negotiated : " + Integer.valueOf(SLOTtext.getText()));
	            //statoplc.setText("CONNESSIONE OK!");
	    	}else {
	    		log.write("CONNESSIONE FALLITA! - NUOVO TENTATIVO FRA "+sleep+" SECONDI\n");
	    		try {
					//indicatore.statoplc.setText("ERRORE");
					//indicatore.statoplc.setBackground(setting.rosso);
				} catch (Exception e) {
					// TODO: handle exception
				}
	    		
	    	}
	    }//fine connetti
	    
	    
	    
	  
	  
	    
	    public void setSleep(int s) {
	    	sleep = s;
	    }


		@Override
		public void run() {
			boolean running = true;
			// TODO Auto-generated method stub
			//log.write("AVVIO RUN plcStatus");
			//while(running) {
				//monitor.append("RUNNING\n");
				if(Client.Connected) {
					
					//stato.setBackground(Setting.verde);
					//stato.setText("OK");
					
					long startTime = System.nanoTime();
					readPLCstatus();
					long endTime = System.nanoTime();
					//monitor.append("\nPostazione "+ nomeStazione + ". Tempo esecuzione = "+ (endTime - startTime)/1000000+" millisecondi");  
				}//fine if
				else {
					log.write("CONFIGURATORE PLC NON CONNESSO! - AVVIO TENTATIVO DI CONNESSIONE");
					//stato.setBackground(Setting.rosso);
					//stato.setText("KO");
					connetti();	
				}
				
				try {
					//monitor.append("slider ="+sleep+"\n");
					Thread.sleep(sleep);
				}catch(Exception e) {
					log.write("\nConfiguratore plc Errore inSleep!\n");
					//stato.setBackground(Setting.rosso);
					//stato.setText("KO");
				}//fine catch
				
			//}//fine while
	       
		}//fine run
	 
	    
	
	 
		   	
	
	  
	   
	   
	   
	  

}//fine classe

