package hangman;

import hangman.EvilHangmanGame.GuessAlreadyMadeException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class main {

	public static void main(String[] args) {
		
		
		
		if (args.length != 3) {
			System.out.print("Usage: java [your main class name] dictionary wordLength guesses\n" + 
				"dictionary is the path to a text file with whitespace separated words " +
				"(no numbers, punctuation, etc.\n" + 
				"wordLength is an integer ≥ 2.\n" +
				"guesses is an integer ≥ 1.\n");
			return;
		}
		EvilHangmanGame hangman = new EvilHangman();
		
		File dictionary = new File(args[0]);
		int wordLength = Integer.parseInt(args[1]);
		int guesses = Integer.parseInt(args[2]);
		ArrayList<String> guessed_chars = new ArrayList<String>();
		Set<String> evil_words = new HashSet<String>();
		
		hangman.startGame(dictionary, wordLength);
		
		StringBuilder cur_pattern = new StringBuilder();
		for (int i = 0; i < wordLength; i++) {
			cur_pattern.append('_');
		}

		
		while (guesses > 0) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("You have " + guesses + " left.");
			StringBuilder guessed_char_strbld = new StringBuilder();
			for (String guessed_char : guessed_chars) {
				guessed_char_strbld.append(guessed_char + " ");
			}
			
			String guessed_char_str = guessed_char_strbld.toString();
			System.out.println("Used letters: " + guessed_char_str);
			
			System.out.println("Word: " + cur_pattern.toString());
			
			System.out.print("Enter guess: ");
			String input = scanner.next();
			
			while (!input.matches("([a-z]|[A-Z])")) {
				System.out.println("Invalid input");
				System.out.print("Enter guess: ");
				input = scanner.next();
			}
			
			try {
				evil_words = hangman.makeGuess(input.charAt(0));
			} catch (GuessAlreadyMadeException e) {
				System.out.println("You already made that guess!");
				scanner.close();
				return;
			}
			Iterator<String> evil_iter = evil_words.iterator();
			String sample_word = evil_iter.next();
			int num_guessed = 0;
			for (int i = 0; i < sample_word.length(); i++) {
				if (sample_word.charAt(i) == input.charAt(0)) {
					cur_pattern.replace(i, i+1, input);
					num_guessed++;
				}
					
			}
			
			if (num_guessed == 0) {
				System.out.println("Sorry, there are no " + input + "'s");
				guesses--;
			}
			else if (num_guessed == 1) { 
				System.out.println("Yes, there is 1 " + input);
			}
			else {
				System.out.println("Yes, there are " + Integer.toString(num_guessed) + " " + input + "'s");
			}
			System.out.print("\n");
			if (!cur_pattern.toString().contains("_")) {
				System.out.println("You Win!");
				System.out.println("The word is: " + cur_pattern);
				scanner.close();
				return;
			}

			if (guesses == 0) {
				System.out.println("You lose!");
				System.out.println("The word was: " + sample_word);
				scanner.close();
				return;
			}
		}
	}
}
