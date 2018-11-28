package game.graph;

import game.useful.Tools;

import java.util.ArrayList;
import java.awt.Point;
		
public class Generator {
    private static int defaultNodes = 15;
    private static int defaultEdges = 20;
    
    public static GraphData makeGraph() {
        return makeGraph(defaultNodes, defaultEdges);
    }
    public static GraphData makeGraph(int nNodes, int nEdges) {
		System.out.println("nodes: " + nNodes);
        System.out.println("edges: " + nEdges);
		// REDO THIS!!!!!!!!!!!!!
        
        int minEdges = nNodes - 1;
        int maxEdges = (nNodes * (nNodes - 1)) / 2;
        
        if (nEdges < minEdges) {
            System.err.println(String.format("error: too few edges (%d edges for %d nodes, should be %d - %d)", nEdges, nNodes, minEdges, maxEdges));
            System.exit(1);
        }
        
        if (nEdges > maxEdges) {
            System.err.println(String.format("error: too many edges (%d edges for %d nodes, should be %d - %d)", nEdges, nNodes, minEdges, maxEdges));
            System.exit(1);
        }
        
        var edges = new int[nEdges][2];
        
        // randomize
        for (int i = 0; i < nEdges; i++) {
            for (int n = 0; n < nNodes; n++) {
                for (int m = 0; m < nNodes; m++) {
                    if (n == m) continue;
                    var candidate = new int[] {n, m};
                    if (edgeExists(candidate, edges)) continue;
                    edges[i] = candidate;
                }
            }
        }
        
        // link unlinked
        outer: for (int n = 0; n < nNodes; n++) {
            for (int[] edge : edges) if (edge[0] == n || edge[1] == n) continue outer;
            // n is unlinked
            for (int m = 0; m < nNodes; m++) {
                if (countEdges(m, edges) < 2) continue;
                for (int o = 0; o < nNodes; o++) {
                    if (countEdges(o, edges) < 2) continue;
                    var search = new int[] {m, o};
                    for (int[] edge : edges) {
                        if (sameEdge(search, edge)) {
                            // m and o are linked and both have 2+ edges
                            edge[Math.random() < 0.5 ? 0 : 1] = n;
                        }
                    }
                }
            }
        }
        
        checkEdges(edges);
        
        System.out.print("edges: ");
        for (int[] edge : edges)
            System.out.print(edge[0] + "-" + edge[1] + " ");
        System.out.println();
        
        return new GraphData(nNodes, edges);
    }
    
    private static boolean sameEdge(int[] edge0, int[] edge1) {
        return edge0[0] == edge1[0] && edge0[1] == edge1[1] || edge0[0] == edge1[1] && edge0[1] == edge1[0];
    }
    
    private static boolean edgeExists(int[] edge, int[][] edges) {
        for (int[] check : edges) if (check != null && sameEdge(edge, check)) return true;
        return false;
    }
    
    private static int countEdges(int node, int[][] edges) {
        int es = 0;
        for (int[] edge : edges) if (edge[0] == node || edge[1] == node) es++;
        return es;
    }
    
    private static boolean linked(int n, int m, int[][] edges) {
        var edge = new int[] {n, m};
        for (int[] check : edges) if (sameEdge(edge, check)) return true;
        return false;
    }
    
    private static void checkEdges(int[][] edges) {
        for (int i = 0; i < edges.length; i++) {
            var edge = edges[i];
            if (edge == null) {
                System.err.println("error: edge is null");
                System.exit(1);
            }
            if (edge[0] == edge[1]) {
                System.err.println("error: self-link");
                System.exit(1);
            }
            for (int j = 0; j < edges.length; j++) {
                if (i == j) continue;
                if (sameEdge(edge, edges[j])) {
                    System.err.println("error: duplicate edge");
                    System.exit(1);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        makeGraph(12, 20);
    }
}