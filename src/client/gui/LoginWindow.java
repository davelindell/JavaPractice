package client.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import shared.communication.ValidateUser_Params;
import shared.communication.ValidateUser_Result;
import client.ClientException;
import client.communication.ClientCommunicator;

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
	
	public LoginWindow(String hostname, String port) {
		this.hostname = hostname;
		this.port = port;
		cc = new ClientCommunicator(hostname, port);

		createComponents();
	}
	
	private void createComponents() {		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
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
				else {}
					//TODO: get number of batches that user has indexed
			} 
			catch (ClientException e1) {
				loginError();
			}
			
		}
	};

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
	
	
}
