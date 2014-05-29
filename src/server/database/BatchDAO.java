package server.database;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import shared.models.Batch;

/**
 * Batch Data Access Object communicates with the database
 * @author lindell
 *
 */
public class BatchDAO {

	private Database db;
	
	public BatchDAO(Database db) {
		this.db = db;
	}
	
	public Batch getBatch(int batch_id) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Batch batch = new Batch();
		try {
			String sql = "select * from batches where batch_id = ?";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, batch_id);
			rs = stmt.executeQuery();
			while(rs.next()) {
				batch.setBatch_id(rs.getInt(1));
				batch.setProject_id(rs.getInt(2));
				batch.setImage_url(rs.getString(3));
				batch.setFirst_y_coord(rs.getInt(4));
				batch.setRecord_height(rs.getInt(5));
				batch.setNum_records(rs.getInt(6));
				batch.setNum_fields(rs.getInt(7));
				batch.setCur_username(rs.getString(8));
			}
		}
		catch (SQLException e) {
			throw new DatabaseException();
		}
		return batch;	
		
	}
	
	public List<Batch> getAll() throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Batch> batches = new ArrayList<Batch>();
		try {
			String sql = "select * from batches";
			stmt = db.getConnection().prepareStatement(sql);
			
			rs = stmt.executeQuery();
			while(rs.next()) {
				Batch batch = new Batch();
				batch.setBatch_id(rs.getInt(1));
				batch.setProject_id(rs.getInt(2));
				batch.setImage_url(rs.getString(3));
				batch.setFirst_y_coord(rs.getInt(4));
				batch.setRecord_height(rs.getInt(5));
				batch.setNum_records(rs.getInt(6));
				batch.setNum_fields(rs.getInt(7));
				batch.setCur_username(rs.getString(8));
				batches.add(batch);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException();
		}
		return batches;	
	}
	
	public int add(Batch batch) throws DatabaseException {
		int primary_key = 0;
		PreparedStatement stmt = null;
		try {
			String sql = "INSERT INTO batches (project_id, image_url, first_y_coord,"
					 + " record_height, num_records, num_fields, cur_username) VALUES (?,?,?,?,?,?,?)";
			stmt = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, batch.getProject_id());
			stmt.setString(2, batch.getImage_url());
			stmt.setInt(3, batch.getFirst_y_coord());
			stmt.setInt(4, batch.getRecord_height());
			stmt.setInt(5, batch.getNum_records());
			stmt.setInt(6, batch.getNum_fields());
			stmt.setString(7, batch.getCur_username());
			
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
	
	public void update(Batch batch) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
		    String sql = "UPDATE batches " + 
		                 "set project_id = ?, image_url = ?, first_y_coord = ?, " +
		                 "record_height = ?, num_records = ?, num_fields = ?, cur_username = ?" + 
		                 "where batch_id = ?";
		    stmt = db.getConnection().prepareStatement(sql);
		    stmt.setInt(1, batch.getProject_id());
		    stmt.setString(2, batch.getImage_url());
		    stmt.setInt(3, batch.getFirst_y_coord());
		    stmt.setInt(4, batch.getRecord_height());
		    stmt.setInt(5, batch.getNum_records());
		    stmt.setInt(6, batch.getNum_fields());
		    stmt.setString(7, batch.getCur_username());
		    stmt.setInt(8, batch.getBatch_id());

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
	
	public void delete(Batch batch) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
		    String sql = "DELETE FROM batches WHERE batch_id = ?";
		    stmt = db.getConnection().prepareStatement(sql);
		    stmt.setInt(1, batch.getBatch_id());
		    
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
