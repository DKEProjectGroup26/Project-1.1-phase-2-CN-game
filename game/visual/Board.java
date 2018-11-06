package game.visual;

import java.awt.*;
import javax.swing.*;

public class Board extends JPanel {
    
    Circle[] circles;
    Line[] lines;
    
    public Board() {
        super(); // does nothing
        setPreferredSize(new Dimension(800, 600));
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
    
    public void testing() {
        Circle c0 = new Circle(100, 100, 50, Color.WHITE);
        Circle c1 = new Circle(200, 200, 20, Color.BLUE);
        Circle c2 = new Circle(50, 500, 30, Color.RED);
        Circle c3 = new Circle(700, 300, 100, Color.YELLOW);
        
        circles = new Circle[]{c0, c1, c2, c3};
        lines = new Line[0];
    }
    
    // public void loadGraph(Graph graph) {
        // circles = new Circle[graph.nodes.length];
        // lines = new Line[graph.edges.length];
    // }
}

class Circle {
    int x;
    int y;
    int diameter;
    Color color;
    
    public Circle(int xx, int yy, int dd, Color cc) {
        x = xx;
        y = yy;
        diameter = dd;
        color = cc;
    }
    
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, diameter, diameter);
    }
}

class Line {
    int x0;
    int y0;
    int x1;
    int y1;
    int thickness;
    Color color;
    
    public Line(int xx0, int yy0, int xx1, int yy1, int tt, Color cc) {
        x0 = xx0;
        y0 = yy0;
        x1 = xx1;
        y1 = yy1;
        thickness = tt;
        color = cc;
    }
    
    public void draw(Graphics g) {
        
    }
}