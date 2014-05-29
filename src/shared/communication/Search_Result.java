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
	

}
