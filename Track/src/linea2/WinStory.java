
package linea2;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import DB.DBConnectionPool;

//import main.ColoredTableCellRenderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.JTable;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.Toolkit;

public class WinStory extends JFrame {

	private JPanel contentPane;
	private String codice_batteria = "-1";
	private static DefaultTableModel model = new DefaultTableModel();
	private JTable table;
	private JScrollPane scrollPane ;
	public 	Connection c_mysql;
	public Statement stmt_mysql ;
	private Setting setting;
	private DBConnectionPool pool;
	private static LoggerFile log = new LoggerFile();
	
	private int timeout_query = 3000;

	/**
	 * Create the frame.
	 */
	public WinStory(String codice_batt) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(WinStory.class.getResource("/resource/icon.png")));
		
		codice_batteria = codice_batt;
		
		setResizable(false);
		setAlwaysOnTop(true);
		setTitle("BATTERIA :" + codice_batteria);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1005, 568);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 979, 360);
		contentPane.add(scrollPane);
		
		try {
			setting = new Setting(true);
		} catch (Exception e) {
			log.write("ERRORE CARICAMENTO CONFIGURAZIONE  - MODULO: CaricaDatiFromdb");
			e.printStackTrace();
		}
		
		
		impostaTabella();
		
		
		//ImageIcon icon = new ImageIcon("/resource/linea.png");
		JLabel sinottico = new JLabel("");
		sinottico.setHorizontalAlignment(SwingConstants.CENTER);
		sinottico.setBounds(10, 382, 979, 143);
		sinottico.setIcon(new ImageIcon(WinStory.class.getResource("/resource/linea2.png")));
		contentPane.add(sinottico);
	    
		//table.setModel(model);
		
	}//fine init
	
	
	
	public void impostaTabella() {
		String[][] data = { 
	            { "11/11/2020 10:42:27","0000125450534", "OK", "125", "126", "1"} 
	        }; 
	  
	        // Column Names 
	    String[] columnNames = {"TIME", "COD. BATTERIA","POSTAZIONE", "TEST", "V1" ,"V2","DIFF"};
		
	    table = new JTable(data,columnNames);
	    table.setBackground(Color.WHITE);
		scrollPane.setViewportView(table);
		
		model.setColumnIdentifiers(columnNames);
		
		table.setModel(model);

		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		
		TableColumn tm = table.getColumnModel().getColumn(0);
		tm.setPreferredWidth(120);
		tm.setCellRenderer(new ColoredTableCellRenderer());
		
		TableColumn tm1 = table.getColumnModel().getColumn(1);
		tm1.setCellRenderer(new ColoredTableCellRenderer());
		
		TableColumn tm2 = table.getColumnModel().getColumn(2);
		tm2.setCellRenderer(new ColoredTableCellRenderer());
		
		TableColumn tm3 = table.getColumnModel().getColumn(3);
		tm3.setCellRenderer(new ColoredTableCellRenderer());
		
		TableColumn tm4 = table.getColumnModel().getColumn(4);
		tm4.setCellRenderer(new ColoredTableCellRenderer());
		
		TableColumn tm5 = table.getColumnModel().getColumn(5);
		tm5.setCellRenderer(new ColoredTableCellRenderer());
		
		TableColumn tm6 = table.getColumnModel().getColumn(6);
		tm6.setCellRenderer(new ColoredTableCellRenderer());
		
		//TableColumn tm3 = table.getColumnModel().getColumn(3);
		//tm3.setCellRenderer(new ColoredTableCellRenderer());
		model.setRowCount(0); 
		
		dati();
		
	}//fine impostatabella
	
	
	public void dati() {
		pool = new DBConnectionPool();
		
		int risposta = -1;  
		
		try {
			c_mysql = pool.getConnection();
		  } catch (Exception e) {  
			log.write("ERRORE CREAZIONE CONNESSIONE CHECK . MODULO WinStory");
	      	System.err.println(e.toString());
	           
	      }
	  
		  try { 
			stmt_mysql = c_mysql.createStatement(); 
			stmt_mysql.setQueryTimeout(timeout_query);
		  } catch (Exception e) {
			  log.write("ERRORE CREAZIONE STATEMENT CHECK. MODULO WinStory ");
	          System.out.println("Errore: " +  e.toString());
	      }//FINE CATCH
		  
		
	  
	   ResultSet rs;
		try {
			
			
			SimpleDateFormat ier = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 22:00:00");
			Date date = new Date(System.currentTimeMillis());
			
			String dax = ier.format(date);
			LocalDate da  = LocalDate.parse(dax);
			LocalDate ieri = da.minusDays(1); 
			
			
			dax = ieri+" 22:00:00";//getMyDate(""+ieri+" 10:00:00", "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
			
			
			rs = stmt_mysql.executeQuery("SELECT * FROM linea2 where codice='"+codice_batteria+"' order by data asc");	
			
			
			while (rs.next()){
				
				try {
					
					String cod_batteria = rs.getString("codice");
					int postazione = rs.getInt("postazione");
					int stato_test = rs.getInt("stato_test");
					String timestamp = rs.getString("data");
					int valore1 = rs.getInt("valore1");
					int valore2 = rs.getInt("valore2");
					int differenza = rs.getInt("differenza");
					
					timestamp = timestamp.substring(0,timestamp.length());
					
					System.out.println("Stampo batteria:" + cod_batteria + "  time:"+timestamp + "   postazione:" + postazione);
					
					
					timestamp = getMyDate(timestamp, "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
					
					  model.addRow(new Object[] { timestamp ,
							  cod_batteria,postazione, stato_test, valore1, valore2, differenza });
					
					
				} catch (Exception e) {
					log.write("ERRORE CREAZIONE BATTERIA: "+ e.toString() + "   modulo:winStory");
					System.out.print("Errore:" +e.toString());
					e.printStackTrace();
				}
			}//fine while
			
			rs.close();
		
			
		
		} catch (SQLException e) {
			log.write("ERRORE IN CHECK. CONNESSIONE. POSSIBILE TIMEOUT: "+e.toString() + " modulo: caricadatifromdb");
			e.printStackTrace();
			
			
		} finally {
			pool.returnConnection(c_mysql);
			//return risposta;
		}
	    
	  
	}//fine dati
	
	
	public static String getMyDate(String myDate, String requiredFormat, String mycurrentFormat) {
        DateFormat dateFormat = new SimpleDateFormat(requiredFormat);
        Date date = null;
        String returnValue = "";
        try {
            date = new SimpleDateFormat(mycurrentFormat, Locale.ENGLISH).parse(myDate);
            returnValue = dateFormat.format(date);
        } catch (Exception e) {
            returnValue = myDate;
        } 
        return returnValue;
    }//fine
	
	
	
	//-----------  classe
		class ColoredTableCellRenderer  extends DefaultTableCellRenderer
		{
	    public Component getTableCellRendererComponent
	        (JTable table, Object value, boolean selected, boolean focused, int row, int column)
	    {
	        setEnabled(table == null || table.isEnabled()); // 
	        
	        String postazione = table.getModel().getValueAt(row, 2).toString();
	             
	        setHorizontalAlignment(SwingConstants.CENTER);
	       
	        if (selected) {
	            //this.setForeground(Color.BLUE.darker());
	            this.setBackground(table.getSelectionBackground());
	        } else {
	            this.setBackground(table.getBackground());
	        }
	 
	        if (focused) {
	        }
	        
	       
	        	String number =""; 
	       try {
	    	   number = ""+value;
	       }catch(Exception g) {
	    	  // number = 
	       }
	        	
	        	if(column==3) {
			        if (number!=null) {  
			           if(number.equals("1")){
			                
			        	   value ="OK";
			                setForeground(Color.darkGray);
			                setBackground(Color.green);
			              
			            }
			            if(number.equals("0")){
			            	value ="KO";
			            	setBackground(Color.red);
			            	setForeground(Color.darkGray);
			            }
			            if(number.equals("")){
			            	value="/";
			            	setBackground(Color.white);
			            	setForeground(Color.gray);
			            }
			        } 
	        	}
	        
	        
	        if ((postazione.equals("1")) && (column==4)) value = value + "";	
	        if ((postazione.equals("2")) && (column==4)) value = value + "";	
	        if ((postazione.equals("3")) && (column==4)) value = value + "";	
	        if ((postazione.equals("4")) && (column==4)) value = value + "";
	        
	        if ((postazione.equals("1")) && (column==5)) value = value + "";	
	        if ((postazione.equals("2")) && (column==5)) value = value + "";	
	        if ((postazione.equals("3")) && (column==5)) value = value + "";	
	        if ((postazione.equals("4")) && (column==5)) value = value + "";
	        	
	        if ((postazione.equals("5")) && (column==4)) value = value + " [mbar]";	
	        if ((postazione.equals("6")) && (column==4)) value = value + " [mbar]";	
	        if ((postazione.equals("5")) && (column==5)) value = value + " [mbar]";	
	        if ((postazione.equals("6")) && (column==5)) value = value + " [mbar]";	
	        
	        
	        if ((postazione.equals("7")) && (column==4)) {
	        	float v1 = ((float)((int)(value)))/1000;
	        	value = v1 + " [mm]";	
	        }
	        if ((postazione.equals("7")) && (column==5)) {
	        	float v2 = ((float)((int)(value)))/1000;
	        	value = v2 + " [mm]";	
	        }
	        
	        if ((postazione.equals("8")) && (column==4)) value = value + " [g]";
	        if ((postazione.equals("9")) && (column==4)) value = value + " [g]";
	        if ((postazione.equals("8")) && (column==5)) value = value + "";
	        if ((postazione.equals("9")) && (column==5)) value = value + "";
	        
	        if ((postazione.equals("1")) && (column==6)) value = value + "";
	        if ((postazione.equals("2")) && (column==6)) value = value + "";
	        if ((postazione.equals("3")) && (column==6)) value = value + "";
	        if ((postazione.equals("4")) && (column==6)) value = value + "";
	        if ((postazione.equals("5")) && (column==6)) value = value + "";
	        if ((postazione.equals("6")) && (column==6)) value = value + "";
	        if ((postazione.equals("7")) && (column==6)) value = value + "";
	        if ((postazione.equals("8")) && (column==6)) value = value + "";
	        
	        
	        if ((postazione.equals("9")) && (column==6)) value = value + " [g]";
	        	
	        	
	         setText(value !=null ? value.toString() : "");
	        return this;
	    }
	}
	
}//fine class
