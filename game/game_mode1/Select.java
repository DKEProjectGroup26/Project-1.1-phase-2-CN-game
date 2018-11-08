package game.game_mode1;

import game.menus.*;

import java.util.Hashtable;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Select {
    public static void start(WindowManager manager) {
        var menu = new Selection("The Bitter End", manager);
        
        menu.addLabel("Please choose an option");
        
        menu.addSep();
        
        menu.addButton("Generate a random graph", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("generate random graph");
            }
        });
        
        menu.addButton("Generate a random graph from parameters...", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Parameters.start(manager);
            }
        });
        
        menu.addButton("Load a graph from file...", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("load from file");
            }
        });
        
        menu.addSep();
        
        menu.addBackButton();
        menu.addExitButton();
        manager.addWindow(menu);
    }
}