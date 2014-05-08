package listem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextOperation {
	
	public TextOperation(){
		
	}
	
	public ArrayList<File> listFiles(File directory, String fileSelectionPattern, boolean recursive) {
		ArrayList<File> files = new ArrayList<File>();
		Pattern str_pattern = Pattern.compile(fileSelectionPattern);
		Matcher str_matcher;
		if(!recursive) {
			File[] new_files = directory.listFiles();
			for (int i = 0; i < new_files.length; i++) {
				str_matcher = str_pattern.matcher(new_files[i].getName());
				if (str_matcher.matches() && new_files[i].isFile()) {
					files.add(new_files[i]);
				}
			}
		}
		else {
			recursiveListFiles(directory, str_pattern, files);
		}
		
		//Iterator<File> iter = files.iterator();
		//System.out.println(directory.listFiles());
		/*
		while(iter.hasNext()){
			File this_file = iter.next();
			//System.out.println(this_file.getPath());
		}
		*/
		return files;
	}
	
	public ArrayList<File> recursiveListFiles(File directory, Pattern str_pattern, ArrayList<File> files) {
		if (directory.isFile()) {
			Matcher str_matcher = str_pattern.matcher(directory.getName());
			if (str_matcher.matches()) {
				files.add(directory);
				//System.out.println(directory.getPath());
				return files;
			}
		}
		File[] these_files = directory.listFiles();
		if (directory.listFiles() != null) {
			for (int i = 0; i < these_files.length; i++) {
				recursiveListFiles(these_files[i], str_pattern, files);
			}
		}
		return files;
	}
	
}
