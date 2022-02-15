package linea;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DB.DBCheckGriglia;
import PLC.plcCommand;

import java.awt.Color;
import javax.swing.JComboBox;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class WinSetGreencode extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtpiastra;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private static DBCheckGriglia dbgriglia;
	private static LoggerFile log = new LoggerFile();
	private JTextField txtNomeBatteria;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					WinSetGreencode frame = new WinSetGreencode();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WinSetGreencode() {

		setVisible(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(WinSetGreencode.class.getResource("/resource/icon.png")));

		plcCommand dbcommand = new plcCommand();

		// AVVIO IL LETTORE check control
		dbgriglia = new DBCheckGriglia();
		// il risultato viene inserito in Setting.listaGreenCode
		dbgriglia.getElencoGreenCode();

		setResizable(false);
		setTitle("CAMBIO CODICE");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 858, 374);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaptionBorder);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JComboBox piastradaprodurre = new JComboBox();
		piastradaprodurre.setModel(new DefaultComboBoxModel(new String[] { "" }));
		piastradaprodurre.setFont(new Font("Arial", Font.BOLD, 30));
		piastradaprodurre.setBounds(49, 130, 755, 82);
		contentPane.add(piastradaprodurre);

		// String[] array = Setting.listaGreenCode.toArray(new String[0]);
		// greenCode[] array = Setting.listaGreenCode.toArray(new String[0]);

		/*
		 * for (String n : array) { piastradaprodurre.addItem(n); }
		 */

		for (int i = 0; i < Setting.listaGreenCode.size(); i++) {
			// System.out.println(Setting.listaGreenCode.get(i));
			piastradaprodurre.addItem(
					Setting.listaGreenCode.get(i).getGreencode() + " -- " + Setting.listaGreenCode.get(i).getNome());
		}

		JButton btnAvvio = new JButton("INVIA AL MARCATORE");
		btnAvvio.setIcon(new ImageIcon(WinSetGreencode.class.getResource("/resource/101.png")));
		btnAvvio.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				if (!dbcommand.writeGreenCode(Setting.listaGreenCode.get(piastradaprodurre.getSelectedIndex() - 1)))

				{
					JOptionPane.showMessageDialog(contentPane, "Errore esecuzione comando !!", "ATTENZIONE",
							JOptionPane.ERROR_MESSAGE);
					txtpiastra.setText("errore");
					txtNomeBatteria.setText("errore");
					// String cod =
					// (""+(piastradaprodurre.getItemAt(piastradaprodurre.getSelectedIndex()))).split("
					// -- ")[0];
					// String nom =
					// (""+(piastradaprodurre.getItemAt(piastradaprodurre.getSelectedIndex()))).split("
					// -- ")[1];
					Setting.txtTipologiaBatteria.setText("Errore invio codice marcatore");
					log.write("\nGREENCODE ERRORE CAMBIO CODICE");
				} else {
					JOptionPane.showMessageDialog(contentPane, "Codice inviato correttamente", "OK",
							JOptionPane.INFORMATION_MESSAGE);
					String cod = ("" + (piastradaprodurre.getItemAt(piastradaprodurre.getSelectedIndex())))
							.split(" -- ")[0];
					String nom = ("" + (piastradaprodurre.getItemAt(piastradaprodurre.getSelectedIndex())))
							.split(" -- ")[1];
					txtpiastra.setText(cod);
					txtNomeBatteria.setText(nom);
					Setting.txtTipologiaBatteria.setText(cod + " / " + nom);
					log.write("\nGREENCODE MODIFICATO CORRETTAMENTE: " + cod + " / " + nom);
				} // fine else
			}
		});
		btnAvvio.setBackground(new Color(0, 250, 154));
		btnAvvio.setFont(new Font("Arial", Font.BOLD, 18));
		btnAvvio.setBounds(49, 223, 587, 59);
		contentPane.add(btnAvvio);

		JButton btnAnnulla = new JButton("CHIUDI");
		btnAnnulla.setIcon(new ImageIcon(WinSetGreencode.class.getResource("/resource/icona_annulla.png")));
		btnAnnulla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				dispose();
			}
		});
		btnAnnulla.setFont(new Font("Arial", Font.BOLD, 18));
		btnAnnulla.setBackground(new Color(255, 182, 193));
		btnAnnulla.setBounds(671, 223, 133, 59);
		contentPane.add(btnAnnulla);

		txtNomeBatteria = new JTextField();
		txtNomeBatteria.setText((String) null);
		txtNomeBatteria.setForeground(Color.RED);
		txtNomeBatteria.setFont(new Font("Arial", Font.BOLD, 20));
		txtNomeBatteria.setEditable(false);
		txtNomeBatteria.setColumns(10);
		txtNomeBatteria.setBounds(292, 51, 512, 46);
		contentPane.add(txtNomeBatteria);

		txtpiastra = new JTextField();
		txtpiastra.setForeground(new Color(255, 0, 0));
		txtpiastra.setEditable(false);
		txtpiastra.setFont(new Font("Arial", Font.BOLD, 20));
		txtpiastra.setBounds(49, 51, 233, 46);
		contentPane.add(txtpiastra);
		txtpiastra.setColumns(10);

		try {
			greenCode green = dbcommand.leggiGreenCode();
			txtpiastra.setText(green.getGreencode());
			txtNomeBatteria.setText(green.getNome());
		} catch (Exception j) {
			log.write("WinSetGrennCode. Errore impostazioni greencode letto");
			System.out.println("WinSetGrennCode. Errore impostazioni greencode letto");
		}

		JLabel lblNewLabel = new JLabel("CODICE IMPOSTATO");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel.setBounds(49, 21, 233, 29);
		contentPane.add(lblNewLabel);

		JLabel lblPiastraDaProdurre = new JLabel("CODICE DA IMPOSTARE");
		lblPiastraDaProdurre.setForeground(Color.BLACK);
		lblPiastraDaProdurre.setFont(new Font("Arial", Font.BOLD, 12));
		lblPiastraDaProdurre.setBounds(49, 103, 288, 23);
		contentPane.add(lblPiastraDaProdurre);

		JLabel lblNomeCodiceImpostato = new JLabel("NOME IMPOSTATO");
		lblNomeCodiceImpostato.setForeground(Color.BLACK);
		lblNomeCodiceImpostato.setFont(new Font("Arial", Font.BOLD, 12));
		lblNomeCodiceImpostato.setBounds(292, 21, 233, 29);
		contentPane.add(lblNomeCodiceImpostato);

	}
}// fine classe
