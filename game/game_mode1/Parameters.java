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
        
        var nodeSliders = menu.addDoubleSlider("nodes", JSlider.HORIZONTAL, 15, 50, 15, 30, 5);
        var minNodeSlider = nodeSliders[0];
        var maxNodeSlider = nodeSliders[1];
		
		menu.addSep();
		
		var edgeSliders = menu.addDoubleSlider("edges", JSlider.HORIZONTAL, 30, 180, 30, 50, 30);
		var minEdgeSlider = edgeSliders[0];
		var maxEdgeSlider = edgeSliders[1];
		minEdgeSlider.setMinorTickSpacing(5);
		maxEdgeSlider.setMinorTickSpacing(5);
		
		minNodeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				nodeSliderChanged(minNodeSlider, minEdgeSlider);
			}
		});
		
		maxNodeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				nodeSliderChanged(maxNodeSlider, maxEdgeSlider);
			}
		});
		
		minEdgeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				edgeSliderChanged(minEdgeSlider, minNodeSlider);
			}
		});
		
		maxEdgeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				edgeSliderChanged(maxEdgeSlider, maxNodeSlider);
			}
		});
        
        var okButton = menu.addButton("Ok", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int nNodes = Tools.randInt(minNodeSlider.getValue(), maxNodeSlider.getValue());
				int nEdges = Tools.randInt(minEdgeSlider.getValue(), maxEdgeSlider.getValue());
                var data = Generator.makeGraph(nNodes, nEdges);
                Play.start(data, manager);
            }
        });
        
        menu.addSep();
        menu.addBackButton();
        menu.addExitButton();
        manager.addWindow(menu);
    }
	
	private static void nodeSliderChanged(JSlider caller, JSlider change) {
		int value = caller.getValue();
		int minValue = value - 1;
		int maxValue = value * (value - 1) / 2;
		
		if (change.getValue() < minValue)
			change.setValue(minValue);
		
		if (change.getValue() > maxValue)
			change.setValue(maxValue);
	}
	
	private static void edgeSliderChanged(JSlider caller, JSlider change) {
		int value = caller.getValue();
		int minValue = (int) Math.round((1 + Math.sqrt(8 * value + 1)) / 2);
		int maxValue = value + 1;
		
		if (change.getValue() < minValue)
			change.setValue(minValue);
		
		if (change.getValue() > maxValue)
			change.setValue(maxValue);
	}
    
    public static void main(String[] args) {
        start(new WindowManager());
    }
}