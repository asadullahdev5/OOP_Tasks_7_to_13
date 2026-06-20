package Task1;
abstract class Account {
    protected String id;
    protected double balance;

    public Account(String id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getID() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountInfo() {
        return "Account ID: " + id + " | Balance: $" + balance;
    }

    public abstract boolean withdraw(double amount);
    public abstract void deposit(double amount);
}

class SavingsAccount extends Account {
    private static final double MIN_BALANCE = 10.0;
    private static final double TRANSACTION_FEE = 2.0;

    public SavingsAccount(String id, double initialDeposit) {
        super(id, initialDeposit);
        if (initialDeposit < MIN_BALANCE) {
            throw new IllegalArgumentException("Initial deposit must be at least $" + MIN_BALANCE);
        }
    }

    @Override
    public boolean withdraw(double amount) {
        double totalDeduction = amount + TRANSACTION_FEE;
        if (amount <= 0) {
            System.out.println("Error: Withdrawal amount must be positive.");
            return false;
        }
        if (balance - totalDeduction >= MIN_BALANCE) {
            balance -= totalDeduction;
            System.out.println("Success: Withdrawn $" + amount + " + Fee $" + TRANSACTION_FEE);
            return true;
        } else {
            System.out.println("Failed: Balance cannot drop below $" + MIN_BALANCE);
            return false;
        }
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Success: Deposited $" + amount);
        } else {
            System.out.println("Error: Deposit amount must be positive.");
        }
    }
}

public class Task01 {
    public static void main(String[] args) {
        try {
            // 1. Account create with $100 initial deposit
                SavingsAccount acc = new SavingsAccount("SA1001", 100.0);

                acc.deposit(50);

                acc.withdraw(30);

                acc.withdraw(110);

                acc.withdraw(20);

                System.out.println("----------------------");

            // 2. Deposit test
                acc.deposit(50);
                System.out.println(acc.getAccountInfo());
                System.out.println("----------------------");

            // 3. Valid withdrawal test: $30 + $2 fee
                acc.withdraw(30);
                System.out.println(acc.getAccountInfo());
                System.out.println("----------------------");

            // 4. Invalid withdrawal test: balance $10 se kam ho jaye ga
                acc.withdraw(110);
                System.out.println(acc.getAccountInfo());
                System.out.println("----------------------");

            // 5. Final withdrawal
                acc.withdraw(20);
                System.out.println("Final Status: " + acc.getAccountInfo());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
