package server.facade;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
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
import shared.communication.SearchResultTuple;
import shared.communication.Search_Params;
import shared.communication.Search_Result;
import shared.communication.SubmitBatch_Params;
import shared.communication.SubmitBatch_Result;
import shared.communication.ValidateUser_Params;
import shared.communication.ValidateUser_Result;
import shared.models.*;

public class ServerFacade {
	
	public static void initialize() throws ServerFacadeException {		
		try {
			Database.initialize();		
		}
		catch (DatabaseException e) {
			throw new ServerFacadeException(e.getMessage(), e);
		}		
	}
	
	public ServerFacade() {
		
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
	
	public DownloadBatch_Result downloadBatch(DownloadBatch_Params params) throws DatabaseException {
		Database db = new Database();
		ValidateUser_Params validate_user_params = new ValidateUser_Params(params.getUsername(), params.getPassword());
		String username = params.getUsername();
		DownloadBatch_Result result = new DownloadBatch_Result();
		
		if (validateUser(validate_user_params).isValid()) {
			
			List<Batch> batches = db.getBatchDAO().getAll();
			Iterator<Batch> iter = batches.iterator();
			boolean found_open_batch = false;
			
			while (iter.hasNext() && !found_open_batch){
				Batch batch = iter.next();
				
				if (batch.getCur_username().equals(null)) {
					found_open_batch = true;
					batch.setCur_username(username);
					db.getBatchDAO().update(batch);
				
					result.setBatch_id(batch.getBatch_id());
					result.setFirst_y_coord(batch.getFirst_y_coord());
					result.setImage_url(batch.getImage_url());
					result.setNum_fields(batch.getNum_fields());
					result.setNum_records(batch.getNum_records());
					result.setProject_id(batch.getProject_id());
					result.setRecord_height(batch.getRecord_height());
					result.setFields(db.getFieldDAO().getBatchFields(batch.getProject_id()));
				}
			}
		}
		return result;
	}
	
	public SubmitBatch_Result submitBatch(SubmitBatch_Params params) throws DatabaseException {
		Database db = new Database();
		ValidateUser_Params validate_user_params = new ValidateUser_Params(params.getUsername(), params.getPassword());
		SubmitBatch_Result result = new SubmitBatch_Result(false);
		
		if (validateUser(validate_user_params).isValid()) {
			List<IndexedData> data = params.getIndexed_data();
			for (IndexedData datum : data) {
				db.getIndexeddataDAO().add(datum);
			}
			if (!data.isEmpty()) {
				// get batch id from one of the data records
				int batch_id = data.get(0).getBatch_id();
				
				// retrieve the batch from the database and set the current user to null
				Batch cur_batch = db.getBatchDAO().getBatch(batch_id);
				cur_batch.setCur_username(null);
				db.getBatchDAO().update(cur_batch);
			}
			result.setValid(true);
		}
		
		return result;
	}
	
	public GetFields_Result getFields(GetFields_Params params) throws DatabaseException {
		Database db = new Database();
		ValidateUser_Params validate_user_params = new ValidateUser_Params(params.getUsername(), params.getPassword());
		GetFields_Result result = new GetFields_Result(null);
		
		if (validateUser(validate_user_params).isValid()) {
			List<Field> fields = null;
			if (params.getProject_id() == 0)
				fields = db.getFieldDAO().getAll();
			else
				fields = db.getFieldDAO().getBatchFields(params.getProject_id());
			
			result.setFields(fields);
		}
		
		return result;
	}
	
	public Search_Result search(Search_Params params) throws DatabaseException {
		Database db = new Database();
		ValidateUser_Params validate_user_params = new ValidateUser_Params(params.getUsername(), params.getPassword());
		Search_Result result = new Search_Result();
		List<SearchResultTuple> matches = new ArrayList<SearchResultTuple>();
		
		if (validateUser(validate_user_params).isValid()) {
			String[] search_entries = params.getSearch_strings().split(",");
			String[] field_ids = params.getField_ids().split(",");
			List<IndexedData> record_results = new ArrayList<IndexedData>();
			for (String entry : search_entries) {
				for (String ids : field_ids) {
					record_results.addAll(db.getIndexeddataDAO().getByFieldAndValue(Integer.parseInt(ids), entry));
				}
			}
			
			for (IndexedData record : record_results) {
				SearchResultTuple search_result_tuple = new SearchResultTuple();
				search_result_tuple.setBatch_id(record.getBatch_id());
				search_result_tuple.setField_id(record.getField_id());
				search_result_tuple.setRecord_number(record.getRecord_number());
				search_result_tuple.setImage_url(db.getBatchDAO().getBatch(record.getBatch_id()).getImage_url());
				matches.add(search_result_tuple);
			}
			result.setMatches(matches);
		}
		return result;
	}

	public DownloadFile_Result downloadFile(DownloadFile_Params params) throws IOException {
		Path path = Paths.get(params.getUrl());
		byte[] data = Files.readAllBytes(path);
		return new DownloadFile_Result(data);
	}
}
