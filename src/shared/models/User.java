package shared.models;

/**
 * The User class contains information that describes a user profile
 * in the record indexer.
 * @author lindell
 */

public class User {
	private int user_id;
	private String user_first_name;
	private String user_last_name;
	private String username;
	private String password;
	private int num_records;
	private String email;
	
	public User(String user_first_name, String user_last_name,String username, String password, int num_records, String email) {
		this.user_id = 0; // sql auto-increment will give id
		this.user_first_name = user_first_name;
		this.user_last_name = user_last_name;
		this.username = username;
		this.password = password;
		this.num_records = num_records;
		this.email = email;
	}

	public User() {
		user_id = 0;
		user_first_name = null;
		user_last_name = null;
		username = null;
		password = null;
		num_records = 0;
		email = null;
	}
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_first_name() {
		return user_first_name;
	}

	public void setUser_first_name(String user_first_name) {
		this.user_first_name = user_first_name;
	}

	public String getUser_last_name() {
		return user_last_name;
	}

	public void setUser_last_name(String user_last_name) {
		this.user_last_name = user_last_name;
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

	public int getNum_records() {
		return num_records;
	}

	public void setNum_records(int num_records) {
		this.num_records = num_records;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
