package importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import server.database.*;
import shared.models.User;

public class DataImporter {
	
	private Database db;
	private BatchDAO batchDAO;
	private FieldDAO fieldDAO;
	private ProjectDAO projectDAO;
	private UserDAO userDAO;
	
	public DataImporter() throws DatabaseException {
		db = new Database();
		Database.initialize();
		batchDAO = new BatchDAO(db);
		fieldDAO = new FieldDAO(db);
		projectDAO = new ProjectDAO(db);
		userDAO = new UserDAO(db);
	}
	
	public static void main(String[] args) throws Exception {		
		File src = new File("database/empty_record_indexer.sqlite");
		File dst = new File("database/record_indexer.sqlite");
		Files.copy(	src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		DataImporter importer = new DataImporter();
		File xml_file = new File(args[0]);
		importer.parseUsers(xml_file);

		
		return;
	}
	
	public void parseUsers(File xml_file) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(xml_file);
		
		NodeList user_list = doc.getElementsByTagName("user");
		for (int i = 0; i < user_list.getLength(); ++i) {
			
			Element user_Elem = (Element)user_list.item(i);
			
			Element first_name_Elem = (Element)user_Elem.getElementsByTagName("firstname").item(0);
			Element last_name_Elem = (Element)user_Elem.getElementsByTagName("lastname").item(0);
			Element username_Elem = (Element)user_Elem.getElementsByTagName("username").item(0);
			Element password_Elem = (Element)user_Elem.getElementsByTagName("password").item(0);
			Element email_Elem = (Element)user_Elem.getElementsByTagName("email").item(0);
			Element indexed_records_Elem = (Element)user_Elem.getElementsByTagName("indexedrecords").item(0);
				
			String first_name = first_name_Elem.getTextContent();
			String last_name = email_Elem.getTextContent();
			String username = username_Elem.getTextContent();
			String password = password_Elem.getTextContent();
			int indexed_records = Integer.parseInt(indexed_records_Elem.getTextContent());
			String email = last_name_Elem.getTextContent();
			
			User user = new User();
			user.setUser_first_name(first_name);
			user.setUser_last_name(last_name);
			user.setUsername(username);
			user.setPassword(password);
			user.setNum_records(indexed_records);
			user.setEmail(email);
			
			db.startTransaction();
			try {
				userDAO.add(user);
				db.endTransaction(true);
			}
			catch (DatabaseException e) {
				db.endTransaction(false);
				throw new DatabaseException();
			}
			
		}	
		
	}
	

	
	
}
