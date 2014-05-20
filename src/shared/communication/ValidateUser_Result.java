package shared.communication;

import shared.models.User;

/** 
 * ValidateUser_Result is a wrapper class used by the communicators
 * to store a User object and a valid flag
 * @author lindell
 */
public class ValidateUser_Result {
	private User user;
	private boolean valid;

	/**
	 * @param user User object stored in this wrapper class
	 * @param valid flag to signify that this object contains a valid user
	 */
	public ValidateUser_Result(User user, boolean valid) {
		this.user = user;
		this.valid = valid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

}
