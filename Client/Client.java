package Client;
import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");

            // Prompt for username
            System.out.print("Enter your username: ");
            String username = stdIn.readLine();
            out.println(username);

            // Start thread to read messages from server
            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Handle user input
            do {
                String userInput;
                String post;
                System.out.println("Subject:");
                userInput = in.readLine();
                post = userInput + "~";//store subject
                
                if (userInput.equalsIgnoreCase("exit"))
                    break;

                System.out.println("Message:");
                userInput = in.readLine();
                post += userInput;
                // while ((userInput = stdIn.readLine()) != null) {
                //     post += userInput;
                //     break;
                // }

                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                out.println(post);
            }while (true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
