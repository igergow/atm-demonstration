public class Account {
    private String accountNumber;
    private String pin;
    private double balance;

    public Account(String accountNumber, String pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
    }

    /**
     * Getter for the account number
     * @return The account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Method to check if the pin is correct
     * @param pin The pin to check
     * @return True if the pin is correct, false otherwise
     */
    public boolean checkPin(String pin) {
        return this.pin.equals(pin);
    }

    /**
     * Getter for the balance
     * @return The balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Method to deposit money into the account balance (cannot be negative)
     * @param amount The amount to deposit
     * @throws IllegalArgumentException If the amount is negative
     */
    public void deposit(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        balance += amount;
    }

    /**
     * Method to withdraw money from the account balance (cannot be negative or greater than the balance)
     * @param amount The amount to withdraw
     * @throws IllegalArgumentException If the amount is negative or greater than the balance
     */
    public void withdraw(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        if (amount > balance) throw new IllegalArgumentException("Amount cannot be greater than balance");
        balance -= amount;
    }

    /**
     * Overridden toString method
     * @return The string representation of the account
     */
    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", pin='" + pin + '\'' +
                ", balance=" + balance +
                '}';
    }
}
