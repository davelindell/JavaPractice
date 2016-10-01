package shared.communication;
import java.util.List;

import shared.models.Field;

/**
 * Wrapper class for the return value of the downloadBatch method.
 * The constructor initializes the variables to a default value, 
 * so they must be set manually with the setter methods given.
 * @author lindell
 *
 */
public class DownloadBatch_Result {
	private int batch_id;
	private int project_id;
	private String image_url;
	private int first_y_coord;
	private int record_height;
	private int num_records;
	private int num_fields;
	List<Field> fields;
	
	public DownloadBatch_Result() {
		batch_id = 0;
		project_id = 0;
		image_url = null;
		first_y_coord = 0;
		record_height = 0;
		num_records = 0;
		num_fields = 0;
		fields = null;
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

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		String result = null;
		if (batch_id == 0) {
			result = "FAILED\n";
		}
		else {
			result = Integer.toString(batch_id) + "\n" + 
					 Integer.toString(project_id) + "\n" + 
					 image_url + "\n" + 
					 Integer.toString(first_y_coord) + "\n" + 
					 Integer.toString(record_height) + "\n" + 
					 Integer.toString(num_records) + "\n" + 
					 Integer.toString(num_fields) + "\n";
			for (Field field : fields) {
				result += Integer.toString(field.getField_id()) + "\n" + 
						  Integer.toString(field.getField_num()) + "\n" + 
						  field.getField_title() + "\n" + 
						  field.getHelp_url() + "\n" + 
						  Integer.toString(field.getX_coord()) + "\n" + 
						  Integer.toString(field.getPixel_width()) + "\n";
				if (field.getKnown_values_url() != null)
					result += field.getKnown_values_url() + "\n";
			}
		}
		return result;
	}
	
}
