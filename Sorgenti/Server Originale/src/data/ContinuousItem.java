package data;

/**
 * <p> Title: ContinuousItem </p>
 * <p> Class description: modella una coppia (Attributo continuo-valore numerico), per esempio Temperature=30.5. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
class ContinuousItem extends Item {
	private static final long serialVersionUID = -8493579589351461733L;

	/**
	 * Invoca il costruttore della classe madre.
	 * @param attribute valore con cui inizializzare l'attributo.
	 * @param value valore con cui inizializzare il valore.
	 */
	ContinuousItem(ContinuousAttribute attribute, Double value) {
		super(attribute, value);
	}

	/**
	 * Determina la distanza (in valore assoluto) tra il valore scalato memorizzato nello item corrente 
	 * e quello scalato associato al parametro a.
	 * @param a oggetto con cui effettuare il confronto.
	 * @return un double rappresentante la distanza in valore assoluto tra il valore scalato memorizzato nello 
	 * 		   Item corrente e quello scalato associato al parametro in input.
	 */
	double distance(Object a) {
		ContinuousAttribute c = (ContinuousAttribute)this.getAttribute();
		
		double one = c.getScaledValue((double) this.getValue());
		double two = c.getScaledValue((double) a);
		
		return Math.abs(one-two);
	}
	
}
