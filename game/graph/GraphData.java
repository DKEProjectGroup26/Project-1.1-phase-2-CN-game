package game.graph;

import game.Tools;
import game.visual.*;

import java.awt.*;

public class GraphData {
    public int nNodes;
    public Point[] edges;
    public Point.Double[] coords;
    public int[] colors; // depends on the colors of circles, only updated when used (white is -1 (uncolored))
    public Circle[] circles;
    public Line[] lines;
    
    public Integer displayWidth = null;
    public Integer displayHeight = null;
    // IMPORTANT: colors is unrelated to the color of each Circle in circles
    
    public GraphData() {this(0, null, null, null, null);}
    public GraphData(int n) {this(n, null, null, null, null);}
    public GraphData(int n, Point[] e) {this(n, e, null, null, null);}
    public GraphData(int n, Point[] e, Point.Double[] c) {this(n, e, c, null, null);}
    public GraphData(int n, Point[] e, Point.Double[] c, Circle[] r) {this(n, e, c, r, null);}
    public GraphData(int n, Point[] e, Point.Double[] c, Circle[] r, Line[] l) {
        nNodes = n;
        edges = e;
        coords = c;
        circles = r;
        lines = l;
    }
    
    public String attributes() {
        // for testing only
        String a = "nodes";
        if (edges != null)
            a += ", edges";
        if (coords != null)
            a += ", coords";
        if (colors != null)
            a += ", colors";
        if (circles != null)
            a += ", circles";
        if (lines != null)
            a += ", lines";
        return a;
    }
    
    public void updateColors() {
        if (circles == null) {
            colors = null;
            return;
        }
        
        resetColors();
        
        for (int i = 0; i < nNodes; i++) {
            var c = circles[i].color;
            if (Tools.isWhite(c))
                colors[i] = -1;
            else
                colors[i] = ColorPrecedence.numberOf(c);
        }
    }
    
    public void setDisplaySize(int w, int h) {
        displayWidth = w;
        displayHeight = h;
    }
    
    public GraphData shallowClone() {
        return new GraphData(nNodes, edges, coords, circles, lines);
    }
    
    public void resetEdges() {
        edges = new Point[edges.length];
    }
    
    public void resetCoords() {
        coords = new Point.Double[nNodes];
    }
    
    public void resetColors() {
        colors = new int[nNodes];
    }
    
    public void resetCircles() {
        circles = new Circle[nNodes];
    }
    
    public void resetLines() {
        lines = new Line[edges.length];
    }
    
    public void makeCoords() {
        coords = Positioner.getCoords(this);
        normalizeCoords();
    }
    
    public void makeLines() {
        if (edges == null || coords == null) {
            System.err.println("error: no edges and/or coordinates");
            System.exit(1);
        }
        
        resetLines();
        
        int i = 0;
        for (Point edge : edges)
            lines[i++] = new Line(coords[edge.x], coords[edge.y], Color.WHITE);
    }
	
    public void makeCircles() {
        if (coords == null) {
            System.err.println("error: no coordinates");
            System.exit(1);
        }
		
		if (lines == null) {
			System.err.println("error: can't make circles without lines");
			System.exit(1);
		}
        
        resetCircles();
        
        for (int i = 0; i < nNodes; i++) {
            circles[i] = new Circle(coords[i], displayWidth, displayHeight, Color.WHITE);
		}
		
		for (int i = 0; i < nNodes; i++)
			circles[i].makeConnections(i, this);
    }
    
    private void normalizeCoords() {
        if (coords == null) {
            System.err.println("error: this shouldn't have happened");
            System.exit(1);
        }
        
        // normalizes the coordinates so that the smallest x and y are 0 and the largest are 1
        double xMin = 1;
        double xMax = 0;
        double yMin = 1;
        double yMax = 0;
        
        for (Point.Double coord : coords) {
            double x = coord.x;
            double y = coord.y;
            if (x < xMin) xMin = x;
            if (x > xMax) xMax = x;
            if (y < yMin) yMin = y;
            if (y > yMax) yMax = y;
        }
        
        double xScale = 1 / (xMax - xMin);
        double yScale = 1 / (yMax - yMin);
        
        for (Point.Double coord : coords) {
            coord.x = (coord.x - xMin) * xScale;
            coord.y = (coord.y - yMin) * yScale;
        }
    }
}