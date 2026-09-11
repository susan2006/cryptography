import java.util.Scanner;

public class MillerRabinTest {

    public static long modPow(long base, long exp, long mod) {
        long res = 1;
        base = base % mod;

        while (exp > 0) {
            if ((exp & 1) == 1) {
                res = (res * base) % mod;
            }
            exp >>= 1;
            base = (base * base) % mod;
        }
        return res;
    }

    public static boolean isProbablyPrime(long n, long a) {
        // Base edge cases
        if (n <= 1) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0) return false;

        // Ensure base 'a' is valid: 1 < a < n-1
        a = a % n;
        if (a <= 1) a = 2;

        // -------------------------------------------------------------
        // Step 1: Write n - 1 = (2^k) * m where m is odd
        // -------------------------------------------------------------
        long m = n - 1;
        int k = 0;
        while (m % 2 == 0) {
            m /= 2;
            k++;
        }

        System.out.println("\n--- STEP 1: DECOMPOSITION ---");
        System.out.println("n - 1 = " + (n - 1) + " = 2^" + k + " * " + m);
        System.out.println("k = " + k + ", m = " + m);

        // -------------------------------------------------------------
        // Step 2: Compute b0 = a^m mod n
        // -------------------------------------------------------------
        long b = modPow(a, m, n);

        System.out.println("\n--- STEP 2: INITIAL CHECK ---");
        System.out.println("b0 = (" + a + "^" + m + ") mod " + n + " = " + b);

        // In mod n arithmetic, -1 mod n is represented as (n - 1)
        if (b == 1 || b == (n - 1)) {
            System.out.println("Result: b0 is " + (b == 1 ? "1" : "-1 (n-1)") + " -> " + n + " is PROBABLY PRIME.");
            return true;
        }

        // -------------------------------------------------------------
        // Step 3: Iterative Squaring: bi = (bi-1)^2 mod n
        // -------------------------------------------------------------
        System.out.println("\n--- STEP 3: ITERATIVE SQUARING ---");
        for (int i = 1; i < k; i++) {
            b = (b * b) % n;
            System.out.println("b" + i + " = (b" + (i - 1) + ")^2 mod " + n + " = " + b);

            if (b == (n - 1)) { // Found -1 mod n
                System.out.println("Result: b" + i + " is -1 (n-1) -> " + n + " is PROBABLY PRIME.");
                return true;
            }
            if (b == 1) { // Hit 1 without hitting -1 first -> Composite
                System.out.println("Result: b" + i + " hit 1 without preceding -1 -> " + n + " is COMPOSITE.");
                return false;
            }
        }

        System.out.println("Result: Loop finished without hitting -1 -> " + n + " is COMPOSITE.");
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number to test (n): ");
        long n = scanner.nextLong();

        System.out.print("Enter base value (a, e.g., 2, 3, 5): ");
        long a = scanner.nextLong();

        boolean result = isProbablyPrime(n, a);

        System.out.println("\n==========================================");
        System.out.println("FINAL VERDICT: " + n + " is " + (result ? "PROBABLY PRIME" : "COMPOSITE"));
        System.out.println("==========================================");

        scanner.close();
    }
}