import java.net.*;
import java.io.*;
import java.util.concurrent.Semaphore;

/*
Server Class for Socket Communication
 */
public class Server extends Thread {

    // server socket object
    private ServerSocket serverSocket;
    // server is running
    private boolean serverRunning;
    // total clients
    private int clientConnections = 0;

    // constant
    static private final int MAX_CLIENTS = 2;
    static private Semaphore semaphore = new Semaphore(MAX_CLIENTS);

    // class constructor for setting up the server
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverRunning = true;
    }

    // thread for running the server
    @Override
    public void run() {
        // listen on port for client connections
        System.out.println("Server:> Listening on port " + serverSocket.getLocalPort());
        while(serverRunning) {
            try {
                // listen for clients to connect
                if(clientConnections < MAX_CLIENTS)
                    serverAccept();
                else {
                    serverSocket.close();
                    break;
                }

            } catch(IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void serverAccept() throws IOException {
        Socket clientSocket;
        clientSocket = serverSocket.accept();
        this.clientConnections += 1;
        System.out.println("Clients connected: " + clientConnections);
        Thread client = new EchoThread(clientSocket, semaphore, this.clientConnections);
        client.start();
    }

}
