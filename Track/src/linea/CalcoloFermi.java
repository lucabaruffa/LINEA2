package linea;

import java.time.LocalDateTime;

public class CalcoloFermi {

	public static String calcolaMotivoFermo(long minuti_fermo) {

		String risultato = "DA GIUSTIFICARE";
		String motivazione = "DA GIUSTIFICARE";

		int ora = LocalDateTime.now().getHour();
		int minuti = LocalDateTime.now().getMinute();

		
		if (((ora == 12) && (minuti <= 30)) && (minuti_fermo < 50)) {
			motivazione = "PAUSA PRANZO";
			risultato = "F0000000";
		}

		// cena
		// if ((ora==18) && (minuti>=48)) {
		// risultato = "PAUSA PRANZO";
		// }
		if (((ora == 19) && (minuti <= 30)) && (minuti_fermo < 50)) {
			motivazione = "PAUSA PRANZO";
			risultato = "F0000000";
		}

		// turno non attivo NOTTE
		if (((ora == 4) && (minuti <= 59)) && (minuti_fermo > 360)) {
			motivazione = "FERMO PROGRAMMATO";
			risultato = "H0000000";
		}
		// MATTINA
		if (((ora == 11) && (minuti <= 59)) && (minuti_fermo > 360)) {
			motivazione = "FERMO PROGRAMMATO";
			risultato = "H0000000";
		}
		// POMERIGIO
		if (((ora == 20) && (minuti <= 59)) && (minuti_fermo > 360)) {
			motivazione = "FERMO PROGRAMMATO";
			risultato = "H0000000";
		}

		if ((minuti_fermo > 500)) {
			motivazione = "FERMO PROGRAMMATO";
			risultato = "H0000000";
		}

		return risultato +"@"+motivazione;
	}// fine calcolaMoticoFermo

}// fine classe
