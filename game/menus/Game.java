package game.menus;

import game.graph.GraphData;
import game.visual.Board;
import game.visual.ColorPicker;
import game.visual.ColorPickerPlus;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

public class Game extends Selection {
    public Board board;
    public ColorPicker colorPicker;
    
    public Game(String title, int nColors, WindowManager manager, GraphData data) {
        this(title, nColors, manager, data, -1);
    }
    public Game(String title, int nColors, WindowManager manager, GraphData data, int seconds) {
        // if seconds >= 0: plus
        super(title, manager);
        // Y_AXIS for buttons below game field
        // X_AXIS for buttons to the right of game field
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        
        // space between board and picker
        var tjp = new JPanel();
        tjp.setPreferredSize(new Dimension(10, 0));
        // tjp.setOpaque
        
        colorPicker = seconds >= 0 ? new ColorPickerPlus(nColors, tjp, seconds) : new ColorPicker(nColors, tjp);
        board = new Board(data, colorPicker);
        board.manager = manager;
        
        mainPanel.add(board);
        mainPanel.add(tjp);
        mainPanel.add(colorPicker);
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        var closeListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manager.exit("Are you sure you want to abandon the current game?");
            }
        };
        addWindowListener(closeListener);
    }
    
    public void standardSetup() {
        addSpace();
        addBackWarnButton();
        addMainMenuWarnButton();
        addExitWarnButton();
    }
}