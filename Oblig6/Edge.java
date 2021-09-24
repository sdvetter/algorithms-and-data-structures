package Oblig6;

public class Edge {
    Edge next;
    Node tooNode;
    int weight;

    public Edge(Node tooNode, Edge next, int weight){
        this.tooNode = tooNode;
        this.next = next;
        this.weight = weight;
    }

}
