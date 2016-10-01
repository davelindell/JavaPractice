package client.qualitychecker;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QualityCheckerTest {
	private QualityChecker quality_checker;
	
	@Before
	public void setUp() throws Exception {
		quality_checker = new QualityChecker();
	}

	@After
	public void tearDown() throws Exception {
		quality_checker = null;
	}

	@Test
	public void testGetSuggestions() {
		String dictionary = "";
		String word = "";
		
		// check cases
		
		// empty input word, dictionary
		assertTrue(quality_checker.getSuggestions(dictionary, word).isEmpty());

		// single character input, empty dictionary
		word = "a";
		assertTrue(quality_checker.getSuggestions(dictionary, word).isEmpty());
		
		// Single charcter input, single character dictionary
		word = "a";
		dictionary = "b";
		assertTrue(quality_checker.getSuggestions(dictionary, word).size() == 1);
		assertTrue(quality_checker.getSuggestions(dictionary, word).get(0).equals("b"));

		// Word with multiple spaces or different capitalizations and check alphabetical order
		List<String> words = Arrays.asList("utahstate","universityofuTah","notresame",
				"c","BY","university ofWtah","notrs damq","d","notr dam");
		dictionary = "University of Utah,Utah State,Notre Dame,"
				+ "UCLA,Duke,USC,ASU,UVU,FSU,CU,UT,BYU,USU,NYU,BSU,ISU";
		
		// test insertion cases
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(0)).size() == 1);
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(0)).get(0).equals("utah state"));

		// check insertion, capitalization
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(1)).size() == 1);
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(1)).get(0).equals("university of utah"));

		// mistyped letter, insertion
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(2)).size() == 1);
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(2)).get(0).equals("notre dame"));

		// alphabetic return order
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(3)).size() == 3);
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(3)).get(0).equals("cu"));
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(3)).get(1).equals("usc"));
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(3)).get(2).equals("ut"));

		// alphabetic return order, dist 2 return
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(4)).size() == 5);
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(4)).get(0).equals("bsu"));
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(4)).get(1).equals("byu"));
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(4)).get(2).equals("cu"));
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(4)).get(3).equals("nyu"));
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(4)).get(4).equals("ut"));
		
		// space insertion + mistyped letter distance 2
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(5)).size() == 1);
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(5)).get(0).equals("university of utah"));

		// double mistype
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(6)).size() == 1);
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(6)).get(0).equals("notre dame"));
		
		// distance 2 return
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(7)).size() == 2);
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(7)).get(0).equals("cu"));
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(7)).get(1).equals("ut"));
	
		// double missed letter
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(8)).size() == 1);
		assertTrue(quality_checker.getSuggestions(dictionary, words.get(8)).get(0).equals("notre dame"));
		
	
	}

	@Test
	public void testCheckWord() {
		String dictionary = "";
		String word = "";
		
		// check cases:
			
		// Empty input word, dictionary
		assertTrue(quality_checker.checkWord(dictionary, word));

		
		// Single character input, empty dictionary
		word = "a";
		assertFalse(quality_checker.checkWord(dictionary, word));

		// Empty character input, single character dictionary
		word = "";
		dictionary = "a";
		assertTrue(quality_checker.checkWord(dictionary, word));

		// Word matches dictionary
		word = "test";
		dictionary = "test";
		assertTrue(quality_checker.checkWord(dictionary, word));

		// Word with multiple spaces
		word = "this is a test";
		dictionary = "this is a test,i am a test,blah,this is a tes";
		assertTrue(quality_checker.checkWord(dictionary, word));

		dictionary = "i am a test,blah,this is a tes";
		assertFalse(quality_checker.checkWord(dictionary, word));

		// Word with non-alphanumeric input
		word = "#$@#$";
		dictionary = "test";
		assertFalse(quality_checker.checkWord(dictionary, word));

		// null dictionary
		word = "test";
		dictionary = null;
		assertTrue(quality_checker.checkWord(dictionary, word));

		
		// check matching against 100 random words in the dictionary
		Random random = new Random((new Date()).getTime());

	    char[] chars = {'a','b','c','d','e','f','g','h','i','j',
	               'k','l','m','n','o','p','q','r','s','t',
	               'u','v','w','x','y','z','A','B','C','D','E',
	               'F','G','H','I','J','K','L','M','N','O','P',
	               'Q','R','S','T','U','V','W','X','Y','Z'};
  
	    StringBuilder dict_builder = new StringBuilder();		
		for(int i = 0; i < 100; ++i) {
			String rand_out = "";
		    int length = 10;
		  
		    for (int j = 0; j < length; ++j) {
		    	int rand_pos = random.nextInt(chars.length);
		    	rand_out += chars[rand_pos];
		    }
		    
		    if (i == 50)
		    	word = rand_out;
		    
		    dict_builder.append(rand_out);
		    dict_builder.append(',');
		}
		
		assertTrue(quality_checker.checkWord(dict_builder.toString(), word));

	}

}
