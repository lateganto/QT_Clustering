package mining;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import data.Data;
import data.Tuple;

/**
 * <p> Title: Cluster </p>
 * <p> Class description: modella un cluster (si individuano gruppi con elementi omogenei 
 * 						  all'interno del gruppo e diversi da gruppo a gruppo). </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
class Cluster implements Iterable<Integer>, Comparable<Cluster>, Serializable {
	
	private static final long serialVersionUID = 3998359756241982723L;
	/**
	 * Elemento significativo di ogni cluster rappresentato come un array di Item.
	 */
	private Tuple centroid;
	/**
	 * Insieme di interi rappresentato con un set di Integer.
	 */
	private Set<Integer> clusteredData; 
	
	/**
	 * Inizializza centroid con il valore passato in input e clusteredData creando un Set di Integer vuoto.
	 * @param centroid valore per inizializzare il centroide.
	 */
	Cluster(Tuple centroid){
		this.centroid = centroid;
		clusteredData = new HashSet<Integer>();
	}
	
	/**
	 * Restituisce il centroide (elemento significativo di ogni cluster).
	 * @return un oggetto di tipo Tuple (centroid).
	 */
	Tuple getCentroid(){
		return centroid;
	}
	
	/**
	 * Restituisce vero se l'intero passato per argomento è stato aggiunto a clusteredData, altrimenti falso.
	 * @param id intero che deve essere aggiunto a clusteredData.
	 * @return un booleano che rappresenta se clusteredData è stato modificato o no.
	 */
	boolean addData(int id){
		return clusteredData.add(id);
	}
	
	
	/**
	 * Verifica se l'intero in input è presente in clusteredData.
	 * @param id intero di cui deve essere verificata la presenza in clusteredData.
	 * @return un booleano: true se l'intero id è presente in clusteredData, falso altrimenti.
	 */
	boolean contain(int id){
		return clusteredData.contains(id);
	}
	
	
	/**
	 * Rimuove l'intero passato in input da clusteredData.
	 * @param id intero da rimuovere in clusteredData.
	 */
	void removeTuple(int id){
		clusteredData.remove(id);
	}
	
	/**
	 * Restituisce il numero di interi memorizzati in clusteredData.
	 * @return un intero indicante la dimensione di clusteredData.
	 */
	int  getSize(){
		return clusteredData.size();
	}
	
	/**
	 * Restituisce un iteratore per il contenitore clusteredData (set di Integer).
	 * @return un iteratore per scorrere gli elementi del contenitore.
	 */
	@Override
	public Iterator<Integer> iterator() {
		return clusteredData.iterator();
	}
	
	/**
	 * Costruisce e restituisce una stringa costituita da tutti i centroidi.
	 * @return una stringa costituita dagli Item in posizione i di centroid.
	 */
	public String toString(){
		String str="Centroid = ( ";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i) + " ";
		str+=")";
		return str;
		
	}
	
	/**
	 * Costruisce e restituisce una stringa costituita dai centroidi, dalle tuple del parametro di tipo Data passato 
	 * in input e, per ogni tupla, restituisce la distanza dal centroide di ogni riga di data. Infine aggiunge alla 
	 * stringa la media delle distanze tra il centroide e le tuple ottenibili dalle righe della matrice in "data" 
	 * aventi indice in clusteredData.
	 * @param data oggetto da cui ricavare gli elementi, la distanza dal centroide e la distanza media.
	 * @return una stringa contenente tutte le informazioni.
	 */
	public String toString(Data data){
		String str="Centroid = (";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i)+ " ";
		str+=")\nExamples:\n";
		
		for(Integer A:clusteredData){
			str+="[";
			for(int j=0; j<data.getNumberOfExplanatoryAttributes(); j++)
				str+=data.getAttributeValue(A, j)+" ";
			str+="] dist="+getCentroid().getDistance(data.getItemSet(A))+"\n";
		}
		
		
		str+="\nAvgD="+getCentroid().avgDistance(data, clusteredData)+"\n";
		return str;
		
	}
	
	/**
	 * Il comparatore confronta due cluster in base alla popolosità restituendo -1 oppure +1.
	 * @param o Cluster usato per effettuare il confronto.
	 * @return un intero pari a -1 se il Cluster su cui è chiamato è minore del Cluster passato 
	 * 		   per argomento, altrimenti 1.
	 */
	public int compareTo(Cluster o) {
		//return +1 or -1
		return(this.getSize() > o.getSize() ? 1 : -1);
	}	
	
	/**
	 * Crea un HashMap contenente come chiavi le tuple sotto forma di stringa presenti nel cluster
	 * e come valori la loro distanza dal centroide del cluster.
	 * @param data oggetto da cui ricavare le tuple presenti nel cluster e la loro distanza dal centroide.
	 * @return un HashMap contenente come chiavi le tuple del cluster e come valori le loro distanze dal centroide.
	 */
	HashMap<String, Double> getClusterData(Data data){
		
		HashMap<String, Double> clusterData = new HashMap<String, Double>();
		
		for(Integer A:clusteredData){		
			clusterData.put(data.getItemSet(A).toString(), getCentroid().getDistance(data.getItemSet(A)));
		}
		
		return clusterData;	
	}	

	/**
	 * Calcola la distanza media degli elementi presenti nel Cluster dal centroide
	 * @param data oggetto da cui ricavare le tuple presenti nel cluster e la loro distanza dal centroide.
	 * @return un double rappresentante la distanza media delle tuple dal centroide.
	 */
	double avgDistance(Data data){
		return getCentroid().avgDistance(data, clusteredData);
	}	

}
