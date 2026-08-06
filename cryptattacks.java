import java.util.Scanner;

public class CaesarBruteForceEarlyStop {

    // Encrypt plaintext using a shift key
    public static String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        shift = (shift % 26 + 26) % 26; // Normalize shift to range [0, 25]

        for (char ch : text.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                char c = (char) ((ch - 'A' + shift) % 26 + 'A');
                result.append(c);
            } else if (Character.isLowerCase(ch)) {
                char c = (char) ((ch - 'a' + shift) % 26 + 'a');
                result.append(c);
            } else {
                result.append(ch); // Keep spaces and punctuation unchanged
            }
        }
        return result.toString();
    }

    // Decrypt ciphertext using a specific shift key
    public static String decrypt(String text, int shift) {
        return encrypt(text, 26 - (shift % 26));
    }

    // Executes brute-force attack and terminates as soon as original text is matched
    public static void bruteForceAttack(String ciphertext, String originalPlaintext) {
        System.out.println("\n==================================================");
        System.out.println("     RUNNING BRUTE-FORCE ATTACK (EARLY STOP)      ");
        System.out.println("==================================================");

        boolean found = false;

        for (int key = 0; key < 26; key++) {
            String candidateText = decrypt(ciphertext, key);
            System.out.printf("Trying Key %2d | Candidate: %s\n", key, candidateText);

            // Stop condition: candidate matches original plaintext
            if (candidateText.equalsIgnoreCase(originalPlaintext)) {
                System.out.println("\n--------------------------------------------------");
                System.out.println("[+] SUCCESS: Original text match found!");
                System.out.println("[+] Discovered Key       : " + key);
                System.out.println("[+] Recovered Plaintext  : " + candidateText);
                System.out.println("--------------------------------------------------");
                found = true;
                break; // Stop testing remaining keys
            }
        }

        if (!found) {
            System.out.println("\n[-] Attack completed without finding an exact match.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Get Plaintext
        System.out.print("Enter Plaintext Message: ");
        String plaintext = scanner.nextLine();

        // 2. Get Secret Key for Initial Encryption
        System.out.print("Enter Secret Shift Key (0-25): ");
        int secretKey = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        // 3. Encrypt Plaintext -> Ciphertext
        String ciphertext = encrypt(plaintext, secretKey);
        System.out.println("\n[+] Encrypted Ciphertext: " + ciphertext);

        // 4. Run Brute-Force Attack until match is found
        bruteForceAttack(ciphertext, plaintext);

        scanner.close();
    }
}


import java.util.Scanner;

public class AffineBruteForceEarlyStop {

    // 12 valid values for multiplier 'a' that are coprime to 26 (gcd(a, 26) = 1)
    private static final int[] VALID_A = {1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25};

    // Computes modular multiplicative inverse of 'a' mod 26
    private static int modInverse(int a) {
        for (int x = 1; x < 26; x++) {
            if ((a * x) % 26 == 1) return x;
        }
        return -1;
    }

    // Encrypts text: C = (a * P + b) mod 26
    public static String encrypt(String text, int a, int b) {
        StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                char c = (char) (((a * (ch - 'A') + b) % 26 + 26) % 26 + 'A');
                result.append(c);
            } else if (Character.isLowerCase(ch)) {
                char c = (char) (((a * (ch - 'a') + b) % 26 + 26) % 26 + 'a');
                result.append(c);
            } else {
                result.append(ch); // Preserve spaces and punctuation
            }
        }
        return result.toString();
    }

    // Decrypts text: P = a^-1 * (C - b) mod 26
    public static String decrypt(String text, int a, int b) {
        int aInv = modInverse(a);
        if (aInv == -1) return "";

        StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                char p = (char) (((aInv * (ch - 'A' - b) % 26 + 26) % 26) + 'A');
                result.append(p);
            } else if (Character.isLowerCase(ch)) {
                char p = (char) (((aInv * (ch - 'a' - b) % 26 + 26) % 26) + 'a');
                result.append(p);
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    // Executes brute-force search over all 312 key pairs and stops when target match is found
    public static void bruteForceAttack(String ciphertext, String targetPlaintext) {
        System.out.println("\n==================================================");
        System.out.println("   AFFINE BRUTE-FORCE ATTACK (KEY SPACE = 312)   ");
        System.out.println("==================================================");

        int attempts = 0;
        boolean found = false;

        // Iterate over all valid 'a' values and 'b' shifts
        for (int a : VALID_A) {
            for (int b = 0; b < 26; b++) {
                attempts++;
                String candidate = decrypt(ciphertext, a, b);

                System.out.printf("Attempt %3d | Trying (a=%2d, b=%2d) | Candidate: %s\n", 
                                  attempts, a, b, candidate);

                // Early stop condition
                if (candidate.equalsIgnoreCase(targetPlaintext)) {
                    System.out.println("\n--------------------------------------------------");
                    System.out.println("[+] SUCCESS: Original text match found!");
                    System.out.println("[+] Total Attempts Needed : " + attempts + " / 312");
                    System.out.println("[+] Discovered Keys       : a = " + a + ", b = " + b);
                    System.out.println("[+] Recovered Plaintext   : " + candidate);
                    System.out.println("--------------------------------------------------");
                    found = true;
                    break;
                }
            }
            if (found) break; // Break outer loop
        }

        if (!found) {
            System.out.println("\n[-] Search exhausted without finding a match.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Get Plaintext
        System.out.print("Enter Plaintext Message: ");
        String plaintext = scanner.nextLine();

        // 2. Get Keys for Initial Encryption
        System.out.print("Enter Key 'a' (must be coprime to 26 e.g., 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25): ");
        int keyA = scanner.nextInt();

        System.out.print("Enter Key 'b' (0-25): ");
        int keyB = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        // Validate key 'a'
        if (modInverse(keyA) == -1) {
            System.out.println("\n[ERROR]: Key 'a' must be coprime to 26 (gcd(a, 26) = 1).");
            scanner.close();
            return;
        }

        // 3. Encrypt Plaintext -> Ciphertext
        String ciphertext = encrypt(plaintext, keyA, keyB);
        System.out.println("\n[+] Encrypted Ciphertext: " + ciphertext);

        // 4. Run Brute-Force Attack until match is found
        bruteForceAttack(ciphertext, plaintext);

        scanner.close();
    }
}
