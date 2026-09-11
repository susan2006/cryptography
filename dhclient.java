import java.io.*;
import java.net.*;
import java.util.Scanner;

public class dhclient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Diffie-Hellman Server.\n");

            // 1. Receive domain parameters and Server Public Key from server
            long p = in.readLong();
            long g = in.readLong();
            long B = in.readLong();

            System.out.println("--> Received from Server: p = " + p + ", g = " + g + ", B = " + B);

            // 2. Prompt client for its private key
            System.out.print("\nEnter Client Private Key (a, e.g., 45): ");
            long a = scanner.nextLong();

            // 3. Compute Client Public Key: A = g^a mod p
            long A = DHEngine.modPow(g, a, p);
            System.out.println("Client Public Key (A) : " + A);

            // 4. Send Public Key (A) to Server
            out.writeLong(A);
            System.out.println("--> Transmitted Client Public Key (A) to server.");

            // 5. Compute Shared Secret: K = B^a mod p
            long sharedKey = DHEngine.modPow(B, a, p);

            System.out.println("\n------------------------------------------");
            System.out.println("[+] Established Shared Key : " + sharedKey);
            System.out.println("------------------------------------------");

            String serverMsg = in.readUTF();
            System.out.println("[Server Response] " + serverMsg);

        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }
}