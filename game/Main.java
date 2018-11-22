package game;

import game.menus.*;

import java.awt.event.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // set the look and feel to nimbus
            // (consistent on mac and win)
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        var manager = new WindowManager();
        var menu = new Selection("Main menu", manager);
        
        menu.addText("Introduction: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent mi augue, dignissim et pellentesque et, feugiat et lorem. Donec ut vulputate mauris, in rhoncus orci. Suspendisse tristique ligula dictum, dignissim turpis et, ullamcorper tellus. Praesent elementum porttitor ullamcorper. Ut ac laoreet est. Fusce vulputate orci imperdiet tellus feugiat ornare. Maecenas imperdiet mi at sapien tempor, eget feugiat nunc suscipit. Nunc risus tellus, placerat ac viverra in, tincidunt non lorem.", 50);
        
        menu.addSep();
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
        
        menu.addSep();
        
        menu.addExitButton();
        manager.addWindow(menu);
    }
}