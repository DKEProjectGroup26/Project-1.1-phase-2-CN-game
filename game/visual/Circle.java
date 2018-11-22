package game.visual;

import game.Tools;

import game.graph.GraphData;

import java.util.ArrayList;

import java.awt.*;

public class Circle {
    // x and y refer to the center of the circle (drawing is adjusted)
    public double x;
    public double y;
    
    public double diameter;
    public int intDiameter;
    public Color color;
    
    public static double defaultDiameter = 0.03;
	
	private boolean highlighted;
	private Line[] myLines;
	private Circle[] myCircles;
	
	public boolean drawHalo = false;
    
    public Circle(double[] xy, int w, int h, Color cc) {this(xy[0], xy[1], w, h, cc);}
    public Circle(double[] xy, double dd, int w, int h, Color cc) {this(xy[0], xy[1], dd, w, h, cc);}
    public Circle(double xx, double yy, int w, int h, Color cc) {this(xx, yy, defaultDiameter, w, h, cc);}
    public Circle(double xx, double yy, double dd, int width, int height, Color cc) {
        x = xx;
        y = yy;
        
        diameter = dd;
        intDiameter = (int) (((width + height) / 2) * diameter);
        
        color = cc;
    }
	
	public void makeConnections(int myIndex, GraphData data) {
		// lines
		var linesList = new ArrayList<Line>();
		
		for (int i = 0; i < data.edges.length; i++) {
			int a = data.edges[i][0] - 1,
			 	b = data.edges[i][1] - 1;
			
			if (a == myIndex || b == myIndex)
				linesList.add(data.lines[i]);
		}
		
		myLines = new Line[linesList.size()];
		for (int i = 0; i < myLines.length; i++)
			myLines[i] = linesList.get(i);
		
		
		// circles
		var circlesList = new ArrayList<Circle>();
		
		for (int[] edge : data.edges) {
			int i = edge[0] - 1,
				j = edge[1] - 1;
			
			if (i == j) {
				System.err.println("terrible");
				System.exit(1);
			}
			
			if (i == myIndex)
				circlesList.add(data.circles[j]);
			else if (j == myIndex)
				circlesList.add(data.circles[i]);
		}
		
		myCircles = new Circle[circlesList.size()];
		for (int i = 0; i < myCircles.length; i++)
			myCircles[i] = circlesList.get(i);
	}
    
    public void draw(Graphics g, int fromX, int toX, int fromY, int toY) {
        int width = toX - fromX,
            height = toY - fromY;
        
        int average = (width + height) / 2;
        int intDiameter = (int) (average * diameter);
		int haloDiameter = intDiameter * 2;
        
        g.setColor(color);
        g.fillOval(
            (int) (width * x - intDiameter / 2) + fromX,
            (int) (height * y - intDiameter / 2) + fromY,
            intDiameter,
            intDiameter
        );
		
		if (drawHalo) {
			g.setColor(Color.WHITE);
			var g2D = (Graphics2D) g;
		    g2D.setStroke(new BasicStroke(3));
	        g.drawOval(
	            (int) (width * x - haloDiameter / 2) + fromX,
	            (int) (height * y - haloDiameter / 2) + fromY,
	            haloDiameter,
	            haloDiameter
	        );
		}
    }
    
	public boolean wasMe(double a, double b, int c, int d, int e, int f) {
		return wasMe(a, b, c, d, e, f, 0);
	}
    public boolean wasMe(double xx, double yy, int fromX, int toX, int fromY, int toY, double tolerance) {
        int width = toX - fromX,
            height = toY - fromY;
        
        int myX = (int) (x * width) + fromX;
        int myY = (int) (y * height) + fromY;
		int myT = (int) (tolerance * (width + height) / 2);
        
        return Tools.euclidDist(xx, yy, myX, myY) <= intDiameter / 2 + myT;
    }
	
	public void highlight(GraphData data) {
		if (highlighted)
			return;
		
		highlighted = true;
		
		for (Line line : myLines)
			line.highlight();
		
		for (Circle circle : myCircles)
			circle.drawHalo = true;
	}
	
	public void unHighlight() {
		if (!highlighted)
			return;
		
		highlighted = false;
		
		for (Line line : myLines)
			line.unHighlight();
		
		for (Circle circle : myCircles)
			circle.drawHalo = false;
	}
    
    public void setColor(Color cc) {
        color = cc;
    }
    
    public void setColor(Color cc, History history) {setColor(cc, history, false);}
    public void setColor(Color cc, History history, boolean cleared) {
        if (cc != color) {
            history.add(new Tuple(this, color, cc, cleared));
            color = cc;
        }
    }
    
    public static void main(String[] args) {
        game.Main.main(null);
    }
}