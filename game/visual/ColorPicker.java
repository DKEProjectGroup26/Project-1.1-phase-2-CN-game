package game.visual;

import game.graph.Node;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;

// TESTING ##################################
import game.graph.solve.Graph;

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
    JButton done;
    JButton solve;
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
            // var tcb = new ColorButton(color, this);
            var tcb = ColorButton.getNew(color, this);
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
        
        done = new JButton("Done");
        done.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // add something here
                // VERY IMPORTANT, TRANSFER COLORS IN GRAPH FROM GRAPHDATA CONSTRUCTOR!!!
                System.out.println(new Graph(board.data).isSolved());
            }
        });
        buttonSubPanel.add(done);
        
        solve = new JButton("Solve");
        solve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // solve the graph and display solution
                Graph graph = new Graph(board.data);
                graph.solve();
                Graph s = graph.solution;
                for (int i = 0; i < s.nodes.length; i++)
                    board.data.nodes[i].color = s.colorOrder[s.nodes[i].color];
                board.repaint(); // update the board to the new colors
            }
        });
        buttonSubPanel.add(solve);
        
        var endGame = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameEnd();
            }
        };
        done.addActionListener(endGame);
        solve.addActionListener(endGame);
        
		highContrast = new JCheckBox("highlight");
		highContrast.setSelected(true);
		buttonSubPanel.add(highContrast);
        
        actionComponents = new JComponent[] {undo, redo, clear, done, solve, highContrast};
        
        add(buttonSubPanel);
                
        pickColor(colors[0]);
    }
    
    public void giveBoard(Board b) {board = b;}
    
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
    
    public void gameEnd() {
        System.out.println("regular gameEnd called");
    }
}