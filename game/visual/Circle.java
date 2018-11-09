package game.visual;

import game.Tools;

import java.util.ArrayList;

import java.awt.*;

public class Circle {
    public int x;
    public int y;
    // x and y refer to the center of the circle (drawing is adjusted)
    public int diameter;
    public Color color;
    
    public Circle(int xx, int yy, Color cc) {this(xx, yy, 30, cc);}
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
        // add tolerance?
        return Tools.euclidDist(x, y, xx, yy) <= diameter / 2;
    }
    
    public void setColor(Color cc) {
        color = cc;
    }
    
    public void setColor(Color cc, History history) {setColor(cc, history, false);}
    public void setColor(Color cc, History history, boolean cleared) {
        if (cc != color) {
            history.add(this, color, cleared);
            color = cc;
        }
    }
}