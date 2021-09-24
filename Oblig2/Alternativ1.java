package Oblig2;

public class Alternativ1 {

    public static void quicksort(int t[], int low, int high){
        if (high - low > 2 ){
            int partitionPos = partition(t, low, high);
            quicksort(t, low, partitionPos - 1);
            quicksort(t, partitionPos +1, high);
        } else median3sort(t, low, high);
    }


    public static void switchP(int []t, int i, int j){
        int k = t[j];
        t[j] = t[i];
        t[i] = k;
    }

    // pv - partition value

    public static int partition(int []t, int low, int high){
        int iv, ih;
        int m = median3sort(t, low, high);
        int pv = t[m];
        switchP(t, m, high-1);
        for (iv = low, ih = high-1;;){
            while (t[++iv] < pv);
            while (t[--ih] > pv);
            if (iv >= ih) break;
            switchP(t, iv, ih);
        }
        switchP(t, iv, high-1);
        return iv;
    }

    public static void dpQuicksort(int t[], int low, int high){
        if (low < high){

            int[] dpPartitions = dualPartition(t, low, high);
            dpQuicksort(t, low, dpPartitions[0] - 1);
            dpQuicksort(t, dpPartitions[0] + 1, dpPartitions[1] - 1);
            dpQuicksort(t, dpPartitions[1] + 1, high);
        }
    }

    public static int[] dualPartition(int t[], int low, int high) {

        int lpv, hpv;
        lpv = (low + (high - low) / 3);
        hpv = (high - (high - low) / 3);

        switchP(t, low, lpv);
        switchP(t, high, hpv);

        if (t[low] > t[high]) switchP(t, low, high);

        // p is the left pivot, and q
        // is the right pivot.
        int j = low + 1;
        int g = high - 1, k = low + 1,
                p = t[low], q = t[high];

        while (k <= g) {

            // If elements are less than the left pivot
            if (t[k] < p) {
                switchP(t, k, j);
                j++;
            }

            // If elements are greater than or equal
            // to the right pivot
            else if (t[k] >= q) {
                while (t[g] > q && k < g)
                    g--;

                switchP(t, k, g);
                g--;

                if (t[k] < p) {
                    switchP(t, k, j);
                    j++;
                }
            }
            k++;
        }
        j--;
        g++;

        // Bring pivots to their appropriate positions.
        switchP(t, low, j);
        switchP(t, high, g);

        return new int[]{j, g};
    }

    public static int median3sort(int []t, int low, int high){
        int m = (low +high)/2;
        if (t[low] > t[m]) switchP(t, low, m);
        if (t[m] > t[high]){
                switchP(t, m, high);
                if (t[low] > t[m]) switchP(t, low, m);
        }
        return m;
    }
}
