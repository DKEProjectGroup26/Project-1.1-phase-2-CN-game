package game.menus;

import game.visual.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends Selection {
    public JPanel subContainer;
    public Board board;
    public ColorPicker colorPicker;

    public Game(String title, int nColors) {
        super(title, BoxLayout.Y_AXIS);
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
    
    public void standardSetup(Selection goBackTo, Selection mainMenu) {
        addSpace();
        addBackButton(goBackTo);
        addButton("Main Menu", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
                goBackTo.close();
                mainMenu.show();
            }
        });
        addExitButton();
        setWarnOnClose();
    }
}