package linea;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DB.DBConnectionPool;

import java.awt.Toolkit;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import javax.swing.SwingConstants;
import java.awt.Font;

public class Segnalazione extends JFrame {

	private JPanel contentPane;
	private JTextField user;
	private JTextArea testo;
	private DBConnectionPool pool;
	private static LoggerFile log = new LoggerFile();
	public Connection c_mysql;
	public Statement stmt_mysql;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public Segnalazione() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Segnalazione.class.getResource("/resource/icon.png")));
		setTitle("SEGNALAZIONE");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 747, 468);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel OPERATORE = new JLabel("OPERATORE");
		panel.add(OPERATORE);

		user = new JTextField();
		user.setHorizontalAlignment(SwingConstants.LEFT);
		user.setText("OPERATORE " + Setting.LINEA);
		panel.add(user);
		user.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		testo = new JTextArea();
		testo.setFont(new Font("Monospaced", Font.BOLD, 18));
		testo.setForeground(new Color(178, 34, 34));
		scrollPane.setViewportView(testo);
		testo.requestFocus();

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		contentPane.add(panel_1, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("INVIA SEGNALAZIONE");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (invia()) {
					JOptionPane.showMessageDialog(contentPane, "Segnalazione inviata!", "INVIO",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();

				} else
					JOptionPane.showMessageDialog(contentPane,
							"Si è verificato un problema nell'invio della segnalazione!", "ERRORe",
							JOptionPane.ERROR_MESSAGE);
			}
		});
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setIcon(new ImageIcon(Segnalazione.class.getResource("/resource/SEND.png")));
		panel_1.add(btnNewButton);
	}// fine init

	public boolean invia() {
		pool = new DBConnectionPool();

		try {
			c_mysql = pool.getConnection();
		} catch (Exception e) {
			log.write("Errore getConnection: " + e.toString() + "   Modulo:Segnalazione");
		}

		try {
			stmt_mysql = c_mysql.createStatement();

		} catch (Exception e) {

			log.write("Errore statement :" + e.getMessage() + "   Modulo:Segnalazione");
		} // FINE CATCH

		try {
			stmt_mysql.executeUpdate("INSERT INTO segnalazioni (operatore,testo,linea) VALUES " + " ('" + user.getText()
					+ "','" + testo.getText() + "', '" + Setting.LINEA_GIUSTIFICATIVI + "')");

		} catch (Exception h) {
			log.write("ERRORE INSERIMENTO db SEGNALAZIONE.   Errore:" + h.toString());
			return false;
		} finally {
			pool.returnConnection(c_mysql);
		}

		return true;
	}// fine invia

}// fine metodo
