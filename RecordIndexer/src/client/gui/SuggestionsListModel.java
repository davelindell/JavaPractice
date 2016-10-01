package client.gui;

import java.util.List;

import javax.swing.AbstractListModel;

import client.synchronizer.BatchState;

@SuppressWarnings("serial")
public class SuggestionsListModel extends AbstractListModel<String>{
	private List<String> suggestions;
	
	public SuggestionsListModel(BatchState batch_state, List<String> suggestions) {
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
