// ==========================================
// 1. SHAPES SECTION (Circle Class)
// ==========================================
class Circle {
    private double radius;
     
    public Circle(double radius) {
        this.radius = radius;
    }
     
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

// ==========================================
// 2. OPERATIONS SECTION (Calculator Class)
// ==========================================
class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
     
    public int subtract(int a, int b) {
        return a - b;
    }
}

// ==========================================
// 3. MAIN EXECUTION CLASS (PackageTest/Task04)
// ==========================================
/**
 * Note: Agar aapki file ka naam 'Task04.java' hai, 
 * toh is public class ka naam bhi 'Task04' hona zaroori hai.
 */
public class Task04 {
    public static void main(String[] args) {
        System.out.println("=== Package & Class Test Simulation ===\n");

        // Circle class ka istemal
        Circle circle = new Circle(5.0);
        System.out.print("Circle Area (Radius 5.0): ");
        System.out.println(circle.calculateArea());
        
        System.out.println("------------------------------------");

        // Calculator class ka istemal (Direct object creation)
        Calculator calc = new Calculator();
        System.out.print("Calculator Add (10 + 20): ");
        System.out.println(calc.add(10, 20));
        
        System.out.print("Calculator Subtract (50 - 30): ");
        System.out.println(calc.subtract(50, 30));
    }
}