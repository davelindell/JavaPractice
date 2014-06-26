import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;


public class ImageEditor {

	private Pixel[][] pixel_array;
	private static String srcFile;
	private static String dstFile;
	private static int width;
	private static int length;
	private static int max_value;
	
	public ImageEditor(){
		width = 0;
		length = 0;
		max_value = 255;
	}
	
	public void grayscaleImage() {
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < width; ++j){
				int avg_value = (pixel_array[i][j].getRed() + pixel_array[i][j].getBlue() + pixel_array[i][j].getGreen())/3;
				pixel_array[i][j].setRed(avg_value);
				pixel_array[i][j].setBlue(avg_value);
				pixel_array[i][j].setGreen(avg_value);
			}
		}
	}
	
	public void invertImage() {
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < width; ++j){
				pixel_array[i][j].setRed(max_value - pixel_array[i][j].getRed());
				pixel_array[i][j].setBlue(max_value - pixel_array[i][j].getBlue());
				pixel_array[i][j].setGreen(max_value - pixel_array[i][j].getGreen());
			}
		}
	}
	
	public void embossImage() {
		Pixel[][] emboss_pixel_array = new Pixel[length][width];
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < width; ++j){
				emboss_pixel_array[i][j] = new Pixel();
			}
		}
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < width; ++j){
				emboss_pixel_array[i][j].setRed(pixel_array[i][j].getRed()); 
				emboss_pixel_array[i][j].setGreen(pixel_array[i][j].getGreen()); 
				emboss_pixel_array[i][j].setBlue(pixel_array[i][j].getBlue()); 
			}
		}
		
		
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < width; ++j){
				int redDiff = 0;
				int greenDiff = 0;
				int blueDiff = 0;
				int maxDiff = 0;
				int v = 0;
				if (i == 0 || j == 0){
					v = 128;
				}
				else {
					redDiff = emboss_pixel_array[i][j].getRed() - emboss_pixel_array[i-1][j-1].getRed();
					greenDiff = emboss_pixel_array[i][j].getGreen() - emboss_pixel_array[i-1][j-1].getGreen();
					blueDiff = emboss_pixel_array[i][j].getBlue() - emboss_pixel_array[i-1][j-1].getBlue();
					if (Math.abs(redDiff) >= Math.abs(greenDiff) && Math.abs(redDiff) >= Math.abs(blueDiff))
						maxDiff = redDiff;
					else if (Math.abs(greenDiff) > Math.abs(redDiff) && Math.abs(greenDiff) >= Math.abs(blueDiff))
						maxDiff = greenDiff;
					else if (Math.abs(blueDiff) > Math.abs(redDiff) && Math.abs(blueDiff) > Math.abs(greenDiff))
						maxDiff = blueDiff;
					v = 128 + maxDiff;
				}

				if (v < 0)
					v = 0;
				else if (v > 255)
					v = 255;
				pixel_array[i][j].setRed(v);
				pixel_array[i][j].setGreen(v);
				pixel_array[i][j].setBlue(v);
			}
		}
	}
	
	public void motionblurImage(int blur_length){
		Pixel[][] blur_pixel_array = new Pixel[length][width];
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < width; ++j){
				blur_pixel_array[i][j] = new Pixel();
			}
		}
		
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < width; ++j){
				blur_pixel_array[i][j].setRed(pixel_array[i][j].getRed()); 
				blur_pixel_array[i][j].setBlue(pixel_array[i][j].getBlue()); 
				blur_pixel_array[i][j].setGreen(pixel_array[i][j].getGreen()); 
			}
		}
		
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < width; ++j){
				int redAvg = 0;
				int blueAvg = 0;
				int greenAvg = 0;
				int num_iter = 0;
				if ((j + blur_length) > width){
					for (int k = j; k < width; ++k){
						redAvg += blur_pixel_array[i][k].getRed();
						blueAvg += blur_pixel_array[i][k].getBlue();
						greenAvg += blur_pixel_array[i][k].getGreen();
						num_iter++;
					}
				}
				else {
					for (int k = j; k < j + blur_length; ++k){
						redAvg += blur_pixel_array[i][k].getRed();
						blueAvg += blur_pixel_array[i][k].getBlue();
						greenAvg += blur_pixel_array[i][k].getGreen();
						num_iter++;
					}
				}
				redAvg = redAvg/num_iter;
				blueAvg = blueAvg/num_iter;
				greenAvg = greenAvg/num_iter;
				pixel_array[i][j].setRed(redAvg);
				pixel_array[i][j].setBlue(blueAvg);
				pixel_array[i][j].setGreen(greenAvg);
			}
		}
	}
	
	public void writeImage(String dstFile) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(dstFile);
		writer.println("P3");
		writer.println(width);
		writer.println(length);
		writer.println(max_value);
		
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < width; ++j){
				writer.println(pixel_array[i][j].getRed());
				writer.println(pixel_array[i][j].getGreen());
				writer.println(pixel_array[i][j].getBlue());
			}
		}
		
		writer.close();
		return;
	}
	
	public void parseImage() throws FileNotFoundException {
		//String input_text = new Scanner(new File(srcFile)).useDelimiter("\\A").next();
		StringBuilder read_text = new StringBuilder(512);
		Scanner scanner = new Scanner((new File(srcFile)));
		
		while (scanner.hasNextLine()) {
			read_text.append(scanner.nextLine() + "\n");
		}
		String input_text = read_text.toString();
		scanner.close();
		String filtered_text = input_text.replaceAll("#.*\n", "\n");
		scanner = new Scanner(filtered_text);
		//System.out.println(filtered_text);
		
		// throw away first line
		if (scanner.hasNext())
			scanner.next();
		// read in the width/length
		if (scanner.hasNext())
			width = Integer.parseInt(scanner.next());
		if (scanner.hasNext())
			length = Integer.parseInt(scanner.next());
		if (scanner.hasNext())
			max_value = Integer.parseInt(scanner.next());
		
		
		pixel_array = new Pixel[length][width];
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < width; ++j){
				pixel_array[i][j] = new Pixel();
			}
		}
		
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < width; ++j){
				pixel_array[i][j].setRed(Integer.parseInt(scanner.next()));
				pixel_array[i][j].setGreen(Integer.parseInt(scanner.next()));
				pixel_array[i][j].setBlue(Integer.parseInt(scanner.next()));
			}
		}
		
		// print out pixel array
		//System.out.println(filtered_text);
		
		/*
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < width; ++j){
				System.out.printf("%d\n%d\n%d\n", pixel_array[i][j].getRed(), pixel_array[i][j].getBlue(), 
						pixel_array[i][j].getGreen());
			
			}
		}
		*/
		scanner.close();
		return;
	}
	
	public static void main(String[] args) {
		ImageEditor image = new ImageEditor();
		boolean cmd_line_fail = true;
		String img_operation = null;
		int blur_length = 0;
		
		if (args.length == 3) {
			if (args[2].equals("grayscale") || args[2].equals("invert") || args[2].equals("emboss")){
				img_operation = args[2];
				cmd_line_fail = false;
			}
		}
		else if (args.length == 4) {
			if (args[2].equals("motionblur")) {
				if (Integer.parseInt(args[3]) >= 0) {
					cmd_line_fail = false;
					img_operation = args[2];
					blur_length = Integer.parseInt(args[3]);
				}
			}
		}

		if(cmd_line_fail) {
			System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motion blur motion-blur-length");
			return;
		}

		image.srcFile = args[0];
		image.dstFile = args[1];
		
		try {
			image.parseImage();
		} catch (FileNotFoundException e) {
			System.out.println("Error reading file!");
		}
		
		if (img_operation.equals("grayscale")){
			image.grayscaleImage();
		}
		else if (img_operation.equals("invert")) {
			image.invertImage();
		}
		else if (img_operation.equals("emboss")) {
			image.embossImage();
		}
		else if (img_operation.equals("motionblur")){
			image.motionblurImage(blur_length);
		}
		
		
		
		try {
			image.writeImage(dstFile);
		} catch (FileNotFoundException e) {
			System.out.println("Error writing file!");
		}
		
		return;
	}
}
