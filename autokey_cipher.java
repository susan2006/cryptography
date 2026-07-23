class AutoKeyCipher {

    String encrypt(String text,String key) {

        String fullKey=key+text;

        String result="";

        for(int i=0;i<text.length();i++) {
            int p=text.charAt(i)-'A';
            int k=fullKey.charAt(i)-'A';
            int c=(p+k)%26;
            result+=(char)(c+'A');
        }

        return result;

    }

    String decrypt(String cipher,String key) {

        String fullKey=key;

        String result="";

        for(int i=0;i<cipher.length();i++) {
            int c=cipher.charAt(i)-'A';
            int k=fullKey.charAt(i)-'A';
            int p=(c-k+26)%26;
            char ch=(char)(p+'A');
            result+=ch;
            fullKey+=ch;
        }

        return result;

    }

}

public class Main {

    public static void main(String args[]) {

        AutoKeyCipher obj=new AutoKeyCipher();

        String cipher=obj.encrypt("HELLO","K");

        System.out.println(cipher);

        System.out.println(obj.decrypt(cipher,"K"));

    }
}
