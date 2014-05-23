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
	
	BatchDAO(Database db) {
		this.db = db;
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
				batches.add(batch);
			}
		}
		catch (SQLException e) {
			// ERROR
		}
		return batches;	
	}
	
	public void add(Batch batch) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
			String sql = "INSERT INTO batches (project_id, image_url, first_y_coord,"
					 + " record_height, num_records, num_fields) VALUES (?,?,?,?,?,?)";
			stmt = db.getConnection().prepareStatement(sql);
			stmt.setInt(1, batch.getProject_id());
			stmt.setString(2, batch.getImage_url());
			stmt.setInt(3, batch.getFirst_y_coord());
			stmt.setInt(4, batch.getRecord_height());
			stmt.setInt(5, batch.getNum_records());
			stmt.setInt(6, batch.getNum_fields());
			
			if (stmt.executeUpdate() == 1) {
				// OK
			}
			else {
				throw new DatabaseException();
			}
			
		} 
		
		catch (SQLException e) {
			// ERROR
		}
		
		finally {
			if (stmt != null) {
				try {
					stmt.close();
				} 
				catch (SQLException e) {
					// ERROR
				}
			}	
		}
	}
	
	public void update(Batch batch) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
		    String sql = "UPDATE batches " + 
		                 "set project_id = ?, image_url = ?, first_y_coord = ?, " +
		                 "record_height = ?, num_records = ?, num_fields = ? " + 
		                 "where batch_id = ?";
		    stmt = db.getConnection().prepareStatement(sql);
		    stmt.setInt(1, batch.getProject_id());
		    stmt.setString(2, batch.getImage_url());
		    stmt.setInt(3, batch.getFirst_y_coord());
		    stmt.setInt(4, batch.getRecord_height());
		    stmt.setInt(5, batch.getNum_records());
		    stmt.setInt(6, batch.getNum_fields());
		    stmt.setInt(7, batch.getBatch_id());

		    if (stmt.executeUpdate() == 1) {
		    	  // OK
		    }
		      
		    else {
		    	// ERROR
		    }
		        
		}
		catch (SQLException e) {
		    // ERROR
		}
		finally {
		    if (stmt != null) {
		    	try {
					stmt.close();
				} 
		    	catch (SQLException e) {
					// ERROR
				}
		    }
		}	
	}
	
	public void delete(Batch batch) throws DatabaseException {
		PreparedStatement stmt = null;
		try {
		    String sql = "DELETE FROM batches WHERE id = ?";
		    stmt = db.getConnection().prepareStatement(sql);
		    stmt.setInt(1, batch.getBatch_id());
		    
		    if (stmt.executeUpdate() == 1) {
		    	  // OK
		    }
		      
		    else {
		    	// ERROR
		    }
		        
		}
		catch (SQLException e) {
		    // ERROR
		}
		finally {
		    if (stmt != null) {
		    	try {
					stmt.close();
				} 
		    	catch (SQLException e) {
					// ERROR
				}
		    }
		}	
	}
	
}
