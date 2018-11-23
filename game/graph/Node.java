package game.graph;

import game.Tools;

import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JCheckBox;

public class Node {
    public static final double diameter = 0.03;
    public static final Color baseColor = Color.WHITE;
    
    
    public Node[] myNodes;
    public Edge[] myEdges;
    
    public Node[] otherNodes;
    public Edge[] otherEdges;
    
    public Double x;
    public Double y;
    
    public Color color;
    
    public boolean linked(Node node) {
        for (Node maybe : myNodes) if (maybe == node) return true;
        return false;
    }
    
    public boolean isValid() {
        for (Node node : myNodes) if (color.equals(node.color)) return false;
        return true;
    }
    
    public void setColor(Color c) {color = c;}
    
    public boolean isMe(Point clicked, Dimension size, int border) {
        var from = new Point(border, (int) size.getWidth() - border * 2);
        var to = new Point(border, (int) size.getHeight() - border * 2);
        int radius = (int) ((to.x - from.x + to.y - from.y) / 2 * diameter);
       
        return clicked.distance(Tools.range(x, 0, 1, x, to.x), Tools.range(y, 0, 1, from.y, to.y)) <= radius;
    }
    
    public void draw(Graphics g) {
        
    }
    
    
    // styling
    public static final int NORMAL = 0;
    public static final int CIRCLE = 1;
    public static final int DARK = 2;
    
    public int style = NORMAL;
    
    private boolean highlighted;
    public void highlight(JCheckBox highContrast) {
        if (highlighted) return;
        highlighted = true;
        
        for (Edge edge : myEdges) edge.style = Edge.THICK;
        
        if (highContrast.isSelected()) {
            for (Edge edge : otherEdges) edge.style = Edge.DARK;
            for (Node node : myNodes) node.style = Node.CIRCLE;
            for (Node node : otherNodes) node.style = Node.DARK;
        }
    }
}