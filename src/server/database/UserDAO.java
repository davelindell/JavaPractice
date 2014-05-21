package server.database;

import java.util.List;

import shared.models.User;
/**
 * The user database access object contains methods for interacting with the 
 * data stored in the users database table.
 * @author lindell
 *
 */
public class UserDAO {
	private Database db;
	
	UserDAO(Database db) {
		this.db = db;
	}
	
	public List<User> getAll() throws DatabaseException {
		
	
		// TODO: Use db's connection to query all users from the database and return them	
		
		
		return null;	
	}
	
	public void add(User user) throws DatabaseException {

	
		// TODO: Use db's connection to add a new user to the database
		
	}
	
	public void update(User user) throws DatabaseException {

	
		// TODO: Use db's connection to update user in the database
		
	}
	
	public void delete(User user) throws DatabaseException {

	
		// TODO: Use db's connection to delete user from the database
		
	}
}
