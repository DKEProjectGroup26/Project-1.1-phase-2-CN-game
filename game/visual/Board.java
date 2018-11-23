package game.visual;

import game.Tools;
import game.graph.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel {
    private Dimension size = new Dimension(800, 600);
    private int border = 50;
    
    public GraphData data;
    public final ColorPicker picker;
    public final History history;
    
    public Board(GraphData d, ColorPicker p) {
        super(); // does nothing
        
        data = d;
        data.makeCoords();
        repaint();
        
        picker = p;
        picker.giveBoard(this);
        
        history = new History(this);
        
        setPreferredSize(size);
        setBackground(Color.black);
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                clicked(e.getX(), e.getY(), e.getButton());
            }
        });
		
        addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                moved(e.getX(), e.getY());
            }
        });
    }
    
        //     @Override
        //     public void paintComponent(Graphics g) {
        //         super.paintComponent(g);
        //
        //         if (data.circles == null || data.lines == null)
        //             return;
        //
        // // darkened components first
        //
        // // dark lines
        //         for (Line line : data.lines)
        //     if (line.drawStyle == Line.DARKER)
        //         line.draw(g, from, upto);
        //
        // // dark circles
        //         for (Circle circle : data.circles)
        //     if (circle.drawStyle == Circle.DARKER)
        //         circle.draw(g, from, upto);
        //
        // // light lines
        //         for (Line line : data.lines)
        //     if (line.drawStyle != Line.DARKER)
        //         line.draw(g, from, upto);
        //
        // // light circles
        //         for (Circle circle : data.circles)
        //     if (circle.drawStyle != Circle.DARKER)
        //         circle.draw(g, from, upto);
        //     }
    
    private void clicked(int x, int y, int button) {
        var node = data.whichNode(new Point(x, y), size, border);
        if (node == null)
            return;
        
        if (button == MouseEvent.BUTTON1) // left click
            history.setColor(node, picker.storedColor);
        else
            history.clearColor(node);
        
        repaint();
    }
	
    private void moved(int x, int y) {
        var node = data.whichNode(new Point(x, y), size, border);
        
        if (node == null)
            history.resetStyles();
        else
            node.highlight(picker.standout.isSelected());

        repaint();
    }
    
    public void removeColor(Color color) {
        for (Node node : data.nodes) if (color.equals(node.color)) history.clearColor(node);
        history.removeColor(color);
        repaint();
    }
}