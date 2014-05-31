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
	List<SearchResultTuple> matches;
	
	public Search_Result() {
		matches = null;
	}

	public List<SearchResultTuple> getMatches() {
		return matches;
	}

	public void setMatches(List<SearchResultTuple> matches) {
		this.matches = matches;
	}

	@Override
	public String toString() {
		String result = "";
		if (matches == null)
			result = "FAILED\n";
		else if (matches.size() == 0) 
			result = "FAILED\n";
		else {
			for (SearchResultTuple s : matches) {
				result += 	s.getBatch_id() + "\n" +
						 	s.getImage_url() + "\n" + 
						 	s.getRecord_number() + "\n" +
						 	s.getField_id() + "\n";
			}
		}
		return result;
	}
	

}
