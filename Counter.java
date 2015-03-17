/**  
		Tin Vo
 **/
import java.util.TreeMap;
public class Counter {
    // instance variables
    private TreeMap<Integer, String> map;
    private TreeNode huffmanTree;
    private int[] freq;
    private int headerFormat;
    private int numBitsSCF;
    private int numBitsSTF;
        
    // constructor for Counter class
    public Counter(TreeNode huffmanTree, TreeMap<Integer, String> map, int[] counter, int headerFormat) {
        if(huffmanTree == null && map == null)
            throw new IllegalArgumentException("Constructor Precondition: huffmnaTree != null, map != null");
        this.huffmanTree = huffmanTree;
        this.map = map;
        this.freq = counter;
        this.headerFormat = headerFormat;
        numBitsSCF = 0;
        numBitsSTF = 0;
        if(headerFormat == IHuffConstants.STORE_COUNTS)
            countSCF();
        else
            countSTF();
    }
    
    /** 
     * method returns written bits Standard Count Format
     * Pre: none
     * Post: returns written bits Standard Count Format
     */
    public int getSCFCount() {
        return numBitsSCF;
    }
    
    /** 
     * method returns written bits Standard Tree Format
     * Pre: none
     * Post: returns written bits Standard Tree Format
     */
    public int getSTFCount() {
        return numBitsSTF;
    }

    // helper method finds num of bits SCF
    private void countSCF() {
        numBitsSCF += IHuffConstants.BITS_PER_INT * 2;
        numBitsSCF += IHuffConstants.BITS_PER_INT * IHuffConstants.ALPH_SIZE;
        countCompressedData();
        countPSEUDO_EOF();
    }
    
    // helper method finds num of bits STF
    private void countSTF() {
        numBitsSTF += IHuffConstants.BITS_PER_INT * 3;
        numBitsSTF +=  numInternalNodes(huffmanTree) + numExternalNodes(huffmanTree) * (IHuffConstants.BITS_PER_WORD + 2);
        countCompressedData();
        countPSEUDO_EOF();
    }
    
    // helper method finds num bits of compressed data
    private void countCompressedData() {
        for(int key: map.keySet()) {
            if(key != IHuffConstants.PSEUDO_EOF) {
                String current = map.get(key);
                if(headerFormat == IHuffConstants.STORE_COUNTS)
                    numBitsSCF += current.length() * freq[key];
                else
                    numBitsSTF += current.length() * freq[key];
            }
        }
    }
    
    // helper method find num bits of PSEUDO_EOF
    private void countPSEUDO_EOF() {
        if(headerFormat == IHuffConstants.STORE_COUNTS)
             numBitsSCF += map.get(IHuffConstants.PSEUDO_EOF).length();
        else
             numBitsSTF += map.get(IHuffConstants.PSEUDO_EOF).length();
    }
    
    // helper method return numbers of external nodes
    private int numExternalNodes(TreeNode currentNode) {
        if(currentNode.isLeaf())
            return 1;
        else {
            int count = 0;
            count += numExternalNodes(currentNode.getLeft()) + numExternalNodes(currentNode.getRight());
            return count;
        }
    }
    
    // helper method returns numbers of internal nodes
    private int numInternalNodes(TreeNode currentNode) {
        if(currentNode.isLeaf()) 
            return 0;
        else {
            int count = 0;
            if(currentNode != null)
                count += 1 + numInternalNodes(currentNode.getLeft()) + numInternalNodes(currentNode.getRight());
            return count;
        }
    }
}
