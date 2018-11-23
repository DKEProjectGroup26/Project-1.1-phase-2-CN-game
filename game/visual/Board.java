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
    Point from;
    Point upto;
    
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
        
        from = new Point(border, border);
        upto = new Point(
            from.x + width - border * 2,
            from.y + height - border * 2
        );
        
        picker = pp;
        picker.giveBoard(this);
        
        data = dd;
        
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
					clicked(new Point.Double(e.getX(), e.getY()), false);
				else if (e.getButton() == MouseEvent.BUTTON3) // right click
					clicked(new Point.Double(e.getX(), e.getY()), true);
            }
		});
		
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				moved(new Point.Double(e.getX(), e.getY()));
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
				line.draw(g, from, upto);
		
		// dark circles
        for (Circle circle : data.circles)
			if (circle.drawStyle == Circle.DARKER)
				circle.draw(g, from, upto);
		
		// light lines
        for (Line line : data.lines)
			if (line.drawStyle != Line.DARKER)
				line.draw(g, from, upto);
        
		// light circles
        for (Circle circle : data.circles)
			if (circle.drawStyle != Circle.DARKER)
				circle.draw(g, from, upto);
    }
    
    private void clicked(Point.Double p, boolean clear) {
        boolean any = false;
        
        for (Circle circle : data.circles) {
            if (circle.wasMe(p, from, upto)) {
                var color = clear ? Color.WHITE : picker.storedColor;
                
                if (circle.canColor(color)) {
                    circle.setColor(color, history);
                    any = true;
                }
            }
        }
        
        if (any)
            repaint();
    }
	
	private void moved(Point.Double p) {
		Circle wasThis = null;
		
		for (Circle circle : data.circles)
			if (circle.wasMe(p, from, upto, 0.01))
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