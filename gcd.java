import java.util.Scanner;

public class gcd {

    public static long greatestCommonDivisor(long a, long b) {
        // Convert to absolute values upfront
        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter first number: ");
        long a = s.nextLong();

        System.out.print("Enter second number: ");
        long b = s.nextLong();

        System.out.println("\nFirst number: " + a + "\nSecond number: " + b);
        System.out.println("GCD of the numbers is: " + greatestCommonDivisor(a, b));
        
        s.close();
    }
}

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
