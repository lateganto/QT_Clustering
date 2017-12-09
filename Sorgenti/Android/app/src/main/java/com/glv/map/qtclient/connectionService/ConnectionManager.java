package com.glv.map.qtclient.connectionService;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.glv.map.qtclient.MainActivity;
import com.glv.map.qtclient.R;
import com.glv.map.qtclient.activityService.ClusterSetResultActivity;
import com.glv.map.qtclient.activityService.TextResultActivity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * <p> Title: ConnectionManager </p>
 * <p> Class description: rappresenta la classe utilizzata per gestire le operazioni di rete,
 *                        connessione, lettura e scrittura dal server al dispositivo e viceversa.
 *                        Contiene inner classi che implementano l'interfaccia Runnable e estendono
 *                        AsyncTask per gestire le operazioni di rete in Thread separati
 *                        dall'interfaccia utente (UI Thread). </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class ConnectionManager {
    /**
     * Utilizzato nell'applicazione per conoscere lo stato della connessione con il Server.
     */
    public static Boolean connectedToServer;
    /**
     * Rappresenta il Socket per la connessione con il Server.
     */
    private Socket clientSocket;
    /**
     * Rappresenta lo stream in ingresso per la ricezione dei dati dal Server.
     */
    private ObjectInputStream in;
    /**
     * Rappresenta lo stream in uscita per l'invio dei dati al Server.
     */
    private ObjectOutputStream out;
    /**
     * Rappresenta l'Activity dalla quale è stata richiamata un'istanza di una delle inner classe.
     * Utile in particolare per definire il contesto su cui devono operare i Toast.
     */
    private Activity workingActivity;

    /**
     * Rappresenta il costruttore della classe.
     * Inizializza il flag dello stato della connessione con il Server, e setta a null lo stato del
     * clientScoket, in e out.
     */
    public ConnectionManager() {
        connectedToServer = false;
        clientSocket = null;
        in = null;
        out = null;
    }

    /**
     * Metodo utilizzato per conoscere lo stato del clientSocket. Utile nel caso si voglia
     * effettuare una nuova connessione; in questo caso si potrà gestire il socket effettuando
     * prima la sua chiusura e poi una nuova apertura su una nuova connessione.
     * @return true se il clientSocket è connesso, false altrimenti
     */
    public Boolean getClientSocketStatus() {
        return(clientSocket!=null);
    }


    /**
     * <p> Title: CloseConnection </p>
     * <p> Class description: Viene istanziata per l'esecuzione di un Thread che si occupa di
     *                        chiudere la connessione con il Server.
     *                        Viene controllato lo stato del clientSocket, di out e in e si tenta
     *                        la loro chiusura.</p>
     * @author Gentile, Lategano, Visaggi
     *
     */
    public class CloseConnection implements  Runnable {

        /**
         * Rappresenta il costruttore della classe.
         * Riceve in input l'Activity utile per decretare il contesto dove mostrare un
         * possibile messaggio di errore con Toast.
         * Cambia o inizializza l'attributo activity della classe contenitore.
         * @param activity activity alla quale fare riferimento per i messaggi con Toast.
         */
        public CloseConnection(Activity activity) {
            workingActivity = activity;
        }

        /**
         * Rappresenta il metodo principale della classe.
         * Quando il Thread creato con un istanza della classe viene avviato, viene richiamato.
         * Tenta di chiudere il clientSocket, l'in stream e l'out stream se sono aperti.
         * Se dovesse essere sollevata un'eccezione, viene eseguito nell'UI Thread un Thread che
         * mostra un Toast con il messaggio dell'eccezione sollevata.
         */
        @Override
        public void run() {
            try {
                if (clientSocket != null)
                    clientSocket.close();
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                workingActivity.runOnUiThread(
                        new ShowToast(workingActivity,
                                workingActivity.getString(R.string.closing_connection_error)));
            }
        }
    }

    /**
     * <p> Title: ConnectToServer </p>
     * <p> Class description: Rappresenta la classe istanziata per l'esecuzione di un Thread per la
     *                        connessione con il Server.</p>
     * @author Gentile, Lategano, Visaggi
     *
     */
    public class ConnectToServer implements Runnable {
        /**
         * Rappresenta l'indirizzo IP oppure l'host al quale connettersi.
         */
        private String ip;
        /**
         * Rappresenta la porta utilizzata dal server per comunicare con l'applicazione.
         */
        private int port;

        /**
         * Rappresenta il costruttore della classe.
         * Inizializza gli attributi della classe e l'attributo della classe contenitore activity.
         * @param ip l'indirizzo IP o l'host sul quale si trova il server.
         * @param port la porta sul server a cui collegarsi.
         * @param activity l'activity di esecuzione della classe (utile per eseguire correttamente
         *                 i Toast senza rischiare di bloccare l'UI Thread).
         */
        public ConnectToServer(String ip, int port, Activity activity) {
            this.ip = ip;
            this.port = port;
            workingActivity = activity;
        }

        /**
         * Rappresenta il metodo principale della classe.
         * Viene richiamato all'avvio del Thread.
         * Tenta di connettersi al server inizializzando il clientSocket, in e out.
         * Se la connessione va a buon fine, viene mostrato un Toast di notifica di avvennuta
         * connessione.
         * In caso contrario, viene segnalato l'errore con un Toast.
         */
        @Override
        public void run() {

            try {
                InetAddress addr = InetAddress.getByName(ip);
                clientSocket = new Socket(addr, port);

                in = new ObjectInputStream(clientSocket.getInputStream());
                out = new ObjectOutputStream(clientSocket.getOutputStream());

                connectedToServer = true;
                showConnectToast(workingActivity.getString(R.string.connected_server_ok));

            } catch (UnknownHostException e) {
                showConnectToast(e.getMessage());
            } catch (IOException e) {
                showConnectToast(e.getMessage());
            }
        }

        /**
         * Viene richiamato per mostrare un Toast per segnalare errori o messaggi di notifiche.
         * @param msg messaggio da mostrare con Toast.
         */
        private void showConnectToast(final String msg) {

            workingActivity.runOnUiThread(new ShowToast(workingActivity, msg));
        }
    }


    /**
     * <p> Title: retryConnection </p>
     * <p> Class description: rappresenta la classe istanziara per l'avvio di un thread che
     *                        tenta una riconnessione.</p>
     * @author Gentile, Lategano, Visaggi
     *
     */
    public class RetryConnection implements Runnable {
        /**
         * Rappresenta l'indirizzo IP o l'host su cui effettuare la riconnessione.
         */
        private String ip;
        /**
         * Rappresenta la porta sulla quale il Server è in ascolto.
         */
        private int port;

        /**
         * Costruttore della classe.
         * Inizializza gli attributi della classe e l'attributo activity della classe contenitore.
         * @param ip indirizzo dove è localizzato il server.
         * @param port porta usata dal server per comunicare.
         * @param activity activity di base per determinare il contesto in cui mostrare possibili Toast.
         */
        public RetryConnection(String ip, int port, Activity activity) {
            this.ip = ip;
            this.port = port;
            workingActivity = activity;
        }

        /**
         * Metodo principale della classe.
         * Viene richiamato all'avvio del Thread.
         * Controlla che gli in e out siano nulli, in caso contrario, prima di effettuare una nuova
         * connessione, tenta di chiuderli.
         * Controlla che il clientSocket non sia nullo per effettuare la sua chiusura.
         * Tenta la nuova connessione ad un server.
         * In caso di errori, crea ed inserisce un nuovo Thread nella coda di esecuzione del
         * UI Thread per l'esecuzione di un Toast con relativo messaggio di errore.
         */
        @Override
        public void run() {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
                clientSocket.close();

                connectedToServer = false;

                new Thread(new ConnectToServer(ip, port, workingActivity)).start();

            } catch (IOException e) {

                final String msg = e.getMessage();

                workingActivity.runOnUiThread(new ShowToast(workingActivity, msg));
            }
        }

    }



    /**
     * <p> Title: LearningFromDb </p>
     * <p> Class description: rappresenta una classe che estende AsyncTask per l'esecuzione in
     *                        background delle operazioni di ricezione dei dati presenti sul db dal server.
     *                        Riceve in input parametri di tipo Stringa e restuisce come risultato
     *                        della sua esecuzione la struttura dati dei dati computati dal Server.</p>
     * @author Gentile, Lategano, Visaggi
     *
     */
    public class LearningFromDb extends AsyncTask
            <String, Void, HashMap<String, HashMap<String, Double>>> {

        /**
         * Rappresenta il costruttore della classe.
         * Inizializza l'attributo workingActivity della classe contenitore.
         * @param activity activity da cui ricavare il contesto per l'esecuzione di Toasts.
         */
        public LearningFromDb(Activity activity) {
            workingActivity = activity;
        }


        /**
         * Rappresenta il metodo di esecuzione principale della classe.
         * Riceve in input parametri di tipo stringa, ovvero, il nome della tabella e il raggio.
         * Richiama il metodo getDataServer() per la ricezione dei dati dal Server.
         * Mostra un Toast se si verifica un errore, restituendo null al metodo onPostExecute().
         * @param params array dei dati in input: nome tabella e raggio.
         * @return un HashMap con i dati ricevuti dal Server.
         */
        @Override
        protected HashMap<String, HashMap<String, Double>> doInBackground(String... params) {
            try {
                return getDataServer(params[0], params[1]);
            } catch (IOException | ClassNotFoundException | ServerException e) {
                workingActivity.runOnUiThread(new ShowToast(workingActivity, e.getMessage()));
            }

            return null;
        }

        /**
         * Rappresenta il metodo che viene invocato dopo l'esecuzione del metodo principale della
         * classe. Riceve in input la struttura dati ricevuta dal server.
         * Ha il compito di comunicare con l'Activity dalla quale è stato avviato l'asyncTask
         * passandogli i dati elaborati in background.
         * Se i dati ricevuti dal server sono nulli, viene mostrato un Toast per segnalare l'errore.
         * @param computedData dati ricevuti dal Server.
         */
        @Override
        protected void onPostExecute(HashMap<String, HashMap<String, Double>> computedData) {

            if (computedData != null) {
                Intent intent = new Intent(workingActivity, ClusterSetResultActivity.class);
                intent.putExtra(MainActivity.COMPUTED_DATA, computedData);
                workingActivity.startActivity(intent);
            }

        }

        /**
         * Viene richiamato per l'esecuzione delle richieste al server. In caso di errori solleva
         * delle eccezioni.
         * Comunica con il Server e resta in attesa per la ricezione di responsi positivi per
         * continuare la sua esecuzione fino all'avvenuta ricezione dei dati.
         * Se riceve un responso negativo solleva una ServerException con il relativo meassaggio.
         * @param table nome della tabella da comunicare al server per la computazione.
         * @param radius valore del raggio da comunicare al server per la computazione.
         * @return la struttura dati computata e ricevuta dal server.
         * @throws IOException in caso di fallimento o interruzione di una operazione di I/O.
         * @throws ClassNotFoundException nel caso in cui la deserializzazione dell'oggetto richieda
         *                                di dover caricare altre classi non trovate.
         * @throws ServerException nel caso in cui il response del Server è diverso dal risultato atteso.
         */
        private HashMap<String, HashMap<String, Double>> getDataServer(String table, String radius)
                throws IOException, ClassNotFoundException, ServerException {

            out.writeObject(0);
            out.writeObject(table);

            String response = (String) in.readObject();
            HashMap<String, HashMap<String, Double>> computedData;

            if (!(response.equals("OK"))) {
                throw new ServerException(response);
            } else {
                out.writeObject(4);
                out.writeObject(Double.parseDouble(radius));

                response = (String) in.readObject();

                if (!(response.equals("OK"))) {
                    throw new ServerException(response);
                } else {

                    computedData = (HashMap<String, HashMap<String, Double>>) in.readObject();

                    out.writeObject(2);
                    response = (String) in.readObject();

                    if (!(response.equals("OK"))) {
                        throw new ServerException(response);
                    }
                }
            }
            return computedData;
        }

    }


    /**
     * <p> Title: LearningFromFile </p>
     * <p> Class description: rappresenta una classe che estende AsyncTask per l'esecuzione in
     *                        background delle operazioni di ricezione dei dati presenti su un File dal server.
     *                        Riceve in input parametri di tipo Stringa e restuisce come risultato della sua esecuzione
     *                        la stringa dei dati computati dal server e contenuti nel file.</p>
     * @author Gentile, Lategano, Visaggi
     *
     */
    public class LearningFromFile extends AsyncTask <String, Void, String> {

        /**
         * Rappresenta il costruttore della classe.
         * Inizializza l'attributo workingActivity della classe contenitore.
         * @param activity activity da cui ricavare il contesto per l'esecuzione di Toasts.
         */
        public LearningFromFile(Activity activity) {
            workingActivity = activity;
        }


        /**
         * Rappresenta il metodo di esecuzione principale della classe.
         * Riceve in input parametri di tipo stringa, ovvero, il nome della tabella e il raggio.
         * Richiama il metodo getFileDataServer per la ricezione dei dati dal server.
         * Mostra un Toast se si verifica un errore, restituendo null al metodo onPostExecute().
         * @param params array dei dati in input: nome tabella e raggio.
         * @return stringa dei dati contenuti nel file sul server.
         */
        @Override
        protected String doInBackground(String... params) {
            try {
                return getFileDataServer(params[0], params[1]);
            } catch (IOException | ClassNotFoundException | ServerException e) {
                workingActivity.runOnUiThread(new ShowToast(workingActivity, e.getMessage()));
            }

            return null;
        }

        /**
         * Rappresenta il metodo che viene invocato dopo l'esecuzione del metodo principale della
         * classe. Riceve in input la stringa dei dati ricevuta dal server.
         * Ha il compito di comunicare con l'activity dalla quale è stato avviato l'asyncTask
         * passandogli i dati elaborati in background.
         * Se i dati ricevuti dal server sono nulli, viene mostrato un Toast per segnalare l'errore.
         * @param computedData dati in formato String ricevuti dal Server.
         */
        @Override
        protected void onPostExecute(String computedData) {

            if (computedData != null) {
                Intent intent = new Intent(workingActivity, TextResultActivity.class);
                intent.putExtra(MainActivity.COMPUTED_TEXT_DATA, computedData);
                workingActivity.startActivity(intent);
            }
        }

        /**
         * Viene richiamato per l'esecuzione delle richieste al server. In caso di errori solleva
         * delle eccezioni.
         * Comunica con il server, e resta in attesa per la ricezione di responsi positivi per
         * continuare la sua esecuzione fino all'avvenuta ricezione dei dati.
         * Se riceve un responso negativo solleva una ServerException con il relativo messaggio.
         * @param table nome della tabella da comunicare al server per la computazione.
         * @param radius valore del raggio da comunicare al server per la computazione.
         * @return una stringa rappresentante i dati presenti nel file sul server.
         * @throws IOException in caso di fallimento o interruzione di una operazione di I/O.
         * @throws ClassNotFoundException nel caso in cui la deserializzazione dell'oggetto richieda
         *                                di dover caricare altre classi non trovate.
         * @throws ServerException nel caso in cui il response del Server è diverso dal risultato atteso.
         */
        private String getFileDataServer(String table, String radius)
                throws IOException, ClassNotFoundException, ServerException {

            out.writeObject(3);
            out.writeObject(table);
            out.writeObject(Double.parseDouble(radius));

            String response = (String)in.readObject();

            if(!(response.equals("OK"))){
                throw new ServerException(response);
            }
            else {
                return (String)in.readObject();
            }

        }

    }



}
