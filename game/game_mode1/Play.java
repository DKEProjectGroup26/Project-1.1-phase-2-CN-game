package game.game_mode1;

import game.menus.*;
import game.visual.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Play {
    
    public static void main(String[] args) {
        start(0, null, null);
    }
    
    public static void start(int nodes, Selection goBackTo, Selection mainMenu) {
        
        var game = new Game("The Bitter End");
        
        game.board.testing(); // draw circles
        
        game.standardSetup(goBackTo, mainMenu);
        game.show();
    }
}