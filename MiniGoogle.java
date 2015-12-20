/*
 * MiniGoogle.java
 *
 * A client program that uses the DatabaseIterator
 * and Article classes, along with additional data
 * structures, to allow a user to create, modify
 * and interact with a encyclopedia database.
 * Edited from Minipedia.
 *
 * Author: Alexander Breen (abreen@bu.edu) and Wayne Snyder (waysnyder@gmail.com)
 * Edited by: ccynthia@bu.edu (ccynthia@bu.edu)
 * Date: 4/29/15
 */

import java.util.*;


public class MiniGoogle {
  
  // debugging: (from Prof. Snyder)
  private static boolean debug = false;           // set this to true if you want to trace your execution
  
  private static void debugger(String s) {            
    if(debug) 
      System.out.println(s);
  }
  
  // BLACKLIST
  private static final String [] blackList = { "the", "of", "and", "a", "to", "in", "is", 
    "you", "that", "it", "he", "was", "for", "on", "are", "as", "with", 
    "his", "they", "i", "at", "be", "this", "have", "from", "or", "one", 
    "had", "by", "word", "but", "not", "what", "all", "were", "we", "when", 
    "your", "can", "said", "there", "use", "an", "each", "which", "she", 
    "do", "how", "their", "if", "will", "up", "other", "about", "out", "many", 
    "then", "them", "these", "so", "some", "her", "would", "make", "like", 
    "him", "into", "time", "has", "look", "two", "more", "write", "go", "see", 
    "number", "no", "way", "could", "people",  "my", "than", "first", "water", 
    "been", "call", "who", "oil", "its", "now", "find", "long", "down", "day", 
    "did", "get", "come", "made", "may", "part" }; 

  
  // Turn a string into all lowercase, with only whitespace, letters, and digits.
  // Takes a String as an input, and returns a String
  private static String preprocess(String s)
  {
    debugger("preprocess");
    
    s = s.toLowerCase();    // converts the String to all lowercase letters
    return s.replaceAll("[^A-Za-z0-9 \t]", "");
  }  

  // Returns true if String s is a member of the blacklist
  // Takes a String as a parameter, returns a boolean value
  private static boolean blacklisted(String s)
  {
    boolean returnVal = false;
    boolean yesNo = false;
    
    for (int i = 0; i < blackList.length; i++)
    {
      if (s.equals(blackList[i]))
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
    
    return returnVal;
  }
  
  // Calculate the cosine similarity between two Strings
  // Takes two Strings as parameters, and returns a double
  private static double getCosineSimilarity(String s, String t)
  {
    debugger("s before preprocess: " + s);
    debugger("t before preprocess: " + t + "\n");
       
    // create new TermFrequencyTable
    TermFrequencyTable X = new TermFrequencyTable();

    s = preprocess(s); //preprocess each string
    t = preprocess(t);
    debugger("s after preprocess: " + s);
    debugger("t after preprocess: " + t);
    
    // create a new StringTokenizer for each string
    StringTokenizer stringS = new StringTokenizer(s);
    StringTokenizer stringT = new StringTokenizer(t);
    
    String a = "";
    while(stringS.hasMoreTokens() == true) // insert all words of String s into TermFrequencyTable
    {
      a = stringS.nextToken();
      if (blacklisted(a) == false)   // only insert if it's not in the blacklist
      {
        X.insert(a, 0);
      }
    }
    while(stringT.hasMoreTokens() == true) // insert all words of String t into TermFrequencyTable
    {
      a = stringT.nextToken();
      if (blacklisted(a) == false)
      {
        X.insert(a, 1);
      }
    }
    
//    X.printHash(); // for debugging
    debugger("cosine Similarity: " + X.cosineSimilarity());
    
    return X.cosineSimilarity();    
  }

  
  
  private static Article[] getArticleList(DatabaseIterator db) {
    
    // count how many articles are in the directory
    int count = db.getNumArticles(); 
    
    // now create array
    Article[] list = new Article[count];
    for(int i = 0; i < count; ++i)
      list[i] = db.next();
    
    return list; 
  }
  
  private static DatabaseIterator setupDatabase(String path) {
    return new DatabaseIterator(path);
  }
  
  private static void addArticle(Scanner s, ArticleTable D) {
    System.out.println();
    System.out.println("Add an article");
    System.out.println("==============");
    
    System.out.print("Enter article title: ");
    String title = s.nextLine();
    
    System.out.println("You may now enter the body of the article.");
    System.out.println("Press return two times when you are done.");
    
    String body = "";
    String line = "";
    do {
      line = s.nextLine();
      body += line + "\n";
    } while (!line.equals(""));
    
    D.insert(new Article(title, body));
  }
  
  
  private static void removeArticle(Scanner s, ArticleTable D) {
    System.out.println();
    System.out.println("Remove an article");
    System.out.println("=================");
    
    System.out.print("Enter article title: ");
    String title = s.nextLine();
    
    D.delete(title);
  }
  
  // search the database by exact title
  private static void titleSearch(Scanner s, ArticleTable D) {
    System.out.println();
    System.out.println("Search by article title");
    System.out.println("=======================");
    
    System.out.print("Enter article title: ");
    String title = s.nextLine();
    
    Article a = D.lookup(title);
    if(a != null)
      System.out.println(a);
    else {
      System.out.println("Article not found!"); 
      return; 
    }
    
    System.out.println("Press return when finished reading.");
    s.nextLine();
  }

  // search the database by phrase or keyword
  private static void keywordSearch(Scanner s, ArticleTable D) {
    System.out.println();
    System.out.println("Search by phrase or keyword(s)");
    System.out.println("==============================");
    
    System.out.print("Enter phrase or keyword(s) to search for: ");
    String title = s.nextLine();
    
    MaxHeap m = new MaxHeap();
    
    D.reset();
    while(D.hasNext())    // iterate through hash table and insert articles with
    {                     // cosine similarity greater than 0
      Article a = D.next();
      double cosSimilarity = getCosineSimilarity(title, a.getBody());
      
      if (cosSimilarity > (double)0)
      {
        m.insert(a, cosSimilarity);
      }
    }
    
    // get top three articles from the heap
    // first result
    if (m.getNext() > 0)
    {
      MaxHeap.Node returnNode = m.getMax();
      System.out.println("[Result #1]     Cosine similarity: " + returnNode.cosSim + "\n");
      System.out.println(returnNode.data);
    }
    else // if there's no first result, print message
    {
      System.out.println("No articles found!\n");
    }
    // second result
    if (m.getNext() > 0)
    {
      MaxHeap.Node returnNode = m.getMax();
      System.out.println("[Result #2]     Cosine similarity: " + returnNode.cosSim + "\n");
      System.out.println(returnNode.data);
    }
    // thirt result
    if (m.getNext() > 0)
    {
      MaxHeap.Node returnNode = m.getMax();
      System.out.println("[Result #3]     Cosine similarity: " + returnNode.cosSim + "\n");
      System.out.println(returnNode.data);
    }
    
    System.out.println("Press return when finished reading.");
    s.nextLine();
  }
  
  public static void main(String[] args) {
    
    Scanner user = new Scanner(System.in);
    
    String dbPath = "articles/";
    
    DatabaseIterator db = setupDatabase(dbPath);
    
    System.out.println("Read " + db.getNumArticles() + 
                       " articles from disk.");
    
    ArticleTable L = new ArticleTable(); 
    Article[] A = getArticleList(db);
    L.initialize(A);
    
    int choice = -1;
    do {
      System.out.println();
      System.out.println("WELCOME TO MINIGOOGLE!");
      System.out.println("=====================");
      System.out.println("Make a selection from the " +
                         "following options:");
      System.out.println();
      System.out.println("Manipulating the database");
      System.out.println("-------------------------");
      System.out.println("    1. add a new article");
      System.out.println("    2. remove an article");
      System.out.println();
      System.out.println("Searching the database");
      System.out.println("----------------------");
      System.out.println("    3. search by exact article title");
      System.out.println("    4. search by phrase or keyword(s)");
      System.out.println();
      
      System.out.print("Enter a selection (1-4, or 0 to quit): ");
      
      choice = user.nextInt();
      user.nextLine();
      
      switch (choice) {
        case 0:
          return;
          
        case 1:
          addArticle(user, L);
          break;
          
        case 2:
          removeArticle(user, L);
          break;
          
        case 3:
          titleSearch(user, L);
          break;
          
        case 4:
          keywordSearch(user, L);
          break;
          
        default:
          break;
      }
      
      choice = -1;
      
    } while (choice < 0 || choice > 5);
    
  }
  
  
}
