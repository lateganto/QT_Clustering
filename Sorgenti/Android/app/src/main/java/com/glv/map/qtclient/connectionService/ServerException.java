package com.glv.map.qtclient.connectionService;

/**
 * <p> Title: ServerException </p>
 * <p> Class description: classe che definisce un tipo di eccezione sollevato quando si verifica un
 *                        errore durante la comunicazione con il Server.</p>
 * @author Gentile, Lategano, Visaggi
 *
 */
class ServerException extends Exception {

    /**
     * Costruttore dell'eccezione a zero argomenti che mostra un messaggio di default.
     */
    public ServerException() {
        super("Server Exception");
    }

    /**
     * Costruttore con una stringa in input indicante il messaggio da visualizzare accanto al nome della classe di cui è istanza
     * l'oggetto eccezione.
     * @param msg - messaggio da visualizzare accanto al nome della classe di cui è istanza l'oggetto eccezione.
     */
    public ServerException(String msg) {
        super(msg);
    }
}