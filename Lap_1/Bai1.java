import java.util.Scanner;

public class Bai1 {
    public static double areaByIntegration(double r, int intervals) {
    double area = 0;
    double width = r / intervals;
    for (int i = 0; i < intervals; i++) {
        double x = i * width;
        area += Math.sqrt(r * r - x * x) * width;
    }
    return area * 4;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap ban kich r: ");
        double r = sc.nextDouble();
        double acreage = areaByIntegration(r, 1000000);
        System.out.printf("Dien tich hinh tron sap xi với r=%.4f là %.6f\n", r, acreage);
        sc.close();
    }
}
