/**  
		Tin Vo
 **/
import java.util.TreeMap;
import java.io.IOException;
public class Frequency {
    // instance variables
    private TreeMap<Integer, TreeNode> nodes;
    private int[] counter;
    
    // constructor for Frequency class
    public Frequency(BitInputStream bitIn, boolean compress) throws IOException {
        nodes = new TreeMap<Integer, TreeNode>();
        counter = new int[IHuffConstants.ALPH_SIZE];
        if(compress)
            compress(bitIn);
        else
            decompress(bitIn);
    }
    
    /**
     * method that creates an ArrayList of TreeNodes and their frequencies
     * Pre: none
     * Post: creates an ArrayList of TreeNodes and their frequencies
     */
    public void compress(BitInputStream bitIn) throws IOException{
        int current = bitIn.read();        
        while(current != -1) {
            if(nodes.containsKey(current)) {
                nodes.get(current).myWeight++;
                counter[current]++;
            }
            // found new node, add to the list
            else {
                nodes.put(current, new TreeNode(current, 1));
                counter[current] = 1;
            }
            current = bitIn.read();
        }
        // add PSEUDO_EOF
        nodes.put(Integer.MIN_VALUE, new TreeNode(IHuffConstants.PSEUDO_EOF, 1));
    }
    
    /**
     * method that creates ArrayList of TreeNode and frequencies(used in uncompress)
     * Pre: none
     * Post: creates ArrayList of TreeNode and frequencies(used in uncompress)
     */
    public void decompress(BitInputStream bitIn) throws IOException {
        for(int i = 0; i < IHuffConstants.ALPH_SIZE; i++) {
            int current = bitIn.readBits(IHuffConstants.BITS_PER_INT);
            counter[i] = current;
            if(current != 0)
                nodes.put(i, new TreeNode(i, counter[i]));
        }
        // add PSEUDO_EOF
        nodes.put(Integer.MIN_VALUE, new TreeNode(IHuffConstants.PSEUDO_EOF, 1));
    }
    
    /**
     * method that returns the HashMap of TreeNode
     * Pre: none
     * Post: returns the HashMap of TreeNode
     */
    public TreeMap<Integer, TreeNode> getNodes() {
        return nodes;
    }
    
    /** 
     * method that return the frequencies
     * Pre: none
     * Post: return the frequencies
     */
    public int[] getCount() {
        return counter;
    }
}