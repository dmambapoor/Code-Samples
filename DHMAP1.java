/*
 * DHMAP1.java
 *
 * The Trie class implements a spell checker system.
 *
 * The Trie is a tree. The Trie is made up of Nodes representing each letter in a given word. Each node has a max of 26 branches. (One for each letter)
 * Each node/letter contains a size 26 array containing each subsequent letter. If a subsuquent letter exists, then its placed in its corresponding box. (A placed in 0, B placed in 1, C placed in 2, and so on.)
 * Each node/letter has a status called terminal. If it is terminal, it is the end of the letter. ("Apple" - 'e' would be terminal.)
 *
 * The main() function takes in single-letter Uppercase commands followed by all-lowercase parameters.
 * Requires being fed the entire dictionary on start to operate as a conventional spell-checker. It, however, excels at custom lists, because any list can be fed to it.
 * e.g. "T appke" will return "Spelling mistake appke"
 *
 *  Updated: 12/23/2019
 *      Author: Dhruva Mambapoor
 */

import java.util.Scanner;

public class DHMAP1 {
	public static class Trie{
		/* NODE CLASS - represents a single letter in a word*/
		public class Node{
			boolean terminal;   //Is this the letter the last letter of a word?
			int outDegree;      //Count of possible unique next letters (e.g. 2/app -> appl OR apps)
			Node[] children;    //Links to other letters in other possible words
			
            //Constructor
			public Node() {
				terminal = false; //Initially false because more letters are not terminal letters
				outDegree = 0;
				children = new Node[26];    //Size 26 empty array for each letter of the alphabet
			}
		}
		
		/* CLASS VARIABLES */
		Node root;
		
		/* CONSTRUCTOR */
		public Trie() {
			root = new Node();
		}
		
		/* MEMBER FUNCTIONS */
		
		//isPresent - checks if a word is within the data structure, returns true if it is
		public boolean isPresent(String s) {
			return isPresent(root, s);
		}
        //recursive function for isPresent
		private boolean isPresent(Node x, String s) {
            //Base Case - If we reach a node looking for a letter but it's not there - then word is not present
			if(x == null)
			{
				return false;
			}
            //Base Case - If last letter is terminal (our structure says its the end of a word), the word is present
			if(s.isEmpty())
			{
				if(x.terminal)
					return true;
				return false;
			}
            
            //Recursive call to look for next letter in sequence
			return isPresent(x.children[s.charAt(0) - 97], s.substring(1));
		}
		
		//insert - inserts a word into the data structure, returns false if already present
		public boolean insert(String s) {
			//checks if word is already present
            if(isPresent(s) == true)
			{
				return false;
			}
			
            //call to recursive function
			return insertR(root, s);
		}
        //recursive function for insert
		private boolean insertR(Node x, String s) {
            //Base Case - if we've added all the letters, return true
			if(s.isEmpty()) {
				x.terminal = true;
				return true; 
			}
			int i = s.charAt(0) - 97;   //format the letter
			if(x.children[i] == null)   //if letter isn't in structure, make a new node as the letter
			{
				x.children[i] = new Node();
                x.outDegree += 1;
			}
            
            //recursive call - add next letter in the sequence
			return insertR(x.children[i], s.substring(1));
		}
		
		//delete - deletes specified word, leaving other words intact. returns false if word isn't present.
		public boolean delete(String s){
			if(isPresent(s))    //checks if already present, if not it deletes.
			{
				delete(root, s);    //call to recursive delete
                return true;
			}
			return false;
		}
        //recursive function for delete
		private boolean delete(Node x, String s)
		{
            //Deletes from bottom up. ( apple -> appl -> app //end bc "app" is another word)
            if(s.isEmpty()) //Checks if we're at the end of the string
            {
                //Returns true if word is part of another word. (e.g. app in apple)
                if(x.outDegree > 0)
                {
                    x.terminal = false;     //If word is part of another word, then we simply indicate that the last letter of deleted word, is no longer the last letter of any word
                    return true;
                }
                else
                {
                    return false;   //If word isn't part of another word, we return that information up
                }
            }
            
            //If current substring isn't another word, deletes the current letter
            int i = s.charAt(0)-97;
            if(delete(x.children[i], s.substring(1)) == false)
            {
                x.children[i] = null;
                x.outDegree += -1;
                if(x.outDegree == 0 && x.terminal == false)
                {
                    return false;
                }
            }
            return true;
		}
        
        //membership\count - returns how many words are within the structure (it would be easier to keep track as we go, but this is done as an excercise to optimally search the tree)
        public int membership(){
            return membership(root); //call to recursive funciton
        }
        //recursive function for membership - sums up all "terminal" nodes, and trickles up the tree
        private int membership(Node x){
            //Base Case - tree is empty, or we are at end of structure
            if(x == null)
            {
                return 0;
            }
            
            int sum = 0;    //rolling track of numbers
            for(int i = 0; i < 26; i++) //follow each branch down if branch is present
            {
                if(x.children[i] != null)
                {
                    sum += membership(x.children[i]); //rolling count of words
                }
            }
            
            //if its the end of a word, +1 to the sum, if not, return current count for that branch
            if(x.terminal == true)
            {
                return 1 + sum;
            }
            else
            {
                return sum;
            }
        }
        
        //list all - lists every word in the structure
        public void listAll(){
            listAll(root, new String()); //call to recursive function
        }
        //recursive function for listAll
        private void listAll(Node x, String s){
            //Base Case - structure is empty or we are at the end of a branch
            if(x == null)
            {
                return;
            }
            if(x.terminal == true) //if its the end of a word, print current string
            {
                System.out.println(s);
            }
            for(int i = 0; i < 26; i++)
            {
                if(x.children[i] != null)
                {
                    listAll(x.children[i], (s + (char)(i+97))); //recursive call, passing in current path as a string
                }
            }
            
            
        
        }
        
        //test - tests the function using popular name "Bob" and popular drink "boba"
        public boolean test()
        {
            System.out.println(insert("boba"));     //true
            System.out.println(insert("bob"));      //true
            System.out.println(insert("boba"));     //false
            System.out.println(membership());       //2
            listAll();
            System.out.println(isPresent("bob"));   //true
            System.out.println(isPresent("boba"));  //true
            System.out.println(delete("boba"));     //true
            System.out.println(isPresent("boba"));  //false
            System.out.println(isPresent("bob"));   //true
            System.out.println(membership());       //1
            return true;
        }
	}
	
    //Starts the menu - built for passing in a file with list of commands
    //***FULL MENU LIST
    //Insert - A
    //Delete - D
    //* Exit - E *
    //List All - L
    //Membership - M
    //Search - S
    //Spelling Check - T
    //*** END LIST
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		Trie tree = new Trie();
        
        //While commands exist, take them in and process
		while(input.hasNext()) {
			String cmd = input.next();
            
            //Insert - A
            if(cmd.equals("A"))
            {
                String data = input.next();
                if(tree.insert(data)) { System.out.println(data + " inserted"); }
                else { System.out.println(data + " already exists"); }
                
                
            }
            
            //Delete - D
            if(cmd.equals("D"))
            {
                String data = input.next();
                if(tree.delete(data)) { System.out.println(data + " deleted"); }
                else { System.out.println(data + " not found"); }
            }
            
            //Membership - M
            if(cmd.equals("M"))
            {
                System.out.println("Membership is " + tree.membership());
            }
            
            //Search - S
            if(cmd.equals("S"))
            {
                String data = input.next();
                if(tree.isPresent(data))
                {
                    System.out.println(data + " found");
                }
                else
                {
                    System.out.println(data + " not found");
                }
            }
            
            //Spelling Check - T
            if(cmd.equals("T"))
            {
                String words = input.nextLine();
                words = words.concat(" END");
                String[] data = words.split(" ");
                int i = 1;
                while(!(data[i].equals("END")))
                {
                    if(tree.isPresent(data[i]) == false)
                    {
                        System.out.println("Spelling mistake " + data[i]);
                    }
                    i++;
                }
            }
            
            //List All - L
            if(cmd.equals("L"))
            {
                tree.listAll();
            }
            
            //Exit - E
            if(cmd.equals("E"))
            {
                break;
            }
		}
	}
}


