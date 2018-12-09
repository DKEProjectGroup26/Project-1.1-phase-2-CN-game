package game.visual;

import game.useful.Tools;
import game.useful.GoodList;
import game.graph.Node;

import java.util.ArrayList;

import java.awt.Color;

public class History {
    private final Board board;
    private final HistList past;
    private final HistList future;
    
    public History(Board b) {
        board = b;
        past = new HistList();
        future = new HistList();
        updateButtons();
    }
    
    private void updateButtons() {
        var undoButton = board.picker.undo;
        var redoButton = board.picker.redo;
        
        if (past.isEmpty())
            undoButton.setEnabled(false);
        else
            undoButton.setEnabled(true);
        
        if (future.isEmpty())
            redoButton.setEnabled(false);
        else
            redoButton.setEnabled(true);
    }
    
    private void clearFuture() {
        future.clear();
    }
    
    private void backOne() {
        var tmp = past.pop();
        tmp.undo();
        future.unshift(tmp);
        
        updateButtons();
    }
    
    private void forwardOne() {
        var tmp = future.shift();
        tmp.redo();
        past.add(tmp);
        
        updateButtons();
    }
    
    private class Flash extends Thread {
        private final Node[] nodes;
        public Flash(Node[] n) {nodes = n;}
        private int flashes = 3;
        public void run() {
            int[] styles = new int[nodes.length];
            for (int i = 0; i < styles.length; i++) styles[i] = nodes[i].style; // store original styles
            for (; flashes >= 0; flashes--) {
                for (Node n : nodes) n.style = Node.FLASHING_ON;
                paintAndWait();
                for (Node n : nodes) n.style = Node.FLASHING_OFF;
                paintAndWait();
            }
            for (int i = 0; i < nodes.length; i++) nodes[i].style = styles[i]; // reset original styles
            board.repaint();
        }
        private void paintAndWait() {
            board.repaint();
            try {Thread.sleep(70);} catch (InterruptedException e) {};
        }
        public void resetFlashes() {
            flashes = 3;
        }
    }
    private Flash flasher = null;
    
    public boolean setColor(Node node, Color newColor) {
        return setColor(node, newColor, false);
    }
    // returns true if color has changed
    public boolean setColor(Node node, Color newColor, boolean clear) {
        if (node.color.equals(newColor)) return false;
        // if same as going forward, go forward (implement!)
        var blockers = node.blockers(newColor);
        if (blockers == null) {
            clearFuture();
            past.add(new Tuple(node, node.color, newColor, clear));
            node.color = newColor;
            updateButtons();
            return true;
        } else {
            if (flasher != null && flasher.isAlive()) flasher.resetFlashes();
            else {
                flasher = new Flash(blockers);
                flasher.start();
            }
        }
        return false;
    }
    
    public boolean clearColor(Node node) {
        return clearColor(node, false);
    }
    public boolean clearColor(Node node, boolean clear) {
        return setColor(node, Node.baseColor, clear);
    }
    public boolean deleteColor(Node node) {
        node.color = Node.baseColor;
        return true;
    }
    
    public void undo() {
        if (past.isEmpty())
            return;
        
        if (past.last().cleared) {
            while (!past.isEmpty() && past.last().cleared)
                backOne();
        } else
            backOne();
        board.clearSolution();
    }
    
    public void redo() {
        if (future.isEmpty())
            return;
        
        if (future.first().cleared) {
            while (!future.isEmpty() && future.first().cleared)
                forwardOne();
        } else
            forwardOne();
        board.clearSolution();
    }
    
    public void removeColor(Color color) {
        past.removeColor(color);
        future.removeColor(color);
        
        updateButtons();
    }
}

class Tuple {
    public final Node who;
    public final Color from;
    public final Color to;
    
    public boolean cleared;
    
    public Tuple(Node w, Color f, Color t) {this(w, f, t, false);}
    public Tuple(Node w, Color f, Color t, boolean c) {
        who = w;
        from = f;
        to = t;
        cleared = c;
    }
    
    public void undo() {
        who.color = from;
    }
    
    public void redo() {
        who.color = to;
    }
}

class HistList extends GoodList<Tuple> {
    public void removeColor(Color color) {
        for (int i = 0; i < size(); i++) {
            if (get(i).from.equals(color) || get(i).to.equals(color)) {
                remove(i);
                i--;
            }
        }
    }
}