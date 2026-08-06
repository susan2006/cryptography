class HillCipher {

    int encryptKey[][] = {
            {3,3},
            {2,5}
    };


    int decryptKey[][] = {
            {15,17},
            {20,9}
    };


    String process(String text,int key[][]) {

        String result="";


        for(int i=0;i<text.length();i+=2) {


            int x=text.charAt(i)-'A';

            int y=text.charAt(i+1)-'A';


            int a=(key[0][0]*x + key[0][1]*y)%26;

            int b=(key[1][0]*x + key[1][1]*y)%26;


            result+=(char)(a+'A');

            result+=(char)(b+'A');

        }


        return result;
    }



    String encrypt(String text) {

        return process(text,encryptKey);

    }


    String decrypt(String text) {

        return process(text,decryptKey);

    }

}



public class Main {

    public static void main(String args[]) {


        HillCipher obj=new HillCipher();


        String cipher=obj.encrypt("HELP");


        System.out.println("Encrypted : "+cipher);


        System.out.println("Decrypted : "+obj.decrypt(cipher));

    }
}

import java.util.Scanner;

public class HillCipher {

    // Computes modular multiplicative inverse of 'a' mod 26
    private static int modInverse(int a, int m) {
        a = (a % m + m) % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) return x;
        }
        return -1;
    }

    // Computes matrix inverse modulo 26 using Gauss-Jordan elimination
    public static int[][] computeInverseKey(int[][] key, int n) {
        int[][] augmented = new int[n][2 * n];

        // Construct [K | I] augmented matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmented[i][j] = (key[i][j] % 26 + 26) % 26;
            }
            augmented[i][i + n] = 1; // Identity matrix on the right
        }

        // Forward and backward elimination mod 26
        for (int i = 0; i < n; i++) {
            // Find pivot element coprime to 26
            int pivotRow = -1;
            for (int r = i; r < n; r++) {
                if (modInverse(augmented[r][i], 26) != -1) {
                    pivotRow = r;
                    break;
                }
            }

            if (pivotRow == -1) {
                return null; // Matrix is non-invertible mod 26
            }

            // Swap current row with pivot row
            if (pivotRow != i) {
                int[] temp = augmented[i];
                augmented[i] = augmented[pivotRow];
                augmented[pivotRow] = temp;
            }

            // Multiply row by modular inverse of pivot
            int invPivot = modInverse(augmented[i][i], 26);
            for (int j = 0; j < 2 * n; j++) {
                augmented[i][j] = (augmented[i][j] * invPivot) % 26;
            }

            // Eliminate column entries in other rows
            for (int r = 0; r < n; r++) {
                if (r != i) {
                    int factor = augmented[r][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmented[r][j] = (augmented[r][j] - factor * augmented[i][j]) % 26;
                        augmented[r][j] = (augmented[r][j] % 26 + 26) % 26;
                    }
                }
            }
        }

        // Extract right half [I | K^-1]
        int[][] invKey = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                invKey[i][j] = augmented[i][j + n];
            }
        }
        return invKey;
    }

    // Generic N x N Hill Cipher Process
    public static String process(String text, int[][] key, int n) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");

        // Pad with 'X' to make length a multiple of N
        while (text.length() % n != 0) {
            text += "X";
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += n) {
            int[] vector = new int[n];
            for (int j = 0; j < n; j++) {
                vector[j] = text.charAt(i + j) - 'A';
            }

            // Matrix-Vector Multiplication: C = (K * P) mod 26
            for (int r = 0; r < n; r++) {
                int sum = 0;
                for (int c = 0; c < n; c++) {
                    sum += key[r][c] * vector[c];
                }
                sum = (sum % 26 + 26) % 26;
                result.append((char) (sum + 'A'));
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter matrix size N (e.g., 2, 3, 4): ");
        int n = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        System.out.print("Enter plaintext message: ");
        String text = scanner.nextLine();

        int[][] key = new int[n][n];
        System.out.println("Enter " + (n * n) + " integer elements for the " + n + "x" + n + " Key Matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                key[i][j] = scanner.nextInt();
            }
        }

        // Derive inverse matrix K^-1 mod 26
        int[][] decryptKey = computeInverseKey(key, n);

        if (decryptKey == null) {
            System.out.println("\n[ERROR]: Key matrix is NOT invertible modulo 26! Choose another key matrix.");
        } else {
            String ciphertext = process(text, key, n);
            String decryptedtext = process(ciphertext, decryptKey, n);

            System.out.println("\n--- RESULTS ---");
            System.out.println("Encrypted Ciphertext : " + ciphertext);
            System.out.println("Decrypted Plaintext  : " + decryptedtext);
        }

        scanner.close();
    }
}
