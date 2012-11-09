

import java.io.*;

public class Node implements Serializable{
    int key;
    char val;
    String code = "";
    Node right;
    Node left;
    Node parent;

    public Node(int k, char v){
        key = k;
        val = v;
    }
    
    public boolean hasChild(){
        if((right != null) || (left != null))
            return true;
        else
            return false;
    }
    
    public String toString(){
        String s = "";
        s += "parent = ";
        if (parent == null)
            s += "null ";
        else
            s += parent.key+" "+parent.val+" ";
        s += "left = ";
        if (left == null)
            s += "null ";
        else
            s += left.key+" "+left.val+" ";
        s += "right = ";
        if (right == null)
            s += "null ";
        else
            s += right.key+" "+right.val;
        return s;
        }
    
}
