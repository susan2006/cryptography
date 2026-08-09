import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DESServer {

    // Converts key string to a valid DES SecretKey object
    private static SecretKey getSecretKey(String secretKeyStr) throws Exception {
        DESKeySpec keySpec = new DESKeySpec(secretKeyStr.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(keySpec);
    }

    // Decrypts Base64 encoded ciphertext
    public static String decrypt(String cipherText, String secretKey) throws Exception {
        SecretKey key = getSecretKey(secretKey);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("==========================================");
            System.out.println("  DES DECRYPTION SERVER STARTED (PORT " + port + ")");
            System.out.println("==========================================");
            System.out.println("Waiting for client connection...\n");

            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                System.out.println("[+] Client connected!");

                // 1. Receive Base64 Ciphertext and Secret Key from Client
                String ciphertext = in.readUTF();
                String key = in.readUTF();

                System.out.println("\n[Received Data from Client]");
                System.out.println("--> Received Ciphertext (Base64) : " + ciphertext);
                System.out.println("--> Received Secret Key          : " + key);

                // 2. Perform Server-side DES Decryption
                String decryptedText = decrypt(ciphertext, key);

                System.out.println("\n[Server Decryption Result]");
                System.out.println("--> Decrypted Plaintext          : " + decryptedText);

                // 3. Send Response Back to Client
                out.writeUTF("SUCCESS: Server decrypted message -> " + decryptedText);

            } catch (EOFException e) {
                System.out.println("Client disconnected.");
            } catch (Exception e) {
                System.err.println("Decryption Error on Server: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }
}
