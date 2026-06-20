package HomeTask3;
abstract class Appliance {
    public abstract void turnOn();
    public abstract void turnOff();

    public void displayStatus(String status) {
        System.out.println("Status: " + status);
    }
}

class Fan extends Appliance {
    String speed;

    public Fan() {
        this.speed = "Off";
    }

    @Override
    public void turnOn() {
        speed = "High";
        displayStatus("Fan is ON at " + speed + " speed");
    }

    @Override
    public void turnOff() {
        speed = "Off";
        displayStatus("Fan is OFF");
    }
}

class WashingMachine extends Appliance {
    String cycleStatus;

    public WashingMachine() {
        this.cycleStatus = "Idle";
    }

    @Override
    public void turnOn() {
        cycleStatus = "Washing";
        displayStatus("Washing Machine started wash cycle");
    }

    @Override
    public void turnOff() {
        cycleStatus = "Stopped";
        displayStatus("Washing Machine stopped");
    }

    public void rinse() {
        cycleStatus = "Rinsing";
        displayStatus("Washing Machine is rinsing");
    }
}

public class Task3 {
    public static void main(String[] args) {
        Fan f = new Fan();
        WashingMachine w = new WashingMachine();
        
        f.turnOn();
        f.turnOff();
        System.out.println("--------------------");
        w.turnOn();
        w.rinse();
        w.turnOff();
    }
}



