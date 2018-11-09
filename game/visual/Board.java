package game.visual;

import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel {
    
    public Circle[] circles;
    public Line[] lines;
    int width;
    int height;
    
    ColorPicker picker;
    History history;
    
    public Board(ColorPicker pp) {
        // this(800, 600);
        this(1400, 800, pp);
    }
    
    public Board(int w, int h, ColorPicker pp) {
        super(); // does nothing
        
        width = w;
        height = h;
        picker = pp;
        picker.giveBoard(this);
        history = new History(this);
        
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.black);
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                clicked(e.getX(), e.getY());
            }
        });
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (circles == null || lines == null)
            return;
        
        for (Line line : lines)
            line.draw(g);
        
        for (Circle circle : circles)
            circle.draw(g);
    }
    
    public void drawCircles(int nNodes, int[][] coords) {
        // coords should be adjusted

        circles = new Circle[nNodes];
        
        for (int i = 0; i < nNodes; i++) {
            int[] coord = coords[i];
            circles[i] = new Circle(coord[0], coord[1], 20, Color.WHITE);
        }
    }
    
    public void drawLines(int nNodes, int[][] edges, int[][] coords) {
        // coords should be adjusted
        
        lines = new Line[edges.length];
        
        // edges are 1-indexed
        for (int i = 0; i < edges.length; i++) {
            var edge = edges[i];

            int[] n0 = null;
            int[] n1 = null;

            for (int c = 0; c < nNodes; c++) {
                if (edge[0] == c + 1)
                    n0 = new int[] {coords[c][0], coords[c][1]};

                if (edge[1] == c + 1)
                    n1 = new int[] {coords[c][0], coords[c][1]};

                if (n0 != null && n1 != null)
                    break;
            }
            
            if (n0 == null || n1 == null)
                System.err.println("THIS IS BAD");
            else
                lines[i] = new Line(n0[0], n0[1], n1[0], n1[1], 5, Color.WHITE);
        }
    }
    
    public void drawGraph(int nNodes, int[][] edges) {
        
        // coordinates are ALWAYS in range [0, 1000]
        int[][] coords = Coordinator.getCoords(nNodes, edges);
        
        // adjustment
        for (int i = 0; i < coords.length; i++)
            coords[i] = adjust(coords[i]);
        
        drawLines(nNodes, edges, coords);
        
        drawCircles(nNodes, coords);
        
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
    
    private void clicked(int x, int y) {
        boolean any = false;
        
        for (Circle circle : circles) {
            if (circle.wasMe(x, y)) {
                circle.setColor(picker.storedColor, history);
                any = true;
            }
        }
        
        if (any)
            repaint();
    }
    
    public void undoColor() {
        if (history.empty())
            return;
        
        history.back();
        repaint();
    }
    
    public void clearColors() {
        for (Circle circle : circles)
            circle.setColor(Color.WHITE, history, true);
        
        repaint();
    }
}