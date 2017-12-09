package server;

import java.net.Socket;
import java.util.HashMap;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import data.Data;
import mining.QTMiner;

/**
 * <p> Title: ServeOneClient </p>
 * <p> Class description: classe che estende la classe Thread e che permette di gestire le richieste del Client. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class ServerOneClient extends Thread {
	/**
	 * Riferimento al socket del Client.
	 */
	private Socket socket;
	/**
	 * Riferimento allo stream di input contenuto nella socket passata al costruttore.
	 */
	private ObjectInputStream in;
	/**
	 * Riferimento allo stream di output contenuto nella socket passata al costruttore.
	 */
	private ObjectOutputStream out; 
	/**
	 * Riferimento a un oggetto di tipo QTMiner che contiene informazioni sul ClusterSet e sul raggio.
	 */
	private QTMiner qtminer;
	/**
	 * ServerLog per la gestione dei messaggi di log del Server.
	 */
	private ServerLog log;

	/**
	 * Costruttore di classe che inizializza gli attributi, socket con il valore passato per argomento e inizializza gli stream 
	 * di input e output. Avvia il thread chiamando start().
	 * @param s socket per inizializzare il riferimento al socket del Client.
	 * @throws IOException in caso di fallimento o interruzione di operazioni di I/O.
	 */
	public ServerOneClient(Socket s) throws IOException{
		socket = s;
		
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		
		start();
	}
	
	/**
	 * Riscrive il metodo run() della superclasse Thread al fine di gestire le richieste del client.
	 */
	public void run() {
		
		log = new ServerLog();
		
		String tabName = "";
		Data data = null;
		double radius = 0.0;
		
		try {
			
			do {
				int request = (int) in.readObject();
				
				switch(request) {
				case 0:
					try {
						tabName = (String) in.readObject();
						data = new Data(tabName);
						
						out.writeObject("OK");
						
					} catch(Exception e) {
						out.writeObject(e.getMessage());
					}
				
					break;
				
				case 1:
					try {
						radius = (double) in.readObject();

						qtminer = new QTMiner(radius);
						int numC = qtminer.compute(data);
						String clusterSet = qtminer.getC().toString(data);

						out.writeObject("OK");
						out.writeObject(numC);
						out.writeObject(clusterSet);
				
					} catch(Exception e) {
						out.writeObject(e.getMessage());
					}
					break;
			
				case 2:
					try {
						qtminer.salva(tabName+radius+".dmp");
						
						out.writeObject("OK");
					
					} catch(Exception e) {
						out.writeObject(e.getMessage());
					}
					break;
				
				case 3:
					try {
						String fName = (String) in.readObject();
						double r = (double) in.readObject();
					
						QTMiner qtFile = new QTMiner(fName+r+".dmp");
					
						out.writeObject("OK");
						out.writeObject(qtFile.toString());
					
					} catch(Exception e) {
						out.writeObject(e.getMessage());
					}
					break;
					
				case 4:
					try {
						radius = (double) in.readObject();

						qtminer = new QTMiner(radius);
						qtminer.compute(data);
						
						HashMap<String, HashMap<String, Double>> computedData = 
								qtminer.getC().getComputedData(data);

						out.writeObject("OK");
						out.writeObject(computedData);
					
					} catch(Exception e) {
						out.writeObject(e.getMessage());
					}
					break;
					
				}
				
			} while(true);
			
		} catch (IOException | ClassNotFoundException e) {
			log.refreshLog(" " + this.getName()+" - "+e.getMessage());
		} finally {
			try {
				socket.close();
			} catch(IOException e) {
				log.refreshLog(" " + this.getName()+" - "+e.getMessage());
			}
		}
	}	

}
