import java.util.*;

public class Playfar {

    static char[][] matrix = new char[5][5];
    static Map<Character, int[]> pos = new HashMap<>();

    // Build 5x5 matrix
    static void buildMatrix(String key) {

        boolean[] used = new boolean[26];

        key = key.toLowerCase().replaceAll("j", "i");

        int r = 0, c = 0;

        // 1. insert key
        for (char ch : key.toCharArray()) {
            if (!used[ch - 'a']) {
                matrix[r][c] = ch;
                pos.put(ch, new int[]{r, c});
                used[ch - 'a'] = true;

                c++;
                if (c == 5) {
                    c = 0;
                    r++;
                }
            }
        }

        // 2. insert remaining letters
        for (char ch = 'a'; ch <= 'z'; ch++) {
            if (ch == 'j') continue;

            if (!used[ch - 'a']) {
                matrix[r][c] = ch;
                pos.put(ch, new int[]{r, c});
                used[ch - 'a'] = true;

                c++;
                if (c == 5) {
                    c = 0;
                    r++;
                }
            }
        }
    }

    // preprocess plaintext
    static String preprocess(String text) {
        text = text.toLowerCase().replaceAll("j", "i");

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'a' && c <= 'z') sb.append(c);
        }

        // handle duplicate pairs with filler 'x'
        for (int i = 0; i < sb.length() - 1; i += 2) {
            if (sb.charAt(i) == sb.charAt(i + 1)) {
                sb.insert(i + 1, 'x');
            }
        }

        if (sb.length() % 2 != 0) sb.append('x');

        return sb.toString();
    }

    // encrypt
    static String encrypt(String text) {

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {

            char a = text.charAt(i);
            char b = text.charAt(i + 1);

            int r1 = pos.get(a)[0], c1 = pos.get(a)[1];
            int r2 = pos.get(b)[0], c2 = pos.get(b)[1];

            // same row
            if (r1 == r2) {
                res.append(matrix[r1][(c1 + 1) % 5]);
                res.append(matrix[r2][(c2 + 1) % 5]);
            }

            // same column
            else if (c1 == c2) {
                res.append(matrix[(r1 + 1) % 5][c1]);
                res.append(matrix[(r2 + 1) % 5][c2]);
            }

            // rectangle
            else {
                res.append(matrix[r1][c2]);
                res.append(matrix[r2][c1]);
            }
        }

        return res.toString();
    }

    // decrypt
    static String decrypt(String text) {

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {

            char a = text.charAt(i);
            char b = text.charAt(i + 1);

            int r1 = pos.get(a)[0], c1 = pos.get(a)[1];
            int r2 = pos.get(b)[0], c2 = pos.get(b)[1];

            // same row
            if (r1 == r2) {
                res.append(matrix[r1][(c1 + 4) % 5]);
                res.append(matrix[r2][(c2 + 4) % 5]);
            }

            // same column
            else if (c1 == c2) {
                res.append(matrix[(r1 + 4) % 5][c1]);
                res.append(matrix[(r2 + 4) % 5][c2]);
            }

            // rectangle
            else {
                res.append(matrix[r1][c2]);
                res.append(matrix[r2][c1]);
            }
        }

        return res.toString();
    }

    // print matrix
    static void printMatrix() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter text:");
        String text = sc.next();

        System.out.println("Enter key:");
        String key = sc.next();

        buildMatrix(key);

        System.out.println("\nPlayfair Matrix:");
        printMatrix();

        String processed = preprocess(text);

        String enc = encrypt(processed);
        System.out.println("\nEncrypted: " + enc);

        String dec = decrypt(enc);
        System.out.println("Decrypted: " + dec);

        sc.close();
    }
}
