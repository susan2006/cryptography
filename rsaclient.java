import java.io.*;
import java.net.*;
import java.util.Scanner;

public class rsaclient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to RSA Server.");

            // 1. Receive Public Key (e, n) from Server
            long e = in.readLong();
            long n = in.readLong();
            System.out.println("--> Received Public Key from Server: (e=" + e + ", n=" + n + ")");

            // 2. Read plaintext input from User
            System.out.print("\nEnter Plaintext Message to Encrypt: ");
            long plaintext = scanner.nextLong();

            // 3. Encrypt each character: C = M^e mod n
            long ciphertext = rsaengine.modPow(plaintext, e, n);

            System.out.println("\n[Client Local Processing]");
            System.out.print("--> Encrypted Ciphertext : " + ciphertext);

            out.writeLong(ciphertext);

            // 5. Read response confirmation from Server
            long serverResponse = in.readLong();
            System.out.println("\n[Server Response] " + serverResponse);

        } catch (IOException ex) {
            System.err.println("Client Error: " + ex.getMessage());
        }
    }
}