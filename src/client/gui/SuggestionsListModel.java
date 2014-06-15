package client.gui;

import java.util.List;

import javax.swing.AbstractListModel;

import client.synchronizer.BatchState;

public class SuggestionsListModel extends AbstractListModel<String>{
	private BatchState batch_state;
	private List<String> suggestions;
	
	public SuggestionsListModel(BatchState batch_state, List<String> suggestions) {
		this.batch_state = batch_state;
		this.suggestions = suggestions;
	}
	
	@Override
	public int getSize() {
		return this.suggestions.size();
	}

	@Override
	public String getElementAt(int index) {
		return this.suggestions.get(index);
	}

}
