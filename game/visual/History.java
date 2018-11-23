package game.visual;

import game.Tools;
import game.graph.Node;

import java.util.ArrayList;

import java.awt.*;
import javax.swing.*;

public class History {
    private final Board board;
    private final GoodList past;
    private final GoodList future;
    
    public History(Board b) {
        board = b;
        past = new GoodList();
        future = new GoodList();
        updateButtons();
    }
    
    private void updateButtons() {
        var undoButton = board.picker.undo;
        var redoButton = board.picker.redo;
        
        if (past.empty())
            undoButton.setEnabled(false);
        else
            undoButton.setEnabled(true);
        
        if (future.empty())
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
        past.push(tmp);
        
        updateButtons();
    }
    
    public void setColor(Node node, Color newColor) {
        setColor(node, newColor, false);
    }
    public void setColor(Node node, Color newColor, boolean clear) {
        clearFuture();
        past.push(new Tuple(node, node.color, newColor, clear));
        node.setColor(newColor);
        
        updateButtons();
    }
    
    public void clearColor(Node node) {
        clearColor(node, false);
    }
    public void clearColor(Node node, boolean clear) {
        setColor(node, Node.baseColor, clear);
    }
    
    public void undo() {
        if (past.empty())
            return;
        
        if (past.last().cleared) {
            while (!past.empty() && past.last().cleared)
                backOne();
        } else
            backOne();
    }
    
    public void redo() {
        if (future.empty())
            return;
        
        if (future.first().cleared) {
            while (!future.empty() && future.first().cleared)
                forwardOne();
        } else
            forwardOne();
    }
    
    public void removeColor(Color color) {
        past.removeColor(color);
        future.removeColor(color);
        
        updateButtons();
    }
    
    public void resetStyles() {
        System.err.println("A LOT LEFT TO IMPLEMENT HERE (STYLES IN HISTORY)");
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
        who.setColor(from);
    }
    
    public void redo() {
        who.setColor(to);
    }
}

class GoodList extends ArrayList<Tuple> {
    public Tuple last() {
        return get(size() - 1);
    }
    
    public Tuple first() {
        return get(0);
    }
    
    public Tuple pop() {
        return remove(size() - 1);
    }
    
    public Tuple shift() {
        return remove(0);
    }
    
    public void push(Tuple tup) {
        add(tup);
    }
    
    public void unshift(Tuple tup) {
        add(0, tup);
    }
    
    public boolean empty() {
        return isEmpty();
    }
    
    public void removeColor(Color color) {
        for (int i = 0; i < size(); i++) {
            if (get(i).from.equals(color) || get(i).to.equals(color)) {
                remove(i);
                i--;
            }
        }
    }
}