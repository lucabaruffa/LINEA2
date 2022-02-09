package linea;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Batteria {
	
	private String codice_batteria="";
	public String timestamp ="01/01/1970";
	private String stato_batteria= "";
	private int index= 0; //posizione dei valori nell'array del plc ; 0 è la prima batteria (top) in lavorazione
	private int dato1= -1;
	private int dato2 = -1;
	private boolean da_sincronizzare = true;  //una volta inviato al database, lo segno come da cancellare
	private String postazione = "-1";
	
	public GregorianCalendar data = new GregorianCalendar(); 
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	
	
	public Batteria() {
		
	}//fine costruttore
	
	public synchronized void setStato(String s) {
		stato_batteria = s;
	}
	
	public Batteria(int posizione, String codice,String time, String stato, int d1, int d2) throws Exception {
		codice_batteria = codice;
		timestamp = time;
		stato_batteria = stato;
		index = posizione;
		dato1 = d1;
		dato2 =d2;
		
		//System.out.println("Ricevo timestamp =" + time);
				
		data.setTime(sdf.parse(time)); 
	
		
	}//fine costruttore
	
	public synchronized String getCodiceBatteria()
	{
		return codice_batteria;
	}
	
	public String gettimestamp()
	{
		return timestamp;
	}
	
	public String getStatoBatteria()
	{
		//if ((this.stato_batteria.contains("TRUE")) || (this.stato_batteria.contains("true")))	
			return stato_batteria;
		//else
		//	return stato_batteria;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public int getDato1()
	{
		return dato1;
	}
	
	public int getDato2()
	{
		return dato2;
	}
	
	public Date getData()
	{
		return data.getTime();
	}
	
	public String getPostazione()
	{
		return postazione;
	}
	
	public synchronized void setDaSincronizzare() {
		da_sincronizzare = false;
	}
	
	public void setPostazione(String p) {
		postazione=p;
	}
	
	
	
}//fine classe
