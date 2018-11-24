package game.graph;

import game.Tools;

import java.awt.Point;
import java.awt.Color;

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
        double maxForce = 0.00001;
        
        double maxVectorLength = 0;
        
        for (Point.Double force : forces) {
            double vectorLength = Math.sqrt(force.x*force.x + force.y*force.y);
            if (vectorLength > maxVectorLength) maxVectorLength = vectorLength;
        }
        
        System.out.println("max force: " + maxVectorLength);
        
        if (maxVectorLength < maxForce)
            return; // don't increase the forces if they're low
        
        for (Point.Double force : forces) {
            force.x = Tools.range(force.x, 0, maxVectorLength, 0, maxForce);
            force.y = Tools.range(force.y, 0, maxVectorLength, 0, maxForce);
        }
    }
    
    private static Point.Double[] generateForces(GraphData data) {
        var forces = new Point.Double[data.nodes.length];
        for (int i = 0; i < forces.length; i++) forces[i] = new Point.Double(0, 0);
        
        double maxNodeAttraction = 0;
        double maxNodeRepulsion = 0;
        
        for (int i = 0; i < data.nodes.length; i++) {
            var node = data.nodes[i];
            
            // linked node attraction
            for (Node neighbor : node.myNodes) {
                forces[i].x += 500 * (neighbor.x - node.x);
                forces[i].y += 500 * (neighbor.y - node.y);
                if (500 * (neighbor.x - node.x) > maxNodeAttraction) maxNodeAttraction = 500 * (neighbor.x - node.x);
                if (500 * (neighbor.y - node.y) > maxNodeAttraction) maxNodeAttraction = 500 * (neighbor.y - node.y);
                
                // double rIntensity = intensity / distance;
            }
            
            // all node repulsion
            for (Node other : data.nodes) {
                if (other == node) continue;
                forces[i].x += (node.x < other.x ? -1 : 1) / Math.pow(other.x - node.x, 2);
                forces[i].y += (node.y < other.y ? -1 : 1) / Math.pow(other.y - node.y, 2);
                if ((node.x < other.x ? -1 : 1) / Math.pow(other.x - node.x, 2) > maxNodeRepulsion) maxNodeRepulsion = (node.x < other.x ? -1 : 1) / Math.pow(other.x - node.x, 2);
                if ((node.y < other.y ? -1 : 1) / Math.pow(other.y - node.y, 2) > maxNodeRepulsion) maxNodeRepulsion = (node.y < other.y ? -1 : 1) / Math.pow(other.y - node.y, 2);
            }
        }
        
        System.out.println("max+force: " + maxNodeAttraction);
        System.out.println("max-force: " + maxNodeRepulsion);
        
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