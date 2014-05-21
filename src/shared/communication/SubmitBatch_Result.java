package shared.communication;
/**
 * Wrapper class for the return value of the submitBatch()
 * method. This simply wraps a boolean value which tells whether
 * the method completed successfully or not.
 * @author lindell
 *
 */
public class SubmitBatch_Result {
	private boolean valid;
	
	SubmitBatch_Result(boolean valid) {
		this.valid = valid;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
}
