package shared.communication;

public class GetFields_Params extends Communicator_Params {
	private int project_id;
	
	/**
	 * Default constructor (project_id is specified)
	 * @param username
	 * @param password
	 * @param project_id
	 */
	public GetFields_Params(String username, String password, int project_id) {
		super(username, password);
		this.project_id = project_id;
	}
	/**
	 * Alternate constructor sets project_id to 0 if project_id is not specified.
	 * @param username
	 * @param password
	 * @param project_id
	 */
	public GetFields_Params(String username, String password) {
		super(username, password);
		this.project_id = 0;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	
}
