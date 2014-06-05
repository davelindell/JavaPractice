package client.gui;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class FileMenu extends JMenuBar {
	private JMenuItem download_batch_menu_item;
	private JMenuItem logout_menu_item;
	private JMenuItem exit_menu_item;
	
	public FileMenu() {
		createComponents();
		
	}
	
	private void createComponents() {	
	    JMenu menu = new JMenu("File");
	    menu.setMnemonic('f');
	    this.add(menu);
	
	    download_batch_menu_item = new JMenuItem("Download Batch", KeyEvent.VK_D);
	    // download_batch_menu_item.addActionListener(actionListener);
	    menu.add(download_batch_menu_item);
	
	    logout_menu_item = new JMenuItem("Logout", KeyEvent.VK_L);
	    // logout_menu_item.addActionListener(actionListener);
	    menu.add(logout_menu_item);
	    
	    exit_menu_item = new JMenuItem("Exit", KeyEvent.VK_X);
	    // exit_menu_item.addActionListener(actionListener);
	    menu.add(exit_menu_item);
	    
	}
}
