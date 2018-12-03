package game.game_mode3;

import game.menus.WindowManager;
import game.menus.Game;
import game.graph.GraphData;

public class Play {
    public static void start(GraphData data, WindowManager manager) {
        var game = new Game("Random Order", 5, manager, data);
        
        game.standardSetup();
        manager.addWindow(game);
    }
}