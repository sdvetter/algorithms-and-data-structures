package Oblig1;

import java.util.Date;

public class Oppgave1 {


    public static void main(String []args){
        // Time
        Date start = new Date();
        int count = 0;
        double time;
        Date end;
        double r;

        do{
            r = exp(3, 14);
            end = new Date();
            count++;
        } while (end.getTime() - start.getTime() < 1000);
        time = (double) (end.getTime() - start.getTime())/count;
        System.out.println(time);
        System.out.println(r);

    }

    // Oppg 2.1-1
    static double exp(double x, int n){
        if (n == 0) return 1;
        return x * exp(x,n-1);
    }
}
