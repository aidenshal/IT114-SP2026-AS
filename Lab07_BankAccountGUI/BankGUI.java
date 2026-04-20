import javax.swing.*;
import java.awt.*;

public class BankGUI extends JFrame {
    private BankAccount account;
    private JLabel headerLabel;
    private JLabel balanceLabel;
    private JTextField amountField;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton exitButton;

    public BankGUI() {
        initializeAccount();
        buildGUI();
    }

    private void initializeAccount() {
        String name = JOptionPane.showInputDialog(null, "Please enter your name:");

        if (name == null || name.trim().isEmpty()) {
            name = "User";
        }

        double initialDeposit;
        while (true) {
            String depositInput = JOptionPane.showInputDialog(null, "Enter initial deposit:");

            if (depositInput == null) {
                System.exit(0);
            }

            try {
                initialDeposit = Double.parseDouble(depositInput);
                if (initialDeposit < 0) {
                    JOptionPane.showMessageDialog(null, "Error: Please enter a valid number");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Please enter a valid number");
            }
        }

        String[] options = {"Checking", "Savings"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose account type:",
                "Account Type",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            account = new CheckingAccount(name, initialDeposit);
        } else if (choice == 1) {
            account = new SavingsAccount(name, initialDeposit);
        } else {
            System.exit(0);
        }
    }

    private void buildGUI() {
        setTitle("Simple Bank Account");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1, 10, 10));

        headerLabel = new JLabel("Welcome, " + account.getAccountHolder() + "!", SwingConstants.CENTER);
        balanceLabel = new JLabel(
                String.format("Current Balance: $%.2f", account.getBalance()),
                SwingConstants.CENTER
        );

        amountField = new JTextField();

        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        exitButton = new JButton("Exit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(exitButton);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.add(headerLabel);
        mainPanel.add(balanceLabel);
        mainPanel.add(new JLabel("Enter Amount:", SwingConstants.CENTER));
        mainPanel.add(amountField);
        mainPanel.add(buttonPanel);

        add(mainPanel);

        depositButton.addActionListener(e -> handleDeposit());
        withdrawButton.addActionListener(e -> handleWithdraw());
        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void handleDeposit() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String message = account.deposit(amount);
            updateBalanceLabel();
            JOptionPane.showMessageDialog(this, message);
            amountField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Please enter a valid number");
        }
    }

    private void handleWithdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String message = account.withdraw(amount);
            updateBalanceLabel();
            JOptionPane.showMessageDialog(this, message);
            amountField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Please enter a valid number");
        }
    }

    private void updateBalanceLabel() {
        balanceLabel.setText(String.format("Current Balance: $%.2f", account.getBalance()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankGUI::new);
    }
}