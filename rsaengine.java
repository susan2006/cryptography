public class rsaengine {

    // 1. Greatest Common Divisor (Euclidean Algorithm)
    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // 2. Extended Euclidean Algorithm to calculate Modular Inverse (d = e^-1 mod phi)
    public static long modInverse(long e, long phi) {
        long t0 = 0, t1 = 1;
        long r0 = phi, r1 = e;

        while (r1 > 0) {
            long q = r0 / r1;
            
            long tempR = r0 - q * r1;
            r0 = r1;
            r1 = tempR;

            long tempT = t0 - q * t1;
            t0 = t1;
            t1 = tempT;
        }

        if (r0 > 1) return -1; // Inverse does not exist
        return (t0 < 0) ? (t0 + phi) : t0;
    }

    // 3. Fast Modular Exponentiation (base^exp mod mod)
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
}