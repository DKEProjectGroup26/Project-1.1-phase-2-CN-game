package game.graph;

import game.Tools;

import java.awt.Point;

public class Positioner {
    public static Point.Double[] getCoords(GraphData data) {
        var coords = new Point.Double[data.nodes.length];
        for (int i = 0; i < coords.length; i++)
            coords[i] = new Point.Double(Math.random(), Math.random());
        
        normalize(coords);
        return coords;
    }
    
    private static void normalize(Point.Double[] coords) {
        var min = new Point.Double(1, 1);
        var max = new Point.Double(0, 0);
        
        for (Point.Double coord : coords) {
            if (coord.x < min.x) min.x = coord.x;
            if (coord.x > max.x) max.x = coord.x;
            if (coord.y < min.y) min.y = coord.y;
            if (coord.y > max.y) max.y = coord.y;
        }
        
        for (Point.Double coord : coords) {
            coord.x = Tools.range(coord.x, min.x, max.x, 0, 1);
            coord.y = Tools.range(coord.y, min.y, max.y, 0, 1);
        }
    }
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