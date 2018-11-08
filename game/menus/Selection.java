package game.menus;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Selection {
    JFrame window;
    JPanel container;
    JPanel buttonPanel;
    WindowManager manager;
    
    // always called
    protected Selection(String title, int layout, WindowManager m) {
        window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        container = new JPanel();
        container.setLayout(new BoxLayout(container, layout));
        var padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        container.setBorder(padding);
        
        window.setResizable(false); // bad
        
        window.getContentPane().add(container);
        
        manager = m;
    }
    
    // called by Game
    public Selection(String title, WindowManager m) {
        this(title, BoxLayout.X_AXIS, m);
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        
        container.add(buttonPanel);
    }
    
    public void add(JComponent thing) {
        thing.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(thing);
        if (window.isVisible()) {
            window.pack();
            reposition();
        }
    }
    
    public void addSpace() {addSpace(15);}
    public void addSpace(int size) {
        var space = new JPanel();
        space.setPreferredSize(new Dimension(0, size));
        add(space);
    }
    
    public JLabel addLabel() {return addLabel("");}
    public JLabel addLabel(String text) {
        var label = new JLabel(text);
        add(label);
        return label;
    }
    
    public JLabel[] addText(String text, int charWidth) {
        String newText = "";
        int lines = 1;
        int charsPassed = 0;
        for (int i = 0; i < text.length(); i++) {
            charsPassed++;
            newText += text.charAt(i);
            if (text.charAt(i) == ' ' && charsPassed >= charWidth) {
                charsPassed = 0;
                newText += "\n";
                lines++;
            }
        }
        
        var labels = new JLabel[lines];
        String[] texts = newText.split("\n");
        
        if (texts.length != lines) {
            System.err.println("error: line number mismatch");
            System.exit(1);
        }
        
        for (int i = 0; i < lines; i++) {
            var tlab = new JLabel(texts[i]);
            labels[i] = tlab;
            add(tlab);
        }
        
        return labels;
    }
    
    public JSeparator addSep() {
        var sep = new JSeparator();
        add(sep);
        return sep; // formal, probably useless
    }
    
    public JButton addButton(String text, ActionListener action) {
        var button = new JButton(text);
        button.addActionListener(action);
        add(button);
        return button;
    }
    
    public void addBackButton() {addBackButton(null);}
    public void addBackButton(String warn) {
        addButton("Back", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.goBack(warn);
            }
        });
    }
    
    public void addMainMenuButton() {addMainMenuButton(null);}
    public void addMainMenuButton(String warn) {
        addButton("Main Menu", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.backToMain(warn);
            }
        });
    }
    
    public void addExitButton() {addExitButton(null);}
    public void addExitButton(String warn) {
        addButton("Exit", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.exit(warn);
            }
        });
    }
    
    public void setWarnOnClose() {
        // var self = this;
        //
        // window.addWindowListener(new WindowAdapter() {
        //     @Override
        //     public void windowClosing(WindowEvent e) {
        //         hide();
        //         CloseWarning.start(null, self);
        //     }
        // });
        // rewrite
    }
    
    public JSlider addSlider(int type, int min, int max, int val) {
        var slider = new JSlider(type, min, max, val);
        add(slider);
        return slider;
    }
    
    public void close() {
        window.dispose();
    }
    
    private void reposition() {
        // Dimension winSize = window.getSize();
        // double winWidth = winSize.getWidth();
        // double winHeight = winSize.getHeight();
        //
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // double screenWidth = screenSize.getWidth();
        // double screenHeight = screenSize.getHeight();
        //
        // int xPos = (int) (screenWidth - winWidth) / 2,
        //     yPos = (int) (screenHeight - winHeight) / 2;
        //
        // window.setLocation(xPos, yPos);
        
        window.setLocationRelativeTo(null); // better
    }
    
    public void show() {
        window.pack();
        reposition();
        window.setVisible(true);
    }
    public void hide() {window.setVisible(false);}
    
    public void enable() {
        window.setEnabled(true);
    }
    
    public void disable() {
        window.setEnabled(false);
    }
}