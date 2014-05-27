package server.database;

import java.util.logging.Logger;
import java.io.File;
import java.sql.*;

/**
 * The database class contains methods to initialize the database 
 * and to begin and end transactions. It also contains database access
 * objects for all the model classes.
 * @author lindell
 *
 */
public class Database {
	private BatchDAO batchDAO;
	private FieldDAO fieldDAO;
	private IndexedDataDAO indexeddataDAO;
	private ProjectDAO projectDAO;
	private UserDAO userDAO;
	private Connection connection;
	private boolean dbError;
	
	public Database() {
		batchDAO = new BatchDAO(this);
		fieldDAO = new FieldDAO(this);
		indexeddataDAO = new IndexedDataDAO(this);
		projectDAO = new ProjectDAO(this);
		userDAO = new UserDAO(this);
		connection = null;
		dbError = false;
	}
	
	/**
	 * Initialize the database
	 * @throws DatabaseException
	 */
	public static void initialize() throws DatabaseException {
		final String driver = "org.sqlite.JDBC";
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// ERROR! Could not load database driver
			throw new DatabaseException();
		}
	}
	
	public BatchDAO getBatchDAO() {
		return batchDAO;
	}

	public void setBatchDAO(BatchDAO batchDAO) {
		this.batchDAO = batchDAO;
	}

	public FieldDAO getFieldDAO() {
		return fieldDAO;
	}

	public void setFieldDAO(FieldDAO fieldDAO) {
		this.fieldDAO = fieldDAO;
	}

	public IndexedDataDAO getIndexeddataDAO() {
		return indexeddataDAO;
	}

	public void setIndexeddataDAO(IndexedDataDAO indexeddataDAO) {
		this.indexeddataDAO = indexeddataDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Start a database transaction
	 * @throws DatabaseException
	 */
	public void startTransaction() throws DatabaseException {
		String dbName = "database" + File.separator + "database.sqlite";
		String connectionURL = "jdbc:sqlite:" + dbName;
		
		connection = null;
		
		try {
		    // Open a database connection
		    connection = DriverManager.getConnection(connectionURL);
		    
		    // Start a transaction
		    connection.setAutoCommit(false);
		}
		catch (SQLException e) {
			throw new DatabaseException();
		}
	}
	
	/**
	 * End a database transaction
	 * @param commit
	 * @throws DatabaseException 
	 */
	public void endTransaction(boolean commit) throws DatabaseException {
		try {
		    if (commit) {
		        connection.commit();
		    }
		    else {
		        connection.rollback();
		    }
		}
		catch (SQLException e) {
			throw new DatabaseException();
		}
		finally {
		    try {
				connection.close();
			} catch (SQLException e) {
				throw new DatabaseException();
			}
		}	
		// close the connection
		connection = null;
	}
}
