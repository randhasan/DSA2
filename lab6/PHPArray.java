/**
 * PHPArray.java
 *
 * Implements a PHPArray, which is a hybrid of a hash table and a linked list.
 * It allows hash table access, and FIFO sequential access.
 *
 * javadoc compilation command:
 * $ javadoc -verbose -private -d ./docs -version -author -tag tt.wrapper:a:"API Wrapper:" ./PHPArray.java
 *
 * java compilation/run command:
 * $ javac ./*.java
 * $ java PHPArray
 *
 * @author  Sherif Khattab
 * @version 1.0
 * @since   2023-02-21
 */

import java.util.Iterator;

public class PHPArray<V> implements Iterable<V> {

	//**************************
	// OBJECT PROPERTIES
	//**************************

	private static final int INIT_CAPACITY = 4;

	private int N;              // number of key-value pairs in the symbol table
	private int M;              // size of linear probing table
	private Node<V>[] entries;  // the hash table
	private Node<V> head;       // head of the linked list
	private Node<V> tail;       // tail of the linked list


	//**************************
	// OBJECT CONSTRUCTORS
	//**************************

	/**
	 * Creates a new PHPArray using the default capacity.
	 */
	public PHPArray () {
		this(INIT_CAPACITY);
	}

	/**
	 * Creates a new PHPArray using the given capacity.
	 *
	 * @param capacity The number of entries available in the hash table.
	 */
	public PHPArray (int capacity) {
		M = capacity;

		@SuppressWarnings("unchecked")
		Node<V>[] temp = (Node<V>[]) new Node[M];
		entries        = temp;

		head = tail = null;
		N = 0;
	}


	//**************************
	// OBJECT FEATURES
	//**************************


	/**
	 * Inserts a key-value pair into the symbol table.
	 *
	 * @param key The search key string of the key-value pair.
	 * @param val The generic data value to be added or updated.
	 */
	public void put (String key, V val) {

		// Double the table size if more than half is full.
		if (N >= M/2) resize(2*M);

		// Search for an existing entry using linear probing.
		// If key-matching node exists, update entry value.
		// Otherwise, add a new node with the given key value.
		int i;
		for (i = hash(key); entries[i] != null; i = (i + 1) % M) {

			if (entries[i].key.equals(key)) {
				entries[i].value = val;
				return;
			}
		}
		entries[i] = new Node<V>(key, val);

		/**
		 * @TODO #1
		 *
		 * - Insert the new node into the tail of the linked list.
		 * - Expected Runtime: O(1)
		 */

		// <-- Add your code here --> //
		if (head == null)
		{
			head = new Node<V>(key, val);
			tail = head;
		}
		else
		{
			tail.next = new Node<V>(key, val);
			tail = tail.next;
		}
		N++;
	}

	/**
	 * Retrieves the value associated with the given key.
	 *
	 * @param key The search key string.
	 * @return Generic data value of the pair if node exists, NULL otherwise.
	 */
	public V get (String key) {
		for (int i = hash(key); entries[i] != null; i = (i + 1) % M) {
			if (entries[i].key.equals(key)) return entries[i].value;
		}
		return null;
	}

	/**
	 * Creates an integer hash from the given key.
	 *
	 * @param key The search key string.
	 * @return A 32-bit signed integer ranging from 0 to M-1.
	 */
	private int hash (String key) {
		return (key.hashCode() & 0x7fffffff) % M;
	}

	/**
	 * Resize the hash table to the given capacity. This is done by re-hashing
	 * all of the existing keys with the new modulus.
	 *
	 * @param capacity The new target capacity of the hash table.
	 */
	private void resize (int capacity) {
		PHPArray<V> temp = new PHPArray<V>(capacity);

		/**
		 * @TODO #2
		 *
		 * - Re-insert the entries from this into temp in original FIFO order
		 * - Expected Runtime: O(n)
		 */

		// <-- Add your code here --> //
		Node<V> curr = head;
		while (curr != null) {
        		temp.put(curr.key, curr.value);
        		curr = curr.next;
    		}


		entries = temp.entries;
		head    = temp.head;
		tail    = temp.tail;
		M       = temp.M;
	}

	//**************************
	// DEFINE ITERATOR
	//**************************


	/**
	 * Returns an iterator compatible with the generic data type.
	 *
	 * @return An Iterator object required for enhanced iteration of PHPArray.
	 */
	public Iterator<V> iterator() {
		return new MyIterator();
	}

	/**
	 * Implements a Java standard Iterator Interface for PHPArray.
	 *
	 * PHPArray is defined as an implementation of a Java Iterable interface.
	 * An instantiated Iterable object allows it to be the target of an
	 * enhanced for-loop, which can be invoked via the "For-Each" statement.
	 * To instantiate an Iterable object requires us to provide implementation
	 * for the abstract method Iterable.iterator(), which returns an
	 * instantiated Java Iterator object. An Iterator is the interface
	 * that, once implemented, allows a custom data type collection (PHPArray)
	 * to provide basic forward direction traversal functionality.
	 * An Iterator can be implemented directly as a final feature
	 * (Java Scanner Class), extended into a more advanced interface
	 * (Java ListIterator Interface), or be implemented as an inner class that
	 * provides data-structure specific iterator functionality for the
	 * encapsulating class (like our PHPArray). In order to define a custom
	 * instantiatable Iterator class, we need to (at the very least)
	 * implement two core abstract methods, Iterator.hasNext() and
	 * Iterator.next(). Optionally, Iterator.remove() can be implemented
	 * to provide mid-traversal deletion of collection items, a feature that
	 * was missing in the older Java Enumeration Interface.
	 */
	public class MyIterator implements Iterator<V> {
		private Node<V> current;

		/**
		 * Creates a new MyIterator with the current cursor property set to the
		 * head node of the PHPArray.
		 */
		public MyIterator() {
			current = head;
		}

		/**
		 * Returns true if the iteration has more elements.
		 *
		 * @return true if iteration cursor is not NULL, false otherwise.
		 */
		public boolean hasNext() {
			return current != null;
		}

		/**
		 * Returns the next element in the iteration and sets the iteration
		 * cursor to the next element.
		 *
		 * @return The next element in the iteration.
		 */
		public V next() {
			V result = current.value;
			current  = current.next;
			return result;
		}
	}


	//**************************
	// DEFINE NODE
	//**************************

	/**
	 * Defines a (singly) linked list element used by PHPArray.
	 * Each node contains data for a (Key, Value) pair, and a reference property
	 * to link the next list element.
	 */
	private class Node<V> {
		private String key;
		private V value;
		private Node<V> next;

		/**
		 * Creates a new Node without a reference to the next node.
		 *
		 * @param key   The key string of the key-value pair.
		 * @param value The value that will correspond to the given key string.
		 */
		Node (String key, V value) {
			this(key, value, null);
		}

		/**
		 * Creates a new Node with reference to the next node.
		 *
		 * @param key   The key string of the key-value pair.
		 * @param value The value that will correspond to the given key string.
		 * @param next  The node that will proceed this node in a list traversal.
		 */
		Node (String key, V value, Node<V> next) {
			this.key   = key;
			this.value = value;
			this.next  = next;
		}
	}



	//**************************
	// STATIC METHODS
	//**************************

	/**
	 * Prints out the contents of a given PHPArray.
	 *
	 * This method will first print the list of values of the PHPArray in a
	 * FIFO (First-In-First-Out) order, which is also the order of insertion.
	 * Then, it will print out the key-value pairs from key index 0 ~ 9.
	 *
	 * @param array PHPArray to print out.
	 */
	private static <V> void show (PHPArray<V> array) {

		System.out.println("Values in FIFO order:");
		System.out.print("\t");
		for (V i : array) {
			System.out.print(i + " ");
		}
		System.out.println();

		for (int i = 0; i < 10; i++) {
			System.out.println("A[\"Key" + i + "\"] = " + array.get("Key" + i));
		}
	}

	/**
	 * Runs the PHPArray test code.
	 *
	 * The main function will initialize a new PHPArray with a given initial
	 * capacity, run a series of put() operations inside a for-loop that
	 * decrements from 9 to 0 (ex. ("Key9",9), ... , ("Key0", 0)), and print out
	 * the PHPArray using the static show() method.
	 *
	 * @param args Program string input parameters.
	 */
	public static void main (String[] args) {

		PHPArray<Integer> A = new PHPArray<>(2);

		for (int i = 9; i >= 0; i--) {
			A.put("Key" + i, i);
		}

		show(A);
	}
}
