package server.database;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.database.Database;



public class DatabaseTest {
	private Database db;
	
	@Before
	public void setUp() {
		db = new Database();
	}
	
	
	@After
	public void tearDown() {
		db = null;
	}
	
	@Test
	public void testDatabase() {
		assertTrue(db != null);
	}

	@Test
	public void testInitialize() {
		try {
			Database.initialize();
		} catch (DatabaseException e) {
			assert(false);
		}	
	}

	@Test
	public void testTransaction() {
		try {
			db.startTransaction();
		} catch (DatabaseException e) {
			assert(false);
		}
		
		try {
			db.endTransaction(true);
		} catch (DatabaseException e) {
			assert(false);
		}
		
		try {
			db.startTransaction();
		} catch (DatabaseException e) {
			assert(false);
		}
		try {
			db.endTransaction(false);
		} catch (DatabaseException e) {
			assert(false);
		}
	}

}
