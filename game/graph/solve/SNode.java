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
        if (color >= 0) {
            if (color == newColor) System.err.println("warning: reset to same color");
            else {
                System.err.println("tried to change SNode color from " + color + " to " + newColor);
                System.exit(1);
            }
        }
        if (newColor < 0) {
            System.err.println("potential error: attempted to set color to -1");
            System.exit(1);
        }
        int i = 0;
        // System.out.println("CHECKING " + newColor);
        for (SNode node : myNodes) {if (node.color == newColor) {
            throw new ColorConflict("would collide with node " + i + ": " + node.color + " -> " + newColor); // probably useless
        } i++;}
        // System.out.println("SETTING " + newColor);
        color = newColor;
        allowed = null;
        // System.out.println("disallowing " + newColor);
        for (SNode node : myNodes) node.disallow(newColor);
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
        
        int index = allowed.indexOf(c);
        if (index >= 0) allowed.remove(index);
        
        // if (allowed.isEmpty()) { // this was for safety
        //     emptyfail++;
        //     throw new ColorConflict("allowed length = 0 (color = " + c + ")");
        // }
        
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
    }
}