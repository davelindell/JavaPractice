package client.gui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import client.synchronizer.BatchState;

public class SuggestionsList extends JList {
	private SuggestionsListModel suggestions_model;
	private BatchState batch_state;

	public SuggestionsList(BatchState batch_state, int row, int column) {
		this.batch_state = batch_state;
		
		suggestions_model = new SuggestionsListModel(batch_state, row, column);
		this.setModel(suggestions_model);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	
	
}
