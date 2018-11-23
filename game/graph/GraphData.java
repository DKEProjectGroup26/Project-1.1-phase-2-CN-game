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
            node.otherNodes = new Node[nodes.length - node.myNodes.length - 1];
            int index = 0;
            for (Node candidate : nodes)
                if (node != candidate && !node.linked(candidate)) node.otherNodes[index++] = candidate;
        }
    }
    
    public void makeCoords() {
        // var coords = Positioner.getCoords(this);
        for (Node node : nodes) {
            node.x = Math.random();
            node.y = Math.random();
        }
    }
    
    public boolean isValid() {
        for (Node node : nodes) if (!node.isValid()) return false;
        return true;
    }
    
    public Node whichNode(Point clicked, Dimension size, int border) {
        for (Node node : nodes)
            if (node.isMe(clicked, size, border)) return node;
        return null;
    }
    
    // no display size, calculate on demand
}