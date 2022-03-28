package linea;

import static linea.Encryption.*;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Setting {

	// SEZIONE PLC
	private static String IPPLC = "100.100.2.1";
	private static String RACK = "0";
	private static String SLOT = "1";
	public static final int DB_CONFIGURAZIONI_ABILITAZIONI_PLC = 5101; // in questo db vengono memorizzati i valori per
																		// abilitare scarto e lettore nel plc
	private static String DB = "5131";
	private static String DB2 = "5132";
	private static String DB3 = "5133";
	private static String DB4 = "5134";
	private static String DB5 = "5135";
	private static String DB6 = "5136";
	private static String DB7 = "5137";
	private static String DB8 = "5138";
	private static String DB9 = "5139";
	private static String DB10 = "5140"; // db per la postazione di controllo
	private static String TIPO_CONNESSIONE = "OP";
	private static String NUMERO_STAZIONI_ATTIVE = "9";

	// NOME DELLE STAZIONI DI CONTROLLO
	public static int STAZIONE_DI_CONTROLLO_1 = 20; // POSTAZIONE 7
	public static String NOME_STAZIONE_DI_CONTROLLO_1 = "POSTAZIONE DI CONTROLLO 1";

	public static int STAZIONE_DI_CONTROLLO_2 = 21; // POSTAZIONE 10
	public static String NOME_STAZIONE_DI_CONTROLLO_2 = "POSTAZIONE DI CONTROLLO 2";

	// public static final int DB_POSTAZIONE_CONTROLLO1 = 5140; ////DB scrittura
	// esito controllo finale. postazione 7
	public static int DB_POSTAZIONE_CONTROLLO2 = 5140; // DB scrittura esito controllo finale . postazione 10
	public static int DB_ESITO_POSTAZIONE_CONTROLLO2 = 5150; // 5150 //DB scrittura esito controllo finale . postazione
																// 10

	// SEZIONE DATABASE
	public static String IPDB = "etpserver.fiamm.dom";
	public static String IPPORT = "3306";
	public static String USERNAMEDB = "luca";
	public static String PASSWORDDB = "TestPassword123";
	public static String DB_NAME = "tracciabilita";

	// EMAIL
	public static String EMAIL_QUALITA = "agostino.caldaroni@fiamm.com";
	public static String EMAIL_CAPOTURNO = "capoturnoupi2@fiamm.com";
	public static String EMAIL_TUTTI = "agostino.caldaroni@fiamm.com;capoturnoupi2@fiamm.com;LUCA.BARUFFA@FIAMM.COM";
	public static String EMAIL_SISTEMISTA = "LUCA.BARUFFA@fiamm.com";
	public static String EMAIL_ELETTRICISTA = "OSVALDO.LELLI@fiamm.com;LUCA.PAVONI@FIAMM.COM";
	public static String EMAIL_ALLERTA = "Domenico.Cutillo@fiamm.com;Gianluca.Pupparo@fiamm.com;Cristian.Desiato@fiamm.com;Maurizio.Marinetti@fiamm.com;Agostino.Caldaroni@fiamm.com;antonio.roncone@fiamm.com;Ermanno.DeMeis@fiamm.com";
	public static String EMAIL_FERMI = "capoturnoupi2@fiamm.com;LUCA.BARUFFA@FIAMM.COM;gianluca.pupparo@fiamm.com;domenico.cutillo@fiamm.com";
	
	// SEZIONE ERRORI
	public static final int BATTERIA_OK = 0;
	public static final int BATTERIA_GIA_PROCESSATA = 1;
	public static final int SCARTO_PER_ESITO = 2;
	public static final int SCARTO_TIMEOUT = 3;
	public static final int SCARTO_SALTO_STAZIONE = 4;
	public static final int BYPASS = 5;
	public static final int BATTERIA_GIA_PROCESSATA_BUFFER = 6;
	public static final int ESITO_KO_ISTANTANEO = 7;
	public static final int BATTERIA_KO_GIA_PROCESSATA_BUFFER = 8;
	public static final int ESITO_KO_BILANCIA = 11;
	public static final int ERRORE_INDEFINITO = 99;

	// READERPLC
	public static final int TIMER_SLEEP_READERPLC = 1400; // ms
	public static final int NUMERO_CICLI_START_STOP = 70; // sleep * NUMERO_CICLI_START_STOP = millisecondi di oltre il
															// quale scatta il microstop
	public static final int DIMENSIONI_BUFFER_PLC = 50;

	// NUMERO DI DATABLOCK
	private int[] arrayDB = new int[40];

	public static final Color rosso = new Color(255, 135, 128);
	public static final Color verde = new Color(189, 255, 198);
	public static final Color arancio = new Color(245, 152, 66);
	public static final Color bianco = new Color(250, 250, 250);
	public static final Color grigio = Color.lightGray;
	// public Color coloreTransizione = new Color(250, 255, 150);
	// funzioni

	// TOTALE, RIPROCESSATE E SCARTATE utilizzato da caricaDatiFromdb e readPLC
	public static int totale_batterie_lavorate[] = new int[40]; // una per postazione
	public static int totale_batterie_rilavorate[] = new int[40]; // una per postazione
	public static int totale_batterie_scartate[] = new int[40]; // una per postazione

	// path
	public static String path = "C:/tracciabilita/log.txt";
	public static String dir = "c:/tracciabilita/";
	public static String nome_CSV = "CSV_batterie.csv";
	public static String nome_CSV_rinominato = "-CSV_batterie_SINCRONIZZATO.csv";

	// timeout query
	public static int timeout_query = 5000; // ms oltre il quale la query genere timeout

	public static int time_update_sender_db = 3000; // ms . tempo di refresh di senderDB

	private static JTextField areaErrore;
	private static JButton codice_batteria_scartata;
	private static JTextField tipologia_batterie;
	private static JTextField labelBatterieBuone;
	private static JTextField labelBatterieScartate;

	public static int tempoMaxLineaFerma = 5; // minuti oltre il quale si segnala la linea ferma
	public static int timeCheckControlCVS = 3; // gestoreCSV si aggiorna ogni 3 minuti;

	public static final String ATTESA_BATTERIA = "IN ATTESA DELLA BATTERIA";

	// vinene aggiornata ad ogni lavorazione di batteria. Da readerPLC.
	public static String data_ultimo_aggiornamento = "";

	public static int BTX12_min_acido = 782;
	public static int BTX12_max_acido = 818;
	public static int BTX14_min_acido = 915;
	public static int BTX14_max_acido = 951;

	public static List<greenCode> listaGreenCode = new ArrayList<>();

	public static int DBGREENCODE = 5020; // 5020;
	public static JTextField txtTipologiaBatteria;
	public static long startTime = 0;
	public static ElencoIndicatori elenco_indicatori = new ElencoIndicatori();
	public static long minuti_fermo_linea = 0; // numero minuti di lineaferma
	public static String data_fermo_linea = ""; // numero minuti di lineaferma
	public static String turno_fermo_linea = ""; // numero minuti di lineaferma
	public static int timeCheckControlGiustificativo = 5; // giustifificativo si aggiorna ogni 2 minuti;

	// sezione BATABASE
	public static String LINEA = "Linea 2";
	public static String LINEA_GIUSTIFICATIVI = "linea2";
	public static String TABLE_LINEA = "linea2";
	public static String DB_TABLE_BATTERIE_LINEA = "batterie_linea2";
	public static String DB_TABLE_FERMI_LINEA = "fermi_linea2";
	public static String DB_TABLE_STOP_LINEA = "stop_linea2";
	public static int DB_BATTERIE_NUM_LINEA = 2;

	byte[] salt = new String("12345678").getBytes();
	String pwd = "TestPassword123";
	
	int iterationCount = 40000;
	int keyLength = 128;
	SecretKeySpec key = createSecretKey(pwd.toCharArray(), salt, iterationCount, keyLength);
	
	public static int DBPLCSTATUS = 200;
	public static StatoPLC statiPLC[] = new StatoPLC[22]; // max 22 plc di campo
	public static int timeCheckControlplcStatus = 1; // giustifificativo si aggiorna ogni 2 minuti;
	
	public static String[] nomiPostazioni = { "TIME P1", "COD. BATTERIA", "CORTI 1", "PUNTAT 1", "PUNTAT 2", "CORTI 2", "TENUTA 1", "TENUTA 2", "ALT. POLAR","PALLETTIZ." };

	public static boolean riazzera_invio_email = true;
	
	/**
	 * @return the data_ultimo_aggiornamento
	 */
	public static synchronized String getData_aggiornamento() {
		return data_ultimo_aggiornamento;
	}

	/**
	 * @param data_ultimo_aggiornamento the data_ultimo_aggiornamento to set
	 */
	public static synchronized void setData_aggiornamento(String data_ultimo_aggiornamento) {
		Setting.data_ultimo_aggiornamento = data_ultimo_aggiornamento;
	}

	public Setting() throws Exception {
		
		
		// long adesso = System.nanoTime();
		// if (((adesso - startTime)/1000000000) > 10) {
		caricaConfigurazione();
		startTime = System.nanoTime();
		System.out.println("Setting con caricamento");
		// }else {
		// System.out.println("Loader Setting. Sono passati meno di 10 secondi. NON
		// RICARICO");

		// }

	}// setting

	public Setting(boolean tmp) throws Exception {
		System.out.println("Setting senza ricaricamento");
		// carico da qui se devo prelevare informazioni quando la configurazione è già
		// stata caricata

	}// setting

	public void WriteProperties(String var1, String value1, String var2, String value2, int postazione) {
		try {

			File configFile = new File("value.xml");
			OutputStream output = new FileOutputStream(configFile);
			Properties prop = new Properties();
			prop.setProperty(var1, value1);
			prop.setProperty(var2, value2);
			prop.storeToXML(output, "" + Setting.NOME_STAZIONE_DI_CONTROLLO_2);

			output.close();

			System.out.println("SCRITTURA OK Properties. var1=" + var1 + "  valore=" + value1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Errore scrittura PROPERTIES");
			e.printStackTrace();
		}
	}// fine writeProperties

	/*
	 * public void WriteProperties3(String var1, String value1, int postazione) {
	 * try {
	 * 
	 * File configFile = new File("value"+postazione+".xml"); OutputStream output =
	 * new FileOutputStream(configFile); Properties prop = new Properties();
	 * prop.setProperty(var1, value1); prop.storeToXML(output, "P" + postazione);
	 * 
	 * output.close();
	 * 
	 * 
	 * System.out.println("SCRITTURA OK Properties. var1=" + var1 + "  valore="+
	 * value1); } catch (IOException e) { // TODO Auto-generated catch block
	 * System.out.println("Errore scrittura PROPERTIES"); e.printStackTrace(); }
	 * }//fine writeProperties
	 */

	public String ReadProperties(String var1, int postazione) {

		File configFile = new File("value.xml");

		try {
			InputStream inputStream = new FileInputStream(configFile);
			Properties props = new Properties();
			props.loadFromXML(inputStream);

			String valore = props.getProperty(var1);

			inputStream.close();

			return valore;

		} catch (IOException ex) {
			System.out.println("Errore lettura Properties.");

			OutputStream output;
			try {
				output = new FileOutputStream(configFile);
				Properties prop = new Properties();
				prop.setProperty("conteggio_finale", "0");
				prop.setProperty("numero_batterie_scartate", "0");
				prop.storeToXML(output, "LINEA");

				output.close();
			} catch (Exception e5) {
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}

			ex.printStackTrace();
			return ("0");
		}
	}// finereadProperties

	// GET
	public String getIPPLC() {
		return IPPLC;
	}

	public String getNumeroStazioniAttive() {
		return NUMERO_STAZIONI_ATTIVE;
	}

	public String getRACK() {
		return RACK;
	}

	public String getSLOT() {
		return SLOT;
	}

	public String getDB() {
		return DB;
	}

	public String getDB2() {
		return DB2;
	}

	public String getDB3() {
		return DB3;
	}

	public String getDB4() {
		return DB4;
	}

	public String getDB5() {
		return DB5;
	}

	public String getDB6() {
		return DB6;
	}

	public String getDB7() {
		return DB7;
	}

	public String getDB8() {
		return DB8;
	}

	public String getDB9() {
		return DB9;
	}

	public String getDB10() {
		return DB10;
	}

	public String getConnectionType() {
		return TIPO_CONNESSIONE;
	}

	// GETDB
	public String getIPDB() {
		return IPDB;
	}

	public String getUSERNAMEDB() {
		return USERNAMEDB;
	}

	public String getPASSWORDDB() {
		return PASSWORDDB;
	}

	public JTextField getLabelBatterieBuone() {
		return labelBatterieBuone;
	}

	public JTextField getLabelBatterieScartate() {
		return labelBatterieScartate;
	}

	// SET
	public void setIPPLC(String s) {
		IPPLC = s;
	}

	public void setRACK(String s) {
		RACK = s;
	}

	public void setSLOT(String s) {
		SLOT = s;
	}

	public void setDB(String s) {
		DB = s;
	}

	public void setDB2(String s) {
		DB2 = s;
	}

	public void setDB3(String s) {
		DB3 = s;
	}

	public void setDB4(String s) {
		DB4 = s;
	}

	public void setDB5(String s) {
		DB5 = s;
	}

	public void setDB6(String s) {
		DB6 = s;
	}

	public void setDB7(String s) {
		DB7 = s;
	}

	public void setDB8(String s) {
		DB8 = s;
	}

	public void setDB9(String s) {
		DB9 = s;
	}

	public void setDB10(String s) {
		DB10 = s;
	}

	public void setConnectionType(String s) {
		TIPO_CONNESSIONE = s;
	}

	public void setNumeroStazioniAttive(String s) {
		NUMERO_STAZIONI_ATTIVE = s;
	}

	public void setLabelBatterieBuone(JTextField txtbatterie) {
		labelBatterieBuone = txtbatterie;
	}

	public void setLabelBatterieScartate(JTextField txtbatterie) {
		labelBatterieScartate = txtbatterie;
	}

	// SET_DB
	public void setIPDB(String s) {
		IPDB = s;
	}

	public void setUSERNAME_DB(String s) {
		USERNAMEDB = s;
	}

	public void setPASSWORD_DB(String s) {
		PASSWORDDB = s;
	}

	public void setTipologiaBatterie(JTextField tipologia_batt) {
		tipologia_batterie = tipologia_batt;
	}

	public JTextField getTipologiaBatterie() {
		return tipologia_batterie;
	}

	public void salvaConfigurazione() throws Exception {

		File configFile = new File("config.xml");

		// SecretKeySpec key = createSecretKey(pwd.toCharArray(), salt, iterationCount,
		// keyLength);

		String originalPassword = PASSWORDDB;

		String encryptedPassword = encrypt(originalPassword, key);
		System.out.println("Encrypted password: " + encryptedPassword);
		// String decryptedPassword = decrypt(encryptedPassword, key);
		// System.out.println("Decrypted password: " + decryptedPassword);

		Properties props = new Properties();
		props.setProperty("ipplc", IPPLC);
		props.setProperty("rack", RACK);
		props.setProperty("slot", SLOT);
		props.setProperty("db", DB);
		props.setProperty("db2", DB2);
		props.setProperty("db3", DB3);
		props.setProperty("db4", DB4);
		props.setProperty("db5", DB5);
		props.setProperty("db6", DB6);
		props.setProperty("db7", DB7);
		props.setProperty("db8", DB8);
		props.setProperty("db9", DB9);
		props.setProperty("db10", DB10);
		props.setProperty("tipoconnessione", TIPO_CONNESSIONE);
		props.setProperty("ipdb", IPDB);
		props.setProperty("usernamedb", USERNAMEDB);
		props.setProperty("passworddb", encryptedPassword);
		props.setProperty("numerostazioniattive", NUMERO_STAZIONI_ATTIVE);
		props.setProperty("dbgreencode", "" + DBGREENCODE);
		props.setProperty("linea", "" + LINEA);
		props.setProperty("linea_giustificativi", "" + LINEA_GIUSTIFICATIVI);
		props.setProperty("table_linea", "" + TABLE_LINEA);
		props.setProperty("db_table_batterie_linea", "" + DB_TABLE_BATTERIE_LINEA);
		props.setProperty("db_table_fermi_linea", "" + DB_TABLE_FERMI_LINEA);
		props.setProperty("db_table_stop_linea", "" + DB_TABLE_STOP_LINEA);
		props.setProperty("db_batterie_num_linea", "" + DB_BATTERIE_NUM_LINEA);
		props.setProperty("tempoMaxLineaFerma", "" + tempoMaxLineaFerma);

		props.setProperty("db_postazione_controllo", "" + DB_POSTAZIONE_CONTROLLO2);
		props.setProperty("db_esito_postazione_controllo", "" + DB_ESITO_POSTAZIONE_CONTROLLO2);

		arrayDB[0] = Integer.parseInt(DB);
		arrayDB[1] = Integer.parseInt(DB2);
		arrayDB[2] = Integer.parseInt(DB3);
		arrayDB[3] = Integer.parseInt(DB4);
		arrayDB[4] = Integer.parseInt(DB5);
		arrayDB[5] = Integer.parseInt(DB6);
		arrayDB[6] = Integer.parseInt(DB7);
		arrayDB[7] = Integer.parseInt(DB8);
		arrayDB[8] = Integer.parseInt(DB9);

		arrayDB[Setting.STAZIONE_DI_CONTROLLO_2] = DB_POSTAZIONE_CONTROLLO2;

		OutputStream outputStream = new FileOutputStream(configFile);
		props.storeToXML(outputStream, "CONFIGURAZIONE LINEA");
		outputStream.close();

	}// fine classe

	public void caricaConfigurazione() throws Exception {

		File configFile = new File("config.xml");

		try {
			InputStream inputStream = new FileInputStream(configFile);
			Properties props = new Properties();
			props.loadFromXML(inputStream);

			IPPLC = props.getProperty("ipplc");
			RACK = props.getProperty("rack");
			SLOT = props.getProperty("slot");
			DB = props.getProperty("db");
			DB2 = props.getProperty("db2");
			DB3 = props.getProperty("db3");
			DB4 = props.getProperty("db4");
			DB5 = props.getProperty("db5");
			DB6 = props.getProperty("db6");
			DB7 = props.getProperty("db7");
			DB8 = props.getProperty("db8");
			DB9 = props.getProperty("db9");
			DB10 = props.getProperty("db10");
			TIPO_CONNESSIONE = props.getProperty("tipoconnessione");
			IPDB = props.getProperty("ipdb");
			USERNAMEDB = props.getProperty("usernamedb");
			String TMP_PASSWORDDB = props.getProperty("passworddb");
			NUMERO_STAZIONI_ATTIVE = props.getProperty("numerostazioniattive");

			DBGREENCODE = Integer.parseInt(props.getProperty("dbgreencode"));

			LINEA = props.getProperty("linea");
			LINEA_GIUSTIFICATIVI = props.getProperty("linea_giustificativi");
			TABLE_LINEA = props.getProperty("table_linea");
			DB_TABLE_BATTERIE_LINEA = props.getProperty("db_table_batterie_linea");
			DB_TABLE_FERMI_LINEA = props.getProperty("db_table_fermi_linea");
			DB_TABLE_STOP_LINEA = props.getProperty("db_table_stop_linea");
			DB_BATTERIE_NUM_LINEA = Integer.parseInt(props.getProperty("db_batterie_num_linea"));
			tempoMaxLineaFerma = Integer.parseInt(props.getProperty("tempoMaxLineaFerma"));

			DB_ESITO_POSTAZIONE_CONTROLLO2 = Integer.parseInt(props.getProperty("db_esito_postazione_controllo"));
			DB_POSTAZIONE_CONTROLLO2 = Integer.parseInt(props.getProperty("db_postazione_controllo"));

			arrayDB[0] = Integer.parseInt(DB);
			arrayDB[1] = Integer.parseInt(DB2);
			arrayDB[2] = Integer.parseInt(DB3);
			arrayDB[3] = Integer.parseInt(DB4);
			arrayDB[4] = Integer.parseInt(DB5);
			arrayDB[5] = Integer.parseInt(DB6);
			arrayDB[6] = Integer.parseInt(DB7);
			arrayDB[7] = Integer.parseInt(DB8);
			arrayDB[8] = Integer.parseInt(DB9);

			// POSTAZIONE DI ECCEZIONE
			arrayDB[Setting.STAZIONE_DI_CONTROLLO_2] = DB_POSTAZIONE_CONTROLLO2; // postazione di controllo 2

			// String originalPassword = TMP_PASSWORDDB;
			// System.out.println("Original password: " + originalPassword);

			String decryptedPassword = decrypt(TMP_PASSWORDDB, key);
			// System.out.println("Decrypted password: " + decryptedPassword);

			PASSWORDDB = decryptedPassword;

			inputStream.close();
		} catch (FileNotFoundException ex) {
			System.out.println(ex.toString());

			salvaConfigurazione();

		} catch (IOException ex) {
			System.out.println(ex.toString());
		}

	}// fine caricaConfigurazione

	public int[] getArrayDB() {
		return arrayDB;
	}

	public void setAreaError(JTextField label) {
		areaErrore = label;
	}

	public JTextField getAreaError() {
		return areaErrore;
	}

	public static void setCodiceBatteriaScartata(JButton label) {
		codice_batteria_scartata = label;
	}

	public static JButton getCodiceBatteriaScartata() {
		return codice_batteria_scartata;
	}

	public static double roundAvoid(double value, int places) {
		double scale = Math.pow(10, places);
		return (Math.round(value * scale) / scale);
	}

}// fine classe
