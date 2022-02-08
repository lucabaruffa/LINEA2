package DB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import linea2.Indicatore;
import linea2.LoggerFile;
import linea2.Setting;

public class DBConnectionPool {
    private static String databaseUrl;
    private static String userName;
    private static String password;
    private int maxPoolSize = 35;
    private int connNum = 0;
    private Setting setting;
    private static final String SQL_VERIFYCONN = "select 1";

    private static Stack<Connection> freePool = new Stack<>();
    private static Set<Connection> occupiedPool = new HashSet<>();
    
    //private Indicatore indicatore = new Indicatore();
    
    private static LoggerFile log = new LoggerFile();

    /**
     * Constructor
     * 
     * @param databaseUrl
     *            The connection url
     * @param userName
     *            user name
     * @param password
     *            password
     * @param maxSize
     *            max size of the connection pool
     */
    
    public DBConnectionPool(String databaseUrl, String userName, String password) {
        this.databaseUrl = databaseUrl;
        this.userName = userName;
        this.password = password;
        //this.maxPoolSize = maxSize;
    }
    

    public void setDBConnectionPool(String databaseUrl, String userName, String password) {
        this.databaseUrl = databaseUrl;
        this.userName = userName;
        this.password = password;
        //his.maxPoolSize = maxSize;
    }
    
    public DBConnectionPool() {
    	try {
			setting = new Setting(true);
		} catch (Exception e) {
			log.write("ERRORE CARICAMENTO CONFIGURAZIONE. Line 52. Modulo:DBConnectionPool");
			
		}
    	
      //POOL CONNESSIONI DB
 		setDBConnectionPool("jdbc:mysql://"+Setting.IPDB+"/"+Setting.DB_NAME, setting.getUSERNAMEDB(), setting.getPASSWORDDB());
    	
 		
 		
 		
 		
 		
    }
    
    /**
     * Get an available connection
     * 
     * @return An available connection
     * @throws SQLException
     *             Fail to get an available connection
     */
    public synchronized Connection getConnection() throws Exception {
        Connection conn = null;
        
        //log.write("RICHIESTA DI CONNESSIONE AL DB\n");

        if (isFull()) {
        	log.write("POOL DI CONNESSIONI PIENO - modulo:DBConnection\n");
            throw new SQLException("The connection pool is full.");
        }

        conn = getConnectionFromPool();

        
	        // If there is no free connection, create a new one.
	        if (conn == null) {
	            conn = createNewConnectionForPool();
	            log.write("POOL DOI CONNESSIONI LIBERE VUOTO! NE AGGIUNGO UNA NUOVA. dimensioni attuali free POOL ="+freePool.size()+" - OCCUPATI:"+occupiedPool.size()+"");
	        }
	
	        // For Azure Database for MySQL, if there is no action on one connection for some
	        // time, the connection is lost. By this, make sure the connection is
	        // active. Otherwise reconnect it.
	        conn = makeAvailable(conn);
	        
	        //log.write("DIMENSIONE free POOL ="+freePool.size()+" - OCCUPATI:"+occupiedPool.size()+"");
	        
	        return conn;
       
    }

    /**
     * Return a connection to the pool
     * 
     * @param conn
     *            The connection
     * @throws SQLException
     *             When the connection is returned already or it isn't gotten
     *             from the pool.
     */
    public synchronized void returnConnection(Connection conn) 
     {
    	if (conn == null) {
            throw new NullPointerException();
        }
    	synchronized(occupiedPool) { 
    		
	        if (!occupiedPool.remove(conn)) {
	        //	indicatore.monitor.append(
	            //log.write("IL POOL DI CONNESSIONE NON CONTIENE LA CONNESSIONE free POOL ="+freePool.size()+" - OCCUPATI:"+occupiedPool.size()+"");       //  "The connection is returned already or it isn't for this pool");
	        	return;
	        }
	        
    	}
    	synchronized(freePool) { 
	        	freePool.push(conn);
	        	//log.write("rIAGGIUNGO LA CONNESSIONE AL FREEPOOL . free POOL ="+freePool.size()+" - OCCUPATI:"+occupiedPool.size()+"");
    	}
        //indicatore.monitor.append("\nRILASCIO CONNESSIONE\n");
    }

    /**
     * Verify if the connection is full.
     * 
     * @return if the connection is full
     */
    private synchronized boolean isFull() {
        return ((freePool.size() == 0) && (connNum >= maxPoolSize));
    }

    /**
     * Create a connection for the pool
     * 
     * @return the new created connection
     * @throws SQLException
     *             When fail to create a new connection.
     */
    private Connection createNewConnectionForPool() throws Exception {
    	synchronized(freePool) { 
	    	Connection conn = createNewConnection();
	        connNum++;
	        occupiedPool.add(conn);
	        return conn;
    	}
    }

    /**
     * Crate a new connection
     * 
     * @return the new created connection
     * @throws SQLException
     *             When fail to create a new connection.
     * @throws ClassNotFoundException 
     */
    private Connection createNewConnection() throws SQLException, ClassNotFoundException {
    	Class.forName("com.mysql.cj.jdbc.Driver");
    	//jdbc:odbc:dataSourceName
    	
    	Connection conn = null;
        conn = DriverManager.getConnection(databaseUrl, userName, password);
    	
        return conn;
    }

    /**
     * Get a connection from the pool. If there is no free connection, return
     * null
     * 
     * @return the connection.
     */
    private Connection getConnectionFromPool() {
        Connection conn = null;
        synchronized(freePool) { 
	        if (freePool.size() > 0) {
	            conn = freePool.pop();
	            occupiedPool.add(conn);
	        }
	        return conn;
        }
       
    }

    /**
     * Make sure the connection is available now. Otherwise, reconnect it.
     * 
     * @param conn
     *            The connection for verification.
     * @return the available connection.
     * @throws SQLException
     *             Fail to get an available connection
     * @throws ClassNotFoundException 
     */
    private Connection makeAvailable(Connection conn) throws SQLException, ClassNotFoundException {
        if (isConnectionAvailable(conn)) {
            return conn;
        }

        // If the connection is't available, reconnect it.
        synchronized(occupiedPool) { 
	        occupiedPool.remove(conn);
	        connNum--;
	        conn.close();
        }

        synchronized(occupiedPool) { 
	        conn = createNewConnection();
	        occupiedPool.add(conn);
	        connNum++;
	        return conn;
        }
    }

    /**
     * By running a sql to verify if the connection is available
     * 
     * @param conn
     *            The connection for verification
     * @return if the connection is available for now.
     */
    private boolean isConnectionAvailable(Connection conn) {
        try (Statement st = conn.createStatement()) {
            st.executeQuery(SQL_VERIFYCONN);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
  

}
