package linea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	private String codice_finale="00000000000";
	
	public static ArrayList<String> giustificativo1 = new ArrayList<>();
	public static ArrayList<String> giustificativo2 = new ArrayList<>();
	public static ArrayList<String> giustificativo3 = new ArrayList<>();
	public static ArrayList<String> giustificativo4 = new ArrayList<>();
	public static ArrayList<String> giustificativo5 = new ArrayList<>();
	private static DBCheckGriglia dbgriglia;
	
	private static ArrayListComboBoxModel model1;
	private static ArrayListComboBoxModel model2;
	private static ArrayListComboBoxModel model3;
	private static ArrayListComboBoxModel model4;
	private static ArrayListComboBoxModel model5;
	private JTextField txtInizioFermo;

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
		loadVectorfromDB();
		GUI();
		
		
	}//fine costruttore
	

	public void GUI() {
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//setVisible(true);
		setBounds(100, 100, 1283, 643);
		getContentPane().setLayout(new BorderLayout());
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
		contentPanel.setLayout(null);
		
		txtFermo = new JTextField();
		txtFermo.setHorizontalAlignment(SwingConstants.CENTER);
		txtFermo.setEditable(false);
		txtFermo.setFont(new Font("Arial", Font.BOLD, 18));
		txtFermo.setBounds(359, 95, 871, 50);
		contentPanel.add(txtFermo);
		txtFermo.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("LINEA IN STATO DI FERMO");
		lblNewLabel.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/alert4.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 23));
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(10, 11, 1220, 73);
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
		
		
		
		giustificativo.setModel(model1);
		giustificativo.setBounds(10, 194, 230, 78);
		contentPanel.add(giustificativo);
		
		JLabel lblNewLabel_1 = new JLabel("Descrizione");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1.setBounds(10, 173, 120, 14);
		contentPanel.add(lblNewLabel_1);
		
		JComboBox giustificativo2 = new JComboBox();
		giustificativo2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0,1);
					String sub2 = codice_finale.substring(3,codice_finale.length());
					String val1 = (""+giustificativo2.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 +" - sub2=" + sub2 );
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}
				
			}
		});
		giustificativo2.setModel(model2);
		giustificativo2.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo2.setBounds(250, 194, 385, 78);
		contentPanel.add(giustificativo2);
		
		JLabel lblNewLabel_1_1 = new JLabel("Macchina/Attrezzatura");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_1.setBounds(250, 173, 188, 14);
		contentPanel.add(lblNewLabel_1_1);
		
		JComboBox giustificativo3 = new JComboBox();
		giustificativo3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0,4);
					String sub2 = codice_finale.substring(6,codice_finale.length());
					String val1 = (""+giustificativo3.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 +" - sub2=" + sub2 );
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}
			}
		});
		giustificativo3.setModel(model3);
		giustificativo3.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo3.setBounds(994, 194, 236, 78);
		contentPanel.add(giustificativo3);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Tipologia");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_1_1.setBounds(994, 173, 236, 14);
		contentPanel.add(lblNewLabel_1_1_1);
		
		JComboBox giustificativo4 = new JComboBox();
		giustificativo4.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0,6);
					String sub2 = codice_finale.substring(8,codice_finale.length());
					String val1 = (""+giustificativo4.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 +" - sub2=" + sub2 );
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}
			}
		});
		giustificativo4.setModel(model4);
		giustificativo4.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo4.setBounds(10, 304, 625, 78);
		contentPanel.add(giustificativo4);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Componente");
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_1_1_1.setBounds(10, 283, 625, 14);
		contentPanel.add(lblNewLabel_1_1_1_1);
		
		JComboBox giustificativo5 = new JComboBox();
		giustificativo5.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0,8);
					String sub2 = codice_finale.substring(10,codice_finale.length());
					String val1 = (""+giustificativo5.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 +" - sub2=" + sub2 );
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}
			}
		});
		giustificativo5.setModel(model5);
		giustificativo5.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo5.setBounds(645, 304, 339, 78);
		contentPanel.add(giustificativo5);
		
		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Stato Componente");
		lblNewLabel_1_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_1_1_1_1.setBounds(645, 283, 339, 14);
		contentPanel.add(lblNewLabel_1_1_1_1_1);
		
		txtCodiceFinale = new JTextField();
		txtCodiceFinale.setForeground(Color.RED);
		txtCodiceFinale.setFont(new Font("Arial", Font.BOLD, 20));
		txtCodiceFinale.setEditable(false);
		txtCodiceFinale.setBounds(10, 411, 282, 78);
		contentPanel.add(txtCodiceFinale);
		txtCodiceFinale.setColumns(10);
		
		txtCodiceFinale.setText(codice_finale);
		JButton okButton = new JButton("INVIA MOTIVAZIONE");
		okButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		okButton.setForeground(new Color(0, 0, 0));
		okButton.setBackground(new Color(255, 182, 193));
		okButton.setBounds(302, 411, 286, 78);
		contentPanel.add(okButton);
		okButton.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/fermo.png")));
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
				
				if (!comando.invia_segnalazione(codice_finale+"5", ID))
					JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!","ATTENZIONE",JOptionPane.ERROR_MESSAGE);
				else {
					JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente","OK",JOptionPane.INFORMATION_MESSAGE);
					setVisible(false);
				}
					
					
				
			}
		});
		okButton.setActionCommand("OK");
		getRootPane().setDefaultButton(okButton);
		
		JLabel lblNewLabel_1_2 = new JLabel("Situazione Linea");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_2.setBounds(994, 284, 120, 14);
		contentPanel.add(lblNewLabel_1_2);
		
		JComboBox giustificativo6 = new JComboBox();
		giustificativo6.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					//System.out.println("cambio");
					String sub1 = codice_finale.substring(0,codice_finale.length()-1);
					String val1 = (""+giustificativo6.getSelectedItem()).split("-")[0];
					codice_finale = sub1+val1;
					txtCodiceFinale.setText(codice_finale);
				}
				
				
			}
		});
		giustificativo6.setModel(new DefaultComboBoxModel(new String[] {"", "C-CRITICO", "N-NON CRITICO"}));
		giustificativo6.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo6.setBounds(994, 304, 236, 78);
		contentPanel.add(giustificativo6);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("Codice Inviato");
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_2_1.setBounds(10, 392, 120, 14);
		contentPanel.add(lblNewLabel_1_2_1);
		
		JComboBox giustificativo7 = new JComboBox();
		giustificativo7.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String sub1 = codice_finale.substring(0,3);
					String sub2 = codice_finale.substring(4,codice_finale.length());
					String val1 = (""+giustificativo7.getSelectedItem()).split("-")[0];
					System.out.println("sub1=" + sub1 + " - val1=" + val1 +" - sub2=" + sub2 );
					codice_finale = sub1 + val1 + sub2;
					txtCodiceFinale.setText(codice_finale);
				}
				
			}
		});
		giustificativo7.setModel(new DefaultComboBoxModel(new String[] {"", "1-MACCHINA FERMA", "2-MACCHINA LAVORA", "3-MACCHINA CON INCEPPAMENTI"}));
		giustificativo7.setFont(new Font("Arial", Font.BOLD, 20));
		giustificativo7.setBounds(645, 194, 339, 78);
		contentPanel.add(giustificativo7);
		
		JLabel lblNewLabel_1_1_1_2 = new JLabel("Stato");
		lblNewLabel_1_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_1_1_2.setBounds(645, 173, 160, 14);
		contentPanel.add(lblNewLabel_1_1_1_2);
		
		txtInizioFermo = new JTextField();
		txtInizioFermo.setHorizontalAlignment(SwingConstants.CENTER);
		txtInizioFermo.setFont(new Font("Arial", Font.BOLD, 18));
		txtInizioFermo.setEditable(false);
		txtInizioFermo.setColumns(10);
		txtInizioFermo.setBounds(10, 95, 339, 50);
		contentPanel.add(txtInizioFermo);
		
		JLabel lblNewLabel_1_3 = new JLabel("Inizio Fermo");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1_3.setBounds(10, 77, 120, 14);
		contentPanel.add(lblNewLabel_1_3);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(SystemColor.inactiveCaptionBorder);
			buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				
				JButton btnCambioLinea = new JButton("AVVIO LINEA");
				btnCambioLinea.setFont(new Font("Tahoma", Font.BOLD, 11));
				btnCambioLinea.setBackground(SystemColor.inactiveCaption);
				btnCambioLinea.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (!comando.invia_segnalazione("E000000"+Setting.DB_BATTERIE_NUM_LINEA, ID))
							JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!","ATTENZIONE",JOptionPane.ERROR_MESSAGE);
						else {
							JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente","OK",JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
					}
				});
				
				JButton btnPausaPranzo = new JButton("PAUSA PRANZO");
				btnPausaPranzo.setFont(new Font("Tahoma", Font.BOLD, 11));
				btnPausaPranzo.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (!comando.invia_segnalazione("F000000"+Setting.DB_BATTERIE_NUM_LINEA, ID))
							JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!","ATTENZIONE",JOptionPane.ERROR_MESSAGE);
						else {
							JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente","OK",JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
					}
				});
				btnPausaPranzo.setBackground(SystemColor.inactiveCaption);
				btnPausaPranzo.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/cibo.png")));
				btnPausaPranzo.setActionCommand("OK");
				buttonPane.add(btnPausaPranzo);
				
				JButton btnPausaCaffe = new JButton("PAUSA CAFFE");
				btnPausaCaffe.setFont(new Font("Tahoma", Font.BOLD, 11));
				btnPausaCaffe.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (!comando.invia_segnalazione("G000000"+Setting.DB_BATTERIE_NUM_LINEA, ID))
							JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!","ATTENZIONE",JOptionPane.ERROR_MESSAGE);
						else {
							JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente","OK",JOptionPane.INFORMATION_MESSAGE);
							setVisible(false);
						}
					}
				});
				btnPausaCaffe.setBackground(SystemColor.inactiveCaption);
				btnPausaCaffe.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/coffee5.png")));
				btnPausaCaffe.setActionCommand("OK");
				buttonPane.add(btnPausaCaffe);
				btnCambioLinea.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/cambiolinea.png")));
				btnCambioLinea.setActionCommand("OK");
				buttonPane.add(btnCambioLinea);
			}
			
			JButton btnFermoProgrammato = new JButton("FERMO PROGRAMMATO");
			btnFermoProgrammato.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (!comando.invia_segnalazione("H000000"+Setting.DB_BATTERIE_NUM_LINEA, ID))
						JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!","ATTENZIONE",JOptionPane.ERROR_MESSAGE);
					else {
						JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente","OK",JOptionPane.INFORMATION_MESSAGE);
						setVisible(false);
					}
				}
			});
			
			JButton btnCambioProduzione = new JButton("CAMBIO PRODUZIONE");
			btnCambioProduzione.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/cambio_linea.png")));
			btnCambioProduzione.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnCambioProduzione.setBackground(SystemColor.inactiveCaption);
			btnCambioProduzione.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (!comando.invia_segnalazione("L000000"+Setting.DB_BATTERIE_NUM_LINEA, ID))
						JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!","ATTENZIONE",JOptionPane.ERROR_MESSAGE);
					else {
						JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente","OK",JOptionPane.INFORMATION_MESSAGE);
						setVisible(false);
					}
				}
			});
			btnCambioProduzione.setActionCommand("OK");
			buttonPane.add(btnCambioProduzione);
			btnFermoProgrammato.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnFermoProgrammato.setBackground(SystemColor.inactiveCaption);
			btnFermoProgrammato.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/fermoprogrammato.png")));
			btnFermoProgrammato.setActionCommand("OK");
			buttonPane.add(btnFermoProgrammato);
			
			JButton btnAssemblea = new JButton("ASSEMBLEA");
			btnAssemblea.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (!comando.invia_segnalazione("I000000"+Setting.DB_BATTERIE_NUM_LINEA, ID))
						JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!","ATTENZIONE",JOptionPane.ERROR_MESSAGE);
					else {
						JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente","OK",JOptionPane.INFORMATION_MESSAGE);
						setVisible(false);
					}
					
				}
			});
			btnAssemblea.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/assemblea.png")));
			btnAssemblea.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnAssemblea.setBackground(SystemColor.inactiveCaption);
			btnAssemblea.setActionCommand("OK");
			buttonPane.add(btnAssemblea);
			
			JButton btnSciopero = new JButton("SCIOPERO");
			btnSciopero.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (!comando.invia_segnalazione("L000000"+Setting.DB_BATTERIE_NUM_LINEA, ID))
						JOptionPane.showMessageDialog(getContentPane(), "Errore esecuzione comando !!","ATTENZIONE",JOptionPane.ERROR_MESSAGE);
					else {
						JOptionPane.showMessageDialog(getContentPane(), "Motivazione inserita correttamente","OK",JOptionPane.INFORMATION_MESSAGE);
						setVisible(false);
					}
				}
			});
			btnSciopero.setIcon(new ImageIcon(Giustificativo.class.getResource("/resource/sciopero.png")));
			btnSciopero.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnSciopero.setBackground(SystemColor.inactiveCaption);
			btnSciopero.setActionCommand("OK");
			buttonPane.add(btnSciopero);
		}
		
		
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		
	}//fine gui
	
	
public void setFermo(String testo,String inizio) {
		
		txtFermo.setText(testo);
		txtInizioFermo.setText(inizio);
		
	}


public void loadVectorfromDB() {
	
	//AVVIO IL LETTORE check control
			dbgriglia = new DBCheckGriglia();
			
			ArrayList<String>[] lista = dbgriglia.getGiustificativi();
			
			giustificativo1 = lista[0];
			giustificativo2 = lista[1];
			giustificativo3 = lista[2];
			giustificativo4 = lista[3];
			giustificativo5 = lista[4];
			
			model1 = new ArrayListComboBoxModel(giustificativo1);
			model2 = new ArrayListComboBoxModel(giustificativo2);
			model3 = new ArrayListComboBoxModel(giustificativo3);
			model4 = new ArrayListComboBoxModel(giustificativo4);
			model5 = new ArrayListComboBoxModel(giustificativo5);
			
	
}//fine load

@Override
public void run() {
	// TODO Auto-generated method stub
	try {
		System.out.println("GIUSTIFICATIVO RUNNING!");
		//verifico i dati di stop
		ID = giustificativo_db.getFermata();
		System.out.println("GIUSTIFICATIVO ID=" + ID);
		if ((Setting.minuti_fermo_linea >=Setting.tempoMaxLineaFerma)&&(ID>0)) {
			
			setFermo("La Linea e' ferma da " + Setting.minuti_fermo_linea + " minuti. Necessario giustificare", Setting.data_fermo_linea);
			this.setVisible(true);
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		log.write("Errore Giustificativo. Err:" + e.toString() );
	}
}//fine run
}//fine classdes



class ArrayListComboBoxModel extends AbstractListModel implements ComboBoxModel {
	  private Object selectedItem;

	  private ArrayList anArrayList;

	  public ArrayListComboBoxModel(ArrayList arrayList) {
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
