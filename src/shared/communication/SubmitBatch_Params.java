package shared.communication;

import java.util.List;

import shared.models.IndexedData;
/**
 * Wrapper class for the parameters of the submitBatch() method.
 * contains an indexed_record object which contains all the information
 * pertaining to specific records in a batch (including batch_id).
 * @author lindell
 *
 */
public class SubmitBatch_Params extends Communicator_Params {
	private List<IndexedData> indexed_data;
	
	SubmitBatch_Params(String username, String password, int batch_id, List<IndexedData> indexed_data) {
		super(username, password);
		this.indexed_data = indexed_data;
	}
	
	public List<IndexedData> getIndexed_data() {
		return indexed_data;
	}
	
	public void setIndexed_data(List<IndexedData> indexed_data) {
		this.indexed_data = indexed_data;
	}
}
