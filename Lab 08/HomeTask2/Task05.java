// ==========================================
// 1. INTERFACE DEFINITION
// ==========================================
interface Vehicle {
    void startEngine();
    void stopEngine();
}

// ==========================================
// 2. CONCRETE IMPLEMENTATIONS (CLASSES)
// ==========================================

// Car Class
class Car implements Vehicle {
    private String model;
     
    public Car(String model) {
        this.model = model;
    }
     
    @Override
    public void startEngine() {
        System.out.println(model + " car engine started");
    }
     
    @Override
    public void stopEngine() {
        System.out.println(model + " car engine stopped");
    }
}

// Bike Class
class Bike implements Vehicle {
    private String brand;
     
    public Bike(String brand) {
        this.brand = brand;
    }
     
    @Override
    public void startEngine() {
        System.out.println(brand + " bike engine started");
    }
     
    @Override
    public void stopEngine() {
        System.out.println(brand + " bike engine stopped");
    }
}

// ==========================================
// 3. MAIN EXECUTION CLASS
// ==========================================
/**
 * Note: Kyunki yeh Task05 hai, isliye file ka naam 'Task05.java' 
 * hona chahiye aur is main class ka naam bhi 'Task05'.
 */
public class Task05 {
    public static void main(String[] args) {
        System.out.println("=== Vehicle Management System Simulation ===\n");

        // Polymorphism ka istemal karte hue references banana
        Vehicle car = new Car("Civic");
        Vehicle bike = new Bike("Honda");
        
        // Car actions execution
        System.out.println("--- Testing Car ---");
        car.startEngine();
        car.stopEngine();
        
        System.out.println("\n------------------------------------\n");
        
        // Bike actions execution
        System.out.println("--- Testing Bike ---");
        bike.startEngine();
        bike.stopEngine();
    }
}