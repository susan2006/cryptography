import java.util.Scanner;

public class modular {
    
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);

        System.out.print("Enter dividend: ");
        int dividend = s.nextInt();

        System.out.print("Enter divisor: ");
        int divisor = s.nextInt();
        
        int rem = 0;
        if(dividend > 0){
            rem = (dividend - divisor * (dividend / divisor));
        }
        else{
            if(Math.abs(dividend) < divisor){
                rem = divisor - Math.abs(dividend);
            }
            else{
                int a = Math.abs(dividend);
                a = (a - divisor * (a / divisor));
                rem = divisor - a;
            }
        }

        System.out.println("Remainder is: " + rem);
        s.close();
    }
}
