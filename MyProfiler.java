/**
 * Filename: MyProfiler.java Project: p3b-201901 Authors: Shiyu Zhu 002
 *
 * Semester: Spring 2019 Course: CS400
 * 
 * Due Date: Before 10pm on March 28, 2019 Version: 1.0
 * 
 * Credits: NONE
 * 
 * Bugs: NONE
 */

import java.util.TreeMap;

/**
 * This class is used as the data structure to test our hash table against Java's TreeMap
 * structures. The program will perform a "bunch" of inserts, lookups, and removes to the data
 * structures to figure out which data structure performs best.
 * 
 * @author Shiyu Zhu
 *
 * @param <K> the generic type
 * @param <V> the generic type
 */
public class MyProfiler<K extends Comparable<K>, V> {

  HashTableADT<K, V> hashtable;
  TreeMap<K, V> treemap;

  /**
   * This constructor is going to insatantiate the HashTable and java's TreeMap
   */
  public MyProfiler() {
    hashtable = new HashTable<K,V>();
    treemap = new TreeMap<K, V>();
  }

  /**
   * This method is going to insert K,V into both data structures.
   * 
   * @param key   The key to be inserted
   * @param value The value to be inserted
   * @throws DuplicateKeyException 
   * @throws IllegalNullKeyException 
   */
  public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
    hashtable.insert(key, value);
    treemap.put(key, value);
  }

  /**
   * This method is going to get value V for Key K from data structures.
   * 
   * @param key The key to be retrieved
   * @throws KeyNotFoundException 
   * @throws IllegalNullKeyException 
   */
  public void retrieve(K key) throws IllegalNullKeyException, KeyNotFoundException {
    hashtable.get(key);
    treemap.get(key);
  }

  /**
   * This main method is going to create an instance of MyProfiler to compare two data structures.
   * 
   * @param args The profile class takes one argument<numElements>
   */
  public static void main(String[] args) {
    try {
      int numElements = Integer.parseInt(args[0]);
      MyProfiler<Integer, Integer> profile = new MyProfiler<Integer, Integer>();
      for (int i = 0; i < numElements; i++) {
        profile.insert(i, i);
        profile.retrieve(i);
      }
      String msg = String.format("Inserted and retreived %d (key,value) pairs", numElements);
      System.out.println(msg);
    } catch (Exception e) {
      System.out.println("Usage: java MyProfiler <number_of_elements>");
      System.exit(1);
    }
  }
}
