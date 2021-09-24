package Oblig4.Oppgave1;

public class HashNode<K, V> {
    K key;
    V value;
    HashNode<K,V> next;

    public HashNode(K key, V value){
        this.key = key;
        this.value = value;
    }

    public K getKey(){
        return this.key;
    }

    public V getValue(){
        return this.value;
    }

}
