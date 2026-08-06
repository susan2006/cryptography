import java.util.Arrays;
import java.util.Scanner;

public class RailFence {

    public static String encryptRailFence(String text, int key) {
        if (key <= 1 || text.length() <= key) return text;

        // Use '\0' (null character) instead of '\n' to prevent text character collisions
        char[][] rail = new char[key][text.length()];
        for (int i = 0; i < key; i++) {
            Arrays.fill(rail[i], '\0');
        }

        boolean dirDown = false;
        int row = 0, col = 0;

        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == key - 1) {
                dirDown = !dirDown;
            }

            rail[row][col++] = text.charAt(i);

            row += dirDown ? 1 : -1;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                if (rail[i][j] != '\0') {
                    result.append(rail[i][j]);
                }
            }
        }

        return result.toString();
    }

    public static String decryptRailFence(String cipher, int key) {
        if (key <= 1 || cipher.length() <= key) return cipher;

        char[][] rail = new char[key][cipher.length()];
        for (int i = 0; i < key; i++) {
            Arrays.fill(rail[i], '\0');
        }

        boolean dirDown = false;
        int row = 0, col = 0;

        // Mark positions in the rail matrix
        for (int i = 0; i < cipher.length(); i++) {
            if (row == 0 || row == key - 1) {
                dirDown = !dirDown;
            }

            rail[row][col++] = '*';

            row += dirDown ? 1 : -1;
        }

        // Fill the marked positions with cipher characters
        int index = 0;
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < cipher.length(); j++) {
                if (rail[i][j] == '*' && index < cipher.length()) {
                    rail[i][j] = cipher.charAt(index++);
                }
            }
        }

        // Reconstruct plaintext by following zig-zag order
        StringBuilder result = new StringBuilder();
        row = 0;
        col = 0;
        dirDown = false;

        for (int i = 0; i < cipher.length(); i++) {
            if (row == 0 || row == key - 1) {
                dirDown = !dirDown;
            }

            if (rail[row][col] != '\0') {
                result.append(rail[row][col++]);
            }

            row += dirDown ? 1 : -1;
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter plaintext message: ");
        String text = scanner.nextLine();

        System.out.print("Enter key (number of rails): ");
        int key = scanner.nextInt();

        String encrypted = encryptRailFence(text, key);
        String decrypted = decryptRailFence(encrypted, key);

        System.out.println("\n--- RESULTS ---");
        System.out.println("Original  : " + text);
        System.out.println("Encrypted : " + encrypted);
        System.out.println("Decrypted : " + decrypted);

        scanner.close();
    }
}
/*import java.util.Arrays;

class RailFence {

    public static String encryptRailFence(String text, int key) {
        char[][] rail = new char[key][text.length()];
        for (int i = 0; i < key; i++)
            Arrays.fill(rail[i], '\n');

        boolean dirDown = false;
        int row = 0, col = 0;

        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == key - 1)
                dirDown = !dirDown;

            rail[row][col++] = text.charAt(i);

            if (dirDown)
                row++;
            else
                row--;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < key; i++)
            for (int j = 0; j < text.length(); j++)
                if (rail[i][j] != '\n')
                    result.append(rail[i][j]);

        return result.toString();
    }

    public static String decryptRailFence(String cipher, int key) {
        char[][] rail = new char[key][cipher.length()];

        for (int i = 0; i < key; i++)
            Arrays.fill(rail[i], '\n');

        boolean dirDown = true;
        int row = 0, col = 0;

        for (int i = 0; i < cipher.length(); i++) {
            if (row == 0)
                dirDown = true;
            if (row == key - 1)
                dirDown = false;

            rail[row][col++] = '*';

            if (dirDown)
                row++;
            else
                row--;
        }

        int index = 0;
        for (int i = 0; i < key; i++)
            for (int j = 0; j < cipher.length(); j++)
                if (rail[i][j] == '*' && index < cipher.length())
                    rail[i][j] = cipher.charAt(index++);

        StringBuilder result = new StringBuilder();
        row = 0;
        col = 0;
        for (int i = 0; i < cipher.length(); i++) {
            if (row == 0)
                dirDown = true;
            if (row == key - 1)
                dirDown = false;

            if (rail[row][col] != '*')
                result.append(rail[row][col++]);

            if (dirDown)
                row++;
            else
                row--;
        }

        return result.toString();
    }

    public static void main(String[] args) {
        String message1 = "attack at once";
        int key1 = 2;
        String encrypted1 = encryptRailFence(message1, key1);

        String message2 = "defend the east wall";
        int key2 = 3;
        String encrypted2 = encryptRailFence(message2, key2);

        System.out.println("--- Message 1 ---");
        System.out.println("Original:  " + message1);
        System.out.println("Encrypted: " + encrypted1);
        System.out.println("Decrypted: " + decryptRailFence(encrypted1, key1));

        System.out.println("\n--- Message 2 ---");
        System.out.println("Original:  " + message2);
        System.out.println("Encrypted: " + encrypted2);
        System.out.println("Decrypted: " + decryptRailFence(encrypted2, key2));
    }
}
*/
