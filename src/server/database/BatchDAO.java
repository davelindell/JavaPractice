package server.database;

import java.util.List;

import shared.models.Batch;

/**
 * Batch Data Access Object communicates with the database
 * @author lindell
 *
 */
public class BatchDAO {

	private Database db;
	
	BatchDAO(Database db) {
		this.db = db;
	}
	
	public List<Batch> getAll() throws DatabaseException {
		
	
		// TODO: Use db's connection to query all batches from the database and return them	
		
		
		return null;	
	}
	
	public void add(Batch batch) throws DatabaseException {

	
		// TODO: Use db's connection to add a new batch to the database
		
	}
	
	public void update(Batch batch) throws DatabaseException {

	
		// TODO: Use db's connection to update batch in the database
		
	}
	
	public void delete(Batch batch) throws DatabaseException {

	
		// TODO: Use db's connection to delete batch from the database
		
	}
}
