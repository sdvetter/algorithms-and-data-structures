package Oblig6;

import java.io.*;
import java.util.*;

public class WeightedGraph {
    int N;
    int K;
    Node[] node;
    PriorityQueue<Node> priorityQueue;



    public WeightedGraph(BufferedReader bufferedReader) throws IOException {
        StringTokenizer st = new StringTokenizer(bufferedReader.readLine());
        this.N = Integer.parseInt(st.nextToken()); // amount of nodes
        this.node = new Node[N];
        for(int i = 0; i < N; ++i) node[i] = new Node(i);
        this.K = Integer.parseInt(st.nextToken());
        for (int j = 0; j < K; j++) {
            st = new StringTokenizer(bufferedReader.readLine());
            
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            
            Edge e = new Edge(node[to], node[from].edge, weight);
            node[from].edge = e;
        }
    }




    private void reduceLength(Node n, Edge e){
        PreviousNode previousNode = (PreviousNode) n.object, previousNode2 = (PreviousNode) e.tooNode.object;
        if (previousNode2.distance > (previousNode.distance + e.weight)){
            previousNode2.distance = (previousNode.distance + e.weight);
            previousNode2.pNode = n;
            this.priorityQueue.remove(e.tooNode);
            this.priorityQueue.add(e.tooNode);
        }
    }

    private void initializePrevious(Node n){
        for (int i = 0; i < node.length; i++){
            node[i].object = new PreviousNode();
        }
        ((PreviousNode) n.object).distance = 0;
    }


    private void prioritizeNodes(){
        this.priorityQueue = new PriorityQueue<>(this.N, (a,b) -> ((PreviousNode) a.object).distance - ((PreviousNode) b.object).distance);
        for (int i = 0; i < N; i++){
            this.priorityQueue.add(node[i]);
        }
    }

    public void dijkstra(Node s){
        initializePrevious(s);
        prioritizeNodes();

        for (int i = this.N; i > 1; i--){
            Node n = this.priorityQueue.poll();
            for (Edge e = n.edge; e != null; e = e.next){
                reduceLength(n, e);
            }
        }
    }

    public static void main(String[] args) {
        WeightedGraph weightedGraph = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("C:\\Users\\simon\\OneDrive\\Dokumenter\\NTNU\\Dataingeniør 2020-2021" +
                    "\\1. Semester\\IDATT2001 Algoritmer og datastrukturer\\Øvinger\\Øving6\\vg2")));
            weightedGraph = new WeightedGraph(bufferedReader);
        }catch (IOException e){
            e.printStackTrace();
        }

        int nodeIndex = 7;
        Node n = weightedGraph.node[nodeIndex];
        weightedGraph.dijkstra(n);

        System.out.format("%-7s%-7s%-7s%n", "Node","From", "Distance");
        for (int i = 0; i < weightedGraph.N; i++) {
            if(((PreviousNode) weightedGraph.node[i].object).distance != PreviousNode.infinite){
                String from = (weightedGraph.node[i].index == nodeIndex)? "Start": String.valueOf(((PreviousNode) weightedGraph.node[i].object).pNode.index);
                System.out.format("%-7s%-7s%-7s%n", weightedGraph.node[i].index,from,((PreviousNode) weightedGraph.node[i].object).distance);
            }else{
                System.out.format("%-7s%-7s%-7s%n", weightedGraph.node[i].index,"","Not reached");
            }
        }
    }





}
