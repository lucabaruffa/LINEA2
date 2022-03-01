package linea;

public class StatoPLC {

	public boolean RUN = false;
	public boolean MANUALE = false;
	public boolean ALLARME = false;
	
	public StatoPLC(boolean R, boolean M, boolean A) {
		RUN = R;
		MANUALE = M;
		ALLARME = A;
		
	}
	
	
}//fine classe
