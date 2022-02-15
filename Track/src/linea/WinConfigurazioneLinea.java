package linea;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;

public class WinConfigurazioneLinea extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JToggleButton btn_scarto_1;
	private JToggleButton btn_scarto_2;
	private JToggleButton btn_scarto_3;
	private JToggleButton btn_scarto_4;
	private JToggleButton btn_scarto_5;
	private JToggleButton btn_scarto_6;
	private JToggleButton btn_scarto_7;
	private JToggleButton btn_scarto_10;

	private JToggleButton btn_lettore_1;
	private JToggleButton btn_lettore_2;
	private JToggleButton btn_lettore_3;
	private JToggleButton btn_lettore_4;
	private JToggleButton btn_lettore_5;
	private JToggleButton btn_lettore_6;
	private JToggleButton btn_lettore_7;
	private JToggleButton btn_lettore_10;

	private AtomoConfigurazioneLinea[] dateList = new AtomoConfigurazioneLinea[50];
	private ConfiguratoreLinea configuratore;
	private JTextField statoconfigurazione;
	private static LoggerFile log = new LoggerFile();

	// private JTextArea monitor;

	/**
	 * Launch the application.
	 */

	public WinConfigurazioneLinea() {

	}

	public WinConfigurazioneLinea(JTextArea m) {
		setBounds(70, 70, 899, 530);
		setTitle("IMPOSTAZIONI LINEA");
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);

		// monitor = m;

		JLabel lblNewLabel = new JLabel("CON. CORTI 1");
		lblNewLabel.setForeground(new Color(128, 0, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(35, 11, 86, 14);
		getContentPane().add(lblNewLabel);

		JLabel lblPostazione = new JLabel("PUNTATRICE 2");
		lblPostazione.setForeground(new Color(128, 0, 0));
		lblPostazione.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPostazione.setBounds(35, 97, 86, 14);
		getContentPane().add(lblPostazione);

		JLabel lblPostazione_1 = new JLabel("P. TENUTA 1");
		lblPostazione_1.setForeground(new Color(128, 0, 0));
		lblPostazione_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPostazione_1.setBounds(35, 195, 86, 14);
		getContentPane().add(lblPostazione_1);

		btn_scarto_1 = new JToggleButton("POSTAZIONE DISATTIVATA");
		btn_scarto_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_scarto_1.isSelected())
					btn_scarto_1.setText("SCARTO ATTIVATO");
				else
					btn_scarto_1.setText("SCARTO DISATTIVATO");
			}

		});
		btn_scarto_1.setBackground(new Color(192, 192, 192));
		btn_scarto_1.setBounds(35, 36, 172, 32);
		getContentPane().add(btn_scarto_1);

		btn_lettore_1 = new JToggleButton("LETTORE ATTIVO");
		btn_lettore_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_lettore_1.isSelected())
					btn_lettore_1.setText("LETTORE ATTIVO");
				else
					btn_lettore_1.setText("LETTORE DISATTIVATO");
			}
		});
		btn_lettore_1.setSelected(true);
		btn_lettore_1.setBackground(Color.LIGHT_GRAY);
		btn_lettore_1.setBounds(217, 36, 172, 32);
		getContentPane().add(btn_lettore_1);

		JLabel lblPostazione_2_2 = new JLabel("VERIFICA");
		lblPostazione_2_2.setForeground(new Color(128, 0, 0));
		lblPostazione_2_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPostazione_2_2.setBounds(507, 295, 100, 14);
		getContentPane().add(lblPostazione_2_2);

		JLabel lblPostazione_2_3 = new JLabel("P. TENUTA 2");
		lblPostazione_2_3.setForeground(new Color(128, 0, 0));
		lblPostazione_2_3.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPostazione_2_3.setBounds(507, 195, 86, 14);
		getContentPane().add(lblPostazione_2_3);

		JLabel lblPostazione_2_4 = new JLabel("ALT. POLARI");
		lblPostazione_2_4.setForeground(new Color(128, 0, 0));
		lblPostazione_2_4.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPostazione_2_4.setBounds(35, 295, 86, 14);
		getContentPane().add(lblPostazione_2_4);

		JLabel lblPostazione_2_5 = new JLabel("CON. CORTI 2");
		lblPostazione_2_5.setForeground(new Color(128, 0, 0));
		lblPostazione_2_5.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPostazione_2_5.setBounds(507, 97, 86, 14);
		getContentPane().add(lblPostazione_2_5);

		JLabel lblPostazione_2_6 = new JLabel("PUNTATRICE 1");
		lblPostazione_2_6.setForeground(new Color(128, 0, 0));
		lblPostazione_2_6.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPostazione_2_6.setBounds(512, 11, 86, 14);
		getContentPane().add(lblPostazione_2_6);

		btn_scarto_2 = new JToggleButton("POSTAZIONE DISATTIVATA");
		btn_scarto_2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_scarto_2.isSelected())
					btn_scarto_2.setText("POSTAZIONE ATTIVA");
				else
					btn_scarto_2.setText("POSTAZIONE DISATTIVATA");
			}
		});
		btn_scarto_2.setBackground(Color.LIGHT_GRAY);
		btn_scarto_2.setBounds(507, 36, 172, 32);
		getContentPane().add(btn_scarto_2);

		btn_lettore_2 = new JToggleButton("LETTORE ATTIVO");
		btn_lettore_2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_lettore_2.isSelected())
					btn_lettore_2.setText("LETTORE ATTIVO");
				else
					btn_lettore_2.setText("LETTORE DISATTIVATO");
			}
		});
		btn_lettore_2.setSelected(true);
		btn_lettore_2.setBackground(Color.LIGHT_GRAY);
		btn_lettore_2.setBounds(689, 36, 172, 32);
		getContentPane().add(btn_lettore_2);

		btn_scarto_3 = new JToggleButton("POSTAZIONE DISATTIVATA");
		btn_scarto_3.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_scarto_3.isSelected())
					btn_scarto_3.setText("POSTAZIONE ATTIVA");
				else
					btn_scarto_3.setText("POSTAZIONE DISATTIVATA");
			}
		});
		btn_scarto_3.setBackground(Color.LIGHT_GRAY);
		btn_scarto_3.setBounds(35, 122, 172, 32);
		getContentPane().add(btn_scarto_3);

		btn_lettore_3 = new JToggleButton("LETTORE ATTIVO");
		btn_lettore_3.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_lettore_3.isSelected())
					btn_lettore_3.setText("LETTORE ATTIVO");
				else
					btn_lettore_3.setText("LETTORE DISATTIVATO");
			}
		});
		btn_lettore_3.setSelected(true);
		btn_lettore_3.setBackground(Color.LIGHT_GRAY);
		btn_lettore_3.setBounds(217, 122, 172, 32);
		getContentPane().add(btn_lettore_3);

		btn_scarto_4 = new JToggleButton("POSTAZIONE DISATTIVATA");
		btn_scarto_4.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_scarto_4.isSelected())
					btn_scarto_4.setText("POSTAZIONE ATTIVA");
				else
					btn_scarto_4.setText("POSTAZIONE DISATTIVATA");
			}
		});
		btn_scarto_4.setBackground(Color.LIGHT_GRAY);
		btn_scarto_4.setBounds(507, 122, 172, 32);
		getContentPane().add(btn_scarto_4);

		btn_lettore_4 = new JToggleButton("LETTORE ATTIVO");
		btn_lettore_4.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_lettore_4.isSelected())
					btn_lettore_4.setText("LETTORE ATTIVO");
				else
					btn_lettore_4.setText("LETTORE DISATTIVATO");
			}
		});
		btn_lettore_4.setSelected(true);
		btn_lettore_4.setBackground(Color.LIGHT_GRAY);
		btn_lettore_4.setBounds(689, 122, 172, 32);
		getContentPane().add(btn_lettore_4);

		btn_scarto_5 = new JToggleButton("POSTAZIONE DISATTIVATA");
		btn_scarto_5.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_scarto_5.isSelected())
					btn_scarto_5.setText("POSTAZIONE ATTIVA");
				else
					btn_scarto_5.setText("POSTAZIONE DISATTIVATA");
			}
		});
		btn_scarto_5.setBackground(Color.LIGHT_GRAY);
		btn_scarto_5.setBounds(35, 220, 172, 32);
		getContentPane().add(btn_scarto_5);

		btn_lettore_5 = new JToggleButton("LETTORE ATTIVO");
		btn_lettore_5.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_lettore_5.isSelected())
					btn_lettore_5.setText("LETTORE ATTIVO");
				else
					btn_lettore_5.setText("LETTORE DISATTIVATO");
			}
		});
		btn_lettore_5.setSelected(true);
		btn_lettore_5.setBackground(Color.LIGHT_GRAY);
		btn_lettore_5.setBounds(217, 220, 172, 32);
		getContentPane().add(btn_lettore_5);

		btn_scarto_6 = new JToggleButton("POSTAZIONE DISATTIVATA");
		btn_scarto_6.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_scarto_6.isSelected())
					btn_scarto_6.setText("POSTAZIONE ATTIVA");
				else
					btn_scarto_6.setText("POSTAZIONE DISATTIVATA");
			}
		});
		btn_scarto_6.setBackground(Color.LIGHT_GRAY);
		btn_scarto_6.setBounds(507, 220, 172, 32);
		getContentPane().add(btn_scarto_6);

		btn_lettore_6 = new JToggleButton("LETTORE ATTIVO");
		btn_lettore_6.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_lettore_6.isSelected())
					btn_lettore_6.setText("LETTORE ATTIVO");
				else
					btn_lettore_6.setText("LETTORE DISATTIVATO");
			}
		});
		btn_lettore_6.setSelected(true);
		btn_lettore_6.setBackground(Color.LIGHT_GRAY);
		btn_lettore_6.setBounds(689, 220, 172, 32);
		getContentPane().add(btn_lettore_6);

		btn_scarto_7 = new JToggleButton("POSTAZIONE DISATTIVATA");
		btn_scarto_7.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_scarto_7.isSelected())
					btn_scarto_7.setText("POSTAZIONE ATTIVA");
				else
					btn_scarto_7.setText("POSTAZIONE DISATTIVATA");
			}
		});
		btn_scarto_7.setBackground(Color.LIGHT_GRAY);
		btn_scarto_7.setBounds(35, 320, 172, 32);
		getContentPane().add(btn_scarto_7);

		btn_lettore_7 = new JToggleButton("LETTORE ATTIVO");
		btn_lettore_7.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_lettore_7.isSelected())
					btn_lettore_7.setText("LETTORE ATTIVO");
				else
					btn_lettore_7.setText("LETTORE DISATTIVATO");
			}
		});
		btn_lettore_7.setSelected(true);
		btn_lettore_7.setBackground(Color.LIGHT_GRAY);
		btn_lettore_7.setBounds(217, 320, 172, 32);
		getContentPane().add(btn_lettore_7);

		btn_scarto_10 = new JToggleButton("POSTAZIONE DISATTIVATA");
		btn_scarto_10.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_scarto_10.isSelected())
					btn_scarto_10.setText("POSTAZIONE ATTIVA");
				else
					btn_scarto_10.setText("POSTAZIONE DISATTIVATA");
			}
		});
		btn_scarto_10.setBackground(Color.LIGHT_GRAY);
		btn_scarto_10.setBounds(507, 320, 172, 32);
		getContentPane().add(btn_scarto_10);

		btn_lettore_10 = new JToggleButton("LETTORE ATTIVO");
		btn_lettore_10.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (btn_lettore_10.isSelected())
					btn_lettore_10.setText("LETTORE ATTIVO");
				else
					btn_lettore_10.setText("LETTORE DISATTIVATO");
			}
		});
		btn_lettore_10.setSelected(true);
		btn_lettore_10.setBackground(Color.LIGHT_GRAY);
		btn_lettore_10.setBounds(689, 320, 172, 32);
		getContentPane().add(btn_lettore_10);

		JButton btn_salva = new JButton("SALVA CONFIGURAZIONE");
		btn_salva.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				salvaDatiConfigurazione();
			}
		});
		btn_salva.setBackground(SystemColor.inactiveCaptionBorder);
		btn_salva.setBounds(351, 427, 189, 32);
		getContentPane().add(btn_salva);

		statoconfigurazione = new JTextField();
		statoconfigurazione.setForeground(new Color(165, 42, 42));
		statoconfigurazione.setFont(new Font("Tahoma", Font.ITALIC, 10));
		statoconfigurazione.setHorizontalAlignment(SwingConstants.RIGHT);
		statoconfigurazione.setBounds(656, 433, 205, 20);
		getContentPane().add(statoconfigurazione);
		statoconfigurazione.setColumns(10);

		caricaDatiConfigurazione();
		statoconfigurazione.setText("configurazione caricata");

	} // fine init

	public void caricaDatiConfigurazione() {

		log.write("WinConfigurazione - inizio caricamento configurazione ");

		try {
			configuratore = new ConfiguratoreLinea();
		} catch (Exception h) {
			statoconfigurazione.setText("errore configuratore");
			log.write("WinConfigurazione - costruttore errore: " + h.toString());
		}

		try {
			dateList = configuratore.getListaAtomoConfigurazione();

			// AtomoConfigurazioneLinea atomo = dateList[0];

			if (dateList[0].statoscanner > 0)
				btn_lettore_1.setSelected(true);
			else
				btn_lettore_1.setSelected(false);

			if (dateList[0].scartoabilitato > 0)
				btn_scarto_1.setSelected(true);
			else
				btn_scarto_1.setSelected(false);
			// ------------------------------------------------------------
			if (dateList[1].statoscanner > 0)
				btn_lettore_2.setSelected(true);
			else
				btn_lettore_2.setSelected(false);

			if (dateList[1].scartoabilitato > 0)
				btn_scarto_2.setSelected(true);
			else
				btn_scarto_2.setSelected(false);
			// ------------------------------------------------------------
			if (dateList[2].statoscanner > 0)
				btn_lettore_3.setSelected(true);
			else
				btn_lettore_3.setSelected(false);

			if (dateList[2].scartoabilitato > 0)
				btn_scarto_3.setSelected(true);
			else
				btn_scarto_3.setSelected(false);
			// ------------------------------------------------------------
			if (dateList[3].statoscanner > 0)
				btn_lettore_4.setSelected(true);
			else
				btn_lettore_4.setSelected(false);

			if (dateList[3].scartoabilitato > 0)
				btn_scarto_4.setSelected(true);
			else
				btn_scarto_4.setSelected(false);
			// ------------------------------------------------------------
			if (dateList[4].statoscanner > 0)
				btn_lettore_5.setSelected(true);
			else
				btn_lettore_5.setSelected(false);

			if (dateList[4].scartoabilitato > 0)
				btn_scarto_5.setSelected(true);
			else
				btn_scarto_5.setSelected(false);
			// ------------------------------------------------------------
			try {
				if (dateList[5].statoscanner > 0)
					btn_lettore_6.setSelected(true);
				else
					btn_lettore_6.setSelected(false);

				if (dateList[5].scartoabilitato > 0)
					btn_scarto_6.setSelected(true);
				else
					btn_scarto_6.setSelected(false);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			// ------------------------------------------------------------
			try {
				if (dateList[6].statoscanner > 0)
					btn_lettore_7.setSelected(true);
				else
					btn_lettore_7.setSelected(false);

				if (dateList[6].scartoabilitato > 0)
					btn_scarto_7.setSelected(true);
				else
					btn_scarto_7.setSelected(false);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// ------------------------------------------------------------

			// QUESTO PULSANTE FA ECCEZIONE. POSTAZIONE DI CONTROLLO
			try {

				// ------------------------------------------------------------
				if (dateList[Setting.STAZIONE_DI_CONTROLLO_2 - 1].statoscanner > 0)
					btn_lettore_10.setSelected(true);
				else
					btn_lettore_10.setSelected(false);
				if (dateList[Setting.STAZIONE_DI_CONTROLLO_2 - 1].scartoabilitato > 0)
					btn_scarto_10.setSelected(true);
				else
					btn_scarto_10.setSelected(false);
			} catch (Exception e) {
				// TODO: handle exception
			}

		} catch (Exception h) {
			statoconfigurazione.setText("errore lista");
			log.write("442 WinConfigurazione - Errore carica dati " + h.toString());
		}

		log.write("WinConfigurazione - FINE caricamento configurazione ");

	}// fine metodo

	public void salvaDatiConfigurazione() {

		log.write("WinConfigurazione - INIZIO SALVATAGGIO\n");

		if (btn_lettore_1.isSelected())
			dateList[0].statoscanner = 256;
		else
			dateList[0].statoscanner = 0;

		if (btn_scarto_1.isSelected())
			dateList[0].scartoabilitato = 256;
		else
			dateList[0].scartoabilitato = 0;

		dateList[0].spare1 = 0;
		dateList[0].spare2 = 0;
		// ------------------------------------------------------------
		if (btn_lettore_2.isSelected())
			dateList[1].statoscanner = 256;
		else
			dateList[1].statoscanner = 0;

		if (btn_scarto_2.isSelected())
			dateList[1].scartoabilitato = 256;
		else
			dateList[1].scartoabilitato = 0;

		dateList[1].spare1 = 0;
		dateList[1].spare2 = 0;
		// ------------------------------------------------------------
		if (btn_lettore_3.isSelected())
			dateList[2].statoscanner = 256;
		else
			dateList[2].statoscanner = 0;

		if (btn_scarto_3.isSelected())
			dateList[2].scartoabilitato = 256;
		else
			dateList[2].scartoabilitato = 0;

		dateList[2].spare1 = 0;
		dateList[2].spare2 = 0;
		// ------------------------------------------------------------
		if (btn_lettore_4.isSelected())
			dateList[3].statoscanner = 256;
		else
			dateList[3].statoscanner = 0;

		if (btn_scarto_4.isSelected())
			dateList[3].scartoabilitato = 256;
		else
			dateList[3].scartoabilitato = 0;

		dateList[3].spare1 = 0;
		dateList[3].spare2 = 0;
		// ------------------------------------------------------------
		if (btn_lettore_5.isSelected())
			dateList[4].statoscanner = 256;
		else
			dateList[4].statoscanner = 0;

		if (btn_scarto_5.isSelected())
			dateList[4].scartoabilitato = 256;
		else
			dateList[4].scartoabilitato = 0;

		dateList[4].spare1 = 0;
		dateList[4].spare2 = 0;
		// ------------------------------------------------------------
		try {
			if (btn_lettore_6.isSelected())
				dateList[5].statoscanner = 256;
			else
				dateList[5].statoscanner = 0;

			if (btn_scarto_6.isSelected())
				dateList[5].scartoabilitato = 256;
			else
				dateList[5].scartoabilitato = 0;

			dateList[5].spare1 = 0;
			dateList[5].spare2 = 0;

			if (btn_lettore_7.isSelected())
				dateList[6].statoscanner = 256;
			else
				dateList[6].statoscanner = 0;

			if (btn_scarto_7.isSelected())
				dateList[6].scartoabilitato = 256;
			else
				dateList[6].scartoabilitato = 0;

			dateList[6].spare1 = 0;
			dateList[6].spare2 = 0;
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// ------------------------------------------------------------

		// ------------------------------------------------------------

		// QUESTO PULSANTE FA ECCEZIONE. POSTAZIONE DI CONTROLLO
		// ------------------------------------------------------------
		try {
			if (btn_lettore_10.isSelected())
				dateList[Setting.STAZIONE_DI_CONTROLLO_2 - 1].statoscanner = 256;
			else
				dateList[Setting.STAZIONE_DI_CONTROLLO_2 - 1].statoscanner = 0;

			if (btn_scarto_10.isSelected())
				dateList[Setting.STAZIONE_DI_CONTROLLO_2 - 1].scartoabilitato = 256;
			else
				dateList[Setting.STAZIONE_DI_CONTROLLO_2 - 1].scartoabilitato = 0;

			dateList[Setting.STAZIONE_DI_CONTROLLO_2 - 1].spare1 = 0;
			dateList[Setting.STAZIONE_DI_CONTROLLO_2 - 1].spare2 = 0;
			// ------------------------------------------------------------
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		configuratore.setListaAtomoConfigurazione(dateList.clone());

		log.write("WinConfigurazione - Fine salvataggio ");

		caricaDatiConfigurazione();

		JOptionPane.showMessageDialog(getContentPane(), "Configurazione Salvata!", "OK",
				JOptionPane.INFORMATION_MESSAGE);

	}// fine metodo

	public void salvaDatiConfigurazionePostazioneInterblocco(JToggleButton btn_scarto) {

		log.write("WinConfigurazione - INIZIO SALVATAGGIO INTERBLOCCO\n");

		try {
			configuratore = new ConfiguratoreLinea();
		} catch (Exception h) {
			log.write("WinConfigurazione - SALVAiNTERBLOCCO errore: " + h.toString());
		}

		dateList = configuratore.getListaAtomoConfigurazione();

		if (btn_scarto.isSelected())
			dateList[(Setting.STAZIONE_DI_CONTROLLO_2) - 1].scartoabilitato = 256;
		else
			dateList[(Setting.STAZIONE_DI_CONTROLLO_2) - 1].scartoabilitato = 0;

		dateList[Setting.STAZIONE_DI_CONTROLLO_2 - 1].spare1 = 0;
		dateList[Setting.STAZIONE_DI_CONTROLLO_2 - 1].spare2 = 0;

		configuratore.setListaAtomoConfigurazione(dateList.clone());

		log.write("WinConfigurazione - Fine salvataggio ");

		JOptionPane.showMessageDialog(getContentPane(), "CONTROLLO MODIFICATO!", "ATTENZIONE",
				JOptionPane.WARNING_MESSAGE);

	}// fine metodo

	public void caricaDatiConfigurazionePostazioneInterblocco(JToggleButton btn_scarto) throws Exception {

		log.write("WinConfigurazione - INIZIO CARICAMENTO DATI INTERBLOCCO\n");

		configuratore = new ConfiguratoreLinea();

		dateList = configuratore.getListaAtomoConfigurazione();

		AtomoConfigurazioneLinea atomo = dateList[Setting.STAZIONE_DI_CONTROLLO_2 - 1];

		try {
			if (atomo.scartoabilitato > 0)
				btn_scarto.setSelected(true);
			else
				btn_scarto.setSelected(false);

			if (btn_scarto.isSelected()) {
				// btn_scarto.setText("CONTROLLO ATTIVO");
				btn_scarto.setBackground(Setting.verde);
			} else {
				// btn_scarto.setText("CONTROLLO DISATTIVATO");
				btn_scarto.setBackground(Setting.grigio);
			}
		} catch (Exception g) {
			log.write("618 WinConfigurazione - ERRORE configurazione postazione controllo\n");
		}

		log.write("WinConfigurazione - FINE CARICAMENTO DATI INTERBLOCCO\n");

	}// fine metodo

}// fine classe
