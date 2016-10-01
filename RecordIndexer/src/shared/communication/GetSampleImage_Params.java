package shared.communication;

/**
 * Wrapper class for the parameters for the getSampleImage method.
 * @author lindell
 *
 */

public class GetSampleImage_Params extends Communicator_Params {
	
	int project_id;
	
	public GetSampleImage_Params(String username, String password, int project_id) {
		super(username, password);
		this.project_id = project_id;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

}
