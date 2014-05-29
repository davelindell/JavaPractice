package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import server.database.DatabaseException;
import server.facade.ServerFacade;
import shared.communication.GetProjects_Params;
import shared.communication.GetProjects_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetProjectsHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("contactmanager"); 
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerFacade facade = new ServerFacade();
		
		XStream xmlStream = new XStream(new DomDriver());
		BufferedInputStream bis = new BufferedInputStream(exchange.getRequestBody());
		GetProjects_Params params = (GetProjects_Params)xmlStream.fromXML(bis);
		GetProjects_Result result = null;
		
		try {
			result = facade.getProjects(params);
			xmlStream.toXML(result, new BufferedOutputStream(exchange.getResponseBody()));
			
		} catch (DatabaseException e) {
			logger.severe("Exception in GetProjects handler");
			throw new IOException(e.getMessage());
		}
	}
}
