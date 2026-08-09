import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ColumnarClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to Columnar Transposition Server.\n");

            // 1. Get Message and Key from User
            System.out.print("Enter message text     : ");
            String msg = scanner.nextLine();

            System.out.print("Enter secret key word  : ");
            String key = scanner.nextLine().toUpperCase().replaceAll("[^A-Z]", "");

            if (key.isEmpty()) {
                System.out.println("Error: Key must contain at least one letter.");
                return;
            }

            // 2. Perform Local Encryption on Client
            String ciphertext = ColumnarEngine.encryptMessage(msg, key);

            System.out.println("\n[Client Local Processing]");
            System.out.println("--> Encrypted Ciphertext : " + ciphertext);
            System.out.println("--> Transmitting to Server...");

            // 3. Send Ciphertext and Key to Server
            out.writeUTF(ciphertext);
            out.writeUTF(key);

            // 4. Receive Response from Server
            String serverResponse = in.readUTF();
            System.out.println("\n[Server Response] " + serverResponse);

        } catch (IOException e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }
}
