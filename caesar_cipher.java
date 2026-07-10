import java.util.Scanner;

public class caesar_cipher {
    
    public static StringBuffer caesar_encrypt(String str, int key){
        StringBuffer sb = new StringBuffer();

        for(int i = 0;i < str.length();++i){
            char c = (char)(((int)str.charAt(i) + key - 97) % 26 + 97);
            sb.append(c);
        }

        return sb;
    }
    public static void main(String[] args){
        
        Scanner s = new Scanner(System.in);

        System.out.print("Enter the plain text in the lowercase: ");
        String str = s.nextLine();

        System.out.print("Enter the key: ");
        int key = s.nextInt();

        System.out.println("Original String: " + str);
        System.out.println("Key: " + key);
        System.out.println("Encrypted cipher: " + caesar_encrypt(str, key));

        s.close();
    }
}
