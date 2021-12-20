package View;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import linea2.LoggerFile;

import java.awt.Toolkit;
import java.awt.Window.Type;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;

public class Scarti extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JPanel contentPane;
	public static JTextArea monitor;
	private static LoggerFile log = new LoggerFile();
	
	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public Scarti(String init) {
		
		inizializza();
		this.setVisible(true);
		
		//else
			//this.setVisible(true);
	}
	
	public Scarti() {
		
		inizializza();
	}
	
	public void inizializza() {
		//setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Scarti.class.getResource("/resource/icon.png")));
		setTitle("Viewer");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(700, 5, 410, 786);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		monitor = new JTextArea();
		scrollPane.setViewportView(monitor);
		
		log.readBatterieBloccate(monitor);
		
		setAlwaysOnTop(true);
		getContentPane().setBackground(Color.WHITE);
		//if (frame_win == null) 
		//	frame_win = new Scarti();
		
		this.setVisible(false);
	}//fine inizializza
	
	
	public void setMessage(String s) {
		monitor.append(s+"\n");
		log.writeBatteriaBloccata(s);
	}

}//fine classe
