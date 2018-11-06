package game.menus;

import game.visual.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends Selection {
    public Board board;

    public Game(String title) {
        super(title, BoxLayout.Y_AXIS);
        // Y_AXIS for buttons below game field
        // X_AXIS for buttons to the right of game field
        
        board = new Board();
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

        container.add(board);
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