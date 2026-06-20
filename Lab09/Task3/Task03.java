// ==========================================
// 1. INTERFACES DEFINITIONS (Fixed Missing Codes)
// ==========================================
interface Drivable {
    void drive();
    void stop();
}

interface Loadable {
    void loadCargo(int weight);
    void unloadCargo();
}

// ==========================================
// 2. CONCRETE VEHICLE CLASSES (Scenario Intact)
// ==========================================

// Car Class (Implements Drivable)
class Car implements Drivable {
    private String model;
     
    public Car(String model) {
        this.model = model;
    }
     
    @Override
    public void drive() {
        System.out.println("Car " + model + " is driving on road");
    }
     
    @Override
    public void stop() {
        System.out.println("Car " + model + " stopped");
    }
}

// Bike Class (Implements Drivable)
class Bike implements Drivable {
    private String brand;
     
    public Bike(String brand) {
        this.brand = brand;
    }
     
    @Override
    public void drive() {
        System.out.println("Bike " + brand + " is riding fast");
    }
     
    @Override
    public void stop() {
        System.out.println("Bike " + brand + " stopped");
    }
}

// Truck Class (Implements both Drivable and Loadable)
class Truck implements Drivable, Loadable {
    private String type;
    private int cargoWeight;
     
    public Truck(String type) {
        this.type = type;
        this.cargoWeight = 0;
    }
     
    @Override
    public void drive() {
        System.out.println("Truck " + type + " is hauling cargo");
    }
     
    @Override
    public void stop() {
        System.out.println("Truck " + type + " stopped");
    }
     
    @Override
    public void loadCargo(int weight) {
        cargoWeight += weight;
        System.out.println("Loaded " + weight + " kg cargo. Total: " + cargoWeight + " kg");
    }
     
    @Override
    public void unloadCargo() {
        System.out.println("Unloaded " + cargoWeight + " kg cargo");
        cargoWeight = 0;
    }
}

// ==========================================
// 3. MAIN EXECUTION CLASS
// ==========================================
public class Task03 {
    public static void main(String[] args) {
        System.out.println("=== Transport Management System Simulation ===\n");

        // Objects creation as per the exact original scenario
        Car car = new Car("Toyota");
        Bike bike = new Bike("Honda");
        Truck truck = new Truck("Heavy Duty");
        
        // Testing Car Actions
        car.drive();
        car.stop();
        
        System.out.println();
        
        // Testing Bike Actions
        bike.drive();
        bike.stop();
        
        System.out.println();
        
        // Testing Truck Actions (Drivable + Loadable interface interactions)
        truck.drive();
        truck.loadCargo(500);
        truck.loadCargo(300);
        truck.stop();
        truck.unloadCargo();
    }
}