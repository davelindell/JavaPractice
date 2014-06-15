package client.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import shared.communication.GetProjects_Params;
import shared.communication.GetProjects_Result;
import shared.communication.ValidateUser_Params;
import shared.communication.ValidateUser_Result;
import shared.models.User;
import client.ClientException;
import client.communication.ClientCommunicator;
import client.synchronizer.BatchState;

@SuppressWarnings("serial")
public class LoginWindow extends JFrame {
	private ClientCommunicator cc;
	private JButton login_button;
	private JButton exit_button;
	private JLabel username_label;
	private JLabel password_label;
	private JTextField username_field;
	private JPasswordField password_field;
	private LoginWindowListener login_listener;
	private BatchState batch_state;
	
	public LoginWindow(String hostname, String port, BatchState batch_state) {
		cc = new ClientCommunicator(hostname, port);
		this.batch_state = batch_state;

		createComponents();
	}
	
	private void createComponents() {				
		// Create JPanels
		JPanel login_window_panel = new JPanel();
		login_window_panel.setLayout(new BoxLayout(login_window_panel, BoxLayout.PAGE_AXIS));
		
		JPanel top_row = new JPanel();
		top_row.setLayout(new BoxLayout(top_row, BoxLayout.LINE_AXIS));
		
		JPanel middle_row = new JPanel();
		middle_row.setLayout(new BoxLayout(middle_row, BoxLayout.LINE_AXIS));
		
		JPanel bottom_row = new JPanel();
		bottom_row.setLayout(new BoxLayout(bottom_row, BoxLayout.LINE_AXIS));
		
		username_label = new JLabel("Username:");
		JPanel username_field_panel = new JPanel();
		username_field = new JTextField(20);
		
		password_label = new JLabel("Password: ");
		JPanel password_field_panel = new JPanel();
		password_field = new JPasswordField(20);
		password_field.setSize(new Dimension(100,10));
		
		login_button = new JButton("Login");
		login_button.addActionListener(login_button_listener);
		
		exit_button = new JButton("Exit");
		exit_button.addActionListener(exit_button_listener);
		
		username_field_panel.add(username_field);
		top_row.add(username_label);
		top_row.add(username_field_panel);
		
		password_field_panel.add(password_field);
		middle_row.add(password_label);
		middle_row.add(password_field_panel);
		
		bottom_row.add(login_button);
		bottom_row.add(Box.createRigidArea(new Dimension(10,0)));
		bottom_row.add(exit_button);

		login_window_panel.add(top_row);
		login_window_panel.add(middle_row);
		login_window_panel.add(bottom_row);
		
		login_window_panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		this.add(login_window_panel);
		this.setLocationRelativeTo(null);
		this.setTitle("Login to Indexer");
		this.setPreferredSize(new Dimension(320, 100));
		this.setResizable(false);
		this.setExtendedState(NORMAL);
	}
	
	private ActionListener login_button_listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String username = username_field.getText();
			String password = new String(password_field.getPassword());
			ValidateUser_Params params = new ValidateUser_Params(username, password);
			ValidateUser_Result result = null;
			try {
				result = cc.validateUser(params);
				if(!result.isValid())
					loginError();
				else {
					loginSuccess(result.getUser());
					username_field.setText("");
					password_field.setText("");
				}
			} 
			catch (ClientException e1) {
				loginError();
			}	
		}
	};

	private void loginSuccess(User user) {
		String message_str = "Welcome " + user.getUser_first_name() + 
							 " " + user.getUser_last_name() + ".\n" + 
							 "You have indexed " + 
							 Integer.toString(user.getNum_records()) + " records.";

		JOptionPane.showMessageDialog(this, message_str, "Welcome to Indexer", JOptionPane.PLAIN_MESSAGE);
		login_listener.loginSuccessful();
		batch_state.setUser(user);
		
		GetProjects_Params proj_params = 
				new GetProjects_Params(user.getUsername(),user.getPassword());
		
		// load prior state if there is one
		batch_state.pushLoad();
		
		GetProjects_Result proj_result;
		try {
			proj_result = cc.getProjects(proj_params);
			batch_state.setProjects(proj_result.getProject_info());
		} catch (ClientException e) {
			JOptionPane.showMessageDialog(this, "Error loading data", 
					  "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void loginError() {
		JOptionPane.showMessageDialog(this, "Invalid username and/or password", 
									  "Login Failed", JOptionPane.ERROR_MESSAGE);
	}
	
	private ActionListener exit_button_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};
	
	public void addListener(LoginWindowListener login_listener) {
		this.login_listener = login_listener;
	}

}

interface LoginWindowListener {	
	public void loginSuccessful();	
}

