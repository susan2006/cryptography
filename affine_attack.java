class AffineAttack {


    int inverse(int a) {

        for(int i=1;i<26;i++) {

            if((a*i)%26==1)
                return i;
        }

        return -1;
    }


    String decrypt(String text,int a,int b) {

        int inv=inverse(a);

        if(inv==-1)
            return "";


        String result="";


        for(char c:text.toCharArray()) {

            int y=c-'A';

            int x=(inv*(y-b+26))%26;

            result += (char)(x+'A');
        }


        return result;
    }


    void attack(String cipher) {


        for(int a=1;a<26;a++) {

            for(int b=0;b<26;b++) {


                String ans=decrypt(cipher,a,b);


                if(ans.length()>0)
                    System.out.println(a+" "+b+" : "+ans);

            }
        }
    }

}



public class Main {

    public static void main(String args[]) {

        AffineAttack obj=new AffineAttack();

        obj.attack("RCLLA");

    }

}
