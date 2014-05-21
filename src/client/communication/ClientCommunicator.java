package client.communication;

import shared.communication.DownloadBatch_Params;
import shared.communication.DownloadBatch_Result;
import shared.communication.DownloadFile_Params;
import shared.communication.DownloadFile_Result;
import shared.communication.GetFields_Params;
import shared.communication.GetFields_Result;
import shared.communication.GetProjects_Params;
import shared.communication.GetProjects_Result;
import shared.communication.GetSampleImage_Params;
import shared.communication.GetSampleImage_Result;
import shared.communication.Search_Params;
import shared.communication.Search_Result;
import shared.communication.SubmitBatch_Params;
import shared.communication.SubmitBatch_Result;
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
	
	/**
	 * getSampleImage returns an image for the given project ID, provided in 
	 * the parameter wrapper class
	 * @param params a wrapper class of type GetSampleImage_params that contains
	 * the username, password, and a project id.
	 * @return GetSampleImage_Result, a wrapper class that contains a sample image url.
	 */
	public GetSampleImage_Result getSampleImage(GetSampleImage_Params params) {
		return null;
	}
	
	/**
	 * Downloads a batch for the user to index. The parameters and return value
	 * are contained in wrapper classes.
	 * @param params wrapper class that contains the username, password and the project ID.
	 * @return an object of type DownloadBatch_Result that contains all the information for the batch.
	 */
	public DownloadBatch_Result downloadBatch(DownloadBatch_Params params) {
		return null;
	}
	
	/**
	 * Submits the batch to the server, allowing it to be searched by key word
	 * @param params wrapper class that contains all the parameters for this method.
	 * @return wrapper class that contains a boolean value signifying the successful
	 * or unsuccessful completion of the method call.
	 */
	public SubmitBatch_Result submitBatch(SubmitBatch_Params params) {
		return null;
	}
	
	/**
	 * Returns information about all of the fields for a specified project
	 * If no project is specified, this should return information about all the 
	 * fields for all the projects in the system.
	 * @param params Wrapper class for the input parameters. If the project_id is 
	 * equal to 0, then it should return information about all the projects.
	 * @return wrapper class that contains an array of fields.
	 */
	public GetFields_Result getFields(GetFields_Params params) {
		return null;
	}
	
	/**
	 * Searches the indexed records for specified strings
	 * @param params contains the specified string and the field ids to be searched
	 * @return returns a wrapper containing a list of tuples with information about the 
	 * results of the search.
	 */
	public Search_Result search(Search_Params params) {
		return null;
	}
	/**
	 * Downloads the file from the server. Uses HTTP GET requests to download the data.
	 * @param params wrapper class containing the download url.
	 * @return wrapper class containing a byte array representing the downloaded data.
	 */
	public DownloadFile_Result downloadFile(DownloadFile_Params params) {
		return null;
	}
	
	
	
	
}

























