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
	
	public static Color darkenColor(Color color) {
		// return Color.BLACK; // CHANGE!!!
		return new Color(
			color.getRed() / 3,
			color.getGreen() / 3,
			color.getBlue() / 3
		);
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
        // if (p[0] >= a[0] && p[0] >= b[0]) return false;
        // if (p[0] <= a[0] && p[0] <= b[0]) return false;
        // if (p[1] >= a[1] && p[1] >= b[1]) return false;
        // if (p[1] <= a[1] && p[1] <= b[1]) return false;
		
		double t = 0; // tolarance
		if (Math.min(a[0], b[0]) - t <= p[0] && Math.max(a[0], b[0]) + t >= p[0] && Math.min(a[1], b[1]) - t <= p[1] && Math.max(a[1], b[1]) + t >= p[1])
			return true;
		
		return false;
    }
    
    public static boolean onALine(double[] p0, double[] p1, double[] p2, double tolerance) {
        // tests if 3 points are on a line
        
        double x0 = p0[0],
               y0 = p0[1],
               x1 = p1[0],
               y1 = p1[1],
               x2 = p2[0],
               y2 = p2[1];
        
        double d = Math.abs(x0*(y1 - y2) + x1*(y2 - y0) + x2*(y0 - y1)) / Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
        // System.out.println("d: " + d);
        return d <= tolerance;
    }
	
	private static double[] intersectionPoint(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
		double x = ((x0*y1 - y0*x1)*(x2-x3)-(x0-x1)*(x2*y3-y2*x3)) / ((x0 - x1)*(y2 - y3) - (y0 - y1)*(x2-x3));
		double y = ((x0*y1 - y0*x1)*(y2-y3)-(y0-y1)*(x2*y3-y2*x3)) / ((x0 - x1)*(y2 - y3) - (y0 - y1)*(x2-x3));
		
		if (!(Double.isFinite(x) && Double.isFinite(y)))
			return null;
		
		return new double[] {x, y};
	}
	private static double[] intersectionPoint(double[] p0, double[] p1, double[] p2, double[] p3) {
		return intersectionPoint(p0[0], p0[1], p1[0], p1[1], p2[0], p2[1], p3[0], p3[1]);
	}
	
	public static boolean edgesIntersect(double[] p0, double[] p1, double[] p2, double[] p3) {
		var point = intersectionPoint(p0, p1, p2, p3);
		
		if (point == null)
			return false;
		
		if (!between(point, p0, p1))
			return false;
		
		if (!between(point, p2, p3))
			return false;
		
		return true;
	}
    
    public static void main(String[] args) {
		System.out.println(edgesIntersect(new double[]{0,0},new double[]{0,1},new double[]{1,1},new double[]{1,0}));
    }
}