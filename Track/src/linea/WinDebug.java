package linea;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;

public class WinDebug extends JFrame implements Runnable {

	private JPanel contentPane;
	private JTextField txtZero1;
	private JTextField textProcessata1;
	private Setting setting;
	private JTextField txtZero2;
	private JTextField textProcessata2;
	private JTextField txtZero3;
	private JTextField textProcessata3;
	private JTextField txtZero4;
	private JTextField textProcessata4;
	private JTextField txtZero5;
	private JTextField textProcessata5;
	private Thread t;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinDebug frame = new WinDebug();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WinDebug() {
		init();
		t = new Thread(this);
		t.start();
		
	}
	
	public void init() {
		
		setTitle("Debug");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1404, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtZero1 = new JTextField();
		txtZero1.setEditable(false);
		txtZero1.setFont(new Font("Arial", Font.BOLD, 18));
		txtZero1.setForeground(Color.RED);
		txtZero1.setBounds(10, 60, 261, 65);
		contentPane.add(txtZero1);
		txtZero1.setColumns(10);
		
		textProcessata1 = new JTextField();
		textProcessata1.setEditable(false);
		textProcessata1.setForeground(new Color(34, 139, 34));
		textProcessata1.setFont(new Font("Arial", Font.BOLD, 18));
		textProcessata1.setColumns(10);
		textProcessata1.setBounds(10, 178, 261, 65);
		contentPane.add(textProcessata1);
		
		JLabel lblNewLabel = new JLabel("PRIMO C.C.");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 261, 23);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("INGRESSO LETTORE");
		lblNewLabel_1.setBounds(10, 45, 141, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("PROCESSATA");
		lblNewLabel_1_1.setBounds(10, 162, 141, 14);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblPuntatrice = new JLabel("PUNTATRICE 1");
		lblPuntatrice.setHorizontalAlignment(SwingConstants.CENTER);
		lblPuntatrice.setFont(new Font("Arial", Font.BOLD, 14));
		lblPuntatrice.setBounds(281, 11, 261, 23);
		contentPane.add(lblPuntatrice);
		
		JLabel lblNewLabel_1_2 = new JLabel("INGRESSO LETTORE");
		lblNewLabel_1_2.setBounds(281, 45, 141, 14);
		contentPane.add(lblNewLabel_1_2);
		
		txtZero2 = new JTextField();
		txtZero2.setForeground(Color.RED);
		txtZero2.setFont(new Font("Arial", Font.BOLD, 18));
		txtZero2.setEditable(false);
		txtZero2.setColumns(10);
		txtZero2.setBounds(281, 60, 261, 65);
		contentPane.add(txtZero2);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("PROCESSATA");
		lblNewLabel_1_1_1.setBounds(281, 162, 141, 14);
		contentPane.add(lblNewLabel_1_1_1);
		
		textProcessata2 = new JTextField();
		textProcessata2.setForeground(new Color(34, 139, 34));
		textProcessata2.setFont(new Font("Arial", Font.BOLD, 18));
		textProcessata2.setEditable(false);
		textProcessata2.setColumns(10);
		textProcessata2.setBounds(281, 178, 261, 65);
		contentPane.add(textProcessata2);
		
		JLabel lblPuntatrice_2 = new JLabel("PUNTATRICE 2");
		lblPuntatrice_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblPuntatrice_2.setFont(new Font("Arial", Font.BOLD, 14));
		lblPuntatrice_2.setBounds(552, 11, 261, 23);
		contentPane.add(lblPuntatrice_2);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("INGRESSO LETTORE");
		lblNewLabel_1_2_1.setBounds(552, 45, 141, 14);
		contentPane.add(lblNewLabel_1_2_1);
		
		txtZero3 = new JTextField();
		txtZero3.setForeground(Color.RED);
		txtZero3.setFont(new Font("Arial", Font.BOLD, 18));
		txtZero3.setEditable(false);
		txtZero3.setColumns(10);
		txtZero3.setBounds(552, 60, 261, 65);
		contentPane.add(txtZero3);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("PROCESSATA");
		lblNewLabel_1_1_1_1.setBounds(552, 162, 141, 14);
		contentPane.add(lblNewLabel_1_1_1_1);
		
		textProcessata3 = new JTextField();
		textProcessata3.setForeground(new Color(34, 139, 34));
		textProcessata3.setFont(new Font("Arial", Font.BOLD, 18));
		textProcessata3.setEditable(false);
		textProcessata3.setColumns(10);
		textProcessata3.setBounds(552, 178, 261, 65);
		contentPane.add(textProcessata3);
		
		JLabel lblSecondoCc = new JLabel("SECONDO C.C.");
		lblSecondoCc.setHorizontalAlignment(SwingConstants.CENTER);
		lblSecondoCc.setFont(new Font("Arial", Font.BOLD, 14));
		lblSecondoCc.setBounds(823, 11, 261, 23);
		contentPane.add(lblSecondoCc);
		
		JLabel lblNewLabel_1_3 = new JLabel("INGRESSO LETTORE");
		lblNewLabel_1_3.setBounds(823, 45, 141, 14);
		contentPane.add(lblNewLabel_1_3);
		
		txtZero4 = new JTextField();
		txtZero4.setForeground(Color.RED);
		txtZero4.setFont(new Font("Arial", Font.BOLD, 18));
		txtZero4.setEditable(false);
		txtZero4.setColumns(10);
		txtZero4.setBounds(823, 60, 261, 65);
		contentPane.add(txtZero4);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("PROCESSATA");
		lblNewLabel_1_1_2.setBounds(823, 162, 141, 14);
		contentPane.add(lblNewLabel_1_1_2);
		
		textProcessata4 = new JTextField();
		textProcessata4.setForeground(new Color(34, 139, 34));
		textProcessata4.setFont(new Font("Arial", Font.BOLD, 18));
		textProcessata4.setEditable(false);
		textProcessata4.setColumns(10);
		textProcessata4.setBounds(823, 178, 261, 65);
		contentPane.add(textProcessata4);
		
		JLabel lblTenuta = new JLabel("TENUTA 1");
		lblTenuta.setHorizontalAlignment(SwingConstants.CENTER);
		lblTenuta.setFont(new Font("Arial", Font.BOLD, 14));
		lblTenuta.setBounds(1101, 11, 261, 23);
		contentPane.add(lblTenuta);
		
		JLabel lblNewLabel_1_3_1 = new JLabel("INGRESSO LETTORE");
		lblNewLabel_1_3_1.setBounds(1101, 45, 141, 14);
		contentPane.add(lblNewLabel_1_3_1);
		
		txtZero5 = new JTextField();
		txtZero5.setForeground(Color.RED);
		txtZero5.setFont(new Font("Arial", Font.BOLD, 18));
		txtZero5.setEditable(false);
		txtZero5.setColumns(10);
		txtZero5.setBounds(1101, 60, 261, 65);
		contentPane.add(txtZero5);
		
		JLabel lblNewLabel_1_1_2_1 = new JLabel("PROCESSATA");
		lblNewLabel_1_1_2_1.setBounds(1101, 162, 141, 14);
		contentPane.add(lblNewLabel_1_1_2_1);
		
		textProcessata5 = new JTextField();
		textProcessata5.setForeground(new Color(34, 139, 34));
		textProcessata5.setFont(new Font("Arial", Font.BOLD, 18));
		textProcessata5.setEditable(false);
		textProcessata5.setColumns(10);
		textProcessata5.setBounds(1101, 178, 261, 65);
		contentPane.add(textProcessata5);
	}//fine init

	@Override
	public void run() {
		
		//init();
		
		try {
			setting = new Setting(); //non reinizializzo setting
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		while(true) {
			
			
			//txtZero1.setText(setting.elenco_indicatori.getIndicatore(0).getBatteriaZero());
			
			setting.elenco_indicatori.getIndicatore(0).setBatteriaZeroDebug(setting.elenco_indicatori.getIndicatore(0).getBatteriaZero(), txtZero1);			
			setting.elenco_indicatori.getIndicatore(0).setBatteriaZeroDebug(setting.elenco_indicatori.getIndicatore(0).getBatteria(), textProcessata1);
			//textProcessata1.setText(setting.elenco_indicatori.getIndicatore(0).getBatteria());
			
			///txtZero2.setText(setting.elenco_indicatori.getIndicatore(1).getBatteriaZero());
			//textProcessata2.setText(setting.elenco_indicatori.getIndicatore(1).getBatteria());
			
			setting.elenco_indicatori.getIndicatore(1).setBatteriaZeroDebug(setting.elenco_indicatori.getIndicatore(1).getBatteriaZero(), txtZero2);			
			setting.elenco_indicatori.getIndicatore(1).setBatteriaZeroDebug(setting.elenco_indicatori.getIndicatore(1).getBatteria(), textProcessata2);
			
			setting.elenco_indicatori.getIndicatore(2).setBatteriaZeroDebug(setting.elenco_indicatori.getIndicatore(2).getBatteriaZero(), txtZero3);			
			setting.elenco_indicatori.getIndicatore(2).setBatteriaZeroDebug(setting.elenco_indicatori.getIndicatore(2).getBatteria(), textProcessata3);
			
			setting.elenco_indicatori.getIndicatore(3).setBatteriaZeroDebug(setting.elenco_indicatori.getIndicatore(3).getBatteriaZero(), txtZero4);			
			setting.elenco_indicatori.getIndicatore(3).setBatteriaZeroDebug(setting.elenco_indicatori.getIndicatore(3).getBatteria(), textProcessata4);
			
			setting.elenco_indicatori.getIndicatore(4).setBatteriaZeroDebug(setting.elenco_indicatori.getIndicatore(4).getBatteriaZero(), txtZero5);			
			setting.elenco_indicatori.getIndicatore(4).setBatteriaZeroDebug(setting.elenco_indicatori.getIndicatore(4).getBatteria(), textProcessata5);
			
			//setting.elenco_indicatori.getIndicatore(5).setBatteriaZeroDebug(setting.elenco_indicatori.getIndicatore(5).getBatteriaZero(), txtZero6);			
			//setting.elenco_indicatori.getIndicatore(5).setBatteriaZeroDebug(setting.elenco_indicatori.getIndicatore(5).getBatteria(), textProcessata6);
			/*
			txtZero3.setText(setting.elenco_indicatori.getIndicatore(2).getBatteriaZero());
			textProcessata3.setText(setting.elenco_indicatori.getIndicatore(2).getBatteria());
			
			txtZero4.setText(setting.elenco_indicatori.getIndicatore(3).getBatteriaZero());
			textProcessata4.setText(setting.elenco_indicatori.getIndicatore(3).getBatteria());
			
			txtZero5.setText(setting.elenco_indicatori.getIndicatore(4).getBatteriaZero());
			textProcessata5.setText(setting.elenco_indicatori.getIndicatore(4).getBatteria());
			*/
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//fine while
		
	}//fine run
}
