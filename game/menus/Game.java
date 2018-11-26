package game.menus;

import game.visual.*;
import game.graph.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends Selection {
    public Board board;
    public ColorPicker colorPicker;
    
    public Game(String title, int nColors, WindowManager manager, GraphData data) {
        this(title, nColors, manager, data, false);
    }
    public Game(String title, int nColors, WindowManager manager, GraphData data, boolean plus) {
        super(title, manager);
        // Y_AXIS for buttons below game field
        // X_AXIS for buttons to the right of game field
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        
        // space between board and picker
        var tjp = new JPanel();
        tjp.setPreferredSize(new Dimension(10, 0));
        // tjp.setOpaque
        
        colorPicker = plus ? new ColorPickerPlus(nColors, tjp) : new ColorPicker(nColors, tjp);
        board = new Board(data, colorPicker);
        
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