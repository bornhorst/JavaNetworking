import java.net.*;
import java.io.*;

/*
Server Class for Socket Communication
 */
public class Server extends Thread {

    // server socket object
    private ServerSocket serverSocket;
    // server is running
    private boolean serverRunning;

    // class constructor for setting up the server
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
        serverRunning = true;
    }

    // thread for running the server
    public void run() {
        while(serverRunning) {
            try {
                // listen on port for client connections
                System.out.println("Listening on port " + serverSocket.getLocalPort());
                Socket server = serverSocket.accept();

                // accept messages from clients
                System.out.println(server.getRemoteSocketAddress() + "connected.");
                DataInputStream input = new DataInputStream(server.getInputStream());

                // close the server socket
                System.out.println(input.readUTF());
                DataOutputStream output = new DataOutputStream(server.getOutputStream());
                output.writeUTF("Closing connection to " + server.getLocalSocketAddress());
                server.close();

                serverRunning = false;

            } catch(SocketTimeoutException s) {
                System.out.println("Timeout on Socket!");
                break;
            } catch(IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
