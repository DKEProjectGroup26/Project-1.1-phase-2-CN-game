package game.visual;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ColorPickerPlus extends ColorPicker {
    public static void main(String[] args) {
        game.Main.main(null);
    }
    
    JButton plusButton;
    
    public ColorPickerPlus(int nColors, JPanel cc) {
        super(nColors, cc);
        
        plusButton = new JButton("+");
        plusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addColor();
            }
        });
        // remove existing buttons to put plusButton in front
        buttonSubPanel.remove(clear);
        buttonSubPanel.remove(undo);
        buttonSubPanel.remove(redo);
        // add plusButton
        buttonSubPanel.add(plusButton);
        // readd removed buttons
        buttonSubPanel.add(clear);
        buttonSubPanel.add(undo);
        buttonSubPanel.add(redo);
    }
    
    public void addColor() {
        
        if (colors.length >= ColorPrecedence.nColors()) {
            System.err.println("do something here, not enough colors");
            return;
            // also a limit for each graph?
        }
        
        var newColors = new Color[colors.length + 1];
        for (int i = 0; i < colors.length; i++)
            newColors[i] = colors[i];
        
        var newButtons = new ColorButton[buttons.length + 1];
        for (int i = 0; i < buttons.length; i++)
            newButtons[i] = buttons[i];
        
        var newColor = ColorPrecedence.colors[newColors.length - 1];
        newColors[newColors.length - 1] = newColor;
        colors = newColors;
        
        var newButton = new ColorButton(newColor, this);
        newButtons[newButtons.length - 1] = newButton;
        buttons = newButtons;
        
        remove(buttonSubPanel);
        add(newButton);
        add(buttonSubPanel);
        
        revalidate();
        repaint();
    }
}