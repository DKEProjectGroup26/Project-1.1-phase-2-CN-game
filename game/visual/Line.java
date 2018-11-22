package game.visual;

import game.Tools;

import java.awt.*;

public class Line {
    public double x0;
    public double y0;
    public double x1;
    public double y1;
    public double thickness;
    public final static double defaultThickness = 0.002;
	public final static double highlightThickness = 0.005;
    public Color color;
	
	public final static int NORMAL = 10; // to avoid potential conflict with Circle
	public final static int DARKER = 11;
	public final static int THICKR = 12;
	
	public int drawStyle = NORMAL;
    
    public Line(Point.Double xy0, Point.Double xy1, Color cc) {
        this(xy0.x, xy0.y, xy1.x, xy1.y, cc);
    }
    public Line(Point.Double xy0, Point.Double xy1, double tt, Color cc) {
        this(xy0.x, xy0.y, xy1.x, xy1.y, tt, cc);
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
    
    public void draw(Graphics gg, Point from, Point upto) {
        int width = upto.x - from.x,
            height = upto.y - from.y;
        
        var g = (Graphics2D) gg;
        
		if (drawStyle == DARKER)
			g.setColor(Tools.darkenColor(color));
		else
			g.setColor(color);
        
		double localThickness = drawStyle == THICKR ? highlightThickness : thickness;
		
        int average = (width + height) / 2;
        g.setStroke(new BasicStroke((int) (average * localThickness)));
        
        g.drawLine(
            (int) (width * x0) + from.x,
            (int) (height * y0) + from.y,
            (int) (width * x1) + from.x,
            (int) (height * y1) + from.y
        );
    }
	
	public static void main(String[] args) {
		game.Main.main(null);
	}
}