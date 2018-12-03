package game.game_mode2;

import game.useful.Tools;
import game.menus.WindowManager;
import game.menus.Selection;
import game.menus.SliderSet;
import game.graph.Generator;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Parameters {
    public static void start(WindowManager manager) {
        var menu = new Selection("The Bitter End", manager);
        
        var sliders = new SliderSet(true);
        menu.add(sliders);
        
        menu.addSpace();
        
        var okButton = menu.addButton("Ok", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                var data = Generator.makeGraph(sliders.randNodes(), sliders.randEdges());
                Play.start(data, sliders.randTime(), manager);
            }
        });
        
        menu.addBackButton();
        menu.addMainMenuButton();
        menu.addExitButton();
        manager.addWindow(menu);
    }
}