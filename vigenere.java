import java.util.Scanner;

class VigenereCipher {

    String encrypt(String text, String key) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        key = key.toUpperCase().replaceAll("[^A-Z]", "");

        if (key.isEmpty()) return text;

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int p = text.charAt(i) - 'A';
            int k = key.charAt(i % key.length()) - 'A';
            int c = (p + k) % 26;
            result.append((char) (c + 'A'));
        }

        return result.toString();
    }

    String decrypt(String text, String key) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        key = key.toUpperCase().replaceAll("[^A-Z]", "");

        if (key.isEmpty()) return text;

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i) - 'A';
            int k = key.charAt(i % key.length()) - 'A';
            int p = (c - k + 26) % 26;
            result.append((char) (p + 'A'));
        }

        return result.toString();
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VigenereCipher obj = new VigenereCipher();

        System.out.print("Enter plaintext: ");
        String text = scanner.nextLine();

        System.out.print("Enter secret key: ");
        String key = scanner.nextLine();

        String cipher = obj.encrypt(text, key);
        System.out.println("\nEncrypted : " + cipher);
        System.out.println("Decrypted : " + obj.decrypt(cipher, key));

        scanner.close();
    }
}
/*
class VigenereCipher {

    String encrypt(String text,String key) {

        String result="";

        for(int i=0;i<text.length();i++) {
            int p=text.charAt(i)-'A';
            int k=key.charAt(i%key.length())-'A';
            int c=(p+k)%26;
            result += (char)(c+'A');
        }

        return result;
    }

    String decrypt(String text,String key) {

        String result="";

        for(int i=0;i<text.length();i++) {
            int c=text.charAt(i)-'A';
            int k=key.charAt(i%key.length())-'A';
            int p=(c-k+26)%26;
            result += (char)(p+'A');
        }

        return result;
    }

}

public class Main {

    public static void main(String args[]) {

        VigenereCipher obj=new VigenereCipher();

        String cipher=obj.encrypt("HELLO","KEY");

        System.out.println("Encrypted : "+cipher);

        System.out.println("Decrypted : "+obj.decrypt(cipher,"KEY"));

    }
}
*/
