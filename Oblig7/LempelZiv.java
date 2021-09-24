package Oblig7;

import java.io.*;
import java.util.*;

public class LempelZiv {

    //4 bits for å lagre lengden på match
    public static final int look_ahead_buffer_size = (1 << 4) - 1;
    //7 bits for å lagre hvor langt bak for å finne referansen
    public static final int  window_size = (1 << 7) - 1;

    byte offset;
    byte length;
    byte[] data;
    ArrayList<Byte> data_list;
    LinkedList<Byte> searchBuffer;
    ArrayList<Byte> lookAheadBuffer;


    public void compress(String innfil_navn, String utfil_navn) throws IOException{
        DataInputStream innfil = new DataInputStream(new BufferedInputStream(new FileInputStream(innfil_navn)));
        DataOutputStream utfil = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(utfil_navn)));

        data = new byte[innfil.available()];
        innfil.readFully(data);
        innfil.close();

        data_list = new ArrayList<>();

        for (int i = 0; i <  data.length; i++) {
            data_list.add(data[i]);
        }

        //System.out.println((char)data_list.get(6).byteValue());

        searchBuffer = new LinkedList<>();
        lookAheadBuffer = new ArrayList<>();

        for (int i = 0; i < look_ahead_buffer_size; i++) {
            lookAheadBuffer.add(data_list.get(0));
            data_list.remove(0);
        }

        //System.out.println(lookAheadBuffer.get(6).byteValue());

        //hvor i searchbuffer vi er
        int sb_index = 0;
        //Hvor vi er i look ahead buffer
        int lab_index = 0;

        String sequence = "";

        ArrayList<Byte> uncomp = new ArrayList<>();

        //Får da ikke komprimert de siste 4 bitsene
        while (data_list.isEmpty() != true){

            boolean match = false;
            byte jumps = 0;

            //Gjøre slik at den ikke går altfor langt bak
            for (int i = searchBuffer.size() - 1; i >= 0; i--) {
                if (jumps == window_size){
                    break;
                }
                jumps ++;
                //Finner match
                if (searchBuffer.get(i).equals(lookAheadBuffer.get(lab_index))){
                    sequence = "";
                    match = true;
                    sb_index = i;
                    sequence += (char)searchBuffer.get(i).byteValue();

                    int index_count = 0;
                    boolean no_more_match = false;

                    //Ser så langt den kan etter hvor lang match sekvensen er
                    while (no_more_match == false){

                        index_count ++;
                        if ((index_count + sb_index < searchBuffer.size()) && (lab_index + index_count < look_ahead_buffer_size)){
                            if (searchBuffer.get(sb_index + index_count).equals(lookAheadBuffer.get(lab_index + index_count))){
                                sequence += (char)searchBuffer.get(sb_index + index_count).byteValue();
                            }
                            //Hvis neste tegn ikke er en match
                            else no_more_match = true;
                        }
                        //Hvis det ikke er mer å lese
                        else no_more_match = true;
                    }
                    //Vil bare ha sekvenser lengre enn 3
                    if ((sequence.length() > 3) && (sequence.length() < look_ahead_buffer_size)){

                        //Skriver inn de tegenen som ikke blir komprimert, ettersom vi nå finner en som blir komprimert
                        //System.out.println(uncomp.size());
                        //Vil ikke printe ut når uncomp ser tom
                        if (uncomp.size() > 0){
                            byte length_notComp = (byte)uncomp.size();

                            utfil.writeByte(length_notComp);
                            for (int j = 0; j < uncomp.size(); j++) {
                                utfil.writeByte(uncomp.get(j).byteValue());
                            }

                            uncomp = new ArrayList<>();
                        }
                        offset = jumps;
                        length = (byte)(sequence.length() - 1);
                        //System.out.println("offset: " + offset + "  length: " + length);
                        utfil.writeByte(-offset);
                        utfil.writeByte(length);

                        //Oppdaterer da search buffer og look ahead buffer
                        for (int j = 0; j < sequence.length(); j++) {
                            update_lab_and_sb();
                        }
                    }
                    else{
                        if (uncomp.size() == look_ahead_buffer_size){
                            //Skriver inn de tegenen som ikke blir komprimert, ettersom vi nå finner en som blir komprimert
                            //System.out.println(uncomp.size());

                            byte length_notComp = (byte)uncomp.size();

                            utfil.writeByte(length_notComp);
                            for (int j = 0; j < uncomp.size(); j++) {
                                utfil.writeByte(uncomp.get(j).byteValue());
                            }

                            uncomp = new ArrayList<>();
                        }
                        //utfil.writeByte(lookAheadBuffer.get(lab_index));
                        uncomp.add(lookAheadBuffer.get(lab_index));
                        //Oppdaterer da search buffer og look ahead buffer
                        update_lab_and_sb();
                    }
                    break;
                }
            }
            if (match == false){
                if (uncomp.size() == look_ahead_buffer_size){
                    //Skriver inn de tegenen som ikke blir komprimert, ettersom vi nå finner en som blir komprimert
                    //System.out.println(uncomp.size());

                    byte length_notComp = (byte)uncomp.size();

                    utfil.writeByte(length_notComp);
                    for (int j = 0; j < uncomp.size(); j++) {
                        utfil.writeByte(uncomp.get(j).byteValue());
                    }

                    uncomp = new ArrayList<>();
                }
                uncomp.add(lookAheadBuffer.get(lab_index));
                update_lab_and_sb();
            }
        }

        //Fordi vi ikke komprimerer de siste 4 bitsene, så legger vi alt som er igjen i uncomp
        for (int i = 0; i < lookAheadBuffer.size(); i++) {
            uncomp.add(lookAheadBuffer.get(i));
        }

        //Skriver ut da blokken som forteller hvor mange tegn som ikke er komprimert
        byte length_notComp = (byte)uncomp.size();
        utfil.writeByte(length_notComp);

        for (int j = 0; j < uncomp.size(); j++) {
            utfil.writeByte(uncomp.get(j).byteValue());
        }
        uncomp = new ArrayList<>();
        utfil.close();
    }

    public void unCompress(String innfil_navn, String utfil_navn) throws IOException{
        DataInputStream innfil = new DataInputStream(new BufferedInputStream(new FileInputStream(innfil_navn)));
        DataOutputStream utfil = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(utfil_navn)));

        data = new byte[innfil.available()];
        innfil.readFully(data);

        data_list = new ArrayList<>();

        for (int i = 0; i <  data.length; i++) {
            data_list.add(data[i]);
        }

        LinkedList<Byte> result = new LinkedList<>();

        while (data_list.isEmpty() != true){
            if (data_list.get(0) < 0){

                byte jumps = data_list.get(0);
                int index = result.size() + jumps;
                int length = data_list.get(1);

                byte singleByte = result.get(index).byteValue();

                result.addLast(singleByte);

                for (int i = 1; i <= length; i++) {
                    singleByte = result.get(index + i);
                    result.addLast(singleByte);
                }

                data_list.remove(0);
                data_list.remove(0);
            }
            else if(data_list.get(0) > 0){

                int length = data_list.get(0);
                data_list.remove(0);

                for (int i = 0; i < length; i++) {
                    if (data_list.isEmpty() != true){
                        result.addLast(data_list.get(0));
                        data_list.remove(0);
                    }
                }
            }
        }

        for (int i = 0; i < result.size(); i++) {
            utfil.writeByte(result.get(i).byteValue());
        }
        utfil.close();
    }

    public void update_lab_and_sb(){
        //Legger til elementente fra lab til sb
        searchBuffer.addLast(lookAheadBuffer.get(0));
        //Fjerner hodet i lista, hvis den den blir større enn 12 bits
        if (searchBuffer.size() == window_size){
            searchBuffer.remove();
        }

        lookAheadBuffer.remove(0);
        if (data_list.isEmpty() != true){
            lookAheadBuffer.add(data_list.get(0));
            data_list.remove(0);
        }
    }
}
