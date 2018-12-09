package game.visual;

import game.graph.Node;
import game.menus.DoneMethods;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.Timer;

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
    JButton hint;
    JButton solve;
	JCheckBox highContrast;
    JComponent[] actionComponents;
    
    public ColorTimer timer = new ColorTimer(1000);
    
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
        clear.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            for (Node node : board.data.nodes)
                board.history.clearColor(node, true);
            board.repaint();
        }});
        buttonSubPanel.add(clear);
        
        done = new JButton("Done");
        done.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            // warn if giving up
            if (board.data.allColored()) {
                DoneMethods.finished(board.manager, board);
            } else {
                DoneMethods.confirmSurrender(board.manager, board);
            }
        }});
        buttonSubPanel.add(done);
        
        hint = new JButton("Hint");
        hint.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            var solution = board.solution();
            
            if (!board.completeSolution.hasValue()) {
                System.err.println("warning: complete solution hasn't been calculated yet");
                return;
            }
            
            System.out.println(solution.nColors + " / " + board.completeSolution.getValue().nColors);
        }});
        buttonSubPanel.add(hint);
        
        // AFTER TESTING, REMOVE SOLVE AND ADD FUNCTIONALITY TO DONE
        solve = new JButton("Solve");
        solve.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            // solve the graph and display solution
            var s = board.solution();
            for (int i = 0; i < s.nodes.length; i++)
                board.history.setColor(board.data.nodes[i], s.colorOrder[s.nodes[i].color], true);
            board.repaint();
        }});
        buttonSubPanel.add(solve);
        
		highContrast = new JCheckBox("highlight");
		highContrast.setSelected(true);
		buttonSubPanel.add(highContrast);
        
        actionComponents = new JComponent[] {undo, redo, clear, done, hint, solve, highContrast};
        
        add(buttonSubPanel);
                
        pickColor(colors[0]);
        
        timer.start();
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
    
    public int stopTimer() {
        timer.stop();
        return timer.iterations;
    }
}