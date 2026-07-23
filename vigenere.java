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
