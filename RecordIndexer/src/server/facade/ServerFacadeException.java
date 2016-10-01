package server.facade;

@SuppressWarnings("serial")
public class ServerFacadeException extends Exception {

	public ServerFacadeException() {
		return;
	}

	public ServerFacadeException(String message) {
		super(message);
	}

	public ServerFacadeException(Throwable cause) {
		super(cause);
	}

	public ServerFacadeException(String message, Throwable cause) {
		super(message, cause);
	}

}
