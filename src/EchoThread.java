import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;

/*
Class for Allowing Server to Communicate with Multiple Clients
 */
public class EchoThread extends Thread {

    protected Socket socket;
    protected int clientPosition;

    private boolean clientRunning;

    Semaphore echoSemaphore;

    // class constructor
    public EchoThread(Socket clientSocket, Semaphore semaphore, int clientNum) {
        this.socket = clientSocket;
        this.clientPosition = clientNum;
        this.clientRunning = true;
        this.echoSemaphore = semaphore;
    }

    @Override
    public void run() {
        //Lock lock = new ReentrantLock();
        InputStream input = null;
        BufferedReader inputBR = null;
        DataOutputStream output = null;

        try {
            input = this.socket.getInputStream();
            inputBR = new BufferedReader(new InputStreamReader(input));
            output = new DataOutputStream(this.socket.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }

        String inputLine;
        String outputLine;
        while(clientRunning) {
            //lock.lock();
            try {
                echoSemaphore.acquire();
                System.out.println("Sem Permits1: " + echoSemaphore.availablePermits());
                inputLine = inputBR.readLine();
                if ((inputLine == null) || inputLine.contains("Exit")) {
                    socket.close();
                    this.clientRunning = false;
                    --this.clientPosition;
                    System.out.println(this.clientPosition);
                    if (this.clientPosition == 0) {
                        System.out.println("All Clients Disconnected");
                        //server.close();
                    }
                    break;
                } else if (inputLine.contains("Hello")) {
                    outputLine = "Welcome client " + clientPosition;
                    output.writeBytes(outputLine + "\n");
                    output.flush();
                    System.out.println("Server:> " + outputLine);
                }
            } catch(InterruptedException i) {
                i.printStackTrace();
                break;
            } catch(IOException e) {
                e.printStackTrace();
                break;
            } finally {
                echoSemaphore.release();
                System.out.println("Sem Permits2: " + echoSemaphore.availablePermits());
            }
        }
    }

}
