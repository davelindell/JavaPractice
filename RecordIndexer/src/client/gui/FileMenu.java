package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListenerAdapter;

@SuppressWarnings("serial")
public class FileMenu extends JMenuBar {
	private JMenuItem download_batch_menu_item;
	private JMenuItem logout_menu_item;
	private JMenuItem exit_menu_item;
	private BatchState batch_state;
	
	public FileMenu(BatchState batch_state) {
		this.batch_state = batch_state;
		batch_state.addListener(batch_state_listener);
		createComponents();
	}
	
	private void createComponents() {	
	    JMenu menu = new JMenu("File");
	    menu.setMnemonic('f');
	    this.add(menu);
	
	    download_batch_menu_item = new JMenuItem("Download Batch", KeyEvent.VK_D);
	    download_batch_menu_item.addActionListener(download_batch_button_listener);
	    menu.add(download_batch_menu_item);
	
	    logout_menu_item = new JMenuItem("Logout", KeyEvent.VK_L);
	    logout_menu_item.addActionListener(logout_button_listener);
	    menu.add(logout_menu_item);
	    
	    exit_menu_item = new JMenuItem("Exit", KeyEvent.VK_X);
	    exit_menu_item.addActionListener(exit_button_listener);
	    menu.add(exit_menu_item);
	}
	
	private ActionListener logout_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			batch_state.pushLogout();
		}
	};
	
	private ActionListener exit_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			batch_state.pushLogout();
			System.exit(0);
		}
	};

	private ActionListener download_batch_button_listener = new ActionListener() {
		@SuppressWarnings("unused")
		@Override
		public void actionPerformed(ActionEvent e) {
			DownloadBatchWindow download_batch_window = new DownloadBatchWindow(batch_state);
		}
	};
	
	private BatchStateListenerAdapter batch_state_listener = new BatchStateListenerAdapter() {
		@Override
		public void fireDownloadBatch() {		
			download_batch_menu_item.setEnabled(false);
		}
		
		@Override
		public void fireSubmitBatch() {	
			download_batch_menu_item.setEnabled(true);
		}
		
		@Override
		public void fireLoad() {	
			if (batch_state.getImage() != null) {
				fireDownloadBatch();
			}
			else {
				fireSubmitBatch();
			}		
		}
	};
	

	
}

