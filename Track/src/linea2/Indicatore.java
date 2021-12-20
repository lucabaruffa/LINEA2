package linea2;
import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Indicatore {
		private JTextField tempo;
		public JTextField stato;
		public JTextField batteria;
		public JTextField batteriaZero;
		public static JTextField statodb;
		public static JTextField statoplc;
		public JTextField conteggio;
		public JTextField risultato;
		//public static JTextArea monitor;
		public JTextField statoLinea;
		public JTextField tempostatoLinea;
		public JTextField riprocessato;
		public JTextField scarto;
		
		public Color rosso = new Color(255, 135, 128);
		public Color verde = new Color(189, 255, 198);
		public Color arancio = new Color(245, 152, 66);
		public Color bianco = new Color(250, 250, 250);
		public Color grigio = Color.lightGray;
		private Color coloreTransizione = new Color(250, 255, 150);
		
		public Indicatore() {
			
		}
		
		public Indicatore(JTextField t , JTextField s , JTextField b, JTextField splc,JTextField sdb,JTextField con,JTextField batteriaZ,JTextField risult,JTextField statolin,JTextField tempostato,JTextField riproces,JTextField scart) {
			tempo = t;
			stato = s;
			batteria = b;
			statodb =sdb;
			statoplc = splc;
			conteggio = con;
			batteriaZero = batteriaZ;
			risultato = risult;
			statoLinea = statolin;
			tempostatoLinea = tempostato;
			riprocessato=riproces;
			scarto=scart;
		}//fine costruttore
		
		public Indicatore getIndicatore() {
			return this;
		}
		
		public void setTempo(String t) {
			tempo.setText(t);
		}
		
		public void setBatteria(String t) {
					
			//Color colore = batteria.getBackground();
			String valore = batteria.getText();
			batteria.setText(t);
			
			if (!valore.equals(t)) {
				Runnable target = new Runnable() {
						@Override
						public void run() {
							batteria.setBackground(coloreTransizione);
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							batteria.setBackground(bianco);
							//batteria.setText("IN ATTESA");
							
						}
						};
					
					new Thread(target).start();
					
			}//fine if
			
			
			
		}//fine setbatteria
		
		
		public void setBatteriaZero(String t) {
			
			Color colore = batteriaZero.getBackground();
			String valore = batteriaZero.getText();
			batteriaZero.setText(t);
			
			if (!valore.equals(t)) {
				Runnable target = new Runnable() {
						@Override
						public void run() {
							batteriaZero.setBackground(coloreTransizione);
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							batteriaZero.setBackground(bianco);
							//batteria.setText("IN ATTESA");
							
						}
						};
					
					new Thread(target).start();
					
			}//fine if
			
			
			
		}//fine setbatteria
		
		public void setStato(String t) {
			
				stato.setText(t);
				
		}//fine set stato
		
		public void setConteggio(String v) {
			Color colore = conteggio.getBackground();
			String valore = conteggio.getText();
			conteggio.setText(v);
			
			if (!valore.equals(v)) {
				Runnable target = new Runnable() {
						@Override
						public void run() {
							conteggio.setBackground(coloreTransizione);
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							conteggio.setBackground(colore);
							
						}
						};
					
					new Thread(target).start();
					
			}//fine if
		
		}
		/*
		public void setMonitor(JTextArea moni) {
			monitor = moni;
		}
		*/
		
		
	}//fine classe indicarore
