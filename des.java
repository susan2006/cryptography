import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class des {

    private static SecretKey getSecretKey(String secretKeyStr) throws Exception {
        DESKeySpec keySpec = new DESKeySpec(secretKeyStr.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(keySpec);
    }

    // Helper: Convert raw bytes to Hexadecimal String
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    // Helper: Convert Hexadecimal String back to raw bytes
    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    public static String encrypt(String plainText, String secretKey) throws Exception {
        SecretKey key = getSecretKey(secretKey);
        // Changed from PKCS5Padding to NoPadding to enforce exactly 1 block (8 bytes)
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
        
        // Manually pad input to exactly 8 bytes if shorter than 8 characters
        byte[] paddedBytes = Arrays.copyOf(plainBytes, 8); 

        byte[] encryptedBytes = cipher.doFinal(paddedBytes);
        
        // Returns 16 Hex characters = 8 bytes = 64 bits
        return bytesToHex(encryptedBytes);
    }

    public static String decrypt(String hexCipherText, String secretKey) throws Exception {
        SecretKey key = getSecretKey(secretKey);
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedBytes = hexToBytes(hexCipherText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        
        return new String(decryptedBytes, StandardCharsets.UTF_8).trim();
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter Plaintext Message (max 8 chars): ");
            String plainText = scanner.nextLine();

            System.out.print("Enter 8-character Key: ");
            String key = scanner.nextLine();

            if (key.length() < 8) {
                System.out.println("Error: DES key must be at least 8 characters long!");
                return;
            }

            // Encrypt
            String encryptedHex = encrypt(plainText, key);
            System.out.println("\n--- RESULTS ---");
            System.out.println("Encrypted (Hex)      : " + encryptedHex);
            System.out.println("Ciphertext Byte Size : " + (encryptedHex.length() / 2) + " bytes (" + (encryptedHex.length() * 4) + " bits)");

            // Decrypt
            String decryptedText = decrypt(encryptedHex, key);
            System.out.println("Decrypted Text       : " + decryptedText);

        } catch (Exception e) {
            System.err.println("Cryptographic Error: " + e.getMessage());
        }
    }
}