package server.database;

import java.util.List;

import shared.models.Field;
/**
 * The Field database access object. Contains methods for interacting
 * with the field table in the database.
 * @author lindell
 *
 */
public class FieldDAO {
	private Database db;
	
	FieldDAO(Database db) {
		this.db = db;
	}
	
	public List<Field> getAll() throws DatabaseException {
		
	
		// TODO: Use db's connection to query all fields from the database and return them	
		
		
		return null;	
	}
	
	public void add(Field field) throws DatabaseException {

	
		// TODO: Use db's connection to add a new field to the database
		
	}
	
	public void update(Field field) throws DatabaseException {

	
		// TODO: Use db's connection to update field in the database
		
	}
	
	public void delete(Field field) throws DatabaseException {

	
		// TODO: Use db's connection to delete field from the database
		
	}
}
