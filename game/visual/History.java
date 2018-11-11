package game.visual;

import java.util.ArrayList;

import java.awt.*;

public class History {
    Board board;
    GoodList past;
    GoodList future;
    
    public History(Board b) {
        board = b;
        past = new GoodList();
        future = new GoodList();
    }
    
    private void clearFuture() {
        future.clear();
    }
    
    private void backOne() {
        var tmp = past.pop();
        tmp.undo();
        future.unshift(tmp);
    }
    
    private void forwardOne() {
        var tmp = future.shift();
        tmp.redo();
        past.push(tmp);
    }
    
    public void add(Tuple tup) {
        clearFuture();
        past.push(tup);
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
}

class Tuple { 
    public final Circle who;
    public final Color from;
    public final Color to;
    
    public boolean cleared;
    
    public Tuple(Circle w, Color f, Color t) {this(w, f, t, false);}
    public Tuple(Circle w, Color f, Color t, boolean c) {
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
}