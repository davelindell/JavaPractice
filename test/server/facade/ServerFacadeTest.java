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
	private FieldDAOTest field_test;
	private IndexedDataDAOTest data_test;
	private ProjectDAOTest project_test;
	private UserDAOTest user_test;
	
	private List<User> users;
	private List<Project> projects;
	private List<IndexedData> data;
	private List<Field> fields;
	private List<Batch> batches;

	@Before
	public void setUp() throws Exception {
		File src = new File("database" + File.separator + "empty_record_indexer.sqlite");
		File dst = new File("database"  + File.separator + "record_indexer.sqlite");
		FileUtils.copyFile(src, dst);		Database.initialize();
	
		sf = new ServerFacade();
		
		batch_test = new BatchDAOTest();
		field_test = new FieldDAOTest();
		data_test = new IndexedDataDAOTest();
		project_test = new ProjectDAOTest();
		user_test = new UserDAOTest();
		
		batches = batch_test.initBatches();
		fields = field_test.initFields();
		data = data_test.initData();
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
		field_test = null;
		data_test = null;
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

	@Test
	public void testGetSampleImage() {
		fail("Not yet implemented");
	}

	@Test
	public void testDownloadBatch() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubmitBatch() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFields() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearch() {
		fail("Not yet implemented");
	}

	@Test
	public void testDownloadFile() {
		fail("Not yet implemented");
	}
	

}
