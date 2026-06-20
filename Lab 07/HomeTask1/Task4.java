package HomeTask1;

abstract class Shape {
    public abstract double calculateArea();

    public void displayArea(double area) {
        System.out.println("Area: " + area);
    }
}

class Circle extends Shape {
    double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    double length;
    double breadth;

    public Rectangle(double length, double breadth) {
        this.length = length;
        this.breadth = breadth;
    }

    @Override
    public double calculateArea() {
        return length * breadth;
    }
}

public class Task4 {
    public static void main(String[] args) {
        Circle c = new Circle(7.0);
        Rectangle r = new Rectangle(10.0, 5.0);

        double circleArea = c.calculateArea();
        System.out.print("Circle - ");
        c.displayArea(circleArea);

        double rectArea = r.calculateArea();
        System.out.print("Rectangle - ");
        r.displayArea(rectArea);
    }
}
