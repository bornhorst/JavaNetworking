import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        // server parameters
        String serverName = "localhost";
        int serverPort = 6666;

        // run the server and client
        try {
            Thread serverThread = new Server(serverPort);
            serverThread.start();

            Thread clientThread = new Client(serverName, serverPort);
            clientThread.start();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
