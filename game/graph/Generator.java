package game.graph;

import game.Tools;

import java.util.ArrayList;
import java.awt.Point;
		
public class Generator {
    private static int defaultNodes = 15;
    private static int defaultEdges = 20;
    
    public static GraphData makeGraph() {return makeGraph(defaultNodes, defaultEdges);}
    public static GraphData makeGraph(int nNodes) {return makeGraph(nNodes, defaultEdges);}
    public static GraphData makeGraph(int nNodes, int nEdges) {
		
		// REDO THIS
        
        int minEdges = nNodes - 1;
        int maxEdges = (nNodes * (nNodes - 1)) / 2;
        
        if (nEdges < minEdges)
            System.err.println("warning: some nodes will be lonely");
        
        if (nEdges > maxEdges)
            System.err.println("warning: too many edges requested");
        
        var edges = new ArrayList<Point>();
        
        mainLoop: for (int i = 0; i < nEdges; i++) {
            var edge = new Point(Tools.randInt(0, nNodes - 1), Tools.randInt(0, nNodes - 1));
			
			if (edge.x == edge.y)
				continue;
            
            for (Point check : edges)
                if (check.x == edge.x && check.y == edge.y ||
                    check.y == edge.x && check.x == edge.y)
                    continue mainLoop;
            
            edges.add(edge);
        }
        
        System.out.println("generated " + edges.size() + " / " + nEdges + " edges");
        
        var edgesA = new Point[edges.size()];
        
        int i = 0;
        for (Point edge : edges)
            edgesA[i++] = edge;
        
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