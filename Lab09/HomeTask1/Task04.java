// ==========================================
// 1. CUSTOM EXCEPTION DEFINITION
// ==========================================
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

// ==========================================
// 2. BANK ACCOUNT CLASS
// ==========================================
class BankAccount {
    private double balance;
    
    public BankAccount(double initialBalance) {
        // FIXED BUG: Original code mein initialBalance ki jagah balance assign ho raha tha
        this.balance = initialBalance;
    }
    
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Withdrawal failed. Amount: " + amount + " > Balance: " + balance);
        }
        balance -= amount;
        System.out.println("Withdrawn: " + amount);
    }
    
    public double getBalance() {
        return balance;
    }
}

// ==========================================
// 3. MAIN EXECUTION CLASS (Scenario Unchanged)
// ==========================================
/**
 * Note: Kyunki class ka naam BankDemo hai, 
 * isliye isay 'BankDemo.java' naam ki file mein save kijiye ga.
 */
public class Task04 {
    public static void main(String[] args) {
        System.out.println("=== Bank Transaction Exception Demo ===\n");

        // Account with 1000 initial balance
        BankAccount account = new BankAccount(1000);
        
        System.out.println("Current Balance: " + account.getBalance());
        
        try {
            // Transaction 1: Successful withdrawal
            System.out.println("\n[SIMULATION] Withdrawing 300...");
            account.withdraw(300);
            System.out.println("Balance after withdrawal: " + account.getBalance());
            
            // Transaction 2: Shoud trigger InsufficientFundsException (800 > 700)
            System.out.println("\n[SIMULATION] Withdrawing 800...");
            account.withdraw(800);
            
        } catch (InsufficientFundsException e) {
            // Catching the custom exception exactly as designed
            System.out.println("Caught Expected Error: " + e.getMessage());
        }
        
        System.out.println("\nFinal Balance: " + account.getBalance());
    }
}