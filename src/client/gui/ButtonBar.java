package client.gui;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class ButtonBar extends JPanel {
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
		this.add(zoom_in_button);
		
		zoom_out_button = new JButton("Zoom Out");
		this.add(zoom_out_button);
		
		invert_image_button = new JButton("Invert Image");
		this.add(invert_image_button);
		
		toggle_highlights_button = new JButton("Toggle Highlights");
		this.add(toggle_highlights_button);
		
		save_button = new JButton("Save");
		this.add(save_button);
		
		submit_button = new JButton("Submit");
		this.add(submit_button);
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	
}
