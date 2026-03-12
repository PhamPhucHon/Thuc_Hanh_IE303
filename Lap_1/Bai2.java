public class Bai2 {
    public static void main(String[] args) {
        int n = 1000000;
        double area = 0;
        double width = 1.0 / n;
        for (int i = 0; i < n; i++) {
            double x = i * width;
            area += Math.sqrt(1 - x * x) * width;
        }
        double pi = 4 * area;
        System.out.printf("Xap xi gia tri cua pi la %.8f\n", pi);
    }
}
