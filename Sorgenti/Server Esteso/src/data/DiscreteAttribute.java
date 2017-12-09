package data;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * <p> Title: DiscreteAttribute </p>
 * <p> Class description: modella un attributo discreto (o categorico). </p>
 * @author Gentile, Lategano, Visaggi
 * 
 */
class DiscreteAttribute extends Attribute implements Iterable<String> {
	
	private static final long serialVersionUID = -876356778894362574L;
	/**
	 * Insieme di stringhe (set), una per ciascun valore del dominio discreto. 
	 * I valori del dominio sono memorizzati in values seguendo un ordine lessicografico.
	 */
	private TreeSet<String> values;
	
	/**
	 * Invoca il costruttore della classe madre e inizializza il membro values con il parametro in input.
	 * @param name valore per inizializzare il nome.
	 * @param index valore per inizializzare l'id numerico dell'attributo.
	 * @param values set di stringhe, una per ciascun valore del dominio discreto.
	 */
	DiscreteAttribute(String name, int index, TreeSet<String> values) {
		super(name, index);
		this.values = values;
	}

	/**
	 * Restituisce la dimensione di values.
	 * @return un intero rappresentante il numero di valori discreti nel dominio dell'attributo.
	 */
	int getNumberOfDistinctValues() {
		return values.size();
	}
	
	/**
	 * Restituisce un iteratore per il contenitore values.
	 * @return un iteratore per scorrere gli elementi del contenitore.
	 */
	public Iterator<String> iterator(){
		return values.iterator();
	}
	
}
