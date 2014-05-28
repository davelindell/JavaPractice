package server.facade;

import java.util.List;

import server.database.Database;
import server.database.DatabaseException;
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
import shared.models.*;

public class ServerFacade {
	
	public static void initialize() throws DatabaseException {		
		try {
			Database.initialize();		
		}
		catch (DatabaseException e) {
			throw new DatabaseException(e.getMessage(), e);
		}		
	}
	
	public ValidateUser_Result validateUser(ValidateUser_Params params) throws DatabaseException {
		Database db = new Database();
		
		try {
			db.startTransaction();
			List<User> users = db.getUserDAO().getAll();
			db.endTransaction(true);
			
			boolean found_user = false;
			User authenticated_user = null;
			for (User user : users) {
				if (params.getUsername().equals(user.getUsername()) &&
					params.getPassword().equals(user.getPassword())) {
					found_user = true;
					authenticated_user = user;
				}
			}
			
			ValidateUser_Result result = new ValidateUser_Result(authenticated_user, found_user);
			return result;
		}
		catch (DatabaseException e) {
			db.endTransaction(false);
			throw new DatabaseException(e.getMessage(), e);
		}
	}
	
	public GetProjects_Result getProjects(GetProjects_Params params) throws DatabaseException {
		Database db = new Database();
		ValidateUser_Params validate_user_params = new ValidateUser_Params(params.getUsername(), params.getPassword());
		
		if (validateUser(validate_user_params).isValid()) {
			try {
				db.startTransaction();
				List<Project> projects = db.getProjectDAO().getAll();
				db.endTransaction(true);
				
				GetProjects_Result result = new GetProjects_Result(projects);
				return result;
			}
			catch (DatabaseException e) {
				db.endTransaction(false);
				throw new DatabaseException(e.getMessage(), e);
			}
		}
		
		else
			return new GetProjects_Result(null);
	}
	
	public GetSampleImage_Result getSampleImage(GetSampleImage_Params params) throws DatabaseException {
		Database db = new Database();
		ValidateUser_Params validate_user_params = new ValidateUser_Params(params.getUsername(), params.getPassword());
		
		if (validateUser(validate_user_params).isValid()) {
			try {
				db.startTransaction();
				List<Batch> batches = db.getBatchDAO().getAll();
				db.endTransaction(true);
				String sample_image = null;
				if(!batches.isEmpty())
					sample_image = batches.get(0).getImage_url();
				
				GetSampleImage_Result result = new GetSampleImage_Result(sample_image);
				return result;
			}
			catch (DatabaseException e) {
				db.endTransaction(false);
				throw new DatabaseException(e.getMessage(), e);
			}
		}
		
		else
			return new GetSampleImage_Result(null);
	}
	
	public DownloadBatch_Result downloadBatch(DownloadBatch_Params params) {
		
		
		
		
		return null;
	}
	
	public SubmitBatch_Result submitBatch(SubmitBatch_Params params) {
		return null;
	}
	
	public GetFields_Result getFields(GetFields_Params params) {
		return null;
	}
	
	public Search_Result search(Search_Params params) {
		return null;
	}

	public DownloadFile_Result downloadFile(DownloadFile_Params params) {
		return null;
	}
	
	
}
