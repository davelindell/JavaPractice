package client.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import shared.communication.ValidateUser_Params;
import shared.communication.ValidateUser_Result;
import shared.models.User;
import client.ClientException;
import client.communication.ClientCommunicator;
import client.synchronizer.BatchState;

public class LoginWindow extends JFrame {
	private ClientCommunicator cc;
	private JButton login_button;
	private JButton exit_button;
	private JLabel username_label;
	private JLabel password_label;
	private JTextField username_field;
	private JPasswordField password_field;
	private String hostname;
	private String port;
	private LoginWindowListener login_listener;
	private BatchState batch_status;
	
	public LoginWindow(String hostname, String port, BatchState batch_status) {
		this.hostname = hostname;
		this.port = port;
		cc = new ClientCommunicator(hostname, port);
		this.batch_status = batch_status;

		createComponents();
	}
	
	private void createComponents() {		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocationRelativeTo(null);
		//this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setTitle("Login to Indexer");
		this.setPreferredSize(new Dimension(370, 100));
		this.setResizable(false);
		this.setLayout(null);
		this.setExtendedState(NORMAL);
		
		username_label = new JLabel("Username:");
		username_label.setBounds(10, 10, 80, 25);
		this.add(username_label);

		username_field = new JTextField(40);
		username_field.setBounds(80, 10, 280, 20);
		this.add(username_field);

		password_label = new JLabel("Password:");
		password_label.setBounds(10, 40, 80, 25);
		this.add(password_label);

		password_field = new JPasswordField(40);
		password_field.setBounds(80, 40, 280, 20);
		this.add(password_field);

		login_button = new JButton("Login");
		login_button.setBounds(120, 70, 70, 25);
		login_button.addActionListener(login_button_listener);
		this.add(login_button);
		
		exit_button = new JButton("Exit");
		exit_button.setBounds(195, 70, 70, 25);
		exit_button.addActionListener(exit_button_listener);
		this.add(exit_button);
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
		int num_records = user.getNum_records();
		String message_str = "Welcome " + user.getUser_first_name() + 
							 " " + user.getUser_last_name() + ".\n" + 
							 "You have indexed " + 
							 Integer.toString(user.getNum_records()) + " records.";

		JOptionPane.showMessageDialog(this, message_str, "Welcome to Indexer", JOptionPane.PLAIN_MESSAGE);
		login_listener.loginSuccessful();
		batch_status.setUser(user);
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

