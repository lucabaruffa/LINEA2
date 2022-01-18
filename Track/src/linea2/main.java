package linea2;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;

import java.awt.SystemColor;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;

import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import DB.CaricaDatiFromDB;
import DB.CheckControl;
import DB.CheckControlCVS;
import DB.DBCommand;
import DB.SenderDB;
import Moka7.S7;
import PLC.ConfiguratorePLc;
import PLC.plcCommand;
import View.Scarti;
import View.View_Impostazioni_PLC;

import javax.swing.JTable;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.RowFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.Window.Type;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;

import java.awt.FlowLayout;
import javax.swing.JToggleButton;

public class main  {

	private JFrame frmPlc;
    protected static JTextArea monitor;
    public static JTextField statoplc;
	public String nomeThread ="";
	public int inizio = 0;
	public static boolean running = false;
	
	private Linea linea5;
	
	protected JProgressBar bufferBatterie;
	private  Thread t1;
	
	private static main window;
	private JTextField statodb;
	private JTextField batteria1;
	private JTextField stato1;
	private static JTextField tempo1;
	private JTextField tempo2;
	private JTextField stato2;
	private JTextField batteria2;
	private JTextField tempo3;
	private JTextField stato3;
	private JTextField batteria3;
	private JTextField tempo4;
	private JTextField stato4;
	private JTextField batteria4;
	private JTextField tempo5;
	private JTextField stato5;
	private JTextField batteria5;
	private JTextField tempo6;
	private JTextField stato6;
	private JTextField batteria6;
	private JTextField tempo7;
	private JTextField stato7;
	private JTextField batteria7;
	private JTextField tempo8;
	private JTextField stato8;
	private JTextField batteria8;
	private JTextField tempo9;
	private JTextField stato9;
	private JTextField batteria9;
	
	private ElencoIndicatori elenco_indicatori = new ElencoIndicatori();
	private SenderDB senderDB;	
	private ArrayBatterie arrayArrayBatterie = new ArrayBatterie();
	private JTextField conteggio1;
	private JTextField conteggio2;
	private JTextField conteggio5;
	private JTextField conteggio4;
	private JTextField conteggio3;
	private JTextField conteggio6;
	private JTextField conteggio7;
	private JTextField conteggio8;
	private JTextField conteggio9;
	
	private ConfiguratoreLinea confl;
	
	private Indicatore indicatore = new Indicatore();
	private static JTable table;
	private JTextField btemporanea1;
	private JTextField btemporanea2;
	private JTextField btemporanea3;
	private JTextField btemporanea4;
	private JTextField btemporanea5;
	private JTextField btemporanea6;
	private JTextField btemporanea7;
	private JTextField btemporanea8;
	private JTextField btemporanea9;
	private JTextField risultato1;
	private JTextField risultato2;
	private JTextField risultato3;
	private JTextField risultato4;
	private JTextField risultato5;
	private JTextField risultato6;
	private JTextField risultato7;
	private JTextField risultato8;
	private JTextField risultato9;
	private static JScrollPane scrollPane_1;
	
	private static DefaultTableModel model = new DefaultTableModel();
	private ArrayBatteryStory arrayBatteryStory;
	private JPanel panel_2;
	private JTextField statoLinea1;
	private JTextField timestatoLinea1;
	private JTextField statoLinea2;
	private JTextField timestatoLinea2;
	private JTextField statoLinea3;
	private JTextField timestatoLinea3;
	private JTextField statoLinea4;
	private JTextField timestatoLinea4;
	private JTextField statoLinea5;
	private JTextField timestatoLinea5;
	private JTextField statoLinea6;
	private JTextField timestatoLinea6;
	private JTextField statoLinea7;
	private JTextField timestatoLinea7;
	private JTextField statoLinea8;
	private JTextField timestatoLinea8;
	private JTextField statoLinea9;
	private JTextField timestatoLinea9;
	private JLabel banner;
	private JTextField search;
	private JMenuItem mntmNewMenuItem_5;
	private JTextField conteggiorip1;
	private JTextField conteggiorip2;
	private JTextField conteggiorip3;
	private JTextField conteggiorip4;
	private JTextField conteggiorip5;
	private JTextField conteggiorip6;
	private JTextField conteggiorip7;
	private JTextField conteggiorip8;
	private JTextField conteggiorip9;
	private JTextField scarto1;
	private JTextField scarto2;
	private JTextField scarto3;
	private JTextField scarto4;
	private JTextField scarto5;
	private JTextField scarto6;
	private JTextField scarto7;
	private JTextField scarto8;
	private JTextField scarto9;
	private JTextField numeroBatterieBuone;
	private JTextField numeroBatterieScarto;
	private JTextField turno;
	private JMenu mnNewMenu_4;
	private JMenuItem mntmNewMenuItem_6;
	private JToggleButton btn_abilitazione_10;
	
	private static LoggerFile log = new LoggerFile();
	private JTextField inizio_conteggio;
	private boolean riazzera_contatori = true;
	private JTextField o_data;
	private JTextField o_ora;
	
	String[] days = {"LUN", "MAR", "MER", "GIO", "VEN", "SAB", "DOM"};
	private JTextField time_avvio;
	private JTextField btemporanea10;
	private JTextField batteria10;
	private JTextField stato10;
	private JTextField risultato10;
	private JTextField tempo10;
	private JTextField conteggio10;
	private JTextField conteggiorip10;
	private JTextField scarto10;
	private JTextField statoLinea10;
	private JTextField timestatoLinea10;
	
	public SystemTray systemTray = SystemTray.getSystemTray();
	public PopupMenu trayPopupMenu = new PopupMenu();
	public TrayIcon trayIcon;
	public Image image;
	private JTextField areaErrore;
	private WinConfigurazioneLinea frame;
	private JTextField tipo_batteria;
	private Setting setting;
	private JMenuItem mntmNewMenuItem;
	private static loader dialog;
	private ConfiguratorePLc configuratore_plc;
	
	
	private static Activator attivatore = new Activator();
	private JLabel lblConfiguratorePlc;
	private JTextField stato_configuratore_plc;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private JButton btnNewButton_3_2;
	
	private BufferedImage wPic;
	private BufferedImage icona_pulsante_controllo;
	private BufferedImage icona_pulsante_controllo_off;
	private BufferedImage icona_pulsante_cambio_codice;
	private BufferedImage icona_pulsante_lettori;
	private BufferedImage icona_pulsante_viewer;
	private JTextField txtTipoBatteria;
	
	
	

	public static void main(String[] args) {
		
				
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName());
					 //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					 if(!SystemTray.isSupported()){
					        System.out.println("System tray non supportato !!! ");
					        return ;
					  }
				
				if (window == null) {	 
					window = new main();
					window.frmPlc.setVisible(true);
				}else
				{
					JOptionPane.showMessageDialog(window.frmPlc, "APPLICAZIONE GIA' IN ESECUZIONE !!","WARNING",JOptionPane.INFORMATION_MESSAGE);
				}
				
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}//fine run
		});
	}//fine main

	/**
	 * Create the application.
	 */
	public main() {
		
				
		initialize();
		
		
	
		//1t menuitem for popupmenu
	    MenuItem action = new MenuItem("Apri");
	    action.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            //JOptionPane.showMessageDialog(null, "Action Clicked");     
	        	frmPlc.setVisible(true);
	        }
	    });     
	    trayPopupMenu.add(action);

	    //2nd menuitem of popupmenu
	    MenuItem close = new MenuItem("Chiudi");
	    close.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	
	        	if (linea5!=null)
	        		//new DBCommand(true).invia_segnalazione("CONTROLLO LINEA 3","PROGRAMMA DI CONTROLLO CHIUSO!",Setting.EMAIL_TUTTI);
	        		
	        	try {
	        		linea5.closeAllThread(); 
	        		configuratore_plc.stop();
	        		log.write("EXIT PROGRAMMA \n");
					Thread.sleep(2000);
				}catch(Exception eee) {
					log.write("\nreader plc Errore inSleep!\n");
				}//fine catch
	        	
	        	System.exit(0);             
	        }
	    });
	    trayPopupMenu.add(close);

	    //setting tray icon
	    trayIcon = new TrayIcon(image, "Monitor Tracciabilita'", trayPopupMenu);
	    //adjust to default size as per system recommendation 
	    trayIcon.setImageAutoSize(true);

	    try{
	        systemTray.add(trayIcon);
	        
	        
	       //trasmetti();
			//btn_avvia.setEnabled(false);
			//btn_stop.setEnabled(true);
	        
	        
	    }catch(AWTException awtException){
	        awtException.printStackTrace();
	    }
	}//fine main
	
	public void setTempoUltima(String s) {
		tempo1.setText(s);
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//boolean result = this.isRunning("javaw.exe");
		//System.out.println("RISULTATO AVVIO : " + result);
		
		log.write("<--------------------------- AVVIO PROGRAMMA ---------------------------------->\n");
		
		try {
			icona_pulsante_controllo = ImageIO.read(this.getClass().getResource("/resource/btn_on.jpg"));
			icona_pulsante_controllo_off = ImageIO.read(this.getClass().getResource("/resource/btn_off.jpg"));
			icona_pulsante_cambio_codice = ImageIO.read(this.getClass().getResource("/resource/icon_laser.png"));
			icona_pulsante_lettori = ImageIO.read(this.getClass().getResource("/resource/scanner.png"));
			icona_pulsante_viewer = ImageIO.read(this.getClass().getResource("/resource/occhio.png"));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		/*
		Runnable runnable3 =
				() ->  {	
					dialog = new loader();
					dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
					dialog.setVisible(true);
				};//fine runnable2
				 //fine runnable2
				
				//AVVIO runnable2
		Thread t3 = new Thread(runnable3);	
		t3.start();
		*/
		
		try {
			setting = new Setting();
		} catch (Exception e4) {
			log.write("Errore in main in avvio setting. Errore:" + e4.toString());
		}
		
		frmPlc = new JFrame();
		
		
		
		
		frmPlc.setIconImage(Toolkit.getDefaultToolkit().getImage(main.class.getResource("/resource/icon.png")));
		
		java.net.URL imgURL = getClass().getResource("/resource/icon.png");
		image = Toolkit.getDefaultToolkit().getImage(imgURL);
		
	
		
		frmPlc.getContentPane().setBackground(new Color(255, 255, 255));
		frmPlc.setTitle("EVIDON LINEA 2");
		frmPlc.setBounds(2, 2, 1927, 1051);
		frmPlc.getContentPane().setLayout(null);
		
		
		//dialog = new loader();
		
		
		
		
		if (attivatore.isAppActive()) {
			JOptionPane.showMessageDialog(frmPlc, "APPLICAZIONE GIA' IN ESECUZIONE !!","ATTENZIONE",JOptionPane.ERROR_MESSAGE);
			log.write("<PROGRAMMA GIA AVVIATO>\n");
           System.exit(1);    
      }//fine if
		
		
		log.write("AVVIO");
		//scrollPane.setViewportView(monitor);
		
		statoplc = new JTextField();
		statoplc.setEditable(false);
		statoplc.setBounds(116, 876, 77, 20);
		frmPlc.getContentPane().add(statoplc);
		statoplc.setColumns(10);
		//Font font = new Font("Serif", Font.PLAIN, 12);
		
		bufferBatterie = new JProgressBar();
		bufferBatterie.setFont(new Font("Arial", Font.BOLD, 8));
		bufferBatterie.setMaximum(8);
		bufferBatterie.setForeground(new Color(102, 205, 170));
		bufferBatterie.setStringPainted(true);
		bufferBatterie.setToolTipText("buffer batterie");
		bufferBatterie.setBackground(new Color(211, 211, 211));
		bufferBatterie.setBounds(212, 961, 1689, 14);
		frmPlc.getContentPane().add(bufferBatterie);
		bufferBatterie.setString("0 BATTERIE IN CODA");
		
		JLabel lblStatoPlc = new JLabel("PLC");
		lblStatoPlc.setHorizontalAlignment(SwingConstants.LEFT);
		lblStatoPlc.setBounds(116, 862, 77, 14);
		frmPlc.getContentPane().add(lblStatoPlc);
		
		JLabel lblStatoDb = new JLabel("DB");
		lblStatoDb.setHorizontalAlignment(SwingConstants.LEFT);
		lblStatoDb.setBounds(10, 862, 73, 14);
		frmPlc.getContentPane().add(lblStatoDb);
		
		statodb = new JTextField();
		statodb.setEditable(false);
		statodb.setColumns(10);
		statodb.setBounds(10, 876, 73, 20);
		frmPlc.getContentPane().add(statodb);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaptionBorder);
		panel.setBounds(361, 83, 1330, 384);
		frmPlc.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblBatteriaInTransito = new JLabel("BATTERIA TESTATA");
		lblBatteriaInTransito.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblBatteriaInTransito.setBounds(10, 78, 119, 14);
		panel.add(lblBatteriaInTransito);
		
		batteria1 = new JTextField();
		batteria1.setHorizontalAlignment(SwingConstants.CENTER);
		batteria1.setForeground(Color.DARK_GRAY);
		batteria1.setFont(new Font("Arial", Font.BOLD, 9));
		batteria1.setBackground(new Color(255, 255, 255));
		batteria1.setEditable(false);
		batteria1.setBounds(10, 92, 119, 24);
		panel.add(batteria1);
		batteria1.setColumns(10);
		
		JLabel lblStatoTest = new JLabel("STATO TEST");
		lblStatoTest.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblStatoTest.setBounds(10, 118, 119, 14);
		panel.add(lblStatoTest);
		
		stato1 = new JTextField();
		stato1.setHorizontalAlignment(SwingConstants.CENTER);
		stato1.setForeground(Color.DARK_GRAY);
		stato1.setFont(new Font("Arial", Font.BOLD, 15));
		stato1.setEditable(false);
		stato1.setColumns(10);
		stato1.setBackground(Color.WHITE);
		stato1.setBounds(10, 134, 119, 24);
		panel.add(stato1);
		
		tempo1 = new JTextField();
		tempo1.setHorizontalAlignment(SwingConstants.CENTER);
		tempo1.setForeground(Color.GRAY);
		tempo1.setFont(new Font("Arial", Font.ITALIC, 10));
		tempo1.setEditable(false);
		tempo1.setColumns(10);
		tempo1.setBackground(Color.WHITE);
		tempo1.setBounds(10, 203, 119, 24);
		panel.add(tempo1);
		
		JLabel lblUltimaLettura = new JLabel("ULTIMA LETTURA");
		lblUltimaLettura.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblUltimaLettura.setBounds(10, 187, 119, 14);
		panel.add(lblUltimaLettura);
		
		JLabel lblNewLabel = new JLabel("CON. CORTI 1");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel.setForeground(new Color(0, 139, 139));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 21, 119, 26);
		panel.add(lblNewLabel);
		
		tempo2 = new JTextField();
		tempo2.setHorizontalAlignment(SwingConstants.CENTER);
		tempo2.setForeground(Color.GRAY);
		tempo2.setFont(new Font("Arial", Font.ITALIC, 10));
		tempo2.setEditable(false);
		tempo2.setColumns(10);
		tempo2.setBackground(Color.WHITE);
		tempo2.setBounds(139, 203, 119, 24);
		panel.add(tempo2);
		
		JLabel lblUltimaLettura_1 = new JLabel("ULTIMA LETTURA");
		lblUltimaLettura_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblUltimaLettura_1.setBounds(139, 187, 119, 14);
		panel.add(lblUltimaLettura_1);
		
		stato2 = new JTextField();
		stato2.setHorizontalAlignment(SwingConstants.CENTER);
		stato2.setForeground(Color.DARK_GRAY);
		stato2.setFont(new Font("Arial", Font.BOLD, 15));
		stato2.setEditable(false);
		stato2.setColumns(10);
		stato2.setBackground(Color.WHITE);
		stato2.setBounds(139, 134, 119, 24);
		panel.add(stato2);
		
		JLabel lblStatoTest_1 = new JLabel("STATO TEST");
		lblStatoTest_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblStatoTest_1.setBounds(139, 118, 119, 14);
		panel.add(lblStatoTest_1);
		
		batteria2 = new JTextField();
		batteria2.setHorizontalAlignment(SwingConstants.CENTER);
		batteria2.setForeground(Color.DARK_GRAY);
		batteria2.setFont(new Font("Arial", Font.BOLD, 9));
		batteria2.setEditable(false);
		batteria2.setColumns(10);
		batteria2.setBackground(Color.WHITE);
		batteria2.setBounds(139, 92, 119, 24);
		panel.add(batteria2);
		
		JLabel lblBatteriaInTransito_1 = new JLabel("BATTERIA TESTATA");
		lblBatteriaInTransito_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblBatteriaInTransito_1.setBounds(139, 78, 119, 14);
		panel.add(lblBatteriaInTransito_1);
		
		JLabel lblPostazione = new JLabel("PUNTATRICE 1");
		lblPostazione.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione.setForeground(new Color(0, 139, 139));
		lblPostazione.setFont(new Font("Arial", Font.BOLD, 14));
		lblPostazione.setBounds(139, 21, 119, 26);
		panel.add(lblPostazione);
		
		tempo3 = new JTextField();
		tempo3.setHorizontalAlignment(SwingConstants.CENTER);
		tempo3.setForeground(Color.GRAY);
		tempo3.setFont(new Font("Arial", Font.ITALIC, 10));
		tempo3.setEditable(false);
		tempo3.setColumns(10);
		tempo3.setBackground(Color.WHITE);
		tempo3.setBounds(268, 203, 119, 24);
		panel.add(tempo3);
		
		JLabel lblUltimaLettura_2 = new JLabel("ULTIMA LETTURA");
		lblUltimaLettura_2.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblUltimaLettura_2.setBounds(268, 187, 119, 14);
		panel.add(lblUltimaLettura_2);
		
		stato3 = new JTextField();
		stato3.setHorizontalAlignment(SwingConstants.CENTER);
		stato3.setForeground(Color.DARK_GRAY);
		stato3.setFont(new Font("Arial", Font.BOLD, 15));
		stato3.setEditable(false);
		stato3.setColumns(10);
		stato3.setBackground(Color.WHITE);
		stato3.setBounds(268, 134, 119, 24);
		panel.add(stato3);
		
		JLabel lblStatoTest_2 = new JLabel("STATO TEST");
		lblStatoTest_2.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblStatoTest_2.setBounds(268, 118, 119, 14);
		panel.add(lblStatoTest_2);
		
		batteria3 = new JTextField();
		batteria3.setHorizontalAlignment(SwingConstants.CENTER);
		batteria3.setForeground(Color.DARK_GRAY);
		batteria3.setFont(new Font("Arial", Font.BOLD, 9));
		batteria3.setEditable(false);
		batteria3.setColumns(10);
		batteria3.setBackground(Color.WHITE);
		batteria3.setBounds(268, 92, 119, 24);
		panel.add(batteria3);
		
		JLabel lblBatteriaInTransito_2 = new JLabel("BATTERIA TESTATA");
		lblBatteriaInTransito_2.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblBatteriaInTransito_2.setBounds(268, 78, 119, 14);
		panel.add(lblBatteriaInTransito_2);
		
		JLabel lblPostazione_1 = new JLabel("PUNTATRICE 2");
		lblPostazione_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione_1.setForeground(new Color(0, 139, 139));
		lblPostazione_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblPostazione_1.setBounds(268, 21, 119, 26);
		panel.add(lblPostazione_1);
		
		tempo4 = new JTextField();
		tempo4.setHorizontalAlignment(SwingConstants.CENTER);
		tempo4.setForeground(Color.GRAY);
		tempo4.setFont(new Font("Arial", Font.ITALIC, 10));
		tempo4.setEditable(false);
		tempo4.setColumns(10);
		tempo4.setBackground(Color.WHITE);
		tempo4.setBounds(397, 203, 119, 24);
		panel.add(tempo4);
		
		JLabel lblUltimaLettura_3 = new JLabel("ULTIMA LETTURA");
		lblUltimaLettura_3.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblUltimaLettura_3.setBounds(397, 187, 119, 14);
		panel.add(lblUltimaLettura_3);
		
		stato4 = new JTextField();
		stato4.setHorizontalAlignment(SwingConstants.CENTER);
		stato4.setForeground(Color.DARK_GRAY);
		stato4.setFont(new Font("Arial", Font.BOLD, 15));
		stato4.setEditable(false);
		stato4.setColumns(10);
		stato4.setBackground(Color.WHITE);
		stato4.setBounds(397, 134, 119, 24);
		panel.add(stato4);
		
		JLabel lblStatoTest_3 = new JLabel("STATO TEST");
		lblStatoTest_3.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblStatoTest_3.setBounds(397, 118, 119, 14);
		panel.add(lblStatoTest_3);
		
		batteria4 = new JTextField();
		batteria4.setHorizontalAlignment(SwingConstants.CENTER);
		batteria4.setForeground(Color.DARK_GRAY);
		batteria4.setFont(new Font("Arial", Font.BOLD, 9));
		batteria4.setEditable(false);
		batteria4.setColumns(10);
		batteria4.setBackground(Color.WHITE);
		batteria4.setBounds(397, 92, 119, 24);
		panel.add(batteria4);
		
		JLabel lblBatteriaInTransito_3 = new JLabel("BATTERIA TESTATA");
		lblBatteriaInTransito_3.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblBatteriaInTransito_3.setBounds(397, 78, 119, 14);
		panel.add(lblBatteriaInTransito_3);
		
		JLabel lblPostazione_2 = new JLabel("CON. CORTI 2");
		lblPostazione_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione_2.setForeground(new Color(0, 139, 139));
		lblPostazione_2.setFont(new Font("Arial", Font.BOLD, 14));
		lblPostazione_2.setBounds(397, 21, 119, 26);
		panel.add(lblPostazione_2);
		
		tempo5 = new JTextField();
		tempo5.setHorizontalAlignment(SwingConstants.CENTER);
		tempo5.setForeground(Color.GRAY);
		tempo5.setFont(new Font("Arial", Font.ITALIC, 10));
		tempo5.setEditable(false);
		tempo5.setColumns(10);
		tempo5.setBackground(Color.WHITE);
		tempo5.setBounds(526, 203, 119, 24);
		panel.add(tempo5);
		
		JLabel lblUltimaLettura_4 = new JLabel("ULTIMA LETTURA");
		lblUltimaLettura_4.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblUltimaLettura_4.setBounds(526, 187, 119, 14);
		panel.add(lblUltimaLettura_4);
		
		stato5 = new JTextField();
		stato5.setHorizontalAlignment(SwingConstants.CENTER);
		stato5.setForeground(Color.DARK_GRAY);
		stato5.setFont(new Font("Arial", Font.BOLD, 15));
		stato5.setEditable(false);
		stato5.setColumns(10);
		stato5.setBackground(Color.WHITE);
		stato5.setBounds(526, 134, 119, 24);
		panel.add(stato5);
		
		JLabel lblStatoTest_4 = new JLabel("STATO TEST");
		lblStatoTest_4.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblStatoTest_4.setBounds(526, 118, 119, 14);
		panel.add(lblStatoTest_4);
		
		batteria5 = new JTextField();
		batteria5.setHorizontalAlignment(SwingConstants.CENTER);
		batteria5.setForeground(Color.DARK_GRAY);
		batteria5.setFont(new Font("Arial", Font.BOLD, 9));
		batteria5.setEditable(false);
		batteria5.setColumns(10);
		batteria5.setBackground(Color.WHITE);
		batteria5.setBounds(526, 92, 119, 24);
		panel.add(batteria5);
		
		JLabel lblBatteriaInTransito_4 = new JLabel("BATTERIA TESTATA");
		lblBatteriaInTransito_4.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblBatteriaInTransito_4.setBounds(526, 78, 119, 14);
		panel.add(lblBatteriaInTransito_4);
		
		JLabel lblPostazione_3 = new JLabel("PROVA TENUTA 1");
		lblPostazione_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione_3.setForeground(new Color(0, 139, 139));
		lblPostazione_3.setFont(new Font("Arial", Font.BOLD, 14));
		lblPostazione_3.setBounds(526, 21, 119, 26);
		panel.add(lblPostazione_3);
		
		tempo6 = new JTextField();
		tempo6.setHorizontalAlignment(SwingConstants.CENTER);
		tempo6.setForeground(Color.GRAY);
		tempo6.setFont(new Font("Arial", Font.ITALIC, 10));
		tempo6.setEditable(false);
		tempo6.setColumns(10);
		tempo6.setBackground(Color.WHITE);
		tempo6.setBounds(655, 203, 119, 24);
		panel.add(tempo6);
		
		JLabel lblUltimaLettura_5 = new JLabel("ULTIMA LETTURA");
		lblUltimaLettura_5.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblUltimaLettura_5.setBounds(655, 187, 119, 14);
		panel.add(lblUltimaLettura_5);
		
		stato6 = new JTextField();
		stato6.setHorizontalAlignment(SwingConstants.CENTER);
		stato6.setForeground(Color.DARK_GRAY);
		stato6.setFont(new Font("Arial", Font.BOLD, 15));
		stato6.setEditable(false);
		stato6.setColumns(10);
		stato6.setBackground(Color.WHITE);
		stato6.setBounds(655, 134, 119, 24);
		panel.add(stato6);
		
		JLabel lblStatoTest_5 = new JLabel("STATO TEST");
		lblStatoTest_5.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblStatoTest_5.setBounds(655, 118, 119, 14);
		panel.add(lblStatoTest_5);
		
		batteria6 = new JTextField();
		batteria6.setHorizontalAlignment(SwingConstants.CENTER);
		batteria6.setForeground(Color.DARK_GRAY);
		batteria6.setFont(new Font("Arial", Font.BOLD, 9));
		batteria6.setEditable(false);
		batteria6.setColumns(10);
		batteria6.setBackground(Color.WHITE);
		batteria6.setBounds(655, 92, 119, 24);
		panel.add(batteria6);
		
		JLabel lblBatteriaInTransito_5 = new JLabel("BATTERIA TESTATA");
		lblBatteriaInTransito_5.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblBatteriaInTransito_5.setBounds(655, 78, 119, 14);
		panel.add(lblBatteriaInTransito_5);
		
		JLabel lblPostazione_4 = new JLabel("PROVA TENUTA 2");
		lblPostazione_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione_4.setForeground(new Color(0, 139, 139));
		lblPostazione_4.setFont(new Font("Arial", Font.BOLD, 14));
		lblPostazione_4.setBounds(655, 21, 119, 26);
		panel.add(lblPostazione_4);
		
		tempo7 = new JTextField();
		tempo7.setHorizontalAlignment(SwingConstants.CENTER);
		tempo7.setForeground(Color.GRAY);
		tempo7.setFont(new Font("Arial", Font.ITALIC, 10));
		tempo7.setEditable(false);
		tempo7.setColumns(10);
		tempo7.setBackground(Color.WHITE);
		tempo7.setBounds(784, 203, 119, 24);
		panel.add(tempo7);
		
		JLabel lblUltimaLettura_6 = new JLabel("ULTIMA LETTURA");
		lblUltimaLettura_6.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblUltimaLettura_6.setBounds(784, 187, 119, 14);
		panel.add(lblUltimaLettura_6);
		
		stato7 = new JTextField();
		stato7.setHorizontalAlignment(SwingConstants.CENTER);
		stato7.setForeground(Color.DARK_GRAY);
		stato7.setFont(new Font("Arial", Font.BOLD, 15));
		stato7.setEditable(false);
		stato7.setColumns(10);
		stato7.setBackground(Color.WHITE);
		stato7.setBounds(784, 134, 119, 24);
		panel.add(stato7);
		
		JLabel lblStatoTest_6 = new JLabel("STATO TEST");
		lblStatoTest_6.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblStatoTest_6.setBounds(784, 118, 119, 14);
		panel.add(lblStatoTest_6);
		
		batteria7 = new JTextField();
		batteria7.setHorizontalAlignment(SwingConstants.CENTER);
		batteria7.setForeground(Color.DARK_GRAY);
		batteria7.setFont(new Font("Arial", Font.BOLD, 9));
		batteria7.setEditable(false);
		batteria7.setColumns(10);
		batteria7.setBackground(Color.WHITE);
		batteria7.setBounds(784, 92, 119, 24);
		panel.add(batteria7);
		
		JLabel lblBatteriaInTransito_6 = new JLabel("BATTERIA TESTATA");
		lblBatteriaInTransito_6.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblBatteriaInTransito_6.setBounds(784, 78, 119, 14);
		panel.add(lblBatteriaInTransito_6);
		
		JLabel lblPostazione_5 = new JLabel("ALT. POLARI");
		lblPostazione_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione_5.setForeground(new Color(0, 139, 139));
		lblPostazione_5.setFont(new Font("Arial", Font.BOLD, 14));
		lblPostazione_5.setBounds(784, 21, 119, 26);
		panel.add(lblPostazione_5);
		
		tempo8 = new JTextField();
		tempo8.setHorizontalAlignment(SwingConstants.CENTER);
		tempo8.setForeground(Color.GRAY);
		tempo8.setFont(new Font("Arial", Font.ITALIC, 10));
		tempo8.setEditable(false);
		tempo8.setColumns(10);
		tempo8.setBackground(Color.WHITE);
		tempo8.setBounds(913, 203, 119, 24);
		panel.add(tempo8);
		
		JLabel lblUltimaLettura_7 = new JLabel("ULTIMA LETTURA");
		lblUltimaLettura_7.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblUltimaLettura_7.setBounds(913, 187, 119, 14);
		panel.add(lblUltimaLettura_7);
		
		stato8 = new JTextField();
		stato8.setHorizontalAlignment(SwingConstants.CENTER);
		stato8.setForeground(Color.DARK_GRAY);
		stato8.setFont(new Font("Arial", Font.BOLD, 15));
		stato8.setEditable(false);
		stato8.setColumns(10);
		stato8.setBackground(Color.WHITE);
		stato8.setBounds(913, 134, 119, 24);
		panel.add(stato8);
		
		JLabel lblStatoTest_7 = new JLabel("STATO TEST");
		lblStatoTest_7.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblStatoTest_7.setBounds(913, 118, 119, 14);
		panel.add(lblStatoTest_7);
		
		batteria8 = new JTextField();
		batteria8.setHorizontalAlignment(SwingConstants.CENTER);
		batteria8.setForeground(Color.DARK_GRAY);
		batteria8.setFont(new Font("Arial", Font.BOLD, 9));
		batteria8.setEditable(false);
		batteria8.setColumns(10);
		batteria8.setBackground(Color.WHITE);
		batteria8.setBounds(913, 92, 119, 24);
		panel.add(batteria8);
		
		JLabel lblBatteriaInTransito_7 = new JLabel("BATTERIA TESTATA");
		lblBatteriaInTransito_7.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblBatteriaInTransito_7.setBounds(913, 78, 119, 14);
		panel.add(lblBatteriaInTransito_7);
		
		JLabel lblPostazione_6 = new JLabel("BILANCIA 1");
		lblPostazione_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione_6.setForeground(new Color(0, 139, 139));
		lblPostazione_6.setFont(new Font("Arial", Font.BOLD, 14));
		lblPostazione_6.setBounds(913, 21, 119, 26);
		panel.add(lblPostazione_6);
		
		tempo9 = new JTextField();
		tempo9.setHorizontalAlignment(SwingConstants.CENTER);
		tempo9.setForeground(Color.GRAY);
		tempo9.setFont(new Font("Arial", Font.ITALIC, 10));
		tempo9.setEditable(false);
		tempo9.setColumns(10);
		tempo9.setBackground(Color.WHITE);
		tempo9.setBounds(1041, 203, 119, 24);
		panel.add(tempo9);
		
		JLabel lblUltimaLettura_7_1 = new JLabel("ULTIMA LETTURA");
		lblUltimaLettura_7_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblUltimaLettura_7_1.setBounds(1041, 187, 97, 14);
		panel.add(lblUltimaLettura_7_1);
		
		stato9 = new JTextField();
		stato9.setHorizontalAlignment(SwingConstants.CENTER);
		stato9.setForeground(Color.DARK_GRAY);
		stato9.setFont(new Font("Arial", Font.BOLD, 15));
		stato9.setEditable(false);
		stato9.setColumns(10);
		stato9.setBackground(Color.WHITE);
		stato9.setBounds(1041, 134, 119, 24);
		panel.add(stato9);
		
		JLabel lblStatoTest_7_1 = new JLabel("STATO TEST");
		lblStatoTest_7_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblStatoTest_7_1.setBounds(1041, 118, 119, 14);
		panel.add(lblStatoTest_7_1);
		
		batteria9 = new JTextField();
		batteria9.setHorizontalAlignment(SwingConstants.CENTER);
		batteria9.setForeground(Color.DARK_GRAY);
		batteria9.setFont(new Font("Arial", Font.BOLD, 9));
		batteria9.setEditable(false);
		batteria9.setColumns(10);
		batteria9.setBackground(Color.WHITE);
		batteria9.setBounds(1041, 92, 119, 24);
		panel.add(batteria9);
		
		JLabel lblBatteriaInTransito_7_1 = new JLabel("BATTERIA TESTATA");
		lblBatteriaInTransito_7_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblBatteriaInTransito_7_1.setBounds(1041, 78, 119, 14);
		panel.add(lblBatteriaInTransito_7_1);
		
		JLabel lblNewLabel_7_1 = new JLabel("BILANCIA 2");
		lblNewLabel_7_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_1.setForeground(new Color(0, 139, 139));
		lblNewLabel_7_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_7_1.setBounds(1041, 21, 119, 26);
		panel.add(lblNewLabel_7_1);
		
		JMenuBar menuBar = new JMenuBar();
		frmPlc.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Connessione");
		menuBar.add(mnNewMenu);
		
		mntmNewMenuItem = new JMenuItem("Connetti");
		mntmNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmNewMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
				start();
				
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenu mnNewMenu_1 = new JMenu("Impostazioni");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Parametri Connessione");
		mntmNewMenuItem_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SettingFrame frame = new SettingFrame();
				frame.setVisible(true);
			}
			
		});
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Configurazione Lettori");
		mntmNewMenuItem_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				WinConfigurazioneLinea frame = new WinConfigurazioneLinea(monitor);
				frame.setVisible(true);
				
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_4);
		mnNewMenu_1.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_8 = new JMenuItem("Parametric PLC");
		mntmNewMenuItem_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				View_Impostazioni_PLC win_impostazioni_plc = new View_Impostazioni_PLC();
				win_impostazioni_plc.setVisible(true);
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_8);
		
		mnNewMenu_4 = new JMenu("Segnalazioni");
		menuBar.add(mnNewMenu_4);
		
		mntmNewMenuItem_6 = new JMenuItem("Invia una segnalazione");
		mntmNewMenuItem_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Segnalazione frame = new Segnalazione();
				frame.setVisible(true);
			}
		});
		mnNewMenu_4.add(mntmNewMenuItem_6);
		
		JMenu mnNewMenu_3 = new JMenu("Log");
		menuBar.add(mnNewMenu_3);
		
		mntmNewMenuItem_5 = new JMenuItem("Visualizza log");
		mntmNewMenuItem_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Log frameLog = new Log();
				frameLog.setVisible(true);
			}
		});
		mntmNewMenuItem_5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		mnNewMenu_3.add(mntmNewMenuItem_5);
		
		JMenuItem mntmNewMenuItem_7 = new JMenuItem("Viewer");
		mntmNewMenuItem_7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Scarti viewer = new Scarti("INIT");
			}
		});
		mnNewMenu_3.add(mntmNewMenuItem_7);
		
		JMenu mnNewMenu_2 = new JMenu("?");
		menuBar.add(mnNewMenu_2);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Help");
		mnNewMenu_2.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Version");
		mntmNewMenuItem_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JOptionPane.showMessageDialog(frmPlc, "Ver. 2.01 - 2021-11-08","INFO",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnNewMenu_2.add(mntmNewMenuItem_3);
		
		
		JLabel lblConteggio = new JLabel("CONTEGGIO");
		lblConteggio.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblConteggio.setBounds(10, 238, 119, 14);
		panel.add(lblConteggio);
		
		conteggio1 = new JTextField();
		conteggio1.setHorizontalAlignment(SwingConstants.CENTER);
		conteggio1.setForeground(Color.DARK_GRAY);
		conteggio1.setFont(new Font("Arial", Font.BOLD, 12));
		conteggio1.setEditable(false);
		conteggio1.setColumns(10);
		conteggio1.setBackground(Color.WHITE);
		conteggio1.setBounds(10, 254, 119, 24);
		panel.add(conteggio1);
		
		conteggio2 = new JTextField();
		conteggio2.setHorizontalAlignment(SwingConstants.CENTER);
		conteggio2.setForeground(Color.DARK_GRAY);
		conteggio2.setFont(new Font("Arial", Font.BOLD, 12));
		conteggio2.setEditable(false);
		conteggio2.setColumns(10);
		conteggio2.setBackground(Color.WHITE);
		conteggio2.setBounds(139, 254, 119, 24);
		panel.add(conteggio2);
		
		JLabel lblConteggio_1 = new JLabel("CONTEGGIO");
		lblConteggio_1.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblConteggio_1.setBounds(139, 238, 119, 14);
		panel.add(lblConteggio_1);
		
		conteggio5 = new JTextField();
		conteggio5.setHorizontalAlignment(SwingConstants.CENTER);
		conteggio5.setForeground(Color.DARK_GRAY);
		conteggio5.setFont(new Font("Arial", Font.BOLD, 12));
		conteggio5.setEditable(false);
		conteggio5.setColumns(10);
		conteggio5.setBackground(Color.WHITE);
		conteggio5.setBounds(526, 254, 119, 24);
		panel.add(conteggio5);
		
		JLabel lblConteggio_2 = new JLabel("CONTEGGIO");
		lblConteggio_2.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblConteggio_2.setBounds(526, 238, 119, 14);
		panel.add(lblConteggio_2);
		
		conteggio4 = new JTextField();
		conteggio4.setHorizontalAlignment(SwingConstants.CENTER);
		conteggio4.setForeground(Color.DARK_GRAY);
		conteggio4.setFont(new Font("Arial", Font.BOLD, 12));
		conteggio4.setEditable(false);
		conteggio4.setColumns(10);
		conteggio4.setBackground(Color.WHITE);
		conteggio4.setBounds(397, 254, 119, 24);
		panel.add(conteggio4);
		
		JLabel lblConteggio_3 = new JLabel("CONTEGGIO");
		lblConteggio_3.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblConteggio_3.setBounds(397, 238, 119, 14);
		panel.add(lblConteggio_3);
		
		conteggio3 = new JTextField();
		conteggio3.setHorizontalAlignment(SwingConstants.CENTER);
		conteggio3.setForeground(Color.DARK_GRAY);
		conteggio3.setFont(new Font("Arial", Font.BOLD, 12));
		conteggio3.setEditable(false);
		conteggio3.setColumns(10);
		conteggio3.setBackground(Color.WHITE);
		conteggio3.setBounds(268, 254, 119, 24);
		panel.add(conteggio3);
		
		JLabel lblConteggio_2_1 = new JLabel("CONTEGGIO");
		lblConteggio_2_1.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblConteggio_2_1.setBounds(268, 238, 119, 14);
		panel.add(lblConteggio_2_1);
		
		conteggio6 = new JTextField();
		conteggio6.setHorizontalAlignment(SwingConstants.CENTER);
		conteggio6.setForeground(Color.DARK_GRAY);
		conteggio6.setFont(new Font("Arial", Font.BOLD, 12));
		conteggio6.setEditable(false);
		conteggio6.setColumns(10);
		conteggio6.setBackground(Color.WHITE);
		conteggio6.setBounds(655, 254, 119, 24);
		panel.add(conteggio6);
		
		JLabel lblConteggio_2_1_1 = new JLabel("CONTEGGIO");
		lblConteggio_2_1_1.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblConteggio_2_1_1.setBounds(655, 238, 119, 14);
		panel.add(lblConteggio_2_1_1);
		
		conteggio7 = new JTextField();
		conteggio7.setHorizontalAlignment(SwingConstants.CENTER);
		conteggio7.setForeground(Color.DARK_GRAY);
		conteggio7.setFont(new Font("Arial", Font.BOLD, 12));
		conteggio7.setEditable(false);
		conteggio7.setColumns(10);
		conteggio7.setBackground(Color.WHITE);
		conteggio7.setBounds(784, 254, 119, 24);
		panel.add(conteggio7);
		
		JLabel lblConteggio_2_1_2 = new JLabel("CONTEGGIO");
		lblConteggio_2_1_2.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblConteggio_2_1_2.setBounds(784, 238, 119, 14);
		panel.add(lblConteggio_2_1_2);
		
		conteggio8 = new JTextField();
		conteggio8.setHorizontalAlignment(SwingConstants.CENTER);
		conteggio8.setForeground(Color.DARK_GRAY);
		conteggio8.setFont(new Font("Arial", Font.BOLD, 12));
		conteggio8.setEditable(false);
		conteggio8.setColumns(10);
		conteggio8.setBackground(Color.WHITE);
		conteggio8.setBounds(913, 254, 119, 24);
		panel.add(conteggio8);
		
		JLabel lblConteggio_2_1_3 = new JLabel("CONTEGGIO");
		lblConteggio_2_1_3.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblConteggio_2_1_3.setBounds(913, 238, 119, 14);
		panel.add(lblConteggio_2_1_3);
		
		conteggio9 = new JTextField();
		conteggio9.setHorizontalAlignment(SwingConstants.CENTER);
		conteggio9.setForeground(Color.DARK_GRAY);
		conteggio9.setFont(new Font("Arial", Font.BOLD, 12));
		conteggio9.setEditable(false);
		conteggio9.setColumns(10);
		conteggio9.setBackground(Color.WHITE);
		conteggio9.setBounds(1041, 254, 119, 24);
		panel.add(conteggio9);
		
		JLabel lblConteggio_2_1_4 = new JLabel("CONTEGGIO");
		lblConteggio_2_1_4.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblConteggio_2_1_4.setBounds(1041, 238, 119, 14);
		panel.add(lblConteggio_2_1_4);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(212, 735, 1479, 209);
		frmPlc.getContentPane().add(scrollPane_1);
		
		String[][] data = { 
	            { "11/11/2020 10:42:27","0000125450534", "OK", "OK", "OK", "OK", "OK","OK","KO","OK","OK" } 
	        }; 
	  
	        // Column Names 
	    String[] columnNames = {"TIME P1", "COD. BATTERIA", "P1", "P2" ,"P3","P4","P5","P6","P7","P8","P9"}; 
	    model.setColumnIdentifiers(columnNames); 
	   
		table = new JTable(data,columnNames);
		table.setModel(model);
		
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		
		 
			table.setRowSorter(sorter);
		
			
		
	    
	    
	    
	    table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
	    //table.setGridColor(Color.orange);
	    
	    //table.addRowSelectionInterval(12, 12);
		
		scrollPane_1.setViewportView(table);
		
		 table.addMouseListener(new java.awt.event.MouseAdapter() { // row is clicked
             public void mouseClicked(java.awt.event.MouseEvent evt) {
                 int selectedRowIndex = table.getSelectedRow();
                 int selectedColumnIndex = table.getSelectedColumn();
                 Object selectedObject = (Object) table.getModel().getValueAt(selectedRowIndex, 1);
                 System.out.println(selectedObject);
                 
                 search.setText(selectedObject.toString());
             }

         });
					
	
		TableColumn tm = table.getColumnModel().getColumn(0);
		tm.setPreferredWidth(120);
		
		
		TableColumn tm1 = table.getColumnModel().getColumn(1);
		tm1.setCellRenderer(new ColoredTableCellRenderer());
		tm1.setPreferredWidth(130);
		
		TableColumn tm2 = table.getColumnModel().getColumn(2);
		tm2.setCellRenderer(new ColoredTableCellRenderer());
		tm2.setPreferredWidth(50);
		
		
		TableColumn tm3 = table.getColumnModel().getColumn(3);
		tm3.setCellRenderer(new ColoredTableCellRenderer());
		tm3.setPreferredWidth(50);
		
		
		TableColumn tm4 = table.getColumnModel().getColumn(4);
		tm4.setCellRenderer(new ColoredTableCellRenderer());
		tm4.setPreferredWidth(50);
		
		
		TableColumn tm5 = table.getColumnModel().getColumn(5);
		tm5.setCellRenderer(new ColoredTableCellRenderer());
		tm5.setPreferredWidth(50);
		
		
		TableColumn tm6 = table.getColumnModel().getColumn(6);
		tm6.setCellRenderer(new ColoredTableCellRenderer());
		tm6.setPreferredWidth(50);
		
		
		TableColumn tm7 = table.getColumnModel().getColumn(7);
		tm7.setCellRenderer(new ColoredTableCellRenderer());
		tm7.setPreferredWidth(50);
		
		
		TableColumn tm8 = table.getColumnModel().getColumn(8);
		tm8.setCellRenderer(new ColoredTableCellRenderer());
		tm8.setPreferredWidth(50);
		
		
		TableColumn tm9 = table.getColumnModel().getColumn(9);
		tm9.setCellRenderer(new ColoredTableCellRenderer());
		tm9.setPreferredWidth(50);
		
		
		TableColumn tm10 = table.getColumnModel().getColumn(10);
		tm10.setCellRenderer(new ColoredTableCellRenderer());
		tm10.setPreferredWidth(50);
		
		
		
		
		
		btemporanea1 = new JTextField();
		btemporanea1.setHorizontalAlignment(SwingConstants.CENTER);
		btemporanea1.setForeground(Color.GRAY);
		btemporanea1.setFont(new Font("Arial", Font.PLAIN, 9));
		btemporanea1.setEditable(false);
		btemporanea1.setColumns(10);
		btemporanea1.setBackground(new Color(211, 211, 211));
		btemporanea1.setBounds(10, 48, 119, 19);
		panel.add(btemporanea1);
		
		btemporanea2 = new JTextField();
		btemporanea2.setHorizontalAlignment(SwingConstants.CENTER);
		btemporanea2.setForeground(Color.GRAY);
		btemporanea2.setFont(new Font("Arial", Font.PLAIN, 9));
		btemporanea2.setEditable(false);
		btemporanea2.setColumns(10);
		btemporanea2.setBackground(new Color(211, 211, 211));
		btemporanea2.setBounds(139, 48, 119, 19);
		panel.add(btemporanea2);
		
		btemporanea3 = new JTextField();
		btemporanea3.setHorizontalAlignment(SwingConstants.CENTER);
		btemporanea3.setForeground(Color.GRAY);
		btemporanea3.setFont(new Font("Arial", Font.PLAIN, 9));
		btemporanea3.setEditable(false);
		btemporanea3.setColumns(10);
		btemporanea3.setBackground(new Color(211, 211, 211));
		btemporanea3.setBounds(268, 48, 119, 19);
		panel.add(btemporanea3);
		
		btemporanea4 = new JTextField();
		btemporanea4.setHorizontalAlignment(SwingConstants.CENTER);
		btemporanea4.setForeground(Color.GRAY);
		btemporanea4.setFont(new Font("Arial", Font.PLAIN, 9));
		btemporanea4.setEditable(false);
		btemporanea4.setColumns(10);
		btemporanea4.setBackground(new Color(211, 211, 211));
		btemporanea4.setBounds(397, 48, 119, 19);
		panel.add(btemporanea4);
		
		btemporanea5 = new JTextField();
		btemporanea5.setHorizontalAlignment(SwingConstants.CENTER);
		btemporanea5.setForeground(Color.GRAY);
		btemporanea5.setFont(new Font("Arial", Font.PLAIN, 9));
		btemporanea5.setEditable(false);
		btemporanea5.setColumns(10);
		btemporanea5.setBackground(new Color(211, 211, 211));
		btemporanea5.setBounds(526, 48, 119, 19);
		panel.add(btemporanea5);
		
		btemporanea6 = new JTextField();
		btemporanea6.setHorizontalAlignment(SwingConstants.CENTER);
		btemporanea6.setForeground(Color.GRAY);
		btemporanea6.setFont(new Font("Arial", Font.PLAIN, 9));
		btemporanea6.setEditable(false);
		btemporanea6.setColumns(10);
		btemporanea6.setBackground(new Color(211, 211, 211));
		btemporanea6.setBounds(655, 48, 119, 19);
		panel.add(btemporanea6);
		
		btemporanea7 = new JTextField();
		btemporanea7.setHorizontalAlignment(SwingConstants.CENTER);
		btemporanea7.setForeground(Color.GRAY);
		btemporanea7.setFont(new Font("Arial", Font.PLAIN, 9));
		btemporanea7.setEditable(false);
		btemporanea7.setColumns(10);
		btemporanea7.setBackground(new Color(211, 211, 211));
		btemporanea7.setBounds(784, 48, 119, 19);
		panel.add(btemporanea7);
		
		btemporanea8 = new JTextField();
		btemporanea8.setHorizontalAlignment(SwingConstants.CENTER);
		btemporanea8.setForeground(Color.GRAY);
		btemporanea8.setFont(new Font("Arial", Font.PLAIN, 9));
		btemporanea8.setEditable(false);
		btemporanea8.setColumns(10);
		btemporanea8.setBackground(new Color(211, 211, 211));
		btemporanea8.setBounds(913, 48, 119, 19);
		panel.add(btemporanea8);
		
		btemporanea9 = new JTextField();
		btemporanea9.setHorizontalAlignment(SwingConstants.CENTER);
		btemporanea9.setForeground(Color.GRAY);
		btemporanea9.setFont(new Font("Arial", Font.PLAIN, 9));
		btemporanea9.setEditable(false);
		btemporanea9.setColumns(10);
		btemporanea9.setBackground(new Color(211, 211, 211));
		btemporanea9.setBounds(1041, 48, 119, 19);
		panel.add(btemporanea9);
		
		
		risultato1 = new JTextField();
		risultato1.setHorizontalAlignment(SwingConstants.CENTER);
		risultato1.setForeground(new Color(34, 139, 34));
		risultato1.setFont(new Font("Arial", Font.BOLD, 13));
		risultato1.setEditable(false);
		risultato1.setColumns(10);
		risultato1.setBackground(new Color(211, 211, 211));
		risultato1.setBounds(10, 158, 119, 19);
		panel.add(risultato1);
		
		risultato2 = new JTextField();
		risultato2.setHorizontalAlignment(SwingConstants.CENTER);
		risultato2.setForeground(new Color(34, 139, 34));
		risultato2.setFont(new Font("Arial", Font.BOLD, 13));
		risultato2.setEditable(false);
		risultato2.setColumns(10);
		risultato2.setBackground(new Color(211, 211, 211));
		risultato2.setBounds(139, 157, 119, 19);
		panel.add(risultato2);
		
		risultato3 = new JTextField();
		risultato3.setHorizontalAlignment(SwingConstants.CENTER);
		risultato3.setForeground(new Color(34, 139, 34));
		risultato3.setFont(new Font("Arial", Font.BOLD, 13));
		risultato3.setEditable(false);
		risultato3.setColumns(10);
		risultato3.setBackground(new Color(211, 211, 211));
		risultato3.setBounds(268, 158, 119, 19);
		panel.add(risultato3);
		
		risultato4 = new JTextField();
		risultato4.setHorizontalAlignment(SwingConstants.CENTER);
		risultato4.setForeground(new Color(34, 139, 34));
		risultato4.setFont(new Font("Arial", Font.BOLD, 13));
		risultato4.setEditable(false);
		risultato4.setColumns(10);
		risultato4.setBackground(new Color(211, 211, 211));
		risultato4.setBounds(397, 158, 119, 19);
		panel.add(risultato4);
		
		risultato5 = new JTextField();
		risultato5.setHorizontalAlignment(SwingConstants.CENTER);
		risultato5.setForeground(new Color(34, 139, 34));
		risultato5.setFont(new Font("Arial", Font.BOLD, 13));
		risultato5.setEditable(false);
		risultato5.setColumns(10);
		risultato5.setBackground(new Color(211, 211, 211));
		risultato5.setBounds(526, 158, 119, 19);
		panel.add(risultato5);
		
		risultato6 = new JTextField();
		risultato6.setHorizontalAlignment(SwingConstants.CENTER);
		risultato6.setForeground(new Color(34, 139, 34));
		risultato6.setFont(new Font("Arial", Font.BOLD, 13));
		risultato6.setEditable(false);
		risultato6.setColumns(10);
		risultato6.setBackground(new Color(211, 211, 211));
		risultato6.setBounds(655, 157, 119, 19);
		panel.add(risultato6);
		
		risultato7 = new JTextField();
		risultato7.setHorizontalAlignment(SwingConstants.CENTER);
		risultato7.setForeground(new Color(34, 139, 34));
		risultato7.setFont(new Font("Arial", Font.BOLD, 13));
		risultato7.setEditable(false);
		risultato7.setColumns(10);
		risultato7.setBackground(new Color(211, 211, 211));
		risultato7.setBounds(784, 157, 119, 19);
		panel.add(risultato7);
		
		risultato8 = new JTextField();
		risultato8.setHorizontalAlignment(SwingConstants.CENTER);
		risultato8.setForeground(new Color(34, 139, 34));
		risultato8.setFont(new Font("Arial", Font.BOLD, 13));
		risultato8.setEditable(false);
		risultato8.setColumns(10);
		risultato8.setBackground(new Color(211, 211, 211));
		risultato8.setBounds(913, 157, 119, 19);
		panel.add(risultato8);
		
		risultato9 = new JTextField();
		risultato9.setHorizontalAlignment(SwingConstants.CENTER);
		risultato9.setForeground(new Color(34, 139, 34));
		risultato9.setFont(new Font("Arial", Font.BOLD, 13));
		risultato9.setEditable(false);
		risultato9.setColumns(10);
		risultato9.setBackground(new Color(211, 211, 211));
		risultato9.setBounds(1041, 158, 119, 19);
		panel.add(risultato9);
		
		
		
		
		
		//imposto il monitor dove visualizzare i messaggi
		//indicatore.setMonitor(monitor);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(10, 104, 190, 747);
		frmPlc.getContentPane().add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.inactiveCaptionBorder);
		tabbedPane.addTab("STATO", null, panel_1, null);
		tabbedPane.setEnabledAt(0, true);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("1 - CONTROLLO CORTI 1");
		lblNewLabel_1.setForeground(new Color(0, 128, 128));
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_1.setBounds(10, 11, 165, 14);
		panel_1.add(lblNewLabel_1);
		
		statoLinea1 = new JTextField();
		statoLinea1.setEditable(false);
		statoLinea1.setFont(new Font("Tahoma", Font.BOLD, 11));
		statoLinea1.setForeground(SystemColor.desktop);
		statoLinea1.setBackground(new Color(255, 192, 203));
		statoLinea1.setHorizontalAlignment(SwingConstants.CENTER);
		statoLinea1.setText("STOP");
		statoLinea1.setBounds(10, 29, 165, 20);
		panel_1.add(statoLinea1);
		statoLinea1.setColumns(10);
		
		timestatoLinea1 = new JTextField();
		timestatoLinea1.setHorizontalAlignment(SwingConstants.CENTER);
		timestatoLinea1.setBackground(UIManager.getColor("Button.background"));
		timestatoLinea1.setEditable(false);
		timestatoLinea1.setBounds(10, 51, 165, 20);
		panel_1.add(timestatoLinea1);
		timestatoLinea1.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("2 - PUNTATRICE 1");
		lblNewLabel_1_1.setForeground(new Color(0, 128, 128));
		lblNewLabel_1_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_1_1.setBounds(10, 82, 165, 14);
		panel_1.add(lblNewLabel_1_1);
		
		statoLinea2 = new JTextField();
		statoLinea2.setText("STOP");
		statoLinea2.setHorizontalAlignment(SwingConstants.CENTER);
		statoLinea2.setForeground(SystemColor.desktop);
		statoLinea2.setFont(new Font("Tahoma", Font.BOLD, 11));
		statoLinea2.setEditable(false);
		statoLinea2.setColumns(10);
		statoLinea2.setBackground(new Color(255, 192, 203));
		statoLinea2.setBounds(10, 100, 165, 20);
		panel_1.add(statoLinea2);
		
		timestatoLinea2 = new JTextField();
		timestatoLinea2.setHorizontalAlignment(SwingConstants.CENTER);
		timestatoLinea2.setEditable(false);
		timestatoLinea2.setColumns(10);
		timestatoLinea2.setBackground(SystemColor.menu);
		timestatoLinea2.setBounds(10, 122, 165, 20);
		panel_1.add(timestatoLinea2);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("3 - PUNTATRICE 2");
		lblNewLabel_1_1_1.setForeground(new Color(0, 128, 128));
		lblNewLabel_1_1_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_1_1_1.setBounds(10, 153, 165, 14);
		panel_1.add(lblNewLabel_1_1_1);
		
		statoLinea3 = new JTextField();
		statoLinea3.setText("STOP");
		statoLinea3.setHorizontalAlignment(SwingConstants.CENTER);
		statoLinea3.setForeground(SystemColor.desktop);
		statoLinea3.setFont(new Font("Tahoma", Font.BOLD, 11));
		statoLinea3.setEditable(false);
		statoLinea3.setColumns(10);
		statoLinea3.setBackground(new Color(255, 192, 203));
		statoLinea3.setBounds(10, 171, 165, 20);
		panel_1.add(statoLinea3);
		
		timestatoLinea3 = new JTextField();
		timestatoLinea3.setHorizontalAlignment(SwingConstants.CENTER);
		timestatoLinea3.setEditable(false);
		timestatoLinea3.setColumns(10);
		timestatoLinea3.setBackground(SystemColor.menu);
		timestatoLinea3.setBounds(10, 193, 165, 20);
		panel_1.add(timestatoLinea3);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("4 - CONTROLLO CORTI 2");
		lblNewLabel_1_1_2.setForeground(new Color(0, 128, 128));
		lblNewLabel_1_1_2.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_1_1_2.setBounds(10, 225, 165, 14);
		panel_1.add(lblNewLabel_1_1_2);
		
		statoLinea4 = new JTextField();
		statoLinea4.setText("STOP");
		statoLinea4.setHorizontalAlignment(SwingConstants.CENTER);
		statoLinea4.setForeground(SystemColor.desktop);
		statoLinea4.setFont(new Font("Tahoma", Font.BOLD, 11));
		statoLinea4.setEditable(false);
		statoLinea4.setColumns(10);
		statoLinea4.setBackground(new Color(255, 192, 203));
		statoLinea4.setBounds(10, 243, 165, 20);
		panel_1.add(statoLinea4);
		
		timestatoLinea4 = new JTextField();
		timestatoLinea4.setHorizontalAlignment(SwingConstants.CENTER);
		timestatoLinea4.setEditable(false);
		timestatoLinea4.setColumns(10);
		timestatoLinea4.setBackground(SystemColor.menu);
		timestatoLinea4.setBounds(10, 265, 165, 20);
		panel_1.add(timestatoLinea4);
		
		JLabel lblNewLabel_1_1_3 = new JLabel("5 - PROVA TENUTA 1");
		lblNewLabel_1_1_3.setForeground(new Color(0, 128, 128));
		lblNewLabel_1_1_3.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_1_1_3.setBounds(10, 296, 165, 14);
		panel_1.add(lblNewLabel_1_1_3);
		
		statoLinea5 = new JTextField();
		statoLinea5.setText("STOP");
		statoLinea5.setHorizontalAlignment(SwingConstants.CENTER);
		statoLinea5.setForeground(SystemColor.desktop);
		statoLinea5.setFont(new Font("Tahoma", Font.BOLD, 11));
		statoLinea5.setEditable(false);
		statoLinea5.setColumns(10);
		statoLinea5.setBackground(new Color(255, 192, 203));
		statoLinea5.setBounds(10, 314, 165, 20);
		panel_1.add(statoLinea5);
		
		timestatoLinea5 = new JTextField();
		timestatoLinea5.setHorizontalAlignment(SwingConstants.CENTER);
		timestatoLinea5.setEditable(false);
		timestatoLinea5.setColumns(10);
		timestatoLinea5.setBackground(SystemColor.menu);
		timestatoLinea5.setBounds(10, 336, 165, 20);
		panel_1.add(timestatoLinea5);
		
		JLabel lblNewLabel_1_1_4 = new JLabel("6 - PROVA TENUTA 2");
		lblNewLabel_1_1_4.setForeground(new Color(0, 128, 128));
		lblNewLabel_1_1_4.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_1_1_4.setBounds(10, 367, 165, 14);
		panel_1.add(lblNewLabel_1_1_4);
		
		statoLinea6 = new JTextField();
		statoLinea6.setText("STOP");
		statoLinea6.setHorizontalAlignment(SwingConstants.CENTER);
		statoLinea6.setForeground(SystemColor.desktop);
		statoLinea6.setFont(new Font("Tahoma", Font.BOLD, 11));
		statoLinea6.setEditable(false);
		statoLinea6.setColumns(10);
		statoLinea6.setBackground(new Color(255, 192, 203));
		statoLinea6.setBounds(10, 385, 165, 20);
		panel_1.add(statoLinea6);
		
		timestatoLinea6 = new JTextField();
		timestatoLinea6.setHorizontalAlignment(SwingConstants.CENTER);
		timestatoLinea6.setEditable(false);
		timestatoLinea6.setColumns(10);
		timestatoLinea6.setBackground(SystemColor.menu);
		timestatoLinea6.setBounds(10, 407, 165, 20);
		panel_1.add(timestatoLinea6);
		
		JLabel lblNewLabel_1_1_5 = new JLabel("7 - ALTEZZA POLARI");
		lblNewLabel_1_1_5.setForeground(new Color(0, 128, 128));
		lblNewLabel_1_1_5.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_1_1_5.setBounds(10, 438, 165, 14);
		panel_1.add(lblNewLabel_1_1_5);
		
		statoLinea7 = new JTextField();
		statoLinea7.setText("STOP");
		statoLinea7.setHorizontalAlignment(SwingConstants.CENTER);
		statoLinea7.setForeground(SystemColor.desktop);
		statoLinea7.setFont(new Font("Tahoma", Font.BOLD, 11));
		statoLinea7.setEditable(false);
		statoLinea7.setColumns(10);
		statoLinea7.setBackground(new Color(255, 192, 203));
		statoLinea7.setBounds(10, 456, 165, 20);
		panel_1.add(statoLinea7);
		
		timestatoLinea7 = new JTextField();
		timestatoLinea7.setHorizontalAlignment(SwingConstants.CENTER);
		timestatoLinea7.setEditable(false);
		timestatoLinea7.setColumns(10);
		timestatoLinea7.setBackground(SystemColor.menu);
		timestatoLinea7.setBounds(10, 478, 165, 20);
		panel_1.add(timestatoLinea7);
		
		JLabel lblNewLabel_1_1_6 = new JLabel("8 - BILANCIA 1");
		lblNewLabel_1_1_6.setForeground(new Color(0, 128, 128));
		lblNewLabel_1_1_6.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_1_1_6.setBounds(10, 509, 165, 14);
		panel_1.add(lblNewLabel_1_1_6);
		
		statoLinea8 = new JTextField();
		statoLinea8.setText("STOP");
		statoLinea8.setHorizontalAlignment(SwingConstants.CENTER);
		statoLinea8.setForeground(SystemColor.desktop);
		statoLinea8.setFont(new Font("Tahoma", Font.BOLD, 11));
		statoLinea8.setEditable(false);
		statoLinea8.setColumns(10);
		statoLinea8.setBackground(new Color(255, 192, 203));
		statoLinea8.setBounds(10, 527, 165, 20);
		panel_1.add(statoLinea8);
		
		timestatoLinea8 = new JTextField();
		timestatoLinea8.setHorizontalAlignment(SwingConstants.CENTER);
		timestatoLinea8.setEditable(false);
		timestatoLinea8.setColumns(10);
		timestatoLinea8.setBackground(SystemColor.menu);
		timestatoLinea8.setBounds(10, 549, 165, 20);
		panel_1.add(timestatoLinea8);
		
		JLabel lblNewLabel_1_1_7 = new JLabel("9 - BILANCIA 2");
		lblNewLabel_1_1_7.setForeground(new Color(0, 128, 128));
		lblNewLabel_1_1_7.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_1_1_7.setBounds(10, 580, 165, 14);
		panel_1.add(lblNewLabel_1_1_7);
		
		statoLinea9 = new JTextField();
		statoLinea9.setText("STOP");
		statoLinea9.setHorizontalAlignment(SwingConstants.CENTER);
		statoLinea9.setForeground(SystemColor.desktop);
		statoLinea9.setFont(new Font("Tahoma", Font.BOLD, 11));
		statoLinea9.setEditable(false);
		statoLinea9.setColumns(10);
		statoLinea9.setBackground(new Color(255, 192, 203));
		statoLinea9.setBounds(10, 598, 165, 20);
		panel_1.add(statoLinea9);
		
		timestatoLinea9 = new JTextField();
		timestatoLinea9.setHorizontalAlignment(SwingConstants.CENTER);
		timestatoLinea9.setEditable(false);
		timestatoLinea9.setColumns(10);
		timestatoLinea9.setBackground(SystemColor.menu);
		timestatoLinea9.setBounds(10, 620, 165, 20);
		panel_1.add(timestatoLinea9);
		
		panel_2 = new JPanel();
		panel_2.setBackground(SystemColor.inactiveCaptionBorder);
		tabbedPane.addTab("ABILITAZIONE", null, panel_2, null);
		tabbedPane.setEnabledAt(1, true);
		panel_2.setLayout(null);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(10, 11, 170, 116);
		panel_2.add(panel_4);
		panel_4.setLayout(null);
		
		JToggleButton tglbtnScartoAttivo = new JToggleButton("SCARTO ATTIVO");
		tglbtnScartoAttivo.setBounds(10, 36, 150, 23);
		panel_4.add(tglbtnScartoAttivo);
		
		JLabel lblNewLabel_2 = new JLabel("POSTAZIONE 7");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(10, 11, 133, 14);
		panel_4.add(lblNewLabel_2);
		
		JToggleButton tglbtnLettoreAttivo = new JToggleButton("LETTORE ATTIVO");
		tglbtnLettoreAttivo.setBounds(10, 70, 150, 23);
		panel_4.add(tglbtnLettoreAttivo);
		
		JPanel panel_4_1 = new JPanel();
		panel_4_1.setLayout(null);
		panel_4_1.setBounds(10, 174, 170, 116);
		panel_2.add(panel_4_1);
		
		JToggleButton tglbtnScartoAttivo_1 = new JToggleButton("SCARTO ATTIVO");
		tglbtnScartoAttivo_1.setBounds(10, 36, 150, 23);
		panel_4_1.add(tglbtnScartoAttivo_1);
		
		JLabel lblNewLabel_2_1 = new JLabel("POSTAZIONE 10");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1.setBounds(10, 11, 150, 14);
		panel_4_1.add(lblNewLabel_2_1);
		
		JToggleButton tglbtnLettoreAttivo_1 = new JToggleButton("LETTORE ATTIVO");
		tglbtnLettoreAttivo_1.setBounds(10, 70, 150, 23);
		panel_4_1.add(tglbtnLettoreAttivo_1);
		arrayBatteryStory = new ArrayBatteryStory();
		
		
		
		
		
		JLabel lblP = new JLabel("P1");
		lblP.setHorizontalAlignment(SwingConstants.CENTER);
		lblP.setForeground(new Color(250, 128, 114));
		lblP.setFont(new Font("Arial", Font.BOLD, 12));
		lblP.setBounds(10, 11, 119, 19);
		panel.add(lblP);
		
		JLabel lblP_1 = new JLabel("P2");
		lblP_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblP_1.setForeground(new Color(250, 128, 114));
		lblP_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblP_1.setBounds(139, 11, 119, 19);
		panel.add(lblP_1);
		
		JLabel lblPostazione_1_1 = new JLabel("P3");
		lblPostazione_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione_1_1.setForeground(new Color(250, 128, 114));
		lblPostazione_1_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblPostazione_1_1.setBounds(268, 11, 119, 19);
		panel.add(lblPostazione_1_1);
		
		JLabel lblPostazione_2_1 = new JLabel("P4");
		lblPostazione_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione_2_1.setForeground(new Color(250, 128, 114));
		lblPostazione_2_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblPostazione_2_1.setBounds(397, 11, 119, 19);
		panel.add(lblPostazione_2_1);
		
		JLabel lblPostazione_3_1 = new JLabel("P5");
		lblPostazione_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione_3_1.setForeground(new Color(250, 128, 114));
		lblPostazione_3_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblPostazione_3_1.setBounds(526, 11, 119, 19);
		panel.add(lblPostazione_3_1);
		
		JLabel lblPostazione_4_1 = new JLabel("P6");
		lblPostazione_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione_4_1.setForeground(new Color(250, 128, 114));
		lblPostazione_4_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblPostazione_4_1.setBounds(655, 11, 119, 19);
		panel.add(lblPostazione_4_1);
		
		JLabel lblPostazione_5_1 = new JLabel("P7");
		lblPostazione_5_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione_5_1.setForeground(new Color(250, 128, 114));
		lblPostazione_5_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblPostazione_5_1.setBounds(784, 11, 119, 19);
		panel.add(lblPostazione_5_1);
		
		JLabel lblPostazione_6_1 = new JLabel("P8");
		lblPostazione_6_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPostazione_6_1.setForeground(new Color(250, 128, 114));
		lblPostazione_6_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblPostazione_6_1.setBounds(913, 11, 119, 19);
		panel.add(lblPostazione_6_1);
		
		JLabel lblNewLabel_7_1_2 = new JLabel("P9");
		lblNewLabel_7_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_1_2.setForeground(new Color(250, 128, 114));
		lblNewLabel_7_1_2.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_7_1_2.setBounds(1041, 11, 119, 19);
		panel.add(lblNewLabel_7_1_2);
		
		conteggiorip1 = new JTextField();
		conteggiorip1.setHorizontalAlignment(SwingConstants.CENTER);
		conteggiorip1.setForeground(Color.DARK_GRAY);
		conteggiorip1.setFont(new Font("Arial", Font.BOLD, 12));
		conteggiorip1.setEditable(false);
		conteggiorip1.setColumns(10);
		conteggiorip1.setBackground(new Color(255, 245, 238));
		conteggiorip1.setBounds(10, 301, 119, 18);
		panel.add(conteggiorip1);
		
		conteggiorip2 = new JTextField();
		conteggiorip2.setHorizontalAlignment(SwingConstants.CENTER);
		conteggiorip2.setForeground(Color.DARK_GRAY);
		conteggiorip2.setFont(new Font("Arial", Font.BOLD, 12));
		conteggiorip2.setEditable(false);
		conteggiorip2.setColumns(10);
		conteggiorip2.setBackground(new Color(255, 245, 238));
		conteggiorip2.setBounds(139, 301, 119, 18);
		panel.add(conteggiorip2);
		
		conteggiorip3 = new JTextField();
		conteggiorip3.setHorizontalAlignment(SwingConstants.CENTER);
		conteggiorip3.setForeground(Color.DARK_GRAY);
		conteggiorip3.setFont(new Font("Arial", Font.BOLD, 12));
		conteggiorip3.setEditable(false);
		conteggiorip3.setColumns(10);
		conteggiorip3.setBackground(new Color(255, 245, 238));
		conteggiorip3.setBounds(268, 301, 119, 18);
		panel.add(conteggiorip3);
		
		conteggiorip4 = new JTextField();
		conteggiorip4.setHorizontalAlignment(SwingConstants.CENTER);
		conteggiorip4.setForeground(Color.DARK_GRAY);
		conteggiorip4.setFont(new Font("Arial", Font.BOLD, 12));
		conteggiorip4.setEditable(false);
		conteggiorip4.setColumns(10);
		conteggiorip4.setBackground(new Color(255, 245, 238));
		conteggiorip4.setBounds(397, 301, 119, 18);
		panel.add(conteggiorip4);
		
		conteggiorip5 = new JTextField();
		conteggiorip5.setHorizontalAlignment(SwingConstants.CENTER);
		conteggiorip5.setForeground(Color.DARK_GRAY);
		conteggiorip5.setFont(new Font("Arial", Font.BOLD, 12));
		conteggiorip5.setEditable(false);
		conteggiorip5.setColumns(10);
		conteggiorip5.setBackground(new Color(255, 245, 238));
		conteggiorip5.setBounds(526, 301, 119, 18);
		panel.add(conteggiorip5);
		
		conteggiorip6 = new JTextField();
		conteggiorip6.setHorizontalAlignment(SwingConstants.CENTER);
		conteggiorip6.setForeground(Color.DARK_GRAY);
		conteggiorip6.setFont(new Font("Arial", Font.BOLD, 12));
		conteggiorip6.setEditable(false);
		conteggiorip6.setColumns(10);
		conteggiorip6.setBackground(new Color(255, 245, 238));
		conteggiorip6.setBounds(655, 301, 119, 18);
		panel.add(conteggiorip6);
		
		conteggiorip7 = new JTextField();
		
		conteggiorip7.setHorizontalAlignment(SwingConstants.CENTER);
		conteggiorip7.setForeground(Color.DARK_GRAY);
		conteggiorip7.setFont(new Font("Arial", Font.BOLD, 12));
		conteggiorip7.setEditable(false);
		conteggiorip7.setColumns(10);
		conteggiorip7.setBackground(new Color(255, 245, 238));
		conteggiorip7.setBounds(784, 301, 119, 18);
		panel.add(conteggiorip7);
		
		conteggiorip8 = new JTextField();
		conteggiorip8.setHorizontalAlignment(SwingConstants.CENTER);
		conteggiorip8.setForeground(Color.DARK_GRAY);
		conteggiorip8.setFont(new Font("Arial", Font.BOLD, 12));
		conteggiorip8.setEditable(false);
		conteggiorip8.setColumns(10);
		conteggiorip8.setBackground(new Color(255, 245, 238));
		conteggiorip8.setBounds(913, 301, 119, 18);
		panel.add(conteggiorip8);
		
		conteggiorip9 = new JTextField();
		conteggiorip9.setHorizontalAlignment(SwingConstants.CENTER);
		conteggiorip9.setForeground(Color.DARK_GRAY);
		conteggiorip9.setFont(new Font("Arial", Font.BOLD, 12));
		conteggiorip9.setEditable(false);
		conteggiorip9.setColumns(10);
		conteggiorip9.setBackground(new Color(255, 245, 238));
		conteggiorip9.setBounds(1041, 301, 119, 18);
		panel.add(conteggiorip9);
		
		scarto1 = new JTextField();
		scarto1.setHorizontalAlignment(SwingConstants.CENTER);
		scarto1.setForeground(Color.DARK_GRAY);
		scarto1.setFont(new Font("Arial", Font.BOLD, 12));
		scarto1.setEditable(false);
		scarto1.setColumns(10);
		scarto1.setBackground(new Color(255, 192, 203));
		scarto1.setBounds(10, 341, 119, 19);
		panel.add(scarto1);
		
		scarto2 = new JTextField();
		scarto2.setHorizontalAlignment(SwingConstants.CENTER);
		scarto2.setForeground(Color.DARK_GRAY);
		scarto2.setFont(new Font("Arial", Font.BOLD, 12));
		scarto2.setEditable(false);
		scarto2.setColumns(10);
		scarto2.setBackground(new Color(255, 192, 203));
		scarto2.setBounds(139, 341, 119, 19);
		panel.add(scarto2);
		
		scarto3 = new JTextField();
		scarto3.setHorizontalAlignment(SwingConstants.CENTER);
		scarto3.setForeground(Color.DARK_GRAY);
		scarto3.setFont(new Font("Arial", Font.BOLD, 12));
		scarto3.setEditable(false);
		scarto3.setColumns(10);
		scarto3.setBackground(new Color(255, 192, 203));
		scarto3.setBounds(268, 341, 119, 19);
		panel.add(scarto3);
		
		scarto4 = new JTextField();
		scarto4.setHorizontalAlignment(SwingConstants.CENTER);
		scarto4.setForeground(Color.DARK_GRAY);
		scarto4.setFont(new Font("Arial", Font.BOLD, 12));
		scarto4.setEditable(false);
		scarto4.setColumns(10);
		scarto4.setBackground(new Color(255, 192, 203));
		scarto4.setBounds(397, 341, 119, 19);
		panel.add(scarto4);
		
		scarto5 = new JTextField();
		scarto5.setHorizontalAlignment(SwingConstants.CENTER);
		scarto5.setForeground(Color.DARK_GRAY);
		scarto5.setFont(new Font("Arial", Font.BOLD, 12));
		scarto5.setEditable(false);
		scarto5.setColumns(10);
		scarto5.setBackground(new Color(255, 192, 203));
		scarto5.setBounds(526, 341, 119, 19);
		panel.add(scarto5);
		
		scarto6 = new JTextField();
		scarto6.setHorizontalAlignment(SwingConstants.CENTER);
		scarto6.setForeground(Color.DARK_GRAY);
		scarto6.setFont(new Font("Arial", Font.BOLD, 12));
		scarto6.setEditable(false);
		scarto6.setColumns(10);
		scarto6.setBackground(new Color(255, 192, 203));
		scarto6.setBounds(655, 341, 119, 19);
		panel.add(scarto6);
		
		scarto7 = new JTextField();
		scarto7.setHorizontalAlignment(SwingConstants.CENTER);
		scarto7.setForeground(Color.DARK_GRAY);
		scarto7.setFont(new Font("Arial", Font.BOLD, 12));
		scarto7.setEditable(false);
		scarto7.setColumns(10);
		scarto7.setBackground(new Color(255, 192, 203));
		scarto7.setBounds(784, 341, 119, 19);
		panel.add(scarto7);
		
		scarto8 = new JTextField();
		scarto8.setHorizontalAlignment(SwingConstants.CENTER);
		scarto8.setForeground(Color.DARK_GRAY);
		scarto8.setFont(new Font("Arial", Font.BOLD, 12));
		scarto8.setEditable(false);
		scarto8.setColumns(10);
		scarto8.setBackground(new Color(255, 192, 203));
		scarto8.setBounds(913, 341, 119, 19);
		panel.add(scarto8);
		
		scarto9 = new JTextField();
		scarto9.setHorizontalAlignment(SwingConstants.CENTER);
		scarto9.setForeground(Color.DARK_GRAY);
		scarto9.setFont(new Font("Arial", Font.BOLD, 12));
		scarto9.setEditable(false);
		scarto9.setColumns(10);
		scarto9.setBackground(new Color(255, 192, 203));
		scarto9.setBounds(1041, 341, 119, 19);
		panel.add(scarto9);
		
		btemporanea10 = new JTextField();
		btemporanea10.setHorizontalAlignment(SwingConstants.CENTER);
		btemporanea10.setForeground(Color.GRAY);
		btemporanea10.setFont(new Font("Arial", Font.PLAIN, 9));
		btemporanea10.setEditable(false);
		btemporanea10.setColumns(10);
		btemporanea10.setBackground(new Color(211, 211, 211));
		btemporanea10.setBounds(1170, 48, 143, 19);
		panel.add(btemporanea10);
		
		JLabel lblBatteriaInTransito_7_1_1 = new JLabel("BATTERIA TESTATA");
		lblBatteriaInTransito_7_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblBatteriaInTransito_7_1_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblBatteriaInTransito_7_1_1.setBounds(1170, 78, 143, 14);
		panel.add(lblBatteriaInTransito_7_1_1);
		
		batteria10 = new JTextField();
		batteria10.setHorizontalAlignment(SwingConstants.CENTER);
		batteria10.setForeground(Color.DARK_GRAY);
		batteria10.setFont(new Font("Arial", Font.BOLD, 9));
		batteria10.setEditable(false);
		batteria10.setColumns(10);
		batteria10.setBackground(Color.WHITE);
		batteria10.setBounds(1170, 92, 143, 24);
		panel.add(batteria10);
		
		JLabel lblStatoTest_7_1_1 = new JLabel("RESPONSO");
		lblStatoTest_7_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatoTest_7_1_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblStatoTest_7_1_1.setBounds(1170, 118, 143, 14);
		panel.add(lblStatoTest_7_1_1);
		
		stato10 = new JTextField();
		stato10.setHorizontalAlignment(SwingConstants.CENTER);
		stato10.setForeground(new Color(0, 128, 0));
		stato10.setFont(new Font("Arial", Font.BOLD, 10));
		stato10.setEditable(false);
		stato10.setColumns(10);
		stato10.setBackground(Color.WHITE);
		stato10.setBounds(1306, 203, 14, 16);
		stato10.setVisible(false);
		panel.add(stato10);
		
		risultato10 = new JTextField();
		risultato10.setEditable(false);
		risultato10.setHorizontalAlignment(SwingConstants.CENTER);
		risultato10.setForeground(new Color(34, 139, 34));
		risultato10.setFont(new Font("Arial", Font.BOLD, 10));
		risultato10.setColumns(10);
		risultato10.setBackground(SystemColor.inactiveCaptionBorder);
		risultato10.setBounds(1170, 134, 143, 43);
		panel.add(risultato10);
		
		JLabel lblUltimaLettura_7_1_1 = new JLabel("ULTIMA LETTURA");
		lblUltimaLettura_7_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblUltimaLettura_7_1_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblUltimaLettura_7_1_1.setBounds(1170, 187, 143, 14);
		panel.add(lblUltimaLettura_7_1_1);
		
		tempo10 = new JTextField();
		tempo10.setHorizontalAlignment(SwingConstants.CENTER);
		tempo10.setForeground(Color.GRAY);
		tempo10.setFont(new Font("Arial", Font.ITALIC, 10));
		tempo10.setEditable(false);
		tempo10.setColumns(10);
		tempo10.setBackground(Color.WHITE);
		tempo10.setBounds(1170, 203, 143, 24);
		panel.add(tempo10);
		
		JLabel lblConteggio_2_1_4_1 = new JLabel("CONTEGGIO");
		lblConteggio_2_1_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblConteggio_2_1_4_1.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblConteggio_2_1_4_1.setBounds(1170, 238, 143, 14);
		panel.add(lblConteggio_2_1_4_1);
		
		conteggio10 = new JTextField();
		conteggio10.setHorizontalAlignment(SwingConstants.CENTER);
		conteggio10.setForeground(Color.DARK_GRAY);
		conteggio10.setFont(new Font("Arial", Font.BOLD, 12));
		conteggio10.setEditable(false);
		conteggio10.setColumns(10);
		conteggio10.setBackground(Color.WHITE);
		conteggio10.setBounds(1170, 254, 143, 43);
		panel.add(conteggio10);
		
		conteggiorip10 = new JTextField();
		conteggiorip10.setVisible(false);
		conteggiorip10.setHorizontalAlignment(SwingConstants.CENTER);
		conteggiorip10.setForeground(Color.DARK_GRAY);
		conteggiorip10.setFont(new Font("Arial", Font.BOLD, 12));
		conteggiorip10.setEditable(false);
		conteggiorip10.setColumns(10);
		conteggiorip10.setBackground(new Color(255, 204, 153));
		conteggiorip10.setBounds(1288, 301, 25, 7);
		panel.add(conteggiorip10);
		
		JLabel lblScartateko_8_1 = new JLabel("SCARTATE");
		lblScartateko_8_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblScartateko_8_1.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblScartateko_8_1.setBounds(1170, 298, 143, 14);
		panel.add(lblScartateko_8_1);
		
		scarto10 = new JTextField();
		scarto10.setHorizontalAlignment(SwingConstants.CENTER);
		scarto10.setForeground(Color.DARK_GRAY);
		scarto10.setFont(new Font("Arial", Font.BOLD, 12));
		scarto10.setEditable(false);
		scarto10.setColumns(10);
		scarto10.setBackground(new Color(255, 192, 203));
		scarto10.setBounds(1170, 317, 143, 43);
		panel.add(scarto10);
		
		
		try {
			wPic = ImageIO.read(this.getClass().getResource("/resource/evidon1.png"));
			banner = new JLabel(new ImageIcon(wPic));
			banner.setHorizontalAlignment(SwingConstants.LEFT);
			banner.setBounds(0, -11, 407, 94);
			frmPlc.getContentPane().add(banner);
			
			search = new JTextField();
			search.setFont(new Font("Arial", Font.BOLD, 14));
			search.setForeground(new Color(165, 42, 42));
			
			
			search.setBounds(1456, 701, 175, 23);
			frmPlc.getContentPane().add(search);
			search.setColumns(10);
			
			JButton btnNewButton = new JButton("");
			btnNewButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if(!search.getText().equals("")) {
			               // remove selected row from the model
			               //model.removeRow(table.getSelectedRow());
						   //String data = table.getValueAt(table.getSelectedRow(), 1).toString();
						   
						   //JOptionPane.showMessageDialog(null, "Selezionata: " + batteria_selezionata.getText());
			               WinStory frame = new WinStory(search.getText());
						   frame.setVisible(true);
			               
			               
			            }else {
			            	JOptionPane.showMessageDialog(null, "Inserisci un codice da cercare");
			            }
				}
			});
			btnNewButton.setIcon(new ImageIcon(main.class.getResource("/resource/search1.png")));
			btnNewButton.setBounds(1635, 701, 56, 23);
			frmPlc.getContentPane().add(btnNewButton);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
	
		JLabel lblNewLabel_1_1_7_1 = new JLabel("10 - FINE LINEA");
		lblNewLabel_1_1_7_1.setForeground(new Color(0, 128, 128));
		lblNewLabel_1_1_7_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_1_1_7_1.setBounds(10, 648, 165, 14);
		panel_1.add(lblNewLabel_1_1_7_1);
		
		statoLinea10 = new JTextField();
		statoLinea10.setText("STOP");
		statoLinea10.setHorizontalAlignment(SwingConstants.CENTER);
		statoLinea10.setForeground(Color.BLACK);
		statoLinea10.setFont(new Font("Tahoma", Font.BOLD, 11));
		statoLinea10.setEditable(false);
		statoLinea10.setColumns(10);
		statoLinea10.setBackground(new Color(255, 192, 203));
		statoLinea10.setBounds(10, 666, 165, 20);
		panel_1.add(statoLinea10);
		
		timestatoLinea10 = new JTextField();
		timestatoLinea10.setHorizontalAlignment(SwingConstants.CENTER);
		timestatoLinea10.setEditable(false);
		timestatoLinea10.setColumns(10);
		timestatoLinea10.setBackground(SystemColor.menu);
		timestatoLinea10.setBounds(10, 688, 165, 20);
		panel_1.add(timestatoLinea10);
		
		
		txtTipoBatteria = new JTextField();
		txtTipoBatteria.setBorder(BorderFactory.createEmptyBorder());
		txtTipoBatteria.setBackground(SystemColor.inactiveCaptionBorder);
		txtTipoBatteria.setHorizontalAlignment(SwingConstants.CENTER);
		txtTipoBatteria.setForeground(Color.RED);
		txtTipoBatteria.setFont(new Font("Arial", Font.BOLD, 48));
		txtTipoBatteria.setEditable(false);
		txtTipoBatteria.setBounds(405, 0, 1286, 84);
		frmPlc.getContentPane().add(txtTipoBatteria);
		txtTipoBatteria.setColumns(10);
		setting.txtTipologiaBatteria = txtTipoBatteria;
		
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat ier = new SimpleDateFormat("yyyy-MM-dd");
		String dax = ier.format(date);
		LocalDate da  = LocalDate.parse(dax);
		LocalDate ieri = da.minusDays(1); 
		dax = ieri+" 22:00:00";
		
		JLabel lblRiprocessate = new JLabel("RIPROCESSATE");
		lblRiprocessate.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblRiprocessate.setBounds(10, 286, 119, 14);
		panel.add(lblRiprocessate);
		
		JLabel lblScartateko = new JLabel("SCARTATE/KO");
		lblScartateko.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblScartateko.setBounds(10, 327, 119, 14);
		panel.add(lblScartateko);
		
		JLabel lblRiprocessate_1 = new JLabel("RIPROCESSATE");
		lblRiprocessate_1.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblRiprocessate_1.setBounds(139, 286, 119, 14);
		panel.add(lblRiprocessate_1);
		
		JLabel lblRiprocessate_2 = new JLabel("RIPROCESSATE");
		lblRiprocessate_2.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblRiprocessate_2.setBounds(268, 286, 119, 14);
		panel.add(lblRiprocessate_2);
		
		JLabel lblRiprocessate_3 = new JLabel("RIPROCESSATE");
		lblRiprocessate_3.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblRiprocessate_3.setBounds(397, 286, 119, 14);
		panel.add(lblRiprocessate_3);
		
		JLabel lblRiprocessate_4 = new JLabel("RIPROCESSATE");
		lblRiprocessate_4.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblRiprocessate_4.setBounds(526, 286, 119, 14);
		panel.add(lblRiprocessate_4);
		
		JLabel lblRiprocessate_5 = new JLabel("RIPROCESSATE");
		lblRiprocessate_5.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblRiprocessate_5.setBounds(655, 286, 119, 14);
		panel.add(lblRiprocessate_5);
		
		JLabel lblRiprocessate_6 = new JLabel("RIPROCESSATE");
		lblRiprocessate_6.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblRiprocessate_6.setBounds(784, 286, 119, 14);
		panel.add(lblRiprocessate_6);
		
		JLabel lblRiprocessate_7 = new JLabel("RIPROCESSATE");
		lblRiprocessate_7.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblRiprocessate_7.setBounds(913, 286, 119, 14);
		panel.add(lblRiprocessate_7);
		
		JLabel lblRiprocessate_8 = new JLabel("RIPROCESSATE");
		lblRiprocessate_8.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblRiprocessate_8.setBounds(1041, 286, 119, 14);
		panel.add(lblRiprocessate_8);
		
		JLabel lblScartateko_1 = new JLabel("SCARTATE/KO");
		lblScartateko_1.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblScartateko_1.setBounds(139, 327, 119, 14);
		panel.add(lblScartateko_1);
		
		JLabel lblScartateko_2 = new JLabel("SCARTATE/KO");
		lblScartateko_2.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblScartateko_2.setBounds(268, 327, 119, 14);
		panel.add(lblScartateko_2);
		
		JLabel lblScartateko_3 = new JLabel("SCARTATE/KO");
		lblScartateko_3.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblScartateko_3.setBounds(397, 327, 119, 14);
		panel.add(lblScartateko_3);
		
		JLabel lblScartateko_4 = new JLabel("SCARTATE/KO");
		lblScartateko_4.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblScartateko_4.setBounds(526, 327, 119, 14);
		panel.add(lblScartateko_4);
		
		JLabel lblScartateko_5 = new JLabel("SCARTATE/KO");
		lblScartateko_5.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblScartateko_5.setBounds(655, 327, 119, 14);
		panel.add(lblScartateko_5);
		
		JLabel lblScartateko_6 = new JLabel("SCARTATE/KO");
		lblScartateko_6.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblScartateko_6.setBounds(784, 327, 119, 14);
		panel.add(lblScartateko_6);
		
		JLabel lblScartateko_7 = new JLabel("SCARTATE/KO");
		lblScartateko_7.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblScartateko_7.setBounds(913, 327, 119, 14);
		panel.add(lblScartateko_7);
		
		JLabel lblScartateko_8 = new JLabel("SCARTATE/KO");
		lblScartateko_8.setFont(new Font("Tahoma", Font.BOLD, 9));
		lblScartateko_8.setBounds(1041, 327, 119, 14);
		panel.add(lblScartateko_8);
		
		JLabel lblNewLabel_7_1_2_1 = new JLabel("CONTROLLO");
		lblNewLabel_7_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_1_2_1.setForeground(new Color(250, 128, 114));
		lblNewLabel_7_1_2_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel_7_1_2_1.setBounds(1170, 11, 143, 19);
		panel.add(lblNewLabel_7_1_2_1);
		
		JLabel lblNewLabel_7_1_1 = new JLabel("FINE LINEA");
		lblNewLabel_7_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_1_1.setForeground(new Color(0, 139, 139));
		lblNewLabel_7_1_1.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_7_1_1.setBounds(1170, 21, 143, 26);
		panel.add(lblNewLabel_7_1_1);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(SystemColor.inactiveCaptionBorder);
		panel_3.setBounds(210, 83, 152, 384);
		frmPlc.getContentPane().add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblNewLabel_3 = new JLabel("TIPO BATTERIA");
		lblNewLabel_3.setBounds(10, 11, 124, 26);
		panel_3.add(lblNewLabel_3);
		lblNewLabel_3.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		
		numeroBatterieBuone = new JTextField();
		numeroBatterieBuone.setBounds(10, 153, 124, 56);
		panel_3.add(numeroBatterieBuone);
		numeroBatterieBuone.setEditable(false);
		numeroBatterieBuone.setBackground(Color.WHITE);
		numeroBatterieBuone.setHorizontalAlignment(SwingConstants.CENTER);
		numeroBatterieBuone.setFont(new Font("Arial", Font.BOLD, 24));
		numeroBatterieBuone.setForeground(new Color(0, 204, 0));
		numeroBatterieBuone.setText("-- %");
		numeroBatterieBuone.setColumns(10);
		
		numeroBatterieScarto = new JTextField();
		numeroBatterieScarto.setBounds(10, 245, 124, 56);
		panel_3.add(numeroBatterieScarto);
		numeroBatterieScarto.setText("-- %");
		numeroBatterieScarto.setHorizontalAlignment(SwingConstants.CENTER);
		numeroBatterieScarto.setForeground(new Color(250, 128, 114));
		numeroBatterieScarto.setFont(new Font("Arial", Font.BOLD, 24));
		numeroBatterieScarto.setEditable(false);
		numeroBatterieScarto.setColumns(10);
		numeroBatterieScarto.setBackground(Color.WHITE);
		
		JLabel lblNewLabel_3_1 = new JLabel("SCARTO");
		lblNewLabel_3_1.setBounds(10, 218, 124, 26);
		panel_3.add(lblNewLabel_3_1);
		lblNewLabel_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3_1.setFont(new Font("Arial", Font.BOLD, 14));
		
		turno = new JTextField();
		turno.setBounds(10, 312, 124, 18);
		panel_3.add(turno);
		turno.setHorizontalAlignment(SwingConstants.CENTER);
		turno.setForeground(Color.DARK_GRAY);
		turno.setFont(new Font("Arial", Font.BOLD, 12));
		turno.setEditable(false);
		turno.setColumns(10);
		turno.setBackground(Color.WHITE);
		
		inizio_conteggio = new JTextField();
		inizio_conteggio.setBounds(10, 334, 124, 18);
		panel_3.add(inizio_conteggio);
		inizio_conteggio.setHorizontalAlignment(SwingConstants.CENTER);
		inizio_conteggio.setForeground(Color.DARK_GRAY);
		inizio_conteggio.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
		inizio_conteggio.setEditable(false);
		inizio_conteggio.setColumns(10);
		inizio_conteggio.setBackground(Color.LIGHT_GRAY);
		
		inizio_conteggio.setText(dax);
		
		JLabel lblNewLabel_3_2 = new JLabel("BATTERIE OK");
		lblNewLabel_3_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3_2.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel_3_2.setBounds(10, 132, 124, 18);
		panel_3.add(lblNewLabel_3_2);
		
		tipo_batteria = new JTextField();
		tipo_batteria.setText("---");
		tipo_batteria.setHorizontalAlignment(SwingConstants.CENTER);
		tipo_batteria.setForeground(Color.BLACK);
		tipo_batteria.setFont(new Font("Arial", Font.BOLD, 24));
		tipo_batteria.setEditable(false);
		tipo_batteria.setColumns(10);
		tipo_batteria.setBackground(Color.WHITE);
		tipo_batteria.setBounds(10, 37, 124, 56);
		panel_3.add(tipo_batteria);
		
		
		//setting.se
		
		o_data = new JTextField();
		o_data.setEditable(false);
		o_data.setHorizontalAlignment(SwingConstants.CENTER);
		o_data.setText("Mer 25/11/2020");
		o_data.setFont(new Font("Arial", Font.ITALIC, 14));
		o_data.setBackground(SystemColor.inactiveCaptionBorder);
		o_data.setBounds(1701, 0, 200, 31);
		frmPlc.getContentPane().add(o_data);
		o_data.setColumns(10);
		
		o_ora = new JTextField();
		o_ora.setEditable(false);
		o_ora.setFont(new Font("Arial", Font.BOLD, 26));
		o_ora.setHorizontalAlignment(SwingConstants.CENTER);
		o_ora.setText("--:--");
		o_ora.setBackground(SystemColor.inactiveCaptionBorder);
		o_ora.setBounds(1701, 28, 200, 52);
		frmPlc.getContentPane().add(o_ora);
		o_ora.setColumns(10);
		
		time_avvio = new JTextField();
		time_avvio.setEditable(false);
		time_avvio.setForeground(Color.BLACK);
		time_avvio.setFont(new Font("Tahoma", Font.ITALIC, 11));
		time_avvio.setBounds(10, 955, 190, 20);
		frmPlc.getContentPane().add(time_avvio);
		time_avvio.setColumns(10);
		
		Date date1 = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		time_avvio.setText("Avvio: " + formatter.format(date1));
		
		try {
			confl = new ConfiguratoreLinea();
		} catch (Exception e1) {
			log.write("\nErrore main, ConfiguratoreLinea\n");
			e1.printStackTrace();
		}
		
		
		elenco_indicatori.setIndicatore(0, tempo1, stato1, batteria1, statoplc, statodb, conteggio1,btemporanea1,risultato1,statoLinea1,timestatoLinea1,conteggiorip1,scarto1);  //indicatori postazione 1	
		elenco_indicatori.setIndicatore(1, tempo2, stato2, batteria2, statoplc, statodb, conteggio2,btemporanea2,risultato2,statoLinea2,timestatoLinea2,conteggiorip2,scarto2);  //indicatori postazione 2
		elenco_indicatori.setIndicatore(2, tempo3, stato3, batteria3, statoplc, statodb, conteggio3,btemporanea3,risultato3,statoLinea3,timestatoLinea3,conteggiorip3,scarto3);  //indicatori postazione 3
		elenco_indicatori.setIndicatore(3, tempo4, stato4, batteria4, statoplc, statodb, conteggio4,btemporanea4,risultato4,statoLinea4,timestatoLinea4,conteggiorip4,scarto4);  //indicatori postazione 4
		elenco_indicatori.setIndicatore(4, tempo5, stato5, batteria5, statoplc, statodb, conteggio5,btemporanea5,risultato5,statoLinea5,timestatoLinea5,conteggiorip5,scarto5);  //indicatori postazione 5
		elenco_indicatori.setIndicatore(5, tempo6, stato6, batteria6, statoplc, statodb, conteggio6,btemporanea6,risultato6,statoLinea6,timestatoLinea6,conteggiorip6,scarto6);  //indicatori postazione 6
		elenco_indicatori.setIndicatore(6, tempo7, stato7, batteria7, statoplc, statodb, conteggio7,btemporanea7,risultato7,statoLinea7,timestatoLinea7,conteggiorip7,scarto7);  //indicatori postazione 7
		elenco_indicatori.setIndicatore(7, tempo8, stato8, batteria8, statoplc, statodb, conteggio8,btemporanea8,risultato8,statoLinea8,timestatoLinea8,conteggiorip8,scarto8);  //indicatori postazione 8
		elenco_indicatori.setIndicatore(8, tempo9, stato9, batteria9, statoplc, statodb, conteggio9,btemporanea9,risultato9,statoLinea9,timestatoLinea9,conteggiorip9,scarto9);  //indicatori postazione 9
		elenco_indicatori.setIndicatore(Setting.STAZIONE_DI_CONTROLLO_2, tempo10, stato10, batteria10, statoplc, statodb, conteggio10,btemporanea10,risultato10,statoLinea10,timestatoLinea10,conteggiorip10,scarto10);
						
		areaErrore = new JTextField();
		areaErrore.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
				   String data = setting.getCodiceBatteriaScartata().getText();
				   //JOptionPane.showMessageDialog(null, "Selezionata: " + data);
	               WinStory frame = new WinStory(data);
				   frame.setVisible(true);
			}
		});
		areaErrore.setBounds(212, 478, 1479, 212);
		frmPlc.getContentPane().add(areaErrore);
		areaErrore.setEditable(false);
		areaErrore.setBackground(new Color(192, 192, 192));
		areaErrore.setHorizontalAlignment(SwingConstants.CENTER);
		areaErrore.setText(Setting.ATTESA_BATTERIA);
		areaErrore.setForeground(new Color(0, 0, 0));
		areaErrore.setFont(new Font("Arial", Font.PLAIN, 44));
		areaErrore.setColumns(10);
		
		try {
			frame = new WinConfigurazioneLinea();
			frame.setVisible(false);
			
			JButton btn_dettaglio = new JButton("");
			btn_dettaglio.setBounds(1317, 341, 13, 20);
			panel.add(btn_dettaglio);
			btn_dettaglio.setVisible(false);
			btn_dettaglio.setEnabled(false);
			btn_dettaglio.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			btn_dettaglio.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					   String data = setting.getCodiceBatteriaScartata().getText();
					   //JOptionPane.showMessageDialog(null, "Selezionata: " + data);
	               WinStory frame = new WinStory(data);
					   frame.setVisible(true);
				}
			});
			
			setting.setCodiceBatteriaScartata(btn_dettaglio);
			
		}catch(Exception j) {
			log.write("Main errore - caricaDatiConfigurazionePostazioneInterblocco  " + j.toString());
			btn_abilitazione_10.setSelected(false);
			btn_abilitazione_10.setEnabled(false);
			btn_abilitazione_10.setBackground(Setting.grigio);
			btn_abilitazione_10.setText("PULSANTE INATTIVO");
		}
		
		//setting.getCodiceBatteriaScartata().setBackground(Setting.grigio);
		
		setting.setTipologiaBatterie(tipo_batteria);
		
		setting.setLabelBatterieBuone(numeroBatterieBuone);
		setting.setLabelBatterieScartate(numeroBatterieScarto);
		
		lblConfiguratorePlc = new JLabel("CONFIGURATORE PLC");
		
		lblConfiguratorePlc.setHorizontalAlignment(SwingConstants.LEFT);
		lblConfiguratorePlc.setBounds(10, 907, 143, 14);
		frmPlc.getContentPane().add(lblConfiguratorePlc);
		
		stato_configuratore_plc = new JTextField();
		stato_configuratore_plc.setEditable(false);
		stato_configuratore_plc.setColumns(10);
		stato_configuratore_plc.setBounds(10, 921, 183, 20);
		
		//
		
		frmPlc.getContentPane().add(stato_configuratore_plc);
		
		
		JButton btnCambioCodice = new JButton("CAMBIO CODICE");
		btnCambioCodice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				WinSetGreencode frame = new WinSetGreencode();
			}
		});
		btnCambioCodice.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnCambioCodice.setBackground(SystemColor.activeCaption);
		btnCambioCodice.setBounds(1701, 130, 200, 84);
		btnCambioCodice.setIcon(new ImageIcon(icona_pulsante_cambio_codice));
		frmPlc.getContentPane().add(btnCambioCodice);
		
		JButton btnImpostazioniPlc = new JButton("VIEWER        ");
		btnImpostazioniPlc.setIcon(new ImageIcon(icona_pulsante_viewer));
		btnImpostazioniPlc.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Scarti viewer = new Scarti("INIT");
			}
		});
		btnImpostazioniPlc.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnImpostazioniPlc.setBackground(SystemColor.activeCaption);
		btnImpostazioniPlc.setBounds(1701, 325, 200, 84);
		frmPlc.getContentPane().add(btnImpostazioniPlc);	
		
				
		JButton btnViewer = new JButton("SETUP LETTORI");
		btnViewer.setIcon(new ImageIcon(icona_pulsante_lettori));
		btnViewer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				WinConfigurazioneLinea frame = new WinConfigurazioneLinea(monitor);
				frame.setVisible(true);
			}
		});
		btnViewer.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnViewer.setBackground(SystemColor.activeCaption);
		btnViewer.setBounds(1701, 225, 200, 84);
		frmPlc.getContentPane().add(btnViewer);	
		
		
			
			JButton btnNewButton_3 = new JButton("CHIAMATA AL CAPOTURNO");
			btnNewButton_3.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					new DBCommand(true).invia_segnalazione("CHIAMATA URGENTE LINEA 2","UN OPERATORE HA EFFETTUATO UNA CHIAMATA URGENTE DALLA LINEA 3. RICHIESTA CAPOTURNO",Setting.EMAIL_CAPOTURNO);
					JOptionPane.showMessageDialog(frmPlc, "MESSAGGIO INVIATO","INFO",JOptionPane.INFORMATION_MESSAGE);
				}
			});
			btnNewButton_3.setFont(new Font("Segoe UI", Font.BOLD, 11));
			btnNewButton_3.setBackground(new Color(0, 191, 255));
			btnNewButton_3.setBounds(1701, 522, 200, 60);
			frmPlc.getContentPane().add(btnNewButton_3);
			
			JButton btnNewButton_3_1 = new JButton("CHIAMATA AL RESP. QUALITA'");
			btnNewButton_3_1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					new DBCommand(true).invia_segnalazione("CHIAMATA URGENTE LINEA 2","UN OPERATORE HA EFFETTUATO UNA CHIAMATA URGENTE DALLA LINEA 3. RICHIESTA QUALITA",Setting.EMAIL_QUALITA);
					JOptionPane.showMessageDialog(frmPlc, "MESSAGGIO INVIATO","INFO",JOptionPane.INFORMATION_MESSAGE);
				}
			});
			btnNewButton_3_1.setFont(new Font("Segoe UI", Font.BOLD, 11));
			btnNewButton_3_1.setBackground(new Color(0, 191, 255));
			btnNewButton_3_1.setBounds(1701, 593, 200, 60);
			frmPlc.getContentPane().add(btnNewButton_3_1);
			
			btnNewButton_3_2 = new JButton("ASSISTENZA ELETTRICA/PLC");
			btnNewButton_3_2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					new DBCommand(true).invia_segnalazione("CHIAMATA URGENTE LINEA 2","UN OPERATORE HA EFFETTUATO UNA CHIAMATA URGENTE DALLA LINEA 3. RICHIESTA ELETTRICA",Setting.EMAIL_ELETTRICISTA);
					JOptionPane.showMessageDialog(frmPlc, "MESSAGGIO INVIATO","INFO",JOptionPane.INFORMATION_MESSAGE);
				}
			});
			btnNewButton_3_2.setFont(new Font("Segoe UI", Font.BOLD, 11));
			btnNewButton_3_2.setBackground(new Color(0, 191, 255));
			btnNewButton_3_2.setBounds(1701, 664, 200, 60);
			frmPlc.getContentPane().add(btnNewButton_3_2);
			
			
			btn_abilitazione_10 = new JToggleButton();
			btn_abilitazione_10.setBackground(Color.WHITE);
			btn_abilitazione_10.setBorderPainted(false);
			btn_abilitazione_10.setBounds(1711, 862, 169, 84);
			btn_abilitazione_10.setIcon(new ImageIcon(icona_pulsante_controllo));
			btn_abilitazione_10.setSelected(true);
			
			
			/*
			btn_abilitazione_10.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (btn_abilitazione_10.isSelected()) {
						//btn_abilitazione_10.setText("CONTROLLO ABILITATO");  
						
						//new DBCommand(true).invia_segnalazione("CONTROLLO FINALE ABILITATO POST.10");
					}
						
			        else {
			        	//btn_abilitazione_10.setText("CONTROLLO DISATTIVATO"); 
			        	
			        	//new DBCommand(true).invia_segnalazione("CONTROLLO FINALE DISATTIVATO POST.10");
			        }
			        	
				}
			});
			*/
			
			btn_abilitazione_10.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					
					
					if((btn_abilitazione_10.isSelected())) {
						btn_abilitazione_10.setIcon(new ImageIcon(icona_pulsante_controllo));
						new DBCommand(true).invia_segnalazione("CONTROLLO LINEA 2","CONTROLLO FINALE ABILITATO ("+Setting.NOME_STAZIONE_DI_CONTROLLO_2+")",Setting.EMAIL_TUTTI);
						//btn_abilitazione_10.setSelectedIcon(new ImageIcon(icona_pulsante_controllo));
					}
					else {
						btn_abilitazione_10.setIcon(new ImageIcon(icona_pulsante_controllo_off));
						new DBCommand(true).invia_segnalazione("CONTROLLO LINEA 2",Setting.NOME_STAZIONE_DI_CONTROLLO_2+" DIABILITATO ("+Setting.NOME_STAZIONE_DI_CONTROLLO_2+")",Setting.EMAIL_TUTTI);
						//btn_abilitazione_10.setDisabledIcon(new ImageIcon(icona_pulsante_controllo_off));
					}
					
					WinConfigurazioneLinea frame = new WinConfigurazioneLinea();
					frame.setVisible(false);
					frame.salvaDatiConfigurazionePostazioneInterblocco(btn_abilitazione_10);
					
				}
			});
			
			//btn_abilitazione_10.setSelectedIcon(new ImageIcon("/resource/img_control.jpg"));
			frmPlc.getContentPane().add(btn_abilitazione_10);
			
			JLabel lblNewLabel_4 = new JLabel("CONTROLLO FINALE");
			lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel_4.setFont(new Font("Arial", Font.BOLD, 14));
			lblNewLabel_4.setBounds(1711, 828, 175, 31);
			frmPlc.getContentPane().add(lblNewLabel_4);
			
			try {
				
				frame.caricaDatiConfigurazionePostazioneInterblocco(btn_abilitazione_10);
				
				
				
			} catch (Exception e1) {
				log.write("\nMain. Errore caricaDatiConfigurazionePostazioneInterblocco : "+ e1.toString()+"\n");
				e1.printStackTrace();
			}
			
			mntmNewMenuItem.setEnabled(false); //il pulsante 'connetti' viene disabilitato
			
			start();
			
				try {
					txtTipoBatteria.setText(new plcCommand().leggiGreenCodeHome());
					
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			//new DBCommand(true).invia_segnalazione("CONTROLLO LINEA 3","PROGRAMMA DI CONTROLLO AVVIATO!",Setting.EMAIL_TUTTI);
	
}//fine metodo
	
	
	
	public void ElaboraDati() {
		
		Runnable target = new Runnable() {
			@Override
			public void run() {
				while(true) {
					
					try {
								Date date1 = new Date();
								LocalDate localDate = LocalDate.now(); 
								DayOfWeek dayOfWeek = DayOfWeek.from(localDate); 
								 
								 int day_of_week = DayOfWeek.from(localDate).getValue(); 
						  
								 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
								 SimpleDateFormat formatter_ora = new SimpleDateFormat("HH:mm");
								 
								 o_data.setText(days[day_of_week-1]+ " "+formatter.format(date1));
								 o_ora.setText(formatter_ora.format(date1));
								
								
								 
								 int ora = LocalDateTime.now().getHour();
								 //  log.write("ora:" + ora);
								 if ((ora>=6) && (ora<14) ) turno.setText("TURNO 1");
								 if ((ora>=14) && (ora<22) ) turno.setText("TURNO 2");
								 if ((ora>=22) && (ora<6) ) turno.setText("TURNO 3");
								 
								
								 if ((ora==22) && riazzera_contatori) {
									 	Date date = new Date(System.currentTimeMillis());
										SimpleDateFormat ier = new SimpleDateFormat("yyyy-MM-dd 22:00");
										String dax = ier.format(date);
										
										//SVUOTO BATTERY STORY
										arrayBatteryStory.svuota();
										
										inizio_conteggio.setText(dax);
										riazzera_contatori = false;
									}
								if ((ora==23)) {
										riazzera_contatori = true;
								}
								 
								// monitor.append("ciclo\n");
								
							
								  model.setRowCount(0); 
															
									
								  for(BatteryStory batteryStory: arrayBatteryStory.getArrayBatteryStory()) {
									    
									  //monitor.append("ciclo\n");
									  //CICLO SULLE BATTERYSTORY POSTAZIONE
									  Queue<Batteria> arrayBatterie = batteryStory.getElencoBatterie(); 
									  String codice = batteryStory.getCodiceBatteryStory();
									  
									  String[] ok = new String[12]; 
									  for(int j=0;j<12;j++) {
										  ok[j]= "/";
									  }
									  
									  String timestamp = batteryStory.getTimestamp();
									        //String codice = batteria.getCodiceBatteria();
									  for(Batteria batteria: arrayBatterie) {
										  if (batteria!=null) {
									        	//timestamp = batteria.gettimestamp();
									        	//monitor.append("prima di if\n");
									        	if (Integer.parseInt(batteria.getStatoBatteria())==1) ok[Integer.parseInt(batteria.getPostazione())-1] = "OK";
									        	else ok[Integer.parseInt(batteria.getPostazione())-1] = "KO";
								        	}//fine if
									   }//fine for
									  
									  
									  
									  
									   // monitor.append("aggiungo\n");
									   model.addRow(new Object[] { timestamp ,
											          codice,ok[0], ok[1], ok[2], ok[3], ok[4],ok[5],ok[6],ok[7],ok[8] });
									        
									    	
									    	// table.scrollRectToVisible(table.getCellRect(table.getRowCount()-1, table.getColumnCount(), true));
									  
							      }//fine for
								  
					} catch (Exception g) {
						log.write("\nErrore ElaboraDati : "+ g.toString()+"\n");
					}
					   
								  
								  
					 try {
							Thread.sleep(15000);
										
					} catch (InterruptedException e) {
								log.write("\nErrore main, Sleep ElaboraDati\n");
					}
								 
				
					 
				}//fine while
		}//fine run
			
			
			};
		
		new Thread(target).start();
		
		
		
	}//fine elabora dati
	
	
	//-----------  classe
	class ColoredTableCellRenderer  extends DefaultTableCellRenderer
	{
    public Component getTableCellRendererComponent
        (JTable table, Object value, boolean selected, boolean focused, int row, int column)
    {
        setEnabled(table == null || table.isEnabled()); // 
        
             
        setHorizontalAlignment(SwingConstants.CENTER);
       
        if (selected) {
            //this.setForeground(Color.BLUE.darker());
            this.setBackground(table.getSelectionBackground());
        } else {
            this.setBackground(table.getBackground());
        }
 
        if (focused) {
        }
        
       
        String number = (String)value;
       
        if (number!=null) {  
           if(number.equals("OK")){
                
                setForeground(Color.darkGray);
                setBackground(new Color(189, 255, 198));
              
            }
            if(number.equals("KO")){
            	setBackground(new Color(255, 135, 128));
            	setForeground(Color.darkGray);
            }
            if(number.equals("/")){
            	setBackground(Color.white);
            	setForeground(Color.gray);
            }
        }  
        
         setText(value !=null ? value.toString() : "");
        return this;
    }
    
   
    
}//fine colored table
	
	
	
	 
private void start() {
	
 
	try {
		CaricaDatiFromDB fromDB = new CaricaDatiFromDB();
 		fromDB.getDataFromDB();
	} catch (Exception e1) {
		log.write("ERRORE AVVIO MAIN CARICADATIFROMDB: " + e1.toString());
		e1.printStackTrace();
	}	
		
	try {
 		ElaboraDati();
	} catch (Exception e1) {
		log.write("ERRORE AVVIO MAIN ELABORADATI: " + e1.toString());
		e1.printStackTrace();
	}


		Runnable runnable2 =
		() ->  {	
				try {
					linea5 = new Linea(bufferBatterie, elenco_indicatori, areaErrore);
					Thread.sleep(600);				
					senderDB = new SenderDB(monitor, bufferBatterie , statoplc, statodb , arrayArrayBatterie);			
				} catch (Exception e1) {
					log.write("ERRORE AVVIO MAIN AVVIOLINEE: " + e1.toString());
					e1.printStackTrace();
				}	
		};//fine runnable2
		 //fine runnable2
		
		//AVVIO runnable2

		Thread t2 = new Thread(runnable2);	
		t2.start();
			
		//MOMENATANEAMENTE DISABILITATO
		/*	
			configuratore_plc = new ConfiguratorePLc(stato_configuratore_plc);
			try {
				configuratore_plc.inizializza();
				Thread t1 = new Thread(configuratore_plc);
				//AVVIO IL LETTORE READER PLC
				t1.start();
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.write("-------------------------->MAIN Errore ConfiguratorePLC:"+e.toString()+"\n");
			}
		*/
			
			
			CheckControlCVS CC_CVS=new CheckControlCVS();
		    scheduler.scheduleAtFixedRate(CC_CVS, 1, Setting.timeCheckControlCVS, TimeUnit.MINUTES); //ogni 3 minuti
			
			

	
}//fine metodo start
}//fine classe main


class Activator{
	
	private String appName="monitor_linea2";
    private File file;
    private FileChannel channel;
    private FileLock lock;
	
	public boolean isAppActive() {
        try {
            file = new File
                 (System.getProperty("user.home"), appName + ".tmp");
            channel = new RandomAccessFile(file, "rw").getChannel();

            try {
                lock = channel.tryLock();
            }
            catch (OverlappingFileLockException e) {
                // already locked
                closeLock();
                return true;
            }

            if (lock == null) {
                closeLock();
                return true;
            }

            Runtime.getRuntime().addShutdownHook(new Thread() {
                    // destroy the lock when the JVM is closing
                    public void run() {
                        closeLock();
                        deleteFile();
                    }
                });
            return false;
        }
        catch (Exception e) {
            closeLock();
            return true;
        }
    }

    private void closeLock() {
        try { lock.release();  }
        catch (Exception e) {  }
        try { channel.close(); }
        catch (Exception e) {  }
    }

    private void deleteFile() {
        try { file.delete(); }
        catch (Exception e) { }
    }



}//fine class Activator