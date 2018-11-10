package game;

public class Tools {
    public static int randInt(int from, int to) {
        // inclusive
        if (to < from) {
            System.err.println("error: invalid range");
            System.exit(1);
        }
        
        return (int) (Math.random() * (to - from + 1) + from);
    }
    
    public static double euclidDist(double x0, double y0, double x1, double y1) {
        return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
    }
    
    public static double euclidDist(double[] c0, double[] c1) {
        return euclidDist(c0[0], c0[1], c1[0], c1[1]);
    }
    
    public static int intEuclidDist(int x0, int y0, int x1, int y1) {
        return (int) euclidDist(x0, y0, x1, y1);
    }
    
    public static int intEuclidDist(int[] c0, int[] c1) {
        double[] dc0 = {(double) c0[0], (double) c0[1]};
        double[] dc1 = {(double) c1[0], (double) c1[1]};
        return (int) euclidDist(dc0, dc1);
    }
    
    public static void main(String[] args) {
        System.out.println(euclidDist(512,74,514.0,83.0));
    }
}