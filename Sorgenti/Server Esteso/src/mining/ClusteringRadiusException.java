package mining;

/**
 * <p> Title: ClusteringRadiusException </p>
 * <p> Class description: modella una eccezione controllata da considerare qualora l'algoritmo di clustering generi un solo cluster. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class ClusteringRadiusException extends Exception {
	private static final long serialVersionUID = 2409768628963283342L;

	/**
	 * Costruttore dell'eccezione a zero argomenti che mostra un messaggio di default.
	 */
	ClusteringRadiusException() {
		super("All tuples in only one Cluster!");
	}
	
	/**
	 * Costruttore con una stringa in input indicante il messaggio da visualizzare accanto al nome della classe di cui è istanza 
	 * l'oggetto eccezione.
	 * @param msg messaggio da visualizzare accanto al nome della classe di cui è istanza l'oggetto eccezione.
	 */
	ClusteringRadiusException(String msg) {
		super(msg);
	}
	
}
