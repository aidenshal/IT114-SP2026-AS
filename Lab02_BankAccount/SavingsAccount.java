public class SavingsAccount extends BankAccount {
    // DO NOT CHANGE ANY VARIABLE, METHOD, OR CLASS NAMES. THE AUTOGRADER DEPENDS ON THEM.
    public SavingsAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            System.out.printf("New Balance: $%.2f%n", getBalance());
            return;
        }

        if (amount > getBalance()) {
            System.out.println("Transaction Denied: Insufficient funds");
        } else {
            setBalance(getBalance() - amount);
        }

        System.out.printf("New Balance: $%.2f%n", getBalance());
    }
}
