package Oblig7;

public class Bitstring {
    int length;
    long numberOfBits;

    public Bitstring(int length, long numberOfBits){
        this.length = length;
        this.numberOfBits = numberOfBits;
    }

    public Bitstring(int length, byte b){
        this.length = length;
        this.numberOfBits = convertByte(b, length);
    }

    public Bitstring(){}

    static Bitstring concat(Bitstring b1, Bitstring b2){
        Bitstring bitstring = new Bitstring();
        bitstring.length = b1.length + b2.length;

        if (bitstring.length + b2.length > 64) return null;
        bitstring.numberOfBits = b2.numberOfBits | (b1.numberOfBits << b2.length);
        return bitstring;
    }

    public long convertByte(byte b, int length){
        long temp = 0;
        for(long i = 1<<length-1; i != 0; i >>= 1){
            if((b & i) == 0){
                temp = (temp << 1);
            }
            else temp = ((temp << 1) | 1);
        }
        return temp;
    }


}
