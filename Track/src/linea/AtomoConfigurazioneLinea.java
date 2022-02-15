package linea;

public class AtomoConfigurazioneLinea {

	public int statoscanner = 0; // non abilitato
	public int scartoabilitato = 0; // non abilitato
	public int spare1 = 0; // non abilitato
	public int spare2 = 0; // non abilitato

	public AtomoConfigurazioneLinea(int scanner, int scarto, int sp1, int sp2) {
		if (scanner >= 1)
			scanner = 256;
		else
			scanner = 0;

		statoscanner = scanner;

		if (scarto >= 1)
			scarto = 256;
		else
			scarto = 0;

		scartoabilitato = scarto;
		spare1 = sp1;
		spare2 = sp2;
	}// fine costruttore

}// fine classe
