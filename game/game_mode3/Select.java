package game.game_mode3;

import game.menus.*;

import java.util.Hashtable;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Select {
    public static void start(WindowManager manager) {
        var menu = new Selection("Random Order", manager);
        
        menu.addLabel("Please choose the number of nodes you wish to color");
        
        var nodeSlider = menu.addSlider(JSlider.HORIZONTAL, 3, 20, 8);
        nodeSlider.setMinorTickSpacing(1);
        nodeSlider.setPaintTicks(true);
        nodeSlider.setPaintLabels(true);
        
        var nodeLabels = new Hashtable<Integer, JLabel>();
        nodeLabels.put(3, new JLabel("3"));
        nodeLabels.put(5, new JLabel("5"));
        nodeLabels.put(10, new JLabel("10"));
        nodeLabels.put(15, new JLabel("15"));
        nodeLabels.put(20, new JLabel("20"));
        nodeSlider.setLabelTable(nodeLabels);
        
        var nodeLabel = menu.addLabel();
        
        var nodeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                nodeLabel.setText(nodeSlider.getValue() + " nodes");
            }
        };
        nodeListener.stateChanged(null);
        nodeSlider.addChangeListener(nodeListener);
        nodeSlider.setSnapToTicks(true);
        
        
        menu.addButton("Ok", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Play.start(, manager);
                // nodeSlider.getValue()
                // make a graphdata here
            }
        });
        
        menu.addBackButton();
        menu.addExitButton();
        manager.addWindow(menu);
    }
}