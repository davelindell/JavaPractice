package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	
	public IndexedDataDAO(Database db) {
		this.db = db;
	}
	
	public List<IndexedData> getByFieldAndValue(int field_id, String value) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IndexedData> data_list = new ArrayList<IndexedData>();
		try {
			String sql = "select * from indexed_data where field_id = ? and record_value = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, field_id);
			stmt.setString(2, value);
			rs = stmt.executeQuery();
			while(rs.next()) {
				IndexedData data = new IndexedData();
				data.setData_id(rs.getInt(1));
				data.setBatch_id(rs.getInt(2));
				data.setRecord_number(rs.getInt(3));
				data.setField_id(rs.getInt(4));
				data.setRecord_value(rs.getString(5));
				data_list.add(data);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException();
		}
		return data_list;	
	}
	
	public List<IndexedData> getAll() throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IndexedData> data_list = new ArrayList<IndexedData>();
		try {
			String sql = "select * from indexed_data";
			stmt = db.getConnection().prepareStatement(sql);
			
			rs = stmt.executeQuery();
			while(rs.next()) {
				IndexedData data = new IndexedData();
				data.setData_id(rs.getInt(1));
				data.setBatch_id(rs.getInt(2));
				data.setRecord_number(rs.getInt(3));
				data.setField_id(rs.getInt(4));
				data.setRecord_value(rs.getString(5));
				data_list.add(data);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException();
		}
		return data_list;	
	}
	
	public void add(IndexedData data) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
			String sql = "INSERT INTO indexed_data (batch_id, record_number, "
					+ "field_id, record_value) VALUES (?,?,?,?)";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, data.getBatch_id());
			stmt.setInt(2, data.getRecord_number());
			stmt.setInt(3, data.getField_id());
			stmt.setString(4, data.getRecord_value());
			
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
	
	public void update(IndexedData data) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
		    String sql = "UPDATE indexed_data " + 
		                 "set batch_id = ?, record_number = ?, " +
		                 "field_id = ?, record_value = ? " + 
		                 "where data_id = ?";
		    
		    stmt = db.getConnection().prepareStatement(sql);
		    
		   	stmt.setInt(1, data.getBatch_id());
		   	stmt.setInt(2, data.getRecord_number());
		   	stmt.setInt(3, data.getField_id());
		   	stmt.setString(4, data.getRecord_value());
		   	stmt.setInt(5, data.getData_id());
		  
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
	
	public void delete(IndexedData data) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
		    String sql = "DELETE FROM indexed_data WHERE data_id = ?";
		    stmt = db.getConnection().prepareStatement(sql);
		    stmt.setInt(1, data.getData_id());
		    
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
