package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p> Title: TableSchema </p>
 * <p> Class description: modella lo schema di una tabella nel database relazionale. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class TableSchema {
	
	@SuppressWarnings("unused")
	/**
	 * Oggetto di tipo DbAccess che permette di realizzare l'accesso alla base di dati.
	 */
	private DbAccess db;
	
	/**
	 * <p> Title: Column </p>
	 * <p> Class description: inner class che modella il dato "colonna". </p>
	 * @author Antonio
	 *
	 */
	public class Column {
		/**
		 * Nome della colonna.
		 */
		private String name;
		/**
		 * Tipo della colonna (number, string, etc.).
		 */
		private String type;
		
		/**
		 * Costruttore che inizializza i membri della inner class.
		 * @param name nome della colonna.
		 * @param type tipo (number, string, etc.) della colonna.
		 */
		Column(String name,String type) {
			this.name=name;
			this.type=type;
		}
		
		/**
		 * Restituisce il nome della colonna.
		 * @return una stringa rappresentante il nome della colonna.
		 */
		public String getColumnName() {
			return name;
		}
		
		/**
		 * Verifica se il tipo della colonna è "number".
		 * @return un booleano per indicare se il tipo della colonna è "number".
		 */
		public boolean isNumber() {
			return type.equals("number");
		}
		
		/**
		 * Restituisce una stringa indicante il nome e il tipo della colonna (separati da una virgola).
		 * @return una stringa rappresentante nome e tipo della colonna.
		 */
		public String toString() {
			return name+":"+type;
		}
	}
	
	/**
	 * Lista di elementi che modellano le colonne (schema della tabella).
	 */
	private List<Column> tableSchema = new ArrayList<Column>();
	
	/**
	 * Costruttore che prende in input un oggetto di tipo DbAccess, per effettuare l'accesso al DB e una stringa rappresentante il nome della tabella. 
	 * Definisce un Map con i vari tipi di dati che possono essere associati alle colonne del DB (chiave) e relativo valore (string o number).
	 * Effettua la connessione al DB richiamando il metodo, grazie al riferimento a DbAccess. Ottiene informazioni sullo schema del DB 
	 * usando un ResultSet.
	 * Costruisce un List di informazioni sul nome e sul tipo della colonna per ogni colonna del DB (cioè per ogni elemento dello schema del DB). 
	 * 
	 * @param db oggetto che permette di  realizzare l'accesso alla base di dati.
	 * @param tableName nome della tabella.
	 * @throws SQLException nel caso in cui la tabella non ha alcuna colonna, oppure nel caso di errori derivanti dall'uso della connessione
	 * 						per ottenere informazioni sulle colonne del DB.
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException {
		
		this.db = db;
		
		HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();
		//http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");
		
		
	
		Connection con = db.getConnection();
		/*	SQLException if a database access error occurs
		 *	or this method is called on a closed connection
		 */
		DatabaseMetaData meta = con.getMetaData();
		 
		/*	SQLException if a database access error occurs */
	    ResultSet res = meta.getColumns(null, null, tableName, null);
		
	     
	    boolean emptyRes = true;
	    while (res.next()) {
	    	emptyRes = false;
	    	if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
	        		tableSchema.add(new Column(
	        				res.getString("COLUMN_NAME"),
	        				mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
	        				);
	    }
	    res.close();
	     
	    //added for control on table: if res is empty (null schema for table), table not found
	    if(emptyRes)
	    	throw new SQLException("Table not Found!");
		}
	  
		/**
		 * Restituisce il numero di colonne del DB (grado del DB o numero di attributi del DB).
		 * @return un intero rappresentante il numero di colonne del DB (o numero di attributi del DB).
		 */
		public int getNumberOfAttributes(){
			return tableSchema.size();
		}
		
		/**
		 * Restituisce la colonna di tableSchema nella posizione specificata.
		 * @param index posizione per estrarre un oggetto di tipo Column.
		 * @return un oggetto di tipo Column.
		 */
		public Column getColumn(int index){
			return tableSchema.get(index);
		}

}

		     


