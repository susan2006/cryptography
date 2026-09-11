import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class aesengine {

    // Converts a String key into a valid AES SecretKeySpec object
    private static SecretKeySpec getSecretKey(String keyStr) {
        byte[] keyBytes = keyStr.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "AES");
    }

    // Encrypts plaintext using AES and returns Base64 encoded string
    public static String encrypt(String plainText, String secretKey) throws Exception {
        SecretKeySpec key = getSecretKey(secretKey);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypts Base64 encoded ciphertext string back to plaintext using AES
    public static String decrypt(String cipherText, String secretKey) throws Exception {
        SecretKeySpec key = getSecretKey(secretKey);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}