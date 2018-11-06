package game.graph;

import java.util.ArrayList;

public class Node {
    int value;
    Integer color = null; // change to int = -1 (faster)
    Node[] linked = null;
    int nColors = 0;
    
    public Node(int vv) {
        value = vv;
    }
    
    public void findLinks(int[][] edges, Node[] nodes) {
        var indices = new ArrayList<Integer>();
        
        for (int[] edge : edges) {
            if (edge[0] == value)
                indices.add(edge[1]);
            else if (edge[1] == value)
                indices.add(edge[0]);
        }
        
        int nIndices = indices.size();
        linked = new Node[nIndices];
        
        for (int i = 0; i < nIndices; i++)
            linked[i] = nodes[indices.get(i)];
    }
    
    public void giveColors(int ccs) {
        nColors = ccs;
    }
    
    public void setColor(int color) throws BranchFailException {
        this.color = color;
        
        for (Node link : linked) {
            if (link.color == color)
                throw new BranchFailException();
            link.checkLinks();
        }
    }
    
    public boolean hasColor() {
        return color != null;
    }
    
    public void checkLinks() throws BranchFailException {
        if (color != null)
            return;
        
        for (Node link : linked)
            if (link.color == null)
                return;
        
        int newColor = 0;
        
        while (true) { // might be inefficient
            int was = newColor;
            for (Node link : linked) {
                if (link.color == newColor) {
                    newColor++;
                    break;
                }
            }
            
            if (was == newColor)
                break;
        }
        
        if (newColor >= nColors)
            throw new BranchFailException();
        
        color = newColor;
    }
}