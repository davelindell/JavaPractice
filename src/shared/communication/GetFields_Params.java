package shared.communication;

public class GetFields_Params extends Communicator_Params {
	private String project_id;
	
	/**
	 * Default constructor (project_id is specified)
	 * @param username
	 * @param password
	 * @param project_id
	 */
	public GetFields_Params(String username, String password, String project_id) {
		super(username, password);
		this.project_id = project_id;
	}
	
	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	
}
