package game.menus;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class WarnButton extends JButton {
    public static final int defaultTime = 1000;
    
    private final String text;
    private final String warn;
    private final int time;
    private final ActionListener action;
    private Timer timer = null;
    
    public WarnButton(String tx, String wa, int ti, ActionListener ac) {
        super();
        text = tx;
        warn = wa;
        time = ti;
        action = ac;
        
        setText(text);
        
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (timer == null)
                    warn();
                else
                    action.actionPerformed(null);
            }
        });
    }
    
    private void warn() {
        setText(warn);
        
        timer = new Timer(time, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopWarn();
            }
        });
        
        timer.start();
    }
    
    private void stopWarn() {
        if (timer == null)
            System.err.println("you shouldn't have gotten here");
        
        setText(text);
        timer.stop();
        timer = null;
    }
}