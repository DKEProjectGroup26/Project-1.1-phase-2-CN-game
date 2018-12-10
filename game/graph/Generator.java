package game.graph;

import game.useful.Tools;

import java.util.ArrayList;
import java.awt.Point;
		
public class Generator {
    
    public static final int defaultNodes = 5;
    public static final int defaultEdges = 5;
    
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
        for (int i = 0; i < nEdges; i++) edges[i] = null;
        int iEdge = 0;
        
        for (int node = 0; iEdge < nEdges;) {
            var unlinked = new ArrayList<Integer>();
            var other = new ArrayList<Integer>();
            for (int c = 0; c < nNodes; c++) {
                if (c == node) continue;
                if (linked(c, node, edges)) continue;
                
                if (unlinked(c, edges)) unlinked.add(c);
                else other.add(c);
            }
            
            var takeFrom = unlinked.isEmpty() ? other : unlinked;
            
            if (takeFrom.isEmpty()) break;
            
            int c = takeFrom.get(Tools.randInt(0, takeFrom.size() - 1));
            
            edges[iEdge] = new int[] {node, c};
            node = c;
            
            iEdge++;
        }
        
        // fill in missing edges
        outer: for (int n = 0; n < nNodes; n++) {
            for (int m = 0; m < nNodes; m++) {
                if (n == m) continue;
                if (linked(n, m, edges)) continue;
                int nextNull = -1;
                for (int i = 0; i < edges.length; i++) if (edges[i] == null) {
                    nextNull = i;
                    break;
                }
                if (nextNull < 0) break outer;
                edges[nextNull] = new int[] {n, m};
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
        for (int[] check : edges) if (check != null && sameEdge(edge, check)) return true;
        return false;
    }
    
    private static boolean unlinked(int n, int[][] edges) {
        for (int[] edge : edges) if (edge != null && (edge[0] == n || edge[1] == n)) return false;
        return true;
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
}