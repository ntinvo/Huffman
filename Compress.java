/**  
		Tin Vo
 **/
import java.util.TreeMap;
import java.io.IOException;
public class Compress {
    // instance variables
    private TreeMap<Integer, String> map;
    private TreeNode huffmanTree;
    private int[] counter;
    private int numBits;
    
    // constructor for Compress class
    public Compress(TreeNode huffmanTree, TreeMap<Integer, String> map, int[] counter) {
        this.huffmanTree = huffmanTree;
        this.map = map;
        this.counter = counter;
        numBits = 0;
    }
    
    /** 
     * write Standard Count Format
     * Pre: none
     * Post: write to file Standard Count Format
     */
    public void writeSCF(BitInputStream bitIn, BitOutputStream bitOut) throws IOException {
        int BITS_PER_INT = IHuffConstants.BITS_PER_INT;
        // write magic number and header
        bitOut.writeBits(BITS_PER_INT, IHuffConstants.MAGIC_NUMBER);
        bitOut.writeBits(BITS_PER_INT, IHuffConstants.STORE_COUNTS);
        numBits += 2 * BITS_PER_INT;
        // header info
        for(int i = 0; i < IHuffConstants.ALPH_SIZE; i++) {
            bitOut.writeBits(BITS_PER_INT, counter[i]);
            numBits += BITS_PER_INT;
        }        
        // write compressed data
        writeData(bitIn, bitOut);
        // write PSEUDO_EOF
        writePSEUDO_EOF(bitOut);
        bitOut.close();
    }
    
    /**
     * write Standard Tree Format 
     * Pre: none
     * Post: write to file Standard Tree Format
     */    
    public void writeSTF(BitInputStream bitIn, BitOutputStream bitOut) throws IOException {
        int BITS_PER_INT = IHuffConstants.BITS_PER_INT;
        // write magic number and header
        bitOut.writeBits(BITS_PER_INT, IHuffConstants.MAGIC_NUMBER);
        bitOut.writeBits(BITS_PER_INT, IHuffConstants.STORE_TREE);
        // write the size of the tree      
        bitOut.writeBits(BITS_PER_INT, treeSize(huffmanTree, 0));
        numBits += 3 * BITS_PER_INT;
        // write the tree
        writeTree(huffmanTree, bitOut);   
        // write compressed data
        writeData(bitIn, bitOut);
        // write PSEUDO_EOF
        writePSEUDO_EOF(bitOut);
        bitOut.close();
        bitIn.close();
    }
    
    /**
     * return the num bits written
     * Pre: none
     * Post: return num bits written to the file
     */
    public int getWrittenBits() {
        return numBits;
    }
    
    // write data helper method
    private void writeData(BitInputStream bitIn, BitOutputStream bitOut) throws IOException{
        int key = bitIn.read();
        while(key != -1) {
            String curr = map.get(key);
            for(int i = 0; i < curr.length(); i++) {
                if(curr.charAt(i) == '1')
                    bitOut.writeBits(1, 1);
                else
                    bitOut.writeBits(1, 0);
                numBits += 1;
            }
            key = bitIn.read();
        }        
    }    
    
    // write PSEUDO_EOF helper method
    private void writePSEUDO_EOF(BitOutputStream bitOut) throws IOException {
        String PSEUDO_EOF = map.get(IHuffConstants.PSEUDO_EOF);
        for(int i = 0; i < PSEUDO_EOF.length(); i++) {
            if(PSEUDO_EOF.charAt(i) == '1')
                bitOut.writeBits(1, 1);
            else 
                bitOut.writeBits(1, 0);
            numBits += 1;
        }
    }
    
    // write the Tree helper method
    private void writeTree(TreeNode currentNode, BitOutputStream bitOut) {
        if(currentNode.isLeaf()) {
            bitOut.writeBits(1 , 1);
            bitOut.writeBits(IHuffConstants.BITS_PER_WORD + 1, currentNode.getValue());
            numBits += IHuffConstants.BITS_PER_WORD + 2;
        } else {
            if(currentNode != null) {
                bitOut.writeBits(1, 0);
                numBits += 1;
                writeTree(currentNode.getLeft(), bitOut);
                writeTree(currentNode.getRight(), bitOut);
            }
        }
    }
     
    // helper method return the size of the Tree
    private int treeSize(TreeNode currentNode, int count) {
        if(currentNode.isLeaf()) {
            count += IHuffConstants.BITS_PER_WORD + 2;
            return count;
        } else {
            if(currentNode != null) {
                count += 1 + treeSize(currentNode.getLeft(), count) + treeSize(currentNode.getRight(), count);
            }
            return count;
        }        
    }    
}