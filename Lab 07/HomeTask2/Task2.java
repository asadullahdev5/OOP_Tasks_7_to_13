package HomeTask2;

abstract class Employee {
    String name;
    String employeeID;

    public Employee(String name, String employeeID) {
        this.name = name;
        this.employeeID = employeeID;
    }

    public abstract double calculateSalary();
    public abstract String getRole();

    public void displayDetails() {
        System.out.println("Role: " + getRole());
        System.out.println("Name: " + name);
        System.out.println("Employee ID: " + employeeID);
        System.out.println("Salary: $" + calculateSalary());
    }
}

class Manager extends Employee {
    double baseSalary;
    double bonus;

    public Manager(String name, String employeeID, double baseSalary, double bonus) {
        super(name, employeeID);
        this.baseSalary = baseSalary;
        this.bonus = bonus;
    }

    @Override
    public double calculateSalary() {
        return baseSalary + bonus;
    }

    @Override
    public String getRole() {
        return "Manager";
    }
}

class Developer extends Employee {
    double hourlyRate;
    int hoursWorked;

    public Developer(String name, String employeeID, double hourlyRate, int hoursWorked) {
        super(name, employeeID);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public String getRole() {
        return "Developer";
    }
}

public class Task2{
    public static void main(String[] args) {
        Manager m = new Manager("Sara Ahmed", "M101", 120000, 25000);
        Developer d = new Developer("Usman Ali", "D205", 2000, 45);
        
        m.displayDetails();
        System.out.println("--------------------");
        d.displayDetails();
    }
}






