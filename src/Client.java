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

    // client constructor
    public Client(String server, int port) {
        this.serverName = server;
        this.serverPort = port;
        this.clientRunning = true;
    }

    // thread for running the client
    public void run() {
        while(clientRunning) {
            try {
                // connect to a server
                System.out.println("Connecting to " + this.serverName + " on port " +
                                    this.serverPort);
                Socket client = new Socket(this.serverName, this.serverPort);

                // connection established
                System.out.println("Connected to " + client.getRemoteSocketAddress());
                OutputStream outputToServer = client.getOutputStream();
                DataOutputStream output = new DataOutputStream(outputToServer);

                // send message to server
                output.writeUTF("Client from " + client.getLocalSocketAddress());
                InputStream inputFromServer = client.getInputStream();
                DataInputStream input = new DataInputStream(inputFromServer);

                // receive message from server
                System.out.println("Server message: " + input.readUTF());

                // close connection
                client.close();

                clientRunning = false;

            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}
