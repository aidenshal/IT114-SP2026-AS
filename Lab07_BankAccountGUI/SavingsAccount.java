public class SavingsAccount extends BankAccount {
    // DO NOT CHANGE ANY VARIABLE, METHOD, OR CLASS NAMES. THE AUTOGRADER DEPENDS ON THEM.
    public SavingsAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
    }

    @Override
    public String withdraw(double amount) {
        if (Double.isNaN(amount) || amount <= 0) {
            return "Please enter a valid number";
        }

        if (amount > getBalance()) {
            return String.format("Transaction Denied: Insufficient funds. Balance remains: $%.2f", getBalance());
        }

        setBalance(getBalance() - amount);
        return String.format("Withdrawal successful. New Balance: $%.2f", getBalance());
    }
}