package game.game_mode2;

import game.menus.*;

import java.util.Hashtable;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Select {
    
    public static void main(String[] args) {
        start(null);
    }
    
    public static void start(Selection goBackTo) {
        var menu = new Selection("Best in Time");
        
        menu.addLabel("Please choose the number of nodes you wish to color...");
        
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
        
        menu.addSpace();

        menu.addLabel("...and the amount of time you wish to have");
        
        var timeSlider = menu.addSlider(JSlider.HORIZONTAL, 10, 300, 60);
        timeSlider.setMinorTickSpacing(10);
        timeSlider.setPaintTicks(true);
        timeSlider.setPaintLabels(true);
        
        var timeLabels = new Hashtable<Integer, JLabel>();
        timeLabels.put(10, new JLabel("10s"));
        timeLabels.put(60, new JLabel("1min"));
        timeLabels.put(120, new JLabel("2min"));
        timeLabels.put(180, new JLabel("3min"));
        timeLabels.put(240, new JLabel("4min"));
        timeLabels.put(300, new JLabel("5min"));
        timeSlider.setLabelTable(timeLabels);
        
        var timeLabel = menu.addLabel();
        
        var timeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                timeLabel.setText(showTime(timeSlider.getValue()));
            }
        };
        timeListener.stateChanged(null);
        timeSlider.addChangeListener(timeListener);
        timeSlider.setSnapToTicks(true);
        
        menu.addSpace();
        
        menu.addButton("Ok", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu.hide();
                Play.start(nodeSlider.getValue(), timeSlider.getValue(), menu, goBackTo);
            }
        });
        
        menu.addSep();
        menu.addLabel("Or, choose a graph file");
        menu.addButton("Choose file", null);
        menu.addSep();
        
        menu.addBackButton(goBackTo);
        menu.addExitButton();
        menu.show();
    }
    
    private static String showTime(int seconds) {
        if (seconds < 60)
            return seconds + "s";
        
        int minutes = seconds / 60;
        seconds %= 60;
        
        return minutes + "min " + (seconds == 0 ? "" : seconds + "s");
    }
}