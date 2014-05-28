package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<User> users = new ArrayList<User>();
		try {
			String sql = "select * from users";
			stmt = db.getConnection().prepareStatement(sql);
			
			rs = stmt.executeQuery();
			while(rs.next()) {
				User user = new User();
				user.setUser_id(rs.getInt(1));
				user.setUser_first_name(rs.getString(2));
				user.setUser_last_name(rs.getString(3));
				user.setUsername(rs.getString(4));
				user.setPassword(rs.getString(5));
				user.setNum_records(rs.getInt(6));
				users.add(user);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException();
		}
		return users;	
	}
	
	public void add(User user) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
			String sql = "INSERT INTO users (user_first_name, user_last_name, username,"
					 + " password, num_records) VALUES (?,?,?,?,?)";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setString(1, user.getUser_first_name());
			stmt.setString(2, user.getUser_last_name());
			stmt.setString(3, user.getUsername());
			stmt.setString(4, user.getPassword());
			stmt.setInt(5, user.getNum_records());
			
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
	
	public void update(User user) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
		    String sql = "UPDATE users " + 
		                 "set user_first_name = ?, user_last_name = ?, username = ?, " +
		                 "password = ?, num_records = ? " + 
		                 "where user_id = ?";
		    stmt = db.getConnection().prepareStatement(sql);
		    stmt.setString(1, user.getUser_first_name());
		    stmt.setString(2, user.getUser_last_name());
		    stmt.setString(3, user.getUsername());
		    stmt.setString(4, user.getPassword());
		    stmt.setInt(5, user.getNum_records());

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
	
	public void delete(User user) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
		    String sql = "DELETE FROM users WHERE user_id = ?";
		    stmt = db.getConnection().prepareStatement(sql);
		    stmt.setInt(1, user.getUser_id());
		    
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
