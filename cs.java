import java.io.*;
import java.net.*;

class ServerLogic {
    // Encrypts plain text into ciphertext using Caesar Cipher
    public static String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        shift = (shift % 26 + 26) % 26;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                char shifted = (char) ((ch - base + shift) % 26 + base);
                result.append(shifted);
            } else {
                result.append(ch); // Preserve spaces and special characters
            }
        }
        return result.toString();
    }
}

public class cs {
    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Encryption Server started on port " + port);
            System.out.println("Waiting for client connection...");

            try (Socket s = ss.accept();
                 DataInputStream in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                 DataOutputStream out = new DataOutputStream(s.getOutputStream())) {

                System.out.println("Client connected!");

                while (true) {
                    String command = in.readUTF();

                    if (command.equalsIgnoreCase("OVER")) {
                        System.out.println("Client requested disconnect.");
                        break;
                    } 
                    else if (command.equalsIgnoreCase("ENCRYPT")) {
                        // Receive plaintext and shift key from client
                        String plainText = in.readUTF();
                        int shiftKey = in.readInt();

                        System.out.println("\n[Received from Client]");
                        System.out.println("Plaintext: " + plainText);
                        System.out.println("Shift Key: " + shiftKey);

                        // Server performs encryption
                        String cipherText = ServerLogic.encrypt(plainText, shiftKey);
                        System.out.println("Encrypted Result: " + cipherText);

                        // Send generated ciphertext back to client
                        out.writeUTF(cipherText);
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
