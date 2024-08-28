import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class for the ATM
 */
public class Server {
    /**
     * Main method for the server
     * @param args Command line arguments
     * @throws IOException If there is an error with the socket
     */
    public static void main(String[] args) throws IOException {
        /**
         * Creating the server socket on port 8080
         */
        ServerSocket serverSocket = new ServerSocket(8080);
        /**
         * Creating a thread pool with 3 threads
         */
        ExecutorService executor = Executors.newFixedThreadPool(3);
        /**
         * Processing the logic for the server
         */
        while(true)
        {
            /**
             * Accepting a new connection
             */
            Socket socket = serverSocket.accept();
            /**
             * Creating a new thread for the connection
             */
            executor.execute(new ServerThread(socket));
        }
    }
}
