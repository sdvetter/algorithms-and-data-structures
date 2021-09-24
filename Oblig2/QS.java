package Oblig2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static Oblig2.Alternativ1.dpQuicksort;
import static Oblig2.Alternativ1.quicksort;

public class QS {

    public static void main(String args[]){

        Random rand = new Random();
        int nrOfElements = 10000000;

        int randomArray[] = new int[nrOfElements];
        int reoccuringArray[] = new int[nrOfElements];
        int test[] = {1,7,2,8,3,6,8,0,6,2};
        int test2[] = new int[14];
        List<Integer> sortArray = new ArrayList<>();
        List<Integer> sortArray2 = new ArrayList<>();

        for (int i = 0; i <test.length; i++){
            sortArray.add(test[i]);
            test2[i] = test[i];
            sortArray2.add(test[i]);

        }

        double initCount1 = 0;
        double initCount2 = 0;

        // Array filled with random numbers
        for (int i = 0; i <nrOfElements; i++){
            randomArray[i] = rand.nextInt(1000000)+1;
            initCount1 += randomArray[i];
        }

        // Array filled with recurring numbers
        for (int i = 0; i <nrOfElements; i++){
            if (i%10 == 0){
                reoccuringArray[i] = 5;
            }
            reoccuringArray[i] = rand.nextInt(1000000)+1;
            initCount2 += reoccuringArray[i];
        }


/*
        Date start = new Date();
        dpQuicksort(reoccuringArray, 0 , nrOfElements-1);
        Date end = new Date();
        System.out.println(end.getTime()-start.getTime() + " ms");
        if (initCount2 == sum(reoccuringArray)){
            System.out.println("godkjent");
        }

 */

/*
        start = new Date();
        dpQuicksort(reoccuringArray,0,nrOfElements-1);
        end = new Date();
        System.out.println(end.getTime()-start.getTime());

 */

/*
        for (int i = 0; i < arr.length; i++){
            sortedArray.add(arr[i]);
        }
        System.out.println(sortedArray);
 */


    // Random test data
        System.out.println("Quicksort");
        System.out.println("");
        System.out.println(sortArray +" Before sorting, sum: "+ sum(test));
        quicksort(test, 0 , 9);
        sortArray.clear();
        for (int i = 0; i <test.length; i++){
            sortArray.add(test[i]);
        }
        System.out.println(sortArray + " After sorting, sum: "+ sum(test));

        System.out.println("");
        System.out.println("Dual pivot quicksort");
        System.out.println(sortArray2 +" Before sorting, sum: "+ sum(test2));
        dpQuicksort(test2, 0 , 9);
        sortArray2.clear();
        for (int i = 0; i <test.length; i++){
            sortArray2.add(test[i]);
        }
        System.out.println(sortArray2 + " After sorting, sum: "+ sum(test));


        for (int i = 0; i < test.length-1; i++){
            if (test[i] > test[i+1]){
                System.out.println("Feil med algoritme");
            }
            break;
        }
    }

    static double sum(int t[]){
        double count = 0;
        for (int i = 0; i < t.length; i++){
            count += t[i];
        }
        return count;
    }

}
