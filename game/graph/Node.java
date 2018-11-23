package game.graph;

import java.awt.Color;

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
    
    public void highlight(boolean highContrast) {
        System.err.println("IMPLEMENT HIGHLIGHTING");
    }
}