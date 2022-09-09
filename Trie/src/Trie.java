import java.util.HashMap;
import java.util.TreeSet;

public class Trie {

    public class TrieNode{

        HashMap<Character, TrieNode> children;
        char c;
        boolean isEndOfWord;

        public TrieNode(){ //If not given a character, this is probably our root node
            children = new HashMap<Character, TrieNode>();
        }

        public TrieNode(char c){
            this.c = c;
            children = new HashMap<Character, TrieNode>();
        }

        public void insert(String word){
            //Null or empty case (Parameter is bad)
            if(word == null || word.isEmpty()){
                return;
            }
            //Create a "HashMap" Tree for each character
            //Get the first character of your word
            char firstChar = word.charAt(0);
            //Map the character to it's key Value pair in children hashmap
            TrieNode child = children.get(firstChar);
            //If null, we have a new child for that letter, so create one
            if (child == null) {
                child = new TrieNode(firstChar);
                children.put(firstChar, child);
            }
            //Recursive Case, move on to the next element in the string
            if (word.length() > 1)
                child.insert(word.substring(1));
                //Base Case, We've "chopped off" all the words in our string
            else
                child.isEndOfWord = true;
        }

    }


    TrieNode root; //Declare the root node, doesn't need parameter (char c)
    // since we don't know what the first letter will be until the user calls for it

    //Build the Trie Data Structure as our "word bank"
    public Trie(TreeSet<String> dict){
        root = new TrieNode(); // Initialize without a parameter, use default constructor
        for(String word : dict){
            root.insert(word);
        }
    }

    public void suggest(TrieNode node, TreeSet<String> suggestionlist, StringBuffer curr) {
        //Base Case: If we parse through each letter of the trie and get to an isEndOfWord,
        // it's a suggestible word, add it to our suggestionList
        if (node.isEndOfWord) {
            suggestionlist.add(curr.toString());
        }
        //If we got to the "end" of a branch (i.e. we've exhausted our options for that
        // given sequence), exit the function, since there no actual words to suggest
        if (node.children == null || node.children.isEmpty())
            return;
        // Recursive case : For every child in our children hashmap for a given TrieNode,
        // check if that one also has children
        for (TrieNode child : node.children.values()) { //Out of the children that this specific trieNode has, see
            // which ones form a word and which are children for something else
            //Recursively call suggest but with updated curr to see if that phrase is a word or has more children that form a word
            suggest(child, suggestionlist, curr.append(child.c));
            //"Chop off" a letter because we're iterating one character at a time
            curr.setLength(curr.length() - 1);
        }
    }

    public TreeSet<String> autocomplete(String prefix) {
        //Make an ArrayList to contain all the suggested words
        TreeSet<String> suggestionList = new TreeSet<>();
        //Make a node that points to the root, we'll use this to traverse the trie
        TrieNode node = root;
        //Use a StringBuffer to add elements as we go
        StringBuffer curr = new StringBuffer();
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null)
                return suggestionList;
            curr.append(c);
        }
        suggest(node, suggestionList, curr);
        return suggestionList;
    }

    public static void main(String[] args) {
        TreeSet<String> dict = new TreeSet<>();
        dict.add("Trophy");
        dict.add("Sunday");
        dict.add("Troop");
        dict.add("Trot");
        dict.add("Trap");
        dict.add("Running");
        dict.add("Apple");
        dict.add("Abundant");
        Trie trie = new Trie(dict);
        System.out.println(trie.autocomplete("Tro"));
    }

}
