package com.glv.map.qtclient.activityService;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.glv.map.qtclient.MainActivity;
import com.glv.map.qtclient.R;

/**
 * <p> Title: TextResultActivity </p>
 * <p> Class description: rappresenta la classe per la visualizzazione dell'activity con i valori
 *                        testuali dei dati computati dal server.</p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class TextResultActivity extends ActionBarActivity {
    /**
     * TextView principale dell'activity, viene usata per mostrare i dati.
     */
    private TextView clusterOutput;

    /**
     * Viene richiamato al momento della creazione dell'Activity.
     * Ha il compito di inizializzare le componenti grafiche e le variabili utili all'activity.
     * Ottiene le componenti grafiche dal corrispettivo file xml.
     * Ottiene i dati dall'activity Main_activity() o ClusterSetActivity().
     * @param savedInstanceState lo stato dell'istanza salvata.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_result);

        clusterOutput = (TextView) findViewById(R.id.clusterTextOutput);

        Bundle receivedData = getIntent().getExtras();
        clusterOutput.setText((String) receivedData.get(MainActivity.COMPUTED_TEXT_DATA));
    }
}
