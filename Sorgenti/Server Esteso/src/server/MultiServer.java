package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p> Title: MultiServer </p>
 * <p> Class description: Server che crea un singolo ServerSocket e chiama accept per attendere una connessione. Non appena la connessione
 * 						  è attiva si utilizza il Socket ottenuto in un nuovo Thread per servire un particolare Client. Il thread principale, 
 * 						  intanto, richiamerà accept() per attendere un nuovo client. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class MultiServer {
	
	/**
	 * Numero di porta.
	 */
	private int port;
	/**
	 * ServerLog per la gestione dei messaggi di log del Server.
	 */
	private ServerLog log;
	
	/**
	 * Costruttore che inizializza il valore della porta e invoca run().
	 */
	public MultiServer() {
		port = 8080;
		run();
	}
	
	/**
	 * Costruttore che inizializza la porta con il valore passato per parametro e invoca run().
	 * @param port intero utilizzato per inizializzare la porta. 
	 */
	public MultiServer(int port) {
		this.port = port;
		run();
	}

	/**
	 * Istanzia un oggetto istanza della classe ServerSocket che pone in attesa di richiesta di connessioni da parte del Client. 
	 * Ad ogni nuova richiesta connessione si istanzia ServerOneClient passandogli il socket ottenuto.
	 */
	private void run() {
		log = new ServerLog();
		try {
			ServerSocket serversocket = new ServerSocket(port);
			
			log.refreshLog(" / Server started: " + serversocket);
			System.out.println("Server started");
			
			try {
				while(true) {
					log.refreshLog(" / Waiting for connection...");
					Socket socket = serversocket.accept();
					
					try {
						log.refreshLog(" / Connection accepted on " + socket.getInetAddress() + " on Socket Port " + socket.getPort());
						
						new ServerOneClient(socket);
						
					} catch (IOException e) {	
						log.refreshLog(" / Accept failed on " + socket.getInetAddress() + " on Socket Port " + socket.getPort());
						socket.close();
					
					}
				}
				
			} finally {
				log.refreshLog(" / Server Closing...");
				serversocket.close();
			}
			
        } catch (IOException ex) {
            System.out.println("Error: unable to listen on port " + port);
            log.refreshLog(" / Unable to listen on port " + port);
            
        }
		System.out.println("Server Closed");
        log.refreshLog(" / Server Closed");
	}
	
	/**
	 * Metodo main che chiama instanzia un oggetto della classe settando la porta.
	 * @param args array di argomenti del main.
	 */
	public static void main(String[] args) {
		new MultiServer(Integer.parseInt(args[0]));
	}
	
}
