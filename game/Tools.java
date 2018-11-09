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
    
    public static double euclidDist(int x0, int y0, int x1, int y1) {
        return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
    }
    
    public static double euclidDist(int[] c0, int[] c1) {
        return euclidDist(c0[0], c0[1], c1[0], c1[1]);
    }
    
    public static int intEuclidDist(int x0, int y0, int x1, int y1) {
        return (int) euclidDist(x0, y0, x1, y1);
    }
    
    public static int intEuclidDist(int[] c0, int[] c1) {
        return (int) euclidDist(c0, c1);
    }
    
    public static void main(String[] args) {
        System.out.println(intEuclidDist(new int[]{1,2}, new int[]{1,1}));
    }
}