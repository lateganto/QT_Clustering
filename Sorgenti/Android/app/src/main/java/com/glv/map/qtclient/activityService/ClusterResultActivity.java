package com.glv.map.qtclient.activityService;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.glv.map.qtclient.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * <p> Title: ClusterResultActivity </p>
 * <p> Class description: rappresenta la classe rappresentante le tuple nel raggio dal centroide
 *                        selezionato.</p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class ClusterResultActivity extends ActionBarActivity {
    /**
     * Struttura dati contenente la tuple e le rispettive distanze dal centroide del cluster.
     */
    private HashMap<String, Double> tuplesFromCentroid;
    /**
     * Grafico a barre per la rappresentazione delle tuple e della loro distanza dal centroide.
     */
    private BarChart clusterBarChart;

    /**
     * Viene richiamato al momento della creazione dell'Activity.
     * Ha il compito di inizializzare le componenti grafiche e le variabili utili all'activity.
     * Inizializza i dati ricevuti dall'activity richiamante.
     * Inizializza il grafico e il listener delle sue componenti.
     * @param savedInstanceState lo stato dell'istanza salvata.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster_result);

        Bundle receiveData = getIntent().getExtras();
        tuplesFromCentroid = (HashMap<String, Double>)
                receiveData.get(ClusterSetResultActivity.TUPLES_ON_CENTROID_RANGE);

        clusterBarChart = (BarChart) findViewById(R.id.clusterBarChart);

        clusterBarChart.setPinchZoom(true);

        clusterBarChart.setDrawValueAboveBar(true);
        clusterBarChart.setDescription("");

        XAxis xAxis = clusterBarChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        clusterBarChart.setOnChartValueSelectedListener(new ListenerOnBarChart());

        setClusterBarChartData();
    }

    /**
     * Viene richiamato per inizializzare il grafico con i dati ricevuti nell'activity.
     * Vengono settati i dati dell'asse x e y del grafico.
     * Viene anche effettuato un controllo per non inserire nel grafico il centroide del cluster.
     */
    private void setClusterBarChartData() {

        Set<String> tuples = tuplesFromCentroid.keySet();

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        String centroid = null;

        int i = 0;
        for(String s:tuples) {

            Double distance = tuplesFromCentroid.get(s);
            if (distance != 0) {
                xVals.add(s);
                yVals.add(new BarEntry(Float.parseFloat(distance.toString()), i));
                i++;
            }
            else centroid = s;
        }

        BarDataSet set = new BarDataSet(yVals, getString(R.string.tuples_in_range_from) + centroid);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set);


        BarData data = new BarData(xVals, dataSets);
        clusterBarChart.setData(data);
    }

    /**
     * <p> Title: ListenerOnBarChart </p>
     * <p> Class description: rappresenta la classe che implementa l'interfaccia del listerner per
     *                        il grafico a barre.
     *                        Ha il compito di mostrare per il rispettivo oggetto cliccato il suo
     *                        valore (tupla) in un Toast.</p>
     * @author Gentile, Lategano, Visaggi
     *
     */
    private class ListenerOnBarChart implements OnChartValueSelectedListener {
        /**
         * Ha il compito di mostrare un Toast che contiene come messaggio il valore della tupla
         * rispettiva al componente che la rappresenta nel grafico a barre.
         * @param entry l'entry del componente cliccato.
         * @param i indice del componente nel grafico (xVals).
         * @param highlight HighLight.
         */
        @Override
        public void onValueSelected(Entry entry, int i, Highlight highlight) {
            Toast.makeText(getApplicationContext(), clusterBarChart.getXValue(entry.getXIndex()),
                    Toast.LENGTH_SHORT).show();
        }

        /**
         * Non esegue nulla.
         */
        @Override
        public void onNothingSelected() {

        }
    }
}
