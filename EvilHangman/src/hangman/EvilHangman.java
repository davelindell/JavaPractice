package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class EvilHangman implements EvilHangmanGame {
	private Set<String> words;
	private Set<String> evil_words;
	private int wordLength;
	private Set<Character> guessed_chars;
	//public String cur_pattern;
	
	public EvilHangman() {
		words = new HashSet<String>();
		evil_words = new HashSet<String>();
		wordLength = 0;
		guessed_chars = new HashSet<Character>();
		//cur_pattern = null;
	}
	
	@Override
	public void startGame(File dictionary, int wordLength) {
		Scanner scanner = null;
		this.wordLength = wordLength;
		int j = 0;
		try {
			scanner = new Scanner(dictionary);
			while (scanner.hasNext()) {
				j++;
				String cur_word = scanner.next();
				if (cur_word.length() == wordLength)
					words.add(cur_word.toLowerCase());
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		finally {
			scanner.close();
		}
		evil_words.addAll(words);
	}
	
	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		guess = Character.toLowerCase(guess);
		if (guessed_chars.contains(guess)) {
			GuessAlreadyMadeException exception = new GuessAlreadyMadeException();
			throw exception;
		}
		guessed_chars.add(guess);
		Map<String, Set<String>> guess_words = getPatternWords(guess);
		//Collection<Set<String>> str_sets = guess_words.values();
		Set<Map.Entry<String,Set<String>>> str_sets = guess_words.entrySet();
		Iterator<Map.Entry<String,Set<String>>> iter = str_sets.iterator();
		int max_size = 0;
		Set<String> largest_strset = null;
		String largest_key = null;
		
		while (iter.hasNext()) {
			Map.Entry<String,Set<String>> cur_set = iter.next();
			if (max_size == 0) {
				largest_strset = cur_set.getValue();
				largest_key = cur_set.getKey();
				max_size = cur_set.getValue().size();
				evil_words = cur_set.getValue();
			}
			else if (cur_set.getValue().size() > max_size) {
				largest_strset = cur_set.getValue();
				largest_key = cur_set.getKey();
				max_size = cur_set.getValue().size();
				evil_words = cur_set.getValue();
			}
			else if (cur_set.getValue().size() == max_size) {
				if (!cur_set.getKey().contains(Character.toString(guess)) 
						&& largest_key.contains(Character.toString(guess))) {
						largest_strset = cur_set.getValue();
						largest_key = cur_set.getKey();
						max_size = cur_set.getValue().size();
						evil_words = cur_set.getValue();
				}
				else if (cur_set.getKey().contains(Character.toString(guess)) 
						&& largest_key.contains(Character.toString(guess))) {
					ArrayList<Integer> index1 = new ArrayList<Integer>();
					ArrayList<Integer> index2 = new ArrayList<Integer>();
					for (int i = 0; i < cur_set.getKey().length(); i++) {
						if (cur_set.getKey().charAt(i) == guess) {
							index1.add(i);
						}
					}
					for (int i = 0; i < largest_key.length(); i++) {
						if (largest_key.charAt(i) == guess) {
							index2.add(i);
						}
					}
					if (index1.size() < index2.size()) {
						largest_strset = cur_set.getValue();
						largest_key = cur_set.getKey();
						max_size = cur_set.getValue().size();
						evil_words = cur_set.getValue();
					}
					else if (index1.size() == index2.size()) {
						int max1 = Collections.max(index1);
						int max2 = Collections.max(index2);
						if(max1 > max2) {
							largest_strset = cur_set.getValue();
							largest_key = cur_set.getKey();
							max_size = cur_set.getValue().size();
							evil_words = cur_set.getValue();
						}
						else if (max1 == max2){
							boolean resolved = false;
							while(!resolved) {
								Integer maxint1 = new Integer(max1);
								Integer maxint2 = new Integer(max2);
								index1.remove(maxint1);
								index2.remove(maxint2);
								if (index1.size() == 0 || index2.size() == 0)
									resolved = true;
								else {
									max1 = Collections.max(index1);
									max2 = Collections.max(index2);
									if(max1 > max2) {
										largest_strset = cur_set.getValue();
										largest_key = cur_set.getKey();
										max_size = cur_set.getValue().size();
										evil_words = cur_set.getValue();
										resolved = true;
									}
								}
							}
						}
					}
				}
			}
		}
			
		
		//cur_pattern = largest_key;
		
		/*
		for (Set<String> str_set : str_sets) {
			if (str_set.size() > max_size) {
				largest_strset = str_set;
			}
		}
		*/
		
		return largest_strset;
	}
	
	private Map<String, Set<String>> getPatternWords(char guess) {
		Map<String, Set<String>> guess_words = new HashMap<String, Set<String>>();
		for (String word : evil_words) {
			ArrayList<Integer> char_locations = new ArrayList<Integer>();
			StringBuilder pattern = new StringBuilder();
			for (int i = 0; i < word.length(); i++) {
				if (word.charAt(i) == guess) {
					char_locations.add(i);
				}
			}
			for (int i = 0; i < wordLength; i++) {
				pattern.append('_');
			}
			for (Integer index : char_locations) {
				pattern.replace(index.intValue(), index.intValue() + 1, Character.toString(guess));
			}
			String str_pattern = pattern.toString();
			if (guess_words.containsKey(str_pattern)) {
				Set<String> str_set = guess_words.get(str_pattern);
				str_set.add(word);
			}
			else {
				Set<String> str_set = new HashSet<String>();
				str_set.add(word);
				guess_words.put(str_pattern, str_set);
			}
		}
		
		return guess_words;
	}
	

}














