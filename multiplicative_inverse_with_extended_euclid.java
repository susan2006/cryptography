public class GFG {

    // Function for extended Euclidean Algorithm
    static int gcdExtended(int a, int b, int[] x, int[] y)
    {
        // Base Case
        if (a == 0) {
            x[0] = 0;
            y[0] = 1;
            return b;
        }

        // To store results of recursive call
        int[] x1 = new int[1];
        int[] y1 = new int[1];
        int gcd = gcdExtended(b % a, a, x1, y1);

        // Update x and y using results of recursive call
        x[0] = y1[0] - (b / a) * x1[0];
        y[0] = x1[0];

        return gcd;
    }

    static int modInverse(int n, int m)
    {
        int[] x = new int[1];
        int[] y = new int[1];

        int g = gcdExtended(n, m, x, y);
        if (g != 1)
            return -1;
        else {

            // m is added to handle negative x
            int res = (x[0] % m + m) % m;
            return res;
        }
    }

    public static void main(String[] args)
    {
        int n = 3, m = 11;

        System.out.println(modInverse(n, m));
    }
}
