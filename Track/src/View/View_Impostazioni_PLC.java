package View;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import PLC.ConfiguratorePLc;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class View_Impostazioni_PLC extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ConfiguratorePLc configuratore_plc;
	private JTextField lt1_stabilitation_time;
	private JLabel lblLtStabilitationTime;
	private JTextField lt2_stabilitation_time;
	private JLabel lblLtTestTime;
	private JTextField lt1_test_time;
	private JLabel txt;
	private JTextField lt2_test_time;
	private JLabel lblLtSetpointPressure;
	private JTextField lt1_setpoint_pressure;
	private JLabel lblLtSetpointPressure_1;
	private JTextField lt2_setpoint_pressure;
	private JLabel lblLtLeak;
	private JTextField lt1_leak;
	private JLabel lblLtLeak_1;
	private JTextField lt2_leak;
	private JLabel lblHpRightMax;
	private JTextField hp_right_max;
	private JLabel lblLtSetpointPressure_3;
	private JTextField hp_right_min;
	private JLabel lblHpLeftMax;
	private JTextField hp_left_max;
	private JLabel lblLtLeak_3;
	private JTextField hp_left_min;
	private JLabel lblTime;
	private JTextField time5;
	private JLabel lblPressure;
	private JTextField pressure5;
	private JLabel lblMeasure;
	private JTextField measure5;
	private JLabel lblAggiornatoAl;
	private JTextField test_time;

	

	/**
	 * Create the frame.
	 */
	public View_Impostazioni_PLC() {
		//configuratore_plc = new ConfiguratorePLc();
		
		inizializza(); 
	}
	
	
	public void inizializza() {
		setBounds(100, 100, 782, 444);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("LT1 stabilitation time (ms)");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 12));
		lblNewLabel.setBounds(32, 38, 154, 24);
		contentPane.add(lblNewLabel);
		
		lt1_stabilitation_time = new JTextField();
		lt1_stabilitation_time.setHorizontalAlignment(SwingConstants.CENTER);
		lt1_stabilitation_time.setForeground(Color.BLACK);
		lt1_stabilitation_time.setEditable(false);
		lt1_stabilitation_time.setBounds(32, 67, 154, 20);
		contentPane.add(lt1_stabilitation_time);
		lt1_stabilitation_time.setColumns(10);
		setResizable(false);
		setTitle("PARAMETRI PLC");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		
		
		lblLtStabilitationTime = new JLabel("LT2 stabilitation time (ms)");
		lblLtStabilitationTime.setFont(new Font("Arial", Font.BOLD, 12));
		lblLtStabilitationTime.setBounds(218, 38, 154, 24);
		contentPane.add(lblLtStabilitationTime);
		
		lt2_stabilitation_time = new JTextField();
		lt2_stabilitation_time.setHorizontalAlignment(SwingConstants.CENTER);
		lt2_stabilitation_time.setText("");
		lt2_stabilitation_time.setForeground(Color.BLACK);
		lt2_stabilitation_time.setEditable(false);
		lt2_stabilitation_time.setColumns(10);
		lt2_stabilitation_time.setBounds(218, 67, 154, 20);
		contentPane.add(lt2_stabilitation_time);
		
		
		
		lblLtTestTime = new JLabel("LT1 test time (ms)");
		lblLtTestTime.setFont(new Font("Arial", Font.BOLD, 12));
		lblLtTestTime.setBounds(404, 38, 154, 24);
		contentPane.add(lblLtTestTime);
		
		lt1_test_time = new JTextField();
		lt1_test_time.setText("");
		lt1_test_time.setHorizontalAlignment(SwingConstants.CENTER);
		lt1_test_time.setForeground(Color.BLACK);
		lt1_test_time.setEditable(false);
		lt1_test_time.setColumns(10);
		lt1_test_time.setBounds(404, 67, 154, 20);
		contentPane.add(lt1_test_time);
		
		txt = new JLabel("LT2 test time (ms)");
		txt.setFont(new Font("Arial", Font.BOLD, 12));
		txt.setBounds(590, 38, 154, 24);
		contentPane.add(txt);
		
		lt2_test_time = new JTextField();
		lt2_test_time.setText("");
		lt2_test_time.setHorizontalAlignment(SwingConstants.CENTER);
		lt2_test_time.setForeground(Color.BLACK);
		lt2_test_time.setEditable(false);
		lt2_test_time.setColumns(10);
		lt2_test_time.setBounds(590, 67, 154, 20);
		contentPane.add(lt2_test_time);
		
		lt1_test_time.setText(ConfiguratorePLc.getLT1_test_time());
		lt2_test_time.setText(ConfiguratorePLc.getLT2_test_time());
		
		lblLtSetpointPressure = new JLabel("LT1 setpoint pressure");
		lblLtSetpointPressure.setFont(new Font("Arial", Font.BOLD, 12));
		lblLtSetpointPressure.setBounds(33, 128, 154, 24);
		contentPane.add(lblLtSetpointPressure);
		
		lt1_setpoint_pressure = new JTextField();
		lt1_setpoint_pressure.setText("");
		lt1_setpoint_pressure.setHorizontalAlignment(SwingConstants.CENTER);
		lt1_setpoint_pressure.setForeground(Color.BLACK);
		lt1_setpoint_pressure.setEditable(false);
		lt1_setpoint_pressure.setColumns(10);
		lt1_setpoint_pressure.setBounds(32, 154, 154, 20);
		contentPane.add(lt1_setpoint_pressure);
		
		lblLtSetpointPressure_1 = new JLabel("LT2 setpoint pressure");
		lblLtSetpointPressure_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblLtSetpointPressure_1.setBounds(218, 127, 154, 24);
		contentPane.add(lblLtSetpointPressure_1);
		
		lt2_setpoint_pressure = new JTextField();
		lt2_setpoint_pressure.setText("");
		lt2_setpoint_pressure.setHorizontalAlignment(SwingConstants.CENTER);
		lt2_setpoint_pressure.setForeground(Color.BLACK);
		lt2_setpoint_pressure.setEditable(false);
		lt2_setpoint_pressure.setColumns(10);
		lt2_setpoint_pressure.setBounds(218, 154, 154, 20);
		contentPane.add(lt2_setpoint_pressure);
		
		lblLtLeak = new JLabel("LT1 leak");
		lblLtLeak.setFont(new Font("Arial", Font.BOLD, 12));
		lblLtLeak.setBounds(404, 127, 146, 24);
		contentPane.add(lblLtLeak);
		
		lt1_leak = new JTextField();
		lt1_leak.setText("");
		lt1_leak.setHorizontalAlignment(SwingConstants.CENTER);
		lt1_leak.setForeground(Color.BLACK);
		lt1_leak.setEditable(false);
		lt1_leak.setColumns(10);
		lt1_leak.setBounds(404, 154, 154, 20);
		contentPane.add(lt1_leak);
		
		lblLtLeak_1 = new JLabel("LT2 leak");
		lblLtLeak_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblLtLeak_1.setBounds(590, 125, 146, 24);
		contentPane.add(lblLtLeak_1);
		
		lt2_leak = new JTextField();
		lt2_leak.setText("");
		lt2_leak.setHorizontalAlignment(SwingConstants.CENTER);
		lt2_leak.setForeground(Color.BLACK);
		lt2_leak.setEditable(false);
		lt2_leak.setColumns(10);
		lt2_leak.setBounds(590, 154, 154, 20);
		contentPane.add(lt2_leak);
		
		
		lblHpRightMax = new JLabel("HP right max");
		lblHpRightMax.setFont(new Font("Arial", Font.BOLD, 12));
		lblHpRightMax.setBounds(33, 216, 154, 24);
		contentPane.add(lblHpRightMax);
		
		hp_right_max = new JTextField();
		hp_right_max.setText("");
		hp_right_max.setHorizontalAlignment(SwingConstants.CENTER);
		hp_right_max.setForeground(Color.BLACK);
		hp_right_max.setEditable(false);
		hp_right_max.setColumns(10);
		hp_right_max.setBounds(32, 241, 154, 20);
		contentPane.add(hp_right_max);
		
		lblLtSetpointPressure_3 = new JLabel("HP right min");
		lblLtSetpointPressure_3.setFont(new Font("Arial", Font.BOLD, 12));
		lblLtSetpointPressure_3.setBounds(217, 215, 154, 24);
		contentPane.add(lblLtSetpointPressure_3);
		
		hp_right_min = new JTextField();
		hp_right_min.setText("");
		hp_right_min.setHorizontalAlignment(SwingConstants.CENTER);
		hp_right_min.setForeground(Color.BLACK);
		hp_right_min.setEditable(false);
		hp_right_min.setColumns(10);
		hp_right_min.setBounds(218, 241, 154, 20);
		contentPane.add(hp_right_min);
		
		lblHpLeftMax = new JLabel("HP left max");
		lblHpLeftMax.setFont(new Font("Arial", Font.BOLD, 12));
		lblHpLeftMax.setBounds(405, 214, 154, 24);
		contentPane.add(lblHpLeftMax);
		
		hp_left_max = new JTextField();
		hp_left_max.setText("");
		hp_left_max.setHorizontalAlignment(SwingConstants.CENTER);
		hp_left_max.setForeground(Color.BLACK);
		hp_left_max.setEditable(false);
		hp_left_max.setColumns(10);
		hp_left_max.setBounds(404, 241, 154, 20);
		contentPane.add(hp_left_max);
		
		lblLtLeak_3 = new JLabel("HP left min");
		lblLtLeak_3.setFont(new Font("Arial", Font.BOLD, 12));
		lblLtLeak_3.setBounds(589, 213, 154, 24);
		contentPane.add(lblLtLeak_3);
		
		hp_left_min = new JTextField();
		hp_left_min.setText("");
		hp_left_min.setHorizontalAlignment(SwingConstants.CENTER);
		hp_left_min.setForeground(Color.BLACK);
		hp_left_min.setEditable(false);
		hp_left_min.setColumns(10);
		hp_left_min.setBounds(590, 241, 154, 20);
		contentPane.add(hp_left_min);
		
		
		
		
		
		
		lblTime = new JLabel("Time 5");
		lblTime.setFont(new Font("Arial", Font.BOLD, 12));
		lblTime.setBounds(33, 297, 154, 24);
		contentPane.add(lblTime);
		
		time5 = new JTextField();
		time5.setText("");
		time5.setHorizontalAlignment(SwingConstants.CENTER);
		time5.setForeground(Color.BLACK);
		time5.setEditable(false);
		time5.setColumns(10);
		time5.setBounds(32, 328, 154, 20);
		contentPane.add(time5);
		
		lblPressure = new JLabel("Pressure 5");
		lblPressure.setFont(new Font("Arial", Font.BOLD, 12));
		lblPressure.setBounds(218, 300, 154, 24);
		contentPane.add(lblPressure);
		
		pressure5 = new JTextField();
		pressure5.setText("");
		pressure5.setHorizontalAlignment(SwingConstants.CENTER);
		pressure5.setForeground(Color.BLACK);
		pressure5.setEditable(false);
		pressure5.setColumns(10);
		pressure5.setBounds(218, 328, 154, 20);
		contentPane.add(pressure5);
		
		lblMeasure = new JLabel("Measure 5");
		lblMeasure.setFont(new Font("Arial", Font.BOLD, 12));
		lblMeasure.setBounds(404, 302, 154, 24);
		contentPane.add(lblMeasure);
		
		measure5 = new JTextField();
		measure5.setText("");
		measure5.setHorizontalAlignment(SwingConstants.CENTER);
		measure5.setForeground(Color.BLACK);
		measure5.setEditable(false);
		measure5.setColumns(10);
		measure5.setBounds(404, 328, 154, 20);
		contentPane.add(measure5);
		
		
		lblAggiornatoAl = new JLabel("Timestamp");
		lblAggiornatoAl.setFont(new Font("Arial", Font.BOLD, 12));
		lblAggiornatoAl.setBounds(592, 301, 154, 24);
		contentPane.add(lblAggiornatoAl);
		
		test_time = new JTextField();
		test_time.setText("");
		test_time.setHorizontalAlignment(SwingConstants.CENTER);
		test_time.setForeground(Color.RED);
		test_time.setEditable(false);
		test_time.setColumns(10);
		test_time.setBounds(590, 328, 154, 20);
		contentPane.add(test_time);
		
		
		lt1_stabilitation_time.setText(ConfiguratorePLc.getLT1_stabilitation_time());
		lt2_stabilitation_time.setText(ConfiguratorePLc.getLT2_stabilitation_time());
		
		lt1_setpoint_pressure.setText(""+ConfiguratorePLc.getLT1_setpoint_pressure());
		lt2_setpoint_pressure.setText(""+ConfiguratorePLc.getLT2_setpoint_pressure());
		lt1_leak.setText(""+ConfiguratorePLc.getLT1_leak());
		lt2_leak.setText(""+ConfiguratorePLc.getLT2_leak());
		
		hp_right_max.setText(""+ConfiguratorePLc.getHP_right_max());
		hp_right_min.setText(""+ConfiguratorePLc.getHP_right_min());
		hp_left_max.setText(""+ConfiguratorePLc.getHP_left_max());
		hp_left_min.setText(""+ConfiguratorePLc.getHP_left_min());
		
		time5.setText(""+ConfiguratorePLc.getTime5());
		pressure5.setText(""+ConfiguratorePLc.getPressure5());
		measure5.setText(""+ConfiguratorePLc.getMeasure5());
		
		test_time.setText(ConfiguratorePLc.getData_aggiornamento());
		
	
	}//fine inizializza
}//fine classe
