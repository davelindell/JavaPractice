package client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import client.synchronizer.BatchStatus;

public class IndexerFrame extends JFrame {
	private FileMenu file_menu;
	private ButtonBar button_bar;
	private JSplitPane vert_split_pane;
	private JSplitPane horz_split_pane;
	private JTabbedPane entry_tabbed_pane;
	private JTabbedPane help_tabbed_pane;
	private TableEntry table_entry;
	private FormEntry form_entry;
	private FieldHelp field_help;
	private ImagePanel image_panel;
	private BatchStatus batch_status;

	public IndexerFrame(BatchStatus batch_status) {
		this.batch_status = batch_status;
		createComponents();
	}
	
	private void createComponents() {
		this.setTitle("Indexer Beta");
		
		file_menu = new FileMenu(batch_status);
		button_bar = new ButtonBar();
		image_panel = new ImagePanel();
		
		table_entry = new TableEntry();
		form_entry = new FormEntry();
		field_help = new FieldHelp();
		
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



