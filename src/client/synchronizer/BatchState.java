package client.synchronizer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import shared.communication.DownloadBatch_Params;
import shared.communication.DownloadBatch_Result;
import shared.communication.DownloadFile_Params;
import shared.communication.DownloadFile_Result;
import shared.models.Field;
import shared.models.IndexedData;
import shared.models.User;
import client.ClientException;
import client.communication.ClientCommunicator;

public class BatchState extends JPanel {
	private boolean has_batch;
	private ClientCommunicator cc;
	private User user;
	
	// synch info
	private int cur_row;
	private int cur_column;

	// batch information
	private int batch_id;
	private List<Field> fields;
	private int first_y_coord;
	private String image_url;
	private int num_fields;
	private int num_records;
	private int project_id;
	private List<List<IndexedData>> records;
	private List<String> field_helps;
	
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
		records = new ArrayList<List<IndexedData>>();
		field_helps = new ArrayList<String>();

		cur_row = -1;
		cur_column =-1;
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
	
	public int getProjectId() {
		return this.project_id;
	}

	public List<Field> getFields() {
		return fields;
	}

	public int getFirstYCoord() {
		return first_y_coord;
	}

	public String getImageUrl() {
		return image_url;
	}

	public int getNumFields() {
		return num_fields;
	}

	public int getNumRecords() {
		return num_records;
	}

	public int getBatchID() {
		return batch_id;
	}

	public List<List<IndexedData>> getRecords() {
		return records;
	}

	public void setRecords(List<List<IndexedData>> records) {
		this.records = records;
	}
	
	public int getCurRow() {
		return cur_row;
	}

	public void setCurRow(int cur_row) {
		this.cur_row = cur_row;
	}

	public int getCurColumn() {
		return cur_column;
	}

	public void setCurColumn(int cur_column) {
		this.cur_column = cur_column;
	}
	
	public List<String> getFieldHelps() {
		return field_helps;
	}

	public List<BatchStateListener> getListeners() {
		return listeners;
	}

	public void pushLogout() {		
		for (BatchStateListener l : listeners) {
			l.fireLogoutButton();
		}
	}
	
	public void pushZoomIn() {
		for (BatchStateListener l : listeners) {
			l.fireZoomInButton();
		}
	}
	
	public void pushZoomOut() {
		for (BatchStateListener l : listeners) {
			l.fireZoomOutButton();
		}
	}
	
	public void pushDownloadBatch(int project_id) {
		DownloadBatch_Params batch_params = new DownloadBatch_Params(user.getUsername(), user.getPassword(), project_id);
		DownloadBatch_Result result = null;
		DownloadFile_Result file_result = null;
		
		BufferedImage batch_image = null;
		try {
			result = cc.downloadBatch(batch_params);
			this.batch_id = result.getBatch_id();
			this.fields = result.getFields();
			this.first_y_coord = result.getFirst_y_coord();
			this.image_url = result.getImage_url();
			this.num_fields = result.getNum_fields();
			this.num_records = result.getNum_records();
			this.project_id = result.getProject_id();
			
			file_result = cc.downloadFile(image_url);
			batch_image = ImageIO.read(new ByteArrayInputStream(file_result.getFile_download()));
			
			for(Field f : fields) {
				String help_url = f.getHelp_url();
				file_result = cc.downloadFile(help_url);
				String field_help = new String(file_result.getFile_download());
				field_helps.add(field_help);
			}
			
		} catch (ClientException e) {
			JOptionPane.showMessageDialog(this, "Connection Error", 
					  "Download Batch Failed", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "File Read Error", 
					  "Download Batch Failed", JOptionPane.ERROR_MESSAGE);
		}
			
		// init indexed data for when we submit the batch
		for (int i = 0; i < num_records; ++i) {
			records.add(new ArrayList<IndexedData>());
			
			for (int j = 0; j < num_fields; ++j) {
				IndexedData cur_data = new IndexedData();
				cur_data.setBatch_id(batch_id);
				cur_data.setRecord_value("");				
				records.get(i).add(cur_data);
			}
		}
		
		
		for (BatchStateListener l : listeners) {
			l.fireDownloadBatch(batch_image);
		}
	}
	
	public void pushInvertImage() {
		for (BatchStateListener l : listeners) {
			l.fireInvertImage();
		}
	}
	
	public void pushSubmitBatch() {
		for (BatchStateListener l : listeners) {
			l.fireSubmitBatch();
		}
	}
	
	public void pushChangeSelectedEntry(int row, int column) {
		cur_row = row;
		cur_column = column;
		for (BatchStateListener l : listeners) {
			l.fireChangeSelectedEntry(row, column);
		}
	}
}


