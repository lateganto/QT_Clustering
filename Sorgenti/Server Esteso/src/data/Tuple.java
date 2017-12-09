package data;

import java.io.Serializable;
import java.util.Set;


/**
 * <p> Title: Tuple </p>
 * <p> Class description: rappresenta una tupla come sequenza (array) di coppie attributo-valore. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class Tuple implements Serializable {
	
	private static final long serialVersionUID = -5437307435988981689L;
	/**
	 * Sequenza (array) di Item (coppie attributo-valore).
	 */
	private Item[] tuple;
	
	/**
	 * Costruisce l'oggetto riferito da tuple.
	 * @param size numero di Item (coppia attributo-valore) che costituiranno la tupla.
	 */
	Tuple(int size) {
		tuple = new Item[size];
	}
	
	/**
	 * Restituisce la lunghezza della tupla.
	 * @return un intero rappresentante la lunghezza della tupla.
	 */
	public int getLength() {
		return tuple.length;
	}
	
	/**
	 * Restituisce l'Item di tuple nella posizione specificata.
	 * @param i posizione dello item.
	 * @return lo Item (coppia attributo-valore) in posizione i di tuple.
	 */
	public Item get(int i) {
		return tuple[i];
	}
	
	/**
	 * Memorizza l'item passato per argomento nella sequenza di Item e nella posizone specificata.
	 * @param c item da memorizzare in tuple.
	 * @param i posizione in cui memorizzare l'Item all'interno tuple.
	 */
	public void add(Item c, int i) {
		tuple[i]=c;
	}
	
	/**
	 * Determina la distanza tra la tupla riferita da obj e la tupla corrente.
	 * La distanza è ottenuta come la somma delle distanze tra gli item in posizioni eguali nelle due tuple.
	 * @param obj tupla da cui calcolare la distanza.
	 * @return un double che rappresenta la somma delle distanze degli Item in posizioni uguali nelle due tuple (corrente e passata per argomento).
	 */
	public double getDistance(Tuple obj) {
	
		double distance=0.0;
		for(int i=0; i<getLength(); i++){
			distance += this.get(i).distance(obj.get(i).getValue());
		}
		return distance;
	}
	
	/**
	 * Restituisce la media delle distanze tra la tupla corrente e quelle ottenibili 
	 * dalle transazioni in data aventi indice in clusteredData.
	 * @param data oggetto di tipo Data che rappresenta l'insieme di transazioni (o tuple).
	 * @param clusteredData set di interi i cui elementi sono usati come indice per calcolare la media delle distanze riga per riga. 
	 * @return un double rappresentante la media delle distanze tra la tupla corrente e quelle ottenibili dalle 
	 * 		   righe della matrice data (in input) aventi indice in clusteredData. 
	 */
	public double avgDistance(Data data, Set<Integer> clusteredData) {
		
		double p=0.0, sumD=0.0;
		for(Integer A:clusteredData) {
			double d = getDistance(data.getItemSet(A));
			sumD += d;
		}
		p=sumD/clusteredData.size();
		
		return p;
	}
	
	
	/**
	 * Restituisce una stringa contenente le informazioni relative alla tupla.
	 * @return una stringa contenente le informazioni relative alla tupla.
	 */
	public String toString(){
		
		String result="";
		
		for(Item i:tuple)
			result += i + " ";
			
		return result;
	}

}
