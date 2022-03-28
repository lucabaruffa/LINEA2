package linea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
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

import DB.DBCheckGriglia;
import DB.DBCommand;
import DB.giustificativoDB;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.GridLayout;


public class Giustificativo extends JDialog implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtFermo;
	private int ID;
	private DBCommand comando = new DBCommand();
	private JComboBox giustificativo;
	private static LoggerFile log = new LoggerFile();
	private giustificativoDB giustificativo_db;
	private JTextField txtCodiceFinale;
	private String codice_finale = "00000000000";

	public static ArrayList<String> array_giustificativo1 = new ArrayList<>();
	public static ArrayList<String> array_giustificativo2 = new ArrayList<>();
	public static ArrayList<String> array_giustificativo3 = new ArrayList<>();
	public static ArrayList<String> array_giustificativo4 = new ArrayList<>();
	public static ArrayList<String> array_giustificativo5 = new ArrayList<>();
	private static DBCheckGriglia dbgriglia;

	private static ArrayListComboBoxModel model1;
	private static ArrayListComboBoxModel model2;
	private static ArrayListComboBoxModel model3;
	private static ArrayListComboBoxModel model4;
	private static ArrayListComboBoxModel model5;
	private JTextField txtInizioFermo;
	private JTextField txtBadge;

	JComboBox<?> giustificativo7;
	JComboBox<?> giustificativo6;
	JComboBox<?> giustificativo5;
	JComboBox<?> giustificativo4;
	JComboBox<?> giustificativo3;
	JComboBox<?> giustificativo2;
	
	
	
	JTextArea txtNote = null;
	
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

		// Setting.winGiustificativo = this;
		giustificativo_db = new giustificativoDB();
		System.out.println("gIUSTIFICATIVO creato!");
		loadVectorfromDB();
		GUI();

	}// fine costruttore

	public void GUI() {
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		// setVisible(true);
		setBounds(100, 100, 1563, 703);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		// setVisible(true);

		setTitle("GIUSTIFICATIVO FERMATA");
		// setBounds(100, 100, 637, 389);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.setLayout(null);

		txtFermo = new JTextField();
		txtFermo.setForeground(new Color(178, 34, 34));
		txtFermo.setHorizontalAlignment(SwingConstants.CENTER);
		txtFermo.setEditable(false);
		txtFermo.setFont(new Font("Arial", Font.BOLD, 22));
		txtFermo.setBounds(508, 107, 1022, 56);
		contentPanel.add(txtFermo);
		txtFermo.setColumns(10);

		JLabel lblNewLabel = new JLabel("LINEA IN STATO DI FERMO");
		lblNewLabel.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/alert4.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 23));
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(10, 11, 663, 70);
		contentPanel.add(lblNewLabel);

		giustificativo = new JComboBox();
		giustificativo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// System.out.println("cambio");
					String sub1 = codice_finale.substring(1, codice_finale.length());
					String val1 = ("" + giustificativo.getSelectedItem()).split("-")[0];
					codice_finale = val1 + sub1;
					txtCodiceFinale.setText(codice_finale);
				}

			}
		});
		giustificativo.setFont(new Font("Arial", Font.BOLD, 20));

		giustificativo.setModel(model1);
		giustificativo.setBounds(10, 194, 204, 78);
		contentPanel.add(giustificativo);

		JLabel lblNewLabel_1 = new JLabel("Descrizione");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1.setBounds(10, 173, 120, 14);
		contentPanel.add(lblNewLabel_1);

		giustificativo2 = new JComboBox();
		giustificativo2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0, 1);
					String sub2 = codice_finale.substring(3, codice_finale.length());
					String val1 = ("" + giustificativo2.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 + " - sub2=" + sub2);
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}

			}
		});
		giustificativo2.setModel(model2);
		giustificativo2.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo2.setBounds(224, 194, 411, 78);
		contentPanel.add(giustificativo2);

		JLabel lblNewLabel_1_1 = new JLabel("Macchina/Attrezzatura");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_1.setBounds(224, 174, 188, 14);
		contentPanel.add(lblNewLabel_1_1);

		giustificativo3 = new JComboBox();
		giustificativo3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0, 4);
					String sub2 = codice_finale.substring(6, codice_finale.length());
					String val1 = ("" + giustificativo3.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 + " - sub2=" + sub2);
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}
			}
		});
		giustificativo3.setModel(model3);
		giustificativo3.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo3.setBounds(1039, 194, 491, 78);
		contentPanel.add(giustificativo3);

		JLabel lblNewLabel_1_1_1 = new JLabel("Tipologia");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_1_1.setBounds(1039, 172, 236, 14);
		contentPanel.add(lblNewLabel_1_1_1);

		giustificativo4 = new JComboBox();
		giustificativo4.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0, 6);
					String sub2 = codice_finale.substring(8, codice_finale.length());
					String val1 = ("" + giustificativo4.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 + " - sub2=" + sub2);
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}
			}
		});
		giustificativo4.setModel(model4);
		giustificativo4.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo4.setBounds(10, 304, 838, 78);
		contentPanel.add(giustificativo4);

		JLabel lblNewLabel_1_1_1_1 = new JLabel("Componente");
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_1_1_1.setBounds(10, 283, 625, 14);
		contentPanel.add(lblNewLabel_1_1_1_1);

		giustificativo5 = new JComboBox();
		giustificativo5.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0, 8);
					String sub2 = codice_finale.substring(10, codice_finale.length());
					String val1 = ("" + giustificativo5.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 + " - sub2=" + sub2);
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}
			}
		});
		giustificativo5.setModel(model5);
		giustificativo5.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo5.setBounds(858, 304, 384, 78);
		contentPanel.add(giustificativo5);

		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Stato Componente");
		lblNewLabel_1_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_1_1_1_1.setBounds(858, 283, 339, 14);
		contentPanel.add(lblNewLabel_1_1_1_1_1);

		txtCodiceFinale = new JTextField();
		txtCodiceFinale.setForeground(Color.RED);
		txtCodiceFinale.setFont(new Font("Arial", Font.BOLD, 28));
		txtCodiceFinale.setEditable(false);
		txtCodiceFinale.setBounds(10, 503, 282, 78);
		contentPanel.add(txtCodiceFinale);
		txtCodiceFinale.setColumns(10);

		txtCodiceFinale.setText(codice_finale);
		JButton okButton = 
				new JButton("INVIA MOTIVAZIONE");
		okButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		okButton.setForeground(new Color(0, 0, 0));
		okButton.setBackground(new Color(144, 238, 144));
		okButton.setBounds(302, 503, 286, 78);
		contentPanel.add(okButton);
		okButton.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/fermo.png")));
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				String badge = txtBadge.getText();

					if (check()){
						
						String descrizione_fermo = "";
						descrizione_fermo = giustificativo.getSelectedItem().toString();
						descrizione_fermo  += "-" +	 giustificativo2.getSelectedItem().toString().split("-")[1];
						descrizione_fermo  += "-" +	 giustificativo7.getSelectedItem().toString().split("-")[1];
						descrizione_fermo  += "-" +	 giustificativo3.getSelectedItem().toString().split("-")[1];
						descrizione_fermo  += "-" +	 giustificativo4.getSelectedItem().toString().split("-")[1];
						descrizione_fermo  += "-" +	 giustificativo5.getSelectedItem().toString().split("-")[1];
						descrizione_fermo  += "-" +	 giustificativo6.getSelectedItem().toString().split("-")[1];
						
						if (!comando.invia_segnalazione(codice_finale + "2", ID,txtNote.getText(), badge, descrizione_fermo ))
							JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!", "ATTENZIONE",
									JOptionPane.ERROR_MESSAGE);
						else {
							JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente", "OK",
									JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
					} else {
						JOptionPane.showMessageDialog(getContentPane(), "INSERIRE COGNOME O BADGE OPERATORE. VERIFICARE CHE TUTTI I CAMPI SIANO COMPILATI !!",
								"ATTENZIONE", JOptionPane.ERROR_MESSAGE);
					}

			}
		});
		okButton.setActionCommand("OK");
		getRootPane().setDefaultButton(okButton);

		JLabel lblNewLabel_1_2 = new JLabel("Situazione Linea");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_2.setBounds(1254, 283, 120, 14);
		contentPanel.add(lblNewLabel_1_2);

		giustificativo6 = new JComboBox();
		giustificativo6.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// System.out.println("cambio");
					String sub1 = codice_finale.substring(0, codice_finale.length() - 1);
					String val1 = ("" + giustificativo6.getSelectedItem()).split("-")[0];
					codice_finale = sub1 + val1;
					txtCodiceFinale.setText(codice_finale);
				}

			}
		});
		giustificativo6.setModel(new DefaultComboBoxModel(new String[] { "", "C-CRITICO", "N-NON CRITICO" }));
		giustificativo6.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo6.setBounds(1252, 304, 278, 78);
		contentPanel.add(giustificativo6);

		JLabel lblNewLabel_1_2_1 = new JLabel("Codice Inviato");
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_2_1.setBounds(10, 484, 120, 14);
		contentPanel.add(lblNewLabel_1_2_1);

		giustificativo7 = new JComboBox();
		giustificativo7.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0, 3);
					String sub2 = codice_finale.substring(4, codice_finale.length());
					String val1 = ("" + giustificativo7.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 + " - sub2=" + sub2);
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}

			}
		});
		giustificativo7.setModel(new DefaultComboBoxModel(
				new String[] { "", "1-MACCHINA FERMA", "2-MACCHINA LAVORA", "3-MACCHINA CON INCEPPAMENTI" }));
		giustificativo7.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo7.setBounds(645, 194, 384, 78);
		contentPanel.add(giustificativo7);

		JLabel lblNewLabel_1_1_1_2 = new JLabel("Stato");
		lblNewLabel_1_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_1_1_2.setBounds(645, 173, 160, 14);
		contentPanel.add(lblNewLabel_1_1_1_2);

		txtInizioFermo = new JTextField();
		txtInizioFermo.setEditable(false);
		txtInizioFermo.setForeground(new Color(178, 34, 34));
		txtInizioFermo.setHorizontalAlignment(SwingConstants.CENTER);
		txtInizioFermo.setFont(new Font("Arial", Font.BOLD, 22));
		txtInizioFermo.setColumns(10);
		txtInizioFermo.setBounds(10, 107, 488, 56);
		contentPanel.add(txtInizioFermo);

		JLabel lblNewLabel_1_3 = new JLabel("Data, ora e turno inizio fermo");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_3.setBounds(10, 88, 402, 14);
		contentPanel.add(lblNewLabel_1_3);

		JLabel lblNewLabel_1_1_1_3 = new JLabel("Cognome o Badge operatore");
		lblNewLabel_1_1_1_3.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_1_1_3.setBounds(1062, 11, 312, 14);
		contentPanel.add(lblNewLabel_1_1_1_3);

		txtBadge = new JTextField();
		txtBadge.setForeground(new Color(255, 0, 0));
		txtBadge.setFont(new Font("Arial", Font.BOLD, 20));
		txtBadge.setBounds(1062, 29, 468, 42);
		contentPanel.add(txtBadge);
		txtBadge.setColumns(10);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(SystemColor.inactiveCaptionBorder);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{

				JButton btnCambioLinea = new JButton("AVVIO LINEA");
				btnCambioLinea.setFont(new Font("Tahoma", Font.BOLD, 11));
				btnCambioLinea.setBackground(SystemColor.inactiveCaption);
				btnCambioLinea.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (!comando.invia_segnalazione("E000000" + Setting.DB_BATTERIE_NUM_LINEA, ID,""+txtNote.getText(), ""+txtBadge.getText(),"AVVIO LINEA"))
							JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!",
									"ATTENZIONE", JOptionPane.ERROR_MESSAGE);
						else {
							JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente", "OK",
									JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
					}
				});
				

				JButton btnPausaPranzo = new JButton("PAUSA MENSA");
				btnPausaPranzo.setFont(new Font("Tahoma", Font.BOLD, 11));
				btnPausaPranzo.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (txtBadge.getText().length()>3)
							if (!comando.invia_segnalazione("F000000" + Setting.DB_BATTERIE_NUM_LINEA, ID,""+txtNote.getText(), ""+txtBadge.getText(),"PAUSA PRANZO"))
								JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!",
										"ATTENZIONE", JOptionPane.ERROR_MESSAGE);
							else {
								JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente", "OK",
										JOptionPane.INFORMATION_MESSAGE);
								setVisible(false);
							}
						else
							JOptionPane.showMessageDialog(getContentPane(), "Inserisci Cognome o badge operatore !!",
									"ATTENZIONE", JOptionPane.WARNING_MESSAGE);
							
					}
				});
				buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 10));
				btnPausaPranzo.setBackground(SystemColor.inactiveCaption);
				btnPausaPranzo.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/cibo.png")));
				btnPausaPranzo.setActionCommand("OK");
				buttonPane.add(btnPausaPranzo);

				JButton btnPausaCaffe = new JButton("PAUSA CAFFE");
				btnPausaCaffe.setFont(new Font("Tahoma", Font.BOLD, 11));
				btnPausaCaffe.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (txtBadge.getText().length()>3)
							if (!comando.invia_segnalazione("G000000" + Setting.DB_BATTERIE_NUM_LINEA, ID,""+txtNote.getText(), ""+txtBadge.getText(),"PAUSA CAFFE"))
								JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!",
										"ATTENZIONE", JOptionPane.ERROR_MESSAGE);
							else {
								JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente", "OK",
										JOptionPane.INFORMATION_MESSAGE);
								setVisible(false);
							}
						else
							JOptionPane.showMessageDialog(getContentPane(), "Inserisci Cognome o badge operatore !!",
									"ATTENZIONE", JOptionPane.WARNING_MESSAGE);
					}
				});
				btnPausaCaffe.setBackground(SystemColor.inactiveCaption);
				btnPausaCaffe.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/coffee5.png")));
				btnPausaCaffe.setActionCommand("OK");
				buttonPane.add(btnPausaCaffe);
				
				JButton btnTurnoPrecedente = new JButton("TUR. PRECEDENTE");
				btnTurnoPrecedente.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (txtBadge.getText().length()>3)
							if (!comando.invia_segnalazione("M000000" + Setting.DB_BATTERIE_NUM_LINEA, ID,""+txtNote.getText(), ""+txtBadge.getText(),"NON GIUSTIFICATO T. PREC."))
								JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!", "ATTENZIONE",
										JOptionPane.ERROR_MESSAGE);
							else {
								JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente", "OK",
										JOptionPane.INFORMATION_MESSAGE);
								setVisible(false);
							}
						else
								JOptionPane.showMessageDialog(getContentPane(), "Inserisci Cognome o badge operatore !!",
										"ATTENZIONE", JOptionPane.WARNING_MESSAGE);	
					}
				});
				btnTurnoPrecedente.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/T_PRECEDENTE.png")));
				btnTurnoPrecedente.setFont(new Font("Tahoma", Font.BOLD, 11));
				btnTurnoPrecedente.setBackground(SystemColor.inactiveCaption);
				btnTurnoPrecedente.setActionCommand("OK");
				buttonPane.add(btnTurnoPrecedente);
				btnCambioLinea.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/cambiolinea.png")));
				btnCambioLinea.setActionCommand("OK");
				buttonPane.add(btnCambioLinea);
			}

			JButton btnFermoProgrammato = new JButton("FERMO PROGRAMMATO");
			btnFermoProgrammato.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (txtBadge.getText().length()>3)
						if (!comando.invia_segnalazione("H000000" + Setting.DB_BATTERIE_NUM_LINEA, ID,""+txtNote.getText(), ""+txtBadge.getText(),"FERMO PROGRAMMATO"))
							JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!", "ATTENZIONE",
									JOptionPane.ERROR_MESSAGE);
						else {
							JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente", "OK",
									JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
					else
							JOptionPane.showMessageDialog(getContentPane(), "Inserisci Cognome o badge operatore !!",
									"ATTENZIONE", JOptionPane.WARNING_MESSAGE);	
				}
			});

			JButton btnCambioProduzione = new JButton("CAMBIO PRODUZIONE");
			btnCambioProduzione.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/cambio_linea.png")));
			btnCambioProduzione.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnCambioProduzione.setBackground(SystemColor.inactiveCaption);
			btnCambioProduzione.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (txtBadge.getText().length()>3)
						if (!comando.invia_segnalazione("L000000" + Setting.DB_BATTERIE_NUM_LINEA, ID,""+txtNote.getText(), ""+txtBadge.getText(),"CAMBIO PRODUZIONE"))
							JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!", "ATTENZIONE",
									JOptionPane.ERROR_MESSAGE);
						else {
							JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente", "OK",
									JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
					else
						JOptionPane.showMessageDialog(getContentPane(), "Inserisci Cognome o badge operatore !!",
								"ATTENZIONE", JOptionPane.WARNING_MESSAGE);	
				}
			});
			btnCambioProduzione.setActionCommand("OK");
			buttonPane.add(btnCambioProduzione);
			btnFermoProgrammato.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnFermoProgrammato.setBackground(SystemColor.inactiveCaption);
			btnFermoProgrammato
					.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/fermoprogrammato.png")));
			btnFermoProgrammato.setActionCommand("OK");
			buttonPane.add(btnFermoProgrammato);

			JButton btnAssemblea = new JButton("ASSEMBLEA");
			btnAssemblea.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (txtBadge.getText().length()>3)
						if (!comando.invia_segnalazione("I000000" + Setting.DB_BATTERIE_NUM_LINEA, ID,""+txtNote.getText(), ""+txtBadge.getText(),"ASSEMBLEA"))
							JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!", "ATTENZIONE",
									JOptionPane.ERROR_MESSAGE);
						else {
							JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente", "OK",
									JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
					else
						JOptionPane.showMessageDialog(getContentPane(), "Inserisci Cognome o badge operatore !!",
								"ATTENZIONE", JOptionPane.WARNING_MESSAGE);	

				}
			});
			btnAssemblea.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/assemblea.png")));
			btnAssemblea.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnAssemblea.setBackground(SystemColor.inactiveCaption);
			btnAssemblea.setActionCommand("OK");
			buttonPane.add(btnAssemblea);

			JButton btnSciopero = new JButton("BLACKOUT");
			btnSciopero.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (txtBadge.getText().length()>3)
						if (!comando.invia_segnalazione("L000000" + Setting.DB_BATTERIE_NUM_LINEA, ID,""+txtNote.getText(), ""+txtBadge.getText(),"BLACKOUT"))
							JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!", "ATTENZIONE",
									JOptionPane.ERROR_MESSAGE);
						else {
							JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente", "OK",
									JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
					else
						JOptionPane.showMessageDialog(getContentPane(), "Inserisci Cognome o badge operatore !!",
								"ATTENZIONE", JOptionPane.WARNING_MESSAGE);	
				}
			});
			btnSciopero.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/battery.png")));
			btnSciopero.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnSciopero.setBackground(SystemColor.inactiveCaption);
			btnSciopero.setActionCommand("OK");
			buttonPane.add(btnSciopero);
		}

		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		giustificativo.setSelectedIndex(-1);
		giustificativo2.setSelectedIndex(-1);
		giustificativo3.setSelectedIndex(-1);
		giustificativo4.setSelectedIndex(-1);
		giustificativo5.setSelectedIndex(-1);
		giustificativo6.setSelectedIndex(-1);
		
		JLabel lblNewLabel_1_2_1_1 = new JLabel("Note");
		lblNewLabel_1_2_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_2_1_1.setBounds(10, 393, 120, 14);
		contentPanel.add(lblNewLabel_1_2_1_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 412, 1520, 56);
		contentPanel.add(scrollPane);
		
		txtNote = new JTextArea();
		scrollPane.setViewportView(txtNote);
		txtNote.setForeground(new Color(139, 0, 0));
		txtNote.setFont(new Font("Arial", Font.BOLD, 14));
		txtNote.setBackground(SystemColor.inactiveCaptionBorder);

	}// fine gui

	public void setFermo(String testo, String inizio) {

		txtFermo.setText(testo);
		txtInizioFermo.setText(inizio);

	}

	public void loadVectorfromDB() {

		// AVVIO IL LETTORE check control
		dbgriglia = new DBCheckGriglia();

		ArrayList<String>[] lista = dbgriglia.getGiustificativi();

		array_giustificativo1 = lista[0];
		array_giustificativo2 = lista[1];
		array_giustificativo3 = lista[2];
		array_giustificativo4 = lista[3];
		array_giustificativo5 = lista[4];

		model1 = new ArrayListComboBoxModel(array_giustificativo1);
		model2 = new ArrayListComboBoxModel(array_giustificativo2);
		model3 = new ArrayListComboBoxModel(array_giustificativo3);
		model4 = new ArrayListComboBoxModel(array_giustificativo4);
		model5 = new ArrayListComboBoxModel(array_giustificativo5);

	}// fine load

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("GIUSTIFICATIVO RUNNING!");
			// verifico i dati di stop
			ID = giustificativo_db.getFermata();
			System.out.println("GIUSTIFICATIVO ID=" + ID);
			
			
			if ((Setting.minuti_fermo_linea >= Setting.tempoMaxLineaFerma) && (ID > 0)) {

				setFermo("La Linea e' ferma da " + Setting.minuti_fermo_linea + " minuti. Necessario giustificare",
						Setting.data_fermo_linea);
				
				sendEmail();
				
				if (!this.isVisible()) {
					
					giustificativo.setSelectedIndex(-1);
					giustificativo2.setSelectedIndex(-1);
					giustificativo3.setSelectedIndex(-1);
					giustificativo4.setSelectedIndex(-1);
					giustificativo5.setSelectedIndex(-1);
					giustificativo6.setSelectedIndex(-1);
					giustificativo7.setSelectedIndex(-1);
					txtNote.setText("");
					txtBadge.setText("");
					txtCodiceFinale.setText("00000000000");
					
					this.setAlwaysOnTop(true);
					this.setVisible(true);
				}else {
					this.setAlwaysOnTop(true);
					this.setVisible(true);
					System.out.println("Giustificativo : finestra già visibile.");
					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.write("Errore Giustificativo. Err:" + e.toString());
		}
	}// fine run
	
	
	private void sendEmail() {
		
		int ora = LocalDateTime.now().getHour();
            
		if (((ora==21)||(ora==5)||(ora==12)) && Setting.riazzera_invio_email) {
			
			Setting.riazzera_invio_email = false;
			
			try {
				//08-03-2022----------------
				new DBCommand(true).invia_segnalazione("MANCATA DICHIARAZIONE FERMI " + Setting.LINEA,
						"Sulla " + Setting.LINEA + " risultano fermi non giustificati.",
						Setting.EMAIL_FERMI);
				log.write("Invio Email per sollecito giustificativo");
				System.out.println("Invio Email per sollecito giustificativo");
				
			}catch(Exception h) {
				log.write("Giustificativo 675: Errore invio email. e:" + h.toString());	
				System.out.println("Giustificativo 675: Errore invio email. e:" + h.toString());	
			}
		}
		if ((ora==22)||(ora==6)||(ora==13)) {
			Setting.riazzera_invio_email = true;
		}
		
	}//fine send Email
	
	
	private boolean check() {
			
				try {
					if (txtBadge.getText().equals("")) return false;
					if (giustificativo.getSelectedItem().equals("")) return false;
					if (giustificativo2.getSelectedItem().equals("")) return false;
					if (giustificativo3.getSelectedItem().equals("")) return false;
					if (giustificativo4.getSelectedItem().equals("")) return false;
					if (giustificativo5.getSelectedItem().equals("")) return false;
					if (giustificativo6.getSelectedItem().equals("")) return false;
					if (giustificativo7.getSelectedItem().equals("")) return false;
				} catch (Exception e) {
					return false;
				}
		
		return true;
	}
}// fine classdes

class ArrayListComboBoxModel extends AbstractListModel implements ComboBoxModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object selectedItem;

	private ArrayList<String> anArrayList;

	public ArrayListComboBoxModel(ArrayList<String> arrayList) {
		anArrayList = arrayList;
	}

	public Object getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(Object newValue) {
		selectedItem = newValue;
	}

	public int getSize() {
		return anArrayList.size();
	}

	public Object getElementAt(int i) {
		return anArrayList.get(i);
	}

}
