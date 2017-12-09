package database;

import java.sql.*;

/**
 * <p> Title: DbAccess </p>
 * <p> Class description: realizza l'accesso alla base di dati. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class DbAccess {
	
	/**
	 * Stringa utilizzata per caricare il DriverManager.
	 */
	private String DRIVER_CLASS_NAME;
	/**
	 * Stringa indicante il protocollo e il sottoprotocollo per la connessione.
	 */
	private final String DBMS;
	/**
	 * Stringa usata per indicare la macchina in cui si trova il db.
	 */
	private final String SERVER;
	/**
	 * Nome del database da usare per comporre la stringa per la connessione.
	 */
	private final String DATABASE;
	/**
	 * Porta della macchina su cui si trova il database.
	 */
	private final String PORT;
	/**
	 * Nome utente per accedere al database.
	 */
	private final String USER_ID;
	/**
	 * Password per accedere al database.
	 */
	private final String PASSWORD;
	/**
	 * Oggetto che modella la connessione al database.
	 */
	private Connection conn;
	
	/**
	 * Costruttore che inizializza gli attributi.
	 */
	public DbAccess() {
		DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver"; 
		DBMS = "jdbc:mysql";
		SERVER ="localhost";
		DATABASE = "MapDB";
		PORT = "3306";
		USER_ID = "MapUser";
		PASSWORD = "map";
	}
	
	/**
	 * Impartisce al class loader l’ordine di caricare il DriverManager MySQL, inizializza la connessione riferita da conn.
	 * @throws DatabaseConnectionException in caso di fallimento nella connessione al database.
	 */
	public void initConnection() throws DatabaseConnectionException {
		
		try {
			Class.forName(DRIVER_CLASS_NAME).newInstance();
		} catch(ClassNotFoundException | IllegalAccessException | InstantiationException e) {
			throw new DatabaseConnectionException(e.getMessage());
		}
		
		try {
			conn = DriverManager.getConnection(DBMS+"://" + SERVER + ":" + PORT + "/" + DATABASE 
					+ "?user=" + USER_ID + "&password=" + PASSWORD);
		} catch(SQLException e) {
			throw new DatabaseConnectionException("SQLException - " + e.getMessage());
		}
	
	}
	
	/**
	 * Restituisce l'oggetto che modella la connessione.
	 * @return oggetto di tipo Connection che permette di gestire la connessione al database.
	 */
	public Connection getConnection() {
		return conn;
	}
	
	/**
	 * Chiude la connessione al database.
	 * @throws DatabaseConnectionException in caso di errori nella chiusura della connessione.
	 */
	public void closeConnection() throws DatabaseConnectionException {
		try {
			conn.close();
		} catch(SQLException e) {
			throw new DatabaseConnectionException("Exception occured on Connection Closing: " + e.getMessage());
		}
		
	}
	
}
