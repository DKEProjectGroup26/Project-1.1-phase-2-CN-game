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
    public void setNColors(Integer newNColors) {
        if (newNColors == null) return;
        nColors = newNColors;
        for (SNode node : nodes) node.setNColors(newNColors);
    }
    
    private void completeCloning(Graph dataIn) {
        // redundant, use Graph#setNColors
        // nColors = dataIn.nColors;
        setNColors(dataIn.nColors);
        for (int i = 0; i < nodes.length; i++) {
            // nodes[i].color = dataIn.nodes[i].color;
            nodes[i].extract(dataIn.nodes[i]);
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
    
    public int[] maxClique() { // returns array of indices
        ArrayList<SNode> stored = null;
        for (int i = 1; i <= nodes.length; i++) {
            System.out.println("trying " + i + "-clique");
            if (stored != null) {
                // try expanding previous clique
                System.out.println("extending");
                var extended = subClique(i, stored, new ArrayList<SNode>());
                System.out.println("done");
                if (extended != null) {
                    stored = extended;
                    System.out.println("success");
                    System.out.println();
                    continue;
                }
                System.out.println("failure");
                System.out.println();
            }
            var next = subClique(i, new ArrayList<SNode>(), new ArrayList<SNode>());
            if (next == null) break; // return stored
            else stored = next;
        }
        var array = new int[stored.size()];
        for (int i = 0; i < stored.size(); i++) {
            for (int j = 0; j < nodes.length; j++) {
                if (stored.get(i) == nodes[j]) {
                    array[i] = j;
                    break;
                }
            }
        }
        return array;
    }
    public ArrayList<SNode> subClique(int size, ArrayList<SNode> sofar, ArrayList<SNode> excluded) {
        if (sofar.size() == size) return sofar;
        var myExcluded = new ArrayList<SNode>(excluded);
        outer: for (SNode node : nodes) {
            if (node.myNodes.length < size - 1) continue; // ignore nodes with not enough edges
            if (sofar.contains(node)) continue;
            if (myExcluded.contains(node)) continue;
            for (SNode stored : sofar) if (!node.linked(stored)) continue outer;
            var newSofar = new ArrayList<SNode>(sofar);
            newSofar.add(node);
            var attempt = subClique(size, newSofar, myExcluded);
            if (attempt != null) return attempt;
            // no cliques with this node exist
            myExcluded.add(node);
        }
        return null;
    }
    
    // the order in which the nodes should be checked
    // contains the indices of the nodes in order
    private int[] nodeOrder = null;
    private void makeOrder() {
        var order = new ArrayList<Integer>();
        while (order.size() < nodes.length) {
            int max = -1;
            int maxIndex = -1;
            for (int i = 0; i < nodes.length; i++) {
                if (order.contains(i)) continue;
                int connections = nodes[i].myNodes.length;
                if (max < 0 || connections > max) {
                    max = connections;
                    maxIndex = i;
                }
            }
            order.add(maxIndex);
        }
        nodeOrder = new int[order.size()];
        for (int i = 0; i < order.size(); i++) {
            nodeOrder[i] = order.get(i);
        }
        // currently useless, sorts min -> max
    }
    
    public boolean solved = false;
    public Graph solution = null;
    public void solve() {
        if (solved) {
            System.err.println("warning: attempting to re-solve Graph");
            return;
        }
        
        if (nodeOrder == null) makeOrder();
        
        System.out.println("calculating max clique");
        long start = System.nanoTime();
        var clique = maxClique();
        System.out.println("TIME>>clique: " + (System.nanoTime() - start)/1_000_000.0 + "ms");
        
        start = System.nanoTime();
        
        var flooded = subFlood(new Graph(this));
        System.out.println("flooded: " + flooded.nColors);
        for (int colors = clique.length; solution == null; colors++) {
            if (colors == flooded.nColors) {
                solution = flooded;
                break;
            }
            
            System.out.println("trying with " + colors + " colors");
            var solving = new Graph(this);
            solving.setNColors(colors);
            
            // set the clique to their colors
            // try {
            //     System.out.println(11);
            //     for (int i = 0; i < clique.length; i++) {
            //         var node = solving.nodes[clique[i]];
            //         if (node.color < 0) node.setColor(i);
            //     }
            //     System.out.println(22);
            // } catch (ColorConflict e) {
            //     System.err.println("this is bad");
            //     e.printStackTrace();
            //     System.exit(1);
            // }
            
            try {
                for (SNode node : solving.nodes) if (node.myNodes.length == 0) node.setColor(0);
            } catch (ColorConflict e) {
                System.err.println("THIS IS TERRIBLE");
                System.exit(1);
            }
            
            solution = subSolve(solving);
        }
        System.out.println("TIME>>solution: " + (System.nanoTime() - start)/1_000_000.0 + "ms");
    }
    
    private Graph subFlood(Graph graph) {
        if (graph.isSolved()) return graph;
        if (graph.nodeOrder == null) graph.makeOrder(); // TESTING ############ should be done by caller
        System.out.println("start");
        int colors = 1;
        graph.setNColors(colors);
        for (int n : graph.nodeOrder) {
            var node = graph.nodes[n];
            wh: while (true) {
                var forbidden = new ArrayList<Integer>();
                for (SNode other : node.myNodes) if (other.color >= 0) forbidden.add(other.color);
                if (forbidden.size() >= graph.nColors) {
                    graph.setNColors(++colors);
                    continue wh;
                }
                for (int i = 0;; i++) {
                    if (!forbidden.contains(i)) {
                        node.color = i;
                        break wh;
                    }
                }
            }
        }
        System.out.println("end");
        return graph;
    }
    
    private Graph subSolve(Graph graph) {
        return subSolve(graph, 0);
    }
    private Graph subSolve(Graph graph, int depth) {
        System.out.println("DEPTH: " + depth);
        // System.out.println(" ".repeat(depth) + "#");
        
        if (graph.isSolved()) return graph;
        
        // nodes or colors first, test performance
        // add exclusion logic
        for (int n : nodeOrder) {
            var node = graph.nodes[n];
            if (node.color >= 0) continue;
            for (int c : node.allowed) {
                try {
                    var next = new Graph(graph);
                    next.nodes[n].setColor(c); // ColorConflict thrown here
                    var attempt = subSolve(next, depth + 1);
                    if (attempt != null) return attempt;
                } catch (ColorConflict e) {}
            }
        }
        return null;
    }
    
    public static void main(String[] args) throws ColorConflict {
        // var graph = Graph.make(3, new int[][] {{0,1},{0,2},{1,2}});
        /*
            these are correct: 2 3 4 5 8 9 13 17
            take too long: 1 6 7 10 11 12 14 15 16 18 19 20
        */
        var graph = new Graph(game.graph.Reader.readGraph("game/Graphs/graph11.txt"));
        // System.out.println(graph.subFlood(new Graph(graph)).nColors);
        graph.solve();

        System.out.println(graph.solution.nColors);
    }
}