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
    
    private boolean selected = false;
    
    private Border unselectedBorder;
    private Border selectedBorder;
    
    public static ColorButton getNew(Color c, ColorPicker p) {
        var myLF = UIManager.getLookAndFeel();
        ColorButton button = null;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // make metal
            button = new ColorButton(c, p);
            UIManager.setLookAndFeel(myLF);
        } catch (Exception e) {
            System.err.println("this is bad");
            System.exit(1);
        }
        return button;
    }
    
    private ColorButton(Color cc, ColorPicker pp) {
        super();
        
        color = cc;
        parent = pp;
        
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.pickColor(color);
            }
        });
        
        setBackground(color);
        setOpaque(true); // may be useless
        setBorderPainted(false);
        setFocusPainted(false);
        
        unselectedBorder = BorderFactory.createLineBorder(color, 16);
        selectedBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(4, 20, 4, 4),
            BorderFactory.createLineBorder(color, 20)
        );
        
        setBorder(unselectedBorder);
    }
    
    public boolean isSelected() {return selected;}
    
    public void select() {
        if (!selected) {
            setBorder(selectedBorder);
            selected = true;
        }
    }
    
    public void deselect() {
        if (selected) {
            setBorder(unselectedBorder);
            selected = false;
        }
    }
}