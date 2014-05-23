package server.database;
import static org.junit.Assert.*;


import org.junit.Test;

import server.database.Database;


public class DatabaseTest {
	private Database db;
	@Test
	public void testDatabase() {
		db = new Database();
		assertTrue(db != null);
	}

	@Test
	public void testInitialize() {
		fail("Not yet implemented");
	}

	@Test
	public void testStartTransaction() {
		fail("Not yet implemented");
	}

	@Test
	public void testEndTransaction() {
		fail("Not yet implemented");
	}

}
