package game;

import game.menus.*;

import java.awt.event.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        var manager = new WindowManager();
        var menu = new Selection("Main menu", manager);
        
        menu.addLabel("Welcome, please choose a game mode");
        
        menu.addSpace();
        
        menu.addButton("The Bitter End", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.game_mode1.Select.start(manager);
            }
        });
        
        menu.addButton("Best Upper Bound in a Fixed Time Frame", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.game_mode2.Select.start(manager);
            }
        });
        
        menu.addButton("Random Order", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.game_mode3.Select.start(manager);
            }
        });
        
        menu.addExitButton();
        manager.addWindow(menu);
    }
}