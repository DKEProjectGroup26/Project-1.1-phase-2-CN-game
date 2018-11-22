package game.visual;

import game.Tools;

import java.awt.*;

public class Line {
    private Point.Double point0;
    private Point.Double point1;
    private double thickness = 0.002;
	private double highlightThickness = 0.005;
    public Color color;
	
	public final static int NORMAL = 10; // to avoid potential conflict with Circle
	public final static int DARKER = 11;
	public final static int THICKR = 12;
	
	public int drawStyle = NORMAL;
    
    public Line(Point.Double p0, Point.Double p1, Color col) {
        point0 = p0;
        point1 = p1;
        color = col;
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
            (int) (width * point0.x) + from.x,
            (int) (height * point0.y) + from.y,
            (int) (width * point1.x) + from.x,
            (int) (height * point1.y) + from.y
        );
    }
	
	public static void main(String[] args) {
		game.Main.main(null);
	}
}