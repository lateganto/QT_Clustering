package data;

/**
 * <p> Title: EmptyDatasetException </p>
 * <p> Class description: modella una eccezione controllata da considerare qualora il dataset sia vuoto. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class EmptyDatasetException extends Exception {
	private static final long serialVersionUID = -9045932415736226492L;

	/**
	 * Costruttore dell'eccezione a zero argomenti che mostra un messaggio di default.
	 */
	EmptyDatasetException() {
		super("Empty data!");
	}
	
	/**
	 * Costruttore con una stringa in input indicante il messaggio da visualizzare
	 * accanto al nome della classe di cui è istanza l'oggetto eccezione.
	 * @param msg messaggio da visualizzare accanto al nome della classe di cui è istanza l'oggetto eccezione.
	 */
	EmptyDatasetException(String msg) {
		super(msg);
	}
}
