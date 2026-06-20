import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// ==========================================
// MAIN EXECUTION CLASS (Scenario Unchanged)
// ==========================================
/**
 * Note: Kyunki public class ka naam FileReadTest hai,
 * isliye isay 'FileReadTest.java' naam ki file mein save kijiye ga.
 */
public class Task07 {
    public static void main(String[] args) {
        String fileName = "data.txt";
        System.out.println("=== Safe File Reader System (Try-With-Resources) ===\n");
        
        // try-with-resources: FileReader execution end hote hi automatically close ho jayega
        try (FileReader reader = new FileReader(fileName)) {
            int ch;
            System.out.println("File content:");
            
            // Loop tab tak chalega jab tak file ke saare characters read na ho jayein (-1 end of file batata hai)
            while ((ch = reader.read()) != -1) {
                System.out.print((char) ch);
            }
            
        } catch (FileNotFoundException e) {
            // Specific catch agar project folder mein data.txt na mile
            System.out.println("\nError: File not found - " + fileName);
            System.out.println("Please create 'data.txt' in your project directory.");
        } catch (IOException e) {
            // Generic catch agar data reading ke dauran stream corrupt ho jaye
            System.out.println("\nError: Problem reading file - " + e.getMessage());
        }
        
        // Try block se nikalte hi resources free ho chuke hain
        System.out.println("\n\nFile closed automatically");
    }
}