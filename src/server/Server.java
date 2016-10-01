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
		
		Level logLevel = Level.INFO;
		
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
			
		validateUserHandler = new ValidateUserHandler();
		getProjectsHandler = new GetProjectsHandler();
		getSampleImageHandler = new GetSampleImageHandler();
		downloadBatchHandler = new DownloadBatchHandler(this.server.getAddress().getPort());
		submitBatchHandler = new SubmitBatchHandler();
		getFieldsHandler = new GetFieldsHandler(this.server.getAddress().getPort());
		searchHandler = new SearchHandler(this.server.getAddress().getPort());
		downloadFileHandler = new DownloadFileHandler();
		
		server.createContext("/ValidateUser", validateUserHandler);
		server.createContext("/GetProjects", getProjectsHandler);
		server.createContext("/GetSampleImage", getSampleImageHandler);
		server.createContext("/DownloadBatch", downloadBatchHandler);
		server.createContext("/SubmitBatch", submitBatchHandler);
		server.createContext("/GetFields", getFieldsHandler);
		server.createContext("/Search", searchHandler);
		server.createContext("/records", downloadFileHandler);

		logger.info("Starting HTTP Server");

		server.start();
	}

	private HttpHandler validateUserHandler;
	private HttpHandler getProjectsHandler;
	private HttpHandler getSampleImageHandler;
	private HttpHandler downloadBatchHandler;
	private HttpHandler submitBatchHandler;
	private HttpHandler getFieldsHandler;
	private HttpHandler searchHandler;
	private HttpHandler downloadFileHandler;
	
	public static void main(String[] args) {
		new Server(Integer.parseInt(args[0])).run();
	}

}
