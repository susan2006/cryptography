import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class DESExample {

    // Converts a 8-character string key into a valid SecretKey object
    private static SecretKey getSecretKey(String secretKeyStr) throws Exception {
        DESKeySpec keySpec = new DESKeySpec(secretKeyStr.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(keySpec);
    }

    // Encrypts plaintext and returns Base64 encoded ciphertext string
    public static String encrypt(String plainText, String secretKey) throws Exception {
        SecretKey key = getSecretKey(secretKey);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypts Base64 encoded ciphertext string back to plaintext
    public static String decrypt(String cipherText, String secretKey) throws Exception {
        SecretKey key = getSecretKey(secretKey);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter Plaintext Message: ");
            String plainText = scanner.nextLine();

            System.out.print("Enter 8-character Key: ");
            String key = scanner.nextLine();

            if (key.length() < 8) {
                System.out.println("Error: DES key must be at least 8 characters long!");
                return;
            }

            // Encrypt
            String encryptedText = encrypt(plainText, key);
            System.out.println("\n--- RESULTS ---");
            System.out.println("Encrypted (Base64) : " + encryptedText);

            // Decrypt
            String decryptedText = decrypt(encryptedText, key);
            System.out.println("Decrypted Text     : " + decryptedText);

        } catch (Exception e) {
            System.err.println("Cryptographic Error: " + e.getMessage());
        }
    }
}
