package game.menus;

import game.visual.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends Selection {
    public JPanel subContainer;
    public Board board;
    public ColorPicker colorPicker;

    public Game(String title, int nColors, WindowManager manager) {
        super(title, BoxLayout.Y_AXIS, manager);
        // Y_AXIS for buttons below game field
        // X_AXIS for buttons to the right of game field
        
        subContainer = new JPanel();
        subContainer.setLayout(new BoxLayout(subContainer, BoxLayout.X_AXIS));

        colorPicker = new ColorPicker(nColors);
        board = new Board(colorPicker);
        
        subContainer.add(board);
        // space
        var tjp = new JPanel();
        tjp.setPreferredSize(new Dimension(10, 0));
        subContainer.add(tjp);
        subContainer.add(colorPicker);
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

        container.add(subContainer);
        container.add(buttonPanel);
    }
    
    public void standardSetup() {
        addSpace();
        String warn = "Do you want to abandon the current game?";
        addBackButton(warn);
        addMainMenuButton(warn);
        addExitButton(warn);
        setWarnOnClose();
    }
}