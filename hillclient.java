import java.io.*;
import java.net.*;
import java.util.Scanner;

public class hillclient {

    // Helper to calculate modular inverse
    private static int modInverse(int a, int m) {
        a = (a % m + m) % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) return x;
        }
        return -1;
    }

    // Helper to calculate determinant
    private static int getDeterminant(int[][] key) {
        int det = (key[0][0] * key[1][1] - key[0][1] * key[1][0]) % 26;
        return (det % 26 + 26) % 26;
    }

    // Validates if key matrix is invertible mod 26
    public static boolean isValidKey(int[][] key) {
        int det = getDeterminant(key);
        return det != 0 && modInverse(det, 26) != -1;
    }

    // Local Hill Cipher Encryption
    public static String encryptLocally(String text, int[][] key) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        if (text.length() % 2 != 0) {
            text += "X"; // Pad odd length strings
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

    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 5000);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Hill Cipher Server.");

            while (true) {
                System.out.println("\n------------------------------------");
                System.out.println("1. Enter Plaintext & Key -> Encrypt Locally -> Send Ciphertext to Server");
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

                    System.out.println("Enter 4 integers for 2x2 Key Matrix [k00, k01, k10, k11]:");
                    int[][] key = new int[2][2];
                    key[0][0] = scanner.nextInt();
                    key[0][1] = scanner.nextInt();
                    key[1][0] = scanner.nextInt();
                    key[1][1] = scanner.nextInt();
                    scanner.nextLine(); // Clear buffer

                    // Validate key matrix locally before encrypting
                    if (!isValidKey(key)) {
                        System.out.println("[ERROR]: Invalid key matrix! Determinant must be coprime to 26.");
                        continue;
                    }

                    // 1. Perform local encryption on client side
                    String ciphertext = encryptLocally(plaintext, key);
                    System.out.println("--> Locally Encrypted Ciphertext: " + ciphertext);

                    // 2. Send DECRYPT command, ciphertext, and key matrix to server
                    out.writeUTF("DECRYPT");
                    out.writeUTF(ciphertext);
                    out.writeInt(key[0][0]);
                    out.writeInt(key[0][1]);
                    out.writeInt(key[1][0]);
                    out.writeInt(key[1][1]);
                    System.out.println("--> Sent Ciphertext and Key Matrix to Server...");

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
