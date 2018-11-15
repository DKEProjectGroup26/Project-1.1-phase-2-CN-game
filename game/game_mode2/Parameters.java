package game.game_mode2;

import game.menus.*;

import java.util.Hashtable;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Parameters {
    public static void start(WindowManager manager) {
        var menu = new Selection("Best Upper Bound", manager);
        
        menu.addLabel("Please choose the number of nodes you wish to color");
        
        menu.addSep();
        
        var sliders = menu.addDoubleSlider("nodes", JSlider.HORIZONTAL, 15, 50, 15, 30, 5);
        var minSlider = sliders[0];
        var maxSlider = sliders[1];
        
        menu.addButton("Ok", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Play.start(slider.getValue(), manager);
                // call or use randomizer
                // convert to GraphData
            }
        });
        
        menu.addSep();
        menu.addBackButton();
        menu.addExitButton();
        manager.addWindow(menu);
    }
}