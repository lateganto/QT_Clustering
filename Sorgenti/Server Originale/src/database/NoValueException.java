package database;

/**
 * <p> Title: NoValueException </p>
 * <p> Class description: modella l’assenza di un valore all’interno di un resultset. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class NoValueException extends Exception {
	
	private static final long serialVersionUID = -2000060478400436524L;

	/**
	 * Costruttore dell'eccezione a zero argomenti.
	 */
	public NoValueException() {
		super("No Value in ResultSet");
	}
	
	/**
	 * Costruttore con una stringa in input indicante il messaggio da visualizzare accanto al nome della classe di cui è 
	 * istanza l'oggetto eccezione.
	 * @param msg messaggio da visualizzare accanto al nome della classe di cui è istanza l'oggetto eccezione.
	 */
	public NoValueException(String msg) {
		super(msg);
	}

}
