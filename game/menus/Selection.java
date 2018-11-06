package game.menus;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Selection {
    JFrame window;
    JPanel container;
    JPanel buttonPanel;
    
    // always called
    protected Selection(String title, int layout) {
        window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        container = new JPanel();
        container.setLayout(new BoxLayout(container, layout));
        var padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        container.setBorder(padding);
        
        window.setResizable(false); // bad
        
        window.getContentPane().add(container);
    }
    
    // called by Game
    public Selection(String title) {
        this(title, BoxLayout.X_AXIS);
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        
        container.add(buttonPanel);
    }
    
    protected Selection(String title, boolean isGame) {
        window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS)); // or X_AXIS
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
    
    // public JLabel[] addText(String text) {
        // 
    // }
    
    public void /*return sep?*/ addSep() {
        var sep = new JSeparator();
        add(sep);
    }
    
    public JButton addButton(String text, ActionListener action) {
        var button = new JButton(text);
        button.addActionListener(action);
        add(button);
        return button;
    }
    
    public void addBackButton(Selection goBackTo) {
        addButton("Back", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
                goBackTo.show();
            }
        });
    }
    
    public void addExitButton() {
        addButton("Exit", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    
    public void setWarnOnClose() {
        var self = this;
        
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                hide();
                CloseWarning.start(null, self);
            }
        });
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
}