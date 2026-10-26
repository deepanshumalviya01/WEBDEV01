import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Tcs {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // Read dimensions M and N
        StringTokenizer st = new StringTokenizer(br.readLine());
        int M = Integer.parseInt(st.nextToken());
        int N = Integer.parseInt(st.nextToken());

        // Read the initial box configuration
        char[][] grid = new char[M][N];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                grid[i][j] = st.nextToken().charAt(0);
            }
        }

        // Read the number of instructions
        st = new StringTokenizer(br.readLine());
        int K = Integer.parseInt(st.nextToken());

        // Apply initial gravity before any rotations
        applyGravity(grid);

        // Process each rotation instruction
        for (int i = 0; i < K; i++) {
            String instruction = br.readLine().trim();
            if (instruction.equals("right")) {
                grid = rotateRight(grid);
            } else if (instruction.equals("left")) {
                grid = rotateLeft(grid);
            }
            
            // Gravity takes effect immediately after each rotation
            applyGravity(grid);
        }

        // Output the final position of the box
        for (int i = 0; i < grid.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < grid[0].length; j++) {
                sb.append(grid[i][j]);
                if (j < grid[0].length - 1) {
                    sb.append(" ");
                }
            }
            System.out.println(sb.toString());
        }
    }

    // Pushes all stones to the bottom of their respective columns
    static void applyGravity(char[][] grid) {
        int R = grid.length;
        int C = grid[0].length;
        
        for (int c = 0; c < C; c++) {
            int stones = 0;
            // Count stones in the current column
            for (int r = 0; r < R; r++) {
                if (grid[r][c] == '*') {
                    stones++;
                }
            }
            // Reposition stones at the bottom
            for (int r = 0; r < R; r++) {
                if (r >= R - stones) {
                    grid[r][c] = '*';
                } else {
                    grid[r][c] = '.';
                }
            }
        }
    }

    // Rotates the matrix 90 degrees clockwise
    static char[][] rotateRight(char[][] grid) {
        int R = grid.length;
        int C = grid[0].length;
        char[][] newGrid = new char[C][R]; // Dimensions swap
        
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                newGrid[c][R - 1 - r] = grid[r][c];
            }
        }
        return newGrid;
    }

    // Rotates the matrix 90 degrees counter-clockwise
    static char[][] rotateLeft(char[][] grid) {
        int R = grid.length;
        int C = grid[0].length;
        char[][] newGrid = new char[C][R]; // Dimensions swap
        
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                newGrid[C - 1 - c][r] = grid[r][c];
            }
        }
        return newGrid;
    }
}