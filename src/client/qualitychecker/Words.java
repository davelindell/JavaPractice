package client.qualitychecker;

public class Words implements Trie {
	private WordNode root;
	private int word_count;
	private int node_count;
	
	public Words() {
		root = new WordNode();
		word_count = 0;
		node_count = 1;
	}
	
	@Override
	public void add(String word) {
		WordNode cur_node = root;
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == ' ') {
				if(cur_node.nodes[26] == null) {
					cur_node.nodes[26] = new WordNode();
					node_count++;
				}
				cur_node = cur_node.nodes[26];	
			}
			else {
				if(cur_node.nodes[word.charAt(i)-'a'] == null) {
					cur_node.nodes[word.charAt(i)-'a'] = new WordNode();
					node_count++;
				}
				cur_node = cur_node.nodes[word.charAt(i)-'a'];	
			}
			
		}
		cur_node.count++;
		word_count++;
	}

	@Override
	public Node find(String word) {
		WordNode cur_node = root;
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == ' ') {
				if(cur_node.nodes[26] == null) 
					return null;
				cur_node = cur_node.nodes[26];	
			}
			else{
				if (cur_node.nodes[word.charAt(i) - 'a'] == null)
					return null;
				cur_node = cur_node.nodes[word.charAt(i) - 'a'];
			}
		}
		if (cur_node.count > 0) 
			return cur_node;
		else
			return null;
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
		result = prime * result + node_count+ word_count;;
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

		return equalsRecurse(root,other.root);
	}

	public boolean equalsRecurse(WordNode node1, WordNode node2) {
		if (node1 == null || node2 == null)
			return true;
		for (int i = 0; i < 27; i++) {
			boolean result = equalsRecurse(node1.nodes[i],node2.nodes[i]);
			if (result == false)
				return false;
		}
		if (node1.count != node2.count)
			return false;
		else
			return true;
	}
	
		@Override
	public String toString() {
			String result = "";
			String path = "";
			return recurseToString(root, result, path);
		}
		
	public String recurseToString(WordNode node, String result, String path) {
		if (node == null) {
			path = path.substring(0,path.length()-1);
			return result.substring(0,result.length()-1);
		}
		if (node.count > 0) {
			result += " " + node.count + "\n" + path;
		}
		
		for (char i = 'a'; i < 'a' + 27; i++) {
			result = recurseToString(node.nodes[i-'a'], result + Character.toString(i), path + Character.toString(i));
		}
		return result.substring(0,result.length()-1);
	}
	
	public class WordNode implements Trie.Node {
		private int count;
		private WordNode[] nodes;
		public WordNode() {
			count = 0;
			nodes = new WordNode[27];
		}
		
		public int getValue() {
			return count;	
		}
		
	}

}
