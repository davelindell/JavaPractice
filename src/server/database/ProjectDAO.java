package server.database;

import java.util.List;

import shared.models.Project;
/**
 * The project database access object contains methods for interacting with the data
 * stored in the project database table.
 * @author lindell
 *
 */
public class ProjectDAO {
	private Database db;
	
	ProjectDAO(Database db) {
		this.db = db;
	}
	
	public List<Project> getAll() throws DatabaseException {
		
	
		// TODO: Use db's connection to query all projects from the database and return them	
		
		
		return null;	
	}
	
	public void add(Project project) throws DatabaseException {

	
		// TODO: Use db's connection to add a new project to the database
		
	}
	
	public void update(Project project) throws DatabaseException {

	
		// TODO: Use db's connection to update project in the database
		
	}
	
	public void delete(Project project) throws DatabaseException {

	
		// TODO: Use db's connection to delete project from the database
		
	}
}
