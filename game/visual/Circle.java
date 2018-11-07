package game.visual;

import java.awt.*;

public class Circle {
    public int x;
    public int y;
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
}