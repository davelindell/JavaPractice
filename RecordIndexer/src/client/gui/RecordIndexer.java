package client.gui;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListenerAdapter;

public class RecordIndexer implements LoginWindowListener {
	private IndexerFrame indexer_frame;
	private LoginWindow login_window;
	private String hostname;
	private String port;
	private BatchState batch_state;
	
	public RecordIndexer(String hostname, String port) {
		this.hostname = hostname;
		this.port = port;
		this.batch_state = new BatchState(hostname, port);
		batch_state.addListener(batch_state_listener);
		createComponents();
	}
	
	private void createComponents() {
		indexer_frame = new IndexerFrame(batch_state);
		login_window = new LoginWindow(hostname, port, batch_state);
		login_window.addListener(this);

	}
	
	private LoginWindow getLoginWindow() {
		return this.login_window;
	}
	
	private IndexerFrame getIndexerFrame() {
		return this.indexer_frame;
	}
	
	public static void main(final String[] args) {

		EventQueue.invokeLater(new Runnable() {		
			public void run() {
				RecordIndexer record_indexer = new RecordIndexer(args[0], args[1]);
				
				IndexerFrame indexer_frame = record_indexer.getIndexerFrame();
				indexer_frame.pack();
				indexer_frame.setVisible(false);
				
				LoginWindow login_window = record_indexer.getLoginWindow();
				login_window.pack();
				login_window.setVisible(true);
			}
		});

	}

	@SuppressWarnings("unused")
	private WindowAdapter windowAdapter = new WindowAdapter() {
    	
        public void windowClosing(WindowEvent e) {
            batch_state.pushLogout();
        	System.exit(0);
        }
    };
	
	private BatchStateListenerAdapter batch_state_listener = new BatchStateListenerAdapter() {
		@Override
		public void fireLogoutButton() {
			indexer_frame.setVisible(false);
			login_window.setVisible(true);
		}
	};
	
	@Override
	public void loginSuccessful() {
		login_window.setVisible(false);
		indexer_frame.setVisible(true);
	}

	
}
