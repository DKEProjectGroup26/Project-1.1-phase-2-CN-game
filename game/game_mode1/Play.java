package game.game_mode1;

import game.menus.*;
import game.visual.*;

import game.graph.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Play {
    
    public static void main(String[] args) {
        start(0, null, null);
    }
    
    public static void start(int nodes, Selection goBackTo, Selection mainMenu) {
        
        var game = new Game("The Bitter End");
        
        var board = game.board;
        
        // game.board.testing(); // draw circles
        
        // board.drawGraph(10, new int[][] {{1, 2}, {2, 3}, {1, 3}});
        
        GraphData data = Reader.readGraph(17);
        System.out.println(data.nNodes);
        board.drawGraph(data.nNodes, data.edges);
        
        
        board.repaint();
        
        game.standardSetup(goBackTo, mainMenu);
        game.show();
    }
}