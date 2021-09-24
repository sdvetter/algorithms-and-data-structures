package Oblig7;
// The Decompressor class should decompress first with hoffman then LZ

import java.io.File;
import java.io.IOException;

public class Decompressor {

    public static void main (String[] args) throws IOException {
        String huffAndLZCompressed = "huffAndLZCompressed.txt";
        String huffmanDecompressed = "huffmanDecompressed.txt";
        String lastFile = "Victory.txt";

        LempelZiv lempelZiv = new LempelZiv();
        Huffman.decompress(huffAndLZCompressed, huffmanDecompressed);
        lempelZiv.unCompress(huffmanDecompressed, lastFile);
    }

}
