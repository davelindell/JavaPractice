package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import server.database.DatabaseException;
import server.facade.ServerFacade;
import shared.communication.DownloadBatch_Params;
import shared.communication.DownloadBatch_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DownloadBatchHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("record_server"); 
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerFacade facade = new ServerFacade();
		
		XStream xmlStream = new XStream(new DomDriver());
		BufferedInputStream bis = new BufferedInputStream(exchange.getRequestBody());
		DownloadBatch_Params params = (DownloadBatch_Params)xmlStream.fromXML(bis);
		DownloadBatch_Result result = null;
		
		try {
			result = facade.downloadBatch(params);
			xmlStream.toXML(result, new BufferedOutputStream(exchange.getResponseBody()));

		} catch (DatabaseException e) {
			logger.severe("Exception in GetSampleImageHandler");
			throw new IOException(e.getMessage());
		}
	}
}
