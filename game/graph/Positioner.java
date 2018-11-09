package game.graph;

import game.Tools;
		
public class Positioner {
    private static int minDist = 60;
    
    // public static int[][] getCoordinates(GraphData data) {
    //     // coordinates are in range [0, 1000]
    //     var coords = new int[data.nNodes][2];
    //
    //     coords[0] = new int[] {Tools.randInt(0, 1000), Tools.randInt(0, 1000)};
    //
    //     return null;// coords;
    // }
    
    // temporary
    public static GraphData getCoords(GraphData data) {
        return randCoordinates(data);
    }
    
    // VERY BAD AND INCORRECT CODE (no errors but the result is terrible)
    public static GraphData randCoordinates(GraphData data) {
        var coords = new int[data.nNodes][2];
        
        for (int i = 0; i < data.nNodes; i++) {
            coords[i] = new int[] {Tools.randInt(0, 1000), Tools.randInt(0, 1000)};
            System.out.println("generated random coord: " + coords[i][0] + "," + coords[i][1]);
        }
        
        // check edge intersections and flip a pair of the vertices (stuff)
        
        int outerLoops = 0;
        int loops = 0;
        
        while (true) {
            boolean hadToBreak = false;
            
            while (true) {
                loops++;
                if (loops > 10) {
                    // System.err.println("warning: overlooped, nodes may overlap");
                    hadToBreak = true;
                    break;
                }
            
                boolean redo = false;
                for (int[] coord : coords) {
                    for (int[] check : coords) {
                        if (check == coord)
                            continue;
                    
                        if (Tools.euclidDist(coord, check) < minDist) {
                            redo = true;
                            spread(coord, check);
                        }
                    }
                }
            
                if (!redo)
                    break;
            }
            
            if (hadToBreak) {
                outerLoops++;
                if (outerLoops > 10)
                    break;
            }
            
            System.out.print("min dist: " + minDist);
            minDist++;
            System.out.println(" -> " + minDist);
        }
        
        var newData = new GraphData();
        newData.nNodes = data.nNodes;
        newData.edges = data.edges;
        newData.coords = coords;
        
        return newData;
    }
    
    private static int subS(int c0, int c1, int n) {
        return c0 + ((n - 1) / 2) * (c0 - c1);
    }
    
    private static void spread(int[] c0, int[] c1) {
        System.out.println("spreading: " + c0[0] + "," + c0[1] + " | " + c1[0] + "," + c1[1]);
        
        int distance = Tools.intEuclidDist(c0, c1);
        if (distance == 0) {
            c0[0]++;
            c0[1]++;
            distance = Tools.intEuclidDist(c0, c1);
        }
        
        if (distance == 0) {
            System.err.println("something has gone very worng");
            System.err.println(c0[0] + "," + c0[1] + " | " + c1[0] + "," + c1[1]);
            System.exit(1);
        }
        
        int scale = minDist / distance;
        
        c0[0] = subS(c0[0], c1[0], scale);
        c0[1] = subS(c0[1], c1[1], scale);
        c1[0] = subS(c1[0], c0[0], scale);
        c1[1] = subS(c1[1], c0[1], scale);
        
    }
    
    public static void main(String[] args) {
        game.Main.main(null);
    }
}