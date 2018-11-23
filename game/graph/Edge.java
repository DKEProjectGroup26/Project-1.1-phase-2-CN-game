package game.graph;

public class Edge {
    public Node a;
    public Node b;
    
    public boolean linked(Node node) {
        return a == node || b == node;
    }
}