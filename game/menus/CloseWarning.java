package game.menus;

import java.awt.event.*;

public class CloseWarning {
    public static void start(Selection ifYes, Selection ifNo) {
        var window = new Selection("Exit");
        
        window.addLabel("Are you sure you want to");
        window.addLabel("abandon the current game?");
        window.addSpace();
        
        window.addButton("Yes", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ifYes == null)
                    System.exit(0);
                
                window.close();
                ifYes.show();
            }
        });
        
        window.addButton("No", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ifNo == null)
                    System.exit(0); // useless
                
                window.close();
                ifNo.show();
            }
        });
        
        window.show();
    }
    
    public static void main(String[] args) {
        start(null, null);
    }
}