package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	
	public FieldDAO(Database db) {
		this.db = db;
	}
	
	public List<Field> getAll() throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Field> fields = new ArrayList<Field>();
		try {
			String sql = "select * from fields";
			stmt = db.getConnection().prepareStatement(sql);
			
			rs = stmt.executeQuery();
			while(rs.next()) {
				Field field = new Field();
				field.setProject_id(rs.getInt(1));
				field.setField_id(rs.getInt(2));
				field.setField_num(rs.getInt(3));
				field.setField_title(rs.getString(4));
				field.setHelp_url(rs.getString(5));
				field.setX_coord(rs.getInt(6));
				field.setPixel_width(rs.getInt(7));
				field.setKnown_values_url(rs.getString(8));
				fields.add(field);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException();
		}
		return fields;	
	
	}
	
	public int add(Field field) throws DatabaseException {
		int primary_key = 0;
		PreparedStatement stmt = null;
		try {
			String sql = "INSERT INTO fields (project_id, field_num, field_title, help_url," +
					  " x_coord, pixel_width, known_values_url) VALUES (?,?,?,?,?,?,?)";
			stmt = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, field.getProject_id());
			stmt.setInt(2, field.getField_num());
			stmt.setString(3, field.getField_title());
			stmt.setString(4, field.getHelp_url());
			stmt.setInt(5, field.getX_coord());
			stmt.setInt(6, field.getPixel_width());
			stmt.setString(7, field.getKnown_values_url());
			
			if (stmt.executeUpdate() == 1) {
				ResultSet key_set = stmt.getGeneratedKeys();
				primary_key = key_set.getInt(1);
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
		return primary_key;
	}
	
	public void update(Field field) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
		    String sql = "UPDATE fields " + 
		                 "set project_id = ?, field_num = ?, field_title = ?, help_url = ?, " +
		                 "x_coord = ?, pixel_width = ?, known_values_url = ? " + 
		                 "where field_id = ?";
		    stmt = db.getConnection().prepareStatement(sql);
		    stmt.setInt(1, field.getProject_id());
		    stmt.setInt(2, field.getField_num());
		    stmt.setString(3, field.getField_title());
		    stmt.setString(4, field.getHelp_url());
		    stmt.setInt(5, field.getX_coord());
		    stmt.setInt(6, field.getPixel_width());
		    stmt.setString(7, field.getKnown_values_url());
		    stmt.setInt(8, field.getField_id());
		    
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
	
	public void delete(Field field) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
		    String sql = "DELETE FROM fields WHERE field_id = ?";
		    stmt = db.getConnection().prepareStatement(sql);
		    stmt.setInt(1, field.getField_id());
		    
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
	
	public List<Field> getBatchFields(int project_id) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Field> fields = new ArrayList<Field>();
		try {
			String sql = "select * from fields where project_id = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, project_id);
				
			rs = stmt.executeQuery();
			while(rs.next()) {
				Field field = new Field();
				field.setProject_id(rs.getInt(1));
				field.setField_id(rs.getInt(2));
				field.setField_num(rs.getInt(3));
				field.setField_title(rs.getString(4));
				field.setHelp_url(rs.getString(5));
				field.setX_coord(rs.getInt(6));
				field.setPixel_width(rs.getInt(7));
				field.setKnown_values_url(rs.getString(8));
				fields.add(field);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException();
		}
		return fields;	
		
	}
		
}
