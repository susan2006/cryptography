import java.math.BigInteger;

public class FermatTestAllBases {

    public static boolean isPrimeAllBases(long p) {
        if (p <= 1) return false;
        if (p <= 3) return true;

        BigInteger bigP = BigInteger.valueOf(p);

        // Test every base 'a' from 1 to p - 1
        for (long a = 1; a < p; a++) {
            BigInteger bigA = BigInteger.valueOf(a);

            // Compute x = a^p - a directly
            BigInteger aPowP = bigA.pow((int) p);
            BigInteger x = aPowP.subtract(bigA);

            // Check if x is a multiple of p (x % p == 0)
            if (!x.remainder(bigP).equals(BigInteger.ZERO)) {
                System.out.printf("Failed at a = %d: x is not a multiple of %d%n", a, p);
                return false; // Composite
            }
        }

        return true; // Prime
    }

    public static void main(String[] args) {
        long p = 7;

        System.out.println("Testing p = " + p + " for all a in [1, " + (p - 1) + "]:");
        
        // Print full trace for p = 7
        BigInteger bigP = BigInteger.valueOf(p);
        for (long a = 1; a < p; a++) {
            BigInteger bigA = BigInteger.valueOf(a);
            BigInteger x = bigA.pow((int) p).subtract(bigA);
            System.out.printf("a = %d -> x = %s -> Divisible by %d? %b%n", 
                a, x.toString(), p, x.remainder(bigP).equals(BigInteger.ZERO));
        }

        System.out.println("\nResult: " + p + " is " + (isPrimeAllBases(p) ? "Prime" : "Composite"));
    }
}
