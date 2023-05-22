/**
 * An implementation of the AutoCompleteInterface using a DLB Trie.
 */

public class AutoComplete implements AutoCompleteInterface {

  private DLBNode root;
  private StringBuilder currentPrefix;
  private DLBNode currentNode;
  private DLBNode backtrack;

  public AutoComplete(){
    root = null;
    currentPrefix = new StringBuilder();
    currentNode = null;
    backtrack = null;
  }

  /**
   * Adds a word to the dictionary in O(alphabet size*word.length()) time
   * @param word the String to be added to the dictionary
   * @return true if add is successful, false if word already exists
   * @throws IllegalArgumentException if word is the empty string
   */
  public boolean add(String word)
  {
    if (word.equals("")) //if word is an empty string
    {
      throw new IllegalArgumentException(); 
    }

    DLBNode curr = root;

    for (int i = 0; i < word.length(); i++) {
      char c = word.charAt(i);
      if (i == 0)
      {
        if (root == null) //first letter
        {
          root = new DLBNode(c); //create new node
          curr = root;
        }
        else if(curr.data != c) //first letter of word doesn't equal root node... so find root node or create a new one for this first letter
        {
          while (curr.data != c && curr.nextSibling != null) {
            curr = curr.nextSibling;
          }
          if (curr.data != c) {
            curr.nextSibling = new DLBNode(c);
            curr = curr.nextSibling;
          }
        }
      }
      else{ //now just add next letter as a child or the sibling of the currentNode's child.  move currentNode pointer as you do this.
        if (curr.child == null) {
          curr.child = new DLBNode(c);
          curr.child.parent = curr;
        }
        curr = curr.child;
        while (curr.data != c && curr.nextSibling != null) {
          curr = curr.nextSibling;
        }
        if (curr.data != c) {
          curr.nextSibling = new DLBNode(c);
          curr = curr.nextSibling;
        }
      }
    }

    if (curr.isWord) //if the last letter that the currentNode is pointing at is already flagged marking the end of a word, the word already exists in the DLB Trie
    {
      return false;
    }
    curr.isWord = true; //otherwise, flag it as the end of a word
    curr.size++; //size keeps track of the number of nodes with isWord == true in the subtree rooted at the node, including the node itself
    while(curr.parent!=null)
    {
      curr = curr.parent;
      curr.size++;
    }
    return true; //add is successful
  }

  /**
   * appends the character c to the current prefix in O(alphabet size) time. 
   * This method doesn't modify the dictionary.
   * @param c: the character to append
   * @return true if the current prefix after appending c is a prefix to a word 
   * in the dictionary and false otherwise
   */
  public boolean advance(char c){
    currentPrefix.append(c);
    currentNode = root; //start at the root and traverse through the DLB trie
    for (int i = 0; i<currentPrefix.length(); i++)
    {
      while (currentNode!=null && currentNode.data!=currentPrefix.charAt(i)) //if currentNode doesn't match the current letter of the word, go through it's siblings to find it
      {
        currentNode = currentNode.nextSibling;
      }
      if (currentNode == null)
      {
        return false;
      }
      if (i < currentPrefix.length() - 1) //as long as we haven't reached the last letter
      {
        currentNode = currentNode.child; //move currentNode pointer to it's child
      }
      if (currentNode == null) //if it equals null we return false
      {
        //currentNode = store;
        //currentPrefix.deleteCharAt(currentPrefix.length() -1);
        return false;
      }
      backtrack = currentNode;
    }
    if (currentNode.child != null || currentNode.isWord)
    {
      return true;
    }
    return false; //if we haven't returned false that means currentNode is successfully pointing at the last letter node in the word.  return if it's flagged as the end of a word.
  }

  /**
   * removes the last character from the current prefix in O(alphabet size) time. This 
   * method doesn't modify the dictionary.
   * @throws IllegalStateException if the current prefix is the empty string
   */
  public void retreat(){
    if (currentPrefix.length() == 0){
      reset();
      throw new IllegalStateException("");
    }
    currentPrefix.deleteCharAt(currentPrefix.length() - 1);

    currentNode = backtrack;
  }

  /**
   * resets the current prefix to the empty string in O(1) time
   */
  public void reset(){
    currentPrefix = new StringBuilder(""); //currentPrefix = ""
    currentNode = root;
    backtrack = null;
  }
    
  /**
   * @return true if the current prefix is a word in the dictionary and false
   * otherwise. The running time is O(1).
   */
  public boolean isWord(){
    if (currentNode == null) //if currentNode now points to null because user typed a letter that wasn't a child of the currentNode so the word must not be in the dictionary
    {
      return false;
    }
    return currentNode.isWord; //otherwise just return if the currentNode is flagged or not as the end of a word
  }

  /**
   * adds the current prefix as a word to the dictionary (if not already a word)
   * The running time is O(alphabet size*length of the current prefix). 
   */
  public void add(){
    add(currentPrefix.toString()); //call the other add method which will traverse down the DLB Trie through the letters that already exist and create a new node for the last char we are adding and a new word
  }

  /** 
   * @return the number of words in the dictionary that start with the current 
   * prefix (including the current prefix if it is a word). The running time is 
   * O(1).
   */
  public int getNumberOfPredictions(){
    if (currentNode == null) //if we have moved currentNode beyond the end of a word (there are no children) we cannot make a prediction
    {
      return 0;
    }
    return currentNode.size; //otherwise just return the size of the current node
  }
  
  /**
   * retrieves one word prediction for the current prefix. The running time is 
   * O(prediction.length())
   * @return a String or null if no predictions exist for the current prefix
   */
  public String retrievePrediction()
  {
    DLBNode curr = currentNode;
    if (curr == null)
    {
      return null;
    }
    StringBuilder prediction = new StringBuilder(currentPrefix); //if prefix is a word we just return that
    while (!curr.isWord) //otherwise we iterate through it's children until finding one that marks the end of a word
    {
      if (curr.child!= null)
      {
        curr = curr.child;
        prediction.append(curr.data); //add child data to prediction
      }
      else
      {
        return null; //if we go through all the children without reaching any that are flagged marking the end of a word, we return null
      }
    }
    return prediction.toString(); //if we haven't returned null then we return the prediction we've made
  }  

  /* ==============================
   * Helper methods for debugging.
   * ==============================
   */

  //print the subtrie rooted at the node at the end of the start String
  public void printTrie(String start){
    System.out.println("==================== START: DLB Trie Starting from \""+ start + "\" ====================");
    if(start.equals("")){
      printTrie(root, 0);
    } else {
      DLBNode startNode = getNode(root, start, 0);
      if(startNode != null){
        printTrie(startNode.child, 0);
      }
    }
    
    System.out.println("==================== END: DLB Trie Starting from \""+ start + "\" ====================");
  }

  //a helper method for printTrie
  private void printTrie(DLBNode node, int depth){
    if(node != null){
      for(int i=0; i<depth; i++){
        System.out.print(" ");
      }
      System.out.print(node.data);
      if(node.isWord){
        System.out.print(" *");
      }
      System.out.println(" (" + node.size + ")");
      printTrie(node.child, depth+1);
      printTrie(node.nextSibling, depth);
    }
  }

  //return a pointer to the node at the end of the start String 
  //in O(start.length() - index)
  private DLBNode getNode(DLBNode node, String start, int index){
    if(start.length() == 0){
      return node;
    }
    DLBNode result = node;
    if(node != null){
      if((index < start.length()-1) && (node.data == start.charAt(index))) {
          result = getNode(node.child, start, index+1);
      } else if((index == start.length()-1) && (node.data == start.charAt(index))) {
          result = node;
      } else {
          result = getNode(node.nextSibling, start, index);
      }
    }
    return result;
  } 

  //The DLB node class
  private class DLBNode{
    private char data;
    private int size;
    private boolean isWord;
    private DLBNode nextSibling;
    private DLBNode previousSibling;
    private DLBNode child;
    private DLBNode parent;

    private DLBNode(char data){
        this.data = data;
        size = 0;
        isWord = false;
        nextSibling = previousSibling = child = parent = null;
    }
  }
}
