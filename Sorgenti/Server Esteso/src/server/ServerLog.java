package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * <p> Title: ServerLog </p>
 * <p> Class description: mette a disposizione dei servizi per il salvataggio delle informazioni
 * 						  di monitoraggio degli eventi del Server. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
class ServerLog {

	/**
	 * Costante rappresentante il path della directory contenente il file di log.
	 */
	private static final String LOG_DIRECTORY = "log/";
	/**
	 * Output Stream di testo per la scrittura delle informazioni sul file di log. 
	 */
	private PrintWriter logOutput;
	/**
	 * Oggetto File che modella la directory contenente il file di log.
	 */
	private File logDirectory;
	/**
	 * Oggetto File che modella il file di log contenente le informazioni degli eventi del server.
	 */
	private File events;
	

	/**
	 * Costruttore che crea la directory "log" e il file di log se non esistono.
	 */
	ServerLog() {
		logDirectory = new File("log");
		events = new File(LOG_DIRECTORY + "events.log");
			
		if (!logDirectory.exists())
			logDirectory.mkdirs();
		
		if (!events.exists())
			try {
				events.createNewFile();
			} catch (IOException e) {
				System.out.println("Exception creating new File: " + e.getMessage());
			}
	}
	
	/**
	 * Scrive il msg stringa che riceve come argomento all'interno del file che contiene le informazioni di log.
	 * La scrittura sul file effettua l'append della stringa msg al testo già presente nel file, senza sovrascrivere.
	 * @param msg messaggio da inserire nel file di log.
	 */
	void refreshLog(String msg){
		
		try {
			logOutput = new PrintWriter(new BufferedWriter(new FileWriter(events, true)));
			logOutput.println(getDate() + msg);
			logOutput.close();
		} catch (IOException e) {
			System.out.println("Exception refreshing log: " + e.getMessage());
		}
	
	}
	
	/**
	 * Calcola la data attuale comprensiva di giorno, mese, anno, ora, minuti, secondi
	 * e la restituisce sotto forma di stringa.
	 * @return la stringa rappresentante la data attuale nel formato gg-mm-aaaa / hh:mm::ss am (or pm).
	 */
	private static String getDate(){
		
		Calendar cal = new GregorianCalendar();
		int giorno = cal.get(Calendar.DAY_OF_MONTH);
		int mese = cal.get(Calendar.MONTH);
		int anno = cal.get(Calendar.YEAR);
		int ora = cal.get(Calendar.HOUR);
		int minuti = cal.get(Calendar.MINUTE);
		int secondi = cal.get(Calendar.SECOND);
		
		String orario;
		if(cal.get(Calendar.AM_PM) == 0)
			  orario = "am";
			else
			  orario = "pm";
		
		return (giorno + "-" + (mese + 1) + "-" + anno + " / "
				+ ora + ":" + minuti + ":" + secondi + " " + orario);
	}
					
}
