package server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import server.database.DatabaseException;
import server.facade.ServerFacade;
import shared.communication.GetFields_Params;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetFieldsHandler implements HttpHandler {
	private Logger logger = Logger.getLogger("record_server"); 
	private int port;
	
	public GetFieldsHandler(int port) {
		this.port = port;
	}
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerFacade facade = new ServerFacade(port);
		XStream xml_stream = new XStream(new DomDriver());
		BufferedInputStream bis = new BufferedInputStream(exchange.getRequestBody());
		GetFields_Params params = (GetFields_Params)xml_stream.fromXML(bis);
		bis.close();
		Object result = null;
		
		try {
			result = (Object)facade.getFields(params);
			exchange.sendResponseHeaders(200, 0);

			OutputStream os = exchange.getResponseBody();

			xml_stream.toXML(result, os);		
			
			os.close();
			
		} catch (DatabaseException e) {
			
			logger.severe("Exception in GetFieldsHandler");
			throw new IOException(e.getMessage());
		}
	}
	
}
