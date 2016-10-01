package server.database;

@SuppressWarnings("serial")
/**
 * A database exception class for exceptions when 
 * managing the database.
 * @author lindell
 *
 */
public class DatabaseException extends Exception {

	public DatabaseException() {
		return;
	}

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(Throwable cause) {
		super(cause);

	}

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

}