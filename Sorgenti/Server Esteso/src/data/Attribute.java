package data;

import java.io.Serializable;

/**
 * <p> Title: Attribute </p>
 * <p> Class description: modella la entità "attributo". </p>
 * @author Gentile, Lategano, Visaggi
 * 
 */
abstract class Attribute implements Serializable{
	private static final long serialVersionUID = -6390565271658857328L;
	/**
	 * Nome simbolico dell'attributo.
	 */
	private String name;
	/**
	 * Identificativo numerico dell'attributo.
	 */
	private int index;
	
	/**
	 * Inizializza gli attributi con i valori passati in input.
	 * @param name valore per inizializzare il nome.
	 * @param index valore per inizializzare l'id numerico dell'attributo.
	 */
	public Attribute(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	/**
	 * Restituisce il nome dell'attributo.
	 * @return una stringa rappresentante il nome dell'attributo.
	 */
	String getName() {
		return name;
	}
	
	/**
	 * Restituisce l'id numerico dell'attributo.
	 * @return un intero rappresentante l'id numerico dell'attributo.
	 */
	int getIndex() {
		return index;
	}
	
	/**
	 * Restituisce la stringa rappresentante lo stato dell'oggetto.
	 * @return una stringa rappresentante il nome dell'attributo.
	 */
	public String toString() {
		return name;
	}
	
}
