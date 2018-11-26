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
        
        menu.addText("Explanation: Hello there player. In this gamemode your skill will immediately be put to the test. The goal is to color all the circles with the least amount of colors. But be aware, it is not allowed to color connected circles with the same color. Do not worry, the chromatic number will be shown to you, but remember that you cannot stop before you reached it. Good luck and may the chromatic gods be with you!", 50);
        
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
