import java.util.Scanner;

public class mul_inv {
    static int gcd(int n, int m, int[] x, int[] y){
        if(n==0){
            x[0]=0;
            y[0]=1;
            return m;
        }
        int[] x1=new int[1];
        int[] y1 = new int[1];
        int g = gcd(m%n,n,x1,y1);

        x[0]=y1[0]-(m/n)*x1[0];
        y[0]=x1[0];
        return g;
    }

    static int inverse(int n, int m){
        int[] x = new int[1];
        int[] y = new int[1];

        int g = gcd(n, m, x, y);
        if(g!=1)  return -1;
        else{
            int r = (x[0]%m+m)%m;
            return r;
        }
    }
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter n and m for finding mul inv of n: ");
        int n = s.nextInt(), m = s.nextInt();
        System.out.println("Inverse of "+n+" is: "+inverse(n,m));
        s.close();
    }
}
