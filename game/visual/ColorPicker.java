package game.visual;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ColorPicker extends JPanel {
    
    public static int width = 50;
    
    // public static Color[] colorPrecedence = {
    //     Color.RED,
    //     Color.GREEN,
    //     Color.BLUE,
    //     Color.PINK,
    //     Color.YELLOW
    // };
    
    Color[] colors;
    Color storedColor;
    ColorButton[] buttons;
    
    public ColorPicker(int nColors) {
        super();
        
        if (nColors < 1) {
            System.err.println("not enough colors");
            System.exit(1);
        }
        
        colors = new Color[nColors];
        
        for (int i = 0; i < nColors; i++) {
            if (i >= ColorPrecedence.colors.length) {
                System.err.println("not enough colors");
                System.exit(1);
            }
            
            colors[i] = ColorPrecedence.colors[i];
        }
        
        storedColor = colors[0];
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        buttons = new ColorButton[nColors];
        
        int i = 0;
        for (Color color : colors) {
            var tcb = new ColorButton(color, this);
            add(tcb);
            buttons[i++] = tcb;
        }
        
        pickColor(colors[0]);
    }
    
    public void pickColor(Color color) {
        storedColor = color;
        for (ColorButton button : buttons) {
            if (button.color == color)
                button.select();
            else
                button.deselect();
        }
    }
}