package server.database;

import java.util.List;

import shared.models.IndexedData;
/**
 * Indexed Data database access object. This class contains methods for interacting
 * with the data that the client has submitted while indexing. This data is stored in 
 * the indexed_data table.
 * @author lindell
 *
 */
public class IndexedDataDAO {
	private Database db;
	
	IndexedDataDAO(Database db) {
		this.db = db;
	}
	
	public List<IndexedData> getAll() throws DatabaseException {
		
	
		// TODO: Use db's connection to query all indexeddatas from the database and return them	
		
		
		return null;	
	}
	
	public void add(IndexedData indexeddata) throws DatabaseException {

	
		// TODO: Use db's connection to add a new indexeddata to the database
		
	}
	
	public void update(IndexedData indexeddata) throws DatabaseException {

	
		// TODO: Use db's connection to update indexeddata in the database
		
	}
	
	public void delete(IndexedData indexeddata) throws DatabaseException {

	
		// TODO: Use db's connection to delete indexeddata from the database
		
	}
}
