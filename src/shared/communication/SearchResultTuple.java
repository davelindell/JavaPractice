package shared.communication;

/**
 * The SearchResult class is a container for individual data items that are returned by the search.
 * @author lindell
 *
 */
public class SearchResultTuple {
	private int batch_id;
	private String image_url; 
	private int record_number;
	private int field_id;
	
	public SearchResultTuple(int batch_id, String image_url, int record_number, int field_id) {
		this.batch_id = batch_id;
		this.image_url = image_url;
		this.record_number = record_number;
		this.field_id = field_id;
	}

	public SearchResultTuple() {
		this.batch_id = 0;
		this.image_url = null;
		this.record_number = 0;
		this.field_id = 0;
	}

	public int getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
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

}