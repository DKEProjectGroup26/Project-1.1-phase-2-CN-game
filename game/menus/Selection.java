package game.menus;

import java.util.Hashtable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class Selection extends JFrame {
    JPanel container;
    JPanel mainPanel;
    public JPanel buttonPanel;
    WindowManager manager;
    
    public Selection(String title, WindowManager m) {
        super(title);
        
        manager = m;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        var padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        container.setBorder(padding);
        setContentPane(container);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        container.add(mainPanel);
        
        var space = new JPanel();
        space.setPreferredSize(new Dimension(0, 5));
        container.add(space);
        container.add(new JSeparator());
        space = new JPanel();
        space.setPreferredSize(new Dimension(0, 5));
        container.add(space);
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        container.add(buttonPanel);
    }
    
    public void add(JComponent thing) {
        thing.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(thing);
        if (isVisible()) {
            pack();
            setLocationRelativeTo(null);
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
    
    public void addSep() {
        addSpace(5);
        add(new JSeparator());
        addSpace(5);
    }
    
    public JButton addButton(String text, ActionListener action) {
        var button = new JButton(text);
        button.addActionListener(action);
        add(button);
        return button;
    }
    
    public JButton addActionButton(String text, ActionListener action) {
        var button = new JButton(text);
        button.addActionListener(action);
        buttonPanel.add(button);
        return button;
    }
    
    public WarnButton addWarnButton(String text, String warn, ActionListener action) {
        return addWarnButton(text, warn, WarnButton.defaultTime, action);
    }
    public WarnButton addWarnButton(String text, String warn, int time, ActionListener action) {
        var button = new WarnButton(text, warn, time, action);
        buttonPanel.add(button);
        return button;
    }
    
    public void addBackWarnButton() {
        addWarnButton("Back", "Click to confirm", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.goBack();
            }
        });
    }
    
    public void addMainMenuWarnButton() {
        addWarnButton("Main Menu", "Click to confirm", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.backToMain();
            }
        });
    }
    
    public void addExitWarnButton() {
        addWarnButton("Exit", "Click to confirm", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.exit();
            }
        });
    }
    
    public void addBackButton() {addBackButton(null);}
    public void addBackButton() {
        var button = addActionButton("Back", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.goBack(warn);
            }
        });
    }
    
    public void addMainMenuButton() {addMainMenuButton(null);}
    public void addMainMenuButton(String warn) {
        addActionButton("Main Menu", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.backToMain(warn);
            }
        });
    }
    
    public void addExitButton() {addExitButton(null);}
    public void addExitButton(String warn) {
        addActionButton("Exit", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manager.exit(warn);
            }
        });
    }
    
    public void visible() {
        pack();
        setLocationRelativeTo(null); // center
        setVisible(true);
    }
    public void invisible() {setVisible(false);}
    public void enabled() {setEnabled(true);}
    public void disabled() {setEnabled(false);}
}