package client.gui;

import java.util.List;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import client.synchronizer.BatchState;

@SuppressWarnings("serial")
public class SuggestionsList extends JList<String> {
	private SuggestionsListModel suggestions_model;

	public SuggestionsList(BatchState batch_state, List<String> suggestions) {
		suggestions_model = new SuggestionsListModel(batch_state, suggestions);
		this.setModel(suggestions_model);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	
	
}
