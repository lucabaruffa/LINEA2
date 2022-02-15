package linea;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPasswordField;
import java.awt.Font;

public class SettingFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField db;
	private JTextField slot;
	private JTextField rack;
	private JTextField ipplc;
	private JTextField ipdb;

	Setting s;
	private JTextField username_db;
	private JPasswordField password_db;
	private JTextField db2;
	private JTextField db3;
	private JTextField db4;
	private JTextField db5;
	private JTextField db6;
	private JTextField db7;
	private JTextField db8;
	private JTextField db9;
	private JTextField db10;
	private JTextField numero_stazioni;

	/**
	 * Create the frame.
	 */
	public SettingFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("IMPOSTAZIONI");

		try {
			s = new Setting();
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(contentPane, "ERRORE NEL CARICAMENTO DELLA CONFIGURAZIONE!", "ERRORE",
					JOptionPane.ERROR_MESSAGE);
			e2.printStackTrace();
		}

		setBounds(100, 100, 590, 360);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JComboBox<String> modalita = new JComboBox<String>();
		modalita.setModel(new DefaultComboBoxModel(new String[] { "OP", "PG", "S7_BASIC" }));
		modalita.setBounds(425, 23, 131, 22);
		contentPane.add(modalita);

		db = new JTextField();
		db.setText("8");
		db.setColumns(10);
		db.setBounds(10, 129, 38, 20);
		contentPane.add(db);

		JLabel lblDbNumber = new JLabel("DB 1");
		lblDbNumber.setBounds(10, 116, 47, 14);
		contentPane.add(lblDbNumber);

		slot = new JTextField();
		slot.setText("1");
		slot.setColumns(10);
		slot.setBounds(247, 24, 47, 20);
		contentPane.add(slot);

		JLabel lblSlot = new JLabel("SLOT");
		lblSlot.setBounds(247, 11, 53, 14);
		contentPane.add(lblSlot);

		rack = new JTextField();
		rack.setText("0");
		rack.setColumns(10);
		rack.setBounds(174, 24, 47, 20);
		contentPane.add(rack);

		JLabel lblRack = new JLabel("RACK");
		lblRack.setBounds(174, 11, 53, 14);
		contentPane.add(lblRack);

		ipplc = new JTextField();
		ipplc.setText("100.100.5.1");
		ipplc.setColumns(10);
		ipplc.setBounds(10, 24, 132, 20);
		contentPane.add(ipplc);

		JLabel lblNewLabel = new JLabel("IP PLC");
		lblNewLabel.setBounds(11, 11, 46, 14);
		contentPane.add(lblNewLabel);

		JLabel lblIpDb = new JLabel("IP DB");
		lblIpDb.setBounds(11, 175, 46, 14);
		contentPane.add(lblIpDb);

		ipdb = new JTextField();
		ipdb.setText("10.25.231.198");
		ipdb.setColumns(10);
		ipdb.setBounds(10, 188, 132, 20);
		contentPane.add(ipdb);

		JButton btnNewButton = new JButton("SALVA CONFIGURAZIONE");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				s.setDB(db.getText());
				s.setDB2(db2.getText());
				s.setDB3(db3.getText());
				s.setDB4(db4.getText());
				s.setDB5(db5.getText());
				s.setDB6(db6.getText());
				s.setDB7(db7.getText());
				s.setDB8(db8.getText());
				s.setDB9(db9.getText());
				s.setDB10(db10.getText());
				s.setIPPLC(ipplc.getText());
				s.setRACK(rack.getText());
				s.setSLOT(slot.getText());
				s.setIPDB(ipdb.getText());
				s.setPASSWORD_DB(password_db.getText());
				s.setUSERNAME_DB(username_db.getText());
				s.setConnectionType("" + modalita.getSelectedItem());
				s.setNumeroStazioniAttive(numero_stazioni.getText());

				try {
					s.salvaConfigurazione();
					JOptionPane.showMessageDialog(contentPane, "Configurazione salvata!", "AVVISO",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(contentPane, "ERRORE NEL SALVATAGGIO DELLA CONFIGURAZIONE!", "ERRORE",
							JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}

			}
		});
		btnNewButton.setBounds(341, 281, 215, 23);
		contentPane.add(btnNewButton);

		JLabel lblTipoConnessione = new JLabel("TIPO CONNESSIONE");
		lblTipoConnessione.setBounds(424, 11, 132, 14);
		contentPane.add(lblTipoConnessione);

		username_db = new JTextField();
		username_db.setText("luca");
		username_db.setColumns(10);
		username_db.setBounds(174, 188, 132, 20);
		contentPane.add(username_db);

		JLabel lblUsernameDb = new JLabel("USERNAME DB");
		lblUsernameDb.setBounds(175, 175, 131, 14);
		contentPane.add(lblUsernameDb);

		JLabel lblPasswordDb = new JLabel("PASSWORD DB");
		lblPasswordDb.setBounds(326, 175, 131, 14);
		contentPane.add(lblPasswordDb);

		password_db = new JPasswordField();
		password_db.setToolTipText("Password");
		password_db.setBounds(325, 188, 132, 20);
		password_db.setText("matrox02");

		contentPane.add(password_db);

		db2 = new JTextField();
		db2.setText("8");
		db2.setColumns(10);
		db2.setBounds(58, 129, 38, 20);
		contentPane.add(db2);

		JLabel lblDb = new JLabel("DB 2");
		lblDb.setBounds(58, 116, 38, 14);
		contentPane.add(lblDb);

		db3 = new JTextField();
		db3.setText("8");
		db3.setColumns(10);
		db3.setBounds(106, 129, 38, 20);
		contentPane.add(db3);

		JLabel lblDb_2 = new JLabel("DB 3");
		lblDb_2.setBounds(106, 116, 38, 14);
		contentPane.add(lblDb_2);

		db4 = new JTextField();
		db4.setText("8");
		db4.setColumns(10);
		db4.setBounds(154, 129, 38, 20);
		contentPane.add(db4);

		JLabel lblDb_2_1 = new JLabel("DB 4");
		lblDb_2_1.setBounds(154, 116, 38, 14);
		contentPane.add(lblDb_2_1);

		db5 = new JTextField();
		db5.setText("8");
		db5.setColumns(10);
		db5.setBounds(202, 129, 40, 20);
		contentPane.add(db5);

		JLabel lblDb_2_2 = new JLabel("DB 5");
		lblDb_2_2.setBounds(202, 116, 40, 14);
		contentPane.add(lblDb_2_2);

		db6 = new JTextField();
		db6.setText("8");
		db6.setColumns(10);
		db6.setBounds(257, 129, 40, 20);
		contentPane.add(db6);

		JLabel lblDb_2_2_1 = new JLabel("DB 6");
		lblDb_2_2_1.setBounds(257, 116, 40, 14);
		contentPane.add(lblDb_2_2_1);

		db7 = new JTextField();
		db7.setText("8");
		db7.setColumns(10);
		db7.setBounds(306, 129, 40, 20);
		contentPane.add(db7);

		JLabel lblDb_2_2_2 = new JLabel("DB 7");
		lblDb_2_2_2.setBounds(306, 116, 40, 14);
		contentPane.add(lblDb_2_2_2);

		db8 = new JTextField();
		db8.setText("8");
		db8.setColumns(10);
		db8.setBounds(357, 129, 40, 20);
		contentPane.add(db8);

		JLabel lblDb_2_2_3 = new JLabel("DB 8");
		lblDb_2_2_3.setBounds(357, 116, 40, 14);
		contentPane.add(lblDb_2_2_3);

		db9 = new JTextField();
		db9.setText("8");
		db9.setColumns(10);
		db9.setBounds(417, 129, 40, 20);
		contentPane.add(db9);

		JLabel lblDb_2_2_4 = new JLabel("DB 9");
		lblDb_2_2_4.setBounds(417, 116, 40, 14);
		contentPane.add(lblDb_2_2_4);

		db10 = new JTextField();
		db10.setText("8");
		db10.setColumns(10);
		db10.setBounds(477, 129, 40, 20);
		contentPane.add(db10);

		JLabel lblDb_2_2_5 = new JLabel("DB 10");
		lblDb_2_2_5.setBounds(477, 116, 40, 14);
		contentPane.add(lblDb_2_2_5);

		JLabel lblNewLabel_1 = new JLabel("DATA BLOCK POSTAZIONI 1..10");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setForeground(new Color(220, 20, 60));
		lblNewLabel_1.setBounds(10, 97, 232, 14);
		contentPane.add(lblNewLabel_1);

		numero_stazioni = new JTextField();
		numero_stazioni.setText("5");
		numero_stazioni.setColumns(10);
		numero_stazioni.setBounds(10, 68, 69, 20);
		contentPane.add(numero_stazioni);

		JLabel lblNumeroStazioniAttive = new JLabel("NUMERO STAZIONI ATTIVE");
		lblNumeroStazioniAttive.setBounds(11, 55, 164, 14);
		contentPane.add(lblNumeroStazioniAttive);

		ipplc.setText(s.getIPPLC());
		rack.setText(s.getRACK());
		slot.setText(s.getSLOT());
		db.setText(s.getDB());
		db2.setText(s.getDB2());
		db3.setText(s.getDB3());
		db4.setText(s.getDB4());
		db5.setText(s.getDB5());
		db6.setText(s.getDB6());
		db7.setText(s.getDB7());
		db8.setText(s.getDB8());
		db9.setText(s.getDB9());
		db10.setText(s.getDB10());
		modalita.setSelectedItem(s.getConnectionType());

		username_db.setText(s.getUSERNAMEDB());
		password_db.setText(s.getPASSWORDDB());
		ipdb.setText(s.getIPDB());
		numero_stazioni.setText(s.getNumeroStazioniAttive());

	}// fine init
}// fine class
