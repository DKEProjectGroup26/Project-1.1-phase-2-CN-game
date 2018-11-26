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
    JPanel buttonPanel;
    WindowManager manager;
    
    public Selection(String title, WindowManager m) {
        super(title);
        
        manager = m;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // bad (or, allow for Game and stretch Board to fit)
        
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
    
    // warning functionality for buttons isn't used
    public void addBackButton() {addBackButton(null);}
    public void addBackButton(String warn) {
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
    
    private static Hashtable<Integer, JLabel> ints2labels(int[] labelPos) {
        var labels = new Hashtable<Integer, JLabel>();
        
        for (int p : labelPos)
            labels.put(p, new JLabel(String.valueOf(p)));
        
        return labels;
    }
    
    public JSlider addSlider(int type, int min, int max, int val) {
        var slider = new JSlider(type, min, max, val);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        add(slider);
        return slider;
    }
    public JSlider addSlider(int a, int b, int c, int d, Hashtable<Integer, JLabel> labels) {
        var slider = addSlider(a, b, c, d);
        slider.setLabelTable(labels);
        return slider;
    }
    public JSlider addSlider(int a, int b, int c, int d, int[] labelPos) {
        return addSlider(a, b, c, d, ints2labels(labelPos));
    }
    
    // make the bottom slider point upwards and only have one set of labels
    public JSlider[] addDoubleSlider(String what, int type, int min, int max, int val0, int val1, Hashtable<Integer, JLabel> labels) {
        var minS = addSlider(type, min, max, val0, labels);
        var maxS = addSlider(type, min, max, val1, labels);
        
        addSpace(5);
        var label = addLabel();
        addSpace(5);
        
        var minL = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                var minV = minS.getValue();
                
                if (maxS.getValue() < minV)
                    maxS.setValue(minV);
                
                var maxV = maxS.getValue();
                
                label.setText(minV == maxV ? minV + " " + what : minV + " - " + maxV + " " + what);
            }
        };
        
        var maxL = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                var maxV = maxS.getValue();
                
                if (minS.getValue() > maxV)
                    minS.setValue(maxV);
                
                var minV = minS.getValue();
                
                label.setText(minV == maxV ? minV + " " + what : minV + " - " + maxV + " " + what);
            }
        };
        
        minL.stateChanged(null);
        minS.addChangeListener(minL);
        maxS.addChangeListener(maxL);
        
        return new JSlider[] {minS, maxS};
    }
    public JSlider[] addDoubleSlider(String a, int b, int c, int d, int e, int f, int[] labelPos) {
        return addDoubleSlider(a, b, c, d, e, f, ints2labels(labelPos));
    }
    public JSlider[] addDoubleSlider(String a, int b, int min, int max, int e, int f, int labelSpacing) {
        var labelPos = new int[(max - min) / labelSpacing + 1];
        
        for (int i = 0; i < labelPos.length; i++)
            labelPos[i] = min + i * labelSpacing;
        
        return addDoubleSlider(a, b, min, max, e, f, labelPos);
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