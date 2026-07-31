import java.io.*;
import java.net.*;

class ServerLogic {
    // Affine Cipher Keys: C = (a * p + b) mod 26
    static int a = 5;
    static int b = 7;

    static String encrypt(String msg) {
        StringBuilder cipher = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            char ch = msg.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                int x = ch - base;
                int encryptedChar = (a * x + b) % 26;
                cipher.append((char) (encryptedChar + base));
            } else {
                cipher.append(ch); // Preserve spaces and punctuation
            }
        }
        return cipher.toString();
    }

    static String decrypt(String cipher) {
        StringBuilder msg = new StringBuilder();
        int a_inv = 0;

        // Find multiplicative inverse of 'a' modulo 26
        for (int i = 0; i < 26; i++) {
            if ((a * i) % 26 == 1) {
                a_inv = i;
                break;
            }
        }

        for (int i = 0; i < cipher.length(); i++) {
            char ch = cipher.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                int y = ch - base;
                
                // Decryption formula: P = a_inv * (y - b) mod 26
                // (val % 26 + 26) % 26 handles negative results in Java
                int decryptedChar = (a_inv * (y - b)) % 26;
                decryptedChar = (decryptedChar % 26 + 26) % 26;

                msg.append((char) (decryptedChar + base));
            } else {
                msg.append(ch);
            }
        }
        return msg.toString();
    }
}

public class affineserver {
    public static void main(String[] args) {
        int port = 5000;
        String savedCiphertext = ""; // Stores latest encrypted text

        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Affine Server started on port " + port);
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
                        savedCiphertext = ServerLogic.encrypt(plaintext);
                        System.out.println("Encrypted & Saved: " + savedCiphertext);

                        // Send encrypted ciphertext back to client
                        out.writeUTF(savedCiphertext);
                    } 
                    else if (command.equalsIgnoreCase("DECRYPT")) {
                        String cipherToDecrypt = in.readUTF();
                        String decryptedText = ServerLogic.decrypt(cipherToDecrypt);
                        System.out.println("Decrypted Text: " + decryptedText);

                        // Send decrypted text back
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
