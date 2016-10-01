package importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

import server.database.*;
import shared.models.*;

import org.apache.commons.io.*;

public class DataImporter {
	
	private Database db;
	
	public DataImporter() throws DatabaseException {
		db = new Database();
		Database.initialize();
	}
	
	public static void main(String[] args) {		
		File src = new File("database/empty_record_indexer.sqlite");
		File dst = new File("database/record_indexer.sqlite");
		try {
			Files.copy(	src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DataImporter importer;
		try {
			importer = new DataImporter();
			
			File xml_file = new File(args[0]);
			importer.parseUsers(xml_file);
			importer.parseProjectData(xml_file);
			importer.copyFiles(args[0]);
		}
		catch (DatabaseException e) {
			e.printStackTrace();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}
	
	public void copyFiles(String xml_path_str) throws IOException {
		File src_file = new File(xml_path_str);
		File dst_file = new File("records");

		//if(!src_file.getParentFile().getCanonicalPath().equals(dst_file.getCanonicalPath()))
		//	FileUtils.deleteDirectory(dst_file);
			
		FileUtils.copyDirectory(src_file.getParentFile(), dst_file);
	}
	
	public void parseProjectData(File xml_file) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(xml_file);
		
		NodeList project_list = doc.getElementsByTagName("project");
		
		for (int i = 0; i < project_list.getLength(); ++i) {
			
			Element project_elem = (Element)project_list.item(i);	
			Element project_title_elem = (Element)project_elem.getElementsByTagName("title").item(0);
			Element first_y_coord_elem = (Element)project_elem.getElementsByTagName("firstycoord").item(0);
			Element record_height_elem = (Element)project_elem.getElementsByTagName("recordheight").item(0);
			Element num_records_elem = (Element)project_elem.getElementsByTagName("recordsperimage").item(0);
			
			String project_title = project_title_elem.getTextContent();
			
			Project project = new Project();
			project.setProject_title(project_title);
			
			// These will be set in each batch object for easier client-server communication later on
			int first_y_coord = Integer.parseInt(first_y_coord_elem.getTextContent());
			int record_height = Integer.parseInt(record_height_elem.getTextContent());
			int num_records = Integer.parseInt(num_records_elem.getTextContent());
			int project_id = 0;
			int num_fields = project_elem.getElementsByTagName("field").getLength();
			
			db.startTransaction();
			try {
				project_id = db.getProjectDAO().add(project);
				db.endTransaction(true);
			}
			catch (DatabaseException e) {
				db.endTransaction(false);
				throw new DatabaseException();
			}
			
			List<Integer> field_ids = parseFields(project_elem, project_id);
			List<Integer> batch_ids = parseBatches(project_elem, project_id, first_y_coord, record_height, num_records, num_fields); 
			parseIndexedData(project_elem, project_id, field_ids, batch_ids);
			
		} 
	}
	
	public void parseIndexedData(Element project_elem, int project_id, List<Integer> field_ids, List<Integer> batch_ids) throws DatabaseException {
		NodeList image_list = project_elem.getElementsByTagName("image");
		for (int i = 0; i < image_list.getLength(); ++i) {		
			Element image_elem = (Element)image_list.item(i);
			NodeList record_list = image_elem.getElementsByTagName("record");
			
			for (int j = 0; j < record_list.getLength(); ++j) {
				Element record_elem = (Element)record_list.item(j);
				NodeList value_list = record_elem.getElementsByTagName("value");
				
				for (int k = 0; k < value_list.getLength(); ++k) {
					Element value_elem = (Element)value_list.item(k);
					
					IndexedData record = new IndexedData();
					
					String record_value = value_elem.getTextContent();
					int record_number = j + 1; // cannot have a 0 index for record_number
				
					record.setBatch_id(batch_ids.get(i));
					record.setField_id(field_ids.get(k));
					record.setRecord_number(record_number);
					record.setRecord_value(record_value);
					
					db.startTransaction();
					try {
						db.getIndexeddataDAO().add(record);
						db.endTransaction(true);
					}
					catch (DatabaseException e) {
						db.endTransaction(false);
						throw new DatabaseException();
					}
				}
			}
		}
		
	}
	
	
	public List<Integer> parseBatches(Element project_elem, int project_id, int first_y_coord, int record_height, int num_records, int num_fields) throws DatabaseException {
		List<Integer> primary_key_list = new ArrayList<Integer>();
		NodeList batch_list = project_elem.getElementsByTagName("image");
		for (int j = 0; j < batch_list.getLength(); ++j) {			
			Element batch_elem = (Element)batch_list.item(j);
			Element image_url_elem = (Element)batch_elem.getElementsByTagName("file").item(0);
			String image_url = image_url_elem.getTextContent();
			
			Batch batch = new Batch();
			batch.setProject_id(project_id);
			batch.setFirst_y_coord(first_y_coord);
			batch.setRecord_height(record_height);
			batch.setNum_records(num_records);
			batch.setNum_fields(num_fields);
			batch.setImage_url("records/" + image_url);
			
			db.startTransaction();
			try {
				primary_key_list.add(db.getBatchDAO().add(batch));
				db.endTransaction(true);
			}
			catch (DatabaseException e) {
				db.endTransaction(false);
				throw new DatabaseException();
			}
		}
		return primary_key_list;
	}
	
	// add all fields within this project and give project_id tag
	public List<Integer> parseFields(Element project_elem, int project_id) throws DatabaseException {
		List<Integer> primary_key_list = new ArrayList<Integer>();
		NodeList field_list = project_elem.getElementsByTagName("field");
		
		for (int j = 0; j < field_list.getLength(); ++j) {
			Element field_elem = (Element)field_list.item(j);
			
			Element field_title_elem = (Element)field_elem.getElementsByTagName("title").item(0);
			Element x_coord_elem = (Element)field_elem.getElementsByTagName("xcoord").item(0);
			Element pixel_width_elem = (Element)field_elem.getElementsByTagName("width").item(0);
			Element help_url_elem = (Element)field_elem.getElementsByTagName("helphtml").item(0);
			Element known_values_url_elem = (Element)field_elem.getElementsByTagName("knowndata").item(0);
			
			String field_title = field_title_elem.getTextContent();
			int x_coord = Integer.parseInt(x_coord_elem.getTextContent());
			int pixel_width = Integer.parseInt(pixel_width_elem.getTextContent());
			String help_url = help_url_elem.getTextContent();
			String known_values_url = null;
			if (known_values_url_elem != null)
				known_values_url = known_values_url_elem.getTextContent();
			
			Field field = new Field();
			field.setProject_id(project_id);
			field.setField_num(j + 1);
			field.setField_title(field_title);
			field.setX_coord(x_coord);
			field.setPixel_width(pixel_width);
			field.setHelp_url(help_url);
			field.setKnown_values_url(known_values_url);
			
			db.startTransaction();
			try {
				primary_key_list.add(db.getFieldDAO().add(field));
				db.endTransaction(true);
			}
			catch (DatabaseException e) {
				db.endTransaction(false);
				throw new DatabaseException();
			}
		}
		return primary_key_list;
	}
	
	public void parseUsers(File xml_file) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(xml_file);
		
		NodeList user_list = doc.getElementsByTagName("user");
		for (int i = 0; i < user_list.getLength(); ++i) {
			
			Element user_Elem = (Element)user_list.item(i);
			
			Element first_name_elem = (Element)user_Elem.getElementsByTagName("firstname").item(0);
			Element last_name_elem = (Element)user_Elem.getElementsByTagName("lastname").item(0);
			Element username_elem = (Element)user_Elem.getElementsByTagName("username").item(0);
			Element password_elem = (Element)user_Elem.getElementsByTagName("password").item(0);
			Element email_elem = (Element)user_Elem.getElementsByTagName("email").item(0);
			Element indexed_records_elem = (Element)user_Elem.getElementsByTagName("indexedrecords").item(0);
				
			String first_name = first_name_elem.getTextContent();
			String last_name = last_name_elem.getTextContent();
			String username = username_elem.getTextContent();
			String password = password_elem.getTextContent();
			int indexed_records = Integer.parseInt(indexed_records_elem.getTextContent());
			String email = email_elem.getTextContent();
			
			User user = new User();
			user.setUser_first_name(first_name);
			user.setUser_last_name(last_name);
			user.setUsername(username);
			user.setPassword(password);
			user.setNum_records(indexed_records);
			user.setEmail(email);
			
			db.startTransaction();
			try {
				db.getUserDAO().add(user);
				db.endTransaction(true);
			}
			catch (DatabaseException e) {
				db.endTransaction(false);
				throw new DatabaseException();
			}
			
		}	
		
	}
	

	
	
}
