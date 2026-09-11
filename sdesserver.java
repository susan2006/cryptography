import java.io.*;
import java.net.*;

public class sdesserver {
    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("S-DES Server active on port " + port);
            System.out.println("Waiting for client connection...\n");

            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                System.out.println("Client Connected!");

                // 1. Read 10-bit key array from Client
                int[] key = new int[10];
                for (int i = 0; i < 10; i++) {
                    key[i] = in.readInt();
                }

                // 2. Read 8-bit ciphertext array from Client
                int[] ciphertext = new int[8];
                for (int i = 0; i < 8; i++) {
                    ciphertext[i] = in.readInt();
                }

                System.out.println("\n--- RECEIVED DATA FROM CLIENT ---");
                System.out.print("Received Key        : ");
                for (int b : key) System.out.print(b + " ");
                System.out.print("\nReceived Ciphertext : ");
                for (int b : ciphertext) System.out.print(b + " ");
                System.out.println();

                // 3. Initialize S-DES Engine on Server and Decrypt
                sdesengine sdes = new sdesengine(key);
                sdes.key_generation();

                int[] decryptedText = sdes.decryption(ciphertext);

                System.out.println("\n--- SERVER DECRYPTION RESULT ---");
                System.out.print("Decrypted Plaintext : ");
                for (int b : decryptedText) System.out.print(b + " ");
                System.out.println();

                // 4. Send Confirmation Response Back to Client
                out.writeUTF("Decryption Successful on Server!");

            } catch (EOFException e) {
                System.out.println("Client disconnected.");
            }

        } catch (IOException e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }
}