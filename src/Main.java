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

            Thread clientThread1 = new Client(serverName, serverPort);
            clientThread1.start();

            Thread clientThread2 = new Client(serverName, serverPort);
            clientThread2.start();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
