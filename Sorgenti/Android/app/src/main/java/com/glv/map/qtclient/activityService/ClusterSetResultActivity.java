package com.glv.map.qtclient.activityService;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.glv.map.qtclient.MainActivity;
import com.glv.map.qtclient.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * <p> Title: ClusterSetResultActivity </p>
 * <p> Class description: rappresenta la classe rappresentante i clusters elaborati dal server in
 *                        un grafico a torta con rispettiva dimensione e centroide.</p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class ClusterSetResultActivity extends ActionBarActivity {
    /**
     * Stringa utilizzata per il passaggio dei parametri tra
     * questa activity e la Cluster Result Activity.
     */
    public final static String TUPLES_ON_CENTROID_RANGE = "Centroid_Result";

    /**
     * Struttura dati contente i dati computati e ricevuti dal server, passati dalla Main_activity.
     */
    private HashMap<String, HashMap<String, Double>> computedData;
    /**
     * Grafico a torta rappresentante i dati computati.
     */
    private PieChart clusterSetpieChart;

    /**
     * Viene richiamato al momento della creazione dell'Activity.
     * Ha il compito di inizializzare le componenti grafiche e le variabili utili all'activity.
     * Inizializza computedData con i dati comunicati dalla Main_activity.
     * Effettua le dovute inizializzazioni per mostrare il grafico a torta e la sua legenda.
     * Richiama il metodo setClusterSetpieChartData() per inizializzarre i dati del grafico.
     * @param savedInstanceState lo stato dell'istanza salvata.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster_set_result);

        Bundle receivedData = getIntent().getExtras();
        computedData =
                (HashMap<String, HashMap<String, Double>>)
                        receivedData.get(MainActivity.COMPUTED_DATA);

        clusterSetpieChart = (PieChart) findViewById(R.id.clusterSet_pieChart);
        clusterSetpieChart.setDescription(getString(R.string.pieChart_description));

        clusterSetpieChart.setDrawCenterText(true);

        clusterSetpieChart.setCenterText(getString(R.string.pieChart_center_text));
        clusterSetpieChart.getLegend().setEnabled(true);

        clusterSetpieChart.setDrawSliceText(false);
        clusterSetpieChart.setOnChartValueSelectedListener(new ListenerOnPieChart());
        setClusterSetpieChartData();


        Legend l = clusterSetpieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }


    /**
     * Inizializza i dati del grafico.
     * Genera un ArrayList contenente i colori per il grafico.
     * Inserisce i dati nel grafico.
     */
    private void setClusterSetpieChartData() {

        Set<String> chiavi = computedData.keySet();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        int i = 0;
        for (String s : chiavi) {
            HashMap<String, Double> mapTuple = computedData.get(s);
            xVals.add(s);

            //auto cast a float
            yVals.add(new Entry(mapTuple.size(), i));
            i++;
        }


        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        PieDataSet dataSet = new PieDataSet(yVals, getString(R.string.pieChart_dataSet_label));
        dataSet.setColors(colors);

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        PieData data = new PieData(xVals, dataSet);
        //data.setValueTextSize(11f);

        clusterSetpieChart.setData(data);
    }


    /**
     * Viene richiamato durante la creazione dell'Activity per creare il menu.
     * Richiama il metodo getMenuInflater che aggiunge le componenti del menu all'action bar se sono
     * presenti, leggendole dal corrispettivo file xml.
     * @param menu usato per l'inizializzazione delle componenti dell'option menu dell'activity.
     * @return true se va a buon fine, false altrimenti.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cluster_set_result, menu);
        return true;
    }

    /**
     * Viene richiamato quando viene selezionato un componente dal menu nell'action bar.
     * Identifica l'id del componente selezionato, ed esegue le appropriate operazioni per la
     * selezione effettuata.
     * @param item componente selezionato nel menu dell'activity.
     * @return true se va a buon fine, false altrimenti.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_text_activity) {
            openTextResults();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * <p> Title: ListenerOnPieChart </p>
     * <p> Class description: rappresenta la classe che implementa l'interfaccia del listerner per
     * il grafico a torta.
     * </p>
     * @author Gentile, Lategano, Visaggi
     *
     */
    private class ListenerOnPieChart implements OnChartValueSelectedListener {

        /**
         * Ha il compito di richiamare l'activity per mostare il grafico a barre del singolo cluster
         * selezionato.
         * Effettua un controllo sulla dimensione dei clusters: se incontra un cluster che contiene solo
         * il centroide, inibisce l'apertura dell'activity ClusterResultActivity(), e mostra un Toast
         * di notifica per l'utente.
         * @param entry l'entry del componente cliccato.
         * @param i indice del componente nel grafico (xVals).
         * @param highlight HighLight.
         */
        @Override
        public void onValueSelected(Entry entry, int i, Highlight highlight) {

            Set<String> keys = computedData.keySet();
            Object[] arrayKeys = keys.toArray();
            HashMap<String, Double> tuples = computedData.get(arrayKeys[entry.getXIndex()]);

            if(tuples.size() != 1) {
                Intent intent = new Intent(getBaseContext(), ClusterResultActivity.class);
                intent.putExtra(TUPLES_ON_CENTROID_RANGE, tuples);

                startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(), R.string.one_tuple_error,
                        Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Non esegue nulla.
         */
        @Override
        public void onNothingSelected() {}
    }

    /**
     * Rappresenta il metodo richiamato dal menu quando si sceglie di visualizzare il contenuto
     * testuale dei dati elaborati dal server.
     * Viene richiamata l'activity TextResultActivity() passandogli la stringa elaborata dal
     * metodo createTextResults().
     */
    private void openTextResults() {
        String dataToSend = createTextResults();

        Intent intent = new Intent(getBaseContext(), TextResultActivity.class);
        intent.putExtra(MainActivity.COMPUTED_TEXT_DATA, dataToSend);
        startActivity(intent);

    }

    /**
     * Ha il compito di elaborare i dati contenuti in computedData() e restituisce la stringa
     * rappresentante i dati computati dal server.
     * @return una stringa contenente i dati elaborati dalla struttura computata dal server.
     */
    private String createTextResults() {
        String textData = getString(R.string.number_of_clusters_str) + computedData.size() + "\n";

        HashMap<String, Double> tempMap;
        Set<String> chiavi1 = computedData.keySet();
        for(String s1:chiavi1){
            textData += getString(R.string.centroid_str) + s1 + "\n\n";

            tempMap = computedData.get(s1);

            Set<String> chiavi2 = tempMap.keySet();
            for(String s2:chiavi2)
                textData += s2 + getString(R.string.distance_str) + tempMap.get(s2) + "\n\n";

            textData +="\n";
        }

        return textData;
    }

}
