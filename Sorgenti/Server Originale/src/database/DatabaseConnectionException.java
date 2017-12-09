package database;

/**
 * <p> Title: DatabaseConnectionException </p>
 * <p> Class description: modella il fallimento nella connessione al database. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class DatabaseConnectionException extends Exception{
	private static final long serialVersionUID = 6312916993042372641L;

	/**
	 * Costruttore dell'eccezione a zero argomenti che mostra un messaggio di default.
	 */
	public DatabaseConnectionException() {
		super("An exception occurred on Database Connection");
	}
	
	/**
	 * Costruttore con una stringa in input indicante il messaggio da visualizzare accanto al nome della classe di cui 
	 * è istanza l'oggetto eccezione.
	 * @param msg messaggio da visualizzare accanto al nome della classe di cuiè istanza l'oggetto eccezione.
	 */
	public DatabaseConnectionException(String msg) {
		super(msg);
	}

}
