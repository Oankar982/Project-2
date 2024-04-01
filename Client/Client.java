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
                }
            }).start();
            
            //Handle user input
            String userInput, post = "";
            while ((userInput = stdIn.readLine()) != null) {
                
                if (userInput.equalsIgnoreCase("exit")) 
                {
                    break;
                }
                else if(post.contains("~"))
                {
                    out.println(post + userInput);
                    post = "";
                }
                else
                {
                    post = userInput + "~";
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
