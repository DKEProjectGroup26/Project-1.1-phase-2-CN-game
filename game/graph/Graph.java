package game.graph;

import java.util.ArrayList;

public class Graph {
    Node[] nodes;
    Edge[] edges;
    Integer nColors = null;
    
    public Graph(int nNodes, int[][] ees) {
        nodes = new Node[nNodes];
        
        for (int i = 0; i < nNodes; i++)
            nodes[i] = new Node(i);
        
        for (Node node : nodes)
            node.findLinks(ees, nodes);
        
        edges = new Edge[ees.length];
        
        for (int i = 0; i < ees.length; i++)
            edges[i] = new Edge(ees[i], nodes);
    }
    
    public Graph(GraphData data) {
        this(data.nNodes, data.edges);
    }
    
    public void giveColors(int ccs) {
        nColors = ccs;
        for (Node node : nodes)
            node.giveColors(ccs);
    }
    
    public boolean solved() {
        for (Node node : nodes)
            if (!node.hasColor())
                return false;
        
        for (Edge edge : edges) // might be useless
            if (edge.node0.color == edge.node1.color)
                return false;
        
        return true;
    }
    
    public Graph solve() {
        return null;
    }
    
    private Graph subSolve() {
        if (solved())
            return this;
        
        var paths = new ArrayList<Graph>();
        
        for (Node node : nodes) {
            if (node.hasColor())
                continue;
            
            for (int color = 0; color < nColors; color++) {
                
            }
        }
        
        return null;
    }
    
    public static void main(String[] args) {
        var g = new Graph(Reader.readGraph(4));
        g.giveColors(4);
    }
}