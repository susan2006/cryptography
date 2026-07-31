import java.io.*;
import java.net.*;
import java.util.Scanner;

public class cc {

    // Local client-side decryption helper
    public static String decryptLocally(String text, int shift) {
        StringBuilder result = new StringBuilder();
        shift = (26 - (shift % 26)) % 26; // Reverse shift for decryption

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

    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 5000);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Encryption Server.");

            while (true) {
                System.out.println("\n------------------------------------");
                System.out.println("1. Send Plaintext -> Server Encrypts -> Client Decrypts locally");
                System.out.println("2. Exit (Over)");
                System.out.print("Choose an option (1-2): ");

                String choice = scanner.nextLine().trim();

                if (choice.equals("2") || choice.equalsIgnoreCase("Over")) {
                    out.writeUTF("OVER");
                    System.out.println("Exiting client.");
                    break;
                } 
                else if (choice.equals("1")) {
                    // 1. Get original plaintext and key from user
                    System.out.print("Enter original text: ");
                    String plaintext = scanner.nextLine();

                    System.out.print("Enter shift key (integer): ");
                    int shiftKey = scanner.nextInt();
                    scanner.nextLine(); // Clear newline buffer

                    // 2. Send request to server
                    out.writeUTF("ENCRYPT");
                    out.writeUTF(plaintext);
                    out.writeInt(shiftKey);
                    System.out.println("--> Sent text and key to server for encryption...");

                    // 3. Receive encrypted ciphertext from server
                    
                    String ciphertext = in.readUTF();
                    //System.out.println("--> Received Ciphertext from Server: " + ciphertext);

                    // 4. Decrypt locally on client side
                    String decryptedText = decryptLocally(ciphertext, shiftKey);
                    System.out.println("--> Client Decrypted Result: " + decryptedText);
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
