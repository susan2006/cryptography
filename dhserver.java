import java.io.*;
import java.net.*;
import java.util.Scanner;

public class dhserver {
    public static void main(String[] args) {
        int port = 5000;
        Scanner s=new Scanner(System.in);
        System.out.print("Enter p:");
        long p = s.nextLong(); 
        System.out.print("Enter g:");
        long g = s.nextLong();   

        try (ServerSocket serverSocket = new ServerSocket(port);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Public parameters: p = " + p + ", g = " + g);

            System.out.print("\nEnter Server Private Key (b, e.g., 97): ");
            long b = scanner.nextLong();

            // Calculate Server's Public Key: B = g^b mod p
            long B = DHEngine.modPow(g, b, p);
            System.out.println("Server Public Key (B) : " + B);
            System.out.println("Waiting for client connection...\n");

            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                System.out.println("[+] Client connected!");

                // 1. Send domain parameters (p, g) and Server Public Key (B)
                out.writeLong(p);
                out.writeLong(g);
                out.writeLong(B);
                System.out.println("--> Sent p, g, and Server Public Key (B) to client.");

                // 2. Receive Client's Public Key (A)
                long A = in.readLong();
                System.out.println("--> Received Client Public Key (A): " + A);

                // 3. Compute Shared Secret: K = A^b mod p
                long sharedKey = DHEngine.modPow(A, b, p);

                System.out.println("\n------------------------------------------");
                System.out.println("[+] Established Shared Key : " + sharedKey);
                System.out.println("------------------------------------------");

                out.writeUTF("SUCCESS: Server generated shared key.");
            }
        } catch (IOException e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }
}