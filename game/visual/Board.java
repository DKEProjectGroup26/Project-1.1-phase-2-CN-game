package game.visual;

import game.useful.Tools;
import game.graph.Node;
import game.graph.Edge;
import game.graph.GraphData;
import game.menus.WindowManager;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel {
    private Dimension size = new Dimension(900, 700); // adjust to screen, make resizeable
    
    private int border = 50;
    
    public WindowManager manager;
    
    public GraphData data;
    public final ColorPicker picker;
    public final History history;
    
    public Board(GraphData d, ColorPicker p) {
        super(); // does nothing
        
        data = d;
        data.makeCoords(this);
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
        
        // redraw currently highlighted node to bring it to front
        for (Node node : data.nodes) {
            if (node.style == Node.HIGHLIGHTED) {
                node.draw(g, size, border);
                break;
            }
        }
    }
    
    private void clicked(int x, int y, int button) {
        var node = data.whichNode(new Point(x, y), size, border);
        if (node == null)
            return;
        
        if (button == MouseEvent.BUTTON1) // left click
            history.setColor(node, picker.storedColor);
        else if (button == MouseEvent.BUTTON3) // right click
            history.clearColor(node);
        
        repaint();
    }
	
    private void moved(int x, int y) {
        var node = data.whichNode(new Point(x, y), size, border);
        
        if (node == null) {
            for (Node n : data.nodes) n.style = Node.NORMAL;
            for (Edge e : data.edges) e.style = Edge.NORMAL;
        } else
            node.highlight(picker.highContrast);

        repaint();
    }
    
    public void removeColor(Color color) {
        for (Node node : data.nodes) if (color.equals(node.color)) history.deleteColor(node);
        history.removeColor(color);
        repaint();
    }
}