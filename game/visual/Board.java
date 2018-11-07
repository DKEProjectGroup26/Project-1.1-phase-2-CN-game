package game.visual;

import java.awt.*;
import javax.swing.*;

public class Board extends JPanel {
    
    public Circle[] circles;
    public Line[] lines;
    int width;
    int height;
    
    public Board() {
        // this(800, 600);
        this(1400, 800);
    }
    
    public Board(int w, int h) {
        super(); // does nothing
        width = w;
        height = h;
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.black);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (circles == null || lines == null)
            return;
        
        for (Circle circle : circles)
            circle.draw(g);
        
        for (Line line : lines)
            line.draw(g);
    }
    
    public void drawGraph(int nNodes, int[][] edges) {
        circles = new Circle[nNodes];
        lines = new Line[edges.length];
        
        int[][] coords = Coordinator.getCoords(nNodes, edges);
        // coordinates are ALWAYS in range [0, 1000]
        
        // 0-1000 coords
        for (int[] c : coords)
            System.out.println(c[0] + ", " + c[1]);
        
        // adjustment
        for (int i = 0; i < coords.length; i++)
            coords[i] = adjust(coords[i]);
        
        System.out.println();
        // width - height coords
        for (int[] c : coords)
            System.out.println(c[0] + ", " + c[1]);
        
        for (int i = 0; i < nNodes; i++) {
            int[] coord = coords[i];
            circles[i] = new Circle(coord[0], coord[1], 20, Color.WHITE);
        }
        
        // edges are 1-indexed
        for (int i = 0; i < edges.length; i++) {
            var edge = edges[i];

            int[] n0 = null;
            int[] n1 = null;

            for (int c = 0; c < nNodes; c++) {
                if (edge[0] == c + 1)
                    n0 = new int[] {circles[c].x, circles[c].y};

                if (edge[1] == c + 1)
                    n1 = new int[] {circles[c].x, circles[c].y};

                if (n0 != null && n1 != null)
                    break;
            }
            
            if (n0 == null || n1 == null)
                System.err.println("THIS IS BAD, ignored for now " + n0 + " " + n1);
            else
                lines[i] = new Line(n0[0], n0[1], n1[0], n1[1], 5, Color.WHITE);
        }
        
        repaint();
    }
    
    private int adjust(int n, int fromMin, int fromMax, int toMin, int toMax) {
        double k = n;
        k -= fromMin;
        k /= fromMax - fromMin;
        k *= toMax - toMin;
        k += toMin;
        return (int) k;
    }
    
    private int centralize(int n, int max, int offset) {
        // assumes n is adjusted
        return adjust(n, 0, max, offset, max - offset);
    }
    
    private int[] adjust(int[] coord) {
        // from [0, 1000] -> [0, width]
        // same for height
        
        return new int[] {
            centralize(adjust(coord[0], 0, 1000, 0, width), width, 40),
            centralize(adjust(coord[1], 0, 1000, 0, height), height, 40)
        };
    }
}