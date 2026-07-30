public class FastExponentiation {
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
}
