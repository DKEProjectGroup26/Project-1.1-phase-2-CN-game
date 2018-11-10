package game.game_mode3;

import game.menus.*;
import game.visual.*;
import game.graph.*;

import java.awt.event.*;
import javax.swing.*;

public class Play {
    public static void start(GraphData data, WindowManager manager) {
        
        var game = new Game("Random Order", 5, manager, data);
        
        game.standardSetup();
        manager.addWindow(game);
    }
}