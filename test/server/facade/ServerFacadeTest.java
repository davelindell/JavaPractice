package server.facade;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.database.*;
import shared.communication.*;
import shared.models.*;

public class ServerFacadeTest {
	private ServerFacade sf;
	private BatchDAOTest batch_test;
	private ProjectDAOTest project_test;
	private UserDAOTest user_test;
	
	private List<User> users;
	private List<Project> projects;
	private List<Field> fields;
	private List<Batch> batches;

	public ServerFacadeTest() {
		
	}
	
	@Before
	public void setUp() throws Exception {
		File src = new File("database" + File.separator + "empty_record_indexer.sqlite");
		File dst = new File("database"  + File.separator + "record_indexer.sqlite");
		FileUtils.copyFile(src, dst);		Database.initialize();
	
		sf = new ServerFacade();
		
		batch_test = new BatchDAOTest();
		project_test = new ProjectDAOTest();
		user_test = new UserDAOTest();
		
		batches = batch_test.initBatches();
		fields = initFields();
		projects = project_test.initProjects();
		users = user_test.initUsers();
		
		Database db = new Database();
		db.startTransaction();
		db.getUserDAO().add(users.get(0));
		db.getUserDAO().add(users.get(1));
		List<User> user_list = db.getUserDAO().getAll();
		users.get(0).setUser_id(user_list.get(0).getUser_id());
		users.get(1).setUser_id(user_list.get(1).getUser_id());
		db.endTransaction(true);
	}

	@After
	public void tearDown() throws Exception {
		batch_test = null;
		project_test = null;
		user_test = null;
	}

	@Test
	public void testValidateUser() throws DatabaseException {
		Database db = new Database();

		ValidateUser_Params params = new ValidateUser_Params(users.get(0).getUsername(), users.get(0).getPassword());
		try {
			ValidateUser_Result result = sf.validateUser(params);
			assertTrue(result.getUser() != null);
		} catch (DatabaseException e) {
			assertTrue(false);
		}
		
		db.startTransaction();
		db.getUserDAO().delete(users.get(0));
		db.endTransaction(true);
		try {
			ValidateUser_Result result = sf.validateUser(params);
			assertTrue(result.getUser() == null);
		} catch (DatabaseException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testGetProjects() throws DatabaseException {
		Database db = new Database();

		db.startTransaction();
		db.getProjectDAO().add(projects.get(0));
		db.getProjectDAO().add(projects.get(0));
		db.endTransaction(true);
		
		GetProjects_Params params = new GetProjects_Params(users.get(0).getUsername(), users.get(0).getPassword());
		try {
			GetProjects_Result result = sf.getProjects(params);
			assertTrue(result.getProject_info().size() == 2);
		} catch (DatabaseException e) {
			assertTrue(false);
		}
	}
/*
	@Test
	public void testGetSampleImage() {
		fail("Not yet implemented");
	}
*/
	@Test
	public void testDownloadBatch() throws DatabaseException {
		Database db = new Database();
		DownloadBatch_Params params = new DownloadBatch_Params(users.get(0).getUsername(), users.get(0).getPassword(), 1);
		db.startTransaction();
		db.getProjectDAO().add(projects.get(0));
		db.getBatchDAO().add(batches.get(0));
		db.endTransaction(true);
		
		try {
			DownloadBatch_Result result = sf.downloadBatch(params);
			assertTrue( result.getBatch_id() == 1 && 
						result.getNum_fields() == 6 &&
						result.getProject_id() == 1);
		} catch (DatabaseException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testSubmitBatch() throws DatabaseException {
		Database db = new Database();
		String record_str = "a,b,c,d;e,f,g,h;";
		List<List<IndexedData>> records = makeRecords(record_str, 1);
		SubmitBatch_Params params = new SubmitBatch_Params(users.get(0).getUsername(), users.get(0).getPassword(), 1, records);
		db.startTransaction();
		db.getProjectDAO().add(projects.get(0));
		db.getBatchDAO().add(batches.get(0));
		db.getFieldDAO().add(fields.get(0));
		db.getFieldDAO().add(fields.get(1));
		db.getFieldDAO().add(fields.get(2));
		db.getFieldDAO().add(fields.get(3));
		db.endTransaction(true);
		
		try {
			testDownloadBatch();
			SubmitBatch_Result result = sf.submitBatch(params);
			assertTrue(result.isValid());
			db.startTransaction();
			assertTrue(db.getBatchDAO().getBatch(1).getCur_username().equals(""));
			assertTrue(db.getIndexeddataDAO().getAll().size() == 8);
			db.endTransaction(true);
			
		} catch (DatabaseException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testGetFields() throws DatabaseException {
		Database db = new Database();

		GetFields_Params params = new GetFields_Params(users.get(0).getUsername(), users.get(0).getPassword(), 1);
		db.startTransaction();
		db.getFieldDAO().add(fields.get(0));
		db.getFieldDAO().add(fields.get(1));
		db.getFieldDAO().add(fields.get(2));
		db.getFieldDAO().add(fields.get(3));
		db.endTransaction(true);
		
		try {
			GetFields_Result result = sf.getFields(params);
			assertTrue( result.getFields().size() == 4 &&
						result.getFields().get(0).getField_title().equals("field1"));
		} catch (DatabaseException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testSearch() throws DatabaseException {
		Database db = new Database();
		String record_str = "a,b,c,d;e,f,g,h;";
		List<List<IndexedData>> records = makeRecords(record_str, 1);
		SubmitBatch_Params params = new SubmitBatch_Params(users.get(0).getUsername(), users.get(0).getPassword(), 1, records);
		db.startTransaction();
		db.getProjectDAO().add(projects.get(0));
		db.getBatchDAO().add(batches.get(0));
		db.getFieldDAO().add(fields.get(0));
		db.getFieldDAO().add(fields.get(1));
		db.getFieldDAO().add(fields.get(2));
		db.getFieldDAO().add(fields.get(3));
		db.endTransaction(true);
		
		Search_Params search_params = new Search_Params(users.get(0).getUsername(), users.get(0).getPassword(), "1,2,3,4","a,b,c,d");
		
		try {
			testDownloadBatch();
			sf.submitBatch(params);
			
			Search_Result search_result = sf.search(search_params);
			assertTrue(search_result.getMatches().size() == 4);
			assertTrue( search_result.getMatches().get(0).getBatch_id() == 1 &&
					search_result.getMatches().get(0).getField_id() == 1 &&
					search_result.getMatches().get(0).getRecord_number() == 1 &&
					search_result.getMatches().get(0).getImage_url().equals("example/img.png"));
		} catch (DatabaseException e) {
			assertTrue(false);
		}
	}
/*
	@Test
	public void testDownloadFile() {
		fail("Not yet implemented");
	}
*/	
	public List<List<IndexedData>> makeRecords(String record_str, int batch_id) {
		String record_strings[] = record_str.split(";");
		List<List<IndexedData>> records = new ArrayList<List<IndexedData>>();
		
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
		return records;
	}

	private List<Field> initFields() {
		List<Field> fields = new ArrayList<Field>();
		
		Field field1 = new Field();
		field1.setProject_id(1);
		field1.setField_num(1);
		field1.setField_title("field1");
		field1.setHelp_url("help/text.txt");
		field1.setX_coord(5);
		field1.setPixel_width(10);
		field1.setKnown_values_url("folder/knownvalues.txt");
		
		Field field2 = new Field();		
		field2.setProject_id(1);
		field2.setField_num(2);
		field2.setField_title("field2");
		field2.setHelp_url("help/text2.txt");
		field2.setX_coord(25);
		field2.setPixel_width(22);
		field2.setKnown_values_url("folder/knownvalues2.txt");
		
		Field field3 = new Field();		
		field3.setProject_id(1);
		field3.setField_num(3);
		field3.setField_title("field3");
		field3.setHelp_url("help/text3.txt");
		field3.setX_coord(25);
		field3.setPixel_width(22);
		field3.setKnown_values_url("folder/knownvalues3.txt");
		
		Field field4 = new Field();		
		field4.setProject_id(1);
		field4.setField_num(4);
		field4.setField_title("field4");
		field4.setHelp_url("help/text4.txt");
		field4.setX_coord(25);
		field4.setPixel_width(22);
		field4.setKnown_values_url("folder/knownvalues4.txt");
		
		fields.add(field1);
		fields.add(field2);
		fields.add(field3);
		fields.add(field4);
		return fields;
	}
}
