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
	
}
