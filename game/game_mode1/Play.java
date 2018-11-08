package game.game_mode1;

import game.menus.*;
import game.visual.*;

import game.graph.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Play {
    public static void start(int nodes, WindowManager manager) {
        var game = new Game("The Bitter End", ColorPrecedence.nColors(), manager);
        
        var board = game.board;
        
        GraphData data = Reader.readGraph(17);
        board.drawGraph(data.nNodes, data.edges);
        
        board.repaint();
        
        game.standardSetup();
        manager.addWindow(game);
    }
}