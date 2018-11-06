package game.graph;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;
		
public class Reader {
    public static void main(String[] args) {
        var data = readGraph(10);
    }
    
    public static GraphData readGraph(int graphNumber) {
        return readGraph(String.format("game/Graphs/graph%02d.txt", graphNumber));
    }
    
    public static GraphData readGraph(String fileName) {
        
        int nNodes = 0;
        int nEdges = 0;
        int seenEdges = 0;
        
        var edges = new ArrayList<int[]>();
        
        try {
            var reader = new FileReader(fileName);
            var buffer = new BufferedReader(reader);
            
            String line;
            
            while ((line = buffer.readLine()) != null) {
                if (line.startsWith("//"))
                    continue;
                else if (line.startsWith("VERTICES = "))
                    nNodes = Integer.parseInt(line.substring(11));
                else if (line.startsWith("EDGES = "))
                    nEdges = Integer.parseInt(line.substring(8));
                else {
                    String[] edgeStr = line.split(" ");
                    int[] edge = {
                        Integer.parseInt(edgeStr[0]) - 1, // -1 for 0-indexing
                        Integer.parseInt(edgeStr[1]) - 1
                    };
                    edges.add(edge);
                    
                    seenEdges++;
                }
            }
        } catch (IOException ex) {
            System.err.println("error: couldn't read file");
            System.exit(1);
        }
        
        if (seenEdges != nEdges) {
            System.err.println("error: edge number mismatch");
            System.exit(1);
        }
        
        var data = new GraphData();
        
        data.nNodes = nNodes;
        data.edges = new int[nEdges][2];
        
        for (int i = 0; i < nEdges; i++)
            data.edges[i] = edges.get(i);
        
        return data;
    }
}