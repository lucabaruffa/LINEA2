package linea2;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import DB.DBConnectionPool;
import PLC.readerPLC;


public class Linea {

	private static CopyOnWriteArrayList<readerPLC> dateList;
	private Setting setting;

	
	
	ArrayBatterie arrayArrayBatterie = new ArrayBatterie();
	private static LoggerFile log = new LoggerFile();
	
	public Linea(JProgressBar bufferBatterie, ElencoIndicatori el, JTextField errore) throws InterruptedException {
		dateList = new CopyOnWriteArrayList<readerPLC>();
		
		try {
			setting = new Setting();
		} catch (Exception e) {
			log.write("ERRORE LINEA:" + e.toString() );
			e.printStackTrace();
		}
		
		
 		 		
 		int stazioni_attive = Integer.parseInt(setting.getNumeroStazioniAttive());
 			
 		setting.setAreaError(errore);
 		
 		
 		//TUTTE LE STAZIONI TRANNEQUELLA DI CONTROLLO DA CUI IL -1
		for (int i=0;i<stazioni_attive-1;i++) {
			
			//INDICATORI PER OGNI POSTAZIONE
			Indicatore indicatore = el.getIndicatore(i);
			int DB = setting.getArrayDB()[i];
			
			//CREO UN ARRAY DI BATTERIE PER OGNI POSTAZIONE
			ArrayBatteriePostazione arrayBatteriePostazione = new ArrayBatteriePostazione();
			arrayBatteriePostazione.nomeLista = "Lista Batterie Linea " +(i+1);
			
			//CREO IL READER DEL PLC PER OGNI POSTAZIONE
			readerPLC lettorePLC = new readerPLC(DB,bufferBatterie,(i+1), indicatore, arrayBatteriePostazione);
			lettorePLC.avvia();
			this.addReader(lettorePLC);
						
			//NELL'ARRAY DELL'ARRAY INSERISCO L'ARRAY DELLA POSTAZIONE
			arrayArrayBatterie.addArray(arrayBatteriePostazione);
			
			//CREO IL THREAD E LO INSERISCO NELLA LISTA DEI THREAD
			Thread t1 = new Thread(lettorePLC);
			//lista_thread.add(t1);
			
			Thread.sleep(800);
			//AVVIO IL LETTORE READER PLC
			t1.start();
	
		}//fine for
		
		
		//AVVIO POSTAZIONE DI CONTROLLO FINALE
		Indicatore indicatore = el.getIndicatore(Setting.STAZIONE_DI_CONTROLLO_2);
		int DB = setting.getArrayDB()[Setting.STAZIONE_DI_CONTROLLO_2];
		
		//CREO UN ARRAY DI BATTERIE PER OGNI POSTAZIONE
		ArrayBatteriePostazione arrayBatteriePostazione = new ArrayBatteriePostazione();
		arrayBatteriePostazione.nomeLista = "Lista Batterie Linea " +(Setting.STAZIONE_DI_CONTROLLO_2);
		
		//CREO IL READER DEL PLC PER OGNI POSTAZIONE
		readerPLC lettorePLC = new readerPLC(DB,bufferBatterie,Setting.STAZIONE_DI_CONTROLLO_2, indicatore, arrayBatteriePostazione);
		lettorePLC.avvia();
		this.addReader(lettorePLC);
		
		//NELL'ARRAY DELL'ARRAY INSERISCO L'ARRAY DELLA POSTAZIONE
		arrayArrayBatterie.addArray(arrayBatteriePostazione);
		
		//CREO IL THREAD E LO AVVIO
		new Thread(lettorePLC).start();
		//lista_thread.add(t1);
		
		Thread.sleep(500);
		
		
	}//fine costruttore
	
	public void addReader(readerPLC b) {
		dateList.add(b);
	}
	
	public void addReaderTop(readerPLC b) {
		dateList.add(0,b);
	}
	
	public int getDimension() {
		return dateList.size();
	}
	
	public readerPLC getOnTop() {
		return dateList.get(0);
	}
	
	public Iterator<readerPLC> getIterator() {
		return dateList.iterator();
	}
	
	public void closeAllThread() {
		Iterator<readerPLC> it = dateList.iterator();
		while(it.hasNext()) {
			   	it.next().stop();
		}
	}
	
	
	
}//fine classe
