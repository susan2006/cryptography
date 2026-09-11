import java.io.*;
import java.net.*;
import java.util.*;

public class elgamalserver {
    public static void main(String[] args) {
        int port = 5000;
        Scanner s=new Scanner(System.in);
        System.out.print("Enter p:");
        long p = s.nextLong(); 
        System.out.print("Enter g:");
        long g = s.nextLong(); 

        System.out.print("\nEnter Server Private Key (b, e.g., 97): ");
        long x = s.nextLong();

        // Step 3: Compute Server Public Key component y = g^x mod p
        long y = elgamalengine.modPow(g, x, p);

        System.out.println("[+] Domain Parameter (p) : " + p);
        System.out.println("[+] Generator (g)        : " + g);
        System.out.println("[+] Public Key (y)       : " + y);
        System.out.println("[+] Private Key (x)      : " + x + " (kept secret)");
        System.out.println("Waiting for client connection...\n");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     DataInputStream in = new DataInputStream(socket.getInputStream());
                     DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                    System.out.println("[+] Client connected!");

                    // Send public parameters (p, g, y) to the client
                    out.writeLong(p);
                    out.writeLong(g);
                    out.writeLong(y);
                    System.out.println("--> Public Parameters (p, g, y) sent to client.");
                    
                    System.out.println("\n[Received Ciphertext from Client]");
                    long c1 =in.readLong();
                    long c2 = in.readLong();
                    System.out.println("c1:"+c1+"c2:"+c2);

                    // Decrypt each block:
                    // 1. s = (c1^x) mod p
                    // 2. s_inv = s^-1 mod p
                    // 3. M = (c2 * s_inv) mod p
                    long k = elgamalengine.modPow(c1, x, p);
                    long sInv = elgamalengine.modInverse(k, p);
                    long m = (c2 * sInv) % p;
                    
                    System.out.println("\n[Server Decryption Result]");
                    System.out.println("--> Recovered Plaintext: " + m);

                    // Send response back
                    out.writeLong(m);
                    System.out.println("------------------------------------------");
                }
            }
        } catch (IOException e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }
}