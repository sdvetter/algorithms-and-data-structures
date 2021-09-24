package Oblig3.Oppgave2;

public class TreeNode {
    String string;
    TreeNode left;
    TreeNode right;
    TreeNode parent;


    public TreeNode(String string, TreeNode left, TreeNode right, TreeNode parent){
        this.string = string;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }



    public TreeNode getRight(){
        return right;
    }
    public TreeNode getLeft(){
        return left;
    }


}
