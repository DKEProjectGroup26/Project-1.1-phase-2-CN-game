package game.game_mode2;

import game.menus.WindowManager;
import game.menus.Game;
import game.graph.GraphData;
import game.visual.ColorPrecedence;


public class Play {
    public static void start(GraphData data, int minutes, WindowManager manager) {
        // var game = new Game("The Bitter End", ColorPrecedence.nColors() - 3, manager, data, minutes * 60);
        var game = new Game("The Bitter End", 2, ColorPrecedence.nColors() - 3, manager, data, 5);
        
        game.standardSetup();
        manager.addWindow(game);
    }
}