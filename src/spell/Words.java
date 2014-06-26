package spell;

public class Words implements Trie {

	private int word_count;
	private int node_count;
	private WordNode root;
	
	public Words() {
		word_count = 0;
		node_count = 1;
		root = new WordNode();
	}
	
	@Override
	public void add(String word) {
		word = word.toLowerCase();
		WordNode curNode = root;
		for (int i = 0; i < word.length(); i++){
			boolean last_letter = false;
			if (i == word.length() -1)
				last_letter = true;
			curNode = traverseNode(curNode, word.charAt(i), last_letter);
		}
		curNode.count++;
	}


	// checks to see if the array letter-index in the node given is valid, if not it
	// makes a new node, puts it in the array and returns the address of that node
	// Also, if a new node is created on the last letter, it increments the unique word count
	public WordNode traverseNode(WordNode node1, char letter, boolean last_letter) {
		if(node1.WordArray[letter - 'a'] != null) {
			return node1.WordArray[letter - 'a'];
		}
		else {
			WordNode new_node = new WordNode();
			node1.WordArray[letter - 'a'] = new_node;
			node_count++;		// increment node count in this case
			if (last_letter)
				word_count++;
			return new_node;
		}
	}
	
	@Override
	public Node find(String word) {
		WordNode curNode = root;
		for (int i = 0; i < word.length(); i++){
			if (curNode.WordArray[word.charAt(i) - 'a'] == null)
				return null;
			else
				curNode = curNode.WordArray[word.charAt(i) - 'a'];
		}
		if(curNode.getValue() == 0)
			return null;
		else
			return curNode;
	}

	@Override
	public int getWordCount() {
		return word_count;
	}

	@Override
	public int getNodeCount() {
		return node_count;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + node_count;
		result = prime * result + word_count;
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
		Words other = (Words) obj;
		if (node_count != other.node_count)
			return false;
		if (root == null) {
			if (other.root != null)
				return false;
		} 
		if (word_count != other.word_count)
			return false;
		if (traverseEquals(root, other.root) == 1)
			return false;
		else
			return true;
	}
	
	public int traverseEquals(WordNode root1, WordNode root2) {
		if (root1 == null || root2 == null) {
			return 0;
		}
		int result = traverseEquals(root1.WordArray[0], root2.WordArray[0]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[1], root2.WordArray[1]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[2], root2.WordArray[2]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[3], root2.WordArray[3]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[4], root2.WordArray[4]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[5], root2.WordArray[5]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[6], root2.WordArray[6]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[7], root2.WordArray[7]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[8], root2.WordArray[8]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[9], root2.WordArray[9]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[10], root2.WordArray[10]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[11], root2.WordArray[11]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[12], root2.WordArray[12]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[13], root2.WordArray[13]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[14], root2.WordArray[14]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[15], root2.WordArray[15]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[16], root2.WordArray[16]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[17], root2.WordArray[17]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[18], root2.WordArray[18]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[19], root2.WordArray[19]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[20], root2.WordArray[20]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[21], root2.WordArray[21]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[22], root2.WordArray[22]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[23], root2.WordArray[23]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[24], root2.WordArray[24]);
		if (result == 1)
			return 1;
		result = traverseEquals(root1.WordArray[25], root2.WordArray[25]);
		if (result == 1)
			return 1;
		if(root1.getValue() != root2.getValue())
			return 1;
		else 
			return 0;	
	}
				
	@Override
	public String toString() {
		String result = "";
		String path = "";
		return toStringRecurse(root, result, path);
	}
	
	private String toStringRecurse(WordNode root1, String result, String path) {	
		if (root1 == null) {
			path = path.substring(0, path.length()-1);
			return result.substring(0,result.length()-1);
		}
		if (root1.getValue() > 0) {
			result += " " + root1.getValue() + "\n" + path;
		}
		result = toStringRecurse(root1.WordArray[0], result + 'a', path + 'a');
		result = toStringRecurse(root1.WordArray[1], result + 'b', path + 'b');
		result = toStringRecurse(root1.WordArray[2], result + 'c', path + 'c');
		result = toStringRecurse(root1.WordArray[3], result + 'd', path + 'd');
		result = toStringRecurse(root1.WordArray[4], result + 'e', path + 'e');
		result = toStringRecurse(root1.WordArray[5], result + 'f', path + 'f');
		result = toStringRecurse(root1.WordArray[6], result + 'g', path + 'g');
		result = toStringRecurse(root1.WordArray[7], result + 'h', path + 'h');
		result = toStringRecurse(root1.WordArray[8], result + 'i', path + 'i');
		result = toStringRecurse(root1.WordArray[9], result + 'j', path + 'j');
		result = toStringRecurse(root1.WordArray[10], result + 'k', path + 'k');
		result = toStringRecurse(root1.WordArray[11], result + 'l', path + 'l');
		result = toStringRecurse(root1.WordArray[12], result + 'm', path + 'm');
		result = toStringRecurse(root1.WordArray[13], result + 'n', path + 'n');
		result = toStringRecurse(root1.WordArray[14], result + 'o', path + 'o');
		result = toStringRecurse(root1.WordArray[15], result + 'p', path + 'p');
		result = toStringRecurse(root1.WordArray[16], result + 'q', path + 'q');
		result = toStringRecurse(root1.WordArray[17], result + 'r', path + 'r');
		result = toStringRecurse(root1.WordArray[18], result + 's', path + 's');
		result = toStringRecurse(root1.WordArray[19], result + 't', path + 't');
		result = toStringRecurse(root1.WordArray[20], result + 'u', path + 'u');
		result = toStringRecurse(root1.WordArray[21], result + 'v', path + 'v');
		result = toStringRecurse(root1.WordArray[22], result + 'w', path + 'w');
		result = toStringRecurse(root1.WordArray[23], result + 'x', path + 'x');
		result = toStringRecurse(root1.WordArray[24], result + 'y', path + 'y');
		result = toStringRecurse(root1.WordArray[25], result + 'z', path + 'z');
		return result.substring(0,result.length()-1);
	}

	public class WordNode implements Node {
		
		private int count;
		private WordNode[] WordArray;
		
		public WordNode(){
			count = 0;
			WordArray = new WordNode[26];
		}
		
		@Override
		public int getValue() {
			return count;
		}

	}

	

}
