package server.database;

import java.util.logging.Logger;
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
	
	/**
	 * Initialize the database
	 * @throws DatabaseException
	 */
	public static void initialize() throws DatabaseException {
		
		// TODO: Load the SQLite database driver
		
	}
	
	public Database() {
		batchDAO = new BatchDAO(this);
		fieldDAO = new FieldDAO(this);
		indexeddataDAO = new IndexedDataDAO(this);
		projectDAO = new ProjectDAO(this);
		userDAO = new UserDAO(this);
		connection = null;
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
	
		
		// TODO: Open a connection to the database and start a transaction
		
	}
	
	/**
	 * End a database transaction
	 * @param commit
	 */
	public void endTransaction(boolean commit) {
		
		
		// TODO: Commit or rollback the transaction and close the connection
		
	}

	
}
