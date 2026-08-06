import java.util.Scanner;

public class FastExponentiation {

    /**
     * Calculates (base^exp) % mod using Binary Exponentiation
     * Time Complexity: O(log exp)
     * Space Complexity: O(1)
     */
    public static long calculate(long base, long exp, long mod) {
        if (mod <= 0) {
            throw new IllegalArgumentException("Modulo must be a positive integer.");
        }
        if (exp < 0) {
            throw new IllegalArgumentException("Exponent cannot be negative.");
        }

        long result = 1;

        // Ensure base is positive in range [0, mod - 1]
        base = (base % mod + mod) % mod;

        while (exp > 0) {
            // Check if exponent is odd (bitwise AND)
            if ((exp & 1) == 1) {
                result = (result * base) % mod;
            }

            // Halve the exponent (right bit shift)
            exp >>= 1;

            // Square the base
            base = (base * base) % mod;
        }

        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter base number: ");
        long base = scanner.nextLong();

        System.out.print("Enter exponent: ");
        long exp = scanner.nextLong();

        System.out.print("Enter modulo value: ");
        long mod = scanner.nextLong();

        try {
            long result = calculate(base, exp, mod);

            System.out.println("\n--- CALCULATION RESULT ---");
            System.out.println("(" + base + "^" + exp + ") % " + mod + " = " + result);

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
/*public class FastExponentiation {
    public static long calculate(long base, long exp, long mod) {
        long result = 1;
        base = base % mod; 
        while (exp > 0) {
            // If the exponent is odd, multiply the base with the result
            if ((exp % 2) == 1) {
                result = (result * base) % mod;
            }
            // Square the base and halve the exponent for the next iteration
            exp = exp / 2; 
            base = (base * base) % mod;
        }
        return result;
    }
    public static void main(String[] args) {
        long base = 6;
        long exp = 109;
        long mod = 25;
        long result = calculate(base, exp, mod);
        System.out.println("Base: " + base);
        System.out.println("Exponent: " + exp);
        System.out.println("Modulo: " + mod);
        System.out.println("Result: " + result); 
    }
}*/
