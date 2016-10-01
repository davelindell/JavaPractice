package shared.models;
/**
 * The Batch model class
 * @author lindell
 *
 */
public class Batch {
	private int batch_id;
	private int project_id;
	private String image_url;
	private int first_y_coord;
	private int record_height;
	private int num_records;
	private int num_fields;
	private String cur_username;
	
	/**
	 * Batch constructor (initializes data to null values, use setters 
	 * to set the data).
	 */
	public Batch() {
		batch_id = 0;
		project_id = 0;
		image_url = null;
		first_y_coord = 0;
		record_height = 0;
		num_records = 0;
		num_fields = 0;
		cur_username = "";
	}

	public int getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public int getFirst_y_coord() {
		return first_y_coord;
	}

	public void setFirst_y_coord(int first_y_coord) {
		this.first_y_coord = first_y_coord;
	}

	public int getRecord_height() {
		return record_height;
	}

	public void setRecord_height(int record_height) {
		this.record_height = record_height;
	}

	public int getNum_records() {
		return num_records;
	}

	public void setNum_records(int num_records) {
		this.num_records = num_records;
	}

	public int getNum_fields() {
		return num_fields;
	}

	public void setNum_fields(int num_fields) {
		this.num_fields = num_fields;
	}
	
	public String getCur_username() {
		return cur_username;
	}

	public void setCur_username(String cur_username) {
		this.cur_username = cur_username;
	}
	
}
