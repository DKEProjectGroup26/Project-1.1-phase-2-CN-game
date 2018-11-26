package game.game_mode2;

import game.menus.*;
import game.graph.*;

import java.util.Hashtable;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Select {
    public static void start(WindowManager manager) {
        var menu = new Selection("Best in Time", manager);
        
        menu.addText("This game mode is for the stress-resistant people that are in our midst. The following rules apply here. You will be given an amount of time of your choosing to complete the graph. Here it is not necessary to do it perfectly, but of course you should try to do so. A clock will be counting down when the graph is created and you will be able to either show the clock or hide it. Whatever you prefer. Good luck!", 50);
        
        menu.addSep();
        
        menu.addLabel("Please choose an option");
        
        menu.addSpace();
        
        menu.addButton("Generate a random graph", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                var data = Generator.makeGraph();
                Play.start(data, manager);
            }
        });
        
        menu.addButton("Generate a random graph from parameters...", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Parameters.start(manager);
            }
        });
        
        menu.addButton("Load a graph from file...", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu.invisible();
                System.out.println("PLEASE IGNORE THE FOLLOWING WARNING IF ON MACOS");
                String path = Chooser.chooseFile();
                menu.visible();
                if (path != null) {
                    System.out.println("picked " + path);
                    var data = Reader.readGraph(path);
                    Play.start(data, manager);
                }
            }
        });
        
        menu.addBackButton();
        menu.addExitButton();
        manager.addWindow(menu);
    }
}
