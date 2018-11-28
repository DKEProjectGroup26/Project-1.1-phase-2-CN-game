package game.graph.solve;

import game.useful.GoodList;
import game.graph.basic.BasicNode;

import java.util.ArrayList;

public class SNode extends BasicNode<SNode, SEdge> {
    public int color = -1; // different from Node.color, -1 is for white (uncolored)
    private Integer nColors = -1;
    public int[] allowed;
    
    public void setNColors(int n) {
        nColors = n;
        allowed = new int[n];
        for (int i = 0; i < nColors; i++) allowed[i] = i;
    }
    
    public void setColor(int newColor) throws ColorConflict {
        if (nColors < 0) {
            System.err.println("error: nColors not given");
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
        int i = 0;
        System.out.println("CHECKING " + newColor);
        for (SNode node : myNodes) {if (node.color == newColor) {
            System.out.println("would collide with node " + i + ": " + node.color + " -> " + newColor);
            throw new ColorConflict(); // probably useless
        } i++;}
        System.out.println("SETTING " + newColor);
        color = newColor;
        allowed = null;
        // System.out.println("disallowing " + newColor);
        for (SNode node : myNodes) node.disallow(newColor);
    }
    
    // only for performance, reenable when it works
    public void disallow(int c) throws ColorConflict {
        System.out.println("DISALLOWING: " + c);
        if (color >= 0) return;
        
        System.out.print("length: " + allowed.length);
        var newAllowed = new int[allowed.length - 1];
        int i = 0;
        for (int a : allowed) if (a != c) newAllowed[i++] = a;
        allowed = newAllowed;
        System.out.println(" -> " + allowed.length);
        
        if (allowed.length == 0) {
            System.out.println("allowed length = 0");
            throw new ColorConflict("allowed length = 0 (color = " + c + ")");
        } else if (allowed.length == 1) {
            // System.out.println("only color left: " + allowed[0]);
            setColor(allowed[0]);
        }
        else {
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