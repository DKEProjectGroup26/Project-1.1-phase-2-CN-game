package game.menus;

import game.useful.GoodList;

public class WindowManager {
    public GoodList<Selection> queue = new GoodList<Selection>();
    private Integer activeWarning = null;
    
    public void addWindow(Selection window) {addWindow(window, true);}
    public void addWindow(Selection window, boolean hideLast) {
        // if warning, add force to front
        
        if (!queue.isEmpty()) {
            if (hideLast)
                queue.last().invisible();
            else
                queue.last().disabled();
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