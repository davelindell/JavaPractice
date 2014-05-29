package server.database;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shared.models.Project;

public class ProjectDAOTest {
	private Database db;
	private ProjectDAO projectDAO;
	private List<Project> projects;
	
	@Before
	public void setUp() throws Exception {	
		File src = new File("database" + File.separator + "empty_record_indexer.sqlite");
		File dst = new File("database"  + File.separator + "record_indexer.sqlite");
		FileUtils.copyFile(src, dst);
		Database.initialize();
		db  = new Database();
		projectDAO = new ProjectDAO(db);
		db.startTransaction();
		projects = initProjects();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(true);
		projectDAO = null;
		File src = new File("database" + File.separator + "empty_record_indexer.sqlite");
		File dst = new File("database"  + File.separator + "record_indexer.sqlite");
		FileUtils.copyFile(src, dst);	}

	@Test
	public void testGetAll() throws DatabaseException {
		projectDAO.add(projects.get(0));
		projectDAO.add(projects.get(1));
		assertEquals(2, projectDAO.getAll().size());
	}

	@Test
	public void testAdd() throws DatabaseException {
		Project project1 = projects.get(0);
		Project project2 = projects.get(1);

		projectDAO.add(project1);
		projectDAO.add(project2);
		
		List<Project> project_list = projectDAO.getAll();
		assertEquals(project_list.size(), 2);
		
		boolean found_project1 = false;
		boolean found_project2 = false;
		
		for(Project project : project_list) {
			if (found_project1 == false) {
				found_project1 = areEqual(project, project1);
			}
			if (found_project2 == false) {
				found_project2 = areEqual(project, project2);
			}
		}
		assertTrue(found_project1 && found_project2);
	}

	@Test
	public void testUpdate() throws DatabaseException {
		Project project1 = projects.get(0);
		Project project2 = projects.get(1);

		projectDAO.add(project1);
		projectDAO.add(project2);
		
		List<Project> project_list = projectDAO.getAll();
		
		project1.setProject_id(project_list.get(0).getProject_id());
		project2.setProject_id(project_list.get(1).getProject_id());
		
		project1.setProject_title("project1new");
		
		project2.setProject_title("project2new");
		
		projectDAO.update(project1);
		projectDAO.update(project2);
		
		project_list = projectDAO.getAll();
		
		boolean updated_project1 = false;
		boolean updated_project2 = false;
		
		for(Project project : project_list) {
			if (!updated_project1) {
				updated_project1 = areEqual(project, project1);
			}		
			if (!updated_project2) {
				updated_project2 = areEqual(project, project2);
			}
		}
		
		assertTrue(updated_project1 && updated_project2);
	}

	@Test
	public void testDelete() throws DatabaseException {
		Project project1 = projects.get(0);
		Project project2 = projects.get(1);

		projectDAO.add(project1);
		projectDAO.add(project2);
		
		List<Project> project_list = projectDAO.getAll();
		project1.setProject_id(project_list.get(0).getProject_id());
		project2.setProject_id(project_list.get(1).getProject_id());
		
		assertEquals(project_list.size(), 2);
		
		projectDAO.delete(project1);
		projectDAO.delete(project2);
		
		project_list = projectDAO.getAll();
		assertTrue(project_list.size() == 0);
	}

	private boolean areEqual(Project project1, Project project2) {
		return 	project1.getProject_title().equals(project2.getProject_title());
	}
	
	private List<Project> initProjects() {
		List<Project> projects = new ArrayList<Project>();
		
		Project project1 = new Project();
		project1.setProject_title("project1");
		
		Project project2 = new Project();
		project2.setProject_title("project2");

		
		projects.add(project1);
		projects.add(project2);

		return projects;
	}
	
}
