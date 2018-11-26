package game.graph;

import game.useful.Tools;
import game.graph.basic.BasicNode;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import javax.swing.JCheckBox;

public class Node extends BasicNode<Node, Edge> {
    public static final double diameter = 0.04;
    public static final Color baseColor = Color.WHITE;
    
    public Double x;
    public Double y;
    
    public Color color = baseColor;
    
    public Point.Double point() {
        return new Point.Double(x, y);
    }
    
    public Node[] blockers(Color newColor) {
        var blockers = new ArrayList<Node>();
        for (Node node : myNodes) {
            if (node.color.equals(baseColor)) continue; // ignore uncolored nodes
            if (newColor.equals(node.color)) blockers.add(node);
        }
        return blockers.isEmpty() ? null : blockers.toArray(new Node[blockers.size()]);
    }
    
    public double distance(Point.Double point) {
        return point.distance(x, y);
    }
    public double distance(double x0, double y0) {
        return distance(new Point.Double(x0, y0));
    }
    public double distance(Node node) {
        return distance(new Point.Double(node.x, node.y));
    }
    
    public boolean isMe(Point clicked, Dimension size, int border) {
        int width = (int) size.getWidth() - 2 * border;
        int height = (int) size.getHeight() - 2 * border;
        int average = (width + height) / 2;
        int intDiameter = (int) (average * diameter);
        
        int xPos = (int) (x * width) + border;
        int yPos = (int) (y * height) + border;
        return clicked.distance(xPos, yPos) <= intDiameter / 2;
    }
    
    public void draw(Graphics g, Dimension size, int border) {
        int width = (int) size.getWidth() - 2 * border;
        int height = (int) size.getHeight() - 2 * border;
        int average = (width + height) / 2;
        int intDiameter = (int) (average * diameter);
        g.setColor(style == FLASHING ? Color.WHITE : style == DARK ? Tools.darkenColor(color) : color);
        
        g.fillOval(
            (int) (width * x - intDiameter / 2) + border,
            (int) (height * y - intDiameter / 2) + border,
            intDiameter,
            intDiameter
        );
        
        if (style == CIRCLE || style == FLASHING /*maybe too flashy*/) {
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
        
        // PRINT FORCES AS ORANGE LINES (TESTING) ##################################
        // g.setColor(Color.ORANGE);
        // var g2 = (Graphics2D) g;
        // g2.setStroke(new BasicStroke(3));
        // g2.drawLine(
        //     (int) (width * x) + border,
        //     (int) (height * y) + border,
        //     (int) (width * (x + 500 * lastForce.x)) + border,
        //     (int) (height * (y + 500 * lastForce.y)) + border
        // );
        // END #####################################################################
    }
    
    
    // styling
    public static final int NORMAL = 0;
    public static final int CIRCLE = 1;
    public static final int DARK = 2;
    public static final int HIGHLIGHTED = 3;
    public static final int FLASHING = 4;
    
    public int style = NORMAL;
    
    public void highlight(JCheckBox highContrast) {
        if (style == HIGHLIGHTED) return;
        style = HIGHLIGHTED;
        
        for (Edge edge : myEdges) edge.style = Edge.THICK;
        
        if (highContrast.isSelected()) {
            for (Edge edge : otherEdges) edge.style = Edge.DARK;
            for (Node node : myNodes) node.style = Node.CIRCLE;
            for (Node node : otherNodes) node.style = Node.DARK;
        }
    }
    
    // TESTING ##########################
    public Point.Double lastForce = new Point.Double();
    // END ##############################
}