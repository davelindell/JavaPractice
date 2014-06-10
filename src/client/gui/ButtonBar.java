package client.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListener;
import client.synchronizer.BatchStateListenerAdapter;

public class ButtonBar extends JPanel {
	private BatchState batch_state;
	private JButton zoom_in_button;
	private JButton zoom_out_button;
	private JButton invert_image_button;
	private JButton toggle_highlights_button;
	private JButton save_button;
	private JButton submit_button;

	public ButtonBar(BatchState batch_state) {
		this.batch_state = batch_state;
		batch_state.addListener(batch_state_listener);
		createComponents();
	}
	
	private void createComponents() {
		zoom_in_button = new JButton("Zoom In");
		zoom_in_button.addActionListener(zoom_in_button_listener);
		zoom_in_button.setEnabled(false);
		this.add(zoom_in_button);
		
		zoom_out_button = new JButton("Zoom Out");
		zoom_out_button.addActionListener(zoom_out_button_listener);
		zoom_out_button.setEnabled(false);
		this.add(zoom_out_button);
		
		invert_image_button = new JButton("Invert Image");
		invert_image_button.addActionListener(invert_button_listener);
		invert_image_button.setEnabled(false);
		this.add(invert_image_button);
		
		toggle_highlights_button = new JButton("Toggle Highlights");
		toggle_highlights_button.setEnabled(false);
		this.add(toggle_highlights_button);
		
		save_button = new JButton("Save");
		save_button.setEnabled(false);
		this.add(save_button);
		
		submit_button = new JButton("Submit");
		submit_button.addActionListener(submit_button_listener);
		submit_button.setEnabled(false);
		this.add(submit_button);
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	
	
	private ActionListener zoom_in_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			batch_state.pushZoomIn();
		}
	};
	
	private ActionListener zoom_out_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			batch_state.pushZoomOut();
		}
	};
	
	private ActionListener invert_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			batch_state.pushInvertImage();
		}
	};

	
	private ActionListener submit_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			batch_state.pushSubmitBatch();
		}
	};
	
	private BatchStateListenerAdapter batch_state_listener = new BatchStateListenerAdapter() {
		@Override
		public void fireDownloadBatch(BufferedImage batch_image) {
			ButtonBar.this.zoom_in_button.setEnabled(true);
			ButtonBar.this.zoom_out_button.setEnabled(true);
			ButtonBar.this.invert_image_button.setEnabled(true);
			ButtonBar.this.toggle_highlights_button.setEnabled(true);
			ButtonBar.this.save_button.setEnabled(true);
			ButtonBar.this.submit_button.setEnabled(true);
		}

		@Override
		public void fireSubmitBatch() {
			ButtonBar.this.zoom_in_button.setEnabled(false);
			ButtonBar.this.zoom_out_button.setEnabled(false);
			ButtonBar.this.invert_image_button.setEnabled(false);
			ButtonBar.this.toggle_highlights_button.setEnabled(false);
			ButtonBar.this.save_button.setEnabled(false);
			ButtonBar.this.submit_button.setEnabled(false);
		}

	};

}
