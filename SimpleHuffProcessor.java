/**  
		Tin Vo
 **/
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TreeMap;
public class SimpleHuffProcessor implements IHuffProcessor {
    // instance variables
    private HuffViewer myViewer;
    private TreeMap<Integer, String> map = new TreeMap<Integer, String>();
    private TreeNode huffmanTree;
    private int[] counter;
    private int headerFormat;
    private int dataSaved;
    
    /**
     * Compresses input to output, where the same InputStream has
     * previously been pre-processed via <code>preprocessCompress</code>
     * storing state used by this call.
     * <br> pre: <code>preprocessCompress</code> must be called before this method
     * @param in is the stream being compressed (NOT a BitInputStream)
     * @param out is bound to a file/stream to which bits are written
     * for the compressed file (not a BitOutputStream)
     * @return the number of bits written. 
     */
    public int compress(InputStream in, OutputStream out, boolean force) throws IOException {    
        if(dataSaved >= 0 || force) {
            BitInputStream bitIn = new BitInputStream(in);
            BitOutputStream bitOut = new BitOutputStream(out);
            // Compress process, write to output file
            Compress comp = new Compress(huffmanTree, map, counter);
            // Standard Count Format
            if(headerFormat == IHuffConstants.STORE_COUNTS)
                comp.writeSCF(bitIn, bitOut);
            // Standard Tree Format
            if(headerFormat == IHuffConstants.STORE_TREE)
                comp.writeSTF(bitIn, bitOut);
            // show num of written bits to the GUI
            showString("Compress Return: " + comp.getWrittenBits());
            return comp.getWrittenBits(); 
        } else if(dataSaved < 0  && !force){
            myViewer.showError("Compressed file has " + Math.abs(dataSaved) + " more bits than uncompressed file.\n" +
                                "Select \"force compression\" option to compress.");  
        }
        return -1;       
    }
    
    /**
     * Uncompress a previously compressed stream in, writing the
     * uncompressed bits/data to out.
     * @param in is the previously compressed data (not a BitInputStream) 
     * @param out is the uncompressed file/stream
     * @return the number of bits written to the uncompressed file/stream
     */
    public int uncompress(InputStream in, OutputStream out) throws IOException {
        BitInputStream bitIn = new BitInputStream(in);
        BitOutputStream bitOut = new BitOutputStream(out);
        // read magic number and throw Exception if needed
        int magic = bitIn.readBits(IHuffConstants.BITS_PER_INT);
        if(magic == -1) {
            bitIn.close();
            bitOut.close();
            throw new IOException("Error reading compressed file. \n" + "unexpected end of input. No PSEUDO_EOF character.");                    
        }else if(magic != IHuffConstants.MAGIC_NUMBER) {
            bitIn.close();
            bitOut.close();
            throw new IOException("Error reading compressed file. \n" +  "File did not start with the huff magic number.");                   
        }
        // read headerFormat
        int header = bitIn.readBits(IHuffConstants.BITS_PER_INT);
        // Standard Count Format
        if(header == IHuffConstants.STORE_COUNTS) 
            return uncompressSCF(in, bitIn, bitOut);                       
        // Standard Tree Format
        if(header == IHuffConstants.STORE_TREE) 
            return uncompressSTF(bitIn, bitOut);            
        bitIn.close();
        bitOut.close();
        return -1;
    }
    
    // helper method to uncompress Standard Count Format
    private int uncompressSCF(InputStream in, BitInputStream bitIn, BitOutputStream bitOut) throws IOException {
        PQueue<TreeNode> priorQueue = new PQueue<TreeNode>();
        Frequency freq = new Frequency(bitIn, false);
        TreeMap<Integer, TreeNode> nodes = freq.getNodes();
        
        // add to priority queue
        for(int node: nodes.keySet()) 
             priorQueue.add(nodes.get(node));
        
        // get Huffman Tree and Map 
        HuffmanTree temp = new HuffmanTree();
        huffmanTree = temp.getHuffmanTree(priorQueue);
        getHuffmanMap(huffmanTree, "");
        
        // print the results
        printResult(false);    
        
        //bitIn = freq.getBitInStream();
        Decompress decomp = new Decompress(huffmanTree);
        decomp.decompressSCF(bitIn, bitOut);
        showString("Decompress Return : " + decomp.getWrittenBits());
        return decomp.getWrittenBits();
    }
    
    // helper method to uncompress Standard Tree Format
    private int uncompressSTF(BitInputStream bitIn, BitOutputStream bitOut) throws IOException{
        Decompress decomp = new Decompress(huffmanTree);        
        TreeNode tempHuffmanTree = decomp.decompressSTF(bitIn, bitOut);
        
        // get Huffman Map and prints the result
        getHuffmanMap(tempHuffmanTree, "");
        
        // print the results
        printResult(false);
        
        // show num written bits results to the GUI
        showString("Decompress Return: " + decomp.getWrittenBits());
        return decomp.getWrittenBits();
    }

    /**
     * Preprocess data so that compression is possible ---
     * count characters/create tree/store state so that
     * a subsequent call to compress will work. The InputStream
     * is <em>not</em> a BitInputStream, so wrap it int one as needed.
     * @param in is the stream which could be subsequently compressed
     * @param headerFormat a constant from IHuffProcessor that determines what kind of
     * header to use, standard count format, standard tree format, or
     * possibly some format added in the future.
     * @return number of bits saved by compression or some other measure
     * Note, to determine the number of 
     * bits saved, the number of bits written includes 
     * ALL bits that will be written including the 
     * magic number, the header format number, the header to 
     * reproduce the tree, AND the actual data.
     */
    public int preprocessCompress(InputStream in, int headerFormat) throws IOException {       
         BitInputStream bitIn = new BitInputStream(in);
         PQueue<TreeNode> priorQueue = new PQueue<TreeNode>();
         Frequency freq = new Frequency(bitIn, true);
         this.headerFormat = headerFormat;
         
         // get nodes and add to priority queue
         TreeMap<Integer, TreeNode> nodes = freq.getNodes();
         for(int node: nodes.keySet()) 
             priorQueue.add(nodes.get(node));
         
         // get Huffman Tree, Huffman Map and counter
         HuffmanTree temp = new HuffmanTree();
         huffmanTree = temp.getHuffmanTree(priorQueue);
         getHuffmanMap(huffmanTree, "");
         counter = freq.getCount();
         
         // print the results
         printResult(true);   
         return getDataSaved(priorQueue, bitIn); 
    }
    
     /**
     * Make sure this model communicates with some view.
     * @param viewer is the view for communicating.
     */
    public void setViewer(HuffViewer viewer) {
        myViewer = viewer;
    }
    
    // helper method prints result to GUI
    private void printResult(boolean compress) {
        if(compress) {
            showString("Results after counting characters in the file\n");
            for(int key: map.keySet()) 
                if(key != IHuffConstants.PSEUDO_EOF)
                    showString("value: " + key + ", value as char: " + (char) key  + ", frequency: " + counter[key]  + ", new code: " + map.get(key));
                else
                    showString("value: " + key + ", value as char: This is the Pseudo EOF character , frequency: 1" + ", new code: " + map.get(key));
        } else {
            showString("DECOMPRESSING: Code for values in file: \n");
            for(int key: map.keySet())
                if(key != IHuffConstants.PSEUDO_EOF)
                    showString("value: " + key + ", value as char: " + (char) key  + ", code: " + map.get(key));
                else
                    showString("value: " + key + ", value as char: This is the Pseudo EOF character, code: " + map.get(key));

        }
    }
    
    // helper method that the returns the data saved
    private int getDataSaved(PQueue<TreeNode> priorQueue, BitInputStream bitIn) throws IOException{
        TreeNode curr = priorQueue.peek();
        
        // num bits in the original file
        int originBits = IHuffConstants.BITS_PER_WORD * (curr.getWeight() - 1);
        
        // num bits written to the file
        Counter count = new Counter(huffmanTree, map, counter, headerFormat); 
        int writtenBits = 0;
        if(headerFormat == IHuffConstants.STORE_COUNTS)
            writtenBits = count.getSCFCount(); 			  //Standard Count Format
        else
            writtenBits = count.getSTFCount();  		  //Standard Tree Format
        
        // num bits saved
        dataSaved = originBits - writtenBits;
        return dataSaved;
    }
    
    // helper method for getHuffmanMap
    private void getHuffmanMap(TreeNode currentNode, String path) {
        if(currentNode.isLeaf())
            map.put(currentNode.getValue(), path);
        else {
            if(currentNode != null) {
                // go left
                if(currentNode.getLeft() != null) 
                    getHuffmanMap(currentNode.getLeft(), path + "0");                          
                // go right
                if(currentNode.getRight() != null) 
                    getHuffmanMap(currentNode.getRight(), path + "1");                
            }
        }
    }
      
    // helper method showString
    private void showString(String s){
        if(myViewer != null)
            myViewer.update(s);
    }
}