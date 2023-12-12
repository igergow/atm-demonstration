import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Server thread class for the ATM
 */
public class ServerThread implements Runnable {
    /**
     * List of all bank accounts
     */
    private static ArrayList<Account> accounts;

    /**
     * Store session data for the server
     */
    private HashMap<Data, Object> sessionData;
    /**
     * Socket for the server
     */
    private Socket socket;
    /**
     * Scanner for user input
     */
    private Scanner reader;
    /**
     * Scanner for server input
     */
    private Scanner scanner;
    /**
     * PrintStream for server output
     */
    private PrintStream writer;

    /**
     * Constructor for the server thread
     * @param server The socket for the server
     * @throws IOException If there is an error with the socket
     */
    public ServerThread (Socket server) throws IOException {
        /**
         * Creating the list of accounts
         */
        accounts = new ArrayList<Account>() {{
            add(new Account("123456789", "1234", 100));
            add(new Account("987654321", "4321", 10000));
            add(new Account("111111111", "1111", 1000));
        }};
        
        sessionData = new HashMap<Data, Object>();
        socket = server;
    }

    /**
     * Run method for the server thread
     */
    @Override
    public void run() {

        try {
            /**
             * Creating the input and output streams for the socket
             */
            var out = socket.getOutputStream();
            var in = socket.getInputStream();

            /**
             * Creating the scanner for user input
             */
            scanner = new Scanner(System.in);
            /** 
             * Creating the scanner and writer for the socket
             */
            reader = new Scanner(in);
            writer = new PrintStream(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Processing the logic for the server
         */
        ServerLogic();
        
        /**
         * Closing the socket, and the input and output streams
         */
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to send a message to the client
     * @param command The command to send
     * @param message The message to send
     */
    public void SendMessage(Commands command, String message)
    {
        var thread = Thread.currentThread();
        // Printing the command and the message to the console with the thread id
        System.out.println("(S" + thread.getId() + ")[" + command + "] " + message);
        // Sending the command and the message to the client
        writer.printf("%s:%s\n", command, message);
    }

    /**
     * Method to send a status to the client
     * @param command The command to send
     * @param status The status to send
     */
    public void SendStatus(Commands command, Status status)
    {
        var thread = Thread.currentThread();
        // Printing the command and the status to the console with the thread id
        System.out.println("(S" + thread.getId() + ")[" + command + "] " + status);
        // Sending the command and the status to the client
        writer.printf("%s:%s\n", command, status);
    }

    /**
     * Method to get a message from the client
     */
    public void GetMessage()
    {
        // Getting the current thread
        var thread = Thread.currentThread();
        // Getting the message from the client
        String wholeMessage = reader.nextLine();
        // Splitting the message into the command and the arguments
        String[] commandArray = wholeMessage.split(":");
        // Getting the command and the arguments
        Commands cmd = Commands.valueOf(commandArray[0]);
        String args = commandArray[1];
        // Printing the command and the arguments to the console with the thread id
        System.out.println("(R" + thread.getId() + ")[" + cmd + "] " + args);
        // Processing the message
        ProcessMessage(cmd, args);
    }

    /**
     * Method to process a message from the client
     * @param cmd The command
     * @param args The arguments
     */
    public void ProcessMessage(Commands cmd, String args)
    {
        switch (cmd)
        {
            case WELCOME:
                SendStatus(Commands.WELCOME, Status.OK);
                break;
            case ACCOUNT_NUMBER:
                // Checking if the account number exists
                Status message = accounts
                                    .stream()
                                    .anyMatch(account -> account.getAccountNumber().equals(args)) ? Status.OK : Status.ERROR;
                // Saving the account number in the session data
                if (message == Status.OK) sessionData.put(Data.ACCOUNT_NUMBER, args);
                // Sending the status to the client
                SendStatus(Commands.ACCOUNT_NUMBER, message);
                break;
            case PIN:
                // Getting the account number from the session data
                var accountNumber = sessionData.get(Data.ACCOUNT_NUMBER);

                // Getting the account from the list of accounts with the account number
                var account = accounts.stream().filter(acc -> acc.getAccountNumber().equals(accountNumber)).findFirst().get();
                // Checking if the pin is correct
                message = account.checkPin(args) ? Status.OK : Status.ERROR;
                // Saving the account in the session data
                if (message == Status.OK) sessionData.put(Data.ACCOUNT, account);
                // Sending the status to the client
                SendStatus(Commands.PIN, message);
                break;
            case WITHDRAW:
                try {
                    // Getting the account from the session data
                    account = (Account) sessionData.get(Data.ACCOUNT);
                    // Withdrawing the amount
                    account.withdraw(Double.parseDouble(args));
                    // Sending the status to the client
                    SendStatus(Commands.WITHDRAW, Status.OK);
                } catch (Exception e) {
                    // Sending the status to the client
                    SendStatus(Commands.WITHDRAW, Status.ERROR);
                    break;
                }
                break;
            case DEPOSIT:
                try {
                    // Getting the account from the session data
                    account = (Account) sessionData.get(Data.ACCOUNT);
                    // Depositing the amount
                    account.deposit(Double.parseDouble(args));
                    // Sending the status to the client
                    SendStatus(Commands.DEPOSIT, Status.OK);
                } catch (Exception e) {
                    // Sending the status to the client
                    SendStatus(Commands.DEPOSIT, Status.ERROR);
                    break;
                }
                break;
            case GET_BALANCE:
                // SendStatus(Commands.GET_BALANCE, Status.OK);
                break;
            default:
                SendStatus(Commands.ERROR, Status.ERROR);
                break;
        }
    }

    /**
     * Method to process the logic for the server
     */
    public void ServerLogic()
    {
        // Sending a command to the client to start the communication
        SendMessage(Commands.WELCOME, "Welcome to the ATM");
        GetMessage();

        // // Sending a command to the client to ask for the account number
        // SendMessage(Commands.ASK_ACCOUNT_NUMBER, "Please enter your account number");
        // GetMessage();

        // // Sending a command to the client to ask for the PIN
        // SendMessage(Commands.ASK_PIN, "Please enter your PIN");
        // GetMessage();

        // // Sending a command to the client to get the balance
        // SendMessage(Commands.GET_BALANCE, "Your balance is $100");
        // GetMessage();

        // // Sending a command to the client to deposit
        // SendMessage(Commands.DEPOSIT, "Please enter the amount to deposit");
        // GetMessage();

        // // Sending a command to the client to withdraw
        // SendMessage(Commands.WITHDRAW, "Please enter the amount to withdraw");
        // GetMessage();

        // // Sending a command to the client to get the balance
        // SendMessage(Commands.ERROR, "Error");
        // GetMessage();
    }
}
