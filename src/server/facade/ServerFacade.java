package server.facade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

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
	
	private Logger logger = Logger.getLogger("record_server"); 
	
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
			db.startTransaction();
			List<Batch> batches = db.getBatchDAO().getAll();
			db.endTransaction(true);
			Iterator<Batch> iter = batches.iterator();
			boolean found_open_batch = false;
			
			while (iter.hasNext() && !found_open_batch){
				Batch batch = iter.next();
				
				if (batch.getCur_username().equals("")) {
					found_open_batch = true;
					batch.setCur_username(username);
					
					db.startTransaction();
					db.getBatchDAO().update(batch);
					db.endTransaction(true);
				
					result.setBatch_id(batch.getBatch_id());
					result.setFirst_y_coord(batch.getFirst_y_coord());
					result.setImage_url(batch.getImage_url());
					result.setNum_fields(batch.getNum_fields());
					result.setNum_records(batch.getNum_records());
					result.setProject_id(batch.getProject_id());
					result.setRecord_height(batch.getRecord_height());
					db.startTransaction();
					result.setFields(db.getFieldDAO().getBatchFields(batch.getProject_id()));
					db.endTransaction(true);
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
			List<List<IndexedData>> records = params.getRecords();
			int field_id = 0;
			List<Field> fields = null;
			int batch_id = params.getBatch_id();
			
			db.startTransaction();
			Batch cur_batch = db.getBatchDAO().getBatch(batch_id);
			db.endTransaction(true);
			
			if (cur_batch.getCur_username().equals(params.getUsername())) {
				// retrieve the batch from the database and set the current user to empty
				cur_batch.setCur_username("");
				
				db.startTransaction();
				db.getBatchDAO().update(cur_batch);
				fields = db.getFieldDAO().getBatchFields(cur_batch.getProject_id());
				db.endTransaction(true);
				
				for (int i = 0; i < records.size(); ++i) {
					for (int j = 0; j < records.get(i).size(); ++j) {
						IndexedData cur_data = records.get(i).get(j);
						cur_data.setRecord_number(i + 1);
						cur_data.setField_id(fields.get(j).getField_id());
						db.startTransaction();
						db.getIndexeddataDAO().add(cur_data);
						db.endTransaction(true);
					}
				}
				
				result.setValid(true);
			}	
			else
				result.setValid(false);
			
			
			
			
					
		}
		
		return result;
	}
	
	public GetFields_Result getFields(GetFields_Params params) throws DatabaseException {
		Database db = new Database();
		ValidateUser_Params validate_user_params = new ValidateUser_Params(params.getUsername(), params.getPassword());
		GetFields_Result result = new GetFields_Result(null);
		
		if (validateUser(validate_user_params).isValid()) {
			List<Field> fields = null;
			if (params.getProject_id() == 0){
				db.startTransaction();
				fields = db.getFieldDAO().getAll();
				db.endTransaction(true);
			}
				
			else {
				db.startTransaction();
				fields = db.getFieldDAO().getBatchFields(params.getProject_id());
				db.endTransaction(true);
			}
				
			
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
					db.startTransaction();
					record_results.addAll(db.getIndexeddataDAO().getByFieldAndValue(Integer.parseInt(ids), entry.toLowerCase()));
					db.endTransaction(true);
				}
			}
			
			for (IndexedData record : record_results) {
				SearchResultTuple search_result_tuple = new SearchResultTuple();
				search_result_tuple.setBatch_id(record.getBatch_id());
				search_result_tuple.setField_id(record.getField_id());
				search_result_tuple.setRecord_number(record.getRecord_number());
				db.startTransaction();
				search_result_tuple.setImage_url(db.getBatchDAO().getBatch(record.getBatch_id()).getImage_url());
				db.endTransaction(true);
				matches.add(search_result_tuple);
			}
			result.setMatches(matches);
		}
		return result;
	}

	public DownloadFile_Result downloadFile(String url) throws IOException {
		
		url = url.substring(1,url.length());
		InputStream input_stream = new FileInputStream(url);
		byte[] data = null;
		data = IOUtils.toByteArray(input_stream);
		//boolean null_data = data == null;
		//logger.fine(Boolean.toString(null_data));
		return new DownloadFile_Result(data);
	}
}
