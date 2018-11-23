package game.graph;

import java.util.ArrayList;

public class GraphData {
    public Node[] nodes;
    public Edge[] edges;
    
    public GraphData(int nNodes, int[][] edgesIn) {
        nodes = new Node[nNodes];
        edges = new Edge[edges.length];
        
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
            for (Edge edge : edges)
                if (edge.linked(node))
                    linkedEdges.add(edge);
            node.myEdges = linkedEdges.toArray(new Edge[linkedEdges.size()]);
            node.myNodes = new Node[node.myEdges.length];
            for (int i = 0; i < node.myEdges.length; i++) {
                var edge = node.myEdges[i];
                node.myNodes[i] = edge.a == node ? edge.b : edge.a;
            }
        }
    }
}