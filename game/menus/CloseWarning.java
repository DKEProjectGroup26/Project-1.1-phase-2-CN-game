package game.menus;

import java.awt.event.*;

public class CloseWarning {
    public static void start(String text, WindowManager manager) {
        var window = new Selection("Warning", manager);
        
        window.addText(text, 25);
        window.addSpace();
        
        window.addButton("Yes", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.goBack(); // close this window
                manager.warningYes();
            }
        });
        
        window.addButton("No", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.goBack(); // close this window
                manager.lastWindow().enable();
            }
        });
        
        manager.addWindow(window, false); // false is to overlay over last window
    }
}