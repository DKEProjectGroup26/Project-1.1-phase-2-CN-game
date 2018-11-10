package game.game_mode1;

import game.menus.*;
import game.visual.*;
import game.graph.*;

import game.graph.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Play {
    public static void start(GraphData data, WindowManager manager) {
        var game = new Game("The Bitter End", ColorPrecedence.nColors() - 3, manager, data);
        
        var board = game.board;
        
        game.standardSetup();
        manager.addWindow(game);
    }
}