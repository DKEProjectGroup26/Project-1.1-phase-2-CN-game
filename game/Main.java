package game;

import game.menus.*;

import java.awt.event.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        var menu = new Selection("Main menu");
        
        menu.addLabel("Welcome, please choose a game mode");
        
        menu.addSpace();
        
        menu.addButton("The Bitter End", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu.hide();
                game.game_mode1.Select.start(menu);
            }
        });
        
        menu.addButton("Best Upper Bound in a Fixed Time Frame", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu.hide();
                game.game_mode2.Select.start(menu);
            }
        });
        
        menu.addButton("Random Order", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu.hide();
                game.game_mode3.Select.start(menu);
            }
        });
        
        menu.addExitButton();
        menu.show();
    }
}