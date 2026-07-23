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
