import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class tcs5 {
    
    static class Interval implements Comparable<Interval> {
        int start, end;
        
        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(Interval other) {
            if (this.start != other.start) return Integer.compare(this.start, other.start);
            return Integer.compare(this.end, other.end);
        }
    }

    static class Segment {
        int fixed;
        int start, end;
        
        public Segment(int fixed, int start, int end) {
            this.fixed = fixed;
            this.start = start;
            this.end = end;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) return;
        
        int n = Integer.parseInt(line.trim());
        
        Map<Integer, List<Interval>> horzLines = new HashMap<>();
        Map<Integer, List<Interval>> vertLines = new HashMap<>();
        
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());
            
            if (y1 == y2) {
                int minX = Math.min(x1, x2);
                int maxX = Math.max(x1, x2);
                horzLines.computeIfAbsent(y1, k -> new ArrayList<>()).add(new Interval(minX, maxX));
            } else if (x1 == x2) {
                int minY = Math.min(y1, y2);
                int maxY = Math.max(y1, y2);
                vertLines.computeIfAbsent(x1, k -> new ArrayList<>()).add(new Interval(minY, maxY));
            }
        }
        
        // Merge and flatten horizontal lines
        List<Segment> mergedHorz = new ArrayList<>();
        for (Map.Entry<Integer, List<Interval>> entry : horzLines.entrySet()) {
            int y = entry.getKey();
            List<Interval> merged = mergeIntervals(entry.getValue());
            for (Interval iv : merged) {
                mergedHorz.add(new Segment(y, iv.start, iv.end));
            }
        }
        
        // Merge vertical lines and store in a sorted map for fast X-coordinate lookup
        Map<Integer, List<Interval>> mergedVert = new TreeMap<>();
        for (Map.Entry<Integer, List<Interval>> entry : vertLines.entrySet()) {
            mergedVert.put(entry.getKey(), mergeIntervals(entry.getValue()));
        }
        
        // Sort horizontal segments from bottom to top (by Y coordinate)
        mergedHorz.sort(Comparator.comparingInt(a -> a.fixed));
        
        // Extract sorted X coordinates that actually contain vertical lines
        List<Integer> sortedX = new ArrayList<>(mergedVert.keySet());
        
        long rectangleCount = 0;
        
        // Iterate through all pairs of horizontal segments
        for (int i = 0; i < mergedHorz.size(); i++) {
            Segment h1 = mergedHorz.get(i);
            
            for (int j = i + 1; j < mergedHorz.size(); j++) {
                Segment h2 = mergedHorz.get(j);
                if (h1.fixed == h2.fixed) continue; // Must be distinct Y heights
                
                int overlapStart = Math.max(h1.start, h2.start);
                int overlapEnd = Math.min(h1.end, h2.end);
                
                if (overlapStart >= overlapEnd) continue; // No common horizontal span
                
                // Find index boundaries for valid X coordinates within the overlap
                int startIdx = Collections.binarySearch(sortedX, overlapStart);
                if (startIdx < 0) startIdx = -(startIdx + 1);
                
                int endIdx = Collections.binarySearch(sortedX, overlapEnd);
                if (endIdx < 0) endIdx = -(endIdx + 1) - 1;
                
                long validVerticals = 0;
                
                // Count how many vertical lines perfectly connect h1 and h2
                for (int k = startIdx; k <= endIdx; k++) {
                    int x = sortedX.get(k);
                    if (covers(mergedVert.get(x), h1.fixed, h2.fixed)) {
                        validVerticals++;
                    }
                }
                
                // Combinatorics: Any 2 valid vertical lines form exactly 1 rectangle
                rectangleCount += (validVerticals * (validVerticals - 1)) / 2;
            }
        }
        
        System.out.println(rectangleCount);
    }
    
    // Merges overlapping continuous intervals
    private static List<Interval> mergeIntervals(List<Interval> intervals) {
        if (intervals == null || intervals.isEmpty()) return new ArrayList<>();
        
        Collections.sort(intervals);
        List<Interval> merged = new ArrayList<>();
        Interval current = intervals.get(0);
        
        for (int i = 1; i < intervals.size(); i++) {
            Interval next = intervals.get(i);
            if (next.start <= current.end) {
                current.end = Math.max(current.end, next.end);
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        return merged;
    }
    
    // Checks if the required gap [reqStart, reqEnd] is fully covered by a vertical line
    private static boolean covers(List<Interval> mergedIntervals, int reqStart, int reqEnd) {
        for (Interval interval : mergedIntervals) {
            if (interval.start <= reqStart && interval.end >= reqEnd) {
                return true;
            }
            if (interval.start > reqStart) break; // Optimization: past the required start point
        }
        return false;
    }
}