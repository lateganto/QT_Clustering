package com.glv.map.qtclient.activityService;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.glv.map.qtclient.R;

/**
 * <p> Title: SettingsActivity </p>
 * <p> Class description: rappresenta la classe per la visualizzazione delle impostazioni
 *                        dell'applicazione. Implementa l'interfaccia OnSharedPreferenceChangeListener per la gestione
 *                        delle preferenze salvate sul dispositivo.</p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class SettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Chiamato quando viene creata l'activity. Aggiunge all'activity le componenti del corrispettivo
     * file xml.
     * @param savedInstanceState stato dell'istanza salvata.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

    /**
     * Viene chiamato quando viene rispresa l'esecuzione dell'activity.
     * Chiama onResume() della superclasse.
     * Aggiorna le preferenze se queste vengono modificate, registrando il listener.
     */
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Viene chiamato per mettere in pausa l'activity.
     * Chiama onPause() della superclasse.
     * deRegistra il listener impostato nel metodo onResume().
     */
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen()
                .getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Viene chiamato quando una shared preference viene cambiata, aggiunta o modifica.
     * In questo caso non esegue nulla.
     * @param sharedPreferences la shared preference modificata.
     * @param key key della rispettiva shared preference modificata.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
