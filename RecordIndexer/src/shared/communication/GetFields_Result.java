package shared.communication;

import java.util.List;

import shared.models.Field;

public class GetFields_Result {
	private List<Field> fields;
	
	public GetFields_Result(List<Field> fields) {
		this.fields = fields;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		String result = "";
		
		if (fields == null)
			result = "FAILED\n";
		else if (fields.size() == 0)
			result = "FAILED\n";
		else {
			for (Field field : fields) {
				result += field.getProject_id() + "\n" + 
						  field.getField_id() + "\n" + 
						  field.getField_title() + "\n";
				
				
			}
		}
		return result;
	}
	
}
