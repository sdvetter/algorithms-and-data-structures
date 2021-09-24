package Oblig7;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Huffman {
    public Huffman() throws IOException {}


    public static void compress(String inputfile, String outputFile) throws IOException {
        int count[] = new int[256];

        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(inputfile));
        int remainingByte = dataInputStream.available();
        for (int i = 0; i < remainingByte; ++i) {
            int c = dataInputStream.read();
            count[c]++;
        }
        dataInputStream.close();
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(256, (a, b) -> a.frequency - b.frequency);
        priorityQueue.addAll(makeNodeList(count));
        HuffmanNode huffmanNode = createHuffmanTree(priorityQueue);
        huffmanNode.initNode(huffmanNode, "");

        FileInputStream fileInputStream = new FileInputStream(inputfile);
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(outputFile));

        for (int t : count) {
            dataOutputStream.writeInt(t);
        }
        int input;
        long writeByte = 0L;
        int i = 0;
        int j = 0;
        ArrayList<Byte> bytes = new ArrayList<>();
        for (int k = 0; k < remainingByte; ++k) {
            input =Math.abs(fileInputStream.read());
            j = 0;
            String bitString = huffmanNode.bits[input];
            while (j < bitString.length()) {
                if (bitString.charAt(j) == '0') writeByte = (writeByte << 1);
                else writeByte = ((writeByte << 1 ) |1);
                j++;
                i++;
                if (i == 8) {
                    bytes.add( (byte) writeByte);
                    i = 0;
                    writeByte = 0L;
                }
            }
        }
        int lastByte = i;
        while (i < 8 && i != 0) {
            writeByte = (writeByte << 1);
            ++i;
        }
        bytes.add( (byte) writeByte);
        dataOutputStream.writeInt(lastByte);
        for (Byte b : bytes) {
            dataOutputStream.write(b);
        }
        fileInputStream.close();
        dataOutputStream.close();
    }


    static void decompress(String file, String outputFile) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(new File(outputFile)));

        int [] count = new int [256];
        for (int i = 0; i < count.length; i++) {
            int freq = dataInputStream.readInt();
            count[i] = freq;
        }

        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(256, (a, b) -> a.frequency - b.frequency);
        priorityQueue.addAll(makeNodeList(count));
        HuffmanNode huffmanNode = createHuffmanTree(priorityQueue);

        int remainingByte = dataInputStream.readInt();
        byte charByte;
        byte [] bytes = dataInputStream.readAllBytes();
        dataInputStream.close();
        int length = bytes.length;
        Bitstring bitstring = new Bitstring(0, 0);
        if(remainingByte > 0) length--;
        for (int i = 0; i <length; i++) {
            charByte = bytes[i];
            Bitstring bs = new Bitstring(8, charByte);
            bitstring = Bitstring.concat(bitstring,bs);
            bitstring = writeChar(huffmanNode, bitstring, dataOutputStream);
        }
        if(remainingByte>0){
            Bitstring b = new Bitstring(remainingByte, bytes[length]>>(8-remainingByte));
            bitstring = Bitstring.concat(bitstring, b);
            writeChar(huffmanNode, bitstring, dataOutputStream);
        }
        dataInputStream.close();
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    private static Bitstring writeChar(HuffmanNode huffmanNode ,Bitstring bitstring, DataOutputStream dataOutputStream) throws IOException {
        HuffmanNode hNode = huffmanNode;
        int c=0;
        for (long j = 1 << bitstring.length - 1; j != 0; j >>= 1) {
            c++;
            if((bitstring.numberOfBits & j) == 0) hNode = hNode.left;
            else hNode = hNode.right;
            if(hNode.left == null){
                long charL = hNode.c;
                dataOutputStream.write((byte) charL);
                long temp = ~ (0 << (bitstring.length - c));
                bitstring.numberOfBits = (bitstring.numberOfBits & temp);
                bitstring.length = bitstring.length - c;
                c = 0;
                hNode = huffmanNode;
            }
        }
        return bitstring;
    }

    private static ArrayList<HuffmanNode> makeNodeList(int[] count) {
        ArrayList<HuffmanNode> nodeList = new ArrayList<>();
        for (int i = 0; i < count.length; i++) {
            if(count[i] != 0){
                nodeList.add(new HuffmanNode(count[i], (char) i , null, null));
            }
        }
        return nodeList;
    }

    private static HuffmanNode createHuffmanTree(PriorityQueue<HuffmanNode> priorityQueue){
        int count[] = new int[256];
        HuffmanNode root = new HuffmanNode();
        for (int i = 0; i < count.length; i++){
            if (count[i] != 0){
                HuffmanNode huffmanNode = new HuffmanNode(count[i], (char) i, null, null);
                priorityQueue.add(huffmanNode);
            }
        }
        while (priorityQueue.size() > 1){
            HuffmanNode huffmanNode1 = priorityQueue.poll();
            HuffmanNode huffmanNode2 = priorityQueue.poll();
            HuffmanNode parentNode = new HuffmanNode((huffmanNode1.frequency + huffmanNode2.frequency), '\0',  huffmanNode2, huffmanNode1);
            priorityQueue.add(parentNode);
            root = parentNode;
        }
        return root;
    }
}


class HuffmanNode {
    int frequency;
    char c;
    HuffmanNode left;
    HuffmanNode right;
    String[] bits;

    public HuffmanNode(){
        bits = new String[256];
    }

    public HuffmanNode(int frequency, char c, HuffmanNode left, HuffmanNode right){
        this.frequency = frequency;
        this.c = c;
        this.left = left;
        this.right = right;
        bits = new String[256];
    }

    public void initNode(HuffmanNode root, String s){
        if (root.left != null && root.right != null) {
            initNode(root.left, s + "0");
            initNode(root.right, s + "1");
        } else {
            bits[root.c] = s;
        }
    }

}

