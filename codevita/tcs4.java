import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class tcs4 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) return;
        
        StringTokenizer st = new StringTokenizer(line);
        int N = Integer.parseInt(st.nextToken());
        
        // 3D array to keep track of slide segments starting from (x,y)
        // [x][y][direction]
        // Directions: 0 = Down-Left, 1 = Down-Right, 2 = Up-Left, 3 = Up-Right
        int MAX_COORD = 505; // Safe padding for coordinates
        boolean[][][] hasSeg = new boolean[MAX_COORD][MAX_COORD][4];
        
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());
            
            int length = Math.abs(x2 - x1);
            int dx = (x2 - x1) > 0 ? 1 : -1;
            int dy = (y2 - y1) > 0 ? 1 : -1;
            
            // Map out every single 1-unit segment of the slide
            for (int j = 0; j < length; j++) {
                int cx = x1 + j * dx;
                int cy = y1 + j * dy;
                int nx = cx + dx;
                int ny = cy + dy;
                
                if (dx == -1 && dy == -1) {
                    hasSeg[cx][cy][0] = true; // Down-Left
                    hasSeg[nx][ny][3] = true; // Up-Right (reverse)
                } else if (dx == 1 && dy == -1) {
                    hasSeg[cx][cy][1] = true; // Down-Right
                    hasSeg[nx][ny][2] = true; // Up-Left (reverse)
                } else if (dx == -1 && dy == 1) {
                    hasSeg[cx][cy][2] = true; // Up-Left
                    hasSeg[nx][ny][1] = true; // Down-Right (reverse)
                } else if (dx == 1 && dy == 1) {
                    hasSeg[cx][cy][3] = true; // Up-Right
                    hasSeg[nx][ny][0] = true; // Down-Left (reverse)
                }
            }
        }
        
        st = new StringTokenizer(br.readLine());
        int cx = Integer.parseInt(st.nextToken());
        int cy = Integer.parseInt(st.nextToken());
        int energy = Integer.parseInt(st.nextToken());
        
        // Step-by-step unit simulation
        while (cy > 0) {
            boolean downLeft = hasSeg[cx][cy][0];
            boolean downRight = hasSeg[cx][cy][1];
            boolean upLeft = hasSeg[cx][cy][2];
            boolean upRight = hasSeg[cx][cy][3];
            
            if (downLeft) {
                // Ball wants to slide Down-Left. 
                // It's blocked if another slide forms a wall going Up-Left.
                if (upLeft) {
                    int unlockCost = cx * cy;
                    if (energy >= unlockCost) {
                        energy -= unlockCost; // Unlock and pass through!
                    } else {
                        break; // Permanently stuck
                    }
                }
                
                if (energy >= 1) {
                    energy -= 1;
                    cx -= 1;
                    cy -= 1;
                } else {
                    break; // Out of energy mid-slide
                }
                
            } else if (downRight) {
                // Ball wants to slide Down-Right. 
                // It's blocked if another slide forms a wall going Up-Right.
                if (upRight) {
                    int unlockCost = cx * cy;
                    if (energy >= unlockCost) {
                        energy -= unlockCost; // Unlock and pass through!
                    } else {
                        break; // Permanently stuck
                    }
                }
                
                if (energy >= 1) {
                    energy -= 1;
                    cx += 1;
                    cy -= 1;
                } else {
                    break; // Out of energy mid-slide
                }
                
            } else {
                // Free fall (no slides directly below going down)
                cy -= 1;
            }
        }
        
        // Output final position
        System.out.println(cx + " " + cy);
    }
}