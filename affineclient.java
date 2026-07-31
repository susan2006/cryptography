import java.io.*;
import java.net.*;
import java.util.Scanner;

public class affineclient {
    public static void main(String[] args) {
        String savedCiphertext = "";

        try (Socket socket = new Socket("127.0.0.1", 5000);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Affine Cipher Server.");

            while (true) {
                System.out.println("\n------------------------------------");
                System.out.println("1. Encrypt Message");
                System.out.println("2. Decrypt Saved Ciphertext");
                System.out.println("3. Exit (Over)");
                System.out.print("Choose an option (1-3): ");

                String choice = scanner.nextLine().trim();

                if (choice.equals("3") || choice.equalsIgnoreCase("Over")) {
                    out.writeUTF("OVER");
                    System.out.println("Exiting client.");
                    break;
                } 
                else if (choice.equals("1")) {
                    System.out.print("Enter plaintext message: ");
                    String plaintext = scanner.nextLine();

                    out.writeUTF("ENCRYPT");
                    out.writeUTF(plaintext);

                    savedCiphertext = in.readUTF();
                    System.out.println("--> Server Encrypted & Saved: " + savedCiphertext);
                } 
                else if (choice.equals("2")) {
                    if (savedCiphertext.isEmpty()) {
                        System.out.println("[ERROR]: No ciphertext saved yet! Encrypt a message first.");
                        continue;
                    }

                    System.out.println("Sending saved ciphertext: \"" + savedCiphertext + "\"");

                    out.writeUTF("DECRYPT");
                    out.writeUTF(savedCiphertext);

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
