package game;

import java.awt.Color;
import java.awt.Point;

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
    
    public static double euclidDist(Point.Double c0, Point.Double c1) {
        return euclidDist(c0.x, c0.y, c1.x, c1.y);
    }
    
    public static int intEuclidDist(int x0, int y0, int x1, int y1) {
        return (int) euclidDist(x0, y0, x1, y1);
    }
    
    public static int intEuclidDist(Point c0, Point c1) {
        var dc0 = new Point.Double((double) c0.x, (double) c0.y);
        var dc1 = new Point.Double((double) c1.x, (double) c1.y);
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
    
    public static boolean between(Point.Double p, Point.Double a, Point.Double b) {
        // tests if p is between a and b
        // if (p[0] >= a[0] && p[0] >= b[0]) return false;
        // if (p[0] <= a[0] && p[0] <= b[0]) return false;
        // if (p[1] >= a[1] && p[1] >= b[1]) return false;
        // if (p[1] <= a[1] && p[1] <= b[1]) return false;
		
		double t = 0; // tolarance
		if (Math.min(a.x, b.x) - t <= p.x && Math.max(a.x, b.x) + t >= p.x && Math.min(a.y, b.y) - t <= p.y && Math.max(a.y, b.y) + t >= p.y)
			return true;
		
		return false;
    }
    
    public static boolean onALine(Point.Double p0, Point.Double p1, Point.Double p2, double tolerance) {
        // tests if 3 points are on a line
        
        double x0 = p0.x,
               y0 = p0.y,
               x1 = p1.x,
               y1 = p1.y,
               x2 = p2.x,
               y2 = p2.y;
        
        double d = Math.abs(x0*(y1 - y2) + x1*(y2 - y0) + x2*(y0 - y1)) / Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
        // System.out.println("d: " + d);
        return d <= tolerance;
    }
	
	private static Point.Double intersectionPoint(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
		double x = ((x0*y1 - y0*x1)*(x2-x3)-(x0-x1)*(x2*y3-y2*x3)) / ((x0 - x1)*(y2 - y3) - (y0 - y1)*(x2-x3));
		double y = ((x0*y1 - y0*x1)*(y2-y3)-(y0-y1)*(x2*y3-y2*x3)) / ((x0 - x1)*(y2 - y3) - (y0 - y1)*(x2-x3));
		
		if (!(Double.isFinite(x) && Double.isFinite(y)))
			return null;
		
		return new Point.Double(x, y);
	}
	private static Point.Double intersectionPoint(Point.Double p0, Point.Double p1, Point.Double p2, Point.Double p3) {
		return intersectionPoint(p0.x, p0.y, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
	}
	
	public static boolean edgesIntersect(Point.Double p0, Point.Double p1, Point.Double p2, Point.Double p3) {
		var point = intersectionPoint(p0, p1, p2, p3);
		
		if (point == null)
			return false;
		
		if (!between(point, p0, p1))
			return false;
		
		if (!between(point, p2, p3))
			return false;
		
		return true;
	}
}