package game.graph;

import game.Tools;
import game.visual.*;

import java.awt.*;

public class OldGraphData {
    public int nNodes;
    public Point[] edges;
    public Point.Double[] coords;
    public int[] colors; // depends on the colors of circles, only updated when used (white is -1 (uncolored))
    public Circle[] circles;
    public Line[] lines;
    
    public Integer displayWidth = null;
    public Integer displayHeight = null;
    // IMPORTANT: colors is unrelated to the color of each Circle in circles
    
    public OldGraphData() {this(0, null, null, null, null);}
    public OldGraphData(int n, Point[] e) {this(n, e, null, null, null);}
    public OldGraphData(int n, Point[] e, Point.Double[] c, Circle[] r, Line[] l) {
        nNodes = n;
        edges = e;
        coords = c;
        circles = r;
        lines = l;
    }
    
    public void updateColors() {
        if (circles == null) {
            colors = null;
            return;
        }
        
        colors = new int[nNodes];
        
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
    
    public void makeCoords() {
        coords = Positioner.getCoords(this);
        normalizeCoords();
    }
    
    public void makeLines() {
        if (edges == null || coords == null) {
            System.err.println("error: no edges and/or coordinates");
            System.exit(1);
        }
        
        lines = new Line[edges.length];
        
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
        
        circles = new Circle[nNodes];
        
        for (int i = 0; i < nNodes; i++)
            circles[i] = new Circle(coords[i], displayWidth, displayHeight, Color.WHITE);
		
		for (int i = 0; i < nNodes; i++)
			circles[i].makeConnections(i, this);
    }
    
    private void normalizeCoords() {
        if (coords == null) {
            System.err.println("error: this shouldn't have happened");
            System.exit(1);
        }
        
        // normalizes the coordinates so that the smallest x and y are 0 and the largest are 1
        var min = new Point.Double(1, 1);
        var max = new Point.Double(0, 0);
        
        for (Point.Double coord : coords) {
            if (coord.x < min.x) min.x = coord.x;
            if (coord.y < min.y) min.y = coord.y;
            if (coord.x > max.x) max.x = coord.x;
            if (coord.y > max.y) max.y = coord.y;
        }
        
        var scale = new Point.Double(1 / (max.x - min.x), 1 / (max.y - min.y));
        
        for (Point.Double coord : coords) {
            coord.x = (coord.x - min.x) * scale.x;
            coord.y = (coord.y - min.y) * scale.y;
        }
    }
    
    public boolean checkValidity() {
        updateColors();
        
        for (Point edge : edges) {
            var c0 = colors[edge.x];
            var c1 = colors[edge.y];
            
            if (c0 < 0 || c1 < 0)
                continue;
            
            if (c0 == c1)
                return false;
        }
        
        return true;
    }
    
    public static void main(String[] args) {
        game.Main.main(null);
    }
}