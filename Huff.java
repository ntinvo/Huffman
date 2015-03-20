/**  
		Tin Vo
 **/

public class Huff {
    public static void main(String[] args){
        HuffViewer sv = new HuffViewer("Huffman Compression");
        IHuffProcessor proc = new SimpleHuffProcessor();
        sv.setModel(proc);    
    }
}
