package client.synchronizer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import shared.communication.DownloadBatch_Params;
import shared.communication.DownloadBatch_Result;
import shared.models.Field;
import shared.models.User;
import client.ClientException;
import client.communication.ClientCommunicator;

public class BatchState extends JPanel {
	private boolean has_batch;
	private ClientCommunicator cc;
	private User user;
	
	// batch information
	private int batch_id;
	private List<Field> fields;
	private int first_y_coord;
	private String image_url;
	private int num_fields;
	private int num_records;
	private int project_id;
	
	private List<BatchStateListener> listeners;
	
	public BatchState(String hostname, String port) {
		listeners = new ArrayList<BatchStateListener>();
		has_batch = false;
		cc = new ClientCommunicator(hostname, port);
		
		batch_id = 0;
		fields = null;
		first_y_coord = 0;
		image_url = null;
		num_fields = 0;
		num_records = 0;
		project_id = 0;
	}
	
	public void addListener(BatchStateListener listener) {
		listeners.add(listener);
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public ClientCommunicator getClientCommunicator() {
		return this.cc;
	}
	
	public void pushLogout() {		
		for (BatchStateListener l : listeners) {
			l.fireLogoutButton();
		}
	}
	
	public void pushDownloadBatch(int project_id) {
		DownloadBatch_Params params = new DownloadBatch_Params(user.getUsername(), user.getPassword(), project_id);
		DownloadBatch_Result result = null;
		try {
			result = cc.downloadBatch(params);
			this.batch_id = result.getBatch_id();
			this.fields = result.getFields();
			this.first_y_coord = result.getFirst_y_coord();
			this.image_url = result.getImage_url();
			this.num_fields = result.getNum_fields();
			this.num_records = result.getNum_records();
			this.project_id = result.getProject_id();
		} catch (ClientException e) {
			JOptionPane.showMessageDialog(this, "Connection Error", 
					  "Download Batch Failed", JOptionPane.ERROR_MESSAGE);
		}
		
		for (BatchStateListener l : listeners) {
			l.fireDownloadedBatch();
		}
	}
}


