import java.util.*;

public class ColumnarTranspositionCipher {

    // Computes alphabetical column order indices from key string
    // Handles duplicate letters correctly (e.g., "SECRET" -> [4, 1, 0, 3, 2, 5])
    private static Integer[] getColumnOrder(String key) {
        int n = key.length();
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;

        Arrays.sort(order, (a, b) -> Character.compare(key.charAt(a), key.charAt(b)));
        return order;
    }

    // Encryption
    public static String encryptMessage(String msg, String key) {
        int col = key.length();
        int row = (int) Math.ceil((double) msg.length() / col);

        char[][] matrix = new char[row][col];
        int k = 0;

        // Fill matrix row-wise with message and '_' padding
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (k < msg.length()) {
                    matrix[i][j] = msg.charAt(k++);
                } else {
                    matrix[i][j] = '_';
                }
            }
        }

        Integer[] colOrder = getColumnOrder(key);
        StringBuilder cipher = new StringBuilder();

        // Read matrix column-wise in alphabetical order of key
        for (int c : colOrder) {
            for (int r = 0; r < row; r++) {
                cipher.append(matrix[r][c]);
            }
        }

        return cipher.toString();
    }

    // Decryption
    public static String decryptMessage(String cipher, String key) {
        int col = key.length();
        int row = (int) Math.ceil((double) cipher.length() / col);

        char[][] matrix = new char[row][col];
        Integer[] colOrder = getColumnOrder(key);

        int k = 0;

        // Fill matrix column-wise based on sorted key order
        for (int c : colOrder) {
            for (int r = 0; r < row; r++) {
                if (k < cipher.length()) {
                    matrix[r][c] = cipher.charAt(k++);
                }
            }
        }

        // Read matrix row-wise to reconstruct original message
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] != '_') {
                    msg.append(matrix[i][j]);
                }
            }
        }

        return msg.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter message text: ");
        String msg = scanner.nextLine();

        System.out.print("Enter secret key word: ");
        String key = scanner.nextLine().toUpperCase().replaceAll("[^A-Z]", "");

        if (key.isEmpty()) {
            System.out.println("Error: Key must contain at least one letter.");
            scanner.close();
            return;
        }

        String encrypted = encryptMessage(msg, key);
        String decrypted = decryptMessage(encrypted, key);

        System.out.println("\n--- RESULTS ---");
        System.out.println("Encrypted Message : " + encrypted);
        System.out.println("Decrypted Message : " + decrypted);

        scanner.close();
    }
}
/*import java.util.*;

public class ColumnarTranspositionCipher {
    // Key for Columnar Transposition
    static final String key = "HACK";
    static Map<Character, Integer> keyMap = new HashMap<>();

    static void setPermutationOrder() {
        // Add the permutation order into the map
        for (int i = 0; i < key.length(); i++) {
            keyMap.put(key.charAt(i), i);
        }
    }

    // Encryption
    static String encryptMessage(String msg) {
        int row, col;
        StringBuilder cipher = new StringBuilder();

        /* Calculate the number of columns in the matrix */
        col = key.length();

        /* Calculate the maximum number of rows in the matrix */
        row = (int) Math.ceil((double) msg.length() / col);

        char[][] matrix = new char[row][col];

        for (int i = 0, k = 0; i < row; i++) {
            for (int j = 0; j < col; ) {
                if (k < msg.length()) {
                    char ch = msg.charAt(k);
                    if (Character.isLetter(ch) || ch == ' ') {
                        matrix[i][j] = ch;
                        j++;
                    }
                    k++;
                } else {
                    /* Add padding character '_' */
                    matrix[i][j] = '_';
                    j++;
                }
            }
        }

        for (Map.Entry<Character, Integer> entry : keyMap.entrySet()) {
            int columnIndex = entry.getValue();

            // Get the cipher text from the matrix column-wise using the permuted key
            for (int i = 0; i < row; i++) {
                if (Character.isLetter(matrix[i][columnIndex]) || matrix[i][columnIndex] == ' ' || matrix[i][columnIndex] == '_') {
                    cipher.append(matrix[i][columnIndex]);
                }
            }
        }

        return cipher.toString();
    }

    // Decryption
    static String decryptMessage(String cipher) {
        /* Calculate the number of columns for the cipher matrix */
        int col = key.length();

        int row = (int) Math.ceil((double) cipher.length() / col);
        char[][] cipherMat = new char[row][col];

        /* Add characters into the matrix column-wise */
        int k = 0;
        for (int j = 0; j < col; j++) {
            for (int i = 0; i < row; i++) {
                cipherMat[i][j] = cipher.charAt(k);
                k++;
            }
        }

        /* Update the order of the key for decryption */
        int index = 0;
        for (Map.Entry<Character, Integer> entry : keyMap.entrySet()) {
            entry.setValue(index++);
        }

        /* Arrange the matrix column-wise according to the permutation order */
        char[][] decCipher = new char[row][col];
        for (int l = 0; l < key.length(); l++) {
            int columnIndex = keyMap.get(key.charAt(l));
            for (int i = 0; i < row; i++) {
                decCipher[i][l] = cipherMat[i][columnIndex];
            }
        }

        /* Get the message using the matrix */
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (decCipher[i][j] != '_') {
                    msg.append(decCipher[i][j]);
                }
            }
        }

        return msg.toString();
    }

    public static void main(String[] args) {
        /* Message */
        String msg = "Geeks for Geeks";

        setPermutationOrder();

        // Calling encryption function
        String cipher = encryptMessage(msg);
        System.out.println("Encrypted Message: " + cipher);

        // Calling Decryption function
        System.out.println("Decrypted Message: " + decryptMessage(cipher));
    }
}
*/
