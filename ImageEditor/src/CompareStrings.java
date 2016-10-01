import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class CompareStrings {
	private String srcFile1;
	private String srcFile2;
	
	public CompareStrings(){
		srcFile1 = null;
		srcFile2 = null;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		CompareStrings comparestrings = new CompareStrings();
		comparestrings.srcFile1 = args[0];
		comparestrings.srcFile2 = args[1];
		
		StringBuilder read_text1 = new StringBuilder(512);
		Scanner scanner;

		scanner = new Scanner((new File(comparestrings.srcFile1)));
		while (scanner.hasNext()) {
			read_text1.append(scanner.next() + "\n");
		}
		String input_text1 = read_text1.toString();
		scanner.close();
		String filtered_text1 = input_text1.replaceAll("#.*\n", "\n");
		
		
		StringBuilder read_text2 = new StringBuilder(512);
		scanner = new Scanner((new File(comparestrings.srcFile2)));
		while (scanner.hasNext()) {
			read_text2.append(scanner.next() + "\n");
		}
		String input_text2 = read_text2.toString();
		scanner.close();
		String filtered_text2 = input_text2.replaceAll("#.*\n", "\n");
		
		Scanner scanner1 = new Scanner(filtered_text1);
		Scanner scanner2 = new Scanner(filtered_text2);
		int line_num = 0;
		if (!filtered_text1.equals(filtered_text2))
			System.out.println("ERROR!");
		/*
		while(scanner1.hasNext()){
			if (!scanner1.next().equals(scanner2.next()))
				System.out.println("ERROR AT " + Integer.toString(line_num));
			line_num++;
		}
		*/
		scanner1.close();
		scanner2.close();
		
		
		return;
	}
}
