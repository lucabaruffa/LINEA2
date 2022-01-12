package PLC;
import java.awt.Color;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import DB.CheckControl;
import Moka7.IntByRef;
import Moka7.S7;
import Moka7.S7Client;
import View.Scarti;
import linea2.ArrayBatteriePostazione;
import linea2.Batteria;
import linea2.ConfiguratoreLinea;
import linea2.ControlloRiempimentoAcido;
import linea2.Indicatore;
import linea2.LoggerFile;
import linea2.Setting;
import linea2.greenCode;

public class plcCommand    {
	
	
	public byte[] Buffer = new byte[65536]; // 64K buffer (maximum for S7400 systems)
	public int Rack = 0; 
	public int Slot = 1; 
    public final S7Client Client = new S7Client();
    public int DataToMove=40; // contiene la dimensione del DB connesso
    private int DB = -1; // db
    private int CurrentStatus = S7.S7CpuStatusUnknown;
    private int sleep = Setting.TIMER_SLEEP_READERPLC;  //tempo di ciclo
    private Indicatore indicatore;
   
    private Setting setting;
    
    public GregorianCalendar data = new GregorianCalendar(); 
	public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	protected ArrayBatteriePostazione array;
	
	private GregorianCalendar dax = new GregorianCalendar(); 
	
	private static LoggerFile log = new LoggerFile();
	
	
	public plcCommand() {
		
		
		
		try {
			setting = new Setting();
		} catch (Exception e) {
			log.write("ERRORE CARICAMENTO CONFIGURAZIONE NEL MODULO plcCommand\n");
			e.printStackTrace();
		}
		
					
		Rack = Integer.parseInt(setting.getRACK());
		Slot = Integer.parseInt(setting.getSLOT());
		DB = Setting.DBGREENCODE;
		
		log.write("AVVIO RUN");
		
		//monitor.append("RUNNING\n");
		if(Client.Connected) {
			
			long startTime = System.nanoTime();
			
			
			//leggiArrayBatterie();
			ShowStatus();
				
			long endTime = System.nanoTime();
			//monitor.append("\nPostazione "+ nomeStazione + ". Tempo esecuzione = "+ (endTime - startTime)/1000000+" millisecondi");  
		}//fine if
		else {
			log.write("NON CONNESSO! - AVVIO TENTATIVO DI CONNESSIONE");
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
        	log.write("\nplcCommand -> PROBLEMA CON READAREA!!!!!!!!! ERRORE =" + Result +". db="+DB);
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
	
	
	
	
	
	
	 public greenCode leggiGreenCode() {
	    	
		 	greenCode green = new greenCode();
		 
		    int offset_DBAREA = 0;	
	  	    DBRead(0);
	        	
	  	  log.write("plcCommand -> Tento lettura dal plc del greencode impostato. ");
	  	    
	        String codice = S7.GetStringAt(Buffer, offset_DBAREA ,10).trim();
	        offset_DBAREA += 10;
	        
	        String nome = S7.GetStringAt(Buffer, offset_DBAREA ,30).trim();
	        
	        green.setGreencode(codice);
	        green.setNome(nome);
	        
	        log.write("plcCommand -> Tento lettura dal plc del greencode impostato. GREEN : " + green +"  -- nome: " + nome);
	        
	        	
	        //timestamp = (S7.GetDateAt(Buffer, offset_DBAREA )).toString();
	        	
	       /*	
	        try {
				data.setTime(sdf.parse(timestamp));
				//log.write("leggo la data :" + timestamp);
			} catch (ParseException e1) {
				log.write("readerPLC - Errore parsing data 331 : " + e1.toString());
			} 
	        
	      */
	        
	        log.write("plcReader -> GREEN : " + green +"  -- nome: " + nome);
	       
	        return green;
	        	
	        
	    	
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
	    		
	    		Indicatore.statoplc.setText("OK");
	    		Indicatore.statoplc.setBackground(Setting.verde);
	    		log.write("\nCONNESSIONE RIUSCITA!\n");
	    		//statoplc.setText("CONNESSIONE OK\n");
	            
	    		//monitor.append("PDU negotiated: " + Client.PDULength()+" bytes" );
	    		//System.out.println("Connected to   : " + setting.getIPPLC() + " (Rack=" + Integer.valueOf(RACKtext.getText()) + ", Slot=" + Integer.valueOf(SLOTtext.getText())+ ")");
	           // System.out.println("PDU negotiated : " + Integer.valueOf(SLOTtext.getText()));
	            //statoplc.setText("CONNESSIONE OK!");
	    	}else {
	    		log.write("CONNESSIONE FALLITA! - NUOVO TENTATIVO FRA "+sleep+" SECONDI\n");
	    		try {
					indicatore.statoplc.setText("ERRORE");
					indicatore.statoplc.setBackground(setting.rosso);
				} catch (Exception e) {
					// TODO: handle exception
				}
	    		
	    	}
	    }//fine connetti
	    
	    
	    
	  
	  
	    
	    public void setSleep(int s) {
	    	sleep = s;
	    }
	 
	    
	
	 
		   	
	public boolean writeGreenCode(greenCode green) {	   	
	   	
		   		  
			int indirizzo_start = 0;
			
			System.out.println("337: Tento la scittura green:" + green.getGreencode()+"   -- nome:" + green.getNome());
	   			   			   		   
	   		//S7.SetDateAt(Buffer, indirizzo_start , date);
	   		
	   		String code = green.getGreencode();
	   		String nome = green.getNome();
	   		
	   		
	   		
	   		
	   		for (int i = code.length() ; i < 10; i++) {
	   			code +="0";
	        }////fine for
	   		
	   		for (int i = nome.length() ; i < 30; i++) {
	   			nome +=" ";
	        }////fine for
	   		
	   		S7.SetString(Buffer, indirizzo_start, code);
            System.out.println("361: Tento di scrivere sul plc :" + new String(Buffer));
            
            indirizzo_start = indirizzo_start + 10;
	   		
            
            S7.SetString(Buffer, indirizzo_start, nome);
            System.out.println("373: Tento di scrivere sul plc :" + new String(Buffer));
	   		
	   		try {
	   			DBWrite(DB); //inizio, dimensioni
	   			return true;
	   		}catch(Exception h) {
	   			log.write("plcCommand -> Errore scrittura batteria sul plc. DB="+DB+" : ERR=" + h.toString());
	   			System.out.println("plcCommand -> Errore scrittura batteria sul plc. DB="+DB+" : ERR=" + h.toString());
	   		}
	   		
	   		return false;
	  	   		
	   }//fine public scartoPostazione
	  
	   
	   
	   
	  

}//fine classe

