package client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import shared.communication.DownloadFile_Result;
import shared.communication.GetSampleImage_Params;
import shared.communication.GetSampleImage_Result;
import shared.models.Project;
import shared.models.User;
import client.ClientException;
import client.communication.ClientCommunicator;
import client.synchronizer.BatchState;

@SuppressWarnings("serial")
public class DownloadBatchWindow extends JDialog {
	private BatchState batch_state;
	private JLabel project_text_label;
	private JButton download_button;
	private JButton cancel_button;
	private JButton view_sample_button;
	private JComboBox<String> project_combobox;
	private JDialog dialog;
	private JButton close_button;
	JDialog sample_image_dialog;
	
	public DownloadBatchWindow(BatchState batch_state) {
		this.batch_state = batch_state;
		
		// make object accessible to inner classes
		dialog = this;
		createComponents();
	}
	
	private void createComponents() {
		// Create interior JPanel
		JPanel select_project_panel = new JPanel();
		select_project_panel.setLayout(new BoxLayout(select_project_panel, BoxLayout.PAGE_AXIS));
		select_project_panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		// Create components
		download_button = new JButton("Download");
		download_button.addActionListener(download_button_listener);
		
		cancel_button = new JButton("Cancel");
		cancel_button.addActionListener(cancel_button_listener);
		
		close_button = new JButton("Close");
		close_button.addActionListener(close_button_listener);
		
		sample_image_dialog = new JDialog();
		
		view_sample_button = new JButton("View Sample");
		view_sample_button.addActionListener(view_sample_button_listener);
		
		project_text_label = new JLabel("Project: ");
		
		List<String> proj_titles = new ArrayList<String>();
		for (Project p : batch_state.getProjects()) {
			proj_titles.add(p.getProject_title());
		}
		
		String[] titles_array = proj_titles.toArray(new String[proj_titles.size()]);
		project_combobox = 
				new JComboBox<String>(titles_array);
		
		// Create top row of components
		JPanel top_select_area = new JPanel();
		top_select_area.setLayout(new BoxLayout(top_select_area, BoxLayout.LINE_AXIS));
		top_select_area.add(project_text_label);
		top_select_area.add(project_combobox);
		top_select_area.add(Box.createRigidArea(new Dimension(5,0)));
		top_select_area.add(view_sample_button);

		// Create bottom row of components
		JPanel bottom_select_area = new JPanel();
		bottom_select_area.add(cancel_button);
		bottom_select_area.add(download_button);

		// Add rows to the panel
		select_project_panel.add(top_select_area);
		select_project_panel.add(Box.createRigidArea(new Dimension(0,5)));
		select_project_panel.add(bottom_select_area);

		// Add the panel to this dialog box and format
		this.add(select_project_panel);
		this.setModal(true);
		this.setResizable(false);
		this.setTitle("Download Batch");
		
		//this.setPreferredSize(new Dimension(300,70));
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
	}
	
	
	private ActionListener download_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int project_id = getSelectedProjectId();
			batch_state.pushDownloadBatch(project_id);
			dialog.dispose();
		}
	};
	
	private ActionListener cancel_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			dialog.dispose();
		}
	};
	
	private ActionListener view_sample_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			ClientCommunicator cc = batch_state.getClientCommunicator();
			User user = batch_state.getUser();
			String username = user.getUsername();
			String password = user.getPassword();
			int project_id = getSelectedProjectId();
			String project_title = String.valueOf(project_combobox.getSelectedItem());

			GetSampleImage_Params params = new GetSampleImage_Params(username, password, project_id);
			GetSampleImage_Result result = null;
			String url = null;
			DownloadFile_Result downloaded_file = null;
			BufferedImage image = null;
			
			try {
				result = cc.getSampleImage(params);
				url = result.getImage_url();
				downloaded_file = cc.downloadFile(url);
				image = ImageIO.read(new ByteArrayInputStream(downloaded_file.getFile_download()));
			} catch (ClientException e1) {
				JOptionPane.showMessageDialog(dialog, "Connection Error", 
						  "Sample Image Failed", JOptionPane.ERROR_MESSAGE);
				return;
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(dialog, "Error Reading File", 
						  "Sample Image Failed", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Create Sample Image viewbox
			Image resized_image = image.getScaledInstance(515, 430, Image.SCALE_FAST);
			JLabel image_label = new JLabel(new ImageIcon(resized_image));
			sample_image_dialog.setPreferredSize(new Dimension(515,430));
			sample_image_dialog.setLayout(new BorderLayout());
			sample_image_dialog.setModal(true);
			sample_image_dialog.setResizable(false);
			sample_image_dialog.setLocationRelativeTo(null);
			sample_image_dialog.setTitle("Sample image from " + project_title);
			sample_image_dialog.add(image_label, BorderLayout.CENTER);
			sample_image_dialog.add(close_button, BorderLayout.SOUTH);
			sample_image_dialog.pack();
			sample_image_dialog.setVisible(true);
		}
	};
	
	private ActionListener close_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			DownloadBatchWindow.this.sample_image_dialog.dispose();
		}
	};
	
	private int getSelectedProjectId() {
		String selection = String.valueOf(project_combobox.getSelectedItem());
		int project_id = 0;
		
		List<Project> projects = batch_state.getProjects();
		for (Project p : projects) {
			if (p.getProject_title().equals(selection)) {
				project_id = p.getProject_id();
			}
		}
		return project_id;
	}

}
