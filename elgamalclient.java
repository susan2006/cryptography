import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class elgamalclient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 5000;
        Random rand = new Random();

        try (Socket socket = new Socket(host, port);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to ElGamal Server.");

            // 1. Receive Server Public Parameters: p, g, y
            long p = in.readLong();
            long g = in.readLong();
            long y = in.readLong();

            System.out.println("--> Received Parameters: p = " + p + ", g = " + g + ", y = " + y);

            // 2. Get Plaintext Input
            System.out.print("\nEnter Plaintext Message to Encrypt: ");
            long m = scanner.nextLong();
            System.out.print("\nEnter Plaintext small k: ");
            long k=scanner.nextLong();
            /* 
            if(elgamalengine.gcd(k,p-1)!=1){
                do {
                    k = 2 + (long) (rand.nextDouble() * (p - 3));
                } while (elgamalengine.gcd(k, p - 1) != 1);
            }
            */
            long c1=elgamalengine.modPow(g, k, p);
            long s = elgamalengine.modPow(y, k, p);
            long c2=(m * s) % p;

            System.out.println("\n[Client Local Processing]");
            System.out.println("Generated Ciphertext Pairs (c1, c2):" +c1 +"  "+c2);

            // 4. Send Ciphertext Pairs to Server
            System.out.println("\n--> Transmitting Ciphertext to Server...");
            out.writeLong(c1);
            out.writeLong(c2);

            // 5. Read Server Confirmation
            long serverResponse = in.readLong();
            System.out.println("\n[Server Response] " + serverResponse);

        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }
}