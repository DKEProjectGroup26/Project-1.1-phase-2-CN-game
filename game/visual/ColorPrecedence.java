package game.visual;

import java.awt.Color;

public class ColorPrecedence {
    public static Color[] colors = {
        new Color(243, 28, 28), // red
        new Color(12, 20, 225), // blue
        new Color(16, 159, 25), // green
        new Color(255, 204, 51), // yellow
        new Color(255, 102, 0), // orange
        new Color(0, 112, 225), // light blue
        new Color(132, 0, 110), // purple
        new Color(153, 255, 153), // mint
        new Color(249, 79, 243), // pink
        new Color(148, 89, 0), // brown
        new Color(30, 255, 233) // aqua
    };
    
    public static int nColors() {
        return colors.length;
    }
}