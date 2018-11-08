package game.game_mode3;

import game.menus.*;
import game.visual.*;

import java.awt.event.*;
import javax.swing.*;

public class Play {
    public static void start(int nodes, Selection goBackTo, Selection mainMenu) {
        
        var game = new Game("Random Order", 0);
        
        game.standardSetup(goBackTo, mainMenu);
        game.show();
    }
}