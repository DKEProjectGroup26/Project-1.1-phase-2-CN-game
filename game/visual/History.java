package game.visual;

import java.util.ArrayList;

import java.awt.*;

public class History {
    Board board;
    ArrayList<Tuple> history;
    
    public History(Board bb) {
        board = bb;
        history = new ArrayList<Tuple>();
    }
    
    public void add(Tuple tup) {
        history.add(tup);
    }
    
    public void add(Circle w, Color f) {
        history.add(new Tuple(w, f));
    }
    
    public void add(Circle w, Color f, boolean c) {
        history.add(new Tuple(w, f, c));
    }
    
    public void back() {
        if (empty())
            return;
        
        if (last().cleared) {
            revertClear();
            return;
        }
        
        pop().revert();
    }
    
    private void revertClear() {
        while (!empty() && last().cleared)
            pop().revert();
    }
    
    public boolean empty() {
        return history.isEmpty();
    }
    
    public Tuple last() {
        return history.get(history.size() - 1);
    }
    
    public Tuple pop() {
        var last = last();
        history.remove(history.size() - 1);
        return last;
    }
}

class Tuple { 
    public final Circle who;
    public final Color from;
    
    public boolean cleared = false;
    
    public Tuple(Circle w, Color f) {
        who = w;
        from = f;
    }
    
    public Tuple(Circle w, Color f, boolean c) {
        who = w;
        from = f;
        cleared = c;
    }
    
    public void justCleared() {
        cleared = true;
    }
    
    public void revert() {
        who.setColor(from);
    }
} 