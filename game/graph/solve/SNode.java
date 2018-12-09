package game.graph.solve;

import game.useful.GoodList;
import game.graph.basic.BasicNode;

import java.util.ArrayList;

public class SNode extends BasicNode<SNode, SEdge> {
    public int color = -1; // different from Node.color, -1 is for white (uncolored)
    private Integer nColors = -1;
    public ArrayList<Integer> allowed = null;
    
    public int[] myNodeIndices;
    public int[] otherNodeIndices;
    public int[] myEdgeIndices;
    public int[] otherEdgeIndices;
    
    public void setNColors(int n) {
        // resets nColors too
        nColors = n;
        allowed = new ArrayList<Integer>();
        for (int i = 0; i < nColors; i++) allowed.add(i);
    }
    
    public void extract(SNode from) {
        color = from.color;
        allowed = color >= 0 || from.allowed == null ? null : new ArrayList<>(from.allowed);
    }
    
    public void setColor(int newColor) throws ColorConflict {
        if (color == newColor) return; // might be dangerous
        
        if (nColors < 0) {
            System.err.println("warning: tried to setColor with nColors not given");
        }
        
        if (color >= 0 && newColor >= 0) {
            System.err.println("warning>> reassigning colored node (" + color + " -> " + newColor + ")");
            throw new RuntimeException();
            // return;
        }
        
        if (newColor < 0) {
            System.err.println("warning>> clearing color (" + color + " -> " + newColor + ")");
            color = newColor;
        }
        else {
            int i = 0;
            for (SNode node : myNodes) {if (node.color == newColor) {
                throw new ColorConflict("(" + this + ") would collide with node " + node + ": " + node.color + " -> " + newColor); // probably useless
            } i++;}
            color = newColor;
            allowed = null;
            for (SNode node : myNodes) node.disallow(newColor);
        }
    }
    
    // resets allowed based on current situation
    public void reevaluate() {
        if (nColors < 0) throw new RuntimeException("error: nColors not given befor reevaluation");
        
        if (color >= 0) {
            allowed = null;
            return;
        }
        
        allowed = new ArrayList<>();
        
        if (nColors == 0) return;
        
        outer: for (int c = 0; c < nColors; c++) {
            for (SNode other : myNodes) if (other.color == c) continue outer;
            allowed.add(c);
        }
        if (allowed.isEmpty()) {
            allowed = null; // just in case, nColors is inadequate in this case
            return;
        }
        if (allowed.size() == 1) {
            color = allowed.get(0);
            allowed = null;
            for (SNode other : myNodes) if (other.color == color)
                throw new RuntimeException("conflict in reevaluate");
            return;
        }
    }
    
    public void disallow(int c) throws ColorConflict {
        if (color >= 0) return;
        
        // if (allowed == null) {
        //     if (color >= 0) {
        //         System.err.println("allowed is null, bad, color: " + color);
        //         System.exit(1);
        //     } else {
        //         allowed = new ArrayList<Integer>();
        //         outer: for (int i = 0; i < nColors; i++) {
        //             for (SNode other : myNodes) if (other.color == i) continue outer;
        //             allowed.add(i);
        //         }
        //     }
        // }
        
        int index = allowed.indexOf(c);
        if (index >= 0) allowed.remove(index);
        
        if (allowed.isEmpty()) // this is for safety
            throw new ColorConflict("allowed length = 0 (color = " + c + ")");
        
        if (allowed.size() == 1)
            setColor(allowed.get(0));
        else {
            boolean allColored = true;
            for (SNode node : myNodes) {
                if (node.color < 0) {
                    allColored = false;
                    break;
                }
            }
            if (allColored) setColor(allowed.get(0));
        }
        
        // reassess();
        // for (SNode node : myNodes) if (node.color < 0) node.reassess();
    }
    
    // public void reassess() {
    //     if (color >= 0) return;
    //     // checks all neighbors and if all of them aren't allowed a color, sets that color
    //     outer: for (int color : allowed) {
    //         for (SNode node : myNodes) if (node.color >= 0 || node.allowed.contains(color)) continue outer;
    //
    //         try {
    //             setColor(color);
    //         } catch (ColorConflict e) {
    //             System.err.println("this really shouldn't have happened");
    //             System.exit(1);
    //         }
    //         break;
    //     }
    // }
}