package game.visual;

import game.useful.Tools;
import game.graph.Node;
import game.graph.Edge;
import game.graph.GraphData;
import game.graph.solve.Graph;
import game.menus.WindowManager;

import java.util.ArrayList;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Mutable<T> {
    private T value = null;
    public Mutable() {}
    public Mutable(T v) {value = v;}
    public boolean hasValue() {return value != null;}
    public T getValue() {return value;}
    public void setValue(T v) {value = v;}
}

public class Board extends JPanel {
    private Dimension size = new Dimension(900, 700); // adjust to screen, make resizeable
    
    private int border = 50;
    
    public final int gameMode;
    
    public WindowManager manager;
    
    public GraphData data;
    public final ColorPicker picker;
    public final History history;
    
    public final Mutable<Graph> completeSolution;
    
    public Board(GraphData d, ColorPicker p, int g) {
        super(); // does nothing
        
        gameMode = g;
        
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
        
        // open a new thread to compute the real solution
        completeSolution = new Mutable<>();
        final Graph graph = new Graph(data);
        var thread = new Thread() {
            public void run() {
                graph.solve();
                completeSolution.setValue(graph.solution);
                System.out.println("DONE CALCULATING COMPLETE SOLUTION!!!");
            }
        };
        thread.start();
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
        
        if (button == MouseEvent.BUTTON1) {// left click
            boolean changed = history.setColor(node, picker.storedColor);
            if (changed) solution = null;
        } else if (button == MouseEvent.BUTTON3) {// right click
            boolean changed = history.clearColor(node);
            if (changed) solution = null;
        }
        
        repaint();
    }
	
    private void moved(int x, int y) {
        var node = data.whichNode(new Point(x, y), size, border);
        
        if (node == null) {
            for (Node n : data.nodes) {
                n.style = Node.NORMAL;
                // hacky
                if (n.gm3status == Node.GM3_MY) n.gm3status = n.storedgm3status;
            }
            for (Edge e : data.edges) e.style = Edge.NORMAL;
        } else
            node.highlight(picker.highContrast);

        repaint();
    }
    
    public void removeColor(Color color) {
        boolean any = false;
        for (Node node : data.nodes) if (color.equals(node.color)) {
            history.deleteColor(node);
            any = true;
        }
        
        history.removeColor(color);
        repaint();
        
        if (any) solution = null;
    }
    
    // IMPORTANT: SHOULD BE nullED ANYTIME A NODE CHANGES COLOR
    private Graph solution = null;
    public void clearSolution() {solution = null;}
    public Graph solution() {
        if (solution == null) {
            System.out.println("recalculating solution");
            // recalculate if null
            
            System.out.println("TESTING DUMP:::GRAPHDATA");
            System.out.print("data colors: [");
            for (Node n : data.nodes) System.out.print(n.color + ", ");
            System.out.println("]");
            
            Graph graph = new Graph(data);
            
            System.out.println("TESTING DUMP:::GRAPH from DATA");
            System.out.print("colors: [");
            for (game.graph.solve.SNode node : graph.nodes) System.out.print(node.color + ", ");
            System.out.println("]");
            
            graph.solve();
            solution = graph.solution;
        }
        
        return solution;
    }
    
    // hacky fix: this is only used by game mode 3, consider extending Board for this purpose
    // could be easy ((GM3Board) board).initiateGame() or something
    public void initiateGameMode3() {
        for (Edge edge : data.edges) edge.gm3 = true;
        var toadd = new ArrayList<Node>();
        for (Node node : data.nodes) {
            node.gm3status = Node.GM3_OFF;
            toadd.add(node);
        }
        var order = new Node[data.nodes.length];
        for (int i = 0; i < data.nodes.length; i++)
            order[i] = toadd.remove((int) (Math.random() * toadd.size()));
        
        // probably need to hack node
        order[0].gm3status = Node.GM3_ON;
    }
}