package game.game_mode2;

import game.menus.*;
import game.visual.*;
import game.graph.*;

import game.graph.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Play {
    public static void start(GraphData data, WindowManager manager) {
        var game = new Game("The Bitter End", ColorPrecedence.nColors() - 3, manager, true);
        
        var board = game.board;
        
        board.drawGraph(data);
        
        board.repaint();
        
        game.standardSetup();
        manager.addWindow(game);
    }
}