import java.io.*;
import java.net.*;

class ServerLogic {

    // Calculates the modular inverse of 'a' mod 'm'
    private static int modInverse(int a, int m) {
        a = (a % m + m) % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1; // No modular inverse exists
    }

    // Calculates the determinant of a 2x2 matrix
    public static int getDeterminant(int[][] key) {
        int det = (key[0][0] * key[1][1] - key[0][1] * key[1][0]) % 26;
        return (det % 26 + 26) % 26;
    }

    // Validates whether the key matrix is invertible modulo 26
    public static boolean isValidKey(int[][] key) {
        int det = getDeterminant(key);
        return det != 0 && modInverse(det, 26) != -1;
    }

    // Derives the 2x2 decryption inverse matrix K^-1 mod 26
    public static int[][] computeInverseKey(int[][] key) {
        int det = getDeterminant(key);
        int invDet = modInverse(det, 26);

        int[][] invKey = new int[2][2];
        // Adjugate matrix values scaled by modular inverse of determinant
        invKey[0][0] = (key[1][1] * invDet) % 26;
        invKey[0][1] = ((-key[0][1]) * invDet) % 26;
        invKey[1][0] = ((-key[1][0]) * invDet) % 26;
        invKey[1][1] = (key[0][0] * invDet) % 26;

        // Ensure all values stay positive in modulo 26 arithmetic
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                invKey[i][j] = (invKey[i][j] % 26 + 26) % 26;
            }
        }
        return invKey;
    }

    // Processes text through 2x2 matrix multiplication modulo 26
    static String process(String text, int[][] key) {
        // Clean text: convert to uppercase, keep letters only
        text = text.toUpperCase().replaceAll("[^A-Z]", "");

        // Pad text with 'X' if odd length to make full pairs
        if (text.length() % 2 != 0) {
            text += "X";
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {
            int x = text.charAt(i) - 'A';
            int y = text.charAt(i + 1) - 'A';

            int a = (key[0][0] * x + key[0][1] * y) % 26;
            int b = (key[1][0] * x + key[1][1] * y) % 26;

            result.append((char) (a + 'A'));
            result.append((char) (b + 'A'));
        }

        return result.toString();
    }

    static String encrypt(String text, int[][] key) {
        return process(text, key);
    }

    static String decrypt(String text, int[][] key) {
        int[][] decryptKey = computeInverseKey(key);
        return process(text, decryptKey);
    }
}

public class hillserver {
    public static void main(String[] args) {
        int port = 5000;
        String savedCiphertext = ""; 
        int[][] savedKey = new int[2][2]; // Stores the key for the current ciphertext

        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Hill Cipher Server started on port " + port);
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

                        // Read 2x2 key matrix from client
                        int[][] key = new int[2][2];
                        key[0][0] = in.readInt();
                        key[0][1] = in.readInt();
                        key[1][0] = in.readInt();
                        key[1][1] = in.readInt();

                        // Validate matrix validity mod 26
                        if (!ServerLogic.isValidKey(key)) {
                            out.writeUTF("ERROR: Invalid key matrix! Determinant must be coprime with 26.");
                            continue;
                        }

                        // Store key and encrypt text
                        savedKey = key;
                        savedCiphertext = ServerLogic.encrypt(plaintext, key);
                        System.out.println("Encrypted & Saved: " + savedCiphertext);

                        out.writeUTF("SUCCESS");
                        out.writeUTF(savedCiphertext);
                    } 
                    else if (command.equalsIgnoreCase("DECRYPT")) {
                        String cipherToDecrypt = in.readUTF();

                        // Read 2x2 key matrix provided by user for decryption
                        int[][] key = new int[2][2];
                        key[0][0] = in.readInt();
                        key[0][1] = in.readInt();
                        key[1][0] = in.readInt();
                        key[1][1] = in.readInt();

                        if (!ServerLogic.isValidKey(key)) {
                            out.writeUTF("ERROR: Invalid key matrix! Cannot compute inverse.");
                            continue;
                        }

                        String decryptedText = ServerLogic.decrypt(cipherToDecrypt, key);
                        System.out.println("Decrypted Text: " + decryptedText);

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
