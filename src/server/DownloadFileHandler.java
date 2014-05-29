package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import server.facade.ServerFacade;
import shared.communication.DownloadFile_Params;
import shared.communication.DownloadFile_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DownloadFileHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("contactmanager"); 
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerFacade facade = new ServerFacade();
		
		XStream xmlStream = new XStream(new DomDriver());
		BufferedInputStream bis = new BufferedInputStream(exchange.getRequestBody());
		DownloadFile_Params params = (DownloadFile_Params)xmlStream.fromXML(bis);
		DownloadFile_Result result = null;
		
		result = facade.downloadFile(params);
		xmlStream.toXML(result, new BufferedOutputStream(exchange.getResponseBody()));

		logger.severe("Exception in DownloadFileHandler");		
	}
}
