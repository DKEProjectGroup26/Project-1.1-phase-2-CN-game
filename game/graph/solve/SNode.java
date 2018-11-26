package game.graph.solve;

import game.graph.basic.BasicNode;

public class SNode extends BasicNode<SNode, SEdge> {
    public int color = -1; // different from Node.color, -1 is for white (uncolored)
}