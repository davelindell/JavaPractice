package client.synchronizer;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import server.database.DatabaseException;
import shared.communication.DownloadBatch_Params;
import shared.communication.DownloadBatch_Result;
import shared.communication.DownloadFile_Params;
import shared.communication.DownloadFile_Result;
import shared.communication.GetProjects_Params;
import shared.communication.GetProjects_Result;
import shared.communication.SubmitBatch_Params;
import shared.models.Field;
import shared.models.IndexedData;
import shared.models.Project;
import shared.models.User;
import client.ClientException;
import client.communication.ClientCommunicator;
import client.qualitychecker.QualityChecker;

import org.apache.commons.io.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class BatchState extends JPanel {
	private ClientCommunicator cc;
	private User user;
	
	// synch info
	private int cur_row;
	private int cur_column;
	private boolean image_inverted;
	private boolean highlights_visible;
	private double zoom_level;
	private int image_pos_x;
	private int image_pos_y;
	private transient BufferedImage image;
	
	// batch information
	private int batch_id;
	private List<Field> fields;
	private int first_y_coord;
	private int first_x_coord;
	private String image_url;
	private int num_fields;
	private int num_records;
	private int project_id;
	private int record_height;
	private List<List<IndexedData>> records;
	private List<String> field_helps;
	private List<Project> projects;
	
	//qualitychecker
	private List<List<Boolean>> quality_entries;
	private List<String> known_values;
	private QualityChecker quality_checker;
	
	// positions
	private Point frame_location;
	private Dimension frame_size;
	private int horz_divider_location;
	private int vert_divider_location;
	
	private transient List<BatchStateListener> listeners;
	
	public BatchState(String hostname, String port) {
		listeners = new ArrayList<BatchStateListener>();
		cc = new ClientCommunicator(hostname, port);
		projects = new ArrayList<Project>();
		init();
	}
	
	public void init() {
		batch_id = 0;
		fields = new ArrayList<Field>();
		image = null;
		first_y_coord = 0;
		first_x_coord = 0;
		image_url = null;
		num_fields = 0;
		num_records = 0;
		project_id = 0;
		record_height = 0;
		records = new ArrayList<List<IndexedData>>();
		field_helps = new ArrayList<String>();
		cur_row = 0;
		cur_column = 1;
		quality_entries = new ArrayList<List<Boolean>>();
		known_values = new ArrayList<String>();
		quality_checker = new QualityChecker();
		image_inverted = false;
		highlights_visible = false;
		zoom_level = 1;
		frame_location = null;
		frame_size = null;
		horz_divider_location = 0;
		vert_divider_location = 0;
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
	
	public List<Project> getProjects() {
		return projects;
	}
	
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	public int getProjectId() {
		return this.project_id;
	}

	public List<Field> getFields() {
		return fields;
	}

	public BufferedImage getImage() {
		return image;
	}
	
	public int getFirstYCoord() {
		return first_y_coord;
	}

	public int getFirstXCoord() {
		return first_x_coord;
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
	

	public int getRecordHeight() {
		return record_height;
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
	
	public void setImageInverted(boolean state) {
		image_inverted = state;
	}
	
	public boolean getImageInverted() {
		return image_inverted;
	}
	
	public boolean getHighlightsVisible() {
		return highlights_visible;
	}

	public void setHighlightsVisible(boolean highlights_visible) {
		this.highlights_visible = highlights_visible;
	}

	public double getZoomLevel() {
		return zoom_level;
	}

	public void setZoomLevel(double zoom_level) {
		this.zoom_level = zoom_level;
	}

	public int getImagePosX() {
		return image_pos_x;
	}

	public int getImagePosY() {
		return image_pos_y;
	}
	
	public void setImagePosX(int image_pos_x) {
		this.image_pos_x = image_pos_x;
	}

	public void setImagePosY(int image_pos_y) {
		this.image_pos_y = image_pos_y;
	}

	public List<String> getFieldHelps() {
		return field_helps;
	}

	
	public List<String> getKnownValues() {
		return known_values;
	}

	public List<List<Boolean>> getQualityEntries() {
		return quality_entries;
	}

	public QualityChecker getQualityChecker() {
		return quality_checker;
	}

	public Point getFrameLocation() {
		return frame_location;
	}

	public void setFrameLocation(Point frame_location) {
		this.frame_location = frame_location;
	}

	public Dimension getFrameSize() {
		return frame_size;
	}

	public void setFrameSize(Dimension frame_size) {
		this.frame_size = frame_size;
	}

	public int getHorzDividerLocation() {
		return horz_divider_location;
	}

	public void setHorzDividerLocation(int horz_divider_location) {
		this.horz_divider_location = horz_divider_location;
	}

	public int getVertDividerLocation() {
		return vert_divider_location;
	}

	public void setVertDividerLocation(int vert_divider_location) {
		this.vert_divider_location = vert_divider_location;
	}

	public List<BatchStateListener> getListeners() {
		return listeners;
	}
	
	public boolean isQuality(int row, int column) {
		// convert from table to img coordinates
		if (column == 0) 
			column = 1;
		return quality_entries.get(row).get(column - 1);
	}

	public List<String> getSuggestions(int row, int column) {
		// change to table coordinates
		if (column == 0)
			column = 1;
		
		String cur_entry = records.get(row).get(column - 1).getRecord_value();
		String known_field_values = known_values.get(column - 1);
		
		return quality_checker.getSuggestions(known_field_values, cur_entry);		
	}
	
	public void pushLogout() {
		pushSave();
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
		
		try {
			result = cc.downloadBatch(batch_params);
			this.batch_id = result.getBatch_id();
			this.fields = result.getFields();
			this.first_y_coord = result.getFirst_y_coord();
			this.first_x_coord = fields.get(0).getX_coord();
			this.image_url = result.getImage_url();
			this.num_fields = result.getNum_fields();
			this.num_records = result.getNum_records();
			this.project_id = result.getProject_id();
			this.record_height = result.getRecord_height();
			
			file_result = cc.downloadFile(image_url);
			image = ImageIO.read(new ByteArrayInputStream(file_result.getFile_download()));
			
			
			for(Field f : fields) {
				String help_url = f.getHelp_url();
				file_result = cc.downloadFile(help_url);
				String field_help = new String(file_result.getFile_download());
				field_helps.add(field_help);
				
				String known_values_url = f.getKnown_values_url();
				String values = null;
				if (known_values_url != null) {
					file_result = cc.downloadFile(known_values_url);
					values = new String(file_result.getFile_download());
				}
				known_values.add(values);
			}
			
		} catch (ClientException e) {
			JOptionPane.showMessageDialog(this, "Connection Error", 
					  "Download Batch Failed", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "File Read Error", 
					  "Download Batch Failed", JOptionPane.ERROR_MESSAGE);
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(this, "Connection Error", 
					  "Download Batch Failed", JOptionPane.ERROR_MESSAGE);
		}
			
		// init indexed data for when we submit the batch
		for (int i = 0; i < num_records; ++i) {
			records.add(new ArrayList<IndexedData>());
			quality_entries.add(new ArrayList<Boolean>());
			for (int j = 0; j < num_fields; ++j) {
				IndexedData cur_data = new IndexedData();
				cur_data.setBatch_id(batch_id);
				cur_data.setRecord_value("");				
				records.get(i).add(cur_data);
				quality_entries.get(i).add(true);
			}
		}
		
		
		for (BatchStateListener l : listeners) {
			l.fireDownloadBatch();
		}
	}
	
	public void pushInvertImage() {
		for (BatchStateListener l : listeners) {
			l.fireInvertImage();
		}
	}
	
	public void pushToggleHighlights() {
		for (BatchStateListener l : listeners) {
			l.fireToggleHighlights();
		}
	}
	
	
	public void pushSubmitBatch() {
		SubmitBatch_Params params = 
					new SubmitBatch_Params(user.getUsername(), user.getPassword(), batch_id, records);
		
		try {
			cc.submitBatch(params);
		} catch (ClientException e) {
			JOptionPane.showMessageDialog(this, "Connection Error", 
					  "Submit Batch Failed", JOptionPane.ERROR_MESSAGE);
		}
		
		init();
		
		for (BatchStateListener l : listeners) {
			l.fireSubmitBatch();
		}
		pushSave();
	}
	
	public void pushChangeSelectedEntry(int row, int column) {
		cur_row = row;
		cur_column = column;
		
		for (BatchStateListener l : listeners) {
			l.fireChangeSelectedEntry(cur_row, cur_column);
		}
	}
	
	public void pushEnteredData(int row, int column) {
		// change to table coordinates
		int table_column = column;
		if (column == 0)
			column = 1;
		
		String cur_entry = records.get(row).get(column - 1).getRecord_value();
		String known_field_values = known_values.get(column - 1);
		
		boolean found = quality_checker.checkWord(known_field_values, cur_entry);
		quality_entries.get(row).set(column - 1, found);
		
		for (BatchStateListener l : listeners) {
			l.fireEnteredData(row, table_column);
		}
	}
	
	public void pushSave() {		
		for (BatchStateListener l : listeners) { 
			l.fireSave();	
		}
		
		File xml_file = new File("save" + File.separator + user.getUsername() + ".xml");
		File img_file = new File("save" + File.separator + user.getUsername() + ".png");
		try {
			Files.deleteIfExists(xml_file.toPath());
			Files.deleteIfExists(img_file.toPath());
			xml_file.createNewFile();
			img_file.createNewFile();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(this, "Error", 
					  "Error Saving File", JOptionPane.ERROR_MESSAGE);
		}
		
		XStream xstream = new XStream(new DomDriver());
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream("save" + File.separator + user.getUsername() + ".xml"));
			xstream.toXML(this, out);
			out.close();
			out = new BufferedOutputStream(new FileOutputStream("save" + File.separator + user.getUsername() + ".png"));
			if(image != null)
				ImageIO.write(image, "png", out);
			out.close();
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Error", 
					  "Error Saving File", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error", 
					  "Error Saving File", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public void pushLoad() {
		init();
		
		File xml_file = new File("save" + File.separator + user.getUsername() + ".xml");
		File img_file = new File("save" + File.separator + user.getUsername() + ".png");

		BatchState batch_state = null;
		XStream xstream = new XStream(new DomDriver());
		BufferedInputStream in = null;
		if (xml_file.exists() && img_file.exists()) {
			try {
				in = new BufferedInputStream(new FileInputStream("save" + File.separator + user.getUsername() + ".xml"));
				batch_state = (BatchState) xstream.fromXML(in);
				in.close();
				
				in = new BufferedInputStream(new FileInputStream("save" + File.separator + user.getUsername() + ".png"));
				image = ImageIO.read(in);
				in.close();
				
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, "Error", 
						  "Error Saving File", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Error", 
						  "Error Saving File", JOptionPane.ERROR_MESSAGE);
			}
			
			cc = batch_state.getClientCommunicator();
			user = batch_state.getUser();
			
			cur_row = batch_state.getCurRow();
			cur_column = batch_state.getCurColumn();
			image_inverted = batch_state.getImageInverted();
			highlights_visible = batch_state.getHighlightsVisible();
			zoom_level = batch_state.getZoomLevel();
			image_pos_x = batch_state.getImagePosX();
			image_pos_y = batch_state.getImagePosY();
			
			batch_id = batch_state.getBatchID();
			fields = batch_state.getFields();
			first_y_coord = batch_state.getFirstYCoord();
			first_x_coord = batch_state.getFirstXCoord();
			image_url = batch_state.getImageUrl();
			num_fields = batch_state.getNumFields();
			num_records = batch_state.getNumRecords();
			project_id = batch_state.getProjectId();
			record_height = batch_state.getRecordHeight();
			records = batch_state.getRecords();
			field_helps = batch_state.getFieldHelps();
			projects = batch_state.getProjects();
			
			quality_entries = batch_state.getQualityEntries();
			known_values = batch_state.getKnownValues();
			quality_checker = batch_state.getQualityChecker();
			
			frame_location = batch_state.getFrameLocation();
			frame_size = batch_state.getFrameSize();
			horz_divider_location = batch_state.getHorzDividerLocation();
			vert_divider_location = batch_state.getVertDividerLocation();

			for (BatchStateListener l : listeners) { 
				l.fireLoad();	
			}
			
		}
	}
	

	
}


