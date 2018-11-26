package game.graph.solve;

import game.graph.basic.BasicGraphData;
import game.graph.basic.BasicNode;
import game.graph.basic.BasicEdge;

public class Graph extends BasicGraphData<SNode, SEdge> {
    public <DataT extends BasicGraphData<? extends BasicNode, ? extends BasicEdge>> Graph (DataT dataIn) {
        var data = dataIn.getBasic(); // constructor can be used with GraphData and Graph (to clone)
        // essentially a deep clone, move more initialization to BasicGraphData
        nodes = new SNode[data.nodes.length];
        for (int i = 0; i < nodes.length; i++) nodes[i] = new SNode();
        edges = new SEdge[data.edges.length];
        for (int i = 0; i < edges.length; i++) {
            var edge = new SEdge();
            edge.a = nodes[data.indexOfNode(data.edges[i].a)];
            edge.b = nodes[data.indexOfNode(data.edges[i].b)];
            edges[i] = edge;
        }
        
        for (int i = 0; i < data.nodes.length; i++) {
            var node = nodes[i];
            var oldNode = data.nodes[i];
            node.myNodes = new SNode[data.nodes[i].myNodes.length];
            for (int j = 0; j < node.myNodes.length; j++) {
                var his = oldNode.myNodes[j];
                int iii = data.indexOfNode(his);
                var n = nodes[iii];
                node.myNodes[j] = n;
            }
            
            node.otherNodes = new SNode[oldNode.otherNodes.length];
            for (int j = 0; j < node.otherNodes.length; j++)
                node.otherNodes[j] = nodes[data.indexOfNode(oldNode.otherNodes[j])];
            
            node.myEdges = new SEdge[oldNode.myEdges.length];
            for (int j = 0; j < node.myEdges.length; j++)
                node.myEdges[j] = edges[data.indexOfEdge(oldNode.myEdges[j])];
            
            node.otherEdges = new SEdge[oldNode.otherEdges.length];
            for (int j = 0; j < node.otherEdges.length; j++)
                node.otherEdges[j] = edges[data.indexOfEdge(oldNode.otherEdges[j])];
        }
    }
    
    public boolean isValid() {
        for (SNode node : nodes) for (SNode other : node.myNodes)
            if (node.color >= 0 && node.color == other.color) return false;
        return true;
    }
}