package game.visual;

import java.awt.*;

public class Coordinator {
    public static int[][] getCoords(int nNodes, int[][] edges) {
        // range: [0, 1000]
        var coords = new int[nNodes][2];
        
        for (int i = 0; i < nNodes; i++) {
            coords[i] = new int[] {randInt(0, 1000), randInt(0, 1000)};
        }
        
        return coords;
    }
    
    private static int randInt(int from, int to) {
        return (int) (Math.random() * (to - from)) + from;
    }
    
    public static void main(String[] args) {
        System.out.println(randInt(0, 1000));
    }
}