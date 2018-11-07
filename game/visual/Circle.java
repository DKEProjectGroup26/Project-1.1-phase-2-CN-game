package game.visual;

import java.awt.*;

public class Circle {
    public int x;
    public int y;
    // x and y refer to the center of the circle (drawing is adjusted)
    public int diameter;
    public Color color;
    
    public Circle(int xx, int yy, int dd, Color cc) {
        x = xx;
        y = yy;
        
        // System.out.println(xx + " -> " + x);
        
        diameter = dd;
        color = cc;
    }
    
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(offset(x), offset(y), diameter, diameter);
    }
    
    private int offset(int coord) {
        return coord - (diameter / 2);
    }
    
    public boolean wasMe(int xx, int yy) {
        // System.out.println("dist " + distance(x, y, xx, yy));
        return distance(x, y, xx, yy) <= diameter / 2;
    }
    
    private static double distance(int x0, int y0, int x1, int y1) {
        return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
    }
    
    public void setColor(Color cc) {
        color = cc;
    }
    
    public static void main(String[] args) {
        System.out.println(new Circle(10, 10, 20, Color.WHITE).wasMe(10, 10));
    }
}