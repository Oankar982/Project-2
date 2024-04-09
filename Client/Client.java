package Client;
import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args){
        //need initialized default vals
        String serverAddy = "";
        int serverPort = 0;
        //buffered reader for fetching 
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Please type '%connect' followed by server address and port: \n");
        //fetch server address and port number from user input
        do{
            try{
                String serverInfo[] = stdIn.readLine().split(" ");
                if(serverInfo[0].equals("%connect"))
                {
                    serverAddy = serverInfo[1];
                    serverPort = Integer.parseInt(serverInfo[2]);
                }
                else{
                    System.out.println("please use %connect followed by address and port number");
                }
            }catch (Exception e) {
                System.out.println("Please fix your command format.");
            }
        }while(serverAddy.equals("") || serverPort == 0);

        try
        {
            Socket socket = new Socket(serverAddy, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
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
            
            //Handle user input
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }

            socket.close();
        } catch (Exception e) {
            System.out.println("Please fix your command format.");
        }
    }
}
