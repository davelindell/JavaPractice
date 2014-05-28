package importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;

public class DataImporter {

	public static void main(String[] args) throws Exception {
		File src = new File("database/empty_record_indexer.sqlite");
		File dst = new File("database/record_indexer.sqlite");
		Files.copy(	src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		File file = new File(args[0]);
		Document doc = builder.parse(file);
		
		NodeList cdList = doc.getElementsByTagName("CD");
		for (int i = 0; i < cdList.getLength(); ++i) {
			
			Element cdElem = (Element)cdList.item(i);
			
			Element titleElem = (Element)cdElem.getElementsByTagName("TITLE").item(0);
			Element artistElem = (Element)cdElem.getElementsByTagName("ARTIST").item(0);
			
			String title = titleElem.getTextContent();
			String artist = artistElem.getTextContent();
			
			System.out.println(title + ", " + artist);
		}	
	
		
	}

	
	
}
