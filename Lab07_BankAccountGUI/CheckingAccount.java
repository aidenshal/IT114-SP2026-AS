public class CheckingAccount extends BankAccount {
    // DO NOT CHANGE ANY VARIABLE, METHOD, OR CLASS NAMES. THE AUTOGRADER DEPENDS ON THEM.
    private final double OVERDRAFT_FEE = 35.00;

    public CheckingAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            System.out.printf("New Balance: $%.2f%n", getBalance());
            return;
        }

        double newBalance = getBalance() - amount;

        if (newBalance < 0) {
            newBalance -= OVERDRAFT_FEE;
            System.out.println("Overdraft! $35.00 fee");
        }

        setBalance(newBalance);
        System.out.printf("New Balance: $%.2f%n", getBalance());
    }
}
