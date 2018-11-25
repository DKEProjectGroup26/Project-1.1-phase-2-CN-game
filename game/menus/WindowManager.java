package game.menus;

import java.util.ArrayList;

public class WindowManager {
    public ArrayList<Selection> queue;
    private Integer activeWarning = null;
    
    public WindowManager() {
        queue = new ArrayList<Selection>();
    }
    
    public Selection lastWindow() {
        return queue.size() > 0 ? queue.get(queue.size() - 1) : null;
    }
    
    public void addWindow(Selection window) {addWindow(window, true);}
    public void addWindow(Selection window, boolean hideLast) {
        // if not hidelast, make last window inactive
        // and force the warning to front
        var last = lastWindow();
        if (last != null) {
            if (hideLast)
                last.hide();
            else
                last.disable();
        }
        
        window.show();
        queue.add(window);
    }
    
    private Selection pop() {
        var last = lastWindow();
        queue.remove(queue.size() - 1);
        return last;
    }
    
    public void goBack() {goBack(null);}
    public void goBack(String warn) {
        if (warn != null) {
            activeWarning = 1;
            CloseWarning.start(warn, this);
        } else {
            pop().close();
            lastWindow().show();
        }
    }
    
    public void backToMain() {backToMain(null);}
    public void backToMain(String warn) {
        if (warn != null) {
            activeWarning = 2;
            CloseWarning.start(warn, this);
        } else {
            while (queue.size() > 1)
                pop().close();
            
            lastWindow().show();
        }
    }
    
    public void exit() {
        lastWindow().hide();
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