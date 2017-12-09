package data;

/**
 * <p> Title: ContinuousAttribute </p>
 * <p> Class description: modella un attributo continuo (numerico). Tale classe include i metodi 
 * 						  per la "normalizzazione" del dominio dell'attributo nell'intervallo [0,1] 
 * 						  al fine di rendere confrontabili attributi aventi domini diversi. </p>
 * @author Gentile, Lategano, Visaggi
 * 
 */
class ContinuousAttribute extends Attribute {
	
	private static final long serialVersionUID = 3837707871311045859L;
	/**
	 * Estremo superiore dell'intervallo di valori (dominio) che l'attributo può realmente assumere.
	 */
	private double min;
	/**
	 * Estremo inferiore dell'intervallo di valori (dominio) che l'attributo può realmente assumere. 
	 */
	private double max;
	
	/**
	 * Invoca il costruttore della classe madre e inizializza i membri aggiunti per estensione.
	 * @param name valore per inizializzare il nome.
	 * @param index valore per inizializzare l'id numerico dell'attributo.
	 * @param min valore per inizializzare l'estremo inferiore dell'intervallo.
	 * @param max valore per inizializzare l'estremo superiore dell'intervallo.
	 */
	ContinuousAttribute(String name, int index, double min, double max) {
		super(name, index);
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Calcola e restituisce il valore normalizzato del parametro passato in input. La normalizzazione ha 
	 * come codominio l'intervallo [0,1]. La normalizzazione di v è quindi calcolata così: v'=(v-min)/(max-min).
	 * @param v valore dell'attributo da normalizzare.
	 * @return un double rappresentante il valore normalizzato di tipo double.
	 */
	double getScaledValue(double v) {
		return (v-min)/(max-min);
	}
	
}
 