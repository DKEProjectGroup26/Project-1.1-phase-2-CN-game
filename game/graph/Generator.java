package game.graph;

import game.Tools;

import java.util.ArrayList;
		
public class Generator {
    private static int defaultNodes = 15;
    private static int defaultEdges = 20;
    
    public static GraphData makeGraph() {return makeGraph(defaultNodes, defaultEdges);}
    public static GraphData makeGraph(int nNodes, int nEdges) {
        
        int minEdges = nNodes - 1;
        int maxEdges = (nNodes * (nNodes - 1)) / 2;
        
        if (nEdges < minEdges)
            System.err.println("warning: some nodes will be lonely");
        
        if (nEdges > maxEdges)
            System.err.println("warning: too many edges requested");
        
        var edges = new ArrayList<int[]>();
        
        mainLoop: for (int i = 0; i < nEdges; i++) {
            var edge = new int[] {Tools.randInt(0, nNodes - 1), Tools.randInt(0, nNodes - 1)};
            
            for (int[] check : edges)
                if (check[0] == edge[0] && check[1] == edge[1] ||
                    check[1] == edge[0] && check[0] == edge[1])
                    continue mainLoop;
            
            edges.add(edge);
        }
        
        System.out.println("generated " + edges.size() + " / " + nEdges + " edges");
        
        var edgesA = new int[edges.size()][2];
        
        int i = 0;
        for (int[] edge : edges) {
            edge[0]++; // for 1-indexing
            edge[1]++;
            edgesA[i++] = edge;
        }
        
        
        return new GraphData(nNodes, edgesA);
    }
    
    public static void main(String[] args) {
        var data = makeGraph(10, 46);
        System.out.println(data.nNodes);
        System.out.println(data.edges.length);
        data.makeLines();
        System.out.println(data.lines.length);
    }
}