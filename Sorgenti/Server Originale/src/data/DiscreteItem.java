package data;

/**
 * <p> Title: DiscreteItem </p>
 * <p> Class description: rappresenta una coppia (Attributo discreto-valore discreto), per esempio Outlook=”Sunny”. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
class DiscreteItem extends Item {
	private static final long serialVersionUID = -8981642325015243784L;

	/**
	 * Invoca il costruttore della classe madre.
	 * @param attribute valore con cui inizializzare l'attributo.
	 * @param value valore con cui inizializzare il valore.
	 */
	DiscreteItem(DiscreteAttribute attribute, String value){
		super(attribute, value);
	}

	/**
	 * Restituisce 0 se il valore (value) dell'oggetto su cui è chiamato 
	 * è uguale a quello dell'oggetto passato per argomento, 1 altrimenti.
	 * @param a oggetto con cui si deve calcolare la distanza (dall'oggetto corrente).
	 * @return un double (0 o 1) a seconda del caso.
	 */
	double distance(Object a) {
		return((getValue().equals(a)) ? 0.0 : 1.0);
	}
	
}	
		
