import java.util.*;

public class rsaclient {
    public static long gcdForTwo(long a, long b){
        while(b!=0){
            long temp = a%b;
            a=b;
            b=temp;
        }
        return a;
    }
    public static long gcdForInv(long n, long m,long[] x, long[] y){
        if(n==0){
            x[0]=0;
            y[0]=1;
            return m;
        }
        long[] x1=new long[1];
        long[] y1=new long[1];
        long g=gcdForInv(m%n,n,x1,y1);
        x[0]=y1[0]-(m/n)*x1[0];
        y[0]=x1[0];
        return g;
    }
    public static long inv(long n, long m){
        long[] x=new long[1];
        long[] y=new long[1];
        long g=gcdForInv(n,m,x,y);
        if(g!=1)  return -1;
        else{
            long r =(x[0]%m+m)%m;
            return r;
        }
    }
    public static long fast(long base, long exp, long mod){
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
    public static String encrypt(String pt,long e, long n){
        long msg=Long.parseLong(pt);
        String ct=Long.toString(fast(msg,e,n));
        return ct;
    }
    public static String decrypt(String ct, long d, long n){
        long cip=Long.parseLong(ct);
        String dt=Long.toString(fast(cip,d,n));
        return dt;
    }
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        System.out.print("Enter p: ");
        long p = s.nextLong();
        System.out.print("Enter q: ");
        long q = s.nextLong();
        
        long n = p * q;
        System.out.println("n: "+n);
        long phi_n = (p-1)*(q-1);
        System.out.println("phi_n: "+phi_n);
        long e;
        while(true){
            System.out.print("Enter encryption key e: ");
            e=s.nextLong();
            if(gcdForTwo(e,phi_n)==1){
                System.out.println("Entered key is ok.");
                break;
            }else{
                System.out.println("Entered key is not ok, enter again.");
            }
        }
        long d = inv(e, phi_n);
        System.out.print("Enter text to encrypt: ");
        String pt=s.nextLine();
        String ct=encrypt(pt,e,n);
        System.out.println("Cipher text: "+ct);
        String dt=decrypt(ct,d,n);
        System.out.println("Decrypted text: "+dt);
        s.close();
    }
}
