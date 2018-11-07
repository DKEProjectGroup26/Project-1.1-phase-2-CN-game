package game.visual;

import java.awt.*;

public class Line {
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
        g.setColor(color);
        g.drawLine(x0, y0, x1, y1);
    }
}