package game.game_mode2;

import game.menus.*;
import game.visual.*;

import java.awt.event.*;
import javax.swing.*;

public class Play {
    public static void start(int nodes, int seconds, Selection goBackTo, Selection mainMenu) {
        
        var game = new Game("Best in Time");
        
        game.standardSetup(goBackTo, mainMenu);
        game.show();
    }
    
    public static void main(String[] args) {
        start(0, 0, null, null);
    }
}