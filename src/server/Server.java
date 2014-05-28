package server;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;


public class Server {

	private static final int SERVER_PORT_NUMBER = 8080;
	private static final int MAX_WAITING_CONNECTIONS = 10;

	
	private HttpServer server;
	
	private Server() {
		return;
	}
	
	private void run() {
		
		logger.info("Initializing Model");
		
		try {
			Model.initialize();		
		}
		catch (ModelException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
		
		logger.info("Initializing HTTP Server");
		
		try {
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER),
											MAX_WAITING_CONNECTIONS);
		} 
		catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);			
			return;
		}

		server.setExecutor(null); // use the default executor
		
		server.createContext("/GetAllContacts", getAllContactsHandler);
		server.createContext("/AddContact", addContactHandler);
		server.createContext("/UpdateContact", updateContactHandler);
		server.createContext("/DeleteContact", deleteContactHandler);
		
		logger.info("Starting HTTP Server");

		server.start();
	}

	private HttpHandler getAllContactsHandler = new GetAllContactsHandler();
	private HttpHandler addContactHandler = new AddContactHandler();
	private HttpHandler updateContactHandler = new UpdateContactHandler();
	private HttpHandler deleteContactHandler = new DeleteContactHandler();
	
	public static void main(String[] args) {
		new Server().run();
	}

}
