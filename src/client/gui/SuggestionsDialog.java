package client.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import shared.models.IndexedData;
import client.synchronizer.BatchState;

@SuppressWarnings("serial")
public class SuggestionsDialog extends JDialog {
	private BatchState batch_state;
	private int row;
	private int column;
	private SuggestionsList suggestions_list;
	private List<String> suggestions;
	
	private JButton cancel_button;
	private JButton use_button;
	
	public SuggestionsDialog(BatchState batch_state, int row, int column) {
		this.batch_state = batch_state;
		this.row = row;
		this.column = column;
		
		suggestions = batch_state.getSuggestions(row, column);
		suggestions_list = new SuggestionsList(batch_state, suggestions);
		
		createComponents();
	}

	private void createComponents() {
		this.setTitle("Suggestions");
		this.setPreferredSize(new Dimension(280,225));
		this.setResizable(false);
		this.setModal(true);
		this.setLocationRelativeTo(null);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JPanel buttons = new JPanel();
		cancel_button = new JButton("Cancel");
		use_button = new JButton("Use Suggestion");
		cancel_button.addActionListener(cancel_button_listner);
		use_button.addActionListener(use_button_listener);
		
		if (suggestions_list.getModel().getSize() == 0)
			use_button.setEnabled(false);
		
		buttons.setLayout(new FlowLayout());
		buttons.add(cancel_button);
		buttons.add(use_button);
		
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 10;
		gbc.weighty = 10;
		gbc.insets = new Insets(10,10,10,10);
		
		JScrollPane scroll_pane = new JScrollPane(suggestions_list);
		Dimension d = scroll_pane.getPreferredSize();
		d.width = 180;
		scroll_pane.setPreferredSize(d);
		this.add(scroll_pane, gbc);
		
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(10,10,10,10);
		
		this.add(buttons, gbc);
		this.pack();
	}
	
	private ActionListener cancel_button_listner = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	};
	
	private ActionListener use_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String cur_value = (String)suggestions_list.getSelectedValue();
			if (cur_value != null) {
				IndexedData cur_entry = batch_state.getRecords().get(row).get(column - 1);
				cur_entry.setRecord_value(cur_value);
				batch_state.pushEnteredData(row, column);
				dispose();
			}
			else{
				JOptionPane.showMessageDialog(SuggestionsDialog.this, "Make a selection first",
						 "Error", JOptionPane.ERROR_MESSAGE);
			}	
		}
	};
	
}
