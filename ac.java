import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ac {

    // Helper method to check if 'a' and 26 are coprime (gcd == 1)
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // Local Affine Encryption: C = (a * P + b) mod 26
    public static String encryptLocally(String msg, int a, int b) {
        StringBuilder cipher = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            char ch = msg.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                int x = ch - base;
                int encryptedChar = (a * x + b) % 26;
                // Handle negative values if b is negative
                encryptedChar = (encryptedChar % 26 + 26) % 26;
                cipher.append((char) (encryptedChar + base));
            } else {
                cipher.append(ch);
            }
        }
        return cipher.toString();
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 5000);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Affine Server.");

            while (true) {
                System.out.println("\n------------------------------------");
                System.out.println("1. Enter Text & Keys -> Encrypt Locally -> Send to Server");
                System.out.println("2. Exit (Over)");
                System.out.print("Choose an option (1-2): ");

                String choice = scanner.nextLine().trim();

                if (choice.equals("2") || choice.equalsIgnoreCase("Over")) {
                    out.writeUTF("OVER");
                    System.out.println("Exiting client.");
                    break;
                } 
                else if (choice.equals("1")) {
                    // Get plaintext
                    System.out.print("Enter plaintext message: ");
                    String plaintext = scanner.nextLine();

                    // Get key 'a'
                    System.out.print("Enter key 'a' (must be coprime to 26 e.g., 1,3,5,7,9,11,15,17,19,21,23,25): ");
                    int a = scanner.nextInt();

                    while (gcd(a, 26) != 1) {
                        System.out.print("[Invalid Key]: 'a' must be coprime to 26. Enter key 'a' again: ");
                        a = scanner.nextInt();
                    }

                    // Get key 'b'
                    System.out.print("Enter key 'b' (integer offset): ");
                    int b = scanner.nextInt();
                    scanner.nextLine(); // Clear newline buffer

                    // Perform local encryption
                    String ciphertext = encryptLocally(plaintext, a, b);
                    System.out.println("--> Locally Encrypted Ciphertext: " + ciphertext);

                    // Send DECRYPT command, ciphertext, and keys to server
                    out.writeUTF("DECRYPT");
                    out.writeUTF(ciphertext);
                    out.writeInt(a);
                    out.writeInt(b);
                    System.out.println("--> Sent Ciphertext and Keys (a=" + a + ", b=" + b + ") to Server...");

                    // Read server response
                    String decryptedText = in.readUTF();
                    System.out.println("--> Server Decrypted Message: " + decryptedText);
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
