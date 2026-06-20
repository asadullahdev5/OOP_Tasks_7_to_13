import java.util.Scanner;

// ==========================================
// MAIN EXECUTION CLASS (Scenario Unchanged)
// ==========================================
/**
 * Note: Kyunki class ka naam DivisionTest hai, 
 * isliye isay 'DivisionTest.java' naam ki file mein save kijiye ga.
 */
public class Task06 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Safe Integer Division System ===\n");
        
        System.out.print("Enter first number: ");
        String input1 = scanner.nextLine();
        
        System.out.print("Enter second number: ");
        String input2 = scanner.nextLine();
        
        try {
            // Parsing strings to integers natively
            int num1 = Integer.parseInt(input1);
            int num2 = Integer.parseInt(input2);
            
            // Core operation that can trigger ArithmeticException (if num2 is 0)
            int result = num1 / num2;
            System.out.println("Result: " + num1 + " / " + num2 + " = " + result);
            
        } catch (NumberFormatException e) {
            // Catches text/alpha inputs gracefully
            System.out.println("Error: Invalid input. Please enter only numbers.");
        } catch (ArithmeticException e) {
            // Catches division by zero
            System.out.println("Error: Division by zero is not allowed.");
        } catch (Exception e) {
            // Catches any unexpected general anomalies
            System.out.println("Error: Something went wrong - " + e.getMessage());
        } finally {
            // Resource cleanup block that always executes
            scanner.close();
            System.out.println("Program ended");
        }
    }
}