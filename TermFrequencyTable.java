/* TermFrequencyTable.java
 * 
 * Author: Cynthia Chan (ccynthia@bu.edu)
 * Date: 4/29/15
 * Assignment: HW07 B3
 * Description: Stores words from two strings (documents) in a hash table
 *              and calculate the cosine similarity of them
 * */

public class TermFrequencyTable {
  
  // debugging: (from Prof. Snyder)
  private static boolean debug = false;           // set this to true if you want to trace your execution
  
  private static void db(String s) {            
    if(debug) 
      System.out.println(s);
  }
  
  // for debugging: prints hash table
  public void printHash()
  {
    for (int i = 0; i < T.length; i++)
    {
      System.out.print("[" + i + "]:\t");
      printList(T[i]);
    }   
  }
  
  // for debugging: prints out a Node (linked list)
  private static void printList(Node r) {
    for( ; r != null; r = r.next)
    {
      System.out.print(r.term);
      System.out.print(" {" + r.termFreq[0] + ", " + r.termFreq[1] + "} -> ");
    }
    System.out.println("null");
  }

  // global variables to interate through hash table
  private int index;   // index of array
  private Node p;      // pointer to first node in linked list
  
  
  // node class
  private class Node {
    String term;
    int[] termFreq = new int[2]; // term frequency in each of two documents
    Node next;
    
//    public Node(String term, int[] termFreq)
//    {
//      this.term = term;
//      this.termFreq = termFreq;
//      this.next = null;
//    }
    
    public Node(String term)
    {
      this.term = term;
      this.termFreq[0] = 0;
      this.termFreq[1] = 0;
      this.next = null;
    }   
  } // end of node class
  
  
  // hash table size
  private final int SIZE = 101;  // prime number around 100 // use SIZE = 5 for the unit test
  
  private Node[] T = new Node[SIZE];
   
  // hash function: Takes a String as a parameter, returns an int
  // (Source: http://research.cs.vt.edu/AVresearch/hashing/strings.php )
  private int hash(String x) {  
   char ch[];
   ch = x.toCharArray();
   int xlength = x.length();

   int i, sum;
   for (sum=0, i=0; i < x.length(); i++)
     sum += ch[i];
   return sum % SIZE;
 }
  
  
  // The following three methods provide a way of traversing
  // all entries in the table
  
  // Resets the pointers to the first valid spot in the hash table
  // Takes no parameters and returns void
  public void reset()
  {
    index = 0;
    if (T[0] != null)
    {
      index = 0;
    }
    else   // if T[0] == null, keep increasing index
    {
      while ((index < T.length) && (T[index] == null)) // increment index when it is a valid index the node is null
      {
        index++;
        db("index: " + index);
      }
    }
    
    if (index >= T.length)   // to prevent out of bounds error
    {                        
      p = T[0];              
    }                   
    else                  
    {                       
      p = T[index];   // sets pointer to a valid spot in the array
    }                       
  }
  
  // Returns true if there is a next value in the hash table
  // Takes no parameters and returns a boolean value
  public boolean hasNext()
  {
    db("index: " + index);
    db("p: " + p);
    if ((index >= T.length-1) && (p == null))
    {
      return false;
    }
     return true;
  }
  
  // Returns the Node that p is currently on, and moves p to next value
  // Takes no parameters
  public Node next()
  {
    db("index: " + index);

    Node returnVal = new Node("");  // create a Node variable to return in the end
    
    // while the hash table has more values, move the pointer to the next value
    if (hasNext()==true)
    {
      db("hasNext() == true");
      returnVal = p;     // save the value we want to return in the end
      
      if (p.next != null)   // if there is a next value in the same linked list
      {
        db("p.next != null");
        p = p.next;
      }
      else if ((p.next == null) && (index < T.length-1))  // if p is on the last node in the linked list and
      {                                                   // the index is not in the last spot in the array
        
        db("p.next == null");
        index++;
        
        while ((index < T.length) && (T[index] == null))
        {
          db("INSIDE OF WHILE LOOP!!!!!!!!!!!!!!");
          db("index: " + index);
          index++;
        }
        
        if (index == T.length)
        {
          p = null;
        }
        else
        {
          p = T[index];
        }
        
      }
      else if ((p.next == null) && (index == T.length-1))
      {
        p = null;
      }
    }
    
    return returnVal;
  } // end next()
  
  
  // Insert a term and increase the termFreq for it's document number
  // Takes a String and int as parameters and returns void
  public void insert(String term, int docNum)
  {
    Node q = T[hash(term)];
    
    if (q == null)    // term is not present yet, so create a new Node
    {
      T[hash(term)] = new Node(term);
      T[hash(term)].termFreq[docNum] = 1;
    }
    else if (q != null)  // the linked list it should be in is not null
    {
      if (isMember(q, term) == false)   // term isn't in list, so add to end of linked list
      {
        db("isMember is false");
//        System.out.println(isMember(q, term));  // for debugging
        
        while (q.next != null)
        {
          q = q.next;
        }
        
        q.next = new Node(term);       
        q.next.termFreq[docNum] = 1;
        
      }
      else if (isMember(q, term) == true) // if term is in list, increment it's term frequency
      {
        db("isMember is true");
        
        if (q.next == null)   // term is first Node in linked list
        {
          q.termFreq[docNum]++;
        }
        else if ((q.term).equals(term))
        {
          q.termFreq[docNum]++;
        }
        
        while ((q.next != null)) // term is not first in linked list, but it is somewhere along the linked list
        {
          if (!((q.next.term).equals(term)))  // q is not at the term yet; keep chaining along
          {
            q = q.next;
          }
          else if (((q.next.term).equals(term)))  // q is at the term's node; increment term frequency
          {
            q.next.termFreq[docNum]++; 
            q = q.next;
          }
        }
      }
    } 
  } // end of insert(String term, int docNum)
  
  // Returns true if a term is in a specified linked list
  // Takes a Node and String as parameters, returns a boolean value
  public static boolean isMember(Node p, String term)
  {
    db(term + " is a member");
    
    boolean returnVal = false;
    boolean yesNo = false;
    
    
    if (p == null)  // if the node is null, the term is not in the list
    {
      returnVal = false;
    }
    else   // if the node is a linked list, chain down the list
    {
      for (;p != null; p = p.next)
      {
        if ((p.term).equals(term))
        {
          yesNo = true;
        }
      }
      
      if (yesNo == true)
      {
        returnVal = true;
      }
      else if (yesNo == false)
      {
        returnVal = false;
      }
      
    }
    return returnVal;
  }
  
  // Return cosine similarity of terms for the two documents stored in the table
  // Takes no parameters and returns a double
  public double cosineSimilarity()
  {    
    int dotProduct = 0;  //  calculate the dot product  
    reset();
    while(hasNext()) {
      Node a = next(); 
      dotProduct = dotProduct + (a.termFreq[0] * a.termFreq[1]);
    }
    db("dot product (numerator): " + dotProduct);
    
    int docZeroSquaredSum = 0;   // square all termFreq's in Document 0 and add them all together
    reset();
    while(hasNext()) {
      Node a = next(); 
      docZeroSquaredSum = docZeroSquaredSum + (a.termFreq[0] * a.termFreq[0]);
    }
    db("first term of denominator squared: " + docZeroSquaredSum);
    
    int docOneSquaredSum = 0;   // sqare all termFreq's in Document 1 and add them all together
    reset();
    while(hasNext()) {
      Node a = next(); 
      docOneSquaredSum = docOneSquaredSum + (a.termFreq[1] * a.termFreq[1]);
    }
    db("second term of denominator squared: " + docOneSquaredSum);
    
    double cosSimilarity = 0;   // calculate cosine similarity
    cosSimilarity = (double)dotProduct /
      (Math.sqrt((double)docZeroSquaredSum) * Math.sqrt((double)docOneSquaredSum));

    return cosSimilarity;   // return cosine similarity
  }
  

  // unit test!! :)
  public static void main(String args[])
  {
    TermFrequencyTable A = new TermFrequencyTable();
    
    System.out.println("Inserting:  Doc 0: \"cynthia cynthia chan is having so so so much fun in school\"");
    System.out.println("            Doc 1: \"cynthia chan is so in love with school * *\"");
    System.out.println("Hash values and term frequencies SHOULD be:");
    System.out.println("  [" + A.hash("cynthia") + "]  \"cynthia\": {2, 1}");
    System.out.println("  [" + A.hash("chan") + "]  \"chan\": {1, 1}");
    System.out.println("  [" + A.hash("is") + "]  \"is\": {1, 1}");
    System.out.println("  [" + A.hash("having") + "]  \"having\": {1, 0}");
    System.out.println("  [" + A.hash("so") + "]  \"so\": {3, 1}");
    System.out.println("  [" + A.hash("much") + "]  \"much\": {1, 0}");
    System.out.println("  [" + A.hash("fun") + "]  \"fun\": {1, 0}");
    System.out.println("  [" + A.hash("in") + "]  \"in\": {1, 1}");
    System.out.println("  [" + A.hash("school") + "]  \"school\": {1, 1}");
    System.out.println("  [" + A.hash("love") + "]  \"love\": {0, 1}");
    System.out.println("  [" + A.hash("with") + "]  \"with\": {0, 1}");
    System.out.println("  [" + A.hash("*") + "]  \"*\": {0, 2}");
    
    // insertions: each word gets inserted individually
    A.insert("cynthia", 0);
    A.insert("cynthia", 0);
    A.insert("chan", 0);
    A.insert("is", 0);
    A.insert("having", 0);
    A.insert("so", 0);
    A.insert("so", 0);
    A.insert("so", 0);
    A.insert("much", 0);
    A.insert("fun", 0);
    A.insert("in", 0);
    A.insert("school", 0);
    A.insert("cynthia", 1);   // ERROR HERE???????
    A.insert("chan", 1);
    A.insert("is", 1);        // ERROR HERE
    A.insert("so", 1);
    A.insert("in", 1);
    A.insert("love", 1);
    A.insert("with", 1);
    A.insert("school", 1);
    A.insert("*", 1);
    A.insert("*", 1);
  
    System.out.println();
    
    System.out.println("hash table:");
    A.printHash();
    System.out.println();
    
    System.out.println("print out hash table word by word");
    A.reset();
    while(A.hasNext()) {
      Node a = A.next(); 
      System.out.println("  \"" + a.term + "\" {" + a.termFreq[0] + ", " + a.termFreq[1] + "}");
    }
    System.out.println();    
    
    System.out.println("cosine similarity between Doc 0 and Doc 1: " + A.cosineSimilarity());
    
    
    // when two documents are the same
    System.out.println("\n=================================================================\n");
    System.out.println("New term frequency table:\n");
    TermFrequencyTable B = new TermFrequencyTable();
    
    System.out.println("Inserting:  Doc 1: \"an apple a day keeps the doctor away\"");
    System.out.println("            Doc 2: \"an apple a day keeps the doctor away\"");
    System.out.println("Hash values and term frequencies SHOULD be:");
    System.out.println("  [" + B.hash("an") + "]  \"an\": {1, 1}");
    System.out.println("  [" + B.hash("apple") + "]  \"apple\": {1, 1}");
    System.out.println("  [" + B.hash("a") + "]  \"a\": {1, 1}");
    System.out.println("  [" + B.hash("day") + "]  \"day\": {1, 1}");
    System.out.println("  [" + B.hash("keeps") + "]  \"keeps\": {1, 1}");
    System.out.println("  [" + B.hash("the") + "]  \"the\": {1, 1}");
    System.out.println("  [" + B.hash("doctor") + "]  \"doctor\": {1, 1}");
    System.out.println("  [" + B.hash("away") + "]  \"away\": {1, 1}");
    
    // inserting
    B.insert("an", 0);      
    B.insert("apple", 0);
    B.insert("a", 0);
    B.insert("day", 0);
    B.insert("keeps", 0);     
    B.insert("the", 0);      
    B.insert("doctor", 0);
    B.insert("away", 0);
    B.insert("an", 1);     
    B.insert("apple", 1);
    B.insert("a", 1);
    B.insert("day", 1);
    B.insert("keeps", 1);    
    B.insert("the", 1);      
    B.insert("doctor", 1);
    B.insert("away", 1);
    
    System.out.println();
    
    System.out.println("hash table:");
    B.printHash();
    System.out.println();
    
    System.out.println("print out hash table word by word");
    B.reset();
    while(B.hasNext()) {
      Node b = B.next(); 
      System.out.println("  \"" + b.term + "\" {" + b.termFreq[0] + ", " + b.termFreq[1] + "}");
    }
    System.out.println();    
    
    System.out.println("cosine similarity between Doc 0 and Doc 1 (should be 1): " + B.cosineSimilarity());

    
    
  }
  
}