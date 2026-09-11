public class elgamalengine {

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

    public static long modInverse(long a, long m) {
        long m0 = m;
        long y = 0, x = 1;

        if (m == 1) return 0;

        while (a > 1) {
            long q = a / m;
            long t = m;

            m = a % m;
            a = t;
            t = y;

            y = x - q * y;
            x = t;
        }

        if (x < 0) x += m0;
        return x;
    }

    // Greatest Common Divisor
    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}