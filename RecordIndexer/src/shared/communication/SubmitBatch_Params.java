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
	private List<List<IndexedData>> records;
	private int batch_id;
	
	public SubmitBatch_Params(String username, String password, int batch_id, List<List<IndexedData>> records) {
		super(username, password);
		this.batch_id = batch_id;
		this.records = records;
	}
	
	public int getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}

	public List<List<IndexedData>> getRecords() {
		return records;
	}

	public void setRecords(List<List<IndexedData>> records) {
		this.records = records;
	}


}
