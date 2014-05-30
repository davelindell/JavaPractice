package server;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.net.httpserver.HttpExchange;
import com.thoughtworks.xstream.XStream;

import server.database.BatchDAOTest;
import server.database.Database;
import server.database.FieldDAOTest;
import server.database.IndexedDataDAOTest;
import server.database.ProjectDAOTest;
import server.database.UserDAOTest;
import server.facade.ServerFacade;
import shared.communication.ValidateUser_Params;
import shared.models.Batch;
import shared.models.Field;
import shared.models.IndexedData;
import shared.models.Project;
import shared.models.User;

public class ValidateUserHandlerTest {
	private ValidateUserHandler validate_handler;
	private UserDAOTest user_test;
	private List<User> users;

	
	@Before
	public void setUp() throws Exception {
		validate_handler = new ValidateUserHandler();
		
		File src = new File("database" + File.separator + "empty_record_indexer.sqlite");
		File dst = new File("database"  + File.separator + "record_indexer.sqlite");
		FileUtils.copyFile(src, dst);		Database.initialize();
		
		user_test = new UserDAOTest();
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
		validate_handler = null;

	}

	@Test
	public void testHandle() {
		XStream xml_stream = new XStream();
		ValidateUser_Params params = new ValidateUser_Params(users.get(0).getUsername(), users.get(0).getPassword());
	//	HttpExchange exchange = new HttpExchange();
	//	validate_handler.handle(exchange);
	}

}
