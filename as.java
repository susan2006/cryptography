import java.io.*;
import java.net.*;

class ServerLogic {

    // Decryption formula: P = a_inv * (C - b) mod 26
    static String decrypt(String cipher, int a, int b) {
        StringBuilder msg = new StringBuilder();
        int a_inv = -1;

        // Find multiplicative inverse of 'a' modulo 26
        for (int i = 0; i < 26; i++) {
            if ((a * i) % 26 == 1) {
                a_inv = i;
                break;
            }
        }

        // If 'a' and 26 are not coprime, decryption is not possible
        if (a_inv == -1) {
            return "[SERVER ERROR]: Key 'a' must be coprime to 26 (e.g., 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25).";
        }

        for (int i = 0; i < cipher.length(); i++) {
            char ch = cipher.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                int y = ch - base;
                
                int decryptedChar = (a_inv * (y - b)) % 26;
                // Handle negative modulo results in Java
                decryptedChar = (decryptedChar % 26 + 26) % 26;

                msg.append((char) (decryptedChar + base));
            } else {
                msg.append(ch); // Keep spaces and special characters intact
            }
        }
        return msg.toString();
    }
}

public class as {
    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Affine Decryption Server started on port " + port);
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
                        // Receive ciphertext and keys 'a' & 'b' from client
                        String cipherToDecrypt = in.readUTF();
                        int a = in.readInt();
                        int b = in.readInt();

                        System.out.println("\n[Received Request]");
                        System.out.println("Ciphertext: " + cipherToDecrypt);
                        System.out.println("Keys: a = " + a + ", b = " + b);

                        // Decrypt text
                        String decryptedText = ServerLogic.decrypt(cipherToDecrypt, a, b);
                        System.out.println("Decrypted Result: " + decryptedText);

                        // Send decrypted text back to client
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
