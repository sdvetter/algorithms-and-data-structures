package Oblig8;

public class Node {
    Edge edge;
    PreviousNode parentNode;
    double longitude;
    double latitude;
    double cosWidth;
    int distance;
    boolean visited;
    int estimatedLength;
    int index;
    boolean reachedGoal;
    String name;
    int code;

    public Node(){};

    public Node(int index, double latitude, double longitude){
        this.index = index;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cosWidth = Math.cos(latitude);
        this.visited = false;
       // this.estimatedLength = -1;
        this.name = "";
    }

    public String toString(){
        return latitude * (180 / Math.PI)+ ", "+ this.longitude*(180 / Math.PI);
    }
}
