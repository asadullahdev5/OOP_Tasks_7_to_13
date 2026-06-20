import java.util.Scanner;

// ==========================================
// 1. CUSTOM EXCEPTIONS
// ==========================================
class InvalidCardException extends Exception {
    public InvalidCardException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class PaymentNetworkException extends Exception {
    public PaymentNetworkException(String message) {
        super(message);
    }
}

// ==========================================
// 2. INTERFACE
// ==========================================
interface PaymentMethod {
    void processPayment(double amount) throws InvalidCardException, InsufficientFundsException, PaymentNetworkException;
}

// ==========================================
// 3. CONCRETE PAYMENT CLASSES (Scenario Unchanged)
// ==========================================
class CreditCard implements PaymentMethod {
    private String cardNumber;
     
    public CreditCard(String cardNumber) {
        this.cardNumber = cardNumber;
    }
     
    @Override
    public void processPayment(double amount) throws InvalidCardException {
        if (cardNumber.length() != 16) {
            throw new InvalidCardException("Invalid card number. Must be 16 digits");
        }
        System.out.println("Credit Card payment of " + amount + " processed successfully");
    }
}

class PayPal implements PaymentMethod {
    private double balance;
     
    public PayPal(double balance) {
        this.balance = balance;
    }
     
    @Override
    public void processPayment(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient PayPal balance. Balance: " + balance);
        }
        balance -= amount;
        System.out.println("PayPal payment of " + amount + " processed successfully");
    }
}

class BankTransfer implements PaymentMethod {
    @Override
    public void processPayment(double amount) throws PaymentNetworkException {
        throw new PaymentNetworkException("Network error during bank transfer");
    }
}

// ==========================================
// 4. MAIN INTERACTIVE EXECUTION CLASS
// ==========================================
public class Task02 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Initializing the payment methods exactly as requested
        CreditCard creditCard = new CreditCard("1234567890123456");
        PayPal payPal = new PayPal(500);
        BankTransfer bankTransfer = new BankTransfer();
        
        while (true) {
            System.out.println("\nChoose payment method:");
            System.out.println("1. Credit Card");
            System.out.println("2. PayPal");
            System.out.println("3. Bank Transfer");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            
            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter 1, 2, 3 or 4");
                continue;
            }
            
            // Agar user Exit (4) select kare to seedha loop se bahar
            if (choice == 4) {
                break;
            }

            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please choose between 1 and 4.");
                continue;
            }
            
            System.out.print("Enter amount: ");
            double amount;
            try {
                amount = scanner.nextDouble();
                scanner.nextLine(); // Scanner buffer clear karne ke liye
            } catch (Exception e) {
                System.out.println("Invalid amount entered.");
                scanner.nextLine(); // Invalid input clear karne ke liye
                continue;
            }
            
            // Processing payments and handling specific exceptions natively
            try {
                if (choice == 1) {
                    creditCard.processPayment(amount);
                    break; // Success par execution complete
                } else if (choice == 2) {
                    payPal.processPayment(amount);
                    break; // Success par execution complete
                } else if (choice == 3) {
                    bankTransfer.processPayment(amount);
                    break;
                }
            } catch (InvalidCardException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.print("Enter new card number: ");
                String newCard = scanner.nextLine();
                creditCard = new CreditCard(newCard); // Card update ho gaya, system loop jari rakhega
            } catch (InsufficientFundsException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (PaymentNetworkException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        scanner.close();
        System.out.println("Payment process ended");
    }
}