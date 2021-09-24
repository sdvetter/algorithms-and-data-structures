package Oblig5;


import java.io.*;
import java.util.*;
import java.util.LinkedList;

public class Graph {


    private int nrVertices;
    private int nrEdges;
    private LinkedList<Integer> adjacentList[]; //Adjacency List
    
    
    // Constructor used for getting reversed graph method
    public Graph(int nrVertices) {
        this.nrVertices = nrVertices;
        adjacentList = new LinkedList[nrVertices];
        for (int i=0; i<nrVertices; ++i)
            adjacentList[i] = new LinkedList();
    }

    // Constructor used to make a graph out of a file
    public Graph(BufferedReader br) throws IOException{
        StringTokenizer tokenizer = new StringTokenizer(br.readLine());

        this.nrVertices = Integer.parseInt(tokenizer.nextToken());
        this.nrEdges = Integer.parseInt(tokenizer.nextToken());

        adjacentList = new LinkedList[nrVertices];
        for (int i = 0; i < nrVertices; i++) { adjacentList[i] = new LinkedList<>(); }
        for (int i = 0; i < nrEdges; i++){
            tokenizer = new StringTokenizer(br.readLine());
            int fra = Integer.parseInt(tokenizer.nextToken());
            int til = Integer.parseInt(tokenizer.nextToken());
            adjacentList[fra].add(til);
        }
    }



    // A recursive function to print DFS starting from given vertex
    void recursiveDFS(int v,boolean visited[]) {
        // Mark the current node as visited
        visited[v] = true;

        System.out.print(v + " ");

        // Search for vertices adjacent to v
        Iterator<Integer> iterator = adjacentList[v].iterator();
        while (iterator.hasNext())
        {
            int n = iterator.next();
            if (!visited[n])
                recursiveDFS(n,visited);
        }
    }

    // Method that returns reverse of graph
    Graph getReversedGraph() {
        Graph graph = new Graph(nrVertices);
        for (int v = 0; v < nrVertices; v++){
            // Search for vertices adjacent to v
            Iterator<Integer> iterator = adjacentList[v].listIterator();
            while(iterator.hasNext())
                graph.adjacentList[iterator.next()].add(v);
        }
        return graph;
    }

    void filler(int v, boolean visited[], Stack stack){
        // Mark the current node as visited
        visited[v] = true;

        // Search for vertices adjacent to v
        Iterator<Integer> iterator = adjacentList[v].iterator();
        while (iterator.hasNext())
        {
            int n = iterator.next();
            if(!visited[n])
                filler(n, visited, stack);
        }
        // All vertices reachable from v are processed by now, store in stack
        stack.push(v);
    }

   // Prints out Strongly Connected Components
    void printSCCs()
    {
        Stack stack = new Stack();

        // Mark all the vertices as not visited (For first DFS)
        boolean visited[] = new boolean[nrVertices];
        for(int i = 0; i < nrVertices; i++)
            visited[i] = false;

        // Fill vertices in stack
        for (int i = 0; i < nrVertices; i++)
            if (visited[i] == false)
                filler(i, visited, stack);

        // Create a reversed graph
        Graph graph = getReversedGraph();

        // Mark all the vertices as not visited (For second DFS)
        for (int i = 0; i < nrVertices; i++)
            visited[i] = false;

        // Now process all vertices in order defined by Stack
        int count = 1;
        while (stack.empty() == false) {

            // Pop a vertex from stack
            int v = (int)stack.pop();

            // Print Strongly connected component of the popped vertex
            if (visited[v] == false)
            {
                if(nrVertices < 100){
                    System.out.print("\r" + count + "              ");
                }
                graph.recursiveDFS(v, visited);
                System.out.println();
            }
            count++;
        }
    }



    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\simon\\OneDrive\\Dokumenter\\" +
                "NTNU\\Dataingeniør 2020-2021\\1. Semester\\IDATT2001 Algoritmer og datastrukturer\\Øvinger\\Øving 5\\l7g2"));

        Graph graph = new Graph(br);

        System.out.println("Component #    Nodes in component");
        graph.printSCCs();

    }
}
