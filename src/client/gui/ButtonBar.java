package client.gui;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import client.synchronizer.BatchStateListener;

public class ButtonBar extends JPanel implements BatchStateListener {
	private JButton zoom_in_button;
	private JButton zoom_out_button;
	private JButton invert_image_button;
	private JButton toggle_highlights_button;
	private JButton save_button;
	private JButton submit_button;

	public ButtonBar() {
		createComponents();
	}
	
	private void createComponents() {
		zoom_in_button = new JButton("Zoom In");
		zoom_in_button.setEnabled(false);
		this.add(zoom_in_button);
		
		zoom_out_button = new JButton("Zoom Out");
		zoom_out_button.setEnabled(false);
		this.add(zoom_out_button);
		
		invert_image_button = new JButton("Invert Image");
		invert_image_button.setEnabled(false);
		this.add(invert_image_button);
		
		toggle_highlights_button = new JButton("Toggle Highlights");
		toggle_highlights_button.setEnabled(false);
		this.add(toggle_highlights_button);
		
		save_button = new JButton("Save");
		save_button.setEnabled(false);
		this.add(save_button);
		
		submit_button = new JButton("Submit");
		submit_button.setEnabled(false);
		this.add(submit_button);
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
	}

	@Override
	public void fireLogoutButton() {		
	}

	@Override
	public void fireDownloadedBatch() {
		this.zoom_in_button.setEnabled(true);
		this.zoom_out_button.setEnabled(true);
		this.invert_image_button.setEnabled(true);
		this.toggle_highlights_button.setEnabled(true);
		this.save_button.setEnabled(true);
		this.submit_button.setEnabled(true);
	}

	@Override
	public void fireSubmittedBatch() {
		this.zoom_in_button.setEnabled(false);
		this.zoom_out_button.setEnabled(false);
		this.invert_image_button.setEnabled(false);
		this.toggle_highlights_button.setEnabled(false);
		this.save_button.setEnabled(false);
		this.submit_button.setEnabled(false);
	}
}
