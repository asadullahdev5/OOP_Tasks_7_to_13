import java.util.ArrayList;
import java.util.List;

// ==========================================
// 1. INTERFACE DEFINITION
// ==========================================
/**
 * Book interface jo library ke basic operations ko define karti hai.
 */
interface Book {
    void borrow();
    void returnBook();
    String getTitle();
    boolean isAvailable();
}

// ==========================================
// 2. CONCRETE IMPLEMENTATIONS (CLASSES)
// ==========================================
/**
 * Fiction Book ki concrete implementation.
 */
class FictionBook implements Book {
    private String title;
    private String author;
    private boolean isAvailable;

    public FictionBook(String title, String author) {
        this.title = title;
        this.author = author;
        this.isAvailable = true; 
    }

    @Override
    public void borrow() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("[SUCCESS] Fiction Book '" + title + "' by " + author + " has been borrowed.");
        } else {
            System.out.println("[ERROR] Sorry, '" + title + "' is currently unavailable.");
        }
    }

    @Override
    public void returnBook() {
        if (!isAvailable) {
            isAvailable = true;
            System.out.println("[SUCCESS] Fiction Book '" + title + "' has been returned to the shelf.");
        } else {
            System.out.println("[INFO] '" + title + "' is already in the library.");
        }
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public boolean isAvailable() {
        return this.isAvailable;
    }
}

/**
 * Non-Fiction Book ki concrete implementation.
 */
class NonFictionBook implements Book {
    private String title;
    private String subject;
    private boolean isAvailable;

    public NonFictionBook(String title, String subject) {
        this.title = title;
        this.subject = subject;
        this.isAvailable = true;
    }

    @Override
    public void borrow() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("[SUCCESS] Non-Fiction Book '" + title + "' (Subject: " + subject + ") has been borrowed.");
        } else {
            System.out.println("[ERROR] Sorry, '" + title + "' is not available right now.");
        }
    }

    @Override
    public void returnBook() {
        if (!isAvailable) {
            isAvailable = true;
            System.out.println("[SUCCESS] Non-Fiction Book '" + title + "' has been safely returned.");
        } else {
            System.out.println("[INFO] '" + title + "' is already available.");
        }
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public boolean isAvailable() {
        return this.isAvailable;
    }
}

// ==========================================
// 3. MAIN SIMULATION CLASS
// ==========================================
/**
 * Main application class. 
 * Yaad rahe: File ka naam 'Task01.java' hai, isliye is main class ka naam bhi 'Task01' hona chahiye.
 */
public class Task01 {
    
    public static void main(String[] args) {
        System.out.println("=== Welcome to the Library Management System ===\n");

        // Polymorphism ka istemal karte hue list banana
        List<Book> libraryInventory = new ArrayList<>();
        
        libraryInventory.add(new FictionBook("The Hobbit", "J.R.R. Tolkien"));
        libraryInventory.add(new NonFictionBook("Sapiens", "Yuval Noah Harari"));
        libraryInventory.add(new FictionBook("1984", "George Orwell"));

        // User Interaction Simulation
        System.out.println("--- Phase 1: Users Borrowing Books ---");
        Book book1 = libraryInventory.get(0);
        book1.borrow();
        
        Book book2 = libraryInventory.get(1);
        book2.borrow();
        
        // Error handling test (dobara borrow karna)
        System.out.println("\n[SIMULATION] Attempting to borrow the same book again:");
        book1.borrow(); 

        System.out.println("\n--- Phase 2: Users Returning Books ---");
        book1.returnBook();

        System.out.println("\n--- Phase 3: Current Library Audit Report ---");
        for (Book b : libraryInventory) {
            String status = b.isAvailable() ? "Available" : "Borrowed";
            System.out.println("- " + b.getTitle() + " | Status: " + status);
        }
    }
}