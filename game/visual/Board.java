package game.visual;

import game.useful.Tools;
import game.graph.Node;
import game.graph.Edge;
import game.graph.GraphData;
import game.graph.solve.Graph;
import game.menus.WindowManager;
import game.menus.DoneMethods;
import game.menus.Selection;

import java.util.ArrayList;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

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
    
    public final Thread completeSolutionThread;
    private final Mutable<Graph> completeSolution;
    public boolean hasCompleteSolution() {return completeSolution.hasValue();}
    public Graph getCompleteSolution() {return completeSolution.getValue();}
    public ActionListener doneCall = null;
    // maybe add a static variable to stop all threads!
    public Graph completeSolution() {
        if (completeSolution.hasValue()) {
            System.out.println("completeSolution has value");
            return completeSolution.getValue();
        }

        // this really shouldn't happen (computer is slower than human)
        var window = new Selection("Waiting...", manager);
        window.addLabel("Waiting for a solution to the graph to be found");
        var skip = new JButton("Skip");
        skip.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            completeSolutionThread.stop(); // deprecated
            if (doneCall == null) {
                System.err.println("there should really be a doneCall here");
                System.exit(1);
            }
            doneCall.actionPerformed(null);
        }});
        window.add(skip);
        window.buttonPanel.add(skip);
        manager.addWindow(window, false);
        return null;
    }
    
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

        addMouseListener(new MouseAdapter() {public void mousePressed(MouseEvent e) {
            clicked(e.getX(), e.getY(), e.getButton());
        }});
        addMouseMotionListener(new MouseAdapter() {public void mouseMoved(MouseEvent e) {
            moved(e.getX(), e.getY());
        }});

        // open a new thread to compute the real solution
        completeSolution = new Mutable<>();
        final Graph graph = new Graph(data);
        completeSolutionThread = new Thread() {
            public void run() {
                graph.solve();
                completeSolution.setValue(graph.solution);
                System.out.println("DONE CALCULATING COMPLETE SOLUTION!!!");
                if (doneCall != null) doneCall.actionPerformed(null);
            }
        };
        completeSolutionThread.start();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // maybe useless
        
        for (Edge edge : data.edges) if (edge.style == Edge.DARK) edge.draw(g, size, border); // dark edges
        for (Node node : data.nodes) if (node.style == Node.DARK) node.draw(g, size, border); // dark nodes
        for (Edge edge : data.edges) if (edge.style != Edge.DARK) edge.draw(g, size, border); // light edges
        for (Node node : data.nodes) if (node.style != Node.DARK) node.draw(g, size, border); // light nodes
        
        // redraw currently highlighted node to bring it to front
        for (Node node : data.nodes) if (node.style == Node.HIGHLIGHTED) {
                node.draw(g, size, border);
                break;
        }
    }
    
    private void clicked(int x, int y, int button) {
        var node = data.whichNode(new Point(x, y), size, border);
        if (node == null) return;
        
        if (button == MouseEvent.BUTTON1) {// left click
            boolean changed = history.setColor(node, picker.storedColor);
            if (changed) {
                solution = null;
                if (gm3order != null) gm3Advance();
                
                boolean allColored = true;
                for (Node n : data.nodes) if (n.color.equals(Color.WHITE)) {
                    allColored = false;
                    break;
                }
                if (allColored) DoneMethods.completed(manager, this);
            }
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
    public void clearSolution() {
        solution = null;
        // start recalculating here
    }
    public Graph solution() {
        if (solution == null) {
            // recalculate if null
            System.out.println("recalculating solution");
            Graph graph = new Graph(data);
            graph.solve();
            solution = graph.solution;
        }
        return solution;
    }
    
    // hacky fix: this is only used by game mode 3, consider extending Board for this purpose
    // could be easy ((GM3Board) board).initiateGame() or something
    private ArrayList<Node> gm3order = null;
    public void initiateGameMode3() {
        picker.buttonSubPanel.remove(picker.clear);
        for (Edge edge : data.edges) edge.gm3 = true;
        var toadd = new ArrayList<Node>();
        for (Node node : data.nodes) {
            node.gm3status = Node.GM3_OFF;
            toadd.add(node);
        }
        
        gm3order = new ArrayList<>();
        for (int i = 0; i < data.nodes.length; i++)
            gm3order.add(toadd.remove((int) (Math.random() * toadd.size())));
        
        // either use some sort of await or check something every 10ms or something
        // or...
        gm3Advance();
    }
    
    public void gm3Advance() {
        // very inefficient, should store
        for (Node node : data.nodes) if (node.gm3status == Node.GM3_ON) {
            node.gm3status = Node.GM3_OFF;
        }
        
        if (gm3order.isEmpty()) return;
        
        gm3order.remove(0).gm3status = Node.GM3_ON;
        // for (Node node : data.nodes) node.style = Node.NORMAL;
        // repaint();
        moved(-10000, -10000);
    }
}