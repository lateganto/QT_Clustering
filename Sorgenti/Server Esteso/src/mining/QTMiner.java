package mining;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import data.Data;

/**
 * <p> Title: QTMiner </p>
 * <p> Class description: include l'implementazione dell'algoritmo QT. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class QTMiner {
	/**
	 * Set di Cluster.
	 */
	private ClusterSet C;
	/**
	 * Raggio del cluster.
	 */
	private double radius;
	
	/**
	 * Apre il file identificato da fileName, legge l'oggetto ivi memorizzato e lo assegna a C.
	 * @param fileName include il percorso e il nome del file da cui leggere per deserializzare l'oggetto.
	 * @throws FileNotFoundException in caso di file non trovato.
	 * @throws IOException in caso di fallimento o interruzione di una operazione di I/O.
	 * @throws ClassNotFoundException nel caso in cui la deserializzazione dell'oggetto richieda di dover caricare altre classi.
	 */
	public QTMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		
		ObjectInputStream inStream = new ObjectInputStream(new FileInputStream("computed/" + fileName));
		
		C = (ClusterSet) inStream.readObject();
		inStream.close();
	}
	
	/**
	 * Crea l'oggetto array riferito da C e inizializza radius con il parametro passato come input.
	 * @param radius valore con cui inizializzare il membro radius.
	 */
	public QTMiner(double radius) {
		C = new ClusterSet();
		this.radius = radius;
	}
	
	/**
	 * Restituisce il ClusterSet C.
	 * @return un ClusterSet (Set di Cluster).
	 */
	public ClusterSet getC() {
		return C;
	}
	
	/**
	 * Esegue l'algoritmo QT eseguendo i passi:
	 * 1. Costruisce un Cluster per ciascuna tupla non ancora clusterizzata, includendo nel Cluster i punti (non ancora 
	 * 	  clusterizzati in alcun altro Cluster) che ricadono nel vicinato sferico della tupla avente raggio radius;
	 * 2. Salva il candidato cluster più popoloso e rimuove tutti punti di tale Cluster dall'elenco delle Tuple ancora da clusterizzare;
	 * 3. Ritorna al passo 1 finchè ci sono ancora tuple da assegnare ad un cluster.
	 * @param data insieme di transazioni (o Tuple).
	 * @return un intero che rappresenta il numero di cluster scoperti.
	 * @throws ClusteringRadiusException qualora l'algoritmo di clustering generi un solo cluster. 
	 */
	public int compute(Data data) throws ClusteringRadiusException {
		
		int numclusters = 0;
		boolean isClustered[] = new boolean[data.getNumberOfExamples()];
		
		for(int i = 0; i < isClustered.length; i++) {
			isClustered[i] = false;
		}
		
		int countClustered = 0;
		while(countClustered != data.getNumberOfExamples()) {
			//find the cluster most sized
			Cluster c = buildCandidateCluster(data, isClustered);
			C.add(c);
			numclusters++;
			
			//remove clustered tuples from data
			for(Integer i:c)
				isClustered[i]=true;
			
			countClustered += c.getSize();
		}
		
		if(numclusters!=1)
			return numclusters;
		else throw new ClusteringRadiusException("All " + countClustered + " tuples in only one Cluster!");
	}
	
	/**
	 * Costruisce un Cluster per ciascuna tupla di data non ancora clusterizzata in un cluster di C e restituisce il cluster 
	 * candidato più popoloso.
	 * @param data insieme di Tuple da raggruppare in cluster.
	 * @param isClustered array contentente lo stato di clusterizzazione di una tupla (per esempio isClustered[i]=false se 
	 * 					  la tupla i-esima di data non è ancora assegnata ad alcun cluster di C, true altrimenti).
	 * @return il Cluster candidato.
	 */
	Cluster buildCandidateCluster(Data data, boolean isClustered[]) {
		
		Cluster clust = null;
		for(int i=0; i<isClustered.length; i++) {
			if(isClustered[i]==false) {	
				Cluster temp = new Cluster(data.getItemSet(i));
			
				for(int j=0; j<isClustered.length; j++) {
					if(isClustered[j]==false) {
						if(data.getItemSet(i).getDistance(data.getItemSet(j))<=radius) {
							temp.addData(j);
						}
					}	
				}
				
				if(clust == null)
					clust = temp;
				else if(temp.getSize() > clust.getSize())
					clust = temp;
			}	
		}
		
		return clust;
	}
	
	/**
	 * Permette di serializzare l'oggetto (il ClusterSet C).
	 * @param fileName include il percorso e il nome del file in cui scrivere per serializzare l'oggetto.
	 * @throws FileNotFoundException in caso di file non trovato.
	 * @throws IOException in caso di fallimento o interruzione di una operazione di I/O.
	 */
	public void salva(String fileName) throws FileNotFoundException, IOException {
		
		File computedDirectory = new File("computed");
			
		if (!computedDirectory.exists())
			computedDirectory.mkdirs();
		
		ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream("computed/" + fileName));
		outStream.writeObject(C);
		outStream.close();
	}
	
	/**
	 * Restituisce una stringa costituita da ciascun centroide per ogni elemento dell'insieme dei Cluster.
	 * @return una stringa costituita da ciascun centroide per ogni elemento dell'insieme dei Cluster.
	 */
	public String toString() {
		return C.toString();
	}
	
}
