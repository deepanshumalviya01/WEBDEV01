import java.util.*;

public class tcs6{
    static final double EPS = 1e-7;

    static class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        Point sub(Point p) {
            return new Point(x - p.x, y - p.y);
        }

        Point add(Point p) {
            return new Point(x + p.x, y + p.y);
        }

        Point mul(double k) {
            return new Point(x * k, y * k);
        }
    }

    static class Segment {
        Point a, b;

        Segment(Point a, Point b) {
            this.a = a;
            this.b = b;
        }
    }

    static class Cut {
        double t;
        Point p;

        Cut(double t, Point p) {
            this.t = t;
            this.p = p;
        }
    }

    static List<Point> nodes = new ArrayList<>();
    static List<List<Integer>> adj = new ArrayList<>();
    static List<Segment> segs = new ArrayList<>();
    static List<List<Cut>> cuts = new ArrayList<>();

    static double cross(Point a, Point b) {
        return a.x * b.y - a.y * b.x;
    }

    static double dot(Point a, Point b) {
        return a.x * b.x + a.y * b.y;
    }

    static double dist(Point a, Point b) {
        return Math.hypot(a.x - b.x, a.y - b.y);
    }

    static Point roundPoint(Point p) {
        return new Point(
            Math.round(p.x * 100.0) / 100.0,
            Math.round(p.y * 100.0) / 100.0
        );
    }

    static int getNode(Point p) {
        for (int i = 0; i < nodes.size(); i++) {
            if (dist(nodes.get(i), p) < EPS) {
                return i;
            }
        }

        nodes.add(p);
        adj.add(new ArrayList<Integer>());
        return nodes.size() - 1;
    }

    static void addCut(int i, Point p) {
        Segment s = segs.get(i);
        Point d = s.b.sub(s.a);
        double den = dot(d, d);
        double t = den < EPS ? 0 : dot(p.sub(s.a), d) / den;
        cuts.get(i).add(new Cut(t, p));
    }

    static boolean onSegment(Point p, Segment s) {
        return Math.abs(cross(p.sub(s.a), s.b.sub(s.a))) < EPS
            && dot(p.sub(s.a), p.sub(s.b)) <= EPS;
    }

    static void intersect(int i, int j) {
        Segment s1 = segs.get(i);
        Segment s2 = segs.get(j);

        Point p = s1.a;
        Point q = s2.a;
        Point r = s1.b.sub(s1.a);
        Point s = s2.b.sub(s2.a);

        double den = cross(r, s);

        if (Math.abs(den) < EPS) {
            Point[] p1 = {s1.a, s1.b};
            Point[] p2 = {s2.a, s2.b};

            for (Point z : p1) {
                if (onSegment(z, s2)) {
                    addCut(i, z);
                    addCut(j, z);
                }
            }

            for (Point z : p2) {
                if (onSegment(z, s1)) {
                    addCut(i, z);
                    addCut(j, z);
                }
            }
            return;
        }

        double t = cross(q.sub(p), s) / den;
        double u = cross(q.sub(p), r) / den;

        if (t >= -EPS && t <= 1 + EPS
                && u >= -EPS && u <= 1 + EPS) {
            Point z = roundPoint(p.add(r.mul(t)));
            addCut(i, z);
            addCut(j, z);
        }
    }

    static long edgeKey(int u, int v) {
        return ((long) u << 32) | (v & 0xffffffffL);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        double totalLength = 0;

        for (int i = 0; i < n; i++) {
            Point a = new Point(sc.nextDouble(), sc.nextDouble());
            Point b = new Point(sc.nextDouble(), sc.nextDouble());

            segs.add(new Segment(a, b));
            cuts.add(new ArrayList<Cut>());
            totalLength += dist(a, b);

            addCut(i, a);
            addCut(i, b);
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                intersect(i, j);
            }
        }

        for (int i = 0; i < n; i++) {
            List<Cut> list = cuts.get(i);
            list.sort(Comparator.comparingDouble(c -> c.t));

            List<Integer> ids = new ArrayList<>();

            for (Cut c : list) {
                int id = getNode(c.p);

                if (ids.isEmpty()
                        || ids.get(ids.size() - 1) != id) {
                    ids.add(id);
                }
            }

            for (int k = 1; k < ids.size(); k++) {
                int u = ids.get(k - 1);
                int v = ids.get(k);

                if (u != v) {
                    adj.get(u).add(v);
                    adj.get(v).add(u);
                }
            }
        }

        int V = nodes.size();
        List<List<Integer>> order = new ArrayList<>();

        for (int u = 0; u < V; u++) {
            final int node = u;
            List<Integer> list =
                new ArrayList<>(adj.get(node));

            list.sort((a, b) -> {
                Point p = nodes.get(node);
                Point pa = nodes.get(a);
                Point pb = nodes.get(b);

                double aa = Math.atan2(
                    pa.y - p.y, pa.x - p.x
                );

                double bb = Math.atan2(
                    pb.y - p.y, pb.x - p.x
                );

                return Double.compare(aa, bb);
            });

            order.add(list);
        }

        Set<Long> used = new HashSet<>();
        double bestArea = 0;
        double bestPerimeter = Double.POSITIVE_INFINITY;

        for (int u = 0; u < V; u++) {
            for (int v : order.get(u)) {
                if (used.contains(edgeKey(u, v))) {
                    continue;
                }

                List<Integer> poly = new ArrayList<>();
                int a = u;
                int b = v;
                double perimeter = 0;
                boolean closed = false;

                while (true) {
                    long key = edgeKey(a, b);

                    if (used.contains(key)) {
                        break;
                    }

                    used.add(key);
                    poly.add(a);

                    perimeter += dist(
                        nodes.get(a), nodes.get(b)
                    );

                    List<Integer> neighbours = order.get(b);
                    int pos = neighbours.indexOf(a);

                    if (pos < 0 || neighbours.isEmpty()) {
                        break;
                    }

                    int next = neighbours.get(
                        (pos - 1 + neighbours.size())
                            % neighbours.size()
                    );

                    a = b;
                    b = next;

                    if (a == u && b == v) {
                        closed = true;
                        break;
                    }
                }

                if (!closed || poly.size() < 3) {
                    continue;
                }

                double twiceArea = 0;

                for (int i = 0; i < poly.size(); i++) {
                    Point p = nodes.get(poly.get(i));
                    Point q = nodes.get(
                        poly.get((i + 1) % poly.size())
                    );

                    twiceArea += cross(p, q);
                }

                double area = Math.abs(twiceArea) / 2.0;

                if (area > bestArea + EPS
                        || (Math.abs(area - bestArea) <= EPS
                        && area > EPS
                        && perimeter < bestPerimeter)) {
                    bestArea = area;
                    bestPerimeter = perimeter;
                }
            }
        }

        if (bestArea < EPS) {
            System.out.println("Abandoned");
            return;
        }

        double leftover = Math.max(
            0, totalLength - bestPerimeter
        );

        double computerArea =
            leftover * leftover / (4.0 * Math.PI);

        if (bestArea >= computerArea - EPS) {
            System.out.println("Kalyan");
        } else {
            System.out.println("Computer");
        }
    }
}