package game.visual;

import game.graph.Node;
import game.menus.WindowManager;
import game.menus.DoneMethods;
import game.menus.Selection;
import game.graph.solve.Graph;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.Timer;

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
    
    private final Color doneButtonColor;
    
    public ColorTimer timer = new ColorTimer(1000);
    
    public ColorPicker(int nColors, JPanel cc, WindowManager manager) {
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
            board.clear();
        }});
        buttonSubPanel.add(clear);
        
        done = new JButton("Done"); // maybe change to Give up / Done with normal / green background
        doneButtonColor = done.getBackground();
        done.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            // warn if giving up
            if (board.data.allColored()) DoneMethods.completed(board.manager, board);
            else DoneMethods.confirmSurrender(board.manager, board);
        }});
        buttonSubPanel.add(done);
        
        hint = new JButton("Hint");
        hint.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            var window = new Selection("", manager);
            Graph solution = null; // stays null if not found yet
            if (board.allColored()) {
                window.addLabel("You have colored all the nodes in the graph.");
                // however / and / -> done
                window.addLabel("add chromatic number info here");
                window.addBackButton();
            } else {
                if (!board.hasCompleteSolution()) {
                    window.setTitle("Sorry");
                    window.addLabel("Sorry, couldn't calculate the chromatic number in time.");
                    window.addLabel("However, an upper bound solution was found,");
                    window.addLabel("click on \"Color a node for me\" to color a node in the graph.");
                    window.addLabel("Node that this might or might not lead to the chromatic number!");
                    window.addButton("Color a node for me", new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            manager.goBack();
                            colorNextNode(board.flooded());
                        }
                    });
                } else {
                    solution = board.solution();
                    window.setTitle("Hint");
                    window.addLabel("--- add information here (chromatic number, etc.) ---");
                    window.addLabel("Click on \"Color a node for me\" to color a node in the graph.");
                    window.addLabel("This won't increase the current number of colors");
                    window.addLabel("you need to finish the graph.");
                
                    var from = solution;
                    window.addButton("Color a node for me", new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            manager.goBack();
                            colorNextNode(from);
                        }
                    });
                }
            
                window.addBackButton();
                var newHint = new JButton("Get another hint"); // no more hints functionality
                newHint.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
                    // new hint
                }});
                window.buttonPanel.add(newHint);
            }
            
            board.manager.addWindow(window, false);
        }});
        buttonSubPanel.add(hint);
        
        // AFTER TESTING, REMOVE SOLVE AND ADD FUNCTIONALITY TO DONE
        solve = new JButton("Solve");
        solve.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            // solve the graph and display solution
            System.out.println("TESTING DUMP:::DATA");
            System.out.println("colors:");
            for (Node node : board.data.nodes) System.out.println(node.color);
            var s = board.solution();
            for (int i = 0; i < s.nodes.length; i++)
                board.history.setColor(board.data.nodes[i], s.colorOrder[s.nodes[i].color], 2);
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
        manager.activeTimers.add(timer);
    }
    
    public void colorNextNode(Graph from) { // hint
        // most connected to colored nodes
        int mostConnectedIndex = -1;
        Node mostConnected = null;
        int mostConnections = 0;
        for (int i = 0; i < board.data.nodes.length; i++) {
            if (!board.data.nodes[i].color.equals(Color.WHITE)) continue;
            int connections = 0;
            for (Node my : board.data.nodes[i].myNodes) if (!my.color.equals(Color.WHITE))
                connections++;
            if (mostConnectedIndex < 0 || connections > mostConnections) {
                mostConnectedIndex = i;
                mostConnected = board.data.nodes[i];
                mostConnections = connections;
            }
        }
        // DIFFERENT FOR GAME MODE 3 @@@@@@@@@@@@@@@@@@@@@@@@@@
        
        if (mostConnected == null) throw new RuntimeException("graph is done but you get hint, bad");
        else board.history.setColor(mostConnected, from.colorOrder[from.nodes[mostConnectedIndex].color]);
    }
    
    public void checkDoneButton() {
        boolean allColored = true;
        for (Node node : board.data.nodes) if (node.color.equals(Color.WHITE)) {
            allColored = false;
            break;
        }
        if (allColored) done.setBackground(Color.GREEN);
        else done.setBackground(doneButtonColor);
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