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
		assertTrue(result.toString().equals("images/1890_image19.png\n"));	
		
		params = new GetSampleImage_Params("sheila", "parker", 2);
		result = cc.getSampleImage(params);
		assertTrue(result.toString().equals("images/1900_image19.png\n"));
		
		params = new GetSampleImage_Params("sheila", "parker", 3);
		result = cc.getSampleImage(params);
		assertTrue(result.toString().equals("images/draft_image19.png\n"));
		
		params = new GetSampleImage_Params("sheila", "parer", 3);
		result = cc.getSampleImage(params);
		assertTrue(result.toString().equals("FAILED\n"));
		
	}

	@Test
	public void testDownloadBatch() throws Exception {		
		DownloadBatch_Params params = new DownloadBatch_Params("sheila", "parker", 1);
		DownloadBatch_Result result = cc.downloadBatch(params);
		assertTrue(result.toString().equals("1\n1\nimages/1890_image0.png\n199\n60\n8\n4\n1\n1\n"
											+ "Last Name\nfieldhelp/last_name.html\n60\n300\n"
											+ "knowndata/1890_last_names.txt\n2\n2\nFirst Name\n"
											+ "fieldhelp/first_name.html\n360\n280\nknowndata/1890_first_names.txt\n"
											+ "3\n3\nGender\nfieldhelp/gender.html\n640\n205\nknowndata/genders.txt\n"
											+ "4\n4\nAge\nfieldhelp/age.html\n845\n120\n"));	
		
		ServerFacadeTest sft = new ServerFacadeTest();
		List<List<IndexedData>> data_list = sft.makeRecords("a,b,c,d;e,,f;",1);
		SubmitBatch_Params submit_params = new SubmitBatch_Params("sheila", "parker",1, data_list);
		cc.submitBatch(submit_params);

		
		params = new DownloadBatch_Params("sheila", "parker", 2);
		result = cc.downloadBatch(params);
		assertTrue(result.toString().equals("21\n2\nimages/1900_image0.png\n204\n62\n10\n5\n5\n1\n"
											+ "Gender\nfieldhelp/gender.html\n45\n205\nknowndata/genders.txt\n6\n"
											+ "2\nAge\nfieldhelp/age.html\n250\n120\n7\n3\n"
											+ "Last Name\nfieldhelp/last_name.html\n370\n325\nknowndata/1900_last_names.txt\n"
											+ "8\n4\nFirst Name\nfieldhelp/first_name.html\n695\n325\n"
											+ "knowndata/1900_first_names.txt\n9\n5\nEthnicity\nfieldhelp/ethnicity.html\n"
											+ "1020\n450\nknowndata/ethnicities.txt\n"));
		
		data_list = sft.makeRecords("a,b,c,d;e,,f;",21);
		submit_params = new SubmitBatch_Params("sheila", "parker",21, data_list);
		cc.submitBatch(submit_params);
		
		params = new DownloadBatch_Params("sheila", "parker", 3);
		result = cc.downloadBatch(params);
		assertTrue(result.toString().equals("41\n3\nimages/draft_image0.png\n195\n65\n7\n4\n10\n1\nLast Name\n"
											+ "fieldhelp/last_name.html\n75\n325\nknowndata/draft_last_names.txt\n11\n2\nFirst Name\n"
											+ "fieldhelp/first_name.html\n400\n325\nknowndata/draft_first_names.txt\n12\n3\nAge\n"
											+ "fieldhelp/age.html\n725\n120\n13\n4\nEthnicity\nfieldhelp/ethnicity.html\n845\n450"
											+ "\nknowndata/ethnicities.txt\n"));
		
		data_list = sft.makeRecords("a,b,c,d;e,,f;;",41);
		submit_params = new SubmitBatch_Params("sheila", "parker",41, data_list);
		cc.submitBatch(submit_params);
		
		params = new DownloadBatch_Params("sheil", "parker", 3);
		result = cc.downloadBatch(params);
		assertTrue(result.toString().equals("FAILED\n"));
		
		

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
		
	}

	@Test
	public void testGetFields() throws Exception {
		GetFields_Params params = new GetFields_Params("sheila", "parker", 1);
		GetFields_Result result = cc.getFields(params);
		assertTrue(result.toString().equals("1\n1\nLast Name\n1\n2\nFirst Name\n1\n3\nGender\n1\n4\nAge\n"));

		params = new GetFields_Params("sheila", "parker", 2);
		result = cc.getFields(params);
		assertTrue(result.toString().equals("2\n5\nGender\n2\n6\nAge\n2\n7\nLast Name\n2\n8\nFirst Name\n2\n9\nEthnicity\n"));
		
		params = new GetFields_Params("sheila", "parker", 3);
		result = cc.getFields(params);
		assertTrue(result.toString().equals("3\n10\nLast Name\n3\n11\nFirst Name\n3\n12\nAge\n3\n13\nEthnicity\n"));
		
		params = new GetFields_Params("sheila", "parkr", 3);
		result = cc.getFields(params);
		assertTrue(result.toString().equals("FAILED\n"));
		
		params = new GetFields_Params("", "", 3);
		result = cc.getFields(params);
		assertTrue(result.toString().equals("FAILED\n"));
	}

	@Test
	public void testSearch() throws Exception {
		Search_Params params = new Search_Params("sheila", "parker", "10,11,12,13", "SLOAN");
		Search_Result result = cc.search(params);
		assertTrue(result.toString().equals("51\nimages/draft_image10.png\n4\n10\n"));
		
		params = new Search_Params("sheila", "parker", "10,11,12,13", "sloan");
		result = cc.search(params);
		assertTrue(result.toString().equals("51\nimages/draft_image10.png\n4\n10\n"));
		
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
		assertTrue(result.toString().equals("44\nimages/draft_image3.png\n2\n12\n45\nimages/draft_image4.png\n"
											+ "2\n12\n45\nimages/draft_image4.png\n3\n12\n45\nimages/draft_image4.png\n"
											+ "5\n12\n46\nimages/draft_image5.png\n4\n12\n47\nimages/draft_image6.png\n"
											+ "7\n12\n48\nimages/draft_image7.png\n5\n12\n49\nimages/draft_image8.png\n"
											+ "7\n12\n50\nimages/draft_image9.png\n2\n12\n51\nimages/draft_image10.png\n"
											+ "4\n12\n51\nimages/draft_image10.png\n5\n12\n52\nimages/draft_image11.png\n"
											+ "2\n12\n56\nimages/draft_image15.png\n2\n12\n56\nimages/draft_image15.png\n"
											+ "3\n12\n56\nimages/draft_image15.png\n5\n12\n57\nimages/draft_image16.png\n2"
											+ "\n12\n"));
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
