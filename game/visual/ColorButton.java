package game.visual;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ColorButton extends JButton {
    public static void main(String[] args) {
        game.Main.main(null);
    }
    
    public int width;
    public int height;
    public Color color;
    private ColorPicker parent;
    
    private boolean selected = false;
    
    private Border unselectedBorder;
    private Border selectedBorder;
    
    public ColorButton(Color cc, ColorPicker pp) {
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
        
        selectedBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 30, 5, 5),
            BorderFactory.createLineBorder(color, 20)
        );
        
        unselectedBorder = BorderFactory.createLineBorder(color, 25);
        
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