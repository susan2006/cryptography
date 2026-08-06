import java.util.*;

public class PlayfairCipher {

    private static char[][] matrix = new char[5][5];
    private static Map<Character, int[]> posMap = new HashMap<>();

    // 1. Build the 5x5 Key Matrix
    public static void buildMatrix(String key) {
        matrix = new char[5][5];
        posMap.clear();

        boolean[] used = new boolean[26];
        key = key.toLowerCase().replaceAll("j", "i").replaceAll("[^a-z]", "");

        int r = 0, c = 0;

        // Insert key characters
        for (char ch : key.toCharArray()) {
            if (!used[ch - 'a']) {
                matrix[r][c] = ch;
                posMap.put(ch, new int[]{r, c});
                used[ch - 'a'] = true;
                c++;
                if (c == 5) { c = 0; r++; }
            }
        }

        // Insert remaining alphabet letters (excluding 'j')
        for (char ch = 'a'; ch <= 'z'; ch++) {
            if (ch == 'j') continue;
            if (!used[ch - 'a']) {
                matrix[r][c] = ch;
                posMap.put(ch, new int[]{r, c});
                used[ch - 'a'] = true;
                c++;
                if (c == 5) { c = 0; r++; }
            }
        }
    }

    // 2. Preprocess Plaintext into Digraphs (Pairs)
    public static String prepareText(String text) {
        text = text.toLowerCase().replaceAll("j", "i").replaceAll("[^a-z]", "");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            sb.append(current);

            // Insert filler 'x' if adjacent pair letters are identical
            if (i + 1 < text.length() && current == text.charAt(i + 1) && sb.length() % 2 != 0) {
                sb.append('x');
            }
        }

        // Pad with 'x' if odd length
        if (sb.length() % 2 != 0) {
            sb.append('x');
        }

        return sb.toString();
    }

    // 3. Encrypt Digraph Pairs
    public static String encrypt(String text) {
        return transform(text, 1);
    }

    // 4. Decrypt Digraph Pairs
    public static String decrypt(String text) {
        return transform(text, 4); // Shift of -1 mod 5 is equivalent to +4 mod 5
    }

    // Unified Transformation Logic
    private static String transform(String text, int shift) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);

            int[] p1 = posMap.get(a);
            int[] p2 = posMap.get(b);

            int r1 = p1[0], c1 = p1[1];
            int r2 = p2[0], c2 = p2[1];

            if (r1 == r2) {
                // Same row -> Shift columns
                result.append(matrix[r1][(c1 + shift) % 5]);
                result.append(matrix[r2][(c2 + shift) % 5]);
            } else if (c1 == c2) {
                // Same column -> Shift rows
                result.append(matrix[(r1 + shift) % 5][c1]);
                result.append(matrix[(r2 + shift) % 5][c2]);
            } else {
                // Rectangle -> Swap columns
                result.append(matrix[r1][c2]);
                result.append(matrix[r2][c1]);
            }
        }

        return result.toString();
    }

    // Display 5x5 Matrix
    public static void printMatrix() {
        System.out.println("\nPlayfair 5x5 Matrix:");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(Character.toUpperCase(matrix[i][j]) + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter plaintext: ");
        String text = sc.nextLine();

        System.out.print("Enter secret key: ");
        String key = sc.nextLine();

        buildMatrix(key);
        printMatrix();

        String preparedText = prepareText(text);
        System.out.println("\nPrepared Digraph Text : " + preparedText);

        String cipherText = encrypt(preparedText);
        System.out.println("Encrypted Ciphertext  : " + cipherText.toUpperCase());

        String decryptedText = decrypt(cipherText);
        System.out.println("Decrypted Text        : " + decryptedText);

        sc.close();
    }
}
/*
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

*/
