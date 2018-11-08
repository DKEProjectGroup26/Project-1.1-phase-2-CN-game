package game.game_mode1;

import game.menus.*;

import java.util.Hashtable;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Parameters {
    public static void start(WindowManager manager) {
        var menu = new Selection("The Bitter End", manager);
        
        menu.addLabel("Please choose the number of nodes you wish to color");
        
        menu.addSep();
        
        var slider = menu.addSlider(JSlider.HORIZONTAL, 3, 20, 8);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        
        var labels = new Hashtable<Integer, JLabel>();
        labels.put(3, new JLabel("3"));
        labels.put(5, new JLabel("5"));
        labels.put(10, new JLabel("10"));
        labels.put(15, new JLabel("15"));
        labels.put(20, new JLabel("20"));
        slider.setLabelTable(labels);
        
        var label = menu.addLabel();
        
        var listener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                label.setText(slider.getValue() + " nodes");
            }
        };
        listener.stateChanged(null);
        slider.addChangeListener(listener);
        slider.setSnapToTicks(true);
        
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