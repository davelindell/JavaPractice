package server;

import java.io.*;
import java.net.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import server.facade.ServerFacade;
import server.facade.ServerFacadeException;

import com.sun.net.httpserver.*;


public class Server {

	private int server_port_number;
	private static final int MAX_WAITING_CONNECTIONS = 10;
	
	private static Logger logger;
	
	static {
		try {
			initLog();
		}
		catch (IOException e) {
			System.out.println("Could not initialize log: " + e.getMessage());
		}
	}
	
	private static void initLog() throws IOException {
		
		Level logLevel = Level.FINE;
		
		logger = Logger.getLogger("record_server"); 
		logger.setLevel(logLevel);
		logger.setUseParentHandlers(false);
		
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);

		FileHandler fileHandler = new FileHandler("log.txt", false);
		fileHandler.setLevel(logLevel);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
	}

	private HttpServer server;
	
	private Server(int server_port_number) {
		this.server_port_number = server_port_number;
		return;
	}
	
	private void run() {
		
		logger.info("Initializing Model");
		
		try {
			ServerFacade.initialize();		
		}
		catch (ServerFacadeException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
		
		logger.info("Initializing HTTP Server");
		
		try {
			server = HttpServer.create(new InetSocketAddress(server_port_number),
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
		new Server(Integer.parseInt(args[0])).run();
	}

}
