package Oblig1;

import java.util.Date;

public class Oppgave2 {

    public static void main(String []args){
        Date start = new Date();
        int count = 0;
        double time;
        Date end;
        double r;

        do{
            r = xnp(3, 14);
            end = new Date();
            count++;
        } while (end.getTime() - start.getTime() < 1000);
        time = (double) (end.getTime() - start.getTime())/count;
        System.out.println(time);
        System.out.println(r);
    }

    // Oppg 2.2-3
    static double xnp(double x, int n){
        if (n == 0) return 1;   // basis
        else if ((n % 2) == 0 && n >= 2) return xnp(x*x,n/2);        // partall
        else return x*xnp(x*x,(n-1)/2);       // oddetall
    }



}
