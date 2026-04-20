public class CheckingAccount extends BankAccount {
    // DO NOT CHANGE ANY VARIABLE, METHOD, OR CLASS NAMES. THE AUTOGRADER DEPENDS ON THEM.
    private final double OVERDRAFT_FEE = 35.00;

    public CheckingAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
    }

    @Override
    public String withdraw(double amount) {
        if (Double.isNaN(amount) || amount <= 0) {
            return "Please enter a valid number";
        }

        double newBalance = getBalance() - amount;

        if (amount > getBalance()) {
            newBalance -= OVERDRAFT_FEE;
            setBalance(newBalance);
            return String.format("Overdraft! $35.00 fee applied. New Balance: $%.2f", getBalance());
        }

        setBalance(newBalance);
        return String.format("Withdrawal successful. New Balance: $%.2f", getBalance());
    }
}