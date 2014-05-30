package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import server.database.DatabaseException;
import server.facade.ServerFacade;
import shared.communication.GetSampleImage_Params;
import shared.communication.GetSampleImage_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetSampleImageHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("record_server"); 
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerFacade facade = new ServerFacade();
		
		XStream xmlStream = new XStream(new DomDriver());
		BufferedInputStream bis = new BufferedInputStream(exchange.getRequestBody());
		GetSampleImage_Params params = (GetSampleImage_Params)xmlStream.fromXML(bis);
		GetSampleImage_Result result = null;
		
		try {
			result = facade.getSampleImage(params);
			xmlStream.toXML(result, new BufferedOutputStream(exchange.getResponseBody()));
			
		} catch (DatabaseException e) {
			logger.severe("Exception in GetSampleImageHandler");
			throw new IOException(e.getMessage());
		}
	}
}
