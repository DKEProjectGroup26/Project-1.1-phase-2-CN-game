package game.game_mode1;

import game.menus.*;
import game.graph.*;

import java.util.Hashtable;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Select {
    public static void start(WindowManager manager) {
        var menu = new Selection("The Bitter End", manager);
        
        menu.addText("I see you are aware of your own skills. This game mode is considered the most difficult of the three. The rules are simple. You are required to color the circles as quickly as possible, but of course that is not all. Just like the title implies, you can not stop before you complete the graph perfectly. In other words, the amount of colors that you use should be the same as the chromatic number of the graph. We will help you by showing both of these numbers, but the rest is up to you. Good luck!", 50);
        
        menu.addSpace();
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
            // delegate this to windowmanager
            public void actionPerformed(ActionEvent e) {
                menu.invisible();
                String path = Chooser.chooseFile();
                menu.visible();
                if (path != null) {
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
