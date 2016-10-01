package shared.models;
/**
 * This class represents an indexed record and contains the record value and
 * information to identify where the record came from (i.e. project id, batch id).
 * @author lindell
 *
 */
public class IndexedData {
	private int data_id;
	private int batch_id;
	private int record_number;
	private int field_id;
	private String record_value;
	
	public IndexedData() {
		data_id = 0;
		batch_id = 0;
		record_number = 0;
		field_id = 0;
		record_value = null;
	}

	public int getData_id() {
		return data_id;
	}

	public void setData_id(int data_id) {
		this.data_id = data_id;
	}

	public int getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}

	public int getRecord_number() {
		return record_number;
	}

	public void setRecord_number(int record_number) {
		this.record_number = record_number;
	}

	public int getField_id() {
		return field_id;
	}

	public void setField_id(int field_id) {
		this.field_id = field_id;
	}

	public String getRecord_value() {
		return record_value;
	}

	public void setRecord_value(String record_value) {
		this.record_value = record_value;
	}
	
}
