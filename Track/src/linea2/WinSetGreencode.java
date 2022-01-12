package linea2;


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
		
		//AVVIO IL LETTORE check control
		dbgriglia = new DBCheckGriglia();
		//il risultato viene inserito in Setting.listaGreenCode
		dbgriglia.getElencoGreenCode();
		
		
							
		
		setResizable(false);
		setTitle("SELEZIONA BATTERIA LINEA 2");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 641, 424);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 245, 238));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JComboBox piastradaprodurre = new JComboBox();
		piastradaprodurre.setModel(new DefaultComboBoxModel(new String[] {""}));
		piastradaprodurre.setFont(new Font("Arial", Font.BOLD, 30));
		piastradaprodurre.setBounds(49, 130, 543,82);
		contentPane.add(piastradaprodurre);
		
		
		
		
		
		//String[] array = Setting.listaGreenCode.toArray(new String[0]);
		//greenCode[] array = Setting.listaGreenCode.toArray(new String[0]);
		
        /*
        for (String n : array) {
        	piastradaprodurre.addItem(n);
        }
        */
        
        for (int i = 0; i < Setting.listaGreenCode.size(); i++) {
            //System.out.println(Setting.listaGreenCode.get(i));
            piastradaprodurre.addItem(Setting.listaGreenCode.get(i).getGreencode());
        }
		
		
		JButton btnAvvio = new JButton("INVIA AL MARCATORE");
		btnAvvio.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
				if (!dbcommand.writeGreenCode(Setting.listaGreenCode.get(piastradaprodurre.getSelectedIndex())))
					
				{
					JOptionPane.showMessageDialog(contentPane, "Errore esecuzione comando !!","ATTENZIONE",JOptionPane.ERROR_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(contentPane, "Piastra impostata correttamente","OK",JOptionPane.INFORMATION_MESSAGE);
					txtpiastra.setText(""+piastradaprodurre.getItemAt(piastradaprodurre.getSelectedIndex()));
				}
			}
		});
		btnAvvio.setBackground(new Color(152, 251, 152));
		btnAvvio.setFont(new Font("Arial", Font.BOLD, 18));
		btnAvvio.setBounds(49, 223, 400, 59);
		contentPane.add(btnAvvio);
		
		JButton btnAnnulla = new JButton("ANNULLA");
		btnAnnulla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				dispose();
			}
		});
		btnAnnulla.setFont(new Font("Arial", Font.BOLD, 18));
		btnAnnulla.setBackground(new Color(255, 182, 193));
		btnAnnulla.setBounds(459, 223, 133, 59);
		contentPane.add(btnAnnulla);
		
		txtpiastra = new JTextField();
		txtpiastra.setForeground(new Color(255, 0, 0));
		txtpiastra.setEditable(false);
		txtpiastra.setFont(new Font("Arial", Font.BOLD, 14));
		txtpiastra.setBounds(49, 51, 543, 35);
		contentPane.add(txtpiastra);
		txtpiastra.setColumns(10);
		
		try {
			txtpiastra.setText(dbcommand.leggiGreenCode().getGreencode());
		}catch(Exception j) {
			log.write("WinSetGrennCode. Errore impostazioni greencode letto");
			System.out.println("WinSetGrennCode. Errore impostazioni greencode letto");
		}
			
		
		JLabel lblNewLabel = new JLabel("BATTERIA IN PRODUZIONE");
		lblNewLabel.setForeground(SystemColor.controlDkShadow);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel.setBounds(49, 21, 182, 29);
		contentPane.add(lblNewLabel);
		
		JLabel lblPiastraDaProdurre = new JLabel("BATTERIA DA PRODURRE");
		lblPiastraDaProdurre.setForeground(SystemColor.controlDkShadow);
		lblPiastraDaProdurre.setFont(new Font("Arial", Font.BOLD, 12));
		lblPiastraDaProdurre.setBounds(49, 97, 182, 29);
		contentPane.add(lblPiastraDaProdurre);
	}
	
	
	
	
	
	
	
}//fine classe
