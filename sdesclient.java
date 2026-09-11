import java.io.*;
import java.net.*;
import java.util.Scanner;

public class sdesclient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to S-DES Server.\n");

            // 1. Get 10-bit Key from User
            System.out.print("Enter 10-bit Key separated by spaces (e.g., 1 0 1 0 0 0 0 0 1 0): ");
            int[] key = new int[10];
            for (int i = 0; i < 10; i++) {
                key[i] = scanner.nextInt();
            }

            // 2. Get 8-bit Plaintext from User
            System.out.print("Enter 8-bit Plaintext separated by spaces (e.g., 1 1 1 0 0 1 0 1): ");
            int[] plaintext = new int[8];
            for (int i = 0; i < 8; i++) {
                plaintext[i] = scanner.nextInt();
            }

            // 3. Perform Local Encryption
            sdesengine sdes = new sdesengine(key);
            sdes.key_generation();

            int[] ciphertext = sdes.encryption(plaintext);

            System.out.println("\n--- CLIENT LOCAL ENCRYPTION ---");
            System.out.print("Generated Ciphertext : ");
            for (int b : ciphertext) System.out.print(b + " ");
            System.out.println("\nSending Ciphertext and Key to Server...");

            // 4. Send Key array to Server
            for (int i = 0; i < 10; i++) {
                out.writeInt(key[i]);
            }

            // 5. Send Ciphertext array to Server
            for (int i = 0; i < 8; i++) {
                out.writeInt(ciphertext[i]);
            }

            // 6. Receive Confirmation from Server
            String response = in.readUTF();
            System.out.println("\n[Server Response] " + response);

        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }
}