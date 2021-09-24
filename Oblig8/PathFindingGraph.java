package Oblig8;



import Oblig5.Graph;

import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class PathFindingGraph {
    PriorityQueue<Node> priorityQueue;
    Node[] nodes;
    int numberOfNodes;
    int numberOfEdges;
    HashMap<String, Integer> places;
     // double cosWidth;

    public PathFindingGraph() throws IOException {
        this.places = new HashMap<>();
    }

    public static void main(String[] args) throws IOException {
        PathFindingGraph pathFindingGraph = new PathFindingGraph();
        try {
            BufferedReader nodeReader = new BufferedReader(new FileReader(new File("noder.txt")));
            BufferedReader edgeReader = new BufferedReader(new FileReader(new File("kanter.txt")));
            BufferedReader placeReader = new BufferedReader(new FileReader(new File("interessepkt.txt")));
            pathFindingGraph.initiateNodes(nodeReader);
            pathFindingGraph.initiateEdges(edgeReader);
            pathFindingGraph.initiatePlaces(placeReader);

        } catch (IOException e){
            e.printStackTrace();
        }
        pathFindingGraph.resetGraph();

        int helsinki1 = 1221382;
        int trondheim = 2399829;

        int karvag = pathFindingGraph.places.get("\"Kårvåg\"");
        int gjemnes = pathFindingGraph.places.get("\"Gjemnes\"");

        // Shortest path between Kårvåg and Gjemnes
        System.out.println("A star");
        pathFindingGraph.runAStar(pathFindingGraph, karvag, gjemnes);

        pathFindingGraph.resetGraph();

        System.out.println(" ");
        System.out.println("Dijkstras ");
        pathFindingGraph.runDijkstras(pathFindingGraph, karvag, gjemnes);



// Shortest path between Trondheim and Helsinki
/*
        System.out.println("A star");
        pathFindingGraph.runAStar(pathFindingGraph,trondheim, helsinki1);

        pathFindingGraph.resetGraph();

        System.out.println(" ");
        System.out.println("Dijkstras ");
        pathFindingGraph.runDijkstras(pathFindingGraph, trondheim, helsinki1);
 */



        // Closest gas stations too Værnes Lufthavn
/*
        System.out.println("Nærmeste bensinstasjoner til Værnes lufthavn");
        pathFindingGraph.runDijkstrasGivenCode(6198111, 2);

        pathFindingGraph.resetGraph();
 */



        // Closest charging station too Røros Hotell
/*
        System.out.println("Nærmest ladestasjoner til Røros Hotell");
        pathFindingGraph.runDijkstrasGivenCode(1117256, 4);

 */
    }


    public void initiateNodes(BufferedReader bufferedReader) throws IOException {
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        this.numberOfNodes = Integer.parseInt(stringTokenizer.nextToken()); // First number in file says how many nodes in file
        this.nodes = new Node[numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            // initiate all variables that are supposed to be in node
            int nodeNumber = Integer.parseInt(stringTokenizer.nextToken());
            double lat = Double.parseDouble(stringTokenizer.nextToken()) * (Math.PI / 180);
            double lon = Double.parseDouble(stringTokenizer.nextToken()) * (Math.PI / 180);

            Node n = new Node(nodeNumber, lat, lon);
            n.parentNode = new PreviousNode();
            n.cosWidth = Math.cos(lat);
            nodes[nodeNumber] = n;


        }
    }

    public void initiateEdges(BufferedReader bufferedReader) throws IOException {
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        this.numberOfEdges = Integer.parseInt(stringTokenizer.nextToken()); // First number in file says how many edges in file
        for (int i = 0; i < numberOfEdges; i++){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());

            int fromNode = Integer.parseInt(stringTokenizer.nextToken());
            int tooNode = Integer.parseInt(stringTokenizer.nextToken());
            int runtime = Integer.parseInt(stringTokenizer.nextToken());
            int distance = Integer.parseInt(stringTokenizer.nextToken());
            int speedLimit = Integer.parseInt(stringTokenizer.nextToken());

            Edge edge = new Edge(nodes[tooNode], nodes[fromNode].edge, runtime, distance, speedLimit);
            nodes[fromNode].edge = edge;

        }
    }

    public void initiatePlaces(BufferedReader bufferedReader) throws IOException{
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int p = Integer.parseInt(stringTokenizer.nextToken());
        for (int i = 0; i < p; i++){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());

            int nodeNr = Integer.parseInt(stringTokenizer.nextToken());
            int code = Integer.parseInt(stringTokenizer.nextToken());
            String place = stringTokenizer.nextToken();

            while (stringTokenizer.hasMoreTokens()) place += stringTokenizer.nextToken();
            nodes[nodeNr].code = code;
            nodes[nodeNr].name = place;
            places.put(place, nodeNr);
        }
    }

    private int AStar(Node start, Node goal){

        start.parentNode.distance = 0;
        start.parentNode.endDistance = distance(start, goal);
        start.parentNode.heuristicDistance = start.parentNode.endDistance;
        goal.reachedGoal = true;
        priorityQueue = new PriorityQueue<>((a,b) -> a.parentNode.heuristicDistance - b.parentNode.heuristicDistance);
        this.priorityQueue.add(start);
        int count = 0;
        while (!this.priorityQueue.isEmpty()) {
            Node node = this.priorityQueue.poll();
            count++;
            if (node.reachedGoal) return count;
            for (Edge edge = node.edge; edge != null; edge = edge.next) {
                PreviousNode previousNode1 = node.parentNode;
                PreviousNode previousNode2 = edge.tooNode.parentNode;
                if (previousNode2.distance > previousNode1.distance + edge.runtime) {
                    if(previousNode2.endDistance == -1) previousNode2.endDistance = distance(edge.tooNode, goal);
                    previousNode2.distance = previousNode1.distance + edge.runtime;
                    previousNode2.pNode = node;
                    previousNode2.heuristicDistance = previousNode2.distance + previousNode2.endDistance;
                    this.priorityQueue.add(edge.tooNode);
                }
            }
        }
        return -1;


    }


     public void runAStar(PathFindingGraph pathFindingGraph, int start, int goal){
        Node startNode = this.nodes[start];
        Node goalNode = pathFindingGraph.nodes[goal];

         Date startTime = new Date();
         int nrNodes = pathFindingGraph.AStar(startNode, goalNode);
         Date endTime = new Date();
         System.out.println("Nodes processed: " + nrNodes);
         System.out.println( (double) (endTime.getTime() - startTime.getTime()) / 1000 + " s");

         try{
             FileWriter fileWriter = new FileWriter("astar.txt");
             int time = goalNode.parentNode.distance / 100;
             System.out.print("Total time to driving: " + time / 3600 + ":");
             System.out.print((time % 3600) / 60 + ":");
             System.out.print((time % 3600) % 60 + " hours\n");

             while(goalNode !=null){
                 fileWriter.write(goalNode.toString() + "\n");
                 goalNode = ((PreviousNode) goalNode.parentNode).pNode;
             }
             fileWriter.close();

         } catch (IOException e) {
             e.printStackTrace();
         }
     }


    public int dijkstra(Node start, Node goal){
        start.parentNode.distance = 0;
        goal.reachedGoal = true;
        this.priorityQueue = new PriorityQueue<>((a,b) -> (a.parentNode.distance) - (b.parentNode.distance));
        priorityQueue.add(start);
        int count = 0;

        while(!priorityQueue.isEmpty()){
            Node node = priorityQueue.poll();
            count++;
            if (node.reachedGoal) return count;

            for (Edge edge = node.edge; edge != null; edge = edge.next){
                PreviousNode previousNode = node.parentNode;
                PreviousNode previousNode2 = edge.tooNode.parentNode;
                if (previousNode2.distance > previousNode.distance + edge.runtime) {
                    previousNode2.distance = previousNode.distance + edge.runtime;
                    previousNode2.pNode = node;
                    this.priorityQueue.add(edge.tooNode);
                }
            }
        }
        return -1;
    }


    public void resetGraph(){
        for (Node node : nodes) {
            node.parentNode = new PreviousNode();
            node.visited = false;
            node.reachedGoal = false;
        }
    }


    private Node[] dijkstrasGivenCode(Node start, int code){
        priorityQueue = new PriorityQueue<>((a,b) -> a.parentNode.distance - b.parentNode.distance);
        Node[] tenNearest = new Node[10];
        start.parentNode.distance = 0;
        priorityQueue.add(start);
        int count = 0;

        while(!priorityQueue.isEmpty()){
            Node node = priorityQueue.poll();
            if (!node.visited && (node.code == code || ((code == 2 || code == 4) && node.code == 6))){
                tenNearest[count] = node;
                count++;
                node.visited = true;
            } if (count == 10) break;

            for (Edge edge = node.edge; edge != null; edge = edge.next){
                PreviousNode previousNode = node.parentNode;
                PreviousNode previousNode2 = edge.tooNode.parentNode;
                if (previousNode2.distance > previousNode.distance + edge.edgeDistance) {
                    previousNode2.distance = previousNode.distance + edge.edgeDistance;
                    previousNode2.pNode = node;
                    this.priorityQueue.add(edge.tooNode);
                }
            }
        }
        return tenNearest;
    }

    public void runDijkstrasGivenCode(int start, int code){
        Node[] closestNodes = dijkstrasGivenCode(nodes[start], code);
        try {
            FileWriter fileWriter = new FileWriter("10closest.txt");
            for (Node node : closestNodes){
                if (node != null){
                    fileWriter.write(node.toString() + "\n");
                    System.out.println(node.name + ", " + node.code);
                }
            }
            fileWriter.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void runDijkstras(PathFindingGraph pathFindingGraph, int start, int goal){
        Node startNode = nodes[start];
        Node goalNode = nodes[goal];
        Date startTime = new Date();
        int nrNodes = pathFindingGraph.dijkstra(startNode, goalNode);
        Date endTime = new Date();
        System.out.println("Nodes processed: " + nrNodes);
        System.out.println( (double) (endTime.getTime() - startTime.getTime()) / 1000 + " s");
        try{
            FileWriter fileWriter = new FileWriter("dijkstras.txt");
             int time = goalNode.parentNode.distance/100;
             System.out.print("Total time to drive: " + time / 3600 +":");
             System.out.print((time % 3600) / 60 + ":");
             System.out.print((time % 3600) % 60 + " hours \n");
            while(goalNode !=null){
                fileWriter.write(goalNode.toString() + "\n");
                goalNode = ((PreviousNode) goalNode.parentNode).pNode;
            }
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int distance(Node n1, Node n2) {
        double time = 6371 * 2 / 130 * 3600 * 100;
        double sinWidth = Math.sin((n1.latitude - n2.latitude) / 2.0);
        double sinLength = Math.sin((n1.longitude - n2.longitude) / 2.0);
        return (int) (time * Math.asin(Math.sqrt(sinWidth * sinWidth + n1.cosWidth * n2.cosWidth * sinLength * sinLength)));
    }

}
