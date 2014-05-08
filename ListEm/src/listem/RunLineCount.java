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
 * A simple main class for running line count. This class is not used by the 
 * passoff program.
 */
public class RunLineCount extends TextOperation implements LineCounter {
	
	/**
	 * If the first argument is -r, the search should be recursive.
	 * 
	 * The next arguments are the base directory and file selection pattern
	 */
	public static void main(String[] args) {
		String dirName = "";
		String filePattern = "";
		boolean recursive = false;
		
		if (args.length == 2) {
			recursive = false;
			dirName = args[0];
			filePattern = args[1];
		} else if (args.length == 3 && args[0].equals("-r")) {
			recursive = true;
			dirName = args[1];
			filePattern = args[2];
		} else {
			System.out.println("USAGE: java listem.RunLineCount [-r] <dir> <file-pattern>");
			return;
		}

		
		LineCounter counter = new RunLineCount();
		Map<File, Integer> lineCountResult = counter.countLines(new File(dirName), filePattern, recursive);
		
		RunLineCount.outputLineCountResult(lineCountResult);
	}

	/**
	 * Prints a formatted version of the map returned from a line counter.
	 */
	public static void outputLineCountResult(Map<File, Integer> result) {
		int totalLines = 0;
		for (Map.Entry<File, Integer> singleFileResult : result.entrySet()) {
			int lineCount = singleFileResult.getValue();
			String file = singleFileResult.getKey().getPath();
			
			System.out.println(lineCount + " " + file);

			totalLines += lineCount;			
		}
		
		System.out.println("TOTAL: " + totalLines);
	}

	@Override
	public Map<File, Integer> countLines(File directory,
			String fileSelectionPattern, boolean recursive) {

		Map<File, Integer> int_map = new HashMap<File,Integer>();
		ArrayList<File> files = listFiles(directory, fileSelectionPattern, recursive);
		Iterator<File> iter = files.iterator();
		
		while(iter.hasNext()){
			int line_count = 0;
			File this_file = iter.next();
			Scanner scanner;
			try {
				scanner = new Scanner(this_file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}

			while(scanner.hasNextLine()){
				scanner.nextLine();
				line_count++;
			}
	
			int_map.put(this_file, line_count);
			scanner.close();
		}
		
		return int_map;
	}

}