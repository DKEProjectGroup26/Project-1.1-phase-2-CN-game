package game.graph;

import game.Tools;

import java.util.ArrayList;
import java.awt.Point;
import java.awt.Dimension;

public class GraphData {
    public Node[] nodes;
    public Edge[] edges;
    
    public GraphData(int nNodes, int[][] edgesIn) {
        nodes = new Node[nNodes];
        edges = new Edge[edgesIn.length];
        
        for (int i = 0; i < nNodes; i++)
            nodes[i] = new Node();
        
        for (int i = 0; i < edgesIn.length; i++) {
            var edgeObject = new Edge();
            edgeObject.a = nodes[edgesIn[i][0]];
            edgeObject.b = nodes[edgesIn[i][1]];
            edges[i] = edgeObject;
        }
        
        for (Node node : nodes) {
            var linkedEdges = new ArrayList<Edge>();
            var unlinkedEdges = new ArrayList<Edge>();
            for (Edge edge : edges) {
                if (edge.linked(node)) linkedEdges.add(edge);
                else unlinkedEdges.add(edge);
            }
            node.myEdges = linkedEdges.toArray(new Edge[linkedEdges.size()]);
            node.otherEdges = unlinkedEdges.toArray(new Edge[unlinkedEdges.size()]);
            node.myNodes = new Node[node.myEdges.length];
            for (int i = 0; i < node.myEdges.length; i++) {
                var edge = node.myEdges[i];
                node.myNodes[i] = edge.a == node ? edge.b : edge.a;
            }
            node.otherNodes = new Node[nodes.length - node.myNodes.length];
            int index = 0;
            for (Node candidate : nodes)
                if (!node.linked(candidate)) node.otherNodes[index++] = candidate;
        }
    }
    
    public void makeCoords() {
        // make coords
    }
    
    public boolean isValid() {
        for (Node node : nodes) if (!node.isValid()) return false;
        return true;
    }
    
    public Node whichNode(Point clicked, Dimension size, int border) {
        var from = new Point(border, (int) size.getWidth() - border * 2);
        var to = new Point(border, (int) size.getHeight() - border * 2);
        int radius = (int) ((to.x - from.x + to.y - from.y) / 2 * Node.diameter);
        
        for (Node node : nodes)
            if (clicked.distance(Tools.range(node.x, 0, 1, from.x, to.x), Tools.range(node.y, 0, 1, from.y, to.y)) <= radius)
                return node;
        return null;
    }
    
    // no display size, calculate on demand
}