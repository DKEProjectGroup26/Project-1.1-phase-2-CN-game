package game.visual;

import game.Tools;
import game.graph.*;

import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel {
	private final static int defaultWidth = 800;
	private final static int defaultHeight = 600;
    int width;
    int height;
    int border = 50;
    int fromX;
    int toX;
    int fromY;
    int toY;
    
    ColorPicker picker;
    History history;
    
    GraphData data;
    
    public Board(ColorPicker pp, GraphData dd) {
        this(defaultWidth, defaultHeight, pp, dd);
    }
    
    public Board(int w, int h, ColorPicker pp, GraphData dd) {
        super(); // does nothing
        
        width = w;
        height = h;
        
        fromX = fromY = border;
        toX = fromX + width - border * 2;
        toY = fromY + height - border * 2;
        
        picker = pp;
        picker.giveBoard(this);
        
        data = dd.shallowClone();
        
        data.setDisplaySize(width, height);
        data.makeCoords();
        data.makeLines();
        data.makeCircles();
        
        repaint();
        
        history = new History(this);
        
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.black);
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) // left click
					clicked(e.getX(), e.getY(), false);
				else if (e.getButton() == MouseEvent.BUTTON3) // right click
					clicked(e.getX(), e.getY(), true);
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
        super.paintComponent(g);
        
        if (data.circles == null || data.lines == null)
            return;
		
		// darkened components first
		
		// dark lines
        for (Line line : data.lines)
			if (line.drawStyle == Line.DARKER)
				line.draw(g, fromX, toX, fromY, toY);
		
		// dark circles
        for (Circle circle : data.circles)
			if (circle.drawStyle == Circle.DARKER)
				circle.draw(g, fromX, toX, fromY, toY);
		
		// light lines
        for (Line line : data.lines)
			if (line.drawStyle != Line.DARKER)
				line.draw(g, fromX, toX, fromY, toY);
        
		// light circles
        for (Circle circle : data.circles)
			if (circle.drawStyle != Circle.DARKER)
				circle.draw(g, fromX, toX, fromY, toY);
    }
    
    private void clicked(int x, int y, boolean clear) {
        boolean any = false;
        
        for (Circle circle : data.circles) {
            if (circle.wasMe(x, y, fromX, toX, fromY, toY)) {
                circle.setColor(clear ? Color.WHITE : picker.storedColor, history);
                any = true;
            }
        }
        
        if (any)
            repaint();
    }
	
	private void moved(int x, int y) {
		Circle wasThis = null;
		
		for (Circle circle : data.circles)
			if (circle.wasMe(x, y, fromX, toX, fromY, toY, 0.01))
				wasThis = circle;
		
		if (wasThis == null) {
			for (Circle circle : data.circles)
				circle.drawStyle = Circle.NORMAL;
			
			for (Line line : data.lines)
				line.drawStyle = Line.NORMAL;
		} else {
			wasThis.highlight(picker.standout.isSelected());
		}
		
		repaint();
	}
    
    public void undoColor() {
        history.undo();
        repaint();
    }
    
    public void redoColor() {
        history.redo();
        repaint();
    }
    
    public void clearColors() {
        for (Circle circle : data.circles)
            circle.setColor(Color.WHITE, history, true);
        
        repaint();
    }
    
    public void removeColor(Color color) {
        System.out.println("removing " + color);
        
        for (Circle circle : data.circles) {
            if (Tools.sameColor(color, circle.color))
                circle.setColor(Color.WHITE);
        }
        
        history.removeColor(color);
        
        repaint();
    }
    
    public static void main(String[] args) {
        game.Main.main(null);
    }
}