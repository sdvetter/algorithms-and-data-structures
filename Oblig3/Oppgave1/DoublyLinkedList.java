package Oblig3.Oppgave1;

public class DoublyLinkedList {
    private Node head = null;
    private Node tail = null;
    private int nrElementes = 0;
    public Node getHead(){return head;}
    public Node getTail(){return tail;}
    public int getNrElementes(){return  nrElementes;}


    public void insertBehind(int value){
        Node n = new Node(value, null, tail);
            if (tail != null) tail.next = n;
            else head = n;
            tail = n;
            nrElementes++;
    }

    public void insertInfront(int value){
        head = new Node(value, head, null);
        if (tail == null) tail = head;
        else head.next.previous = head;
        nrElementes++;
    }




}
