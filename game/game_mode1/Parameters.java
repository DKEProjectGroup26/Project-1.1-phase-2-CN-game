package game.game_mode1;

import game.Tools;
import game.menus.*;
import game.graph.*;

import java.util.Hashtable;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Parameters {
    public static void start(WindowManager manager) {
        var menu = new Selection("The Bitter End", manager);
        
        menu.addLabel("Please choose how many nodes you wish to color");
        
        menu.addSep();
        
        
        
        var minSlider = menu.addSlider(JSlider.HORIZONTAL, 3, 20, 5);
        minSlider.setMinorTickSpacing(1);
        minSlider.setPaintTicks(true);
        minSlider.setPaintLabels(true);
        
        var minLabels = new Hashtable<Integer, JLabel>();
        minLabels.put(3, new JLabel("3"));
        minLabels.put(5, new JLabel("5"));
        minLabels.put(10, new JLabel("10"));
        minLabels.put(15, new JLabel("15"));
        minLabels.put(20, new JLabel("20"));
        minSlider.setLabelTable(minLabels);
        
        
        var maxSlider = menu.addSlider(JSlider.HORIZONTAL, 3, 20, 10);
        maxSlider.setMinorTickSpacing(1);
        maxSlider.setPaintTicks(true);
        maxSlider.setPaintLabels(true);
        
        var maxLabels = new Hashtable<Integer, JLabel>();
        maxLabels.put(3, new JLabel("3"));
        maxLabels.put(5, new JLabel("5"));
        maxLabels.put(10, new JLabel("10"));
        maxLabels.put(15, new JLabel("15"));
        maxLabels.put(20, new JLabel("20"));
        maxSlider.setLabelTable(maxLabels);
        
        
        var label = menu.addLabel();
        
        var minListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                var min = minSlider.getValue();
                var max = maxSlider.getValue();
                if (min > max) {
                    maxSlider.setValue(min);
                    max = min;
                }
                
                setLabelText(label, min, max);
            }
        };
        minListener.stateChanged(null);
        minSlider.addChangeListener(minListener);
        minSlider.setSnapToTicks(true);
        
        var maxListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                var min = minSlider.getValue();
                var max = maxSlider.getValue();
                if (max < min) {
                    minSlider.setValue(max);
                    min = max;
                }
                
                setLabelText(label, min, max);
            }
        };
        maxListener.stateChanged(null);
        maxSlider.addChangeListener(maxListener);
        maxSlider.setSnapToTicks(true);
        
        
        
        menu.addButton("Ok", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int nNodes = Tools.randInt(minSlider.getValue(), maxSlider.getValue());
                var data = Generator.makeGraph(nNodes);
                Play.start(data, manager);
            }
        });
        
        menu.addSep();
        menu.addBackButton();
        menu.addExitButton();
        manager.addWindow(menu);
    }
    
    private static void setLabelText(JLabel label, int min, int max) {
        if (min == max)
            label.setText(min + " nodes");
        else
            label.setText(min + " - " + max + " nodes");
    }
    
    public static void main(String[] args) {
        start(new WindowManager());
    }
}