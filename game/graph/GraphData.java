package game.graph;

import game.visual.*;

import java.awt.*;

public class GraphData {
    public int nNodes;
    public int[][] edges = null;
    public double[][] coords = null;
    public Color[] colors = null;
    public Circle[] circles = null;
    public Line[] lines = null;
    
    public Integer displayWidth = null;
    public Integer displayHeight = null;
    // IMPORTANT: colors is unrelated to the color of each Circle in circles
    
    public GraphData() {this(0, null, null, null, null, null);}
    public GraphData(int n) {this(n, null, null, null, null, null);}
    public GraphData(int n, int[][] e) {this(n, e, null, null, null, null);}
    public GraphData(int n, int[][] e, double[][] c) {this(n, e, c, null, null, null);}
    public GraphData(int n, int[][] e, double[][] c, Color[] l) {this(n, e, c, l, null, null);}
    public GraphData(int n, int[][] e, double[][] c, Color[] l, Circle[] r) {this(n, e, c, l, r, null);}
    public GraphData(int n, int[][] e, double[][] c, Color[] l, Circle[] r, Line[] i) {
        nNodes = n;
        edges = e;
        coords = c;
        colors = l;
        circles = r;
        lines = i;
    }
    
    public void setDisplaySize(int w, int h) {
        displayWidth = w;
        displayHeight = h;
    }
    
    public GraphData shallowClone() {
        return new GraphData(nNodes, edges, coords, colors, circles, lines);
    }
    
    public GraphData deepClone() {
        int[][] newEdges;
        double[][] newCoords;
        Color[] newColors;
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
        
        if (colors == null)
            newColors = null;
        else {
            newColors = new Color[nNodes];
            for (int i = 0; i < nNodes; i++)
                newColors[i] = colors[i];
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
        
        return new GraphData(nNodes, newEdges, newCoords, newColors);
    }
    
    public void resetEdges() {
        edges = new int[edges.length][2];
    }
    
    public void resetCoords() {
        coords = new double[nNodes][2];
    }
    
    public void resetColors() {
        colors = new Color[nNodes];
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
    
    public void makeCircles() {
        if (coords == null) {
            System.err.println("error: no coordinates");
            System.exit(1);
        }
        
        resetCircles();
        
        for (int i = 0; i < nNodes; i++)
            circles[i] = new Circle(coords[i], displayWidth, displayHeight, Color.WHITE);
    }
    
    public void makeLines() {
        if (edges == null || coords == null) {
            System.err.println("error: no edges and/or coordinates");
            System.exit(1);
        }
        
        resetLines();
        
        int i = 0;
        for (int[] edge : edges)
            lines[i++] = new Line(coords[edge[0] - 1], coords[edge[1] - 1], Color.WHITE);
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