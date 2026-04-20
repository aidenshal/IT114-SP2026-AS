public abstract class BankAccount {
    // DO NOT CHANGE ANY VARIABLE, METHOD, OR CLASS NAMES. THE AUTOGRADER DEPENDS ON THEM.
    private String accountHolder;
    private double balance;

    public BankAccount(String accountHolder, double initialDeposit) {
        this.accountHolder = accountHolder;
        this.balance = initialDeposit;
    }

    public String deposit(double amount) {
        if (amount <= 0) {
            return "Please enter a valid number";
        }

        balance += amount;
        return String.format("Deposit successful. New Balance: $%.2f", balance);
    }

    /**
     * Abstract method: Child classes must implement this.
     */
    public abstract String withdraw(double amount);

    // --- GETTERS AND SETTERS ---

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }
}