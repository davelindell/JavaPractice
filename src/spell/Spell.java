package spell;

import java.awt.List;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import spell.SpellCorrector.NoSimilarWordFoundException;
import spell.Trie.Node;

public class Spell implements SpellCorrector {
	
	private Words words;
	
	public Spell(){
		words = new Words();
	}
	
	public Words getWords() {
		return words;
	}
	
	/**
	 * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
	 * for generating suggestions. 
	 * @param dictionaryFileName File containing the words to be used
	 * @throws IOException If the file cannot be read
	 */
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		words = new Words();
		Scanner scanner = null;
		
		scanner = new Scanner(new FileReader(dictionaryFileName));

		while(scanner.hasNext()){
			String word = scanner.next();
			boolean is_a_word = true;
			for (int i = 0; i < word.length(); i++){
				if(!Character.isLetter(word.charAt(i)))
					is_a_word = false;
			}
			if (is_a_word) {
				words.add(word);
			}
		}
		scanner.close();
	}
	/**
	 * Suggest a word from the dictionary that most closely matches
	 * <code>inputWord</code>
	 * @param inputWord
	 * @return The suggestion
	 * @throws NoSimilarWordFoundException If no similar word is in the dictionary
	 */
	@Override
	public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException {
		inputWord = inputWord.toLowerCase();
		if (words.find(inputWord) != null)
			return inputWord;

		ArrayList<Tuple> suggStrings = new ArrayList<Tuple>(10);
		
		Set<String> distOneList = new TreeSet<String>();
		distOneList = getDistOneList(inputWord);
		Set<String> distOneListCopy = new TreeSet<String>();
		distOneListCopy.addAll(distOneList);
		//System.out.println(distOneList);
		Iterator<String> iter1 = distOneListCopy.iterator();
		
		Set<String> distTwoList = null;
		Iterator<String> iter2 = null;
		
		while (iter1.hasNext()) {
			String curString = iter1.next();
			Node curNode = words.find(curString);
			if (curNode == null) {
				iter1.remove();
			}
			else {
				int value = curNode.getValue();
				suggStrings.add(new Tuple(value, curString));
			}
		}
		// if first iteration returned nothing
		if(suggStrings.size() == 0) {
			distTwoList = new TreeSet<String>();			
			Iterator<String> dist_iter = distOneList.iterator(); 
			
			while (dist_iter.hasNext()) {
				distTwoList.addAll(getDistOneList(dist_iter.next()));
			}
			
			
			iter2 = distTwoList.iterator();
			while (iter2.hasNext()) {
				String curString = iter2.next();
				Node curNode = words.find(curString);
				if (curNode == null) {
					iter2.remove();
				}
				else {
					int value = curNode.getValue();
					suggStrings.add(new Tuple(value, curString));
				}
			}
			
			if (suggStrings.size() == 0) {
				throw new NoSimilarWordFoundException();
			}
			
			else {
				Iterator<Tuple> tup_iter = suggStrings.iterator();
				Tuple curTup = null;
				int maxVal = 0;
				
				ArrayList<Tuple> maxTuples = new ArrayList<Tuple>(10);
				
				while (tup_iter.hasNext()) {
					curTup = tup_iter.next();
					if (curTup.val > maxVal) {
						maxVal = curTup.val;
					}
				}
				
				tup_iter = suggStrings.iterator();
				while(tup_iter.hasNext()) {
					curTup = tup_iter.next();
					if (curTup.val == maxVal) {
						maxTuples.add(curTup);
					}
				}
				
				tup_iter = maxTuples.iterator();
				String firstString = null;
				while(tup_iter.hasNext()) {
					curTup = tup_iter.next();
					if (firstString == null)
						firstString = curTup.name;
					else if (curTup.name.compareTo(firstString) == -1) {
						firstString = curTup.name;
					}
				}
				return firstString;
			}
				
			
		}
		// if we have something from first iteration:
		else {			
			Iterator<Tuple> tup_iter = suggStrings.iterator();
			Tuple curTup = null;
			int maxVal = 0;
			
			ArrayList<Tuple> maxTuples = new ArrayList<Tuple>(10);
			
			while (tup_iter.hasNext()) {
				curTup = tup_iter.next();
				if (curTup.val > maxVal) {
					maxVal = curTup.val;
				}
			}
			
			tup_iter = suggStrings.iterator();
			while(tup_iter.hasNext()) {
				curTup = tup_iter.next();
				if (curTup.val == maxVal) {
					maxTuples.add(curTup);
				}
			}
			
			tup_iter = maxTuples.iterator();
			String firstString = null;
			while(tup_iter.hasNext()) {
				curTup = tup_iter.next();
				if (firstString == null)
					firstString = curTup.name;
				else if (curTup.name.compareTo(firstString) == -1) {
					firstString = curTup.name;
				}
			}
			return firstString;
		}
	}

	public Set<String> getDistOneList(String word) {
		// remember to remove original string if its there
		Set<String> result = new TreeSet<String>();
		result.addAll(getDeletionStrings(word));
		result.addAll(getTranspositionStrings(word));
		result.addAll(getAlterationStrings(word));
		result.addAll(getInsertionStrings(word));
		return result;
	}
	
	public Set<String> getDeletionStrings(String word) {
		Set<String> result = new TreeSet<String>();
		StringBuilder edit_word;
		for (int i = 0; i < word.length(); i++){
			edit_word = new StringBuilder(word);
			result.add(edit_word.deleteCharAt(i).toString());
		}
		return result;
	}
	
	public Set<String> getTranspositionStrings(String word) {
		Set<String> result = new TreeSet<String>();
		StringBuilder edit_word;
		for (int i = 0; i < word.length()-1; i++) {
			edit_word = new StringBuilder(word);
			String first = word.substring(i,i+1);
			String second = word.substring(i+1,i+2);
			edit_word.replace(i,i+1,second);
			edit_word.replace(i+1,i+2,first);
			result.add(edit_word.toString());
		}
		return result;
	}
	
	public Set<String> getAlterationStrings(String word) {
		Set<String> result = new TreeSet<String>();
		StringBuilder edit_word;
		for (int i = 0; i<word.length(); i++) {
			for (char j = 97; j < (122 + 1); j++) {
				edit_word = new StringBuilder(word);
				result.add(edit_word.replace(i, i+1,Character.toString(j)).toString());
			}
		}
		return result;
	}
	
	public Set<String> getInsertionStrings(String word) {
		Set<String> result = new TreeSet<String>();
		StringBuilder edit_word;
		for (int i = 0; i<word.length(); i++) {
			for (char j = 97; j < (122 + 1); j++) {
				edit_word = new StringBuilder(word);
				result.add(edit_word.insert(i, (Character.toString(j))).toString());
			}
		}
		for (char j = 97; j < (122 + 1); j++) {
			edit_word = new StringBuilder(word);
			result.add(edit_word.append((Character.toString(j))).toString());
		}
		//System.out.println(result);
		return result;
	}

	@Override
	public String toString() {
		return words.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((words == null) ? 0 : words.hashCode());
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
		if (words == null) {
			if (other.words != null)
				return false;
		} else if (!words.equals(other.words))
			return false;
		return true;
	}
	
	
	public class Tuple {
		private int val;
		private String name;
		
		public Tuple(int val, String name) {
			this.val = val;
			this.name = name;
		}
		
		public int getVal() {
			return val;
		}
		public void setVal(int val) {
			this.val = val;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	
}
