package game.menus;

import game.visual.*;
import game.graph.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends Selection {
    public JPanel subContainer;
    public Board board;
    public ColorPicker colorPicker;
    
    public Game(String title, int nColors, WindowManager manager, GraphData data) {
        this(title, nColors, manager, data, false);
    }
    public Game(String title, int nColors, WindowManager manager, GraphData data, boolean plus) {
        super(title, BoxLayout.Y_AXIS, manager);
        // Y_AXIS for buttons below game field
        // X_AXIS for buttons to the right of game field
        
        subContainer = new JPanel();
        subContainer.setLayout(new BoxLayout(subContainer, BoxLayout.X_AXIS));
        
        // space between board and picker
        var tjp = new JPanel();
        tjp.setPreferredSize(new Dimension(10, 0));
        // tjp.setOpaque
        
        colorPicker = plus ? new ColorPickerPlus(nColors, tjp) : new ColorPicker(nColors, tjp);
        board = new Board(colorPicker, data);
        
        subContainer.add(board);
        subContainer.add(tjp);
        subContainer.add(colorPicker);
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        
        container.add(subContainer);
        container.add(buttonPanel);
        
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        var closeListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manager.exit("Are you sure you want to abandon the current game?");
            }
        };
        window.addWindowListener(closeListener);
    }
    
    public void standardSetup() {
        addSpace();
        addBackWarnButton();
        addMainMenuWarnButton();
        addExitWarnButton();
    }
}