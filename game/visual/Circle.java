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
	
	public void makeLines(int myIndex, GraphData data) {
		var linesList = new ArrayList<Line>();
		
		for (int i = 0; i < data.edges.length; i++) {
			int a = data.edges[i][0] - 1;
			int b = data.edges[i][1] - 1;
			
			if (a == myIndex || b == myIndex)
				linesList.add(data.lines[i]);
		}
		
		myLines = new Line[linesList.size()];
		for (int i = 0; i < myLines.length; i++)
			myLines[i] = linesList.get(i);
	}
    
    public void draw(Graphics g, int fromX, int toX, int fromY, int toY) {
        int width = toX - fromX,
            height = toY - fromY;
        
        int average = (width + height) / 2;
        int intDiameter = (int) (average * diameter);
        
        g.setColor(color);
        g.fillOval(
            (int) (width * x - intDiameter / 2) + fromX,
            (int) (height * y - intDiameter / 2) + fromY,
            intDiameter,
            intDiameter
        );
    }
    
    public boolean wasMe(double xx, double yy, int fromX, int toX, int fromY, int toY) {
        int width = toX - fromX,
            height = toY - fromY;
        
        int myX = (int) (x * width) + fromX;
        int myY = (int) (y * height) + fromY;
        
        return Tools.euclidDist(xx, yy, myX, myY) <= intDiameter / 2;
    }
	
	public void highlight(GraphData data) {
		if (highlighted)
			return;
		
		highlighted = true;
		
		for (Line line : myLines)
			line.highlight();
	}
	
	public void unHighlight() {
		if (!highlighted)
			return;
		
		highlighted = false;
		
		for (Line line : myLines)
			line.unHighlight();
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