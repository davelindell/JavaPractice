package client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import client.synchronizer.BatchState;

public class IndexerFrame extends JFrame {
	private FileMenu file_menu;
	private ButtonBar button_bar;
	private JSplitPane vert_split_pane;
	private JSplitPane horz_split_pane;
	private JTabbedPane entry_tabbed_pane;
	private JTabbedPane help_tabbed_pane;
	private JScrollPane table_entry;
	private JScrollPane form_entry;
	private JScrollPane field_help;
	private ImagePanel image_panel;
	private BatchState batch_state;

	public IndexerFrame(BatchState batch_state) {
		this.batch_state = batch_state;
		createComponents();
	}
	
	private void createComponents() {
		this.setTitle("Indexer Beta");
		this.setLocationRelativeTo(null);

		file_menu = new FileMenu(batch_state);
		button_bar = new ButtonBar(batch_state);
		image_panel = new ImagePanel(batch_state);
		
		table_entry = new JScrollPane(new TableEntry(batch_state));
		form_entry = new JScrollPane(new FormEntry());
		field_help = new JScrollPane(new FieldHelp(batch_state));
		
		entry_tabbed_pane = new JTabbedPane();
		help_tabbed_pane = new JTabbedPane();
		horz_split_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, entry_tabbed_pane, help_tabbed_pane);
		vert_split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, image_panel, horz_split_pane);
		
		entry_tabbed_pane.addTab("Table Entry", table_entry);
		entry_tabbed_pane.addTab("Form Entry", form_entry);

		help_tabbed_pane.addTab("Field Help", field_help);
		
		setJMenuBar(file_menu);
		
		this.add(button_bar, BorderLayout.NORTH);
		
		this.add(vert_split_pane);
	}
	
	
	
	private WindowAdapter windowAdapter = new WindowAdapter() {
    	
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    };
	

}



