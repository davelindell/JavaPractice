package shared.communication;

/** 
 * ValidateUser_Params is a wrapper class to store the parameters
 * for the validateUser method in the communicator classes
 * 
 * @author lindell
 *
 */

public class ValidateUser_Params extends Communicator_Params {

	/**
	 * The constructor for the ValidateUser_Params class
	 * @param username the user's username
	 * @param password the user's password
	 */
	public ValidateUser_Params(String username, String password) {
		super(username, password);
	}
}
