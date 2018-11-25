package game.visual;

import game.Tools;

import game.graph.GraphData;

import java.util.ArrayList;

import java.awt.*;

public class OldCircle {
    // x and y refer to the center of the circle (drawing is adjusted)
    private Point.Double point;
    
    public double diameter = 0.03;
    public int intDiameter;
    public Color color;
	
	public final static int NORMAL = 0;
	public final static int DARKER = 1;
	public final static int HALOED = 2;
	public final static int HOVERD = 3;
	
	public int drawStyle = NORMAL;
	
	private Line[] myLines;
	private Line[] otherLines;
	private Circle[] myCircles;
	private Circle[] otherCircles;
    
    public Circle(Point.Double p, int width, int height, Color cc) {
        point = p;
        intDiameter = (int) (((width + height) / 2) * diameter);
        
        color = cc;
    }
	
	public void makeConnections(int myIndex, GraphData data) {
        // improve this
        
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
		
		myCircles = lMUnique.toArray(new Circle[lMUnique.size()]);
		otherCircles = lOUnique.toArray(new Circle[lOUnique.size()]);
	}
    
    public void draw(Graphics g, Point from, Point upto) {
        int width = upto.x - from.x,
            height = upto.y - from.y;
        
        int average = (width + height) / 2;
        int intDiameter = (int) (average * diameter);
		int haloDiameter = intDiameter * 2;
        
		if (drawStyle == DARKER)
			g.setColor(Tools.darkenColor(color));
        else
			g.setColor(color);
		
        g.fillOval(
            (int) (width * point.x - intDiameter / 2) + from.x,
            (int) (height * point.y - intDiameter / 2) + from.y,
            intDiameter,
            intDiameter
        );
		
		if (drawStyle == HALOED) {
			g.setColor(Color.WHITE);
			var g2D = (Graphics2D) g;
		    g2D.setStroke(new BasicStroke(3));
	        g.drawOval(
	            (int) (width * point.x - haloDiameter / 2) + from.x,
	            (int) (height * point.y - haloDiameter / 2) + from.y,
	            haloDiameter,
	            haloDiameter
	        );
		}
    }
    
	public boolean wasMe(Point.Double p, Point from, Point upto) {
		return wasMe(p, from, upto, 0);
	}
    public boolean wasMe(Point.Double p, Point from, Point upto, double tolerance) {
        int width = upto.x - from.x,
            height = upto.y - from.y;
        
        var myPos = new Point(
            (int) (point.x * width) + from.x,
            (int) (point.y * height) + from.y
        );
        
		int myT = (int) (tolerance * (width + height) / 2);
        
        return myPos.distance(p) <= intDiameter / 2 + myT;
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
    
    public boolean canColor(Color c) {
        for (Circle circle : myCircles)
            if (Tools.sameColor(circle.color, c))
                return false;
        
        return true;
    }
    
    public Circle[] whoBlocks(Color c) {
        var blockers = new ArrayList<Circle>();
        
        for (Circle circle : myCircles)
            if (Tools.sameColor(circle.color, c))
                blockers.add(circle);
        
        return blockers.toArray(new Circle[blockers.size()]);
    }
    
    public void setColor(Color c) {
        color = c;
    }
    
    public void setColor(Color c, History history) {setColor(c, history, false);}
    public void setColor(Color c, History history, boolean cleared) {
        if (c != color) {
            history.add(new Tuple(this, color, c, cleared));
            color = c;
        }
    }
    
    public static void main(String[] args) {
        game.Main.main(null);
    }
}