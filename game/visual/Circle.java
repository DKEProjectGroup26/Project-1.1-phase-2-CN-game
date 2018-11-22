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
	
	public final static int NORMAL = 0;
	public final static int DARKER = 1;
	public final static int HALOED = 2;
	public final static int HOVERD = 3;
	
	public int drawStyle = NORMAL;
	
	private Line[] myLines;
	private Line[] otherLines;
	private Circle[] myCircles;
	private Circle[] otherCircles;
    
    public Circle(Point.Double xy, int w, int h, Color cc) {this(xy.x, xy.y, w, h, cc);}
    public Circle(Point.Double xy, double dd, int w, int h, Color cc) {this(xy.x, xy.y, dd, w, h, cc);}
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
		var lMyLines = new ArrayList<Line>();
		var lOtherLines = new ArrayList<Line>();
		
		for (int i = 0; i < data.edges.length; i++) {
			int a = data.edges[i].x,
			 	b = data.edges[i].y;
			
			if (a == myIndex || b == myIndex)
				if (!lMyLines.contains(data.lines[i]))
					lMyLines.add(data.lines[i]);
			else
				if (!lOtherLines.contains(data.lines[i]))
					lOtherLines.add(data.lines[i]);
		}
		
		myLines = new Line[lMyLines.size()];
		for (int i = 0; i < myLines.length; i++)
			myLines[i] = lMyLines.get(i);
		
		otherLines = new Line[data.lines.length - myLines.length];
		int index = 0;
		for (Line line : data.lines)
			if (!lMyLines.contains(line))
				otherLines[index++] = line;
		
		
		// circles
		var lMyCircles = new ArrayList<Circle>();
		var lOtherCircles = new ArrayList<Circle>();
		
		for (Point edge : data.edges) {
			int i = edge.x,
				j = edge.y;
			
			if (i == myIndex)
				// if (!lMyCircles.contains(data.circles[j]))
					lMyCircles.add(data.circles[j]);
			else if (j == myIndex)
				// if (!lMyCircles.contains(data.circles[i]))
					lMyCircles.add(data.circles[i]);
			
			if (i != myIndex && j != myIndex) {
				lOtherCircles.add(data.circles[i]);
				lOtherCircles.add(data.circles[j]);
			}
		}
		
		var lMUnique = new ArrayList<Circle>();
		var lOUnique = new ArrayList<Circle>();
		// for (Circle circle : lMyCircles)
			// if (!lMUnique.contains(circle))
				// lMUnique.add(circle);
				
		for (Circle circle : data.circles) {
			if (circle == this)
				continue;
			
			if (lMyCircles.contains(circle)) {
				if (!lMUnique.contains(circle))
					lMUnique.add(circle);
			} else {
				if (!lOUnique.contains(circle))
					lOUnique.add(circle);
			}
		}
		
		myCircles = new Circle[lMUnique.size()];
		for (int i = 0; i < myCircles.length; i++)
			myCircles[i] = lMUnique.get(i);
		
		otherCircles = new Circle[lOUnique.size()];
		for (int i = 0; i < otherCircles.length; i++)
			otherCircles[i] = lOUnique.get(i);
	}
	
	// public void makeConnections(int myIndex, GraphData data) {
	// 	// lines
	// 	var lMyLines = new ArrayList<Line>();
	// 	var lOtherLines = new ArrayList<Line>();
	//
	// 	for (int i = 0; i < data.edges.length; i++) {
	// 		// one line per edge
	// 		int a = data.edges[i][0] - 1,
	// 		 	b = data.edges[i][1] - 1;
	//
	// 		if (a == myIndex || b == myIndex)
	// 			lMyLines.add(data.lines[i]);
	// 		else
	// 			lOtherLines.add(data.lines[i]);
	// 	}
	//
	// 	myLines = new Line[lMyLines.size()];
	// 	for (int i = 0; i < myLines.length; i++)
	// 		myLines[i] = lMyLines.get(i);
	//
	// 	otherLines = new Line[lOtherLines.size()];
	// 	for (int i = 0; i < otherLines.length; i++)
	// 		otherLines[i] = lOtherLines.get(i);
	//
	// 	// circles
	// 	var lMyCircles = new ArrayList<Circle>();
	// 	var lOtherCircles = new ArrayList<Circle>();
	//
	// 	for (int[] edge : data.edges) {
	// 		// may be repeated
	// 		int i = edge[0] - 1,
	// 			j = edge[1] - 1;
	//
	// 		if (i == j) {
	// 			System.err.println("terrible");
	// 			System.exit(1);
	// 		}
	//
	// 		if (myIndex == i)
	// 			lMyCircles.add(data.circles[j]);
	// 		else if (myIndex == j)
	// 			lMyCircles.add(data.circles[j]);
	// 		else {
	// 			lOtherCircles.add(data.circles[i]);
	// 			lOtherCircles.add(data.circles[j]);
	// 		}
	// 	}
	//
	// 	var lMC2 = new ArrayList<Circle>();
	// 	for (Circle c : lMyCircles) {
	// 		if (!lMC2.contains(c))
	// 			lMC2.add(c);
	// 		else
	// 			System.out.println("here 1");
	// 	}
	//
	// 	var lOC2 = new ArrayList<Circle>();
	// 	for (Circle c : lOtherCircles) {
	// 		if (!lOC2.contains(c))
	// 			lOC2.add(c);
	// 		else
	// 			System.out.println("here 2");
	// 	}
	//
	// 	myCircles = new Circle[lMC2.size()];
	// 	for (int i = 0; i < myCircles.length; i++)
	// 		myCircles[i] = lMC2.get(i);
	//
	// 	otherCircles = new Circle[lOC2.size()];
	// 	for (int i = 0; i < otherCircles.length; i++)
	// 		otherCircles[i] = lOC2.get(i);
	// }
    
    public void draw(Graphics g, int fromX, int toX, int fromY, int toY) {
        int width = toX - fromX,
            height = toY - fromY;
        
        int average = (width + height) / 2;
        int intDiameter = (int) (average * diameter);
		int haloDiameter = intDiameter * 2;
        
		if (drawStyle == DARKER)
			g.setColor(Tools.darkenColor(color));
		else
			g.setColor(color);
		
        g.fillOval(
            (int) (width * x - intDiameter / 2) + fromX,
            (int) (height * y - intDiameter / 2) + fromY,
            intDiameter,
            intDiameter
        );
		
		if (drawStyle == HALOED) {
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
	
	public void highlight(boolean standout) {
		if (drawStyle == HOVERD)
			return;
		drawStyle = HOVERD;
		
		for (Line line : myLines)
			line.drawStyle = Line.THICKR;
		
		if (standout) {
			for (Line line : otherLines)
				line.drawStyle = Line.DARKER;
		
			for (Circle circle : myCircles)
				circle.drawStyle = HALOED;
		
			for (Circle circle : otherCircles)
				circle.drawStyle = Circle.DARKER;
		}
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