package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Project> projects = new ArrayList<Project>();
		try {
			String sql = "select * from projects";
			stmt = db.getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				Project project = new Project();
				project.setProject_id(rs.getInt(1));
				project.setProject_title(rs.getString(2));
				projects.add(project);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException();
		}
		return projects;	
	}
	
	public void add(Project project) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
			String sql = "INSERT INTO projects (project_title) VALUES (?)";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setString(1, project.getProject_title());
			
			if (stmt.executeUpdate() == 1) {
				// OK
			}
			else {
				throw new DatabaseException();
			}
			
		} 
		
		catch (SQLException e) {
			throw new DatabaseException();
		}
		
		finally {
			if (stmt != null) {
				try {
					stmt.close();
				} 
				catch (SQLException e) {
					throw new DatabaseException();
				}
			}	
		}
	}
	
	public void update(Project project) throws DatabaseException {

		PreparedStatement stmt = null;
		try {
		    String sql = "UPDATE projects " + 
		                 "set project_title = ? " + 
		                 "where project_id = ?";
		    stmt = db.getConnection().prepareStatement(sql);
		    stmt.setString(1,project.getProject_title());
		    stmt.setInt(2, project.getProject_id());
		    
		    if (stmt.executeUpdate() == 1) {
		    	  // OK
		    }
		      
		    else {
		    	throw new DatabaseException();
		    }
		        
		}
		catch (SQLException e) {
			throw new DatabaseException();
		}
		finally {
		    if (stmt != null) {
		    	try {
					stmt.close();
				} 
		    	catch (SQLException e) {
		    		throw new DatabaseException();
				}
		    }
		}	
		
	}
	
	public void delete(Project project) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
		    String sql = "DELETE FROM projects WHERE project_id = ?";
		    stmt = db.getConnection().prepareStatement(sql);
		    stmt.setInt(1, project.getProject_id());
		    
		    if (stmt.executeUpdate() == 1) {
		    	  // OK
		    }
		      
		    else {
		    	throw new DatabaseException();
		    }
		        
		}
		catch (SQLException e) {
		    throw new DatabaseException();
		}
		finally {
		    if (stmt != null) {
		    	try {
					stmt.close();
				} 
		    	catch (SQLException e) {
		    		throw new DatabaseException();
				}
		    }
		}	
	}
		
}
