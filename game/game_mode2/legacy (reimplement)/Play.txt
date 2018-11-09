package game.game_mode2;

import game.menus.*;
import game.visual.*;
import game.graph.*;

import java.awt.event.*;
import javax.swing.*;

public class Play {
    public static void start(GraphData data, int seconds, WindowManager manager) {
        var game = new Game("Best in Time", 3, manager);
        
        game.standardSetup();
        manager.addWindow(game);
    }
}