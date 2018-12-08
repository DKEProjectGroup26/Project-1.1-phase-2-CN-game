package game.menus;

import game.menus.WindowManager;
import game.menus.Selection;
import game.visual.Board;
import game.visual.ColorPickerPlus;
import game.graph.Node;
import game.useful.Tools;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BoxLayout;

public class DoneMethods {
    public static void confirmSurrender(WindowManager manager, Board board) {
        var window = new Selection("Sure?", manager);
        
        window.addLabel("The graph isn't finished,");
        window.addLabel("are you sure you wish to surrender?");
        
        var hPanel = new JPanel();
        hPanel.setLayout(new BoxLayout(hPanel, BoxLayout.X_AXIS));
        
        var yes = new JButton("Yes");
        yes.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            manager.goBack();
            manager.queue.last().enabled();
            surrender(manager, board);
        }});
        var no = new JButton("No");
        no.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            manager.goBack();
            manager.queue.last().enabled();
        }});
        
        window.buttonPanel.add(yes);
        window.buttonPanel.add(no);
        
        manager.addWindow(window, false);
    }
    
    public static void surrender(WindowManager manager, Board board) {
        var window = new Selection("You surrendered", manager);
        
        window.addLabel("You surrendered.");
        
        int nodesColored = 0;
        for (Node node : board.data.nodes) if (!node.color.equals(Color.WHITE)) nodesColored++;
        window.addLabel("Nodes colored: " + nodesColored);
        
        int timeTaken = board.picker.stopTimer();
        window.addLabel("Time taken: " + Tools.timeToString(timeTaken));
        
        if (board.gameMode == 2) {
            int timeGiven = ((ColorPickerPlus) board.picker).timeGiven;
            if (timeTaken > timeGiven) {
                window.addLabel("You took " + Tools.timeToString(timeTaken - timeGiven) + " more than the " + Tools.timeToString(timeGiven) + " you had.");
            } else if (timeTaken < timeGiven) {
                window.addLabel("You took " + Tools.timeToString(timeGiven - timeTaken) + " less than the " + Tools.timeToString(timeGiven) + " you had.");
            } else {
                window.addLabel("You took exactly the " + Tools.timeToString(timeGiven) + " you had.");
            }
            // window.addLabel("")
        }
        
        window.addMainMenuButton();
        window.addExitButton();
        
        manager.addWindow(window, false);
    }
    
    public static void finished(WindowManager manager, Board board) {
        // quick check
        for (Node check : board.data.nodes) if (check.color.equals(Color.WHITE)) {
            System.err.println("how did this even happen?");
            System.exit(1);
        }
        
        var window = new Selection("You finished", manager);
        
        window.addLabel("Congratulations!");
        window.addLabel("You finished the graph.");
        
        window.addMainMenuButton();
        window.addExitButton();
        
        manager.addWindow(window, false);
    }
}