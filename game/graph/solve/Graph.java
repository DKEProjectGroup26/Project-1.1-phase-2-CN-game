package game.graph.solve;

import game.graph.basic.BasicGraphData;
import game.graph.basic.BasicNode;
import game.graph.basic.BasicEdge;

import java.util.ArrayList;

public class Graph extends BasicGraphData<SNode, SEdge> {
    // FOR TESTING ONLY
    public static Graph make(int nNodes, int[][] inEdges) {
        System.out.println("test constructor called");
        return new Graph(new game.graph.GraphData(nNodes, inEdges));
    }
    
    public <DataT extends BasicGraphData<? extends BasicNode, ? extends BasicEdge>> Graph (DataT dataIn) {
        var data = dataIn.getBasic(); // constructor can be used with GraphData and Graph (to clone)
        // essentially a deep clone, move more initialization to BasicGraphData
        nodes = new SNode[data.nodes.length];
        for (int i = 0; i < nodes.length; i++) nodes[i] = new SNode();
        edges = new SEdge[data.edges.length];
        for (int i = 0; i < edges.length; i++) {
            var edge = new SEdge();
            edge.a = nodes[data.indexOfNode(data.edges[i].a)];
            edge.b = nodes[data.indexOfNode(data.edges[i].b)];
            edges[i] = edge;
        }
        
        for (int i = 0; i < data.nodes.length; i++) {
            var node = nodes[i];
            var oldNode = data.nodes[i];
            node.myNodes = new SNode[data.nodes[i].myNodes.length];
            for (int j = 0; j < node.myNodes.length; j++) {
                var his = oldNode.myNodes[j];
                int iii = data.indexOfNode(his);
                var n = nodes[iii];
                node.myNodes[j] = n;
            }
            
            node.otherNodes = new SNode[oldNode.otherNodes.length];
            for (int j = 0; j < node.otherNodes.length; j++)
                node.otherNodes[j] = nodes[data.indexOfNode(oldNode.otherNodes[j])];
            
            node.myEdges = new SEdge[oldNode.myEdges.length];
            for (int j = 0; j < node.myEdges.length; j++)
                node.myEdges[j] = edges[data.indexOfEdge(oldNode.myEdges[j])];
            
            node.otherEdges = new SEdge[oldNode.otherEdges.length];
            for (int j = 0; j < node.otherEdges.length; j++)
                node.otherEdges[j] = edges[data.indexOfEdge(oldNode.otherEdges[j])];
        }
        
        if (dataIn instanceof Graph) completeCloning((Graph) dataIn);
    }
    
    public Integer nColors = null;
    public void setNColors(int newNColors) {
        nColors = newNColors;
        for (SNode node : nodes) node.setNColors(newNColors);
    }
    
    private void completeCloning(Graph dataIn) {
        nColors = dataIn.nColors;
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].color = dataIn.nodes[i].color;
            if (nColors != null) nodes[i].setNColors(nColors);
        }
    }
    
    public boolean isValid() {
        for (SNode node : nodes) for (SNode other : node.myNodes)
            if (node.color >= 0 && node.color == other.color) return false;
        return true;
    }
    
    public boolean isSolved() {
        if (!isValid()) return false;
        for (SNode node : nodes) if (node.color < 0) return false;
        return true;
    }
    
    public SNode[] maxClique() {
        ArrayList<SNode> stored = null;
        for (int i = 1; i <= nodes.length; i++) {
            var next = subClique(i, new ArrayList<SNode>());
            if (next != null) stored = next;
        }
        return stored.toArray(new SNode[stored.size()]);
    }
    public ArrayList<SNode> subClique(int size, ArrayList<SNode> sofar) {
        if (sofar.size() == size) return sofar;
        outer: for (SNode node : nodes) {
            if (sofar.contains(node)) continue;
            for (SNode stored : sofar) if (!node.linked(stored)) continue outer;
            var newSofar = new ArrayList<SNode>(sofar);
            newSofar.add(node);
            var attempt = subClique(size, newSofar);
            if (attempt != null) return attempt;
        }
        return null;
    }
    
    public boolean solved = false;
    public Graph solution = null;
    public void solve() {
        if (solved) {
            System.err.println("warning: attempting to re-solve Graph");
            return;
        }
        
        var clique = maxClique();
        
        for (int nColors = clique.length; solution == null; nColors++) {
            System.out.println("trying with " + nColors + " colors");
            var solving = new Graph(this);
            solving.setNColors(nColors);
            // for (SNode node : solving.nodes) if (node.myNodes.length == 0) node.color = 0;
            
            try {
                for (SNode node : solving.nodes) if (node.myNodes.length == 0) node.setColor(0);
            } catch (ColorConflict e) {
                System.err.println("THIS IS TERRIBLE");
                System.exit(1);
            }
            
            // test performance with sortedList
            
            solution = subSolve(solving);
        }
    }
    
    private Graph subSolve(Graph graph) {
        return subSolve(graph, 0);
    }
    private Graph subSolve(Graph graph, int depth) {
        System.out.println("depth: " + depth);
        if (depth > 100) {
            System.err.println("depth > 100");
            System.exit(1);
        }
        if (graph.isSolved()) return graph;
        // nodes or colors first, test performance
        // sort nodes by most -> least connected (disallowing, performance)
        for (int n = 0; n < graph.nodes.length; n++) {
            // System.out.println("  n = " + n);
            if (graph.nodes[n].color >= 0) {
                // System.out.println("    has color");
                continue;
            }
            for (int c : graph.nodes[n].allowed) {
                // System.out.println("    c = " + c);
                try {
                    var next = new Graph(graph);
                    // System.out.println("      try " + n + " to " + c);
                    next.nodes[n].setColor(c); // failed prints here
                    // System.out.println("      set " + n + " to " + c);
                    var attempt = subSolve(next, depth + 1);
                    if (attempt != null) return attempt;
                } catch (ColorConflict e) {
                    // should only be thrown through disallow
                    // System.out.println("conflict");
                    // System.out.println("      failed");
                }
            }
        }
        return null;
    }
    
    public static void main(String[] args) throws ColorConflict {
        // var graph = Graph.make(3, new int[][] {{0,1},{0,2},{1,2}});
        /*
            broken graphs: 1, 6?, 7, 10, 11, 12?, 14, 16, 18?, 19?
        */
        var graph = new Graph(game.graph.Reader.readGraph("game/Graphs/CLIQUE.txt"));
        graph.solve();

        System.out.println(graph.solution.nColors);
    }
}