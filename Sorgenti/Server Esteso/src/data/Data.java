package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import data.EmptyDatasetException;
import database.EmptySetException;
import database.Example;
import database.TableSchema;
import database.DbAccess;
import database.TableData;
import database.TableSchema.Column;
import database.DatabaseConnectionException;
import database.QUERY_TYPE;
import database.NoValueException;

/**
 * <p> Title: Data </p>
 * <p> Class description: modella l'insieme di transazioni (o tuple). </p>
 * @author Gentile, Lategano, Visaggi
 *
 */
public class Data {
	/** 
	 * List di Example dove ogni elemento modella una transazione.
	 */
	private List<Example> data;
	/** 
	 * Cardinalità dell'insieme di transazioni (dimensione di data).
	 */
	private int numberOfExamples;
	/** 
	 * Lista di Attribute (attributi in ciascuna tupla, ovvero schema della base di dati).
	 */
	private List<Attribute> explanatorySet;
	
	/**
	 * Inizializza data con le transazioni distinte presenti nel DB (crea quindi prima un oggetto di tipo DbAccess e 
	 * inizializza la connessione al DB).
	 * Inizializza numberOfExamples con il numero di transazioni memorizzate in data (dimensione di data).
	 * Inizializza explanatorySet creando una lista di oggetti di tipo DiscreteAttribute (uno per ciascun attributo).
	 * @param tName nome della tabella per ottenere le transazioni distinte.
	 * @throws EmptyDatasetException qualora il dataset sia vuoto.
	 * @throws SQLException in presenza di errori nella esecuzione della query.
	 * @throws NoValueException in caso di assenza di un valore all'interno di un resultset. 
	 * @throws DatabaseConnectionException in caso di fallimento nella connessione al database. 
	 */
	public Data(String tName) throws EmptyDatasetException, SQLException, DatabaseConnectionException, NoValueException {
		
		DbAccess db = new DbAccess();
		db.initConnection();
		TableSchema tSchema = new TableSchema(db, tName);
		
		data = new ArrayList<Example>();
		explanatorySet = new LinkedList<Attribute>();
		
		TableData tData = new TableData(db);
		
		try {
			data = tData.getDistinctTransazioni(tName);
		} catch(EmptySetException e) {
			throw new EmptyDatasetException(e.getMessage());
		}
		
		numberOfExamples = data.size();
		
		for(int i=0; i < tSchema.getNumberOfAttributes(); i++){
			Column c = tSchema.getColumn(i);
			
			if(c.isNumber()) {
				double min = (double)tData.getAggregateColumnValue(tName, c, QUERY_TYPE.MIN);
				double max = (double)tData.getAggregateColumnValue(tName, c, QUERY_TYPE.MAX);
			
				explanatorySet.add(new ContinuousAttribute(c.getColumnName(), i, min, max));
			}
			else {
				
				TreeSet<String> values = new TreeSet<String>();
				for(Object A:tData.getDistinctColumnValues(tName, c))
					values.add((String)A);
				
				explanatorySet.add(new DiscreteAttribute(c.getColumnName(), i, values));
			}
			
		}
		
		db.closeConnection();
	}
	
	/**
	 * Restituisce la cardinalità dell'insieme di transazioni (numero di righe in data).
	 * @return un intero rappresentante il numero di transazioni presenti in data.
	 */
	public int getNumberOfExamples() {
		return numberOfExamples;
	}
	
	/**
	 * Restituisce la dimensione di explanatorySet, ovvero il grado del DB.
	 * @return un intero rappresentante il numero di attributi del DB, cioè il grado del DB.
	 */
	public int getNumberOfExplanatoryAttributes() {
		return explanatorySet.size();
	}
	
	/**
	 * Restituisce explanatorySet.
	 * @return una lista di attributi che modella lo schema della tabella di dati.
	 */
	List<Attribute> getAttributeSchema() {
		return explanatorySet;
	}
	
	/**
	 * Restituisce l'oggetto di data in posizione [exampleIndex][attributeIndex].
	 * @param exampleIndex indice di riga in riferimento a data (lista di transazioni).
	 * @param attributeIndex indice in riferimento a Example (che modella una transazione).
	 * @return un oggetto rappresentante il valore assunto in data dall'attributo in posizione [exampleIndex - attributeIndex].
	 */
	public Object getAttributeValue(int exampleIndex, int attributeIndex) {
		return data.get(exampleIndex).get(attributeIndex);
	}
	
	/**
	 * Crea e restituisce una istanza di Tuple che modella la transazione con indice index in data. Restituisce il riferimento a 
	 * tale istanza. Viene usato lo RTTI per distinguere tra ContinuousAttribute e DiscreteAttribute (e quindi per 
	 * creare nella tupla un ContinuousItem o un DiscreteItem).
	 * @param index indice per ottenere la transazione corrispondente da data.
	 * @return oggetto di tipo Tuple che modella la riga i-esima come una sequenza (tupla) di Item (coppie attributo-valore).
	 */
	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(explanatorySet.size());

		int i=0;
		for(Attribute A:explanatorySet){
			if(A instanceof DiscreteAttribute)
				tuple.add(new DiscreteItem((DiscreteAttribute)A, (String)data.get(index).get(i)),i);
			else 
				tuple.add(new ContinuousItem((ContinuousAttribute)A, (Double)data.get(index).get(i)), i);
			i++;
		}
		return tuple;
	}
	
	
	/**
	 * Crea e restituisce una stringa in cui memorizza lo schema della tabella (explanatorySet) 
	 * e le transazioni memorizzate in data, opportunamente enumerate. 
	 * @return una stringa che modella lo schema della tabella.
	 */
	public String toString() {
		String schema = "";
		
		int i = 1;
		for(Attribute A:explanatorySet) {
			schema += A;
			if(i<explanatorySet.size())
				schema += ",";
			i++;
		}
		
		i = 0;
		for(Example E:data) {
			schema += "\n" + (i) + ":" + E; 
			i ++;
		}
		
		return schema + "\n";
	}

}
