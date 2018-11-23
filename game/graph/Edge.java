package game.graph;

public class Edge {
    public Node a;
    public Node b;
    
    public boolean linked(Node node) {
        return a == node || b == node;
    }
    
    
    // styling
    public static final int NORMAL = 10; // +10 to avoid interference with Node.style
    public static final int THICK = 1;
    public static final int DARK = 2;
    
    public int style = NORMAL;
}