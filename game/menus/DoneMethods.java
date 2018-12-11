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
    
    private static void addTimeTaken(int timeTaken, Selection window, ColorPickerPlus picker) {
        int timeGiven = picker.timeGiven;
        if (timeTaken > timeGiven) window.addLabel("You took " + Tools.timeToString(timeTaken - timeGiven) + " more than the " + Tools.timeToString(timeGiven) + " you had.");
        else if (timeTaken < timeGiven) window.addLabel("You took " + Tools.timeToString(timeGiven - timeTaken) + " less than the " + Tools.timeToString(timeGiven) + " you had.");
        else window.addLabel("You took exactly the " + Tools.timeToString(timeGiven) + " you had.");
    }
    
    public static void surrender(WindowManager manager, Board board) {
        int timeTaken = board.picker.stopTimer();
        
        var window = new Selection("You surrendered", manager);
        
        window.addLabel("You surrendered.");
        
        int nodesColored = 0;
        for (Node node : board.data.nodes) if (!node.color.equals(Color.WHITE)) nodesColored++;
        
        window.addLabel("Nodes colored: " + nodesColored);
        window.addLabel("Time taken: " + Tools.timeToString(timeTaken));
        if (board.gameMode == 2) addTimeTaken(timeTaken, window, (ColorPickerPlus) board.picker);
        
        window.addMainMenuButton();
        window.addExitButton();
        
        manager.addWindow(window, false);
    }
    
    private static void finished(WindowManager manager, Board board) {
        // quick check
        for (Node check : board.data.nodes) if (check.color.equals(Color.WHITE)) {
            System.err.println("how did this even happen?");
            System.exit(1);
        }
        
        int timeTaken = board.picker.stopTimer();
        
        var window = new Selection("You finished", manager);
        
        window.addLabel("Congratulations!");
        window.addLabel("You finished the graph.");
        window.addLabel("Time taken: " + Tools.timeToString(timeTaken));
        if (board.gameMode == 2) addTimeTaken(timeTaken, window, (ColorPickerPlus) board.picker);
        
        window.addMainMenuButton();
        window.addExitButton();
        
        manager.addWindow(window, false);
    }
    
    private static void finalized(WindowManager manager, Board board) {
        // quick check (same as finished)
        for (Node check : board.data.nodes) if (check.color.equals(Color.WHITE)) {
            System.err.println("how did this even happen? (v2)");
            System.exit(1);
        }
        
        // similar to finished but tells you chromatic number and allows try again
        var mine = board.solution();
        var real = board.completeSolution();
        int timeTaken = board.picker.stopTimer();
        
        var window = new Selection("You finished coloring the graph", manager);
        window.addLabel("Congratulations!");
        window.addLabel("You finished the graph.");
        window.addLabel("Time taken: " + Tools.timeToString(timeTaken));
        if (board.gameMode == 2) addTimeTaken(timeTaken, window, (ColorPickerPlus) board.picker);
        
        // another quick check
        if (mine.nColors < real.nColors) {
            System.err.println("error: somehow you solved it with fewer colors than the chromatic number");
            System.exit(1);
        }
        
        // add different text depending on whether you got the chromatic number
        if (mine.nColors == real.nColors)
            window.addLabel("You managed to find the chromatic number (" + mine.nColors + "), nice!");
        else {
            window.addLabel("Unfortunately, you used more colors than you had to");
            window.addLabel("you used " + mine.nColors + " colors while you only needed " + real.nColors);
            switch (mine.nColors - real.nColors) {
                case 1: window.addLabel("but you only used one more color, you should try again.");
                break;
                case 2: window.addLabel("but you only used two more colors, consider trying again");
                break;
                case 3: window.addLabel("you used three more colors, maybe it would help to try again");
                break;
                default:
                window.addLabel("you overshot by " + (mine.nColors - real.nColors) + " colors");
                window.addLabel("maybe if you try again you can do better.");
                break;
            }
            
            var tryAgain = new JButton("Try again");
            tryAgain.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
                System.out.println("TRY AGAIN");
                // do something here
                System.exit(0);
            }});
            window.buttonPanel.add(tryAgain);
        }
        
        window.addMainMenuButton();
        window.addExitButton();
        manager.addWindow(window, false);
        // make all these windows do something on close
    }
    
    
    // generalize the waiter to all methods
    public static void completed(WindowManager manager, Board board) {
        var real = board.completeSolution();
        if (real == null) {
            System.out.println("real is null");
            // execute the rest after solution is found
            board.doneCall = new ActionListener() {public void actionPerformed(ActionEvent e) {
                manager.goBack();
                if (!board.hasCompleteSolution()) {
                    System.err.println("YOU SKIPPED");
                    finished(manager, board); // can't tell you much
                } else {
                    System.out.println("YOU'VE WAITED");
                    finalized(manager, board); // more stuff
                }
            }};
        } else {
            System.out.println("real solution given");
            finalized(manager, board);
        }
    }
}