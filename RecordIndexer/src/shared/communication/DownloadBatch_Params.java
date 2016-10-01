package shared.communication;

/**
 * Wrapper class for the parameters o the downloadBatch method of the ClientCommunicator
 * @author lindell
 *
 */
public class DownloadBatch_Params extends Communicator_Params {
	private int project_id;
	
	public DownloadBatch_Params(String username, String password, int project_id) {
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
