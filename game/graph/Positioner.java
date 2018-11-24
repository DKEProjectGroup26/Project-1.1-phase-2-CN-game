package game.graph;

import game.Tools;

import java.awt.Point;

public class Positioner {
    public static void createCoords(GraphData data) {
        for (Node node : data.nodes) {
            node.x = Math.random();
            node.y = Math.random();
        }
        
        int iterations = 1; // how many times to run physics
        
        for (int i = 0; i < iterations; i++)
            iteratePhysics(data);
        
        normalizeCoords(data); // makes range [0, 1]
    }
    
    private static void normalizeCoords(GraphData data) {
        var min = new Point.Double(data.nodes[0].x, data.nodes[0].y);
        var max = new Point.Double(data.nodes[0].x, data.nodes[0].y);
        
        for (Node node : data.nodes) {
            // one iteration is redundant
            if (node.x < min.x) min.x = node.x;
            if (node.x > max.x) max.x = node.x;
            if (node.y < min.y) min.y = node.y;
            if (node.y > max.y) max.y = node.y;
        }
        
        for (Node node : data.nodes) {
            node.x = Tools.range(node.x, min.x, max.x, 0, 1);
            node.y = Tools.range(node.y, min.y, max.y, 0, 1);
        }
    }
    
    public static void iteratePhysics(GraphData data) {
        // forces are absolute-value (subtraction is done in this method)
        var forces = generateForces(data);
        
        for (int i = 0; i < data.nodes.length; i++) {
            data.nodes[i].x += forces[i].x;
            data.nodes[i].y += forces[i].y;
        }
        
        normalizeCoords(data); // TEMPORARY, FOR ANIMATION ONLY
    }
    
    private static void normalize(Point.Double[] forces) {
        double maxForce = 0.0000001;
        
        double maxVectorLength = 0;
        
        for (Point.Double force : forces) {
            double vectorLength = Math.sqrt(force.x*force.x + force.y*force.y);
            if (vectorLength > maxVectorLength) maxVectorLength = vectorLength;
        }
        
        if (maxVectorLength < maxForce)
            return; // don't increase the forces if they're low
        
        for (Point.Double force : forces) {
            force.x = Tools.range(force.x, 0, maxVectorLength, 0, maxForce);
            force.y = Tools.range(force.y, 0, maxVectorLength, 0, maxForce);
        }
    }
    
    private static Point.Double[] generateForces(GraphData data) {
        var forces = new Point.Double[data.nodes.length];
        
        // linked node attraction
        // for (int i = 0; i < data.nodes.length; i++) {
        //     var node = data.nodes[i];
        //     forces[i] = new Point.Double(0, 0);
        //     for (Node neighbor : node.myNodes) {
        //         double distance = new Point.Double(neighbor.x, neighbor.y).distance(node.x, node.y);
        //         double k = distance < 0.05 ? -1 : 1;
        //         forces[i].x += k * (neighbor.x - node.x);
        //         forces[i].y += k * (neighbor.y - node.y);
        //     }
        // }
        
        // all node repulsion
        for (int i = 0; i < data.nodes.length; i++) {
            var node = data.nodes[i];
            forces[i] = new Point.Double(0, 0);
            for (Node other : data.nodes) {
                if (other == node) continue;
                forces[i].x -= (other.x > node.x ? 1 : -1) * 1 / Math.pow(other.x - node.x, 2);
                forces[i].y -= (other.y > node.y ? 1 : -1) * 1 / Math.pow(other.y - node.y, 2);
            }
        }
        
        // crossed edge twist // make
        
        normalize(forces);
        return forces;
    }
}














//     private static Point.Double[] nodeAttraction(GraphData data) {
//         var forces = new Point.Double[data.nodes.length];
//         for (int i = 0; i < forces.length; i++)
//             forces[i] = new Point.Double(0, 0);
//         // for (int i = 0; i < data.nodes.length; i++) {
//         //     var node = data.nodes[i];
//         //     var force = new Point.Double(0, 0);
//         //     for (Node myNode : node.myNodes) {
//         //         // linear restoring force
//         //         force.x += myNode.x - node.x;
//         //         force.y += myNode.y - node.y;
//         //     }
//         //     if (force == null) {
//         //         System.out.println("f is null");
//         //         System.exit(9);
//         //     }
//         //     forces[i] = force;
//         // }
//         //
//         // for (int i = 0; i < forces.length; i++) {
//         //     if (forces[i] == null) System.out.println("force is null in nA, index: " + i);
//         //     System.exit(i);
//         // }
//         // System.out.println("OK");
//
//         normalizeForces(forces, 0.001);
//         return forces;
//     }
//
//     private static Point.Double[] nodeRepulsion(GraphData data) {
//         var forces = new Point.Double[data.nodes.length];
//
//         for (int i = 0; i < data.nodes.length; i++) {
//             var node = data.nodes[i];
//             for (Node myNode : node.myNodes) {
//                 // inverse square law
//                 if (node.x == myNode.x && node.y == myNode.y)
//                     forces[i] = new Point.Double(1,1);
//                 else
//                     forces[i] = new Point.Double(
//                         (myNode.x > node.x ? 1 : -1) / Math.pow(myNode.x - node.x, 2),
//                         (myNode.y > node.y ? 1 : -1) / Math.pow(myNode.y - node.y, 2)
//                     );
//             }
//         }
//
//         normalizeForces(forces, 0.001);
//         return forces;
//     }
// }