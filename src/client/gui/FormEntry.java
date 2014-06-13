package client.gui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class FormEntry extends JPanel {
	FormEntryModel form_model = new FormEntryModel();
	
	public FormEntry() {
		createComponents();
	}
	
	private void createComponents() {
		this.setPreferredSize(new Dimension(600, 200));
		
		JList row_list = new JList();
		this.form_model = new FormEntryModel();
		row_list.setModel(this.form_model);
		
		
		JSplitPane split_pane = new JSplitPane();

	}
}
