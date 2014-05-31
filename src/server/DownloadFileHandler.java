package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import server.facade.ServerFacade;
import shared.communication.DownloadFile_Result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DownloadFileHandler implements HttpHandler {
	
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
