package Sensitive;

public class Bubble {
    public static void Sort(double[] a) {
        for (int i = 0; i < a.length-1; i++) {
            boolean flag=true;
            for (int j = 0; j < a.length-1-i; j++) {
                if (a[j]>a[j+1]) {
                    double temp=a[j];
                    a[j]=a[j+1];
                    a[j+1]=temp;
                    flag=false;
                }
            }
            if (flag) {
                break;
            }
        }
    }
}

