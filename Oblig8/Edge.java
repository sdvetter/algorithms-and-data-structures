package Oblig8;

public class Edge {
    Edge next;
    Node tooNode;
    int edgeDistance;
    int runtime;
    int speed;
    int fromNodeIndex;
    int tooNodeIndex;

    public Edge(Node tooNode, Edge edge, int runtime, int edgeDistance, int speedLimit){
        this.tooNode = tooNode;
        this.next = edge;
        this.runtime = runtime;
        this.edgeDistance = edgeDistance;
        this.speed = speedLimit;
    }

    public int getNeighbourIndex(int nodeIndex) {
        if (this.fromNodeIndex == nodeIndex) {
            return this.tooNodeIndex;
        } else {
            return this.fromNodeIndex;
        }
    }

}
