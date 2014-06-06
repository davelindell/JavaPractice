package client.gui;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class RecordIndexer extends JFrame {
	private IndexerFrame indexer_frame;
	private LoginWindow login_window;
	private String hostname;
	private String port;
	
	public RecordIndexer(String hostname, String port) {
		this.hostname = hostname;
		this.port = port;
		createComponents();
	}
	
	private void createComponents() {
		indexer_frame = new IndexerFrame();
		login_window = new LoginWindow(hostname, port);
	}
	
	private LoginWindow getLoginWindow() {
		return this.login_window;
	}
	
	public static void main(final String[] args) {

		EventQueue.invokeLater(new Runnable() {		
			public void run() {
				RecordIndexer record_indexer = new RecordIndexer(args[0], args[1]);
				record_indexer.pack();
				record_indexer.setVisible(false);
				
				LoginWindow login_window = record_indexer.getLoginWindow();
				login_window.pack();
				login_window.setVisible(true);
			}
		});

	}
}
