package game.graph.solve;

import game.useful.GoodList;
import game.graph.basic.BasicNode;

import java.util.Collections;

public class SNode extends BasicNode<SNode, SEdge> {
    public int color = -1; // different from Node.color, -1 is for white (uncolored)
    public GoodList<Integer> allowed = null;
    
    public void setNColors(int nColors) {
        if (allowed != null) System.err.println("warning: redefinind allowed");
        allowed = new GoodList<Integer>();
        for (int i = 0; i < nColors; i++) allowed.add(i);
    }
    
    public void setColor(int newColor) throws ColorConflict {
        if (newColor < 0) {
            System.err.println("attempted to set color to -1, not bad, just might be");
            System.exit(1);
        }
        for (SNode node : myNodes) if (node.color == newColor) throw new ColorConflict();
        color = newColor;
        for (SNode node : myNodes) node.disallow(newColor);
    }
    
    public void disallow(int c) throws ColorConflict {
        if (color >= 0) return;
        allowed.deleteValue(c);
        if (allowed.isEmpty())
            throw new ColorConflict();
        else if (allowed.size() == 1)
            setColor(allowed.first());
        else {
            boolean allColored = true;
            for (SNode node : myNodes) {
                if (node.color < 0) {
                    allColored = false;
                    break;
                }
            }
            if (allColored) setColor(Collections.min(allowed));
        }
    }
}