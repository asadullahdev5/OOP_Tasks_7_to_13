package Task3;
import java.time.LocalDate;

class Person {
    String name;
    String address;
    String phoneNumber;
    String email;

    public Person(String name, String address, String phoneNumber, String email) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}

abstract class Employee extends Person {
    double salary;
    LocalDate dateHired;

    public Employee(String name, String address, String phoneNumber, String email, double salary, LocalDate dateHired) {
        super(name, address, phoneNumber, email);
        this.salary = salary;
        this.dateHired = dateHired;
    }

    public abstract String getDetails();
}

class Faculty extends Employee {
    String rank;
    String department;

    public Faculty(String name, String address, String phoneNumber, String email, double salary, LocalDate dateHired, String rank, String department) {
        super(name, address, phoneNumber, email, salary, dateHired);
        this.rank = rank;
        this.department = department;
    }

    @Override
    public String getDetails() {
        return "Faculty | Name: " + name + " | Rank: " + rank + " | Dept: " + department + " | Salary: $" + salary + " | Hired: " + dateHired;
    }
}

class Staff extends Employee {
    String position;
    String office;

    public Staff(String name, String address, String phoneNumber, String email, double salary, LocalDate dateHired, String position, String office) {
        super(name, address, phoneNumber, email, salary, dateHired);
        this.position = position;
        this.office = office;
    }

    @Override
    public String getDetails() {
        return "Staff | Name: " + name + " | Position: " + position + " | Office: " + office + " | Salary: $" + salary + " | Hired: " + dateHired;
    }
}

public class Task03 {
    public static void main(String[] args) {
        Faculty f1 = new Faculty("Dr. Ayesha Khan", "Lahore", "03001234567", "ayesha@uni.edu", 150000, LocalDate.of(2020, 8, 15), "Professor", "Computer Science");
        Staff s1 = new Staff("Ali Raza", "Karachi", "03119876543", "ali@uni.edu", 70000, LocalDate.of(2022, 3, 1), "Admin Officer", "Room 205");
        
        System.out.println(f1.getDetails());
        System.out.println(s1.getDetails());
    }
}
