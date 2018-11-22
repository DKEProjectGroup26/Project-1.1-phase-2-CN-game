package game.graph;

import game.Tools;
import game.visual.*;

import java.awt.*;

public class GraphData {
    public int nNodes;
    public int[][] edges;
    public double[][] coords;
    public int[] colors; // depends on the colors of circles, only updated when used (white is -1 (uncolored))
    public Circle[] circles;
    public Line[] lines;
    
    public Integer displayWidth = null;
    public Integer displayHeight = null;
    // IMPORTANT: colors is unrelated to the color of each Circle in circles
    
    public GraphData() {this(0, null, null, null, null);}
    public GraphData(int n) {this(n, null, null, null, null);}
    public GraphData(int n, int[][] e) {this(n, e, null, null, null);}
    public GraphData(int n, int[][] e, double[][] c) {this(n, e, c, null, null);}
    public GraphData(int n, int[][] e, double[][] c, Circle[] r) {this(n, e, c, r, null);}
    public GraphData(int n, int[][] e, double[][] c, Circle[] r, Line[] l) {
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
    
    public GraphData deepClone() {
        int[][] newEdges;
        double[][] newCoords;
        Circle[] newCircles;
        Line[] newLines;
        
        if (edges == null)
            newEdges = null;
        else {
            newEdges = new int[edges.length][2];
            for (int i = 0; i < edges.length; i++)
                newEdges[i] = new int[] {edges[i][0], edges[i][1]};
        }
        
        if (coords == null)
            newCoords = null;
        else {
            newCoords = new double[nNodes][2];
            for (int i = 0; i < nNodes; i++)
                newCoords[i] = new double[] {coords[i][0], coords[i][1]};
        }
        
        if (circles == null)
            newCircles = null;
        else {
            newCircles = new Circle[nNodes];
            for (int i = 0; i < nNodes; i++) {
                var c = circles[i];
                newCircles[i] = new Circle(c.x, c.y, c.diameter, displayWidth, displayHeight, c.color);
            }
        }
        
        if (lines == null)
            newLines = null;
        else {
            newLines = new Line[edges.length];
            for (int i = 0; i < edges.length; i++) {
                var l = lines[i];
                newLines[i] = new Line(l.x0, l.y0, l.x1, l.y1, l.thickness, l.color);
            }
        }
        
        return new GraphData(nNodes, newEdges, newCoords);
    }
    
    public void resetEdges() {
        edges = new int[edges.length][2];
    }
    
    public void resetCoords() {
        coords = new double[nNodes][2];
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
        for (int[] edge : edges)
            lines[i++] = new Line(coords[edge[0]], coords[edge[1]], Color.WHITE);
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
        
        for (double[] coord : coords) {
            double x = coord[0];
            double y = coord[1];
            if (x < xMin) xMin = x;
            if (x > xMax) xMax = x;
            if (y < yMin) yMin = y;
            if (y > yMax) yMax = y;
        }
        
        double xScale = 1 / (xMax - xMin);
        double yScale = 1 / (yMax - yMin);
        
        for (double[] coord : coords) {
            coord[0] = (coord[0] - xMin) * xScale;
            coord[1] = (coord[1] - yMin) * yScale;
        }
    }
}