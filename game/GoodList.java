package game;

import java.util.ArrayList;

public class GoodList<T> extends ArrayList<T> {
    public T first() {return get(0);}
    public T last() {return get(size() - 1);}
    public T pop() {return remove(size() - 1);}
    // add
    public T shift() {return remove(0);}
    public void unshift(T value) {add(0, value);}
}