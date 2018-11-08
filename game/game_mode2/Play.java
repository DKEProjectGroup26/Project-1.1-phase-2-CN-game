package game.game_mode2;

import game.menus.*;
import game.visual.*;

import java.awt.event.*;
import javax.swing.*;

public class Play {
    public static void start(int nodes, int seconds, WindowManager manager) {
        var game = new Game("Best in Time", 0, manager);
        
        game.standardSetup();
        manager.addWindow(game);
    }
}