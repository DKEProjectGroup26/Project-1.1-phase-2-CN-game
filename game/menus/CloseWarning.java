package game.menus;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CloseWarning {
    public static void start(String text, WindowManager manager) {
        var window = new Selection("Warning", manager);
        
        window.addText(text, 25);
        window.addSpace();
        
        window.addButton("Yes", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.goBack(); // close this window
                manager.warningYes(); // perform pending action
            }
        });
        
        window.addButton("No", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.goBack(); // close this window
                manager.queue.last().enabled(); // cancel
            }
        });
        
        manager.addWindow(window, false); // false is to overlay over last window
    }
}