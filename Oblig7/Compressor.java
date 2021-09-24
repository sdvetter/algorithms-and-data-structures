package Oblig7;

import java.io.File;
import java.io.IOException;

public class Compressor {
    // The compressor class should compress a file using both Lempel-Ziv and Hoffman

    public static void main (String[] args) throws IOException {
        String path = "diverse.txt";
        String lempelCompressed = "lempelCompressed.txt";
        String huffAndLZCompressed = "huffAndLZCompressed.txt";

        LempelZiv lempelZiv = new LempelZiv();
        lempelZiv.compress(path, lempelCompressed);
        Huffman.compress(lempelCompressed, huffAndLZCompressed);
    }
}
