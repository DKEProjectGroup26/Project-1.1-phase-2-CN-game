package game.game_mode3;

import game.menus.*;
import game.visual.*;

import java.awt.event.*;
import javax.swing.*;

public class Play {
    public static void start(int nodes, WindowManager manager) {
        
        var game = new Game("Random Order", 5, manager);
        
        game.standardSetup();
        manager.addWindow(game);
    }
}