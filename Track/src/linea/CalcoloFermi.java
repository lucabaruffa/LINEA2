package linea;

import java.time.LocalDateTime;

public class CalcoloFermi {

	public static String calcolaMotivoFermo(long minuti_fermo) {

		String risultato = "DA GIUSTIFICARE";

		int ora = LocalDateTime.now().getHour();
		int minuti = LocalDateTime.now().getMinute();

		// pranzo
		// if ((ora==11) && (minuti>=48)) {
		// risultato = "PAUSA PRANZO";
		// }
		if ((ora == 12) && (minuti <= 30) && (minuti_fermo < 50)) {
			risultato = "PAUSA PRANZO";
		}

		// cena
		// if ((ora==18) && (minuti>=48)) {
		// risultato = "PAUSA PRANZO";
		// }
		if ((ora == 19) && (minuti <= 30) && (minuti_fermo < 50)) {
			risultato = "PAUSA PRANZO";
		}

		// turno non attivo NOTTE
		if ((ora == 6) && (minuti <= 15) && (minuti_fermo > 450)) {
			risultato = "TURNO FERMO";
		}
		// MATTINA
		if ((ora == 14) && (minuti <= 15) && (minuti_fermo > 450)) {
			risultato = "TURNO FERMO";
		}
		// POMERIGIO
		if ((ora == 22) && (minuti <= 15) && (minuti_fermo > 450)) {
			risultato = "TURNO FERMO";
		}

		if ((minuti_fermo > 500)) {
			risultato = "LINEA FERMA";
		}

		return risultato;
	}// fine calcolaMoticoFermo

}// fine classe
