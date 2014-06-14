package client.qualitychecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.qualitychecker.SpellCorrector.NoSimilarWordFoundException;

public class QualityChecker {
	private Map<Integer, String> dictionaries;
	
	public QualityChecker() {
		
	}

	public List<String> getSuggestions(String dictionary, String word) {
		SpellCorrector corrector = new Spell();
		
		if(dictionary == null)
			return new ArrayList<String>();
		else {
			try {
				corrector.useDictionary(dictionary);
				return corrector.suggestSimilarWord(word);
			} catch (IOException e) {
				return null;
			}
		}
	}
		
	public boolean checkWord(String dictionary, String word) {
		
		word = word.toUpperCase();
		if (word.equals(""))
			return true;
		
		else if (dictionary == null) {
			 try { 
				 Integer.parseInt(word);
				 return true;
			 } 
			 catch(NumberFormatException e) { 
				 return false; 
			 }
		}
		
		else {
			List<String> values = Arrays.asList(dictionary.split(","));
			return values.contains(word);
		}
			
		
	}

}

