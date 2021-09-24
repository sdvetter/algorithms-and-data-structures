package Oblig4.Oppgave1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class Map {
    private LinkedList<HashNode>[] array;
        public int size; // size of map
        static int collisionNr = 0;
        static int totalElements = 0;
        static HashNode[] arr = new HashNode[12500003];

    public Map(){
        this.size = arr.length;

        // Commented code to run Oppgave 1 Also important to add size to parameter in constructor
/*
        this.size = size;
        array = new LinkedList[size]; // Initialize the array
        for (int i = 0; i < size; i++){
            array[i] = null;
        }
 */
    }

    public  static void main(String[] args) throws IOException {
        Random r = new Random();
        Map map = new Map();
        HashMap hashMap = new HashMap();

        int[] test = new int[10 * 1000000];

        for (int i = 0; i < test.length; i++){
            test[i] = Math.abs(r.nextInt(987686998));
        }


        Date start = new Date();
        for (int i = 0; i < test.length; i++){
            map.put2(test[i], test[i]);
        }
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + "ms");

        System.out.println("Map size: " + map.size);
        System.out.println("Number of elements: " + totalElements);
        System.out.println("Number of collisions: " + collisionNr);
        System.out.println("Collisions per element: " + (double) collisionNr / totalElements);
        System.out.println("Load factor: " +  totalElements * 100 / map.size + "%");
        System.out.println(" ");
        System.out.println("Java's HashMap function");
        start = new Date();
        for (int i = 0; i < test.length; i++){
            hashMap.put(test[i], test[i]);
        }
        end = new Date();
        System.out.println(end.getTime() - start.getTime() + "ms");

    // Commented code that runs Oppgave 1
/*
        Map map = new Map(113);
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\simon\\Desktop\\nameListOblig4.txt"));
        String string;
        while((string = br.readLine()) != null)
            map.put(string, string);

        System.out.println("Map size: " + map.size);
        System.out.println("Number of elements: " + totalElements);
        System.out.println("Number of collisions: " + collisionNr);
        System.out.println("Load factor: " +  totalElements * 100 / map.size + "%");

        System.out.println(map.get("Simon Dreyer,Vetter"));
        System.out.println(map.get("Simno Dreyer,Vetter"));


 */

    }

    public void put2(Object key, Object value){
        if (isFull()) return;
        int hv = restHash(key);

        boolean t = true;
        if (arr[hv] != null){   // If element in given index already exists
            collisionNr++;
            while(t){
                if (secondHash(key) == 0) return; // If hashed value of key is 0, an infinite loop will occur
                hv = (hv + secondHash(key)) % size;     // Try for new hashed value
                if (arr[hv] == null){       // New index does not contain an element
                    arr[hv] = new HashNode(key, value);
                    totalElements++;
                    t = false;
                }
                collisionNr++;
            }
        } else {
            arr[hv] = new HashNode<>(key, value);   // If first index does not contain an element
            totalElements++;
        }
    }


    private boolean isFull(){
        return size == totalElements;
    }


    public void put(Object key, Object value){
        if (isFull()) return;

        int hv = restHash(key);
        LinkedList<HashNode> items = array[hv];

        if (items == null){     // If key value has not been added before
            items = new LinkedList<HashNode>();
            items.add(new HashNode(key, value));
            totalElements++;
            array[hv] = items;
        } else {
            for (HashNode hashNode: items){
                collisionNr++;
                if (hashNode.key.equals(key)){
                    hashNode.value = value; // updates node value
                    return;
                }
            }
            items.add(new HashNode(key, value));
            totalElements++;
        }
    }

    private int restHash(Object key){
        Integer keyInt = (Integer) key;
        return keyInt % size; // rest division function
    }

    private int secondHash(Object key){
        Integer keyInt = (Integer) key;
        return keyInt % ((size - 1 ) + 1 );
    }

    public void get2(Object key){
        int index1 = restHash(key);
        int index2 = secondHash(key);

        if (key == null){
            return;
        }

        int i = 0;
        while (arr[(index1 + i * index2 % size)].key != key){
            if (arr[(index1 + i * index2)] == null){
                System.out.println("Key does not exist");
                return;
            }
            i++;
        }
        System.out.println(arr[(index1 + i*index2 % size)].value);

    }

    public Object get(Object key){
        HashNode hashNode = getObject(key);
        if (hashNode == null){
            return null;
        }
        return hashNode.value;
    }

    private HashNode getObject(Object key){
        if (key == null){
            return null;
        }

        int hv = restHash(key);
        LinkedList<HashNode> items = array[hv];

        if (items == null){
            return null;
        }

        for (HashNode hashNode : items){
            if (hashNode.key.equals(key)){
                return hashNode;
            }
        }
        return null;
    }
}
