package game.menus;

import game.useful.GoodList;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class WindowManager {
    public GoodList<Selection> queue = new GoodList<Selection>();
    private Integer activeWarning = null;
    
    public void addWindow(Selection window) {addWindow(window, true);}
    public void addWindow(Selection window, boolean hideLast) {
        // hideLast == true => show over last window, back if closed, disable last window, always to front
        if (!queue.isEmpty()) {
            if (hideLast)
                queue.last().invisible();
            else {
                queue.last().disabled();
                window.setAlwaysOnTop(true);
                window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        goBack();
                        queue.last().enabled();
                    }
                });
            }
        }
        
        window.visible();
        queue.add(window);
    }
    
    public void goBack() {goBack(null);}
    public void goBack(String warn) {
        if (warn != null) {
            activeWarning = 1;
            CloseWarning.start(warn, this);
        } else {
            queue.pop().dispose();
            queue.last().visible();
        }
    }
    
    public void backToMain() {backToMain(null);}
    public void backToMain(String warn) {
        if (warn != null) {
            activeWarning = 2;
            CloseWarning.start(warn, this);
        } else {
            while (queue.size() > 1) queue.pop().dispose();
            queue.last().visible();
        }
    }
    
    public void exit() {
        queue.last().invisible();
        System.exit(0);
    }
    
    public void exit(String warn) {
        if (warn == null) exit(); // legacy support
        activeWarning = 3;
        CloseWarning.start(warn, this);
    }
    
    public void warningYes() {
        if (activeWarning == null) return;
        
        int aW = activeWarning;
        activeWarning = null;
        
        switch (aW) {
            case 1:
                goBack();
                break;
            case 2:
                backToMain();
                break;
            case 3:
                exit();
                break;
            default:
                System.err.println("error: warning type not found");
                System.exit(1);
                break;
        }
    }
}