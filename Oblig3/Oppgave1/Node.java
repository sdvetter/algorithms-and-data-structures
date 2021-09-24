package Oblig3.Oppgave1;

public class Node {
    int value;
    Node next;
    Node previous;

    public Node(int v, Node n, Node p){
        value = v;
        next = n;
        previous = p;
    }


    public int getValue(){
        return this.value;
    }

    public void toNegative(){
         this.value *= -1;
    }

    public Node getNext(){
        return this.next;
    }

    public Node getPrevious(){
        return this.previous;
    }

}
