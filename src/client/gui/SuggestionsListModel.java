package client.gui;

import java.util.List;

import javax.swing.AbstractListModel;

import client.synchronizer.BatchState;

public class SuggestionsListModel extends AbstractListModel<String>{
	private BatchState batch_state;
	private List<String> suggestions;
	private int row;
	private int column;
	
	public SuggestionsListModel(BatchState batch_state, int row, int column) {
		this.batch_state = batch_state;
		suggestions = batch_state.getSuggestions(row, column);
		this.row = row;
		this.column = column;
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
