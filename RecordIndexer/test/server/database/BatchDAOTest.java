package server.database;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shared.models.Batch;

public class BatchDAOTest {
	private BatchDAO batchDAO;
	private Database db;
	private List<Batch> batches;
	
	public BatchDAOTest() throws DatabaseException {
		Database.initialize();
		db  = new Database();
		batchDAO = new BatchDAO(db);
		db.startTransaction();
		batches = initBatches();
	}
	
	@Before
	public void setUp() throws Exception {
		File src = new File("database" + File.separator + "empty_record_indexer.sqlite");
		File dst = new File("database"  + File.separator + "record_indexer.sqlite");
		FileUtils.copyFile(src, dst);		Database.initialize();
		
		db  = new Database();
		batchDAO = new BatchDAO(db);
		db.startTransaction();
		batches = initBatches();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(true);
		batchDAO = null;
		File src = new File("database" + File.separator + "empty_record_indexer.sqlite");
		File dst = new File("database"  + File.separator + "record_indexer.sqlite");
		FileUtils.copyFile(src, dst);	}

	@Test
	public void testGetBatch() throws DatabaseException {
		batchDAO.add(batches.get(0));
		batchDAO.add(batches.get(1));
		List<Batch> batch_list = batchDAO.getAll();
		int batch_id = batch_list.get(0).getBatch_id();
		
		assertTrue(areEqual(batchDAO.getBatch(batch_id), batches.get(0)));
	}
	
	@Test
	public void testGetAll() throws DatabaseException {
		batchDAO.add(batches.get(0));
		batchDAO.add(batches.get(1));
		assertEquals(2, batchDAO.getAll().size());
	}

	@Test
	public void testAdd() throws DatabaseException {
		Batch batch1 = batches.get(0);
		Batch batch2 = batches.get(1);
		
		batchDAO.add(batch1);
		batchDAO.add(batch2);
		
		List<Batch> batch_list = batchDAO.getAll();
		assertEquals(batch_list.size(), 2);
		
		boolean found_batch1 = false;
		boolean found_batch2 = false;
		
		for(Batch batch : batch_list) {
			if (found_batch1 == false) {
				found_batch1 = areEqual(batch, batch1);
			}
			if (found_batch2 == false) {
				found_batch2 = areEqual(batch, batch2);
			}
		}
		assertTrue(found_batch1 && found_batch2);
	}
	

	@Test
	public void testUpdate() throws DatabaseException {
		Batch batch1 = batches.get(0);
		Batch batch2 = batches.get(1);
		
		batchDAO.add(batch1);
		batchDAO.add(batch2);
		
		List<Batch> batch_list = batchDAO.getAll();
		batch1.setBatch_id(batch_list.get(0).getBatch_id());
		batch2.setBatch_id(batch_list.get(1).getBatch_id());
		
		batch1.setFirst_y_coord(5);
		batch1.setImage_url("example/changed.png");
		batch1.setNum_fields(7);
		batch1.setNum_records(22);
		batch1.setProject_id(5);
		batch1.setRecord_height(24);
		
		batch2.setFirst_y_coord(33);
		batch2.setImage_url("example/changed2.img");
		batch2.setNum_fields(11);
		batch2.setNum_records(23);
		batch2.setProject_id(42);
		batch2.setRecord_height(64);
		
		batchDAO.update(batch1);
		batchDAO.update(batch2);
		
		batch_list = batchDAO.getAll();
		
		boolean updated_batch1 = false;
		boolean updated_batch2 = false;
		
		for(Batch batch : batch_list) {
			if (!updated_batch1) {
				updated_batch1 = areEqual(batch, batch1);
			}		
			if (!updated_batch2) {
				updated_batch2 = areEqual(batch, batch2);
			}
		}
		
		assertTrue(updated_batch1 && updated_batch2);
		
	}

	@Test
	public void testDelete() throws DatabaseException {
		Batch batch1 = batches.get(0);
		Batch batch2 = batches.get(1);
		
		batchDAO.add(batch1);
		batchDAO.add(batch2);
		
		List<Batch> batch_list = batchDAO.getAll();
		batch1.setBatch_id(batch_list.get(0).getBatch_id());
		batch2.setBatch_id(batch_list.get(1).getBatch_id());
		
		assertTrue(batch_list.size() == 2);
		
		batchDAO.delete(batch1);
		batchDAO.delete(batch2);
		
		batch_list = batchDAO.getAll();
		assertTrue(batch_list.size() == 0);
	}

	private boolean areEqual(Batch batch1, Batch batch2) {
		return 	batch1.getFirst_y_coord() == batch2.getFirst_y_coord() &&
				batch1.getImage_url().equals(batch2.getImage_url()) &&
				batch1.getNum_fields() == batch2.getNum_fields() &&
				batch1.getNum_records() == batch2.getNum_records() && 
				batch1.getProject_id() == batch2.getProject_id() &&
				batch1.getRecord_height() == batch2.getRecord_height();
	}
	
	public List<Batch> initBatches() {
		List<Batch> batches = new ArrayList<Batch>();
		
		Batch batch1 = new Batch();
		batch1.setFirst_y_coord(4);
		batch1.setImage_url("example/img.png");
		batch1.setNum_fields(6);
		batch1.setNum_records(18);
		batch1.setProject_id(1);
		batch1.setRecord_height(14);
		
		Batch batch2 = new Batch();
		batch2.setFirst_y_coord(4);
		batch2.setImage_url("example/img.png");
		batch2.setNum_fields(6);
		batch2.setNum_records(18);
		batch2.setProject_id(2);
		batch2.setRecord_height(14);
		
		batches.add(batch1);
		batches.add(batch2);
		return batches;
	}
	
}
