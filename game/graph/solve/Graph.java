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

        for (SNode node : nodes) node.graph = this;
        
        if (dataIn instanceof Graph) completeCloning((Graph) dataIn);
    }
    
    private void completeCloning(Graph dataIn) {
        nColors = dataIn.nColors;
        for (int i = 0; i < nodes.length; i++) nodes[i].color = dataIn.nodes[i].color;
    }
    
    public Integer nColors = null;
    public void setNColors(int newNColors) {
        nColors = newNColors;
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
    
    public boolean solved = false;
    public Graph solution = null;
    public void solve() {
        if (solved) {
            System.err.println("warning: attempting to re-solve Graph");
            return;
        }
        
        for (int nColors = 1;; nColors++) {
            System.out.println("trying with " + nColors + " colors");
            var solving = new Graph(this);
            solving.setNColors(nColors);
            
            try {
                // IMPORTANT, sets unlinked nodes
                for (SNode node : solving.nodes) if (node.myNodes.length == 0) node.setColor(0);
            } catch (ColorConflict e) {
                System.err.println("THIS IS TERRIBLE");
                System.exit(1);
            }
            
            var attempt = subSolve(solving); // null if couldn't
            if (attempt != null) {
                solution = attempt;
                return;
            }
        }
    }
    
    private Graph subSolve(Graph graph) {return subSolve(graph, 0);}
    private Graph subSolve(Graph graph, int depth) {
        if (depth > 100) {
            System.err.println("depth > 100");
            System.exit(1);
        }
        if (graph.isSolved()) return graph;
        // nodes or colors first, test performance
        for (int n = 0; n < graph.nodes.length; n++) {
            if (graph.nodes[n].color >= 0) continue;
            for (int c : graph.nodes[n].allowed()) {
                try {
                    var next = new Graph(graph);
                    next.nodes[n].setColor(c);
                    return subSolve(next, depth + 1);
                } catch (ColorConflict e) {}
            }
        }
        return null;
    }
    
    public static void main(String[] args) throws ColorConflict {
        // var graph = Graph.make(3, new int[][] {{0,1},{1,2},{2,0}});
        /*
            broken graphs: 1, 6?, 7, 10, 11, 12?, 14, 16, 18?, 19?
        */
        var graph = new Graph(game.graph.Reader.readGraph("game/Graphs/Graph18.txt"));
        
        graph.solve();
        
        System.out.println(graph.solution.nColors);
    }
}