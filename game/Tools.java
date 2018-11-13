package game;

import java.awt.Color;

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
}