import java.io.*;
import java.net.*;

public class vigenereserver {
    static String decrypt(String text, String key) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        key = key.toUpperCase().replaceAll("[^A-Z]", "");

        if (key.isEmpty()) return text;

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i) - 'A';
            int k = key.charAt(i % key.length()) - 'A';
            int p = (c - k + 26) % 26;
            result.append((char) (p + 'A'));
        }

        return result.toString();
    }

    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Vigenere Cipher Server started on port " + port);
            System.out.println("Waiting for client connection...");

            try (Socket s = ss.accept();
                 DataInputStream in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                 DataOutputStream out = new DataOutputStream(s.getOutputStream())) {

                System.out.println("Client connected!");

                while (true) {
                    String command = in.readUTF();

                    if (command.equalsIgnoreCase("OVER")) {
                        System.out.println("Client disconnected.");
                        break;
                    } 
                    else if (command.equalsIgnoreCase("DECRYPT")) {
                        // Receive ciphertext and key matrix sent by client
                        String ciphertext = in.readUTF();
                        String key = in.readUTF();

                        System.out.println("\n[Received Request]");
                        System.out.println("Ciphertext: " + ciphertext);
                        
                        // Server decrypts the received ciphertext
                        String decryptedText = decrypt(ciphertext, key);
                        System.out.println("--> Decrypted Result: " + decryptedText);

                        // Send confirmation and decrypted result back to client
                        out.writeUTF("SUCCESS");
                        out.writeUTF(decryptedText);
                    }
                }

            } catch (EOFException e) {
                System.out.println("Client connection closed.");
            }

        } catch (IOException i) {
            System.err.println("Server error: " + i.getMessage());
        }
    }
}
