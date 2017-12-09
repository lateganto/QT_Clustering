package com.glv.map.qtclient.connectionService;


import android.content.Context;
import android.widget.Toast;

/**
 * <p> Title: ShowShortToast </p>
 * <p> Class description: la classe implementa l'interfaccia Runnable per la visualizzazione di un
 *                        Toast di breve durata come messaggio di errore o notifica per l'utente.</p>
 * @author Gentile, Lategano, Visaggi
 *
 */
class ShowToast implements Runnable{
    /**
     * Rappresenta il contesto nel quale deve essere mostrato il Toast.
     */
    private Context context;
    /**
     * Rappresenta il messaggio da mostrare nel Toast.
     */
    private String msg;

    /**
     * Costruttore che inizializza gli attributi.
     * @param context il contesto nel quale eseguire il Toast.
     * @param msg il messaggio da mostrare nel Toast.
     */
    ShowToast(Context context, String msg) {
        this.context = context;
        this.msg = msg;
    }

    /**
     * Rappresenta il punto di ingresso nel codice della classe.
     * Viene eseguito quando viene avviato un Thread creato con la classe ShowShortThread che
     * implementa l'interfaccia Runnable.
     * Viene mostrato il Toast con il messaggio di errore.
     */
    @Override
    public void run() {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}