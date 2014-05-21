package shared.communication;

/**
 * Wrapper class that contains the comma separated field ids and search strings
 * that will be passed into the search() method.
 * @author lindell
 *
 */
public class Search_Params {
	// These strings represent comma separated lists
	private String field_ids;
	private String search_strings;
	
	public Search_Params(String field_ids, String search_strings) {
		this.field_ids = field_ids;
		this.search_strings = search_strings;
	}

	public String getField_ids() {
		return field_ids;
	}

	public void setField_ids(String field_ids) {
		this.field_ids = field_ids;
	}

	public String getSearch_strings() {
		return search_strings;
	}

	public void setSearch_strings(String search_strings) {
		this.search_strings = search_strings;
	}


}
