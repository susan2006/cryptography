import java.util.Scanner;

public class gcd {

    public static long greatestCommonDivisor(long a, long b){
        if(b == 0){
            return Math.abs(a);
        }

        return Math.abs(greatestCommonDivisor(b, a % b));
    }
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);

        System.out.print("Enter first number: ");
        long a = s.nextLong();

        System.out.print("Enter second number: ");
        long b = s.nextLong();

        System.out.println("\nFirst number: " + a + "\nSecond number: " + b);
        System.out.println("\nGCD of the numbers is: " + greatestCommonDivisor(a, b));
        s.close();
    }
    
}
