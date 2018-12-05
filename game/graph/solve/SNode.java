package game.graph.solve;

import game.useful.GoodList;
import game.graph.basic.BasicNode;

import java.util.ArrayList;

public class SNode extends BasicNode<SNode, SEdge> {
    public int color = -1; // different from Node.color, -1 is for white (uncolored)
    private Integer nColors = -1;
    public ArrayList<Integer> allowed = null;
    
    public void setNColors(int n) {
        // resets nColors too
        nColors = n;
        allowed = new ArrayList<Integer>();
        for (int i = 0; i < nColors; i++) allowed.add(i);
    }
    
    public void extract(SNode from) {
        color = from.color;
        if (from.allowed != null) allowed = new ArrayList<>(from.allowed);
    }
    
    public void setColor(int newColor) throws ColorConflict {
        if (nColors < 0) {
            System.err.println("error: nColors not given");
            System.exit(1);
        }
        // if (color >= 0) {
        //     if (color == newColor) System.err.println("warning: reset to same color");
        //     else {
        //         System.err.println("tried to change SNode color from " + color + " to " + newColor);
        //         System.exit(1);
        //     }
        // }
        if (newColor < 0) color = newColor;
        else {
            int i = 0;
            // System.out.println("CHECKING " + newColor);
            for (SNode node : myNodes) {if (node.color == newColor) {
                throw new ColorConflict("would collide with node " + i + ": " + node.color + " -> " + newColor); // probably useless
            } i++;}
            color = newColor;
            allowed = null;
            for (SNode node : myNodes) node.disallow(newColor);
        }
    }
    
    // only for performance, reenable when it works
    public void disallow(int c) throws ColorConflict {
        // TESTING ################
        // if (emptyfail < 10 || emptyfail % 10 == 0) System.out.println("emptyfail: " + emptyfail);
        // if (colorleft < 10 || colorleft % 10 == 0) System.out.println("colorleft: " + colorleft);
        // if (allcolors < 10 || allcolors % 10 == 0) System.out.println("allcolors: " + allcolors);
        // END ####################
        
        // System.out.println("DISALLOWING: " + c);
        if (color >= 0) return;
        
        if (allowed == null) {
            System.err.println("allowed is null, bad");
            System.exit(1);
        }
        
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