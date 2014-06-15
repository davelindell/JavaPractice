package client.qualitychecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QualityChecker {	
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
		if (word.equals(""))
			return true;
		
		else if (dictionary == null) {
			return true;
		}
		
		else {
			dictionary = dictionary.toUpperCase();
			word = word.toUpperCase();
			List<String> values = Arrays.asList(dictionary.split(","));
			return values.contains(word);
		}
	}
}

