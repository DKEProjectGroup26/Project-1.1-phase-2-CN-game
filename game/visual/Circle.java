package game.visual;

import game.Tools;

import java.util.ArrayList;

import java.awt.*;

public class Circle {
    // x and y refer to the center of the circle (drawing is adjusted)
    public double x;
    public double y;
    
    public double diameter;
    public int intDiameter;
    public Color color;
    
    public static double defaultDiameter = 0.03;
    
    public Circle(double[] xy, int w, int h, Color cc) {this(xy[0], xy[1], w, h, cc);}
    public Circle(double[] xy, double dd, int w, int h, Color cc) {this(xy[0], xy[1], dd, w, h, cc);}
    public Circle(double xx, double yy, int w, int h, Color cc) {this(xx, yy, defaultDiameter, w, h, cc);}
    public Circle(double xx, double yy, double dd, int width, int height, Color cc) {
        x = xx;
        y = yy;
        
        diameter = dd;
        intDiameter = (int) (((width + height) / 2) * diameter);
        
        color = cc;
    }
    
    public void draw(Graphics g, int width, int height) {
        int average = (width + height) / 2;
        int intDiameter = (int) (average * diameter);
        
        g.setColor(color);
        g.fillOval(
            (int) (width * x - intDiameter / 2),
            (int) (height * y - intDiameter / 2),
            intDiameter,
            intDiameter
        );
    }
    
    public boolean wasMe(double xx, double yy, int width, int height) {
        int myX = (int) (x * width);
        int myY = (int) (y * height);
        
        return Tools.euclidDist(xx, yy, myX, myY) <= intDiameter / 2;
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
    
    public static void main(String[] args) {
        game.Main.main(null);
    }
}