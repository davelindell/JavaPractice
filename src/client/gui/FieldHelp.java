package client.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import client.communication.ClientCommunicator;
import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListenerAdapter;

public class FieldHelp extends JPanel {
	private JScrollPane scroll_pane;
	private BatchState batch_state;
	
	public FieldHelp(BatchState batch_state) {
		scroll_pane = new JScrollPane();
		this.batch_state = batch_state;
		createComponents();
	}
	
	private void createComponents() {
		this.setPreferredSize(new Dimension(600, 200));
		
		
	}
	
	private BatchStateListenerAdapter batch_state_listener = new BatchStateListenerAdapter() {

	};

}
