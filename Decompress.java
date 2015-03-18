/**  
		Tin Vo
 **/
import java.io.IOException;
public class Decompress {
    // instance variables
    private TreeNode huffmanTree;
    private int numBits;
    
    //constructor for Decompress class     
    public Decompress(TreeNode huffmanTree) {
        this.huffmanTree = huffmanTree;
        numBits = 0;
    }
   
    /** 
     * uncompress Standard Count Format
     * Pre: none
     * Post: uncompress and write data Standard Count Format
     */
    public void decompressSCF(BitInputStream bitIn, BitOutputStream bitOut) throws IOException {
        writeData(bitIn, bitOut, huffmanTree);
    }
    
    /** 
     * uncompress Standard Tree Format
     * Pre: none
     * Post: uncompress, write data Standard Tree Format and return new HuffmanTree
     */
    public TreeNode decompressSTF(BitInputStream bitIn, BitOutputStream bitOut) throws IOException {
        bitIn.readBits(IHuffConstants.BITS_PER_INT);        
        TreeNode newHuffmanTree = newHuffmanTree(bitIn);        
        writeData(bitIn, bitOut, newHuffmanTree);        
        return newHuffmanTree;
    }
    
    /**
     * returns num written bits
     * Pre: none
     * Post: return the num bits written
     */
    public int getWrittenBits() {
        return numBits;
    }
    
    // helper method recreate HuffmanTree
    private TreeNode newHuffmanTree(BitInputStream bitIn) throws IOException {
        int val = bitIn.readBits(1);
        if(val == 0) {
            TreeNode result = new TreeNode(-1, -1);
            result.setLeft(newHuffmanTree(bitIn));
            result.setRight(newHuffmanTree(bitIn));
            return result;
        } else {
            int temp = bitIn.readBits(IHuffConstants.BITS_PER_WORD + 1);
            TreeNode result = new TreeNode(temp, 0);
            return result;
        }
    }
     
    // helper method to write the data
    private void writeData(BitInputStream bitIn, BitOutputStream bitOut, TreeNode huffmanTree) throws IOException {
        TreeNode root = huffmanTree;
        TreeNode currentNode = huffmanTree;
        int val = bitIn.readBits(1);
        while(val != -1 && currentNode.getValue() != IHuffConstants.PSEUDO_EOF) {
            if(currentNode.getLeft() == null && currentNode.getRight() == null) {
                bitOut.writeBits(IHuffConstants.BITS_PER_WORD, currentNode.getValue());
                numBits += IHuffConstants.BITS_PER_WORD;
                currentNode = root;
            } else {
                if(val == 1)
                    currentNode = currentNode.getRight();
                else
                    currentNode = currentNode.getLeft();
                val = bitIn.readBits(1);
            }            
        }
    }
}
