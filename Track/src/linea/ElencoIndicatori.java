package linea;
import javax.swing.JTextField;

public class ElencoIndicatori {
	
	
	public static Indicatore[] listaIndicatori = new Indicatore[50];
	
	
	public void setIndicatore(int indice, JTextField tempo , JTextField stato , JTextField batteria,JTextField splc,JTextField sdb,JTextField con,JTextField batteriaZero,JTextField risultato,JTextField statolinea,JTextField timestatolinea,JTextField scart)
	{
		listaIndicatori[indice] = new Indicatore(tempo,stato,batteria, splc, sdb,con,batteriaZero,risultato,statolinea,timestatolinea,scart);
	}
	
	public Indicatore getIndicatore(int indice)
	{
		return listaIndicatori[indice];
	}
	
	
}//fine classe


