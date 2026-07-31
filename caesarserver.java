import java.io.*;
import java.net.*;

class ServerLogic {
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
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static String decrypt(String text, int shift) {
        return encrypt(text, 26 - shift);
    }
}

public class caesarserver {
    public static void main(String[] args) {
        int port = 5000;
        String savedCiphertext = ""; // Stores the latest encrypted text
        int savedKey;
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
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
                    else if (command.equalsIgnoreCase("ENCRYPT")) {
                        String plaintext = in.readUTF();
                        int shift = in.readInt();
                        savedKey = shift;
                        // Encrypt and save in memory
                        savedCiphertext = ServerLogic.encrypt(plaintext, shift);
                        System.out.println("Encrypted & Saved text: " + savedCiphertext + " and key: "+ savedKey);

                        // Send the encrypted result back to client
                        out.writeUTF(savedCiphertext);
                        out.writeInt(savedKey);
                    } 
                    else if (command.equalsIgnoreCase("DECRYPT")) {
                        // Receive ciphertext and shift for decryption
                        String cipherToDecrypt = in.readUTF();
                        int shift = in.readInt();

                        String decryptedText = ServerLogic.decrypt(cipherToDecrypt, shift);
                        System.out.println("Decrypted Text: " + decryptedText);

                        // Send decrypted result back
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
