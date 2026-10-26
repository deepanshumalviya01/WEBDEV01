import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;



public class tcs3 {



   

    static StringTokenizer st;

    static BufferedReader br;



    static String nextToken() throws IOException {

        while (st == null || !st.hasMoreTokens()) {

            String line = br.readLine();

            if (line == null) return null;

            st = new StringTokenizer(line);

        }

        return st.nextToken();

    }



    public static void main(String[] args) throws Exception {

        br = new BufferedReader(new InputStreamReader(System.in));



        String nStr = nextToken();

        if (nStr == null) return;

        int n = Integer.parseInt(nStr);



        int[][] rect = new int[n][4];



        for (int i = 0; i < n; i++) {

            for (int j = 0; j < 4; j++) {

                rect[i][j] = Integer.parseInt(nextToken());

            }

        }



        int x1 = Integer.parseInt(nextToken());

        int y1 = Integer.parseInt(nextToken());

        int x2 = Integer.parseInt(nextToken());

        int y2 = Integer.parseInt(nextToken());



        ArrayList<Integer> validX = new ArrayList<>();

        ArrayList<Integer> validY = new ArrayList<>();



        for (int x = x1; x <= x2; x++) {

            boolean valid = true;



            for (int i = 0; i < n; i++) {

                if (rect[i][0] < x && x < rect[i][2]) {

                    valid = false;

                    break;

                }

            }



            if (valid) {

                validX.add(x);

            }

        }



        for (int y = y1; y <= y2; y++) {

            boolean valid = true;



            for (int i = 0; i < n; i++) {

                if (rect[i][1] < y && y < rect[i][3]) {

                    valid = false;

                    break;

                }

            }



            if (valid) {

                validY.add(y);

            }

        }



        long minWidth = Long.MAX_VALUE;

        long minHeight = Long.MAX_VALUE;



        for (int i = 1; i < validX.size(); i++) {

            minWidth = Math.min(minWidth, validX.get(i) - validX.get(i - 1));

        }



        for (int i = 1; i < validY.size(); i++) {

            minHeight = Math.min(minHeight, validY.get(i) - validY.get(i - 1));

        }



        // If no gap found, handle edge case safely

        if (minWidth == Long.MAX_VALUE) minWidth = 1;

        if (minHeight == Long.MAX_VALUE) minHeight = 1;



        System.out.println(minWidth * minHeight);

    }

}