package game.game_mode3;

import game.menus.*;
import game.graph.*;

import java.util.Hashtable;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Select {
    public static void start(WindowManager manager) {
        var menu = new Selection("Random Order", manager);
        
        menu.addText("Explanation: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent mi augue, dignissim et pellentesque et, feugiat et lorem. Donec ut vulputate mauris, in rhoncus orci. Suspendisse tristique ligula dictum, dignissim turpis et, ullamcorper tellus. Praesent elementum porttitor ullamcorper. Ut ac laoreet est. Fusce vulputate orci imperdiet tellus feugiat ornare. Maecenas imperdiet mi at sapien tempor, eget feugiat nunc suscipit. Nunc risus tellus, placerat ac viverra in, tincidunt non lorem.", 50);
        
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