package game.visual;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ColorPickerPlus extends ColorPicker {
    JButton minusButton;
    JButton plusButton;
    
    public ColorPickerPlus(int nColors, JPanel cc) {
        super(nColors, cc);
        
        minusButton = new JButton("-");
        minusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeColor();
            }
        });
        
        plusButton = new JButton("+");
        plusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addColor();
            }
        });
        // remove existing buttons to put plusButton in front
        for (JComponent c : actionComponents)
            buttonSubPanel.remove(c);
        
        // add color buttons
        buttonSubPanel.add(minusButton);
        buttonSubPanel.add(plusButton);
        
        // re-add removed buttons
        for (JComponent c : actionComponents)
            buttonSubPanel.add(c);
    }
    
    private void removeColor() {
        
        if (colors.length <= 1) {
            System.err.println("you shouldn't have gotten here");
            return;
        }
        
        board.removeColor(colors[colors.length - 1]);
        
        var newColors = new Color[colors.length - 1];
        for (int i = 0; i < colors.length - 1; i++)
            newColors[i] = colors[i];
        
        var newButtons = new ColorButton[buttons.length - 1];
        for (int i = 0; i < buttons.length - 1; i++)
            newButtons[i] = buttons[i];
        
        remove(buttons[buttons.length - 1]);
        
        colors = newColors;
        buttons = newButtons;
        
        revalidate(); // maybe useless
        repaint();
        
        updateButtons();
    }
    
    private void addColor() {
        
        if (colors.length >= ColorPrecedence.nColors()) {
            System.err.println("you shouldn't have gotten here");
            return;
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
        
        // var newButton = new ColorButton(newColor, this);
        var newButton = ColorButton.getNew(newColor, this);
        newButtons[newButtons.length - 1] = newButton;
        buttons = newButtons;
        
        remove(buttonSubPanel);
        add(newButton);
        add(buttonSubPanel);
        
        revalidate();
        repaint();
        
        updateButtons();
    }
    
    private void updateButtons() {
        boolean any = false;
        for (ColorButton cb : buttons) {
            if (cb.isSelected()) {
                any = true;
                break;
            }
        }
        
        if (!any)
            pickColor(buttons[buttons.length - 1].color);
        
        if (colors.length >= ColorPrecedence.nColors())
            plusButton.setEnabled(false);
        else
            plusButton.setEnabled(true);
        
        if (colors.length == 1)
            minusButton.setEnabled(false);
        else
            minusButton.setEnabled(true);
    }
}