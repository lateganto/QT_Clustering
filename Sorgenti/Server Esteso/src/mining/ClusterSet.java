package mining;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import data.Data;

/**
 * <p> Title: ClusterSet </p>
 * <p> Class description: rappresenta un insieme di cluster (determinati da QT) </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class ClusterSet implements Iterable<Cluster>, Serializable {
	
	private static final long serialVersionUID = -7584671043257017816L;
	/**
	 * Insieme ordinato (set) di Cluster modellato come TreeSet.
	 */
	private Set<Cluster> C = new TreeSet<Cluster>();
	
	/**
	 * Aggiunge il Cluster passato in input al membro C (set di Cluster).
	 * @param c Cluster che deve essere aggiunto al Set di Cluster (C).
	 */
	void add(Cluster c) {
		
		C.add(c);	
	}
	
	/**
	 * Restituisce una stringa costituita da ciascun centroide per ogni elemento dell'insieme dei Cluster.
	 * @return una stringa costituita da ciascun centroide per ogni elemento dell'insieme dei Cluster.
	 */
	public String toString() {
		
		String result = "";
		int i = 1;
		for(Cluster A:C){
			result+= i + ":" + A.toString() + "\n";
			i++;
		}
		
		return result;
	}
	
	/**
	 * Restituisce una stringa che descrive lo stato di ciascun Cluster in C.
	 * @param data oggetto di tipo Data da cui calcolare ricavare gli elementi, la distanza dal centroide e la distanza media.
	 * @return una stringa che descrive lo stato di ciascun Cluster in C.
	 */
	public String toString(Data data) {
		
		String str="";
		int i = 1;
		for(Cluster A:C){
			if(A!=null){
				str+= i +": "+A.toString(data)+"\n";
			}
			i++;
		}
		
		return str;
	}
	
	/**
	 * Restituisce un iteratore per il contenitore C (Set di Cluster).
	 * @return un iteratore per scorrere gli elementi del contenitore.
	 */
	@Override
	public Iterator<Cluster> iterator() {
		return C.iterator();
	}
	
	
	/**
	 * Costruisce un HashMap contenente come chiavi i centroidi dei cluster e la distanza media delle tuple presenti nel cluster
	 * sotto forma di stringa e come valori un HashMap contenente le informazioni delle tuple presenti in ogni cluster.
	 * @param data oggetto da cui ricavare i cluster e le informazioni delle rispettive tuple al loro interno.
	 * @return un HashMap contenente come chiavi le informazioni dei centroidi dei cluster e come valori le informazioni delle
	 * 		   tuple nei rispettivi cluster.
	 */
	public HashMap<String, HashMap<String, Double>> getComputedData(Data data) {
		
		HashMap<String, HashMap<String, Double>> computedData = 
				new HashMap<String, HashMap<String, Double>>();

		for(Cluster A:C){
			if(A!=null){
				computedData.put(A.getCentroid().toString() + "\n AvgD=" 
						+ A.avgDistance(data), A.getClusterData(data));
			}
		}
		
		return computedData;
	}
	
}
