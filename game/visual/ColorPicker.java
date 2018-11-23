package game.visual;

import game.graph.Node;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ColorPicker extends JPanel {
    Color[] colors;
    Color storedColor;
    ColorButton[] buttons;
    Board board = null;
    JPanel colorPanel;
    JPanel buttonSubPanel;
    
    JButton undo;
    JButton redo;
    JButton clear;
    JButton check;
    JButton done;
	JCheckBox highContrast;
    JComponent[] actionComponents;
    
    public ColorPicker(int nColors, JPanel cc) {
        super();
        
        if (nColors < 1) {
            System.err.println("error: not enough colors selected");
            System.exit(1);
        }
        
        colorPanel = cc;
        
        colors = new Color[nColors];
        
        for (int i = 0; i < nColors; i++) {
            if (i >= ColorPrecedence.colors.length) {
                System.err.println("error: too many colors selected");
                System.exit(1);
            }
            colors[i] = ColorPrecedence.colors[i];
        }
        
        storedColor = colors[0];
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        buttons = new ColorButton[nColors];
        
        int i = 0;
        for (Color color : colors) {
            var tcb = new ColorButton(color, this);
            add(tcb);
            buttons[i++] = tcb;
        }
        
        buttonSubPanel = new JPanel();
        buttonSubPanel.setLayout(new BoxLayout(buttonSubPanel, BoxLayout.Y_AXIS));
        
        undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.history.undo();
                board.repaint();
            }
        });
        buttonSubPanel.add(undo);
        
        redo = new JButton("Redo");
        redo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.history.redo();
                board.repaint();
            }
        });
        buttonSubPanel.add(redo);
        
        clear = new JButton("Clear");
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Node node : board.data.nodes)
                    board.history.clearColor(node, true);
                board.repaint();
            }
        });
        buttonSubPanel.add(clear);
        
        check = new JButton("Check");
        check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(board.data.isValid());
            }
        });
        buttonSubPanel.add(check);
        
        done = new JButton("Done");
        done.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("done clicked");
            }
        });
        buttonSubPanel.add(done);
		
		highContrast = new JCheckBox("stand-out");
		highContrast.setSelected(true);
		highContrast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		buttonSubPanel.add(highContrast);
        
        actionComponents = new JComponent[] {undo, redo, clear, check, done, highContrast};
        
        add(buttonSubPanel);
                
        pickColor(colors[0]);
    }
    
    public void giveBoard(Board bb) {
        board = bb;
    }
    
    public void pickColor(Color color) {
        storedColor = color;
        for (ColorButton button : buttons) {
            if (button.color == color)
                button.select();
            else
                button.deselect();
        }
        colorPanel.setBackground(color);
    }
	
	public static void main(String[] args) {
		game.Main.main(null);
	}
}