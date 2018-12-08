package game.game_mode1;

import game.menus.Game;
import game.menus.WindowManager;
import game.graph.GraphData;
import game.visual.ColorPrecedence;

public class Play {
    public static void start(GraphData data, WindowManager manager) {
        var game = new Game("The Bitter End", 1, ColorPrecedence.nColors(), manager, data);
        
        var board = game.board;
        
        game.standardSetup();
        manager.addWindow(game);
    }
}