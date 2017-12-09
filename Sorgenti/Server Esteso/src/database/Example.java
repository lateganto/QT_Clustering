package database;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Title: Example </p>
 * <p> Class description: modella una transazione letta dalla base di dati. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class Example implements Comparable<Example> {
	
	/**
	 * Lista di oggetti, dove ogni oggetto rappresenta un elemento della transazione del DB considerato.
	 */
	private List<Object> example = new ArrayList<Object>();

	/**
	 * Permette di aggiungere un nuovo elemento (passato per argomento) alla lista di oggetti.
	 * @param o oggetto che deve essere aggiunto.
	 */
	public void add(Object o) {
		example.add(o);
	}
	
	/**
	 * Restituisce l'oggetto del List nella posizione specificata.
	 * @param i indice per l'estrazione dell'oggetto.
	 * @return un oggetto di example nella posizione specificata.
	 */
	public Object get(int i) {
		return example.get(i);
	}
	
	/**
	 * Implementazione del metodo compareTo dell'interfaccia Comparable per il confronto di due istanze di Example 
	 * (locale e quella passata per argomento).
	 * @param ex un Example con cui effettuare il confronto.
	 * @return un intero: -1 se l'oggetto su cui è chiamato è minore di quello passato per argomento, 0 se sono uguali, 1 altrimenti.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compareTo(Example ex) {
		
		int i=0;
		for(Object o:ex.example){
			if(!o.equals(this.example.get(i)))
				return ((Comparable)o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}
	
	/**
	 * Restituisce una stringa rappresentante lo stato dell'oggetto.
	 * @return una stringa con lo stato dell'oggetto.
	 */
	public String toString() {
		String str="";
		for(Object o:example)
			str+=o.toString() + " ";
		return str;
	}
	
}