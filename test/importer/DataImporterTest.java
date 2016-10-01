package importer;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.database.Database;
import shared.models.*;

public class DataImporterTest {
	private DataImporter data_importer;
	private Database db;
	@Before
	public void setUp() throws Exception {
		db  = new Database();
		Database.initialize();
		data_importer = new DataImporter();
		File src = new File("database" + File.separator + "empty_record_indexer.sqlite");
		File dst = new File("database"  + File.separator + "record_indexer.sqlite");
		FileUtils.copyFile(src, dst);	}

	@After
	public void tearDown() throws Exception {
		db = null;
		data_importer = null;
	}

	@Test
	public void testParseProjectData() throws Exception {
		data_importer.parseProjectData(new File("test/test.xml"));
		
		db.startTransaction();
		
		List<Project> projects = db.getProjectDAO().getAll();
		List<IndexedData> indexed_data = db.getIndexeddataDAO().getAll();
		List<Field> fields = db.getFieldDAO().getAll();
		List<Batch> batches = db.getBatchDAO().getAll();
		
		assertTrue(projects.size() == 3);
		assertTrue(indexed_data.size() == 560);
		assertTrue(fields.size() == 13);
		assertTrue(batches.size() == 60);
				
		boolean found_proj1 = false;
		boolean found_proj2 = false;
		boolean found_proj3 = false;

		for (Project proj : projects) {
			if (proj.getProject_title().equals("1890 Census")) {
				found_proj1 = true;
			}
			if (proj.getProject_title().equals("1900 Census")) {
				found_proj2 = true;
			}
			if (proj.getProject_title().equals("Draft Records")) {
				found_proj3 = true;
			}
		}
		assertTrue(found_proj1 && found_proj2 && found_proj3);
		
		IndexedData data = indexed_data.get(559);
		
		assertTrue(	data.getBatch_id() == 60 && 
					data.getRecord_number() == 7 && 
					data.getField_id() == 13 && 
					data.getRecord_value().equals("ASIAN"));
		
		Field field = fields.get(12);
		
		assertTrue( field.getProject_id() == 3 &&
					field.getField_title().equals("Ethnicity") &&
					field.getHelp_url().equals("fieldhelp/ethnicity.html") &&
					field.getX_coord() == 845 &&
					field.getPixel_width() == 450 &&
					field.getKnown_values_url().equals("knowndata/ethnicities.txt"));
		
		Batch batch = batches.get(59);
		
		assertTrue( batch.getProject_id() == 3 &&
					batch.getFirst_y_coord() == 195 &&
					batch.getRecord_height() == 65 && 
					batch.getNum_records() == 7 &&
					batch.getNum_fields() == 4);
		
		db.endTransaction(false);
	}

	@Test
	public void testParseUsers() throws Exception {
		data_importer.parseUsers(new File("test/test.xml"));
		
		db.startTransaction();
		List<User> users = db.getUserDAO().getAll();
		assertTrue(users.size() == 3);
		
		boolean found_sheila = false;
		boolean found_test1 = false;
		boolean found_test2 = false;
		
		for (User user : users) {
			if (!found_sheila) {
				if (user.getUser_first_name().equals("Sheila") && 
					user.getUser_last_name().equals("Parker") &&
					user.getUsername().equals("sheila") &&
					user.getPassword().equals("parker") &&
					user.getEmail().equals("sheila.parker@gmail.com")) {
					found_sheila = true;
				}	
			}
			
			if (!found_test1) {
				if (user.getUser_first_name().equals("Test") && 
					user.getUser_last_name().equals("One") &&
					user.getUsername().equals("test1") &&
					user.getPassword().equals("test1") &&
					user.getEmail().equals("test1@gmail.com")) {
					found_test1 = true;
				}	
			}
			
			if (!found_test2) {
				if (user.getUser_first_name().equals("Test") && 
					user.getUser_last_name().equals("Two") &&
					user.getUsername().equals("test2") &&
					user.getPassword().equals("test2") &&
					user.getEmail().equals("test2@gmail.com")) {
					found_test2 = true;
				}	
			}
			
		}
		
		assertTrue(found_sheila && found_test1 && found_test2);
		
		db.endTransaction(false);
	}

}
