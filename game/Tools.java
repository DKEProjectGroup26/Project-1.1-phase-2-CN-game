package game;

import java.awt.Color;

public class Tools {
    public static double random(double from, double to) {
        // includes from, excludes to
        if (to < from) {
            System.err.println("error: invalid range");
            System.exit(1);
        }
        
        return Math.random() * (to - from) + from;
    }
    
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
    
    public static int rgb2int(Color color) {
        int r = color.getRed(),
            g = color.getGreen(),
            b = color.getBlue();
        
        return r * 65_536 + g * 256 + b;
    }
    
    public static Color int2rgb(int h) {
        int r = h / 65_536;
        h %= 65_536;
        int g = h / 256;
        h %= 256;
        
        return new Color(r, g, h);
    }
    
    public static Color invertColor(Color color) {
        return int2rgb(0xffffff - rgb2int(color));
    }
    
    public static boolean sameColor(Color a, Color b) {
        return a.getRed() == b.getRed() && a.getGreen() == b.getGreen() && a.getBlue() == b.getBlue();
    }
    
    public static boolean isWhite(Color color) {
        return sameColor(color, Color.WHITE);
    }
    
    public static void flip(Object[] array, int i, int j) {
        Object hold = array[i];
        array[i] = array[j];
        array[j] = hold;
    }
    
    public static boolean between(double[] p, double[] a, double[] b) {
        // tests if p is between a and b
        if (p[0] >= a[0] && p[0] >= b[0]) return false;
        if (p[0] <= a[0] && p[0] <= b[0]) return false;
        if (p[1] >= a[1] && p[1] >= b[1]) return false;
        if (p[1] <= a[1] && p[1] <= b[1]) return false;
        return true;
    }
    
    public static boolean onALine(double[] p0, double[] p1, double[] p2, double tolerance) {
        // tests if 3 points are on a line
        System.out.println(p0[0]+","+p0[1]);
        System.out.println(p1[0]+","+p1[1]);
        System.out.println(p2[0]+","+p2[1]);
        
        double s = (p0[1] - p1[1]) / (p0[0] - p1[0]);
        
        double d = Math.abs((p2[1] / s) - p2[0] + p0[0] - (p0[1] / s)) / Math.sqrt(1 + 1 / Math.pow(s, 2));
        System.out.println(d);
        return d <= tolerance;
    }
    
    public static void main(String[] args) {
        onALine(new double[]{0.01,0.02},new double[]{0.9,0.5},new double[]{0.02,0},0.1);
    }
}