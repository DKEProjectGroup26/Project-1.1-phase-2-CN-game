package game.visual;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ColorButton extends JButton {
    public int width;
    public int height;
    public Color color;
    private ColorPicker parent;
    
    private Border unselectedBorder;
    private Border selectedBorder;
    
    public ColorButton(Color cc, ColorPicker pp) {
        this("", 50, 50, cc, pp);
    }
    
    public ColorButton(String text, int ww, int hh, Color cc, ColorPicker pp) {
        super(text);
        
        width = ww;
        height = hh;
        
        color = cc;
        parent = pp;
        
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.pickColor(color);
            }
        });
        
        setBackground(color);
        setOpaque(true); // may be useless
        // setBorderPainted(false);
        setFocusPainted(false);
        
        selectedBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 5),
            BorderFactory.createLineBorder(color, 20)
        );
        
        unselectedBorder = BorderFactory.createLineBorder(color, 25);
        
        setBorder(unselectedBorder);
    }
    
    public void select() {
        setBorder(selectedBorder);
    }
    
    public void deselect() {
        setBorder(unselectedBorder);
    }
}