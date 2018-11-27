package game.graph.solve;

import game.useful.GoodList;
import game.graph.basic.BasicNode;

import java.util.ArrayList;

public class SNode extends BasicNode<SNode, SEdge> {
    public int color = -1; // different from Node.color, -1 is for white (uncolored)
    public Graph graph = null; // parent reference
    
    public void setColor(int newColor) throws ColorConflict {
        if (graph == null) {
            System.err.println("error: graph is null");
            System.exit(1);
        }
        if (color >= 0) {
            System.err.println("tried to change SNode color from " + color + " to " + newColor);
            System.exit(1);
        }
        if (newColor < 0) {
            System.err.println("potential error: attempted to set color to -1");
            System.exit(1);
        }
        for (SNode node : myNodes) if (node.color == newColor) throw new ColorConflict(); // probably useless
        color = newColor;
        // System.out.println("disallowing " + newColor);
        for (SNode node : myNodes) node.disallow(newColor);
    }
    
    public int[] allowed() {
        if (color >= 0) {
            System.err.println("error: called allowed on solved node");
            System.exit(1);
        }
        var aList = new ArrayList<Integer>();
        outer: for (int c = 0; c < graph.nColors; c++) {
            for (SNode node : myNodes) if (node.color == c) continue outer;
            aList.add(c);
        }
        var array = new int[aList.size()];
        for (int i = 0; i < aList.size(); i++) array[i] = aList.get(i);
        return array;
    }
    
    // only for performance, reenable when it works
    public void disallow(int c) throws ColorConflict {
        if (color >= 0) return;

        var allowed = allowed(); // will be sorted, min is first
        
        if (allowed.length == 0) throw new ColorConflict("allowed length = 0 (color = " + c + ")");
        else if (allowed.length == 1) {
            // System.out.println("only color left: " + allowed[0]);
            setColor(allowed[0]);
        } else {
            boolean allColored = true;
            for (SNode node : myNodes) {
                if (node.color < 0) {
                    allColored = false;
                    break;
                }
            }
            if (allColored) {
                // System.out.println("all colored, setting to: " + allowed[0]);
                setColor(allowed[0]);
            }
        }
    }
}