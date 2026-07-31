import java.io.*;
import java.net.*;
import java.util.Scanner;

public class hillclient {
    public static void main(String[] args) {
        String savedCiphertext = "";

        try (Socket socket = new Socket("127.0.0.1", 5000);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Hill Cipher Server.");

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

                    System.out.println("Enter 4 integers for the 2x2 Encryption Key matrix [k00, k01, k10, k11]:");
                    int k00 = scanner.nextInt();
                    int k01 = scanner.nextInt();
                    int k10 = scanner.nextInt();
                    int k11 = scanner.nextInt();
                    scanner.nextLine(); // Clear input buffer

                    // Send command, plaintext, and key matrix values
                    out.writeUTF("ENCRYPT");
                    out.writeUTF(plaintext);
                    out.writeInt(k00);
                    out.writeInt(k01);
                    out.writeInt(k10);
                    out.writeInt(k11);

                    String status = in.readUTF();
                    if (status.startsWith("ERROR")) {
                        System.out.println(status);
                    } else {
                        savedCiphertext = in.readUTF();
                        System.out.println("--> Server Encrypted & Saved: " + savedCiphertext);
                    }
                } 
                else if (choice.equals("2")) {
                    if (savedCiphertext.isEmpty()) {
                        System.out.println("[ERROR]: No ciphertext saved yet! Encrypt a message first.");
                        continue;
                    }

                    System.out.println("Sending saved ciphertext: \"" + savedCiphertext + "\"");
                    System.out.println("Enter 4 integers for the 2x2 Encryption Key matrix used during encryption:");
                    int k00 = scanner.nextInt();
                    int k01 = scanner.nextInt();
                    int k10 = scanner.nextInt();
                    int k11 = scanner.nextInt();
                    scanner.nextLine(); // Clear input buffer

                    out.writeUTF("DECRYPT");
                    out.writeUTF(savedCiphertext);
                    out.writeInt(k00);
                    out.writeInt(k01);
                    out.writeInt(k10);
                    out.writeInt(k11);

                    String status = in.readUTF();
                    if (status.startsWith("ERROR")) {
                        System.out.println(status);
                    } else {
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
