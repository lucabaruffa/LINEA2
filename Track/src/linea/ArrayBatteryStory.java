package linea;
import java.util.LinkedList;
import java.util.Queue;


public class ArrayBatteryStory {

	
	private static Queue<BatteryStory> arrayBatteryStory = new LinkedList<>();
	private static int MAX_SIZE = 5000; //dimensioni massime numero BatteryStore
	private static LoggerFile log = new LoggerFile();
	
	public ArrayBatteryStory() {
		
	}
	
	  public BatteryStory getFirstBatteria() {
		  //return arrayBatterie.poll(); //la restituisce, la rimuove anche dalla coda
		 return arrayBatteryStory.element();//la restituisce, ma NON la rimuove anche dalla coda
	  }
		
	  
	  private void aggiungi(BatteryStory battstory) {
		  //codice_batteria = batt.getCodiceBatteria();
		  synchronized(arrayBatteryStory) {
			  arrayBatteryStory.add(battstory);
		  
			  if (arrayBatteryStory.size()>MAX_SIZE) {
				  log.write("SUPERATE MASSIME DIMENSIONI PER ARRAYBATTERYSTORY. CANCELLO LA PRIMA CODICE:"+arrayBatteryStory.poll().getCodiceBatteryStory());
				  
			  }
		  
		  }
		  
	  }//fine 
	  
	  //svuota tutta la klista di batterie
	  public void svuota() {
		  synchronized(arrayBatteryStory) {
				 int dimensioni = arrayBatteryStory.size();
				 for(int i=0; i<dimensioni; i++) {
					 arrayBatteryStory.poll();
				 }
		  }
	  }//fine svuota
	  
	  
	  public void itera() {
		  arrayBatteryStory.forEach(batteryStory -> {
			 //batteryStory.getElencoBatterie().pe //faccio qualcosa
			  
	      });
	  }
	  
	  public boolean contains(Batteria batt) {
		  synchronized(arrayBatteryStory) {
			  for(BatteryStory batteryStory: arrayBatteryStory) {
		          if (batteryStory.contains(batt)) {
		        	  return true;
		          }
		      }//fine for
		  }
			  return false;
	  }
	  
	  
	  public BatteryStory cerca(Batteria batteria) {
		  synchronized(arrayBatteryStory) {
				  for(BatteryStory batteryStory: arrayBatteryStory) {
			         
					  if (batteryStory.contains(batteria)) {
			        	  return batteryStory;
			          }
			      }//fine for 
		  }
		  return null;
	  }
	  
	  public void aggiungiBatteria(Batteria batteria) {
		 
			  BatteryStory bStory = this.cerca(batteria);
			   
				  if (bStory!=null) {
					  //la batteria e presente in bStory
					  	//monitor.append("\nArrayBatteryStory -> La batteria codice:"+batteria.getCodiceBatteria()+" è presente. La AGGIUNGO");
					  	bStory.aggiungi(batteria);
				  }else {
					  //creo BatteryStory e la inserisco
					  //monitor.append("\nArrayBatteryStory -> La batteria codice:"+batteria.getCodiceBatteria()+" NON è presente. CREO e AGGIUNGO");
					  BatteryStory b = new BatteryStory(batteria);
					  this.aggiungi(b);
				  }
			
			 
	  }//fine aggiungi
	  
	  
	  public void print() {
		  int i = 1;
		  for(BatteryStory batteryStory: arrayBatteryStory) {
			  log.write("\nBatteryStory:"+i+" "+batteryStory.print());  
			  i +=1;
	      }//fine for
		  log.write("-------------------\n");
		  
	  }//fine print
	  
	  public Queue<BatteryStory> getArrayBatteryStory() {
		 return arrayBatteryStory;
	  }//fine print
	
}//fine classe
