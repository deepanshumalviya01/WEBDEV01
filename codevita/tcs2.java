import java.util.Locale;
import java.util.Scanner;

public class tcs2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        if (!sc.hasNextInt()) return;
        
        int N = sc.nextInt();
        int[] x = new int[N];
        int[] y = new int[N];
        
        // Read coordinates
        for (int i = 0; i < N; i++) {
            x[i] = sc.nextInt();
            y[i] = sc.nextInt();
        }
        
        double perimeter = 0;
        double minLength = Double.MAX_VALUE;
        
        // Calculate the perimeter and find the minimum edge length
        for (int i = 0; i < N; i++) {
            int next = (i + 1) % N;
            double length = Math.abs(x[next] - x[i]) + Math.abs(y[next] - y[i]);
            perimeter += length;
            if (length < minLength) {
                minLength = length;
            }
        }
        
        // Calculate the initial Area using the Shoelace formula
        double area = 0;
        for (int i = 0; i < N; i++) {
            int next = (i + 1) % N;
            area += (double) x[i] * y[next] - (double) x[next] * y[i];
        }
        area = Math.abs(area) / 2.0;
        
        // Calculate the maximum possible H to keep the shortest edge at least 0.1
        // L - 2H >= 0.1 => H <= (L - 0.1) / 2
        double maxH = (minLength - 0.1) / 2.0;
        
        double maxVolume = 0.0;
        
        // Test all heights in increments of 0.1 up to the max bound
        // Added 1e-9 tolerance to safely avoid floating-point inaccuracies during limits check
        for (int h = 1; ; h++) {
            double H = h * 0.1;
            if (H > maxH + 1e-9) {
                break;
            }
            
            // Area_base = Area - P*H + 4*H^2
            double baseArea = area - (perimeter * H) + (4.0 * H * H);
            double volume = baseArea * H;
            
            if (volume > maxVolume) {
                maxVolume = volume;
            }
        }
        
        // Output max volume formatted to exactly 2 decimal places
        System.out.printf(Locale.US, "%.2f\n", maxVolume);
        
        sc.close();
    }
}