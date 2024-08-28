/**
 * Commands enum
 * Used to represent the commands that can be sent from the server to the client
 */
public enum Commands {
    WELCOME,
    ASK_ACCOUNT_NUMBER,
    ACCOUNT_NUMBER,
    ASK_PIN,
    PIN,
    WITHDRAW,
    DEPOSIT,
    GET_BALANCE, 
    ERROR,
    EXIT
}
