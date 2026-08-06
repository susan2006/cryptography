import java.io.*;
import java.net.*;
import java.util.*;

public class PlayfairServer {

    private static char[][] matrix = new char[5][5];
    private static Map<Character, int[]> posMap = new HashMap<>();

    // Reconstruct the 5x5 Key Matrix on the Server
    private static void buildMatrix(String key) {
        matrix = new char[5][5];
        posMap.clear();

        boolean[] used = new boolean[26];
        key = key.toLowerCase().replaceAll("j", "i").replaceAll("[^a-z]", "");

        int r = 0, c = 0;

        for (char ch : key.toCharArray()) {
            if (!used[ch - 'a']) {
                matrix[r][c] = ch;
                posMap.put(ch, new int[]{r, c});
                used[ch - 'a'] = true;
                c++;
                if (c == 5) { c = 0; r++; }
            }
        }

        for (char ch = 'a'; ch <= 'z'; ch++) {
            if (ch == 'j') continue;
            if (!used[ch - 'a']) {
                matrix[r][c] = ch;
                posMap.put(ch, new int[]{r, c});
                used[ch - 'a'] = true;
                c++;
                if (c == 5) { c = 0; r++; }
            }
        }
    }

    // Decrypt Ciphertext (-1 shift mod 5 is equivalent to +4 mod 5)
    public static String decrypt(String ciphertext, String key) {
        buildMatrix(key);
        ciphertext = ciphertext.toLowerCase();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i += 2) {
            char a = ciphertext.charAt(i);
            char b = ciphertext.charAt(i + 1);

            int[] p1 = posMap.get(a);
            int[] p2 = posMap.get(b);

            int r1 = p1[0], c1 = p1[1];
            int r2 = p2[0], c2 = p2[1];

            if (r1 == r2) {
                // Same row -> Shift columns left
                result.append(matrix[r1][(c1 + 4) % 5]);
                result.append(matrix[r2][(c2 + 4) % 5]);
            } else if (c1 == c2) {
                // Same column -> Shift rows up
                result.append(matrix[(r1 + 4) % 5][c1]);
                result.append(matrix[(r2 + 4) % 5][c2]);
            } else {
                // Rectangle -> Swap columns
                result.append(matrix[r1][c2]);
                result.append(matrix[r2][c1]);
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Playfair Cipher Server started on port " + port);
            System.out.println("Waiting for client connection...");

            try (Socket socket = ss.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                System.out.println("Client connected!");

                while (true) {
                    String command = in.readUTF();

                    if (command.equalsIgnoreCase("OVER")) {
                        System.out.println("Client requested disconnect. Exiting...");
                        break;
                    } 
                    else if (command.equalsIgnoreCase("DECRYPT")) {
                        // Receive encrypted message and key from client
                        String ciphertext = in.readUTF();
                        String key = in.readUTF();

                        System.out.println("\n------------------------------------");
                        System.out.println("[Received Request]");
                        System.out.println("Ciphertext : " + ciphertext.toUpperCase());
                        System.out.println("Secret Key : " + key);

                        // Decrypt ciphertext on the server side
                        String decryptedText = decrypt(ciphertext, key);
                        System.out.println("--> Server Decrypted Result: " + decryptedText);

                        // Send response back to client
                        out.writeUTF("SUCCESS");
                        out.writeUTF(decryptedText);
                    }
                }

            } catch (EOFException e) {
                System.out.println("Client disconnected.");
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
