package server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import server.database.DatabaseException;
import server.facade.ServerFacade;
import shared.communication.Search_Params;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SearchHandler implements HttpHandler {
	private int port;
	private Logger logger = Logger.getLogger("record_server"); 
	
	public SearchHandler(int port) {
		super();
		this.port = port;
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerFacade facade = new ServerFacade(port);
		XStream xml_stream = new XStream(new DomDriver());
		BufferedInputStream bis = new BufferedInputStream(exchange.getRequestBody());
		Search_Params params = (Search_Params)xml_stream.fromXML(bis);
		bis.close();
		Object result = null;
		
		try {
			result = (Object)facade.search(params);
			exchange.sendResponseHeaders(200, 0);

			OutputStream os = exchange.getResponseBody();

			xml_stream.toXML(result, os);		
			
			os.close();
			
		} catch (DatabaseException e) {
			
			logger.severe("Exception in SearchHandler");
			throw new IOException(e.getMessage());
		}
	}
	
}
