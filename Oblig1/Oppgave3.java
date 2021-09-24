package Oblig1;

import java.util.Date;

public class Oppgave3 {

    public static void main(String []args){
        Date start = new Date();
        int count = 0;
        double time;
        Date end;

        do{
            double r = Math.pow(0.58, 10000);
            end = new Date();
            count++;
        } while (end.getTime() - start.getTime() < 1000);
        time = (double) (end.getTime() - start.getTime())/count;
        System.out.println(time);
    }
}
