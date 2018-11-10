package game.visual;

import game.graph.*;

import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel {
    int width;
    int height;
    
    ColorPicker picker;
    History history;
    
    GraphData data;
    
    public Board(ColorPicker pp, GraphData dd) {
        this(1400, 800, pp, dd);
    }
    
    public Board(int w, int h, ColorPicker pp, GraphData dd) {
        super(); // does nothing
        
        width = w;
        height = h;
        
        picker = pp;
        picker.giveBoard(this);
        
        data = dd.shallowClone();
        
        data.setDisplaySize(width, height);
        data.makeCoords();
        data.makeLines();
        data.makeCircles();
        
        repaint();
        
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
        
        if (data.circles == null || data.lines == null)
            return;

        for (Line line : data.lines)
            line.draw(g, width, height);
        
        for (Circle circle : data.circles)
            circle.draw(g, width, height);
    }
    
    private void clicked(int x, int y) {
        boolean any = false;
        
        for (Circle circle : data.circles) {
            if (circle.wasMe(x, y, width, height)) {
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
        for (Circle circle : data.circles)
            circle.setColor(Color.WHITE, history, true);
        
        repaint();
    }
    
    public static void main(String[] args) {
        game.Main.main(null);
    }
}