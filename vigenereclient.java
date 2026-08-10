import java.io.*;
import java.net.*;
import java.util.Scanner;

public class vigenereclient {
    static String encrypt(String text, String key) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        key = key.toUpperCase().replaceAll("[^A-Z]", "");

        if (key.isEmpty()) return text;

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int p = text.charAt(i) - 'A';
            int k = key.charAt(i % key.length()) - 'A';
            int c = (p + k) % 26;
            result.append((char) (c + 'A'));
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

                    System.out.print("Enter key: ");
                    String key = scanner.nextLine();

                    // 1. Perform local encryption on client side
                    String ciphertext = encrypt(plaintext, key);
                    System.out.println("--> Locally Encrypted Ciphertext: " + ciphertext);

                    // 2. Send DECRYPT command, ciphertext, and key matrix to server
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
