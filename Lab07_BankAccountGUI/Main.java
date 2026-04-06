import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialization
        System.out.print("Please enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter initial deposit amount: ");
        double initialDeposit = scanner.nextDouble();

        System.out.println("Choose Account:");
        System.out.println("1. Checking:");
        System.out.println("2. Savings:");
        System.out.print("Choice: ");
        int accountChoice = scanner.nextInt();

        BankAccount account;
        if (accountChoice == 1) {
            account = new CheckingAccount(name, initialDeposit);
        } else {
            account = new SavingsAccount(name, initialDeposit);
        }

        // Menu loop
        int choice = 0;
        while (choice != 4) {
            System.out.println("--- MENU ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Balance");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Amount to deposit: ");
                    double dep = scanner.nextDouble();
                    account.deposit(dep);
                    break;

                case 2:
                    System.out.print("Amount to withdraw: ");
                    double wd = scanner.nextDouble();
                    account.withdraw(wd);
                    break;

                case 3:
                    System.out.printf("Current Balance: $%.2f%n", account.getBalance());
                    break;

                case 4:
                    System.out.println("Goodbye.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}
