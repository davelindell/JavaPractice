package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import shared.communication.ValidateUser_Params;
import shared.communication.ValidateUser_Result;
import client.ClientException;
import client.synchronizer.BatchStatus;
import client.synchronizer.BatchStatusListener;

public class FileMenu extends JMenuBar implements BatchStatusListener {
	private JMenuItem download_batch_menu_item;
	private JMenuItem logout_menu_item;
	private JMenuItem exit_menu_item;
	private BatchStatus batch_status;
	
	public FileMenu(BatchStatus batch_status) {
		this.batch_status = batch_status;
		batch_status.addListener(this);
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
			batch_status.pushLogout();
		}
	};
	
	private ActionListener exit_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};

	private ActionListener download_batch_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			// TODO: Make the dialog box that pops up and prompts to select a project
			//batch_status.pushDownloadBatch(project_id);
		}
	};
	
	@Override
	public void fireLogoutButton() {		
	}

	@Override
	public void fireDownloadedBatch() {		
		download_batch_menu_item.setEnabled(false);
	}

	@Override
	public void fireSubmittedBatch() {		
	}
	
}

