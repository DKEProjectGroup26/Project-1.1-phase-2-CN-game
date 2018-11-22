package game.visual;

import java.awt.*;

public class Line {
    public double x0;
    public double y0;
    public double x1;
    public double y1;
    public double thickness;
    public static double defaultThickness = 0.002;
	public static double highlightThickness = 0.005;
    public Color color;
    
    public Line(double[] xy0, double[] xy1, Color cc) {
        this(xy0[0], xy0[1], xy1[0], xy1[1], cc);
    }
    public Line(double[] xy0, double[] xy1, double tt, Color cc) {
        this(xy0[0], xy0[1], xy1[0], xy1[1], tt, cc);
    }
    public Line(double xx0, double yy0, double xx1, double yy1, Color cc) {
        this(xx0, yy0, xx1, yy1, defaultThickness, cc);
    }
    public Line(double xx0, double yy0, double xx1, double yy1, double tt, Color cc) {
        x0 = xx0;
        y0 = yy0;
        x1 = xx1;
        y1 = yy1;
        thickness = tt;
        color = cc;
    }
	
	public void highlight() {
		thickness = highlightThickness;
	}
	
	public void unHighlight() {
		thickness = defaultThickness;
	}
    
    public void draw(Graphics gg, int fromX, int toX, int fromY, int toY) {
        int width = toX - fromX,
            height = toY - fromY;
        
        var g = (Graphics2D) gg;
        
        g.setColor(color);
        
        int average = (width + height) / 2;
        g.setStroke(new BasicStroke((int) (average * thickness)));
        
        g.drawLine(
            (int) (width * x0) + fromX,
            (int) (height * y0) + fromY,
            (int) (width * x1) + fromX,
            (int) (height * y1) + fromY
        );
    }
}