/**  
		Tin Vo
 **/
import java.util.LinkedList;
public class PQueue<E> {
    // instance variables
    private LinkedList<TreeNode> con;
    private int size;
    
    //constructor for the PQueue
    public PQueue(){
        con = new LinkedList<TreeNode>();
        size = 0;
    }
    
    /**
     * add to the Queue, the new item must go behind the items already present(fair way)
     * Pre: node != null
     * Post: node is added to the PQueue
     */ 
    public boolean add(TreeNode node) {
        if(node == null)
            throw new IllegalArgumentException("Violation of Precondition. add() method: node != null");
        
        if(size == 0) {
            // empty, add to the Queue
            con.add(node);
            size++;
            return true;
        } else {
            int index = 0;
            // find the correct index to insert
            for(int i = 0; i < this.size(); i++) {
                if(node.compareTo(con.get(i)) > 0)
                    index++;
                if(node.compareTo(con.get(i)) == 0 && node.getValue() >= con.get(i).getValue())
                    index++;
            }
            // add current node to the Queue
            con.add(index,node);
            size++;
            return true;
        }
    }
    
    /** 
     * remove from front of the Queue
     * Pre: size > 0
     * Post: remove front of the Queue, size--
     */
    public TreeNode remove() {
        if(this.size() <= 0)
            throw new IllegalArgumentException("Violation of Precondition. remove() method: size > 0");
        size--;
        return con.remove(0);
    }
    
    /** 
     * return the front of the Queue (not remove)
     * Pre: none
     * Post: return the front of the Queue, null if the Queue is empty
     */
    public TreeNode peek() {
        if(this.size() != 0)
            return con.get(0);
        else
            return null;
    }
    
    /** 
     * return the size of the Queue
     * Pre: none
     * Post: returns the size of the Queue
     */
    public int size() {
        return size;
    }    
}