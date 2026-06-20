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
        this.balance = initialBalance;
    }
     
    public void deposit(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Deposit amount cannot be negative");
        }
        balance += amount;
        System.out.println("Deposited: " + amount);
    }
     
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount < 0) {
            throw new IllegalArgumentException("Withdrawal amount cannot be negative");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds. Balance: " + balance);
        }
        balance -= amount;
        System.out.println("Withdrawn: " + amount);
    }
     
    public double getBalance() {
        return balance;
    }
}

// ==========================================
// 3. MAIN EXECUTION CLASS
// ==========================================
/**
 * Note: Agar aapki file ka naam 'BankTest.java' hai, 
 * toh is public class ka naam bhi 'BankTest' hona bilkul zaroori hai.
 */
public class Task01 {
    public static void main(String[] args) {
        System.out.println("=== Banking System Exception Test ===\n");
        
        BankAccount account = new BankAccount(1000);
        
        // Block 1: Normal operations and negative amount testing
        try {
            account.deposit(500);
            System.out.println("Balance: " + account.getBalance());
            
            account.withdraw(300);
            System.out.println("Balance: " + account.getBalance());
            
            System.out.println("\n[SIMULATION] Attempting negative deposit...");
            account.deposit(-100); // Yeh IllegalArgumentException throw karega
            
        } catch (IllegalArgumentException e) {
            System.out.println("Caught Expected Error: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        // Block 2: Insufficient funds testing
        try {
            System.out.println("\n[SIMULATION] Attempting to withdraw more than balance...");
            account.withdraw(2000); // Yeh InsufficientFundsException throw karega
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("Caught Expected Error: " + e.getMessage());
        }
        
        System.out.println("\nFinal Balance: " + account.getBalance());
    }
}