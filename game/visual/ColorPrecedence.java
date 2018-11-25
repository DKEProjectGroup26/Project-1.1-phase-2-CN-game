package game.visual;

import game.useful.Tools;

import java.awt.Color;

public class ColorPrecedence {
    public static Color[] colors = {
        new Color(243, 28, 28), // red
        Tools.invertColor(new Color(243, 28, 28)),
        new Color(12, 20, 225), // blue
        Tools.invertColor(new Color(12, 20, 225)),
        new Color(16, 159, 25), // green
        Tools.invertColor(new Color(16, 159, 25)),
        new Color(0, 112, 225), // light blue
        Tools.invertColor(new Color(0, 112, 225)),
        new Color(153, 255, 153), // mint
        Tools.invertColor(new Color(153, 255, 153)),
        new Color(148, 89, 0), // brown
        Tools.invertColor(new Color(148, 89, 0))
    };
    
    public static int nColors() {
        return colors.length;
    }
    
    public static int numberOf(Color color) {
        for (int i = 0; i < colors.length; i++)
            if (color.equals(colors[i]))
                return i;
        
        System.err.println("error: you shouldn't have gotten here");
        System.exit(1);
        return -1; // formality
    }
}