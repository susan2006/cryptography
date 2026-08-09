import java.io.*;
import java.net.*;

public class ColumnarServer {
    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("=================================================");
            System.out.println("  COLUMNAR TRANSPOSITION SERVER STARTED (PORT " + port + ")");
            System.out.println("=================================================");
            System.out.println("Waiting for client connection...\n");

            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                System.out.println("[+] Client connected!");

                // 1. Read Ciphertext and Key from Client
                String ciphertext = in.readUTF();
                String key = in.readUTF();

                System.out.println("\n[Received Data from Client]");
                System.out.println("--> Received Ciphertext : " + ciphertext);
                System.out.println("--> Received Key        : " + key);

                // 2. Perform Server-side Decryption
                String decryptedText = ColumnarEngine.decryptMessage(ciphertext, key);

                System.out.println("\n[Server Decryption Result]");
                System.out.println("--> Decrypted Plaintext  : " + decryptedText);

                // 3. Send Response Back to Client
                out.writeUTF("SUCCESS: Server decrypted message -> " + decryptedText);

            } catch (EOFException e) {
                System.out.println("Client disconnected.");
            }

        } catch (IOException e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }
}
