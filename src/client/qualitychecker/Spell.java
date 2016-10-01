package client.qualitychecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import client.qualitychecker.Trie.Node;


public class Spell implements SpellCorrector {
	public Words dict;
	
	
	public Spell() {
		dict = new Words();
	}
	
	@Override
	public void useDictionary(String dictionary) throws IOException {
		dictionary = dictionary.replaceAll(",","\n");
		Scanner scanner = new Scanner(dictionary);
		while (scanner.hasNextLine()) {
			String cur_word = scanner.nextLine();
			cur_word = cur_word.toLowerCase();
			dict.add(cur_word);
		}
		scanner.close();
	}

	@Override
	public List<String> suggestSimilarWord(String inputWord) {		
		inputWord = inputWord.toLowerCase();
		
		Set<String> dist_words = new TreeSet<String>();
		dist_words.addAll(getDistOne(inputWord));
			
		Set<String> dist_words2 = new TreeSet<String>();
		for (String word : dist_words) {
			dist_words2.addAll(getDistOne(word));
		}
		Map<String, Integer> found_words2 = getFoundWords(dist_words2);
		if (found_words2.isEmpty())
			return new ArrayList<String>();
		else {
			List<String> result = new ArrayList<String>();
			result.addAll(found_words2.keySet());
			Collections.sort(result);	
			return result;	
		}
		
	}

	
	public Map<String, Integer> getFoundWords(Set<String> dist_words) {
		Map<String, Integer> found_words = new HashMap<String, Integer>();
		for (String word : dist_words) {
			Node cur_node = dict.find(word);
			if (cur_node != null) {
				found_words.put(word, cur_node.getValue());
			}
		}
		return found_words;
	}
	
	public Set<String> getDistOne(String input) {
		Set<String> dist_words = new TreeSet<String>();
		dist_words.addAll(getDeletion(input));
		dist_words.addAll(getTransposition(input));
		dist_words.addAll(getAlteration(input));
		dist_words.addAll(getInsertion(input));
		return dist_words;
	}
	
	public Set<String> getDeletion(String input) {
		Set<String> dist_words = new TreeSet<String>();
		for (int i = 0; i < input.length(); i++) {
			StringBuilder str = new StringBuilder(input);
			str.replace(i, i+1, "");
			dist_words.add(str.toString());
		}
		return dist_words;
	}
	
	public Set<String> getTransposition(String input) {
		Set<String> dist_words = new TreeSet<String>();
		for (int i = 0; i < input.length()-1; i++) {
			StringBuilder str = new StringBuilder(input);
			char tmp = str.charAt(i);
			str.setCharAt(i, input.charAt(i+1));
			str.setCharAt(i+1, tmp);
			dist_words.add(str.toString());
		}
		return dist_words;
	}
	
	public Set<String> getAlteration(String input) {
		Set<String> dist_words = new TreeSet<String>();
		for (int i = 0; i < input.length(); i++) {
			for (char j = 'a'; j < 'a' + 26; j++) {
				StringBuilder str = new StringBuilder(input);
				str.setCharAt(i, j);
				dist_words.add(str.toString());
			}
			StringBuilder str = new StringBuilder(input);
			str.setCharAt(i, ' ');
			dist_words.add(str.toString());
		}
		return dist_words;
	}
	
	public Set<String> getInsertion(String input) {
		Set<String> dist_words = new TreeSet<String>();
		for (int i = 0; i < input.length(); i++) {
			for (char j = 'a'; j < 'a' + 26; j++) {
				StringBuilder str = new StringBuilder(input);
				str.insert(i, j);
				dist_words.add(str.toString());
			}
			StringBuilder str = new StringBuilder(input);
			str.insert(i, ' ');
			dist_words.add(str.toString());
		}
		for (char j = 'a'; j < 'a' + 26; j++) {
			StringBuilder str = new StringBuilder(input);
			str.append(j);
			dist_words.add(str.toString());
		}
		StringBuilder str = new StringBuilder(input);
		str.append(' ');
		dist_words.add(str.toString());
		return dist_words;
	}

	@Override
	public String toString() {
		return dict.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dict == null) ? 0 : dict.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Spell other = (Spell) obj;
		if (dict == null) {
			if (other.dict != null)
				return false;
		} 
		return dict.equals(other.dict);
	}
	
	
	
	
	
	
	
	
	
	

}
