package shared.models;
/**
 * Class to represent a field
 * @author lindell
 *
 */
public class Field {
	private int project_id;
	private int field_id;
	private int field_num;
	private String field_title;
	private String help_url;
	private int x_coord;
	private int pixel_width;
	private String known_values_url;
	
	public Field() {
		project_id = 0;
		field_id = 0;
		field_title = null;
		help_url = null;
		x_coord = 0;
		pixel_width = 0;
		known_values_url = null;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public int getField_id() {
		return field_id;
	}

	public void setField_id(int field_id) {
		this.field_id = field_id;
	}

	public String getField_title() {
		return field_title;
	}

	public void setField_title(String field_title) {
		this.field_title = field_title;
	}

	public String getHelp_url() {
		return help_url;
	}

	public void setHelp_url(String help_url) {
		this.help_url = help_url;
	}

	public int getX_coord() {
		return x_coord;
	}

	public void setX_coord(int x_coord) {
		this.x_coord = x_coord;
	}

	public int getPixel_width() {
		return pixel_width;
	}

	public void setPixel_width(int pixel_width) {
		this.pixel_width = pixel_width;
	}

	public String getKnown_values_url() {
		return known_values_url;
	}

	public void setKnown_values_url(String known_values_url) {
		this.known_values_url = known_values_url;
	}
	
	public int getField_num() {
		return field_num;
	}

	public void setField_num(int field_num) {
		this.field_num = field_num;
	}
}
