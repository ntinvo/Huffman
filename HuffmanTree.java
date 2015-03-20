/**  
		Tin Vo
 **/
import java.io.IOException;
public class HuffmanTree
{
    // instance variables 
    private TreeNode huffmanTree;
    
    // constructor
    public HuffmanTree() {
        huffmanTree = new TreeNode(0, 0);
    }
    
    public TreeNode getHuffmanTree(PQueue<TreeNode> priorQueue) throws IOException {
        if(priorQueue == null)
            throw new IllegalArgumentException("Violation of Precondition. getHuffmanTree: priorQueue != null");
        while(priorQueue.size() != 1) {
            TreeNode firstNode = priorQueue.remove();
            TreeNode secondNode = priorQueue.remove();
            if(secondNode != null) {
                int totalWeight = firstNode.getWeight() + secondNode.getWeight();
                priorQueue.add( new TreeNode(Integer.MAX_VALUE, totalWeight, firstNode, secondNode));
            }
        }
        huffmanTree = priorQueue.peek();
        return huffmanTree;
    }
}