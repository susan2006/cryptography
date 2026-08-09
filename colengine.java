import java.util.Arrays;

public class ColumnarEngine {

    // Computes alphabetical column order indices from key string
    public static Integer[] getColumnOrder(String key) {
        int n = key.length();
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;

        Arrays.sort(order, (a, b) -> Character.compare(key.charAt(a), key.charAt(b)));
        return order;
    }

    // Encryption Logic
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

    // Decryption Logic
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
}
