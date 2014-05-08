package listem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple main class for running grep. This class is not used by the 
 * passoff program.
 */
public class RunGrep extends TextOperation implements Grep {
	
	/**
	 * If the first argument is -r, the search should be recursive.
	 * 
	 * The next arguments are the base directory, file selection pattern,
	 * and search pattern.
	 */
	public static void main(String[] args) {		
		String dirName = "";
		String filePattern = "";
		String searchPattern = "";
		boolean recursive = false;
		
		if (args.length == 3) {
			recursive = false;
			dirName = args[0];
			filePattern = args[1];
			searchPattern = args[2];
		} else if (args.length == 4 && args[0].equals("-r")) {
			recursive = true;
			dirName = args[1];
			filePattern = args[2];
			searchPattern = args[3];
		} else {
			System.out.println("USAGE: java listem.RunGrep [-r] <dir> <file-pattern> <search-pattern>");
			return;
		}
		

		Grep grep = new RunGrep();
		
		Map<File, List<String>> result = grep.grep(new File(dirName), filePattern, searchPattern, recursive);
		
		outputGrepResult(result);
	}

	/**
	 * Prints a formatted version of the map returned from grep.
	 */
	public static void outputGrepResult(Map<File, List<String>> result) {
		int totalMatches = 0;
		for (Map.Entry<File, List<String>> singleFileResult : result.entrySet()) {
			System.out.println("FILE: " + singleFileResult.getKey().getPath());
			
			List<String> lineMatches = singleFileResult.getValue();
			for (String lineMatch : lineMatches) {
				System.out.println(lineMatch);				
			}
			
			int matches = lineMatches.size();
			System.out.println("MATCHES: " + matches);
			totalMatches += matches;
			
			System.out.println();
		}
		
		System.out.println("TOTAL MATCHES: " + totalMatches);
	}

	@Override
	public Map<File, List<String>> grep(File directory,
			String fileSelectionPattern, String substringSelectionPattern,
			boolean recursive) {
		
		Map<File, List<String>> file_map = new HashMap<File, List<String>>();
		ArrayList<File> files = listFiles(directory, fileSelectionPattern, recursive);
		Iterator<File> iter = files.iterator();
		
		while(iter.hasNext()){
			List<String> this_list = new ArrayList<String>();
			File this_file = iter.next();
			Pattern p = Pattern.compile(substringSelectionPattern); 
			Scanner scanner;
			try {
				scanner = new Scanner(this_file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}

			while(scanner.hasNextLine()){
				String this_line = scanner.nextLine();
				Matcher m = p.matcher(this_line);
				if(m.find()) {
					this_list.add(this_line);
				}
			}
			if (this_list.size() > 0)
				file_map.put(this_file, this_list);
			scanner.close();
		}
		
		return file_map;
	}
	

	
	

}