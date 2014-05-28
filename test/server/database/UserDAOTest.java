package server.database;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shared.models.User;

public class UserDAOTest {
	private Database db;
	private UserDAO userDAO;
	private List<User> users;
	
	@Before
	public void setUp() throws Exception {
		Database.initialize();
		db  = new Database();
		userDAO = new UserDAO(db);
		db.startTransaction();
		users = initUsers();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(false);
		userDAO = null;
	}

	@Test
	public void testGetAll() throws DatabaseException {
		userDAO.add(users.get(0));
		userDAO.add(users.get(1));
		assertEquals(2, userDAO.getAll().size());
	}

	@Test
	public void testAdd() throws DatabaseException {
		User user1 = users.get(0);
		User user2 = users.get(1);

		userDAO.add(user1);
		userDAO.add(user2);
		
		List<User> user_list = userDAO.getAll();
		assertEquals(user_list.size(), 2);
		
		boolean found_user1 = false;
		boolean found_user2 = false;
		
		for(User user : user_list) {
			if (found_user1 == false) {
				found_user1 = areEqual(user, user1);
			}
			if (found_user2 == false) {
				found_user2 = areEqual(user, user2);
			}
		}
		assertTrue(found_user1 && found_user2);
	}

	@Test
	public void testUpdate() throws DatabaseException {
		User user1 = users.get(0);
		User user2 = users.get(1);

		userDAO.add(user1);
		userDAO.add(user2);
		
		List<User> user_list = userDAO.getAll();
		
		user1.setUser_id(user_list.get(0).getUser_id());
		user2.setUser_id(user_list.get(1).getUser_id());
		
		user1.setUser_first_name("Rich");
		user1.setUser_last_name("Frankenfurt");
		user1.setUsername("richfrank");
		user1.setPassword("hotdog");
		user1.setNum_records(325);
		user1.setEmail("this@this.com");
		
		user2.setUser_first_name("Shaniqua");
		user2.setUser_last_name("Lataetae");
		user2.setUsername("bonquiqui");
		user2.setPassword("password");
		user2.setNum_records(211);
		user1.setEmail("this2@this.com");
		userDAO.update(user1);
		userDAO.update(user2);
		
		user_list = userDAO.getAll();
		
		boolean updated_user1 = false;
		boolean updated_user2 = false;
		
		for(User user : user_list) {
			if (!updated_user1) {
				updated_user1 = areEqual(user, user1);
			}		
			if (!updated_user2) {
				updated_user2 = areEqual(user, user2);
			}
		}
		
		assertTrue(updated_user1 && updated_user2);
	}

	@Test
	public void testDelete() throws DatabaseException {
		User user1 = users.get(0);
		User user2 = users.get(1);

		userDAO.add(user1);
		userDAO.add(user2);
		
		List<User> user_list = userDAO.getAll();
		user1.setUser_id(user_list.get(0).getUser_id());
		user2.setUser_id(user_list.get(1).getUser_id());
		
		assertEquals(user_list.size(), 2);
		
		userDAO.delete(user1);
		userDAO.delete(user2);
		
		user_list = userDAO.getAll();
		assertTrue(user_list.size() == 0);
	}
	
	private boolean areEqual(User user1, User user2) {
		return 	user1.getUser_first_name().equals(user2.getUser_first_name()) &&
				user1.getUser_last_name().equals(user2.getUser_last_name()) &&
				user1.getUsername().equals(user2.getUsername()) &&
				user1.getPassword().equals(user2.getPassword()) &&
				user1.getNum_records() == user2.getNum_records() &&
				user1.getEmail().equals(user2.getEmail());
	}
	
	private List<User> initUsers() {
		List<User> users = new ArrayList<User>();
		
		User user1 = new User();
		user1.setUser_first_name("Dave");
		user1.setUser_last_name("Lindell");
		user1.setUsername("dlindell");
		user1.setPassword("ilikegrapes");
		user1.setNum_records(5);
		user1.setEmail("gmail@gmail.com");
		
		User user2 = new User();
		user2.setUser_first_name("Amie");
		user2.setUser_last_name("Saratoga");
		user2.setUsername("asaratoga");
		user2.setPassword("h1pst3r4lyfe");
		user2.setNum_records(21);
		user2.setEmail("hotmail@hotmail.com");
		
		users.add(user1);
		users.add(user2);

		return users;
	}
}
