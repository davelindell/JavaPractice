package client.communication;

import shared.communication.GetProjects_Params;
import shared.communication.GetProjects_Result;
import shared.communication.ValidateUser_Params;
import shared.communication.ValidateUser_Result;

/** 
 * The Client Communicator class acts as a proxy communicator for the client
 * when it needs to send/receive information to the server.
 * 
 * @author lindell
 *
 */

public class ClientCommunicator {
	
	/** 
	 * ValidateUser_Result takes a wrapper class and returns a wrapper
	 * with a User object inside of it. The method queries the server to see 
	 * if there is a valid user with the information provided in the parameter.
	 * 
	 * @param params object of type ValidateUser_Params which is a wrapper for 
	 * strings which describe the user.
	 * @return ValidateUser_Result, a wrapper class for a User, which is not null
	 * if the validation was successful.
	 */
	
	public ValidateUser_Result validateUser(ValidateUser_Params params) {
		return null;
	}
	
	/**
	 * getProjects queries the server with the project information given in
	 * the wrapper class parameter and returns information about all the 
	 * available projects.
	 * @param params a wrapper class of type GetProject_Params that contains
	 * the user's name and password.
	 * @return GetProject_Result, a wrapper class that contains info on all current 
	 * projects.
	 */
	public GetProjects_Result getProjects(GetProjects_Params params) {
		return null;
	}
	
	
}
