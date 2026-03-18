import java.util.*;

public class Bai3 {
    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Cross product to determine orientation
    static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;  // collinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    // Function to find the convex hull
    static List<Point> convexHull(Point[] points, int n) {
        if (n < 3) return Arrays.asList(points);

        List<Point> hull = new ArrayList<>();

        // Find the leftmost point
        int l = 0;
        for (int i = 1; i < n; i++)
            if (points[i].x < points[l].x)
                l = i;

        int p = l, q;
        do {
            hull.add(points[p]);
            q = (p + 1) % n;
            for (int i = 0; i < n; i++) {
                if (orientation(points[p], points[i], points[q]) == 2)
                    q = i;
            }
            p = q;
        } while (p != l);

        return hull;
    }

    public static void main(String[] args) {
        Scanner scv = new Scanner(System.in);
        System.out.printf("Nhap so luong tram canh bao: ");
        int n = scv.nextInt();
        Point[] points = new Point[n];
        System.out.printf("Nhap toa do cac tram canh bao:\n");
        for (int i = 0; i < n; i++) {
            int x = scv.nextInt();
            int y = scv.nextInt();
            points[i] = new Point(x, y);
        }
        List<Point> hull = convexHull(points, n);
        System.out.printf("Cac tram canh bao trong bao quanh:\n");
        for (Point p : hull) {
            System.out.println(p.x + " " + p.y);
        }
    }
}