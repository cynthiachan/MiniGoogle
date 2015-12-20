/*
 * ArticleTable.java
 * 
 * Inserts into, looks up, and deletes Nodes from a 
 * hash table using separate chaining. Includes 
 * interator methods to iterate through the hash table.
 * Edited from DumbList.java
 *
 * Author: cynthia chan (ccynthia@bu.edu)
 * Date: 4/29/15
 */

public class ArticleTable {
  
  // debugging: (from Prof. Snyder)
  private static boolean debug = false;           // set this to true if you want to trace your execution
  
  private static void db(String s) {            
    if(debug) 
      System.out.println(s);
  }
  
  // print list for debugging (from Prof. Snyder)
  private static void printList(Node r) {            
    for( ; r != null; r = r.next)
      System.out.print(r.data.getTitle() + " -> ");
    System.out.println("null"); 
  }
  
  // for debugging: print hash table
  private void printHash()
  {
    for (int i = 0; i < T.length; i++)
    {
      System.out.print("  [" + i + "]:\t");
      printList(T[i]);
    }
  }
  

  // Node class
   public static class Node {
      public Article data;
      public Node next;
      
      public Node(Article data, Node n) {
         this.data = data;
         this.next = n;
      }
      
      public Node(Article data) {
         this(data, null);
      }
   } // end Node class
   
   
   // global variables to interate through hash table
  private int index;    // index of array
  private Node p;       // pointer to first node in linked list
  
  // initialize the hash table
  private final int SIZE = 2521; // prime number around 2500 // use SIZE = 11 for unit testing
  private Node [] T = new Node[SIZE];

  // hash function: takes a String as a parameter, returns an int
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
    else // if T[0] == null, keep increasing index
    {
      while ((index < T.length) && (T[index] == null) ) // increment index when it is a valid index the node is null
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
  public Article next()
  {
    db("index: " + index);

    Article returnVal = new Article("",""); // create an Article variable to return in the end
    
    // while the hash table has more values, move the pointer to the next value
    if (hasNext()==true)
    {
      db("hasNext() == true");
      returnVal = p.data;   // save the value we want to return in the end
      
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
          db("INSIDE OF WHILE LOOP");
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
   
   public void initialize(Article[] A) {
      for(int i = 0; i < A.length; ++i) 
        insert(A[i]);
   }
  
   
   /******************** INSERT *******************/
  
  // Insert a into the table using the title of a as the hash key  
  // Takes an article as a parameter, returns void
  public void insert(Article a)
  {
    if (lookup(a.getTitle())!= null)     // avoid duplicates
    {
      System.out.println("Article is already in database and will not be added!");
    }
    else
    {
      Node q = T[hash(a.getTitle())];  // use hash key
      if (q == null)
      {
        T[hash(a.getTitle())] = new Node(a);
        db("q is null");
      }
      else
      {
        db("q is not null");
        
        while (q.next != null)
        {
          q = q.next;
        }
        
        q.next = new Node(a);
      }
    }
  }

   /******************** end insert ************************/
   
   
   /******************** LOOKUP *********************/
  
  // Returns the article with the given title or null if not found  
  // Takes a String as a parameter, returns an Article
  public Article lookup(String title)
  {
    db("in lookup method");
    int hashVal = hash(title);
    Node q = T[hash(title)];
    db("hash value is: " + hashVal);
    db("Printing node q: \t");
//    printList(q); // debugging
    
    // Search through the index of the String's hash value
    return lookupHelper(title, q);
  }
  
  // lookup helper method (recursive)
  // Takes a String and a Node as inputs, returns an Article
  private Article lookupHelper(String title, Node q)
  {
    db("Printing node q: \t");
  //  printList(q);
    db("in lookupHelper");
    
    if (q == null)  // if q is null, the article does not exist
    {
      db("base case 1: value is not in table");
      return null;
    }
    else if (title.equals(q.data.getTitle()))  // if title equals the title at the current node,
    {                                          // return the current node
      db("base case 2: value is found");
      return q.data;
    }
    else     // title still has not been found, recursively search the rest of the linked list
    {
      db("Recursive call");
      return lookupHelper(title, q.next);
    }
  }
      
  /********************** end lookup ******************/
   
   
  /*********************** DELETE **********************/

  // Deletes the article with the specified title
  // Takes a String as a paremeters, returns void
  public void delete(String title)
  {
    T[hash(title)] = deleteHelper(title, T[hash(title)]);
  }
  
  // delete helper method:
  // edited from Prof. Snyder's delete method from Recursion and Linked Lists notes
  // Deletes Node with specified title from a linked list q
  // Takes a String and a Node as parameters, returns a Node
  private Node deleteHelper(String title, Node q)
  {
    db("deleteHelper");
    
    if (q==null)
    {
      return q;
    }
    else if (title.equals(q.data.getTitle()))
    {
      return q.next;
    }
    else
    {
      q.next = deleteHelper(title, q.next);
      return q;
    }
  }
   
   /********************* end delete **************************/
   
 
  // Unit test!! :)
  public static void main(String args[])
  {
    ArticleTable A = new ArticleTable();
    
    String title = "Hello";
    String body = "Example of use:\n" +
      "Person A: Hello, how are you?\n" +
      "Person B: Hello. What is your name?\n" +
      "Person A: Oliver.";
    Article hello = new Article(title, body);

    title = "H-A-T-E";
    body = "H IS FOR THE WAY YOU HATE AT ME, \nA IS YOU'RE ARROGANT,\n" + 
      "T IS VERY VERY, TOXIC, \nE IS EVEN MORE THAN ANYONE THAT YOU HATE";
    Article hate = new Article(title, body);
    
    title = "Apples";
    body = "An apple a day keeps the doctor away :)\n" +
      "Fast facts!\n" +
      "- A medium apple is about 80 calories\n" +
      "- 2,500 varieties of apples are grown in the United States\n" +
      "- 7,500 vairities of apples are grown throughout the world\n" +
      "- 100 varieties of apples are grown comercially in the United States\n" +
      "- Apple trees take four to five years to produce their own fruit\n" +
      "- A bushel of apples weighs about 52 pounds";
    Article apples = new Article(title, body);
    
    title = "CS112";
    body = "A painful class.";
    Article cs112 = new Article(title, body);
    
    title = "Cynthia Chan";
    body = "An awesome person!";
    Article cynthia = new Article(title, body);
    
    title = "Stop";
    body = "STOP THAT NOW";
    Article stop = new Article(title, body);
    
    System.out.println("INSERTING ARTICLES (6 articles): \"Hello\", \"H-A-T-E\", \"Apples\", \"CS112\", \"Cynthia\", \"Stop\"\n"); 
    System.out.println("Inserting Hello...");
    A.insert(hello);
    System.out.println("Inserting H-A-T-E...");
    A.insert(hate);
    System.out.println("Inserting Apples...");
    A.insert(apples);
    // check if node is null (should be null)
    if (A.T[1] != null)
    {
      System.out.println("   T[1] is not null");
    }
    else if (A.T[1] == null)
    {
      System.out.println("   T[1] is null");
    }    
    System.out.println("Inserting CS112...");
    A.insert(cs112);
    // check if node is null (shouldn't be null)
    if (A.T[1] != null)
    {
      System.out.println("   T[1] is not null");
    }
    else if (A.T[1] == null)
    {
      System.out.println("   T[1] is null");
    }    
    System.out.println("Inserting Cynthia Chan...");
    A.insert(cynthia);
    System.out.println("Inserting Stop...");
    A.insert(stop);

    System.out.println("\nHash values:");
    System.out.println("Hash \"Hello\": " + A.hash(hello.getTitle()));
    System.out.println("Hash \"H-A-T-E\": " + A.hash(hate.getTitle()));
    System.out.println("Hash \"Apples\": " + A.hash(apples.getTitle()));
    System.out.println("Hash \"CS112\": " + A.hash(cs112.getTitle()));
    System.out.println("Hash \"Cynthia Chan\": " + A.hash(cynthia.getTitle()));
    System.out.println("Hash \"Stop\": " + A.hash(stop.getTitle()));
    System.out.println();
    
    System.out.println("Hash table SHOULD be:");
    System.out.println("[0]:  null");
    System.out.println("[1]:  \"CS112\" -> null");  
    System.out.println("[2]:  null");  
    System.out.println("[3]:  null");  
    System.out.println("[4]:  \"Cynthia Chan\" -> \"Stop\" -> null");  
    System.out.println("[5]:  \"Hello\" -> \"H-A-T-E\" -> null");  
    System.out.println("[6]:  null"); 
    System.out.println("[7]:  null");  
    System.out.println("[8]:  \"Apples\" -> null");  
    System.out.println("[9]:  null");  
    System.out.println("[10]: null");  
    System.out.println();
    
    System.out.println("(1) Printing the hash table:");
    A.printHash();

    System.out.println("\n\nListing all articles (Using Prof. Snyder's while loop) (6 articles) \n");
    A.reset();
    while(A.hasNext()) {
      Article a = A.next(); 
      System.out.println(a);
    }
    System.out.println("Finished printing all articles! \n\n");
    
    System.out.println("(2) Printing the hash table (to make sure items in hash table were not affected):");
    A.printHash();
    
    System.out.println();
    
    System.out.println("TESTING LOOKUP:");
    System.out.println("Looking up \"CS112\"");
    System.out.println(A.lookup("CS112"));
    System.out.println("Looking up \"Hello\"");
    System.out.println(A.lookup("Hello"));    
    System.out.println("Looking up \"Tom Cruise\" (not in hash table: should be null)");
    System.out.println(A.lookup("Panda Express"));
    
    System.out.println("\nTESTING DELETE:");
    System.out.println("Deleteing \"Stop\"... (6 articles after this)");
    A.delete("Stop");
    System.out.println("Searching for \"Stop\" (should be null now)");
    System.out.println(A.lookup("Stop"));
        
    System.out.println("\n\n\nPrint out hash table (after deleting Stop, 6 articles)");
    A.printHash();
    
    System.out.println("\nDeleting \"What\"... (not in hash table: hash table should remain unchanged");
    A.delete("What");
    A.printHash();
    
    System.out.println("\n\nPrint out all articles again (6 articles) \n");
    A.reset();
    while(A.hasNext()) {
      Article a = A.next(); 
      System.out.println(a);
    }
    System.out.println("Done! \n\n");
    
    System.out.println("Hash table (make sure nothing weird has happened):");
    A.printHash();
    
    
    
    Article hello2 = new Article("Hello", "a duplicate article");
    System.out.println("\nInsert a duplicate article \"Hello\": (Should not get inserted again)");
    A.insert(hello2);
    A.printHash();
    
    
    System.out.println("\n\nPrinting out all articles  \n");
    A.reset();
    while(A.hasNext()) {
      Article a = A.next(); 
      System.out.println(a);
    }
    System.out.println("Done! \n\n");
    
    System.out.println("\nDeleting all articles...");
    A.delete("Hello");
    A.delete("H-A-T-E");
    A.delete("Apples");
    A.delete("CS112");
    A.delete("Cynthia Chan");
    A.delete("Stop");


    System.out.println("Hash table:");
    A.printHash();
    System.out.println("\nPrinting all articles (shouldn't print anything):");
    A.reset();
    while(A.hasNext()) {
      Article a = A.next(); 
      System.out.println(a);
    }
    System.out.println("Done!");
   
  }

}
