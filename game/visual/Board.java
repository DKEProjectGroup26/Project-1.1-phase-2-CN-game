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
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // maybe useless

        // darkened components first

        // dark edges
        for (Edge edge : data.edges)
            if (edge.style == Edge.DARK)
                edge.draw(g, size, border);

        // dark nodes
        for (Node node : data.nodes)
            if (node.style == Node.DARK)
                node.draw(g, size, border);

        // light edges
        for (Edge edge : data.edges)
            if (edge.style != Edge.DARK)
                edge.draw(g, size, border);

        // light nodes
        for (Node node : data.nodes)
            if (node.style != Node.DARK)
                node.draw(g, size, border);
    }
    
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
            for (Node n : data.nodes)
                n.style = Node.NORMAL;
        else
            node.highlight(picker.highContrast);

        repaint();
    }
    
    public void removeColor(Color color) {
        for (Node node : data.nodes) if (color.equals(node.color)) history.clearColor(node);
        history.removeColor(color);
        repaint();
    }
}