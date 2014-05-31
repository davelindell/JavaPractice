package client.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import client.ClientException;
import servertester.controllers.Controller;
import shared.communication.DownloadBatch_Params;
import shared.communication.DownloadBatch_Result;
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
	private Controller controller;
	private String host_url;
	private String port;

	public ClientCommunicator(String url, String port, Controller controller) {
		this.host_url = url;
		this.port = port;
		this.controller = controller;
	}
	
	public ClientCommunicator(String url, String port) {
		this.host_url = url;
		this.port = port;
		this.controller = null;
	}
	
	/** 
	 * ValidateUser takes a wrapper class and returns a wrapper
	 * with a User object inside of it. The method queries the server to see 
	 * if there is a valid user with the information provided in the parameter.
	 * 
	 * @param params object of type ValidateUser_Params which is a wrapper for 
	 * strings which describe the user.
	 * @return ValidateUser_Result, a wrapper class for a User, which is not null
	 * if the validation was successful.
	 * @throws ClientException 
	 */
	
	public ValidateUser_Result validateUser(ValidateUser_Params params) throws ClientException {
		return (ValidateUser_Result)doPost("http://" + host_url + ":" + port + "/ValidateUser", params);
	}
	
	/**
	 * getProjects queries the server with the project information given in
	 * the wrapper class parameter and returns information about all the 
	 * available projects.
	 * @param params a wrapper class of type GetProject_Params that contains
	 * the user's name and password.
	 * @return GetProject_Result, a wrapper class that contains info on all current 
	 * projects.
	 * @throws ClientException 
	 */
	public GetProjects_Result getProjects(GetProjects_Params params) throws ClientException {
		return (GetProjects_Result)doPost("http://" + host_url + ":" + port + "/GetProjects", params);
	}
	
	/**
	 * getSampleImage returns an image for the given project ID, provided in 
	 * the parameter wrapper class
	 * @param params a wrapper class of type GetSampleImage_params that contains
	 * the username, password, and a project id.
	 * @return GetSampleImage_Result, a wrapper class that contains a sample image url.
	 * @throws ClientException 
	 */
	public GetSampleImage_Result getSampleImage(GetSampleImage_Params params) throws ClientException {
		return (GetSampleImage_Result)doPost("http://" + host_url + ":" + port + "/GetSampleImage", params);

	}
	
	/**
	 * Downloads a batch for the user to index. The parameters and return value
	 * are contained in wrapper classes.
	 * @param params wrapper class that contains the username, password and the project ID.
	 * @return an object of type DownloadBatch_Result that contains all the information for the batch.
	 * @throws ClientException 
	 */
	public DownloadBatch_Result downloadBatch(DownloadBatch_Params params) throws ClientException {
		return (DownloadBatch_Result)doPost("http://" + host_url + ":" + port + "/DownloadBatch", params);
	}
	
	/**
	 * Submits the batch to the server, allowing it to be searched by key word
	 * @param params wrapper class that contains all the parameters for this method.
	 * @return wrapper class that contains a boolean value signifying the successful
	 * or unsuccessful completion of the method call.
	 * @throws ClientException 
	 */
	public SubmitBatch_Result submitBatch(SubmitBatch_Params params) throws ClientException {
		return (SubmitBatch_Result)doPost("http://" + host_url + ":" + port + "/SubmitBatch", params);
	}
	
	/**
	 * Returns information about all of the fields for a specified project
	 * If no project is specified, this should return information about all the 
	 * fields for all the projects in the system.
	 * @param params Wrapper class for the input parameters. If the project_id is 
	 * equal to 0, then it should return information about all the projects.
	 * @return wrapper class that contains an array of fields.
	 * @throws ClientException 
	 */
	public GetFields_Result getFields(GetFields_Params params) throws ClientException {
		return (GetFields_Result)doPost("http://" + host_url + ":" + port + "/GetFields", params);
	}
	
	/**
	 * Searches the indexed records for specified strings
	 * @param params contains the specified string and the field ids to be searched
	 * @return returns a wrapper containing a list of tuples with information about the 
	 * results of the search.
	 * @throws ClientException 
	 */
	public Search_Result search(Search_Params params) throws ClientException {
		return (Search_Result)doPost("http://" + host_url + ":" + port + "/Search", params);
	}
	/**
	 * Downloads the file from the server. Uses HTTP GET requests to download the data.
	 * @param params wrapper class containing the download url.
	 * @return wrapper class containing a byte array representing the downloaded data.
	 * @throws ClientException 
	 */
	public DownloadFile_Result downloadFile(String url) throws ClientException {
		return new DownloadFile_Result(doGet("http://" + host_url + ":" + port + "/" + url));
	}
	
	private byte[] doGet(String urlPath) throws ClientException {
		// Make HTTP GET request to the specified URL, 
		// and return the object returned by the server
		byte[] result = null;
		
		try {
			URL url = new URL(urlPath);

			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			   
			connection.setRequestMethod("GET");	   
			
			if(controller != null)
				controller.getView().setRequest(connection.getRequestMethod());

			// Set HTTP request headers, if necessary
			// connection.addRequestProperty(”Accept”, ”text/html”);
			
			connection.connect();
			   
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// Get HTTP response headers, if necessary
				// Map<String, List<String>> headers = connection.getHeaderFields();
		
				InputStream response_body = connection.getInputStream();
				// Read response body from InputStream ...
				
				result = IOUtils.toByteArray(response_body);
			}
			else {
				// SERVER RETURNED AN HTTP ERROR
				//System.out.println("HTTP Error");
				throw new ClientException();
			}
		}
		catch (IOException e) {
			// IO ERROR
			//System.out.println("IO Error");
			throw new ClientException();
		} 
			
		return result;
	}
	
	private Object doPost(String urlPath, Object postData) throws ClientException {
		// Make HTTP POST request to the specified URL, 
		// passing in the specified postData object
		XStream xml_stream = new XStream(new DomDriver());
		Object result_obj = null;
		
		
		try {
			URL url = new URL(urlPath);
			   
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			   
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.connect();
			OutputStream requestBody = connection.getOutputStream();
			
			// Write request body to OutputStream ...
			xml_stream.toXML(postData, requestBody);
			
			if(controller != null)
				controller.getView().setRequest(connection.getRequestMethod() + "\n" + xml_stream.toXML(postData));
			
			requestBody.close();
			
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// Get HTTP response headers, if necessary
				// Map<String, List<String>> headers = connection.getHeaderFields();

				InputStream responseBody = connection.getInputStream();
				// Read response body from InputStream ...
				result_obj = xml_stream.fromXML(responseBody);
			}
			else {
				// SERVER RETURNED AN HTTP ERROR
				//System.out.println("HTTP Error");
				throw new ClientException();
			}
		}
		catch (IOException e) {
			// IO ERROR
			//System.out.println("IO Error");
			throw new ClientException();
		} 
		return result_obj;
	}
	
}




