package data;

import java.io.Serializable;

/**
 * <p> Title: Item </p>
 * <p> Class description: modella un generico item (coppia attributo-valore, per esempio Outlook=”Sunny”). </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
abstract class Item implements Serializable {
	
	private static final long serialVersionUID = 5243009330985665716L;
	/** 
	 * Attributo coinvolto nell'item.
	 */
	private Attribute attribute;
	/**
	 * Valore assegnato all'attributo.
	 */
	private Object value;
	
	/**
	 * Inizializza i valori dei membri.
	 * @param attribute valore usato per inizializzare l'attributo.
	 * @param value valore usato per inizializzare il valore.
	 */
	Item(Attribute attribute, Object value){
		this.attribute=attribute;
		this.value=value;
	}

	/**
	 * Restituisce l'attributo coinvolto nell'item.
	 * @return un Attribute rappresentante l'attributo coinvolto nell'Item.
	 */
	Attribute getAttribute(){
		return attribute;
	}
	
	/**
	 * Restituisce value (valore assegnato all'attributo).
	 * @return un Object rappresentante il valore assegnato all'attributo.
	 */
	Object getValue(){
		return value;
	}
	
	/**
	 * Restituisce una stringa rappresentante lo stato di value.
	 * @return una stringa rappresentante lo stato di value.
	 */
	public String toString(){
		return value.toString();
	}
	
	/**
	 * Metodo che ha implementazione diversa per Item discreto e Item continuo.
	 * @param a oggetto da cui si vuole calcolare la distanza.
	 * @return un double rappresentante la distanza.
	 */
	abstract double distance(Object a);
}
