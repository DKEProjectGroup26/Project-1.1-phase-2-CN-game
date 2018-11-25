package game.graph;

import game.Tools;

import java.awt.Point;

public class PositionerJIC {
    public static void createCoords(GraphData data) {
        for (Node node : data.nodes) {
            // inefficient
            double x = 0, y = 0;
            while (x == y) {
                x = Math.random();
                y = Math.random();
            }
            node.x = x;
            node.y = y;
        }
        
        int iterations = 1; // iterations of the force simulation
        for (int i = 0; i < iterations; i++) iteratePhysics(data);
        
        for (int s = 0; s < 10; s++) System.out.println(); // space
        System.out.println("FINAL COORDINATES");
        for (Node node : data.nodes)
            System.out.println(node.x + ", " + node.y);
        
        normalizeCoords(data);
        
        for (int s = 0; s < 10; s++) System.out.println(); // space
        System.out.println("NORMALIZED COORDINATES");
        for (Node node : data.nodes)
            System.out.println(node.x + ", " + node.y);
    }
    // THERE IS A HUGE NaN PROBLEM HERE SOMEWHERE
    public static void iteratePhysics(GraphData data) {
        var aForces = attractiveForces(data);
        var rForces = repulsiveForces(data);
        
        for (int s = 0; s < 3; s++) System.out.println(); // space
        
        for (int i = 0; i < data.nodes.length; i++) {
            var force = new Point.Double(aForces[i].x - rForces[i].x, aForces[i].y - rForces[i].y);
            if (data.nodes[i].color.equals(game.visual.ColorPrecedence.colors[0])) {
                System.out.println("atrct: " + aForces[i].x + ", " + aForces[i].y);
                System.out.println("repls: " + rForces[i].x + ", " + rForces[i].y);
                System.out.println("force: " + force.x + ", " + force.y);
            }
            if (Double.isNaN(force.x)) force.x = 0;
            if (Double.isNaN(force.y)) force.y = 0;
            data.nodes[i].x += force.x;
            data.nodes[i].y += force.y;
        }
        
        // remove this
        normalizeCoords(data);
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
    // JOIN THESE TWO FUNCTIONS
    private static void normalize(Point.Double[] points, double maxForce) {
        var min = new Point.Double(1, 1);
        var max = new Point.Double(0, 0);
        
        for (Point.Double point : points) {
            if (point.x < min.x) min.x = point.x;
            if (point.x > max.x) max.x = point.x;
            if (point.y < min.y) min.y = point.y;
            if (point.y > max.y) max.y = point.y;
        }
        
        for (Point.Double point : points) {
            point.x = Tools.range(point.x, min.x, max.x, 0, maxForce); // non-standard range
            point.y = Tools.range(point.y, min.y, max.y, 0, maxForce);
        }
    }
    
    private static Point.Double[] attractiveForces(GraphData data) {
        double k = 1; // constant (force = k * length) [Hooke's law]
        var forces = new Point.Double[data.nodes.length];
        for (int i = 0; i < data.nodes.length; i++) {
            var node = data.nodes[i];
            var force = new Point.Double(0, 0);
            for (Node myNode : node.myNodes) {
                // linear
                // force.x += k * (myNode.x - node.x);
                // force.y += k * (myNode.y - node.y);
                
                // square (farther = stronger)
                var xDist = myNode.x - node.x;
                var yDist = myNode.y - node.y;
                force.x += sign(xDist) * k * Math.pow(xDist, 2);
                force.y += sign(yDist) * k * Math.pow(yDist, 2);
            }
            forces[i] = force;
        }
        normalize(forces, 0.01);
        for (Point.Double force : forces)
            if (Double.isNaN(force.x) || Double.isNaN(force.y))
                System.out.println("nan in aF: " + force);
        return forces;
    }
    
    private static int sign(double v) {return v < 0 ? -1 : 1;}
    private static Point.Double[] repulsiveForces(GraphData data) {
        double k = 1; // contstant (force = k / length^2) [inverse square law]
        var forces = new Point.Double[data.nodes.length];
        for (int i = 0; i < data.nodes.length; i++) {
            var node = data.nodes[i];
            var force = new Point.Double(0, 0);
            for (Node otherNode : node.otherNodes) {
                double xDist = otherNode.x - node.x;
                double yDist = otherNode.y - node.y;
                force.x += sign(xDist) * k / Math.pow(xDist, 2);
                force.y += sign(yDist) * k / Math.pow(yDist, 2);
            }
            forces[i] = force;
        }
        normalize(forces, 0.01);
        for (Point.Double force : forces)
            if (Double.isNaN(force.x) || Double.isNaN(force.y))
                System.out.println("nan in rF: " + force);
        return forces;
    }
    
    // implement repulsion from other edges
}
		
// public class Positioner {
//     // ALL COORDINATES ARE DOUBLES [0, 1]
//     private static int minDist = 60;
//
//     // this is the method that's called
//     public static Point.Double[] getCoords(GraphData data) {
//         return normalize(fCoords(data));
//     }
//
//     public static Point.Double[] randCoordinates(GraphData data) {
//         var coords = new Point.Double[data.nodes.length];
//
//         for (int i = 0; i < data.nodes.length; i++)
//             coords[i] = new Point.Double(Math.random(), Math.random());
//
//         // check edge intersections and flip a pair of the vertices (stuff)
//
//         return coords;
//     }
//
//     private static Point.Double[] fCoords(GraphData data) {
//         var coords = new Point.Double[data.nodes.length];
//
//         coords[0] = new Point.Double(0, 0);
//
//         for (int i = 1; i < data.nodes.length; i++)
//             coords[i] = farthestPoint(coords);
//
//         flipCoordinates(coords, data.edges);
//         // improveEdgeCross(coords, data.edges);
//
//         int loops = 0;
//         while (true) {
//             if (loops++ > 100) {
//                 System.err.println("warning: couldn't figure out how to avoid edge overlaps");
//                 break;
//             }
//
//             Integer p = edgeOverlap(coords, data.edges);
//
//             if (p == null)
//                 break;
//
//             // coords[p][0] += Tools.random(-0.02, 0.02);
//             // coords[p][1] += Tools.random(-0.02, 0.02);
//             coords[p] = farthestPoint(coords);
//         }
//
//         return coords;
//     }
//
//     private static Integer edgeOverlap(Point.Double[] coords, Point[] edges) {
//         for (Point edge : edges) {
//             int i = edge.x,
//                 j = edge.y;
//
//             for (int k = 0; k < coords.length; k++) {
//                 if (i == k || j == k)
//                     continue;
//
//                 if (!Tools.between(coords[k], coords[i], coords[j]))
//                     continue;
//
//                 if (Tools.onALine(coords[i], coords[j], coords[k], 0.1))
//                     return k;
//             }
//         }
//
//         return null;
//     }
//
//     private static void improveEdgeCross(Point.Double[] coords, Point[] edges) {
//         for (Point edge : edges) {
//             int i = edge.x,
//                 j = edge.y;
//
//             for (Point edge2 : edges) {
//                 int k = edge2.x,
//                     l = edge2.y;
//
//                 // i---j and k---l
//
//                 if (i == k && j == l) // same edge
//                     continue;
//
//                 if (Tools.edgesIntersect(coords[i], coords[j], coords[k], coords[l])) {
//                     Tools.flip(coords, i, j);
//
//                     // not finished
//                 }
//             }
//         }
//     }
//
//     private static void flipCoordinates(Point.Double[] coords, Point[] edges) {
//         for (int i = 0; i < coords.length; i++) {
//             for (int j = 0; j < coords.length; j++) {
//                 double l = lineLengthSum(coords, edges);
//                 Tools.flip(coords, i, j);
//                 double m = lineLengthSum(coords, edges);
//                 if (m > l)
//                     Tools.flip(coords, i, j);
//             }
//         }
//     }
//
//     private static double lineLengthSum(Point.Double[] coords, Point[] edges) {
//         double sum = 0;
//         for (Point edge : edges) {
//             int i = edge.x;
//             int j = edge.y;
//
//             sum += coords[i].distance(coords[j]);
//         }
//         return sum;
//     }
//
//     private static Point.Double farthestPoint(Point.Double[] points) {
//         double maxD = 0;
//         var maxP = new Point.Double(0, 0);
//
//         int precision = 2;
//
//         for (int p = 1; p <= precision; p++) {
//
//             double increment = Math.pow(10, -p);
//
//             for (double x = 0; x <= 1; x += increment) {
//                 for (double y = 0; y <= 1; y += increment) {
//                     double d = 1 / 0.0; // infinity
//
//                     for (Point.Double point : points) {
//                         if (point == null)
//                             break;
//                         // not yet defined
//
//                         double nD = point.distance(new Point.Double(x, y));
//                         if (nD < d)
//                             d = nD;
//                     }
//
//                     if (d > maxD) {
//                         maxP.x = x;
//                         maxP.y = y;
//                         maxD = d;
//                     }
//                 }
//             }
//         }
//
//         return maxP;
//     }
//
//     public static void main(String[] args) {
//         game.Main.main(null);
//     }
// }