package database;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import database.TableSchema.Column;

/**
 * <p> Title: TableData </p>
 * <p> Class description: modella l'insieme di transazioni collezionate in una tabella. 
 * 						  La singola transazione è modellata dalla classe Example. </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class TableData {

	/**
	 * Oggetto di tipo DbAccess per effettuare la connessione al DB.
	 */
	private DbAccess db;
	
	/**
	 * Costruttore che prende in input un oggetto di tipo DbAccess per realizzare la connessione al DB.
	 * @param db oggetto di tipo DbAccess.
	 */
	public TableData(DbAccess db) {
		this.db=db;	
	}

	/**
	 * Ricava lo schema della tabella con nome table. Esegue una interrogazione per estrarre le tuple distinte da tale tabella. 
	 * Per ogni tupla del resultset si crea un oggetto, istanza della classe Example, il cui riferimento va incluso nella lista 
	 * da restituire. In particolare, per la tupla corrente nel resultset, si estraggono i valori dei singoli campi (usando 
	 * getFloat() o getString()), e li si aggiungono all'oggetto istanza della classe Example che si sta costruendo.
	 * @param table nome della tabella nel database.
	 * @return una lista di transazioni distinte memorizzate nella tabella.
	 * @throws SQLException in presenza di errori nella esecuzione della query.
	 * @throws EmptySetException se il resultset è vuoto.
	 * @throws NoValueException in caso di assenza di un valore all'interno di un resultset. 
	 */
	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException, NoValueException {
		LinkedList<Example> transSet = new LinkedList<Example>();
		Statement statement;
		TableSchema tSchema = new TableSchema(db,table);
		
		
		String query="select distinct ";
		
		for(int i=0; i<tSchema.getNumberOfAttributes(); i++){
			Column c = tSchema.getColumn(i);
			if(i>0)
				query+=",";
			query += c.getColumnName();
		}
		
		
		query += (" FROM "+ table);
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);

		try {
			while (rs.next()) {
				Example currentTuple = new Example();
				for(int i=0; i<tSchema.getNumberOfAttributes(); i++) {
					if(tSchema.getColumn(i).isNumber())
						currentTuple.add(rs.getDouble(i+1));
					else
						currentTuple.add(rs.getString(i+1));

					if(rs.wasNull())
						throw new NoValueException("No value on column " + tSchema.getColumn(i).getColumnName());
				}
				transSet.add(currentTuple);
			}
			
			if(transSet.size() == 0)
				throw new EmptySetException(table+" is Empty!");
		
		}
		finally {
			rs.close();
			statement.close();
		}
		return transSet;
	}

	/**
	 * Formula ed esegue una interrogazione SQL per estrarre i valori distinti ordinati di column e popolare un insieme da restituire.
	 * @param table nome della tabella.
	 * @param column nome della colonna nella tabella del DB da cui si vogliono estrarre informazioni.
	 * @return insieme di valori distinti ordinati in modalità ascendente che l'attributo identificato dal nome column assume nella 
	 * 		   tabella identificata dal nome table.
	 * @throws SQLException in presenza di errori nella esecuzione della query.
	 */
	public Set<Object> getDistinctColumnValues(String table,Column column) throws SQLException {
		Set<Object> valueSet = new TreeSet<Object>();
		Statement statement;
		
		String query="select distinct ";
		
		query += column.getColumnName();
		query += (" FROM "+table);
		query += (" ORDER BY " + column.getColumnName());
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
				if(column.isNumber())
					valueSet.add(rs.getDouble(1));
				else
					valueSet.add(rs.getString(1));
		}

		rs.close();
		statement.close();
		
		return valueSet;

	}

	/**
	 * Formula ed esegue una interrogazione SQL per estrarre il valore aggregato (valore minimo o valore massimo) cercato nella 
	 * colonna di nome "column" della tabella "table". Il metodo solleva e propaga una NoValueException se il resultset è vuoto 
	 * o se il valore calcolato è pari a null.
	 * @param table nome della tabella.
	 * @param column nome della colonna nella tabella.
	 * @param aggregate operatore SQL di aggregazione (min,max).
	 * @return l'aggregato cercato.
	 * @throws SQLException in presenza di errori nella esecuzione della query.
	 */
	public  Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException {
		Statement statement;
		
		Object value=null;
		String aggregateOp="";
		
		String query="select ";
		if(aggregate==QUERY_TYPE.MAX)
			aggregateOp+="max";
		else
			aggregateOp+="min";
		query+=aggregateOp+"("+column.getColumnName()+ ") FROM "+table;
		
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		if (rs.next()) {
				if(column.isNumber())
					value=rs.getDouble(1);
				else
					value=rs.getString(1);
			
		}

		rs.close();
		statement.close();
		
		return value;
	}

	
}
