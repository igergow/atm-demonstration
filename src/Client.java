import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client class for the ATM
 */
public class Client {
    /**
     * Scanner for user input
     */
    public static Scanner scanner;
    /**
     * Scanner for server input
     */
    public static Scanner reader;
    /**
     * PrintStream for server output
     */
    public static PrintStream writer;
    /**
     * Socket for the client
     */
    public static Socket socket;


    /**
     * Main method for the client
     * @param args Command line arguments
     * @throws IOException If there is an error with the socket
     */
    public static void main(String[] args) throws IOException {
        /**
         * Creating a new socket to the server, that is running on 127.0.0.1:8080
         */
        socket = new Socket("127.0.0.1", 8080);
        /**
         * Creating the input and output streams for the socket
         */
        var out = socket.getOutputStream();
        var in = socket.getInputStream();

        /**
         * Creating the scanner and writer for the socket
         */
        reader = new Scanner(in);
        writer = new PrintStream(out);

        /**
         * Creating the scanner for user input
         */
        scanner = new Scanner(System.in);
        /**
         * Processing the logic for the client
         */
        RunLogic();

        /**
         * Closing the socket, and the input and output streams
         */
        reader.close();
        writer.close();
        socket.close();
    }

    /**
     * Performs an action based on the command received from the server
     * @param wholeMessage The command received from the server
     */
    public static void PerformAction(String wholeMessage)
    {
        /**
         * Splitting the command into the command and the arguments
         */
        String[] commandArray = wholeMessage.split(":");
        /**
         * Getting the command from the command array
         */
        Commands cmd = Commands.valueOf(commandArray[0]);
        /**
         * Getting the arguments from the command array
         */
        String args = commandArray[1];

        /**
         * Printing the command and the arguments
         */
        var thread = Thread.currentThread();
        System.out.println("(R" + thread.getId() + ")[" + cmd  + "]: " + args);

        /**
         * Switching on the command received from the server
         */
        switch (cmd)
        {
            case WELCOME:
                // Sending a command to the server
                SendMessage(Commands.WELCOME, "Welcome from the client");
                break;
            case ASK_ACCOUNT_NUMBER:
                var accountNumber = scanner.nextLine();
                // Sending a command to the server
                SendMessage(Commands.ACCOUNT_NUMBER, accountNumber);
                break;
            case ACCOUNT_NUMBER:
                // Sending a command to the server
                System.out.println("Account number: " + args);
                break;
            case PIN:
                // Sending a command to the server
                System.out.println("PIN: " + args);
                break;
            case ASK_PIN:
                // Sending a command to the server
                var pin = scanner.nextLine();
                SendMessage(Commands.PIN, pin);
                break;
            case WITHDRAW:
                // Sending a command to the server
                var amount = scanner.nextLine();
                SendMessage(Commands.WITHDRAW, amount);
                break;
            case DEPOSIT:
                // Sending a command to the server
                amount = scanner.nextLine();
                SendMessage(Commands.DEPOSIT, amount);
                break;
            case GET_BALANCE:
                // Sending a command to the server
                SendStatus(Commands.GET_BALANCE, Status.OK);
                break;
            case ERROR:
                // Sending a command to the server
                SendStatus(Commands.GET_BALANCE, Status.ERROR);
                break;
            default:
                // Printing an error message if the command is not recognized
                SendMessage(Commands.ERROR, "Command not recognized");
                break;
        }
    }

    /**
     * Method to send a message to the server
     * @param command The command to send
     * @param message The message to send
     */
    public static void SendMessage(Commands command, String message)
    {
        var thread = Thread.currentThread();
        System.out.println("(S" + thread.getId() + ")[" + command + "] " + message);
        writer.printf("%s:%s\n", command, message);
        /**
         * Read the result of the operation
         */
        String wholeMessage = reader.nextLine();
        System.out.println("(R" + thread.getId() + ")[" + command + "] " + wholeMessage.split(":")[1]);
    }

    /**
     * Method to send a status to the server
     * @param command The command to send
     * @param status The status to send
     */
    public static void SendStatus(Commands command, Status status)
    {
        var thread = Thread.currentThread();
        System.out.println("(S" + thread.getId() + ")[" + command + "] " + status);
        writer.printf("%s:%s\n", command, status);
    }

    /**
     * Runs the logic for the client
     */
    public static void RunLogic()
    {
        /*
         * Reading the next line from the server
         */
        String wholeMessage = null;

        do {
            /*
             * Reading the next line from the server
             */
            wholeMessage = reader.nextLine();
            /*
             * Performing an action based on the command received from the server
             */
            PerformAction(wholeMessage);
        } while (wholeMessage != null && !wholeMessage.isEmpty() && !wholeMessage.isBlank());
    }
}
