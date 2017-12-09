package com.glv.map.qtclient;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.glv.map.qtclient.activityService.SettingsActivity;
import com.glv.map.qtclient.connectionService.ConnectionManager;

/**
 * <p> Title: MainActivity </p>
 * <p> Class description: rappresenta la classe principale dell'applicazione.</p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class MainActivity extends ActionBarActivity {
    /**
     * Stringa utilizzata per il passaggio dei parametri tra la
     * Main Activity e la ClusterSet Result Activity.
     */
    public final static String COMPUTED_DATA = "Computed_Data";
    /**
     * Stringa utilizzata per il passaggio dei parametri tra
     * la Main Activity e la Text Result Activity.
     */
    public final static String COMPUTED_TEXT_DATA = "Computed_Text_Data";
    /**
     * Rappresenta l'EditText per l'inserimento del nome della tabella.
     */
    private EditText table_name;
    /**
     * Rappresenta l'EditText per l'inserimento del raggio.
     */
    private EditText radius;
    /**
     * Rappresenta l'indirizzo IP o l'host del Server al quale l'app deve connettersi.
     */
    private String ip;
    /**
     * Rappresenta la porta del Server sulla quale avviene la comunicazione tra l'app e il Server.
     */
    private int port;
    /**
     * Rappresenta lo stato della connessione Wifi del dispositivo.
     */
    private Boolean wifiConnected;
    /**
     * Rappresenta lo stato della connessione Mobile del dispositivo.
     */
    private Boolean mobileConnected;
    /**
     * Rappresenta il layout dell'Activity. Viene usato come parametro per mostrare una SnackBar.
     */
    private RelativeLayout relativeLayout;
    /**
     * Viene usato per gestire i Thread per la connessione e la comunicazione con il Server.
     */
    private ConnectionManager connManager;
    /**
     * Rappresenta il listener per il Button nel menu (menu_main) dell'Activity.
     */
    private View.OnClickListener retryConnectionListener;

    /**
     * Viene richiamato al momento della creazione dell'Activity.
     * Ha il compito di inizializzare le componenti grafiche e le variabili utili all'Activity.
     * Inizializza i flag per la connessione, ottiene le componenti grafiche dal corrispettivo file
     * xml, imposta il comportamento del listener per il button Reconnect nel menu dell'Activity.
     * @param savedInstanceState rappresenta lo stato dell'istanza salvata.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiConnected = false;
        mobileConnected = false;

        connManager = new ConnectionManager();

        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main_layout);
        table_name = (EditText) findViewById(R.id.table_editText);
        radius = (EditText) findViewById(R.id.radius_editText);

        retryConnectionListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryConnection();
            }
        };
    }

    /**
     * Viene richiamato subito dopo il metodo onCreate(), oppure quando viene ripresa l'attività
     * dell'Activity dopo il metodo onResume() della superclasse.
     * Ha il compito di avviare l'Activity.
     * Richiama il metodo onStart() della superclasse.
     * Ottiene le preferenze salvate durante l'ultima sessione, o, ne crea di nuove con valori di
     * default se non sono presenti.
     * Inizializza le variabili ip e port leggendo le preferenze.
     * Effettua un controllo sul valore della porta: se supera il range di porta consentito (65535)
     * mostra dei messaggi all'utente attraverso Toasts, e imposta il valore della variabile port
     * con il valore di default.
     * Se non è stata ancora stabilita alcuna connessione con il Server, tenta una nuova connessione
     * richiamando il metodo tryConnection().
     */
    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        ip = sharedPrefs.getString("ipPref", getString(R.string.default_ip));
        port = Integer.parseInt(sharedPrefs.getString("portPref", getString(R.string.default_port)));

        //aggiunto per crashing su porta oltre il valore specificato
        int defaultRangePort = Integer.parseInt(getString(R.string.default_port_range));
        if (port > defaultRangePort) {
            port = Integer.parseInt(getString(R.string.default_port));
            Toast.makeText(getBaseContext(), R.string.port_out_range_error,
                    Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), R.string.using_8080_error, Toast.LENGTH_SHORT).show();
        }

        if (!ConnectionManager.connectedToServer)
            tryConnection();

    }

    /**
     * Viene richiamato durante la creazione dell'Activity per creare il menu.
     * Richiama il metodo getMenuInflater che aggiunge le componenti del menu all'action bar, se sono
     * presenti, leggendole dal corrispettivo file xml.
     * @param menu usato per l'inizializzazione delle componenti dell'option menu dell'Activity.
     * @return true se va a buon fine, false altrimenti.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Viene richiamato quando l'Activity cessa di esistere.
     * In genere viene richiamato quando l'applicazione viene terminata.
     * Richiama onDestroy() della superclasse.
     * Tenta di chiudere la connessione se è attiva.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ConnectionManager.connectedToServer)
            connManager.new CloseConnection(MainActivity.this);
    }

    /**
     * Viene richiamato quando viene selezionato un componente dal menu nell'action bar.
     * Identifica l'id del componente selezionato, ed esegue le appropriate operazioni per la
     * selezione effettuata.
     * @param item componente selezionato nel menu dell'Activity.
     * @return true se va a buon fine, false altrimenti.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(settingsActivity);
            return true;
        } else if (id == R.id.action_reconnect) {
            tryConnection();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Viene richiamato per controllare lo stato della connessione del dispositivo.
     * Imposta i valori dei flag di connessione in base allo stato della connessione.
     */
    private void updateConnectedFlags() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();

        if (activeInfo != null && activeInfo.isConnected()) {

            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

        } else {

            wifiConnected = false;
            mobileConnected = false;
        }
    }

    /**
     * Viene richiamato per tentare una connessione al Server.
     * Aggiorna lo stato della connessione del dispositivo. Se il dispositivo non dispone di una
     * connessione dati, mostra una SnackBar per segnalare all'utente l'errore.
     * Altrimenti, verifica che lo stato del clientSocket non sia nullo; in questo caso crea e avvia
     * un nuovo Thread per l'avvio di una nuova connessione.
     * In caso contrario, crea e avvia un nuovo Thread che tenta prima di chiudere la connessione
     * con il Server, e poi ne avvia una nuova con i parametri in input.
     * Questo ultimo caso si verifica solitamente quando si seleziona Reconnect dal menu dell'Activity.
     */
    private void tryConnection() {

        updateConnectedFlags();

        if (wifiConnected || mobileConnected) {

            if (!connManager.getClientSocketStatus()) {
                new Thread(connManager.new ConnectToServer(ip, port, MainActivity.this)).start();
            } else {
                new Thread(connManager.new RetryConnection(ip, port, MainActivity.this)).start();
            }
        } else {
            Snackbar
                    .make(relativeLayout, R.string.no_connection_error, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_button, retryConnectionListener)
                    .show();
        }
    }

    /**
     * Viene richiamato quando si seleziona il Button appropriato (Mine).
     * Ha il compito di leggere i valori contenuti nella textEdit table e radius.
     * Viene effettuato un controllo sui valori letti, per verificare che non siano vuoti.
     * In caso contrario viene mostrato il corretto messaggio di errore, e si interrompe
     * l'esecuzione del metodo.
     * Se i valori letti non sono vuoti, viene controllato lo stato della connessione con il Server.
     * Se si è connessi, viene eseguito l'AsyncTask LearningFromDb, altrimenti viene mostrato un
     * Toast per segnalare l'errore.
     * @param view vista passata dal Button richiamante.
     */
    public void onClickMine(View view) {

        String a = table_name.getText().toString();
        String b = radius.getText().toString();

        if (a.isEmpty()) {
            table_name.setError(getString(R.string.table_blank_field_error));
            return;
        }

        if (b.isEmpty()) {
            radius.setError(getString(R.string.radius_blank_field_error));
            return;
        }

        if (ConnectionManager.connectedToServer) {
            connManager.new LearningFromDb(MainActivity.this).execute(a, b);
        } else Toast
                .makeText(getBaseContext(), R.string.unable_connect_error, Toast.LENGTH_SHORT)
                    .show();
    }

    /**
     * Viene richiamato quando si seleziona il Button appropriato (Store From File).
     * Ha il compito di leggere i valori contenuti nella textEdit table e radius.
     * Viene effettuato un controllo sui valori letti, per verificare che non siano vuoti.
     * In caso contrario viene mostrato il corretto messaggio di errore, e si interrompe
     * l'esecuzione del metodo.
     * Se i valori letti non sono vuoti, viene controllato lo stato della connessione con il Server.
     * Se si è connessi, viene eseguito l'AsyncTask LearningFromFile, altrimenti viene mostrato un
     * Toast per segnalare l'errore.
     * @param view vista passata dal Button richiamante.
     */
    public void onClickStoreFromFile (View view) {

        String a = table_name.getText().toString();
        String b = radius.getText().toString();


        if (a.isEmpty()) {
            table_name.setError(getString(R.string.table_blank_field_error));
            return;
        }

        if (b.isEmpty()) {
            radius.setError(getString(R.string.radius_blank_field_error));
            return;
        }

        if (ConnectionManager.connectedToServer) {
                connManager.new LearningFromFile(MainActivity.this).execute(a, b);
        } else Toast
                    .makeText(getBaseContext(), R.string.unable_connect_error, Toast.LENGTH_LONG)
                    .show();

    }
}
