import java.io.*;
import java.net.*;
import java.util.*;

public class PlayfairClient {

    private static char[][] matrix = new char[5][5];
    private static Map<Character, int[]> posMap = new HashMap<>();

    // Build local matrix for client-side encryption
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

    // Preprocess text (separate duplicate letters with 'x', pad odd lengths)
    private static String prepareText(String text) {
        text = text.toLowerCase().replaceAll("j", "i").replaceAll("[^a-z]", "");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            sb.append(current);

            if (i + 1 < text.length() && current == text.charAt(i + 1) && sb.length() % 2 != 0) {
                sb.append('x');
            }
        }

        if (sb.length() % 2 != 0) {
            sb.append('x');
        }

        return sb.toString();
    }

    // Encrypt locally on client side
    private static String encryptLocally(String text, String key) {
        buildMatrix(key);
        String prepared = prepareText(text);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < prepared.length(); i += 2) {
            char a = prepared.charAt(i);
            char b = prepared.charAt(i + 1);

            int[] p1 = posMap.get(a);
            int[] p2 = posMap.get(b);

            int r1 = p1[0], c1 = p1[1];
            int r2 = p2[0], c2 = p2[1];

            if (r1 == r2) {
                // Same row -> Shift columns right
                result.append(matrix[r1][(c1 + 1) % 5]);
                result.append(matrix[r2][(c2 + 1) % 5]);
            } else if (c1 == c2) {
                // Same column -> Shift rows down
                result.append(matrix[(r1 + 1) % 5][c1]);
                result.append(matrix[(r2 + 1) % 5][c2]);
            } else {
                // Rectangle -> Swap columns
                result.append(matrix[r1][c2]);
                result.append(matrix[r2][c1]);
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 5000);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Playfair Cipher Server.");

            while (true) {
                System.out.println("\n------------------------------------");
                System.out.println("1. Enter Text & Key -> Encrypt Locally -> Send to Server");
                System.out.println("2. Exit (Over)");
                System.out.print("Choose an option (1-2): ");

                String choice = scanner.nextLine().trim();

                if (choice.equals("2") || choice.equalsIgnoreCase("Over")) {
                    out.writeUTF("OVER");
                    System.out.println("Exiting client.");
                    break;
                } 
                else if (choice.equals("1")) {
                    System.out.print("Enter plaintext message: ");
                    String plaintext = scanner.nextLine();

                    System.out.print("Enter secret key: ");
                    String key = scanner.nextLine();

                    // 1. Perform client-side local encryption
                    String ciphertext = encryptLocally(plaintext, key);
                    System.out.println("\n--> Locally Encrypted Ciphertext: " + ciphertext.toUpperCase());

                    // 2. Send command, ciphertext, and key to server
                    out.writeUTF("DECRYPT");
                    out.writeUTF(ciphertext);
                    out.writeUTF(key);
                    System.out.println("--> Sent Ciphertext and Key to Server...");

                    // 3. Receive server response
                    String status = in.readUTF();
                    if (status.equals("SUCCESS")) {
                        String decryptedText = in.readUTF();
                        System.out.println("--> Server Decrypted Message: " + decryptedText);
                    }
                } 
                else {
                    System.out.println("Invalid option. Please try again.");
                }
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
