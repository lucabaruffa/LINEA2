package linea2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import DB.DBCommand;
import DB.giustificativoDB;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class Giustificativo extends JDialog implements Runnable {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtFermo;
	private int ID;
	private DBCommand comando = new DBCommand();
	private JComboBox giustificativo;
	private static LoggerFile log = new LoggerFile();
	private giustificativoDB giustificativo_db;
	private JTextField txtCodiceFinale;
	private String codice_finale="0000000";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Giustificativo dialog = new Giustificativo();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Giustificativo() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Giustificativo.class.getResource("/resource/alert4.png")));
		setResizable(false);
		
		//Setting.winGiustificativo = this;
		giustificativo_db = new giustificativoDB();
		System.out.println("gIUSTIFICATIVO creato!");
		GUI();
		
		
	}//fine costruttore
	

	public void GUI() {
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//setVisible(true);
		setBounds(100, 100, 1283, 437);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		//setVisible(true);
		
		setTitle("GIUSTIFICATIVO FERMATA");
		//setBounds(100, 100, 637, 389);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtFermo = new JTextField();
		txtFermo.setHorizontalAlignment(SwingConstants.CENTER);
		txtFermo.setEditable(false);
		txtFermo.setFont(new Font("Arial", Font.BOLD, 18));
		txtFermo.setBounds(10, 84, 1220, 50);
		contentPanel.add(txtFermo);
		txtFermo.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("LA LINEA E' IN STATO DI FERMO");
		lblNewLabel.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/alert4.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 23));
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(10, 22, 1220, 62);
		contentPanel.add(lblNewLabel);
		
		giustificativo = new JComboBox();
		giustificativo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					//System.out.println("cambio");
					String sub1 = codice_finale.substring(1,codice_finale.length());
					String val1 = (""+giustificativo.getSelectedItem()).split("-")[0];
					codice_finale = val1 + sub1;
					txtCodiceFinale.setText(codice_finale);
				}
				
				
			}
		});
		giustificativo.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo.setModel(new DefaultComboBoxModel(new String[] {"0-Seleziona..", "A-Programmato", "B-Sicurezza", "C-Produttivit\u00E0", "D-Qualit\u00E0"}));
		giustificativo.setBounds(10, 194, 167, 78);
		contentPanel.add(giustificativo);
		
		JLabel lblNewLabel_1 = new JLabel("Descrizione");
		lblNewLabel_1.setBounds(10, 173, 120, 14);
		contentPanel.add(lblNewLabel_1);
		
		JComboBox giustificativo2 = new JComboBox();
		giustificativo2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0,1);
					String sub2 = codice_finale.substring(2,codice_finale.length());
					String val1 = (""+giustificativo2.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 +" - sub2=" + sub2 );
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}
				
			}
		});
		giustificativo2.setModel(new DefaultComboBoxModel(new String[] {"0-Seleziona...", "1-Macchina Ferma", "2-Macchina lavora", "3-Macchina lavora con inceppamenti"}));
		giustificativo2.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo2.setBounds(187, 194, 362, 78);
		contentPanel.add(giustificativo2);
		
		JLabel lblNewLabel_1_1 = new JLabel("Macchina/Attrezzatura");
		lblNewLabel_1_1.setBounds(187, 173, 188, 14);
		contentPanel.add(lblNewLabel_1_1);
		
		JComboBox giustificativo3 = new JComboBox();
		giustificativo3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0,2);
					String sub2 = codice_finale.substring(3,codice_finale.length());
					String val1 = (""+giustificativo3.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 +" - sub2=" + sub2 );
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}
			}
		});
		giustificativo3.setModel(new DefaultComboBoxModel(new String[] {"0-Seleziona...", "1-Elettrico", "2-Fluidico", "3-Pneumatico", "4-Meccanico"}));
		giustificativo3.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo3.setBounds(559, 194, 179, 78);
		contentPanel.add(giustificativo3);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Tipologia");
		lblNewLabel_1_1_1.setBounds(559, 173, 160, 14);
		contentPanel.add(lblNewLabel_1_1_1);
		
		JComboBox giustificativo4 = new JComboBox();
		giustificativo4.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0,3);
					String sub2 = codice_finale.substring(5,codice_finale.length());
					String val1 = (""+giustificativo4.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 +" - sub2=" + sub2 );
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}
			}
		});
		giustificativo4.setModel(new DefaultComboBoxModel(new String[] {"00-Seleziona...", "01-Cannello", "02-Catena", "03-Cavo elettrico", "04-cCnghia", "05-Fine corsa", "06-Manometro", "07-Motore", "08-Motore elettrico", "09-Motoriduttore", "10-Pannello", "11-Pettine-caricatore", "12-Pistone", "13-Plc", "14-Pompa", "15-Raccordo", "16-Riduttore pressione", "17-Riparo macchina", "18-Sensore", "19-Sonda NTC", "20-Sonda PTC", "21-Staffa", "22-Stampo a gravit\u00E0", "23-Vite"}));
		giustificativo4.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo4.setBounds(748, 194, 236, 78);
		contentPanel.add(giustificativo4);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Componente");
		lblNewLabel_1_1_1_1.setBounds(748, 173, 188, 14);
		contentPanel.add(lblNewLabel_1_1_1_1);
		
		JComboBox giustificativo5 = new JComboBox();
		giustificativo5.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0,5);
					String sub2 = codice_finale.substring(7,codice_finale.length());
					String val1 = (""+giustificativo5.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 +" - sub2=" + sub2 );
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}
			}
		});
		giustificativo5.setModel(new DefaultComboBoxModel(new String[] {"00-Seleziona...", "01-Guasto ", "02-Danneggiato", "03-Perde", "04-Deteriorato", "05-Mancante"}));
		giustificativo5.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo5.setBounds(994, 194, 236, 78);
		contentPanel.add(giustificativo5);
		
		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Stato Componente");
		lblNewLabel_1_1_1_1_1.setBounds(994, 173, 188, 14);
		contentPanel.add(lblNewLabel_1_1_1_1_1);
		
		txtCodiceFinale = new JTextField();
		txtCodiceFinale.setForeground(Color.RED);
		txtCodiceFinale.setFont(new Font("Arial", Font.BOLD, 20));
		txtCodiceFinale.setEditable(false);
		txtCodiceFinale.setBounds(10, 303, 1220, 40);
		contentPanel.add(txtCodiceFinale);
		txtCodiceFinale.setColumns(10);
		
		txtCodiceFinale.setText(codice_finale);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(SystemColor.inactiveCaption);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("INSERISCI MOTIVAZIONE");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						
						
						if (!comando.invia_segnalazione(codice_finale+"2", ID))
							JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!","ATTENZIONE",JOptionPane.ERROR_MESSAGE);
						else {
							JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente","OK",JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
							
							
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		//setVisible(true);
	}//fine gui
	
	
public void setFermo(String testo) {
		
		txtFermo.setText(testo);
		
	}

@Override
public void run() {
	// TODO Auto-generated method stub
	try {
		System.out.println("GIUSTIFICATIVO RUNNING!");
		//verifico i dati di stop
		ID = giustificativo_db.getFermata();
		System.out.println("GIUSTIFICATIVO ID=" + ID);
		if ((Setting.minuti_fermo_linea >=Setting.tempoMaxLineaFerma)) {
			
			setFermo("La Linea e' ferma da " + Setting.minuti_fermo_linea + " minuti. Necessario giustificare");
			this.setVisible(true);
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		log.write("Errore Giustificativo. Err:" + e.toString() );
	}
}//fine run
}//fine classdes
