package game.graph;

public class Edge {
    Node node0;
    Node node1;
    
    public Edge(int[] edge, Node[] nodes) {
        node0 = nodes[edge[0]];
        node1 = nodes[edge[1]];
    }
}