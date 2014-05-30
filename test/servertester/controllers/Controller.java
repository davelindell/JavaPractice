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
		
		String[] param_values = _view.getParameterValues();

		String record_values[] = param_values[3].split(",");
		List<IndexedData> indexed_data = new ArrayList<IndexedData>();
		for(String r : record_values) {
			IndexedData cur_data = new IndexedData();
			cur_data.setRecord_value(r);
		}
		
		SubmitBatch_Params params = new SubmitBatch_Params(param_values[0], param_values[1], Integer.parseInt(param_values[2]), indexed_data);
		
		try {
			SubmitBatch_Result result = cc.submitBatch(params);
			System.out.println(result.toString());
		} catch (ClientException e) {
			System.out.println("FAILED\n");
		}
	}
	
	private void search() {
	}

}

