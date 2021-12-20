package linea2;

import java.util.HashMap;
import java.util.Map;

public class ControlloRiempimentoAcido {

	/*
	 * memorizzo le batterie in ingresso della riempitrice acido.
	 * Quando escono (bilancia 2) devo calcolare la differenza di peso rispetto all'ingresso
	 * Quindi nella MAP inserisco codiceBatteria,peso
	 */
		private static LoggerFile log = new LoggerFile();
		private static Map<String, Integer> batterie = new HashMap<>();
		
		//se è la prima bilancia inserisco la batteria, se è la seconda, prelevo il valore della prima e ne faccio la differenza. cancello
		public int inserisci(String codice, int valore, int bilancia) {
				
			try {
					if (bilancia==1) {
						batterie.put(codice, valore);
						//log.write("\nCONTROLLO RIEMPIMENTO ACIDO. DIMENSIONI MAP =" + batterie.size());
						return Setting.BATTERIA_OK; //OK
					}
					if (bilancia==2) {
						
						int valoreIniziale = 0;
						int differenza = 0;
						
						if (batterie.containsKey(codice))
							valoreIniziale = batterie.get(codice);
						else
							return Setting.BATTERIA_OK;
												
						differenza = valore - valoreIniziale;
						
						try {
							batterie.remove(codice);
						}catch(Exception k) {		}
							
						//log.write("\nCONTROLLO RIEMPIMENTO ACIDO. DIMENSIONI MAP =" + batterie.size());
						
						String tipo = codice.substring(0,2);
		   				if (tipo.equals("12")) {
		   					//BTX12
		   					if ((differenza>=Setting.BTX12_min_acido)&&(differenza<=Setting.BTX12_max_acido)) return Setting.BATTERIA_OK;
		   				}
		   				if (tipo.equals("14")) {
		   					//BTX14
		   					if ((differenza>=Setting.BTX14_min_acido)&&(differenza<=Setting.BTX14_max_acido)) return Setting.BATTERIA_OK;
		   				}
						
						//ritorno 7 se non va bene
		   				//log.write("\nCONTROLLO RIEMPIMENTO ACIDO. KO BILANCIA 2. Codice="+codice+" valore="+valore+"-  bilancia="+bilancia);
		   				return Setting.ESITO_KO_BILANCIA;
					}
			}catch(Exception j) {
				log.write("\nCONTROLLO RIEMPIMENTO ACIDO. ERRORE =" + j.toString());
			}
			
			return -1;
		}//fine metodo
	
	
}//fine classe
