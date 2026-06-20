// ==========================================
// 1. INTERFACE DEFINITION
// ==========================================
interface SmartDevice {
    void turnOn();
    void turnOff();
}

// ==========================================
// 2. CONCRETE IMPLEMENTATIONS (CLASSES)
// ==========================================

// Light Class
class Light implements SmartDevice {
    private String location;

    public Light(String location) {
        this.location = location;
    }

    @Override
    public void turnOn() {
        System.out.println("Light in " + location + " is ON");
    }

    @Override
    public void turnOff() {
        System.out.println("Light in " + location + " is OFF");
    }
}

// Thermostat Class
class Thermostat implements SmartDevice {
    private int temperature;

    public Thermostat(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public void turnOn() {
        System.out.println("Thermostat ON. Set to " + temperature + "°C");
    }

    @Override
    public void turnOff() {
        System.out.println("Thermostat OFF");
    }
}

// SecurityCamera Class
class SecurityCamera implements SmartDevice {
    private String position;

    public SecurityCamera(String position) {
        this.position = position;
    }

    @Override
    public void turnOn() {
        System.out.println("Security Camera at " + position + " started recording");
    }

    @Override
    public void turnOff() {
        System.out.println("Security Camera at " + position + " stopped recording");
    }
}

// ==========================================
// 3. MAIN EXECUTION CLASS
// ==========================================
/**
 * Note: Agar aapki file ka naam 'Task02.java' hai, 
 * toh is public class ka naam bhi 'Task02' hona zaroori hai.
 */
public class Task02 {
    public static void main(String[] args) {
        System.out.println("=== Smart Home Automation Simulation ===\n");

        // Polymorphism ka istemal karte hue objects banana
        SmartDevice light = new Light("Living Room");
        SmartDevice thermostat = new Thermostat(24);
        SmartDevice camera = new SecurityCamera("Main Gate");

        // Turning devices ON
        light.turnOn();
        thermostat.turnOn();
        camera.turnOn();

        System.out.println("\n------------------------------------\n");

        // Turning devices OFF
        light.turnOff();
        thermostat.turnOff();
        camera.turnOff();
    }
}


