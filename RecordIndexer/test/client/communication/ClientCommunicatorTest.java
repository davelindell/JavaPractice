package client.communication;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import importer.DataImporter;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.database.Database;
import server.facade.ServerFacadeTest;
import shared.communication.*;
import shared.models.Batch;
import shared.models.IndexedData;


public class ClientCommunicatorTest {
	private ClientCommunicator cc;
	private String url;
	private String port;
	private Database db;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		String[] args = {"test_records/Records.xml"};
		DataImporter.main(args);
	}
	
	@Before
	public void setUp() throws Exception {
		Database.initialize();
		db = new Database();
		url = "localhost";
		port = "39640";
		cc = new ClientCommunicator(url, port);
		
	}
	
	@After
	public void tearDown() throws Exception {
		db = null;
		url = null;
		port = null;
		cc = null;
	}

	@Test
	public void testValidateUser() throws Exception {
		ValidateUser_Params params = new ValidateUser_Params("sheila", "parker");
		ValidateUser_Result result = cc.validateUser(params);
		result.getUser().setNum_records(0);
		db.startTransaction();
		db.getUserDAO().update(result.getUser());
		db.endTransaction(true);
		assertTrue(result.toString().equals("TRUE\nSheila\nParker\n0\n"));
		
		params = new ValidateUser_Params("", "");
		result = cc.validateUser(params);
		assertTrue(result.toString().equals("FALSE\n"));
		
		params = new ValidateUser_Params("SHEILA", "parker");
		result = cc.validateUser(params);
		assertTrue(result.toString().equals("FALSE\n"));
		
		params = new ValidateUser_Params("", "parker");
		result = cc.validateUser(params);
		assertTrue(result.toString().equals("FALSE\n"));
		
		params = new ValidateUser_Params("SHEILA", "");
		result = cc.validateUser(params);
		assertTrue(result.toString().equals("FALSE\n"));
		
		params = new ValidateUser_Params("b$en", "franklin2");
		result = cc.validateUser(params);
		assertTrue(result.toString().equals("FALSE\n"));
	}

	@Test
	public void testGetProjects() throws Exception {
		GetProjects_Params params = new GetProjects_Params("sheila", "parker");
		GetProjects_Result result = cc.getProjects(params);
		assertTrue(result.toString().equals("1\n1890 Census\n2\n1900 Census\n3\nDraft Records\n"));

		params = new GetProjects_Params("sheil", "parker");
		result = cc.getProjects(params);
		assertTrue(result.toString().equals("FAILED\n"));
		
		params = new GetProjects_Params("sheil", "");
		result = cc.getProjects(params);
		assertTrue(result.toString().equals("FAILED\n"));
	}

	@Test
	public void testGetSampleImage() throws Exception {
		GetSampleImage_Params params = new GetSampleImage_Params("sheila", "parker", 1);
		GetSampleImage_Result result = cc.getSampleImage(params);
		assertTrue(result.toString().equals("http://localhost:39640/records/images/1890_image19.png\n"));	
		
		params = new GetSampleImage_Params("sheila", "parker", 2);
		result = cc.getSampleImage(params);
		assertTrue(result.toString().equals("http://localhost:39640/records/images/1900_image19.png\n"));
		
		params = new GetSampleImage_Params("sheila", "parker", 3);
		result = cc.getSampleImage(params);
		assertTrue(result.toString().equals("http://localhost:39640/records/images/draft_image19.png\n"));
		
		params = new GetSampleImage_Params("sheila", "parer", 3);
		result = cc.getSampleImage(params);
		assertTrue(result.toString().equals("FAILED\n"));
		
	}

	@Test
	public void testDownloadBatch() throws Exception {		
		DownloadBatch_Params params = new DownloadBatch_Params("sheila", "parker", 1);
		DownloadBatch_Result result = cc.downloadBatch(params);
		assertTrue(result.getFields().get(0).getField_title().equals("Last Name") &&
				   result.getFields().get(0).getPixel_width() == 300);

		ServerFacadeTest sft = new ServerFacadeTest();
		List<List<IndexedData>> data_list = sft.makeRecords("a,b,c,d;e,,f;",1);
		SubmitBatch_Params submit_params = new SubmitBatch_Params("sheila", "parker",1, data_list);
		cc.submitBatch(submit_params);

		
		params = new DownloadBatch_Params("sheila", "parker", 2);
		result = cc.downloadBatch(params);
		assertTrue(result.getFields().size() == 5 &&
				   result.getFields().get(0).getField_id() == 5 &&
				   result.getFields().get(0).getX_coord() == 45);
		
		data_list = sft.makeRecords("a,b,c,d;e,,f;",21);
		submit_params = new SubmitBatch_Params("sheila", "parker",21, data_list);
		cc.submitBatch(submit_params);
		
		params = new DownloadBatch_Params("sheila", "parker", 3);
		result = cc.downloadBatch(params);
		assertTrue(result.getFields().get(0).getField_id() == 10 &&
				   result.getFields().get(0).getField_title().equals("Last Name"));
		
		data_list = sft.makeRecords("a,b,c,d;e,,f;;",41);
		submit_params = new SubmitBatch_Params("sheila", "parker",41, data_list);
		cc.submitBatch(submit_params);
		
		params = new DownloadBatch_Params("sheil", "parker", 3);
		result = cc.downloadBatch(params);
		assertTrue(result.toString().equals("FAILED\n"));
		
		db.startTransaction();
		Batch batch1 = db.getBatchDAO().getBatch(1);
		Batch batch2 = db.getBatchDAO().getBatch(21);
		Batch batch3 = db.getBatchDAO().getBatch(41);
		
		batch1.setCur_username("");
		batch2.setCur_username("");
		batch3.setCur_username("");
		
		db.getBatchDAO().update(batch1);
		db.getBatchDAO().update(batch2);
		db.getBatchDAO().update(batch3);

		db.endTransaction(true);

	}

	@Test
	public void testSubmitBatch() throws Exception {
		DownloadBatch_Params params = new DownloadBatch_Params("sheila", "parker", 1);
		cc.downloadBatch(params);
		
		ServerFacadeTest sft = new ServerFacadeTest();
		List<List<IndexedData>> data_list = sft.makeRecords("a,b,c,d;e,,f;",1);
		SubmitBatch_Params submit_params = new SubmitBatch_Params("sheila", "parker",1, data_list);
		SubmitBatch_Result submit_result = cc.submitBatch(submit_params);
		assertTrue(submit_result.toString().equals("TRUE\n"));
		
		params = new DownloadBatch_Params("sheila", "parker", 2);
		cc.downloadBatch(params);
		
		data_list = sft.makeRecords("a,b,c,d;e,,f;",21);
		submit_params = new SubmitBatch_Params("sheila", "parker",21, data_list);
		submit_result = cc.submitBatch(submit_params);
		assertTrue(submit_result.toString().equals("TRUE\n"));
		
		params = new DownloadBatch_Params("sheil", "parker", 3);
		cc.downloadBatch(params);
		
		data_list = sft.makeRecords("a,b,c,d;e,,f;;",41);
		submit_params = new SubmitBatch_Params("sheila", "parker",41, data_list);
		submit_result = cc.submitBatch(submit_params);
		
		data_list = sft.makeRecords("a,b,c,d;e,,f;;",41);
		submit_params = new SubmitBatch_Params("sheil", "parker",41, data_list);
		submit_result = cc.submitBatch(submit_params);
		assertTrue(submit_result.toString().equals("FAILED\n"));
		
		
		data_list = sft.makeRecords("a,b,c,d;e,,f;;",1000);
		submit_params = new SubmitBatch_Params("sheila", "parker",1000, data_list);
		submit_result = cc.submitBatch(submit_params);
		assertTrue(submit_result.toString().equals("FAILED\n"));
		
		db.startTransaction();
		Batch batch1 = db.getBatchDAO().getBatch(1);
		Batch batch2 = db.getBatchDAO().getBatch(21);
		Batch batch3 = db.getBatchDAO().getBatch(41);
		
		batch1.setCur_username("");
		batch2.setCur_username("");
		batch3.setCur_username("");
		
		db.getBatchDAO().update(batch1);
		db.getBatchDAO().update(batch2);
		db.getBatchDAO().update(batch3);

		db.endTransaction(true);
		
	}

	@Test
	public void testGetFields() throws Exception {
		GetFields_Params params = new GetFields_Params("sheila", "parker", "1");
		GetFields_Result result = cc.getFields(params);
		assertTrue(result.toString().equals("1\n1\nLast Name\n1\n2\nFirst Name\n1\n3\nGender\n1\n4\nAge\n"));

		params = new GetFields_Params("sheila", "parker", "2");
		result = cc.getFields(params);
		assertTrue(result.toString().equals("2\n5\nGender\n2\n6\nAge\n2\n7\nLast Name\n2\n8\nFirst Name\n2\n9\nEthnicity\n"));
		
		params = new GetFields_Params("sheila", "parker", "3");
		result = cc.getFields(params);
		assertTrue(result.toString().equals("3\n10\nLast Name\n3\n11\nFirst Name\n3\n12\nAge\n3\n13\nEthnicity\n"));
		
		params = new GetFields_Params("sheila", "parkr", "3");
		result = cc.getFields(params);
		assertTrue(result.toString().equals("FAILED\n"));
		
		params = new GetFields_Params("", "", "3");
		result = cc.getFields(params);
		assertTrue(result.toString().equals("FAILED\n"));
	}

	@Test
	public void testSearch() throws Exception {
		Search_Params params = new Search_Params("sheila", "parker", "10,11,12,13", "SLOAN");
		Search_Result result = cc.search(params);
		assertTrue(result.getMatches().size() == 1);
		
		params = new Search_Params("sheila", "parker", "10,11,12,13", "sloan");
		result = cc.search(params);
		assertTrue(result.getMatches().size() == 1);
		
		params = new Search_Params("sheila", "parker", "11", "slOAn");
		result = cc.search(params);
		assertTrue(result.toString().equals("FAILED\n"));

		params = new Search_Params("sheila", "parker", ",,,", "slOAn");
		result = cc.search(params);
		assertTrue(result.toString().equals("FAILED\n"));
		
		params = new Search_Params("sheila", "parker", "", "");
		result = cc.search(params);
		assertTrue(result.toString().equals("FAILED\n"));
		
		params = new Search_Params("", "", "", "");
		result = cc.search(params);
		assertTrue(result.toString().equals("FAILED\n"));
		
		params = new Search_Params("sheila", "parker", "10,11,12,13", "22");
		result = cc.search(params);
		assertTrue(result.getMatches().size() == 16);
	}

	@Test
	public void testDownloadFile() throws Exception  {
		String file_url = "records/images/1890_image0.png";
		DownloadFile_Result obj_result = cc.downloadFile(file_url);
		byte[] result = obj_result.getFile_download();
		Path path = new File("records/images/1890_image0.png").toPath();
		assertTrue(Arrays.equals(result, Files.readAllBytes(path)));
		
	}

}
