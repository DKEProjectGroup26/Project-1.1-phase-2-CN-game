package game.menus;

import game.menus.WindowManager;
import game.menus.Selection;
import game.visual.Board;
import game.visual.ColorPickerPlus;
import game.graph.Node;
import game.graph.solve.SNode;
import game.useful.Tools;

import java.util.ArrayList;

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
        
        if (!board.hasCompleteSolution()) window.addLabel("Sorry, couldn't calculate the chromatic number in time");
        else addChromaticInfo(window, manager, board);
        
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
        
        addChromaticInfo(window, manager, board);
        
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
    
    private static void addChromaticInfo(Selection window, WindowManager manager, Board board) {
        int colorsUsed = board.numberOfColors();
        if (!board.hasCompleteSolution()) {
            window.addLabel("Sorry, couldn't calculate the chromatic number in time");
            window.addLabel("You used " + colorsUsed + " colors");
            int flooded = board.flooded().nColors;
            if (flooded < colorsUsed) {
                window.addLabel("It was possible to solve the graph with " + flooded + " colors or fewer");
                window.addLabel("consider trying again.");
            } else
                window.addLabel("Couldn't find a solution with fewer than " + flooded + " colors");
        } else {
            var fromThis = board.solution(); // might hang, careful
            var cs = new ArrayList<Integer>();
            for (SNode node : fromThis.nodes) if (node.color >= 0 && !cs.contains(node.color)) cs.add(node.color);
            int mine = cs.size();
            
            var complete = board.completeSolution();
            if (complete == null) throw new RuntimeException("hasCompleteSolution -> null, bad");
            int real = complete.nColors;
            
            System.out.println("mine: " + mine);
            System.out.println("real: " + real);
            System.out.println("colorsUsed: " + colorsUsed);
            // there's a bug here where mine isn'
            
            window.addLabel("The chromatic number of the graph is " + real);
            
            if (board.allColored()) {
                if (colorsUsed == real)
                    window.addLabel("You managed to find the chromatic number, nice!");
                else {
                    window.addLabel("You used " + (colorsUsed - real) + " more colors than you had to,");
                    window.addLabel("you used " + colorsUsed + " colors while you only needed " + real + ".");
                    if (colorsUsed == real + 1) window.addLabel("It's only one color, you should keep trying.");
                    else window.addLabel("Maybe if you try again you can do better.");
                    addKeepTrying(window, manager, board);
                    addTryAgain(window, manager, board);
                }
            } else {
                if (mine <= real) {
                    window.addLabel("You can still solve the graph without starting over,");
                    if (colorsUsed < mine)
                        window.addLabel("you'll need to use " + (mine - colorsUsed) + " more colors.");
                    else window.addLabel("but you can't use any more colors.");
                    addKeepTrying(window, manager, board);
                } else {
                    // mine > real
                    window.addLabel("You won't be able to reach the chromatic number like this.");
                    if (colorsUsed > real) window.addLabel("You've already exceeded the chromatic number.");
                    window.addLabel("Try changing some colors.");
                    addKeepTrying(window, manager, board);
                    addTryAgain(window, manager, board);
                }
            }
        }
    }
    
    private static void addKeepTrying(Selection window, WindowManager manager, Board board) {
        var keepTrying = new JButton("Keep trying");
        keepTrying.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            manager.goBack(); // keep current state
        }});
        window.buttonPanel.add(keepTrying);
    }
    
    private static void addTryAgain(Selection window, WindowManager manager, Board board) {
        var tryAgain = new JButton("Try again");
        tryAgain.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            board.clear(); // can still undo to previous state
            manager.goBack();
        }});
        window.buttonPanel.add(tryAgain);
    }
}