package shared.models;
/**
 * A project model class
 * @author lindell
 *
 */
public class Project {
	private int project_id;
	private String project_title;
	
	public Project(String project_title) {
		project_id = 0; // sql auto-increment will give correct project id
		this.project_title = project_title;
	}

	public Project() {
		project_id = 0;
		project_title = null;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public String getProject_title() {
		return project_title;
	}

	public void setProject_title(String project_title) {
		this.project_title = project_title;
	}
	
}
