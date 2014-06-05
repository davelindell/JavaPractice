package client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class IndexerFrame extends JFrame {
	private FileMenu file_menu;
	private ButtonBar button_bar;
	private JSplitPane vert_split_pane;
	private JSplitPane horz_split_pane;
	private ImagePanel image_panel;

	public IndexerFrame() {
		createComponents();
		
	}
	
	private void createComponents() {
		file_menu = new FileMenu();
		setJMenuBar(file_menu);
		
		button_bar = new ButtonBar();
		this.add(button_bar, BorderLayout.NORTH);
		
		image_panel = new ImagePanel();
		
		horz_split_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		vert_split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, image_panel, horz_split_pane);
		this.add(vert_split_pane);
	}
	
	
	
	private WindowAdapter windowAdapter = new WindowAdapter() {
    	
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    };
	
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {		
			public void run() {
				IndexerFrame frame = new IndexerFrame();
				frame.pack();
				frame.setVisible(true);
			}
		});

	}
}
