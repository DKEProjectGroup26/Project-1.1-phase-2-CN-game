package game.game_mode3;

import game.menus.WindowManager;
import game.menus.Selection;
import game.graph.Generator;
import game.graph.Chooser;
import game.graph.Reader;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Select {
    public static void start(WindowManager manager) {
        var menu = new Selection("Random Order", manager);
        
        menu.addText("Here your ability to think ahead will be tested. The way this game mode works is that the order of coloring the vertices will be decided for you . This means you will be forced to color the vertices in a specific way. Fortunately there is no need to complete the graph perfectly, but as always you should try to use the least amount of colors. Should you get stuck and run out of colors, then there is always an option to create a new color. Good luck!", 50);
        
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
