package database;

/**
 * <p> Title: EmptySetException </p>
 * <p> Class description: modella la restituzione di un resultset vuoto. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class EmptySetException extends Exception {
	private static final long serialVersionUID = -5833380353580231430L;

	/**
	 * Costruttore dell'eccezione a zero argomenti che mostra un messaggio di default.
	 */
	public EmptySetException() {
		super("Empty ResultSet!");
	}
	
	/**
	 * Costruttore con una stringa in input indicante il messaggio da visualizzare accanto al nome della classe di cui è istanza 
	 * l'oggetto eccezione.
	 * @param msg messaggio da visualizzare accanto al nome della classe di cui è istanza l'oggetto eccezione.
	 */
	public EmptySetException(String msg) {
		super(msg);
	}

}
