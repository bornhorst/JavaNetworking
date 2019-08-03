import java.net.*;
import java.io.*;

/*
Client Class for Socket Communication
 */
public class Client extends Thread {

    // server name to connect to
    private String serverName;
    // server port to connect to
    private int serverPort;
    // client is running
    private boolean clientRunning;
    // socket for client connection
    private Socket clientSocket;
    // client position
    private String clientPosition;
    // client messaging
    private InputStream input = null;
    private BufferedReader inputBR = null;
    private DataOutputStream output = null;

    // client constructor
    public Client(String server, int port) {
        this.serverName = server;
        this.serverPort = port;
        this.clientRunning = true;
    }

    // thread for running the client
    @Override
    public void run() {
        try {
            // connect to the server
            clientConnect();
            input = this.clientSocket.getInputStream();
            inputBR = new BufferedReader(new InputStreamReader(input));
            output = new DataOutputStream(this.clientSocket.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }

        while(clientRunning) {
            try {
                // initial state
                clientSend("Hello");

                // receive message from server
                if(clientReceive().contains("Welcome")) {
                    clientSend("Exit");
                    clientRunning = false;
                }

            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    this.clientSocket.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String clientReceive() throws IOException {
        String inputLine = inputBR.readLine();
        System.out.println("Read from Server: " + inputLine);
        this.clientPosition = inputLine;
        this.clientPosition = clientPosition.replaceAll("[^0-9]+", "");
        return inputLine;
    }

    private void clientSend(String message) throws IOException {
        this.output.writeBytes(message + "\n");
        this.output.flush();
        if(this.clientPosition == null)
            System.out.println("Client:> " + message);
        else
            System.out.println("Client" + this.clientPosition + ":> " + message);
    }

    private void clientConnect() throws IOException {
        // connect to a server
        System.out.println("Client:> Connecting to " + serverName + " on port " + serverPort);
        this.clientSocket = new Socket(serverName, serverPort);
    }

}
