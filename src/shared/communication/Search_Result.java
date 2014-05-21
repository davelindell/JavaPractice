package shared.communication;

import java.util.List;
/**
 * Wrapper class that contains a list of tuples. 
 * Each tuple comprises the result of one successful 
 * search entry.
 * @author lindell
 *
 */
public class Search_Result {
	List<SearchResult> matches;
	
	public Search_Result() {
		matches = null;
	}

	public List<SearchResult> getMatches() {
		return matches;
	}

	public void setMatches(List<SearchResult> matches) {
		this.matches = matches;
	}
	
	/**
	 * The Tuple class is a container for individual data items that are returned by the search.
	 * @author lindell
	 *
	 */
	public class SearchResult {
		private int batch_id;
		private String image_url; 
		private int record_number;
		private int field_id;
		
		public SearchResult(int batch_id, String image_url, int record_number, int field_id) {
			this.batch_id = batch_id;
			this.image_url = image_url;
			this.record_number = record_number;
			this.field_id = field_id;
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
}
