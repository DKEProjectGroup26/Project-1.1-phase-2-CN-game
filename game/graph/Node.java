package game.graph;

import game.Tools;

import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
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
    
    public Color color = baseColor;
    
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
        var from = new Point(border, (int) size.getWidth() - border);
        var to = new Point(border, (int) size.getHeight() - border);
        int radius = (int) ((to.x - from.x + to.y - from.y) / 2 * diameter);
       
        return clicked.distance(Tools.range(x, 0, 1, border, (int) size.getWidth() - border), Tools.range(y, 0, 1, border, (int) size.getHeight() - border)) <= radius;
    }
    
    public void draw(Graphics g, Dimension size, int border) {
        int width = (int) size.getWidth() - 2 * border;
        int height = (int) size.getHeight() - 2 * border;
        int average = (width + height) / 2;
        int intDiameter = (int) (average * diameter); // change to diameter
        g.setColor(style == DARK ? Tools.darkenColor(color) : color);
        
        g.fillOval(
            (int) (width * x - intDiameter / 2) + border,
            (int) (height * y - intDiameter / 2) + border,
            intDiameter,
            intDiameter
        );
        
        if (style == CIRCLE) {
            g.setColor(Color.WHITE);
            var g2D = (Graphics2D) g;
            g2D.setStroke(new BasicStroke(3));
            g.drawOval(
                (int) (width * x - intDiameter) + border,
                (int) (height * y - intDiameter) + border,
                intDiameter * 2,
                intDiameter * 2
            );
        }
        
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