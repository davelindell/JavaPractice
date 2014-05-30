package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import server.facade.ServerFacade;
import shared.communication.DownloadFile_Params;
import shared.communication.DownloadFile_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DownloadFileHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("record_server"); 
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerFacade facade = new ServerFacade();
		
		DownloadFile_Result result = null;
		URI uri = exchange.getRequestURI();	
		String url = uri.toString();
		
		result = facade.downloadFile(url.toString());
		OutputStream os = exchange.getResponseBody();
		byte[] data = result.getFile_download();

		exchange.sendResponseHeaders(200, data.length);
		
		os.write(data);
		os.close();
	}
}
