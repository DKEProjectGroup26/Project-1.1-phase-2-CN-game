package game.graph.solve;

import game.graph.basic.BasicGraphData;
import game.graph.basic.BasicNode;
import game.graph.basic.BasicEdge;
import game.graph.GraphData;
import game.graph.Node;
import game.visual.ColorPrecedence;

import java.awt.Color;

import java.util.ArrayList;

public class Graph extends BasicGraphData<SNode, SEdge> {
    // FOR TESTING ONLY ########################
    public static Graph make(int nNodes, int[][] inEdges) {
        System.out.println("test constructor called");
        return new Graph(new game.graph.GraphData(nNodes, inEdges));
    }
    // #########################################
    
    public Color[] colorOrder = null;
    // this stores the relationships between visual colors (if contained in input GraphData object) and the 0,1,... colors used by Graph, SNode and SEdge
    
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
        var b = System.nanoTime();
        for (int i = 0; i < data.nodes.length; i++) {
            var node = nodes[i];
            var oldNode = data.nodes[i];
            node.myNodes = new SNode[data.nodes[i].myNodes.length];
            for (int j = 0; j < node.myNodes.length; j++)
                node.myNodes[j] = nodes[data.indexOfNode(oldNode.myNodes[j])];
            
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
        System.out.println((System.nanoTime() - b) / 1e6 + "ms for my/other stuff");
        
        if (dataIn instanceof Graph) {
            // extract data from Graph object
            var dataGraph = (Graph) dataIn;
            setNColors(dataGraph.nColors);
            for (int i = 0; i < nodes.length; i++) {
                // nodes[i].color = dataIn.nodes[i].color;
                nodes[i].extract(dataGraph.nodes[i]);
            }
            colorOrder = dataGraph.colorOrder;
        } else if (dataIn instanceof GraphData) {
            // recover visual colors from GraphData object and convert to int scale, also set nColors
            var dataGData = (GraphData) dataIn;
            var vColors = new ArrayList<Color>();
            for (int i = 0; i < dataGData.nodes.length; i++) {
                var color = dataGData.nodes[i].color;
                if (Color.WHITE.equals(color)) {
                    nodes[i].color = -1;
                } else {
                    if (!vColors.contains(color)) vColors.add(color);
                }
            }
            setNColors(vColors.size());
            
            for (int i = 0; i < nodes.length; i++) {
                try {
                    nodes[i].setColor(vColors.indexOf(dataGData.nodes[i].color));
                } catch (ColorConflict e) {
                    nodes[i].color = vColors.indexOf(dataGData.nodes[i].color);
                    nodes[i].allowed = null;
                }
                // should also work for white -> -1
            }
            
            // for (SNode node : nodes) node.update(); // for allowed logic to work with the new colors
            System.out.println("imported: " + vColors);
            colorOrder = vColors.toArray(new Color[vColors.size()]);
        }
    }
    
    public void clearColors() {
        // implement
    }
    
    public Integer nColors = null;
    public void setNColors(Integer newNColors) {
        if (newNColors == null) return;
        nColors = newNColors;
        for (SNode node : nodes) node.setNColors(newNColors);
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
        int startColors = nColors == null ? clique.length : Math.max(clique.length, nColors);
        // start from what you have
        for (int colors = startColors; solution == null; colors++) {
            if (colors == flooded.nColors) {
                solution = flooded;
                break;
            }
            
            System.out.println("trying with " + colors + " colors");
            var solving = new Graph(this);
            solving.setNColors(colors);
            
            try {
                for (SNode node : solving.nodes) if (node.myNodes.length == 0) node.setColor(0);
            } catch (ColorConflict e) {
                System.err.println("THIS IS TERRIBLE");
                System.exit(1);
            }
            
            solution = subSolve(solving);
        }
        System.out.println("TIME>>solution: " + (System.nanoTime() - start)/1_000_000.0 + "ms");
        
        // remake colorOrder
        var newOrder = new Color[solution.nColors];
        for (int i = 0; i < newOrder.length; i++) {
            if (i < colorOrder.length) newOrder[i] = colorOrder[i]; // preserve previously assigned
            else {
                // find first unmentioned color
                cLoop: for (Color color : ColorPrecedence.colors) {
                    for (Color check : newOrder) {
                        if (check == null) break;
                        if (check == color) continue cLoop;
                    }
                    
                    // found color
                    newOrder[i] = color;
                    break;
                }
            }
        }
        System.out.println("color order remade, length: " + colorOrder.length + " -> " + newOrder.length);
        solution.colorOrder = newOrder;
    }
    
    private Graph subFlood(Graph graph) {
        if (graph.isSolved()) return graph;
        if (graph.nodeOrder == null) graph.makeOrder(); // TESTING ############ should be done by caller
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
                    long b = System.nanoTime();
                    var next = new Graph(graph);
                    System.out.println((System.nanoTime() - b) / 1e6 + "ms");
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
        var graph = new Graph(game.graph.Reader.readGraph("game/Graphs/graph01.txt"));
        // System.out.println(graph.subFlood(new Graph(graph)).nColors);
        graph.solve();

        System.out.println(graph.solution.nColors);
    }
}