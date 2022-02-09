package linea2;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import java.awt.SystemColor;

public class Log extends JFrame {

	private JPanel contentPane;
	private JTextArea monitor;

	private static LoggerFile log = new LoggerFile();
	private JPanel panel;
	private JButton btnNewButton;
	/**
	 * Launch the application.
	 */
	
	/**
	 * Create the frame.
	 */
	public Log() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				
				log.read(monitor);
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage(Log.class.getResource("/resource/icon.png")));
		setTitle("LOG");
		setBounds(100, 100, 1051, 571);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		monitor = new JTextArea();
		scrollPane.setViewportView(monitor);
		
		log.read(monitor);
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setBackground(SystemColor.inactiveCaptionBorder);
		
		btnNewButton = new JButton("Aggiorna");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setIcon(new ImageIcon(Log.class.getResource("/resource/reload.png")));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				log.read(monitor);
			}
		});
		panel.add(btnNewButton);
	}
	
	
	
	public JTextArea getMonitor() {
		return monitor;
	}
	

}
