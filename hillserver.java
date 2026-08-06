import java.io.*;
import java.net.*;

class ServerLogic {

    // Calculates modular multiplicative inverse of 'a' mod 'm'
    private static int modInverse(int a, int m) {
        a = (a % m + m) % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1; // No inverse exists
    }

    // Calculates determinant of 2x2 matrix mod 26
    public static int getDeterminant(int[][] key) {
        int det = (key[0][0] * key[1][1] - key[0][1] * key[1][0]) % 26;
        return (det % 26 + 26) % 26;
    }

    // Derives 2x2 inverse matrix K^-1 mod 26 for decryption
    public static int[][] computeInverseKey(int[][] key) {
        int det = getDeterminant(key);
        int invDet = modInverse(det, 26);

        int[][] invKey = new int[2][2];
        invKey[0][0] = (key[1][1] * invDet) % 26;
        invKey[0][1] = ((-key[0][1] % 26 + 26) * invDet) % 26;
        invKey[1][0] = ((-key[1][0] % 26 + 26) * invDet) % 26;
        invKey[1][1] = (key[0][0] * invDet) % 26;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                invKey[i][j] = (invKey[i][j] % 26 + 26) % 26;
            }
        }
        return invKey;
    }

    // Server Decryption Logic
    public static String decrypt(String ciphertext, int[][] key) {
        int[][] invKey = computeInverseKey(key);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i += 2) {
            int x = ciphertext.charAt(i) - 'A';
            int y = ciphertext.charAt(i + 1) - 'A';

            int a = (invKey[0][0] * x + invKey[0][1] * y) % 26;
            int b = (invKey[1][0] * x + invKey[1][1] * y) % 26;

            a = (a % 26 + 26) % 26;
            b = (b % 26 + 26) % 26;

            result.append((char) (a + 'A'));
            result.append((char) (b + 'A'));
        }

        return result.toString();
    }
}

public class hillserver {
    public static void main(String[] args) {
        int port = 5000;

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
                    else if (command.equalsIgnoreCase("DECRYPT")) {
                        // Receive ciphertext and key matrix sent by client
                        String ciphertext = in.readUTF();
                        int[][] key = new int[2][2];
                        key[0][0] = in.readInt();
                        key[0][1] = in.readInt();
                        key[1][0] = in.readInt();
                        key[1][1] = in.readInt();

                        System.out.println("\n[Received Request]");
                        System.out.println("Ciphertext: " + ciphertext);
                        System.out.println("Key Matrix: [[" + key[0][0] + ", " + key[0][1] + "], [" + key[1][0] + ", " + key[1][1] + "]]");

                        // Server decrypts the received ciphertext
                        String decryptedText = ServerLogic.decrypt(ciphertext, key);
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
