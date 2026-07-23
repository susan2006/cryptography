import java.util.Scanner;
public class Coprime {
    public static int gcd(int x,int y){
          while(y!=0){
            int t=x;
            x=y;
            y=t%x;
          }
          return x;
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
           System.out.println("enter two numbers to find gcd");
           int a=sc.nextInt();
           int b=sc.nextInt();
        if(gcd(a,b)==1) System.out.println(a+" "+b+" are coprimes");
        else System.out.println("Not coprimes");
    }
}
