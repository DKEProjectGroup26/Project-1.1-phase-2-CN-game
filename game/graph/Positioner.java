package game.graph;

import game.Tools;
		
public class Positioner {
    // ALL COORDINATES ARE DOUBLES [0, 1]
    private static int minDist = 60;
    
    // this is the method that's called
    public static double[][] getCoords(GraphData data) {
        return fCoords(data);
    }
    
    public static double[][] randCoordinates(GraphData data) {
        var coords = new double[data.nNodes][2];
        
        for (int i = 0; i < data.nNodes; i++)
            coords[i] = new double[] {Math.random(), Math.random()};
        
        // check edge intersections and flip a pair of the vertices (stuff)
        
        return coords;
    }
    
    private static double[][] fCoords(GraphData data) {
        var coords = new double[data.nNodes][2];
        
        coords[0] = new double[] {0, 0};
        
        for (int i = 1; i < data.nNodes; i++)
            coords[i] = farthestPoint(coords);
        
        flipCoordinates(coords, data.edges);
		// improveEdgeCross(coords, data.edges);
        
        int loops = 0;
        while (true) {
            if (loops++ > 100) {
                System.err.println("warning: couldn't figure out how to avoid edge overlaps");
                break;
            }
            
            Integer p = edgeOverlap(coords, data.edges);
            
            if (p == null)
                break;
            
            // coords[p][0] += Tools.random(-0.02, 0.02);
            // coords[p][1] += Tools.random(-0.02, 0.02);
			coords[p] = farthestPoint(coords);
        }
        
        return coords;
    }
    
    private static Integer edgeOverlap(double[][] coords, int[][] edges) {
        for (int[] edge : edges) {
            int i = edge[0] - 1,
                j = edge[1] - 1;
                
            for (int k = 0; k < coords.length; k++) {
                if (i == k || j == k)
                    continue;
                
                if (!Tools.between(coords[k], coords[i], coords[j]))
                    continue;
                
                if (Tools.onALine(coords[i], coords[j], coords[k], 0.1)) {
                    // testing:
                    // System.out.println();
                    // System.out.println(coords[i][0] + ",," + coords[i][1]);
                    // System.out.println(coords[j][0] + ",," + coords[j][1]);
                    // System.out.println(coords[k][0] + ".." + coords[k][1]);
                    return k;
                }
            }
        }
        
        return null;
    }
	
	private static void improveEdgeCross(double[][] coords, int[][] edges) {
		for (int[] edge : edges) {
			int i = edge[0] - 1,
				j = edge[1] - 1;
			
			for (int[] edge2 : edges) {
				int k = edge2[0] - 1,
					l = edge2[1] - 1;
				
				// i---j and k---l
				
				if (i == k && j == l) // same edge
					continue;
				
				if (Tools.edgesIntersect(coords[i], coords[j], coords[k], coords[l])) {
					Tools.flip(coords, i, j);
				}
			}
		}
	}
    
    private static void flipCoordinates(double[][] coords, int[][] edges) {
        for (int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords.length; j++) {
                double l = lineLengthSum(coords, edges);
                Tools.flip(coords, i, j);
                double m = lineLengthSum(coords, edges);
                if (m > l)
                    Tools.flip(coords, i, j);
            }
        }
    }
    
    private static double lineLengthSum(double[][] coords, int[][] edges) {
        // edges are 1-indexed
        double sum = 0;
        for (int[] edge : edges) {
            int i = edge[0] - 1;
            int j = edge[1] - 1;
            
            sum += Tools.euclidDist(coords[i], coords[j]);
        }
        return sum;
    }
    
    private static double[] farthestPoint(double[][] points) {
        double maxD = 0;
        double[] maxP = {0, 0};
    
        int precision = 2;
    
        for (int p = 1; p <= precision; p++) {
        
            double increment = Math.pow(10, -p);
        
            for (double x = 0; x <= 1; x += increment) {
                for (double y = 0; y <= 1; y += increment) {
                    double d = 1 / 0.0; // infinity
                
                    for (double[] point : points) {
                        if (point == null)
                            break;
                        // not yet defined
                        
                        double nD = Tools.euclidDist(point[0], point[1], x, y);
                        if (nD < d)
                            d = nD;
                    }
                
                    if (d > maxD) {
                        maxP[0] = x;
                        maxP[1] = y;
                        maxD = d;
                    }
                }
            }
        }
    
        return maxP;
    }
    
    // public static double[][] pCoords(GraphData data) {
    //     var coords = new double[data.nNodes][2];
    //
    //     for (int i = 0; i < data.nNodes; i++)
    //         coords[i] = new double[] {Math.random(), Math.random()};
    //
    //     var forces = new double[data.nNodes][2];
    //     for (int i = 0; i < data.nNodes; i++) {
    //
    //         double x = coords[i][0];
    //         double y = coords[i][1];
    //
    //         double fx = 0;
    //         double fy = 0;
    //
    //         for (double[] c : coords) {
    //             if (c[0] == x && c[1] == y)
    //                 continue;
    //
    //             fx += 1 / (x - c[0]);
    //             fy += 1 / (y - c[1]);
    //         }
    //
    //         forces[i] = new double[] {fx, fy};
    //     }
    //
    //     for (int i = 0; i < data.nNodes; i++) {
    //         coords[i][0] += forces[i][0];
    //         coords[i][1] += forces[i][1];
    //     }
    //
    //     double xMax = 0;
    //     double yMax = 0;
    //     double xMin = 0;
    //     double yMin = 0;
    //     for (int i = 0; i < data.nNodes; i++) {
    //         double x = coords[i][0];
    //         double y = coords[i][1];
    //         xMax = x > xMax ? x : xMax;
    //         yMax = y > yMax ? y : yMax;
    //         xMin = x < xMin ? x : xMin;
    //         yMin = y < yMin ? y : yMin;
    //     }
    //
    //     for (int i = 0; i < data.nNodes; i++) {
    //         coords[i][0] += xMin;
    //         coords[i][0] /= (xMax - xMin);
    //         coords[i][1] += yMin;
    //         coords[i][1] /= (yMax - yMin);
    //     }
    //
    //     return coords;
    // }
    
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