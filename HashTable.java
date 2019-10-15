/////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION ///////////////////////
// Title: P3b_Performance Analysis
// Files: HashTable.java
// Semester: Spring 2019
// Course: CS400
// Lecture: 002
// Due Date: Before 10pm on March 28, 2019
// Author: Shiyu Zhu
// Email: SZHU227@wisc.edu
// Lecture's Name: Deb Deppeler
// Bugs: no known bugs
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name: (name of your pair programming partner)
// Partner Email: (email address of your programming partner)
// Partner Lecturer's Name: (name of your partner's lecturer)
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
// ___ Write-up states that pair programming is allowed for this assignment.
// ___ We have both read and understand the course Pair Programming Policy.
// ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here. Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do. If you received no outside help from either type
// of source, then please explicitly indicate NONE.
//
// Persons: NONE
// Online Sources: NONE
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

/**
 * This class implement a hash table. I am going to use the the number 5 CHAINED BUCKET: array of
 * linked nodes algorithm for hashing
 * 
 * @author Shiyu Zhu
 *
 * @param <K> the generic type
 * @param <V> the generic type
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {

  /**
   * This is inner class is used to implement linked list. Keys that have the same hash code are
   * placed together in a linked list. A ListNode holds a key value pair.
   * 
   * @author shiyu Zhu
   *
   */
  private class ListNode<K, V> {
    private K key;
    private V value;
    private ListNode<K, V> next; // Pointer to next node in the list

    /**
     * This is an inner class constructor which assign key to k,value to v.
     * 
     * @param k - key of the node
     * @param v - value of the node
     * @param n - the next ListNode
     */
    private ListNode(K pkey, V pValue, ListNode<K, V> pNextNode) {
      this.key = pkey;
      this.value = pValue;
      this.next = pNextNode;
    }
  }

  private int tableSize; // The initial capacity (bucket array size) of the hash table.
  private double loadFactorThreshold; // If the loadFactorThreshold greater that 0.75, then we need
                                      // to rehash the hash table
  private int count; // The number of (key,value) pairs in the hash table.
  private ListNode<K, V>[] hashTable; // The hash table, represented as an array of linked lists.

  /**
   * This is a default no-arg constructor, with initial capacity equal to 11, and load factor
   * threshold equal to 0.75
   */
  public HashTable() {
    this(11, 0.75);
    hashTable = new ListNode[tableSize];
  }

  /**
   * This is a constructor that accepts with provided initial capacity and load factor threshold.
   * The threshold is the load factor that causes a resize and rehash
   * 
   * @param initialCapacity
   * @param loadFactorThreshold
   */
  public HashTable(int initialCapacity, double loadFactorThreshold) {
    this.tableSize = initialCapacity;
    this.loadFactorThreshold = loadFactorThreshold;
    hashTable = new ListNode[tableSize];
  }

  /**
   * This method is going to insert the key,value pair to the data structure and increase the number
   * of keys.
   * 
   * @param key   The key to be inserted
   * @param value The value to be inserted
   * @throws DuplicateKeyException   If key is already in data structure, throw
   *                                 DuplicateKeyException()
   * @throws IllegalNullKeyException If key is null, throw IllegalNullKeyException
   * @see DataStructureADT#insert(K key, V value)
   */
  @Override
  public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
    // If key is null, throw IllegalNullKeyException;
    if (key == null) {
      throw new IllegalNullKeyException();
    }
    // computer the hashIndex value of the specific key
    int hashIndex = Math.abs(key.hashCode()) % tableSize;
    // go to the first ListNode of the specific hash index
    ListNode<K, V> list = hashTable[hashIndex];
    // look throw the whole linked list to check if key is already in data structure, throw
    // DuplicateKeyException();
    while (list != null) {
      if (list.key.equals(key)) {
        throw new DuplicateKeyException();
      } else {
        list = list.next;
      }
    }
    if (count >= loadFactorThreshold * tableSize) {
      resize();
    }
    // Add the key,value pair as the first! node of the linked list and increase the number of keys.
    ListNode<K, V> newNode = new ListNode<K, V>(key, value, hashTable[hashIndex]);
    hashTable[hashIndex] = newNode;
    count++;
  }

  /**
   * This is a private method which resize the hash table.
   * 
   * @throws DuplicateKeyException   If key is already in data structure, throw
   *                                 DuplicateKeyException()
   * @throws IllegalNullKeyException If key is null, throw IllegalNullKeyException
   */
  private void resize() throws IllegalNullKeyException, DuplicateKeyException {
    int newSize = 2 * hashTable.length + 1;
    HashTable<K, V> newHashTable = new HashTable<K, V>(newSize, this.loadFactorThreshold);
    for (int i = 0; i < tableSize; i++) {
      ListNode<K, V> x = hashTable[i];
      if (x != null) {
        while (x != null) {
          newHashTable.insert(x.key, x.value);
          x = x.next;
        }
      }
    }
    this.tableSize = newSize;
    this.hashTable = newHashTable.hashTable;
  }

  /**
   * This method is going to remove the key,value pair from the data structure and decrease the
   * number of keys.
   * 
   * @param key   The key to be removed
   * @param value The value to be removed
   * @throws IllegalNullKeyException If key is null, throw IllegalNullKeyException
   * @see DataStructureADT#remove(K key, V value)
   */
  @Override
  public boolean remove(K key) throws IllegalNullKeyException {
    // If key is null, throw IllegalNullKeyException;
    if (key == null) {
      throw new IllegalNullKeyException();
    }
    // computer the hashIndex value of the specific key
    int hashIndex = Math.abs(key.hashCode()) % tableSize;
    // go to the first ListNode of the specific hash index
    ListNode<K, V> list = hashTable[hashIndex];
    // if the list is null, then key not exist, return false
    if (list == null) {
      return false;
    } else {// if the list is not null, go through the whole list to check is the key value exists
      // front case
      if (list.key.equals(key)) {
        list = list.next;
        this.count--;
        return true;
      }
      // non-front case
      while (list.next != null) {
        if (list.next.key.equals(key)) {
          list.next = list.next.next;
          this.count--;
          return true;
        }
        list = list.next; // move to the next node
      }
    }
    return false;
  }

  /**
   * This method is going to return the value of the specific key from the data structure.
   * 
   * @param key The key to be searched
   * @throws KeyNotFoundException    If key is not found, throw KeyNotFoundException().
   * @throws IllegalNullKeyException If key is null, throw IllegalNullKeyException
   * @see DataStructureADT#get(K key)
   */
  @Override
  public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
    // If key is null, throw IllegalNullKeyException;
    if (key == null) {
      throw new IllegalNullKeyException();
    }
    // computer the hashIndex value of the specific key
    int hashIndex = Math.abs(key.hashCode()) % tableSize;
    // go to the first ListNode of the specific hash index
    ListNode<K, V> list = hashTable[hashIndex];
    // look the whole list to check if the key is in the list
    while (list != null) {
      if (list.key.equals(key)) {
        return list.value;
      }
      list = list.next; // move to the next node
    }
    // If key is not found, throw KeyNotFoundException().
    throw new KeyNotFoundException();
  }

  /**
   * Returns the number of key,value pairs in the data structure
   * 
   * @see DataStructureADT#numKeys()
   */
  @Override
  public int numKeys() {
    return this.count;
  }

  /**
   * Returns the load factor threshold that was passed into the constructor when creating the
   * instance of the HashTable
   * 
   * @see HashTableADT#getLoadFactorThreshold()
   */
  @Override
  public double getLoadFactorThreshold() {
    return this.loadFactorThreshold;
  }

  /**
   * Returns the current load factor for this hash table load factor = number of items / current
   * table size
   * 
   * @see HashTableADT#getLoadFactor()
   */
  @Override
  public double getLoadFactor() {
    return ((double) (count)) / tableSize;
  }

  /**
   * Return the current Capacity (table size) of the hash table array. The initial capacity must be
   * a positive integer, 1 or greater and is specified in the constructor. REQUIRED: When the load
   * factor threshold is reached, the capacity must increase to: 2 * capacity + 1 Once increased,
   * the capacity never decreases
   * 
   * @see HashTableADT#getCapacity()
   */
  @Override
  public int getCapacity() {
    return tableSize;
  }

  /**
   * Returns the collision resolution scheme used for this hash table. Implement with one of the
   * following collision resolution strategies. Define this method to return an integer to indicate
   * which strategy.
   * 
   * @see HashTableADT#getCollisionResolution()
   */
  // 1 OPEN ADDRESSING: linear probe
  // 2 OPEN ADDRESSING: quadratic probe
  // 3 OPEN ADDRESSING: double hashing
  // 4 CHAINED BUCKET: array of arrays
  // 5 CHAINED BUCKET: array of linked nodes
  // 6 CHAINED BUCKET: array of search trees
  // 7 CHAINED BUCKET: linked nodes of arrays
  // 8 CHAINED BUCKET: linked nodes of linked node
  // 9 CHAINED BUCKET: linked nodes of search trees
  @Override
  public int getCollisionResolution() {
    return 5;
  }



}

