package game.graph;

import java.awt.Color;

public class Node {
    private static final double diameter = 0.03;
    private static final Color baseColor = Color.WHITE;
    
    
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
}