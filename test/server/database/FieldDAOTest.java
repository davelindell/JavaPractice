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
import shared.models.Field;

public class FieldDAOTest {
	private Database db;
	private FieldDAO fieldDAO;
	private List<Field> fields;
	
	@Before
	public void setUp() throws Exception {
		File src = new File("database" + File.separator + "empty_record_indexer.sqlite");
		File dst = new File("database"  + File.separator + "record_indexer.sqlite");
		FileUtils.copyFile(src, dst);		Database.initialize();
		db  = new Database();
		fieldDAO = new FieldDAO(db);
		db.startTransaction();
		fields = initFields();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(true);
		fieldDAO = null;
		File src = new File("database" + File.separator + "empty_record_indexer.sqlite");
		File dst = new File("database"  + File.separator + "record_indexer.sqlite");
		FileUtils.copyFile(src, dst);	}

	@Test
	public void testGetAll() throws DatabaseException {
		fieldDAO.add(fields.get(0));
		fieldDAO.add(fields.get(1));
		assertEquals(2, fieldDAO.getAll().size());
	}

	@Test
	public void testAdd() throws DatabaseException {	
		Field field1 = fields.get(0);
		Field field2 = fields.get(1);
		
		fieldDAO.add(field1);
		fieldDAO.add(field2);
		
		List<Field> field_list = fieldDAO.getAll();
		assertEquals(field_list.size(), 2);
		
		boolean found_field1 = false;
		boolean found_field2 = false;
		
		for(Field field : field_list) {
			if (found_field1 == false) {
				found_field1 = areEqual(field, field1);
			}
			if (found_field2 == false) {
				found_field2 = areEqual(field, field2);
			}
		}
		assertTrue(found_field1 && found_field2);
	}

	@Test
	public void testUpdate() throws DatabaseException {
		Field field1 = fields.get(0);
		Field field2 = fields.get(1);
		
		fieldDAO.add(field1);
		fieldDAO.add(field2);
		
		List<Field> field_list = fieldDAO.getAll();
		field1.setField_id(field_list.get(0).getField_id());
		field2.setField_id(field_list.get(1).getField_id());
		
		field1.setProject_id(3);
		field1.setField_title("field1new");
		field1.setHelp_url("help/textnew.txt");
		field1.setX_coord(51);
		field1.setPixel_width(102);
		field1.setKnown_values_url("folder/knownvaluesnew.txt");
		
		field2.setProject_id(4);
		field2.setField_title("field2new");
		field2.setHelp_url("help/text2new.txt");
		field2.setX_coord(253);
		field2.setPixel_width(221);
		field2.setKnown_values_url("folder/knownvalues2new.txt");
		
		fieldDAO.update(field1);
		fieldDAO.update(field2);
		
		field_list = fieldDAO.getAll();
		
		boolean updated_field1 = false;
		boolean updated_field2 = false;
		
		for(Field field : field_list) {
			if (!updated_field1) {
				updated_field1 = areEqual(field, field1);
			}		
			if (!updated_field2) {
				updated_field2 = areEqual(field, field2);
			}
		}
		
		assert(updated_field1 && updated_field2);
	}

	@Test
	public void testDelete() throws DatabaseException {
		Field field1 = fields.get(0);
		Field field2 = fields.get(1);
		
		fieldDAO.add(field1);
		fieldDAO.add(field2);
		
		List<Field> field_list = fieldDAO.getAll();
		field1.setField_id(field_list.get(0).getField_id());
		field2.setField_id(field_list.get(1).getField_id());
		
		assert(field_list.size() == 2);
		
		fieldDAO.delete(field1);
		fieldDAO.delete(field2);
		
		field_list = fieldDAO.getAll();
		assert(field_list.size() == 0);
	}
	
	@Test 
	public void testGetBatchFields() throws DatabaseException {
		Field field1 = fields.get(0);
		Field field2 = fields.get(1);
		
		fieldDAO.add(field1);
		fieldDAO.add(field2);
		
		List<Field> field_list = fieldDAO.getAll();
		field1.setField_id(field_list.get(0).getField_id());
		field2.setField_id(field_list.get(1).getField_id());
		
		assert(field_list.size() == 2);
		
		Batch batch = new Batch();
		batch.setProject_id(1);
		
		List<Field> batch_fields = fieldDAO.getBatchFields(batch.getProject_id());
		
		assertTrue(batch_fields.size() == 1);
	}
	
	private boolean areEqual(Field field1, Field field2) {
		return 	field1.getProject_id() == field2.getProject_id() &&
				field1.getField_title().equals(field2.getField_title()) &&
				field1.getHelp_url().equals(field2.getHelp_url()) &&
				field1.getX_coord() == field2.getX_coord() &&
				field1.getPixel_width() == field2.getPixel_width() &&
				field1.getField_num() == field2.getField_num() &&
				field1.getKnown_values_url().equals(field2.getKnown_values_url());
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
		field2.setProject_id(2);
		field2.setField_num(2);
		field2.setField_title("field2");
		field2.setHelp_url("help/text2.txt");
		field2.setX_coord(25);
		field2.setPixel_width(22);
		field2.setKnown_values_url("folder/knownvalues2.txt");
		
		fields.add(field1);
		fields.add(field2);
		return fields;
	}
	

	
}
