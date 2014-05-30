package servertester.controllers;

import java.util.*;

import client.ClientException;
import client.communication.ClientCommunicator;
import servertester.views.*;
import shared.communication.*;
import shared.models.IndexedData;


public class Controller implements IController {

	private IView _view;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	// IController methods
	//
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("39640");
		operationSelected();
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}
	
	private void validateUser() {
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		
		String[] param_values = _view.getParameterValues();

		ValidateUser_Params params = new ValidateUser_Params(param_values[0], param_values[1]);
		
		try {
			ValidateUser_Result result = cc.validateUser(params);
			System.out.println(result.toString());
		} catch (ClientException e) {
			System.out.println("FAILED\n");
		}
		
	}
	
	private void getProjects() {
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		
		String[] param_values = _view.getParameterValues();

		GetProjects_Params params = new GetProjects_Params(param_values[0], param_values[1]);
		
		try {
			GetProjects_Result result = cc.getProjects(params);
			System.out.println(result.toString());
		} catch (ClientException e) {
			System.out.println("FAILED\n");
		}
	}
	
	private void getSampleImage() {
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		
		String[] param_values = _view.getParameterValues();

		GetSampleImage_Params params = new GetSampleImage_Params(param_values[0], param_values[1], Integer.parseInt(param_values[2]));
		
		try {
			GetSampleImage_Result result = cc.getSampleImage(params);
			System.out.println(result.toString());
		} catch (ClientException e) {
			System.out.println("FAILED\n");
		}
	}
	
	private void downloadBatch() {
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		
		String[] param_values = _view.getParameterValues();

		DownloadBatch_Params params = new DownloadBatch_Params(param_values[0], param_values[1], Integer.parseInt(param_values[2]));
		
		try {
			DownloadBatch_Result result = cc.downloadBatch(params);
			System.out.println(result.toString());
		} catch (ClientException e) {
			System.out.println("FAILED\n");
		}
	}
	
	private void getFields() {
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		
		String[] param_values = _view.getParameterValues();

		GetFields_Params params = new GetFields_Params(param_values[0], param_values[1], Integer.parseInt(param_values[2]));
		
		try {
			GetFields_Result result = cc.getFields(params);
			System.out.println(result.toString());
		} catch (ClientException e) {
			System.out.println("FAILED\n");
		}
	}
	
	private void submitBatch() {
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		List<List<IndexedData>> records = new ArrayList<List<IndexedData>>();
		
		String[] param_values = _view.getParameterValues();
		int batch_id = Integer.parseInt(param_values[2]);
		
		String record_strings[] = param_values[3].split(";");
		List<IndexedData> indexed_data = new ArrayList<IndexedData>();
		
		for(int i = 0; i < record_strings.length; ++i) {
			records.add(new ArrayList<IndexedData>());
			String[] data = record_strings[i].split(",");
			for (int j = 0; j < data.length; ++j) {
				IndexedData cur_data = new IndexedData();
				cur_data.setBatch_id(batch_id);
				cur_data.setRecord_value(data[j]);
				records.get(i).add(cur_data);
			}
		}
		
		SubmitBatch_Params params = new SubmitBatch_Params(param_values[0], param_values[1], batch_id, records);
		
		try {
			SubmitBatch_Result result = cc.submitBatch(params);
			System.out.println(result.toString());
		} catch (ClientException e) {
			System.out.println("FAILED\n");
		}
	}
	
	private void search() {
		ClientCommunicator cc = new ClientCommunicator(_view.getHost(), _view.getPort());
		
		String[] param_values = _view.getParameterValues();

		Search_Params params = new Search_Params(param_values[0], param_values[1], param_values[2], param_values[3]);
		
		try {
			Search_Result result = cc.search(params);
			System.out.println(result.toString());
		} catch (ClientException e) {
			System.out.println("FAILED\n");
		}
	
	
	
	}

}

