package game.graph;

import game.Tools;
		
public class Positioner {
    // ALL COORDINATES ARE DOUBLES [0, 1]
    private static int minDist = 60;
    
    // this is the method that's called
    public static double[][] getCoords(GraphData data) {
        return randCoordinates(data);
    }
    
    public static double[][] randCoordinates(GraphData data) {
        var coords = new double[data.nNodes][2];
        
        for (int i = 0; i < data.nNodes; i++)
            coords[i] = new double[] {Math.random(), Math.random()};
        
        // check edge intersections and flip a pair of the vertices (stuff)
        
        return coords;
    }
    
    // private static int subS(int c0, int c1, int n) {
    //     return c0 + ((n - 1) / 2) * (c0 - c1);
    // }
    
    // private static void spread(int[] c0, int[] c1) {
    //     System.out.println("spreading: " + c0[0] + "," + c0[1] + " | " + c1[0] + "," + c1[1]);
    //
    //     int distance = Tools.intEuclidDist(c0, c1);
    //     if (distance == 0) {
    //         c0[0]++;
    //         c0[1]++;
    //         distance = Tools.intEuclidDist(c0, c1);
    //     }
    //
    //     if (distance == 0) {
    //         System.err.println("something has gone very worng");
    //         System.err.println(c0[0] + "," + c0[1] + " | " + c1[0] + "," + c1[1]);
    //         System.exit(1);
    //     }
    //
    //     int scale = minDist / distance;
    //
    //     c0[0] = subS(c0[0], c1[0], scale);
    //     c0[1] = subS(c0[1], c1[1], scale);
    //     c1[0] = subS(c1[0], c0[0], scale);
    //     c1[1] = subS(c1[1], c0[1], scale);
    //
    // }
    
    public static void main(String[] args) {
        game.Main.main(null);
    }
}