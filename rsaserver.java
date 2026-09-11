import java.io.*;
import java.net.*;
import java.util.*;

public class rsaserver {
    public static void main(String[] args) {
        Scanner s=new Scanner(System.in);
        int port = 5000;
        System.out.print("Enter p:");
        long p = s.nextLong();
        System.out.print("Enter p:");
        long q = s.nextLong();

        long n = p * q;                      
        long phi = (p - 1) * (q - 1);        

        System.out.print("Enter e:");
        long e = s.nextLong();
        while (rsaengine.gcd(e, phi) != 1) {
            e += 2;
        }

        long d = rsaengine.modInverse(e, phi);

        System.out.println("[+] Prime p       : " + p);
        System.out.println("[+] Prime q       : " + q);
        System.out.println("[+] Modulus (n)   : " + n);
        System.out.println("[+] Phi(n)        : " + phi);
        System.out.println("[+] Public Key (e): " + e);
        System.out.println("[+] Private Key(d): " + d);
        System.out.println("\nWaiting for client connection...\n");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                System.out.println("[+] Client connected!");

                // Send Public Key (e, n) to Client
                out.writeLong(e);
                out.writeLong(n);
                System.out.println("--> Public Key sent to client: (e=" + e + ", n=" + n + ")");
                
                long ciphertext = in.readLong();
                System.out.print("\n--> Received Ciphertext : "+ ciphertext);

                // Decrypt each block: M = C^d mod n
                long decryptedText = rsaengine.modPow(ciphertext, d, n);

                System.out.println("\n[Server Decryption Result]");
                System.out.println("--> Recovered Plaintext: " + decryptedText);

                out.writeLong(decryptedText);

            } catch (EOFException ex) {
                System.out.println("Client disconnected.");
            }
        } catch (IOException ex) {
            System.err.println("Server Error: " + ex.getMessage());
        }
    }
}