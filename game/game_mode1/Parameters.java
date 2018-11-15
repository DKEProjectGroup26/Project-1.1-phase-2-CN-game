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
        
        var sliders = menu.addDoubleSlider("nodes", JSlider.HORIZONTAL, 15, 50, 15, 30, 5);
        var minSlider = sliders[0];
        var maxSlider = sliders[1];
        
        var okButton = menu.addButton("Ok", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int nNodes = Tools.randInt(minSlider.getValue(), maxSlider.getValue());
                var data = Generator.makeGraph(nNodes);
                Play.start(data, manager);
            }
        });
        
        // maxSlider.addChangeListener(new ChangeListener() {
        //     public void stateChanged(ChangeEvent e) {
        //         var maxV = maxSlider.getValue();
        //         if (maxV > 40)
        //             okButton.setText("Sure you want so many?");
        //         else
        //             okButton.setText("Ok");
        //     }
        // });
        // warns you if you choose a lot of nodes (to be reimplemented as a jlabel or something)
        
        menu.addSep();
        menu.addBackButton();
        menu.addExitButton();
        manager.addWindow(menu);
    }
    
    public static void main(String[] args) {
        start(new WindowManager());
    }
}