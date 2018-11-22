package game.graph;

import java.io.*;

import java.util.ArrayList;
		
public class Reader {
    public static void main(String[] args) {
        var data = readGraph(17);
        System.out.println(data.nNodes);
        System.out.println(data.edges.length);
    }
    
    public static GraphData readGraph(int graphNumber) {
        // return readGraph(String.format("game/Graphs/graph%02d.txt", graphNumber));
        return readGraph(String.format("game/Graphs/graph%02d.txt", graphNumber));
    }
    
    public static GraphData readGraph(String fileName) {
        return readGraph(new File(fileName));
    }
    
    public static GraphData readGraph(File file) {
        
        System.out.println("attempting to read " + file);
        // String path = file.getAbsolutePath();
        // path = "/Users/pietro/Desktop/UM/Projects/Project 1-2/Graphs/graph01.txt";
                
        // System.out.println("attempting to read \"" + fileName + "\"");
        
        int nNodes = 0;
        int nEdges = 0;
        int seenEdges = 0;
                                            
        var edges = new ArrayList<int[]>();
        
        try {
            String path = file.getCanonicalPath();
            System.out.println("path: " + path);
            
            var reader = new FileReader(path);
            var buffer = new BufferedReader(reader);
            
            System.out.println("reader and buffer initialized");
            
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
                        Integer.parseInt(edgeStr[0]) - 1, // so that edges start from 0
                        Integer.parseInt(edgeStr[1]) - 1
                    };
                    edges.add(edge);
                    
                    seenEdges++;
                }
            }
        } catch (IOException e) {
            System.err.println("error: couldn't read file");
            System.err.println(e);
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