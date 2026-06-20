package Task2;
final class ShippingBox {
    double width;
    double height;
    double depth;

    public ShippingBox(double width, double height, double depth) {
        this.width = width;
    this.height = height;
        this.depth = depth;
    }

    double calculateVolume() {
        return width * height * depth;
    }
}

public class Task02 {
    public static void main(String[] args) {
        ShippingBox box = new ShippingBox(12.5, 8.0, 5.0);
        double volume = box.calculateVolume();
        System.out.println("Box Dimensions: " + box.width + " x " + box.height + " x " + box.depth);
        System.out.println("Volume: " + volume + " cubic units");
    }
}


