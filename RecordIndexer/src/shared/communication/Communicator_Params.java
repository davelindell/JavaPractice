package shared.communication;

/**
 * Super class for the _Params methods that share 
 * the username and password data members.
 * @author lindell
 *
 */
public class Communicator_Params {
	private String username;
	private String password;
	
	public Communicator_Params (String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
