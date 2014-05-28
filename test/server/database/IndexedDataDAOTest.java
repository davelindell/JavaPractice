package server.database;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shared.models.IndexedData;

public class IndexedDataDAOTest {
	private Database db;
	private IndexedDataDAO dataDAO;
	private List<IndexedData> data;
	
	@Before
	public void setUp() throws Exception {
		Database.initialize();
		db  = new Database();
		dataDAO = new IndexedDataDAO(db);
		db.startTransaction();
		data = initData();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(false);
		dataDAO = null;
	}

	@Test
	public void testGetAll() throws DatabaseException {
		dataDAO.add(data.get(0));
		dataDAO.add(data.get(1));
		assertEquals(2, dataDAO.getAll().size());
	}

	@Test
	public void testAdd() throws DatabaseException {
		IndexedData data1 = data.get(0);
		IndexedData data2 = data.get(1);
		
		dataDAO.add(data1);
		dataDAO.add(data2);
		
		List<IndexedData> data_list = dataDAO.getAll();
		assertEquals(data_list.size(), 2);
		
		boolean found_data1 = false;
		boolean found_data2 = false;
		
		for(IndexedData datum : data_list) {
			if (found_data1 == false) {
				found_data1 = areEqual(datum, data1);
			}
			if (found_data2 == false) {
				found_data2 = areEqual(datum, data2);
			}
		}
		assertTrue(found_data1 && found_data2);
	}

	@Test
	public void testUpdate() throws DatabaseException {
		IndexedData data1 = data.get(0);
		IndexedData data2 = data.get(1);
		
		dataDAO.add(data1);
		dataDAO.add(data2);
		
		List<IndexedData> data_list = dataDAO.getAll();
		data1.setData_id(data_list.get(0).getData_id());
		data2.setData_id(data_list.get(1).getData_id());
		
		data1.setBatch_id(12);
		data1.setField_id(22);
		data1.setRecord_number(332);
		data1.setRecord_value("Becky");
		
		data1.setBatch_id(12);
		data1.setField_id(32);
		data1.setRecord_number(22);
		data1.setRecord_value("Weasley");
		
		dataDAO.update(data1);
		dataDAO.update(data2);
		
		data_list = dataDAO.getAll();
		
		boolean updated_data1 = false;
		boolean updated_data2 = false;
		
		for(IndexedData datum : data_list) {
			if (!updated_data1) {
				updated_data1 = areEqual(datum, data1);
			}		
			if (!updated_data2) {
				updated_data2 = areEqual(datum, data2);
			}
		}
		
		assertTrue(updated_data1 && updated_data2);
	}

	@Test
	public void testDelete() throws DatabaseException {
		IndexedData data1 = data.get(0);
		IndexedData data2 = data.get(1);
		
		dataDAO.add(data1);
		dataDAO.add(data2);
		
		List<IndexedData> data_list = dataDAO.getAll();
		data1.setData_id(data_list.get(0).getData_id());
		data2.setData_id(data_list.get(1).getData_id());
		
		assertEquals(data_list.size(), 2);
		
		dataDAO.delete(data1);
		dataDAO.delete(data2);
		
		data_list = dataDAO.getAll();
		assertTrue(data_list.size() == 0);
	}
	
	private boolean areEqual(IndexedData data1, IndexedData data2) {
		return 	data1.getBatch_id() == data2.getBatch_id()&&
				data1.getRecord_number() == data2.getRecord_number() &&
				data1.getField_id() == data2.getField_id() &&
				data1.getRecord_value().equals(data2.getRecord_value());
	}
	
	private List<IndexedData> initData() {
		List<IndexedData> data = new ArrayList<IndexedData>();
		
		IndexedData data1 = new IndexedData();
		data1.setBatch_id(1);
		data1.setField_id(2);
		data1.setRecord_number(32);
		data1.setRecord_value("Abigail");
		
		IndexedData data2 = new IndexedData();
		data2.setBatch_id(3);
		data2.setField_id(4);
		data2.setRecord_number(45);
		data2.setRecord_value("Ronald");
		
		data.add(data1);
		data.add(data2);

		return data;
	}
}
