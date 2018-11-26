package game.visual;

import game.useful.Tools;
import game.useful.GoodList;
import game.graph.Node;

import java.util.ArrayList;

import java.awt.*;
import javax.swing.*;

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
    
    public void setColor(Node node, Color newColor) {
        setColor(node, newColor, false);
    }
    public void setColor(Node node, Color newColor, boolean clear) {
        var blockers = node.blockers(newColor);
        if (blockers == null) {
            clearFuture();
            past.add(new Tuple(node, node.color, newColor, clear));
            node.color = newColor;
        
            updateButtons();
        } else {
            class Flash extends Thread {
                private final Node[] nodes;
                public Flash(Node[] n) {nodes = n;}
                public void run() {
                    for (int i = 0; i < 3; i++) {
                        flash(Node.FLASHING);
                        flash(Node.NORMAL);
                    }
                }
                private void flash(int style) {
                    for (Node n : nodes) n.style = style;
                    board.repaint();
                    try {Thread.sleep(70);} catch (InterruptedException e) {};
                }
            }
            new Flash(blockers).start();
        }
    }
    
    public void clearColor(Node node) {
        clearColor(node, false);
    }
    public void clearColor(Node node, boolean clear) {
        setColor(node, Node.baseColor, clear);
    }
    
    public void deleteColor(Node node) {
        node.color = Node.baseColor;
    }
    
    public void undo() {
        if (past.isEmpty())
            return;
        
        if (past.last().cleared) {
            while (!past.isEmpty() && past.last().cleared)
                backOne();
        } else
            backOne();
    }
    
    public void redo() {
        if (future.isEmpty())
            return;
        
        if (future.first().cleared) {
            while (!future.isEmpty() && future.first().cleared)
                forwardOne();
        } else
            forwardOne();
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