import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class DESClient {

    // Converts key string to a valid DES SecretKey object
    private static SecretKey getSecretKey(String secretKeyStr) throws Exception {
        DESKeySpec keySpec = new DESKeySpec(secretKeyStr.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(keySpec);
    }

    // Encrypts plaintext and returns Base64 encoded ciphertext
    public static String encrypt(String plainText, String secretKey) throws Exception {
        SecretKey key = getSecretKey(secretKey);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to DES Encryption Server.\n");

            // 1. Get Inputs from User
            System.out.print("Enter Plaintext Message : ");
            String plainText = scanner.nextLine();

            System.out.print("Enter 8-character Key   : ");
            String key = scanner.nextLine();

            if (key.length() < 8) {
                System.out.println("Error: DES key must be at least 8 characters long!");
                return;
            }

            // 2. Perform Local Encryption on Client
            String ciphertext = encrypt(plainText, key);

            System.out.println("\n[Client Local Processing]");
            System.out.println("--> Encrypted Ciphertext (Base64) : " + ciphertext);
            System.out.println("--> Transmitting to Server...");

            // 3. Send Ciphertext and Key to Server
            out.writeUTF(ciphertext);
            out.writeUTF(key);

            // 4. Receive Response from Server
            String serverResponse = in.readUTF();
            System.out.println("\n[Server Response] " + serverResponse);

        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Encryption Error: " + e.getMessage());
        }
    }
}
