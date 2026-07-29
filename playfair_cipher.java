import java.util.*;

public class Playfar {

    static char[][] matrix = new char[5][5];
    static Map<Character, int[]> pos = new HashMap<>();

    // Build 5x5 matrix
    static void buildMatrix(String key) {

        boolean[] used = new boolean[26];

        key = key.toLowerCase().replaceAll("j", "i");

        int r = 0, c = 0;

        // 1. insert key
        for (char ch : key.toCharArray()) {
            if (!used[ch - 'a']) {
                matrix[r][c] = ch;
                pos.put(ch, new int[]{r, c});
                used[ch - 'a'] = true;

                c++;
                if (c == 5) {
                    c = 0;
                    r++;
                }
            }
        }

        // 2. insert remaining letters
        for (char ch = 'a'; ch <= 'z'; ch++) {
            if (ch == 'j') continue;

            if (!used[ch - 'a']) {
                matrix[r][c] = ch;
                pos.put(ch, new int[]{r, c});
                used[ch - 'a'] = true;

                c++;
                if (c == 5) {
                    c = 0;
                    r++;
                }
            }
        }
    }

    // preprocess plaintext
    static String preprocess(String text) {
        text = text.toLowerCase().replaceAll("j", "i");

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'a' && c <= 'z') sb.append(c);
        }

        // handle duplicate pairs with filler 'x'
        for (int i = 0; i < sb.length() - 1; i += 2) {
            if (sb.charAt(i) == sb.charAt(i + 1)) {
                sb.insert(i + 1, 'x');
            }
        }

        if (sb.length() % 2 != 0) sb.append('x');

        return sb.toString();
    }

    // encrypt
    static String encrypt(String text) {

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {

            char a = text.charAt(i);
            char b = text.charAt(i + 1);

            int r1 = pos.get(a)[0], c1 = pos.get(a)[1];
            int r2 = pos.get(b)[0], c2 = pos.get(b)[1];

            // same row
            if (r1 == r2) {
                res.append(matrix[r1][(c1 + 1) % 5]);
                res.append(matrix[r2][(c2 + 1) % 5]);
            }

            // same column
            else if (c1 == c2) {
                res.append(matrix[(r1 + 1) % 5][c1]);
                res.append(matrix[(r2 + 1) % 5][c2]);
            }

            // rectangle
            else {
                res.append(matrix[r1][c2]);
                res.append(matrix[r2][c1]);
            }
        }

        return res.toString();
    }

    // decrypt
    static String decrypt(String text) {

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {

            char a = text.charAt(i);
            char b = text.charAt(i + 1);

            int r1 = pos.get(a)[0], c1 = pos.get(a)[1];
            int r2 = pos.get(b)[0], c2 = pos.get(b)[1];

            // same row
            if (r1 == r2) {
                res.append(matrix[r1][(c1 + 4) % 5]);
                res.append(matrix[r2][(c2 + 4) % 5]);
            }

            // same column
            else if (c1 == c2) {
                res.append(matrix[(r1 + 4) % 5][c1]);
                res.append(matrix[(r2 + 4) % 5][c2]);
            }

            // rectangle
            else {
                res.append(matrix[r1][c2]);
                res.append(matrix[r2][c1]);
            }
        }

        return res.toString();
    }

    // print matrix
    static void printMatrix() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter text:");
        String text = sc.next();

        System.out.println("Enter key:");
        String key = sc.next();

        buildMatrix(key);

        System.out.println("\nPlayfair Matrix:");
        printMatrix();

        String processed = preprocess(text);

        String enc = encrypt(processed);
        System.out.println("\nEncrypted: " + enc);

        String dec = decrypt(enc);
        System.out.println("Decrypted: " + dec);

        sc.close();
    }
}


import java.util.*;
public class PlayfairCipher {
	public static void main(String []args){
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter the plain text: ");
		String plain=sc.next();
		System.out.println("Enter the key: ");
		String key=sc.next();
                System.out.println("\nEncryption using playfair:\n");
		String pairplain=pair(plain);
		char[][] keymatrix=formKeyMatrix(key);
		System.out.println("Filler letter: X\n");
		System.out.println("5x5 key matrix(considering I and J as a single letter): ");
		for(int i=0;i<5;i++)
		{
			for(int j=0;j<5;j++)
			{
				System.out.print(keymatrix[i][j]+" ");
			}
			System.out.println("");
		}
		String ciphertext=toCipherOrPlain(keymatrix,pairplain);
		System.out.println("\nCorresponding Cipher text: "+ciphertext);
		System.out.println("\nDecryption using playfair:\n");
		String paircipher=pair(ciphertext);
		String pt=toCipherOrPlain(keymatrix,paircipher);
		String plaintext=removeFiller(pt);
		System.out.println("Cipher text: "+ciphertext);
		System.out.println("Corresponding Plain text: "+plaintext);

	}	
	public static String pair(String plain)
	{
		String pair="";
		char filler='X';
		for(int i=0;i<plain.length();i+=2)
		{
			if(plain.charAt(i)!=plain.charAt(i+1)) //if the letters to be paired are not same->pair them
			{
				pair+=plain.charAt(i)+""+plain.charAt(i+1);
			}
			else //if the letters to be paired are same->pair the first letter with filler
			{
				pair+=plain.charAt(i)+""+filler;
				i-=1;
			}
		}
		return pair;
	}
	
	public static char[][] formKeyMatrix(String key)
	{
		key=key.replaceAll("J","I"); //replace all J's with I's in key
		char[][] keymatrix=new char[5][5];
		int a=0;
		int Jascii=74;
		Set<Character> unique= new LinkedHashSet<Character>();
		String uniquestr="";
		for(int p=0;p<key.length();p++)
		{
			unique.add(key.charAt(p)); //resultant set will contain only unique characters in the key
		}
		for(char i=65;i<91;i++)
		{
			if(i!=Jascii) //The key matrix will not contain J (as I and J are considered as a single letter(I))
			unique.add(i);
		}
		Iterator<Character> iter=unique.iterator();
		while(iter.hasNext())
		{
			uniquestr+=iter.next();
		}
		for(int i=0;i<5;i++)
		{
			for(int j=0;j<5;j++)
			{
				keymatrix[i][j]=uniquestr.charAt(a);
				a++;
			}
		}
		return keymatrix;
	}
	public static String toCipherOrPlain(char[][] key, String pair)
	{
		boolean found1=false;
		boolean found2=false;
		int frow = 0,fcol=0,srow=0,scol=0;
		String cipherorplain="";
		for(int ind=0;ind+1<pair.length();ind+=2)
		{
			found1=false;
			found2=false;
			for(int i=0;i<5;i++)
			{
				for(int j=0;j<5;j++)
				{
					if(pair.charAt(ind)==key[i][j]) 
					{
						frow=i;
						fcol=j;
						found1=true;
					}
					if(pair.charAt(ind+1)==key[i][j])
					{
						srow=i;
						scol=j;
						found2=true;
					}
					if(found1 & found2)
						break;
				}
				if(found1 & found2)
					break;
			}

			if(frow==srow) //case I - both the letters are in the same row
			{
				if(fcol+1<5)
				{
					cipherorplain+=key[frow][fcol+1];
				}
				else
				{
					cipherorplain+=key[frow][0];
				}
				if(scol+1<5)
				{
					cipherorplain+=key[srow][scol+1];
				}
				else
				{
					cipherorplain+=key[frow][0];
				}
			}
			else if(fcol==scol) //case II - both the letters are in the same column
			{
				if(frow+1<5)
				{
					cipherorplain+=key[frow+1][fcol];
				}
				else
				{
					cipherorplain+=key[0][fcol];
				}
				if(srow+1<5)
				{
					cipherorplain+=key[srow+1][fcol];
				}
				else
				{
					cipherorplain+=key[0][fcol];
				}
			}
			else // Case III - both the letters are neither in the same row nor in the same column
			{
				cipherorplain+=key[frow][scol];
				cipherorplain+=key[srow][fcol];
			}

		}
       return cipherorplain;
	}
	public static String removeFiller(String text)
	{
		StringBuilder ct=new StringBuilder(text);
		for(int i=0;i<ct.length();i++)
		{
			if(ct.charAt(i)=='X')
			{
				ct.deleteCharAt(i);
				
			}
		}
		return ct.toString();	
	}
	}


