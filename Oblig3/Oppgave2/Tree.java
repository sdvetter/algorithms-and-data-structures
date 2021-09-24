package Oblig3.Oppgave2;

import java.util.Scanner;

public class Tree {
    TreeNode root;
    static int count = 8;   // number of spaces

    public Tree(){
        root = null;
    }

    // Recursive method for adding nodes
    private TreeNode addRecursive(TreeNode n, String s){
        if (n == null){
            return new TreeNode(s, null, null,null);
        }
        TreeNode parent = null;
        parent = n;
        if (s.compareTo(parent.string) < 0){            // Compare with a String returns -1 if string occurs earlier in the alphabet
            n.left = addRecursive(n.left, s);
        } else if (s.compareTo(parent.string) > 0){
            n.right = addRecursive(n.right, s);
        } else return n;                                // If word already is registered in the tree, it ignores it
        return n;
    }

    public void addNode(String s){
        root = addRecursive(root, s);
    }

    static void traverseTree(TreeNode root, int space)
    {
        if (root == null)
            return;

        // Increase distance between node level
        space += count;

        // traverses right node first
        traverseTree(root.right, space);

        // Print out amount of spaces and node
        System.out.print("\n");
        for (int i = count; i < space; i++)
            System.out.print(" ");
        System.out.print(root.string + "\n");

        // traverses other child
        traverseTree(root.left, space);
    }

    // calls other method, so we dont need a parameter
    public void traverse()
    {
        // Pass initial space amount as 0
        traverseTree(root, 0);
    }




    public static void main (String[] args){
        Tree tree = new Tree();
        Scanner s = new Scanner(System.in);
        System.out.println("Write inn words you want to add in the tree, remember space between words");
        String input = s.nextLine();
        String[] split = input.split("\\s+");      // Array containing words from input

        // Adds words into the tree
        for (int i = 0; i < split.length; i++) {
            tree.addNode(split[i]);
        }

        tree.traverse(); // call traverse method
    }


}
