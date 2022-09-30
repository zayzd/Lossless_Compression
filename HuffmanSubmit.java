import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;




public class HuffmanSubmit implements Huffman {
	
	static String s;
	static int[] ft;
	
	
	
	// Feel free to add more methods and variables as required.
	
	 public static int[] frequency(String s) {
		 //create an int array with a size of 256 to account for all ascii 
		 int[] frequency = new int[256];
		 //for each unique character, increment by one each time it is in the string
		 for(char character:s.toCharArray()) {
			 frequency[character]++;
		 }
		 return frequency;
		 
	 }
	 

	 
	 public static class Node implements Comparable<Node>{
			//create a node with char, frequency, and children
			char character;
			int frequency;
			Node leftChild;
			Node rightChild;
			//assign to node
			Node(char character, int frequency,Node leftChild,Node rightChild){
				this.character = character;
				this.frequency = frequency;
				this.leftChild = leftChild;
				this.rightChild = rightChild;
			}
				boolean isLeaf() {
					//check if a node is a leaf by checking if the children are null
					Boolean val  = leftChild == null && rightChild == null;

					return val;
				}
				@Override
				public int compareTo(Node n) {
					//allow the nodes to be compared 
					//freqComp is the comparison of two nodes
					int freqComp = Integer.compare(frequency, n.frequency);
					//if they are not equal, return freqComp
					if(freqComp!=0) {
						return freqComp;
					}
					//return the value of the comparison
					return freqComp;				
				}
				
			}
		 
	 public static Node buildHuffman(int[]a) {
		 //creates a new PriorityQueue
		PriorityQueue<Node> pq = new PriorityQueue();
		for(int i = 0; i<a.length;i++) {
			//when the frequency value is not 0
			//avoids adding extra characters that have a 0 value
			if(a[i]>0) {
				//i acts as the character, as an int can be converted into a char
				//the value of i is the frequency, and it holds null children
				pq.add(new Node((char)i, a[i],null,null));
			}
		}
		while(pq.size()>1) {
			//poll 2 elements, make them children of a node with an empty char value and its frequency is
			//the sum of the node left and node right's frequency values
			Node left = pq.peek();
			pq.poll();
			Node right = pq.peek();
			pq.poll();			
			Node parent = new Node('\0',left.frequency+right.frequency,left,right);
			//add the parent node back to the priorityqueue
			pq.add(parent);
			}
			//return root
		return pq.poll();
		
	    }	   
	 
	 
	    public static void code(Node n, String s, Map<Character,String> map) {
	    	if(n==null)
	    		return;
	    	if(!(n.isLeaf())) {
	    		//while traversing the tree, if the node is left, add 0 to the string
	    		code(n.leftChild,s+'0',map);
	    		//else add a one
				code(n.rightChild,s+'1',map);
	    	}else{
	    		//if node isnt a leaf, put it into the table
	    		map.put(n.character, s);
	    	}

	    }
	   
	    
	  public static void writeToFreq(String inputFile) throws IOException {
		  FileWriter writer = new FileWriter("FrequencyFile.txt");
		  for(int i = 32;i<256;i++) {
			  if(ft[i]>0)
	 			//take the ft (frequencyTable) array, and write it to the FrequencyFile
	 			//convert the i value to a binary 
	 			//write it to file with file writer
				  	writer.write(Integer.toBinaryString(i)+": "+ft[i]+" \n");
	 			}
	 			writer.close();
	   }
	    

	    
	    //standard method to print array
	    //used during testing
	    public static void printArray(int[]a) {
	 	   for(int i =0; i<a.length;i++) {
	 		   if(a[i]>0)
	 		  System.out.print(a[i]+" ");
	 	   }
	    }

	    public static Map<Character,String> huffmanStuff(Node n){
	    	//created a hashmap
			Map<Character,String> table = new HashMap();
			//runs the node and table through the code method and return the table
			code(n,"",table);
			return  table;
			
		}
	    
	   
 
	public void encode(String inputFile, String outputFile, String freqFile){
		//make use of the binary bin method
		BinaryIn in = new BinaryIn(inputFile);
		String s = in.readString();
		
		  
		//make a frequency table based off the string
	   		ft = frequency(s);
	   		System.out.println();
	      
	   		try {
				writeToFreq(s);
				 Node n = buildHuffman(ft);
			      Map<Character,String> table = huffmanStuff(n);
			      //prints out the hashmap for refernece
				   System.out.println("Characters and huffman code = ");

			      System.out.print(table);
				   System.out.println();
//			   	  String hashString = mapToString(table);	  	  
			   	   HuffmanSubmit hs = new HuffmanSubmit();
			   	   FileWriter fw = new FileWriter(outputFile);
			   	   //calls convertedMessage method to convert the chars to their respective binary representation
				   String converted = convertedMessage(s);
				   System.out.println("Converted Message = "+converted);
				  //write the message to the file
					fw.write(converted);
					fw.close();
				   System.out.println();
			   	   
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   }
	
	 public static void decode(String S, Node root, BinaryOut out) {
		 
		 out.write("Decoded Message = ");
		 //traverse the tree, moving left or right according to the way
		 //the encoded message is printed
		    Node currentRoot = root;
		    if (root == null) 
		    	return;
		    char[] a = S.toCharArray();
		    //if the char is 0, go left
		    for (int i = 0; i < a.length; i++) {
		      if (a[i] == '0') {
		    	  currentRoot = currentRoot.leftChild; 
		      } 
		      else {
		    	  //if char is 1, go right
		    	  currentRoot =currentRoot.rightChild;  
		      }
		      if (currentRoot.leftChild == null && currentRoot.rightChild == null) {
		    	  //use the binary write file to write the character to the file
		    	  out.write(currentRoot.character);
		    	  //reset
	              currentRoot = root;
		      }
		    }
	        out.close();
		  }
	 
	public static String convertedMessage(String s) {
		//assign the node to the huffman tree based on the frequency file
		 Node n = buildHuffman(ft);
		 //make the hashmap
	     Map<Character,String> table = huffmanStuff(n);
	     //encode the hashmap
	     code(n, "", table);
	     //replace the string with a char array and replace characters (which are keys)
	     //with its value
	     StringBuilder sb = new StringBuilder();
	        for (char c: s.toCharArray()) {
	            sb.append(table.get(c));

	        }
	        //turn to string and return
	        String convmessage = sb.toString();
	        return convmessage;

	}

   public void decode(String inputFile, String outputFile, String freqFile){
	   //create tree based on ft and assign to node
       Node n = buildHuffman(ft);
       //create binaryin and binaryout
	   BinaryIn in2 = new BinaryIn(inputFile);
	   BinaryOut out = new BinaryOut(outputFile);
	   String outputString = in2.readString();
	   //write decoded message to the output file
	   decode(outputString,n,out);
	   System.out.println();	  
}
   
  

   
   
   public static void main(String[] args) throws IOException{
	   
	// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
	// On linux and mac, you can use `diff' command to check if they are the same. 
	  Huffman  huffman = new HuffmanSubmit();
	  huffman.encode("input.txt", "output.enc","FrequencyFile.txt");
	  huffman.decode("output.enc", "inputdec.txt", "FrequencyFile.txt");
	  
	  huffman.encode("ur.jpg", "output.enc","FrequencyFile.txt");
	  huffman.decode("output.enc", "inputdec.jpg", "FrequencyFile.txt");


     }

       

       

       
       }
    


