import java.util.*;

public class Bai3 {
    static class Point {
        int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
        public String toString() { return x + " " + y; }
    }

    // cross product (p->q) x (p->r)
    static long cross(Point p, Point q, Point r) {
        return (long)(q.x - p.x) * (r.y - p.y) - (long)(q.y - p.y) * (r.x - p.x);
    }

    public static List<Point> convexHull(List<Point> pts) {
        int n = pts.size();
        if (n <= 1) return new ArrayList<>(pts);
        // find leftmost lowest
        Point start = pts.get(0);
        for (Point p : pts) {
            if (p.x < start.x || (p.x == start.x && p.y < start.y)) {
                start = p;
            }
        }
        final Point s = start;
        // sort by polar angle (counterclockwise) around start; if collinear, farthest first
        pts.sort((a, b) -> {
            long c = cross(s, a, b);
            if (c == 0) {
                long da = (long)(a.x - s.x) * (a.x - s.x) + (long)(a.y - s.y) * (a.y - s.y);
                long db = (long)(b.x - s.x) * (b.x - s.x) + (long)(b.y - s.y) * (b.y - s.y);
                return Long.compare(db, da);
            }
            return -Long.compare(c, 0); // sort clockwise so hull later will be clockwise
        });
        
        Deque<Point> hull = new ArrayDeque<>();
        for (Point p : pts) {
            while (hull.size() >= 2) {
                Point q = hull.removeLast();
                Point r = hull.peekLast();
                if (cross(r, q, p) < 0) { // right turn (clockwise), we keep
                    hull.addLast(q);
                    break;
                }
            }
            hull.addLast(p);
        }
        // rotate hull to start at the leftmost-lowest
        List<Point> res = new ArrayList<>(hull);
        int idx = 0;
        for (int i = 0; i < res.size(); i++) {
            Point p = res.get(i);
            if (p.x == s.x && p.y == s.y) { idx = i; break; }
        }
        Collections.rotate(res, -idx);
        return res;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Point> pts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            pts.add(new Point(x, y));
        }
        List<Point> hull = convexHull(pts);
        for (Point p : hull) {
            System.out.println(p.x + " " + p.y);
        }
        sc.close();
    }
}
