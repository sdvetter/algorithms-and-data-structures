package Oblig8;

public class PreviousNode {
    private static final int infinite = 1000000000;
    int distance;
    int heuristicDistance;
    int endDistance;
    Node pNode;

    public PreviousNode(){
        this.distance = infinite;
        this.heuristicDistance = infinite;
        this.endDistance = -1;

    }
}
