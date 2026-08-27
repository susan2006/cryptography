import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DESAlice {
    static final String SERVER_ADDRESS = "127.0.0.1";
    static final int PORT = 5000;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to Server...\n");

            // Input Plaintext
            System.out.print("Enter Plain Text (multiple of 8 chars): ");
            String plainText = scanner.nextLine();

            // Input Key (DES requires exactly an 8-character / 64-bit key)
            System.out.print("Enter Key (8 characters): ");
            String key = scanner.nextLine();

            // Ensure key is exactly 8 bytes long
            if (key.length() < 8) {
                key = String.format("%-8s", key).replace(' ', '0'); // Pad with zeros
            } else if (key.length() > 8) {
                key = key.substring(0, 8); // Truncate to 8 chars
            }

            // Encrypt using DES Algorithm (NoPadding)
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));

            System.out.println("\nGenerated Cipher Bytes (Hex): " + bytesToHex(encryptedBytes));

            // Send Key and Cipher Bytes to Server
            out.writeUTF(key);
            out.writeInt(encryptedBytes.length);
            out.write(encryptedBytes);
            out.flush();

            System.out.println("Data sent to server successfully.");

        } catch (Exception e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }

    //----------------------------------------------------
    // Helper: Convert bytes to hex string for display
    //----------------------------------------------------
    static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
}
import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DESBob {

    static final int PORT = 5000;
    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server Started...");
            System.out.println("Waiting for Client...");
            Socket socket = server.accept();
            System.out.println("Client Connected...\n");
            DataInputStream in = new DataInputStream(socket.getInputStream());

            // Receive Key
            String key = in.readUTF();

            // Receive Cipher Bytes
            int length = in.readInt();
            byte[] cipherBytes = new byte[length];
            in.readFully(cipherBytes);

            System.out.println("===== DATA RECEIVED =====");
            System.out.println("Key         : " + key);
            System.out.println("Cipher Bytes: " + bytesToHex(cipherBytes));
            // =========================================================
            // PAUSE HERE: Wait for user to press ENTER before decrypting
            // =========================================================
            System.out.print("\n>>> Press [ENTER] to perform DES decryption... ");
            Scanner console = new Scanner(System.in);
            console.nextLine();

            // Decrypt using DES Algorithm
            String plainText = decryptDES(cipherBytes, key);

            System.out.println("Original Text : " + plainText);

            // Clean up resources
            console.close();
            in.close();
            socket.close();
            server.close();

        } catch (Exception e) {
            System.out.println("Server Error: " + e.getMessage());
        }

    }

    //----------------------------------------------------
    // DES Decryption (NoPadding, raw bytes)
    //----------------------------------------------------
    static String decryptDES(byte[] cipherBytes, String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] decryptedBytes = cipher.doFinal(cipherBytes);
        return new String(decryptedBytes, "UTF-8");
    }

    //----------------------------------------------------
    // Helper: Convert bytes to hex string for display
    //----------------------------------------------------
    static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
}
explain the working of two codes and how will the output look like
