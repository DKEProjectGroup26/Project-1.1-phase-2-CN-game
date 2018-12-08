package game.visual;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.Timer;

class BetterListener implements ActionListener {
    private final int time;
    public int elapsed = 0;
    public int overtime = 0;
    public boolean inOvertime = false;
    private JLabel timerLabel;
    public BetterListener(int t, JLabel l) {
        time = t;
        timerLabel = l;
    }
    public void actionPerformed(ActionEvent e) {
        if (inOvertime) overtime++;
        else elapsed++;
        int timeLeft = inOvertime ? overtime : time - elapsed;
        if (timeLeft <= 0) {
            inOvertime = true;
            timerLabel.setForeground(Color.RED);
        }
        int mins = timeLeft / 60;
        int secs = timeLeft % 60;
        String text;
        if (mins > 0) text = String.format("%s%d:%02d", inOvertime ? "+" : "-", mins, secs);
        else text = String.format("%s%ds", inOvertime ? "+" : "-", secs);
        timerLabel.setText("  " + text);
    }
}

public class ColorPickerPlus extends ColorPicker {
    JButton minusButton;
    JButton plusButton;
    JLabel timerLabel;
    private final int totalSeconds;
    private Timer timer;
    private BetterListener listener;
    
    public ColorPickerPlus(int nColors, JPanel cc, int seconds) {
        super(nColors, cc);
        
        minusButton = new JButton("-");
        minusButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            removeColor();
        }});
        plusButton = new JButton("+");
        plusButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            addColor();
        }});
        
        // remove existing buttons to put +/- in front
        for (JComponent c : actionComponents) buttonSubPanel.remove(c);
        
        totalSeconds = seconds;
        timerLabel = new JLabel();
        timer = new Timer(1000, null);
        listener = new BetterListener(seconds, timerLabel);
        listener.actionPerformed(null);
        timer.addActionListener(listener);
        timer.start();
        
        // add color buttons
        buttonSubPanel.add(minusButton);
        buttonSubPanel.add(plusButton);
        buttonSubPanel.add(timerLabel);
        
        // re-add removed buttons
        for (JComponent c : actionComponents)
            buttonSubPanel.add(c);
    }
    
    private void removeColor() {
        
        if (colors.length <= 1) {
            System.err.println("you shouldn't have gotten here");
            return;
        }
        
        board.removeColor(colors[colors.length - 1]);
        
        var newColors = new Color[colors.length - 1];
        for (int i = 0; i < colors.length - 1; i++)
            newColors[i] = colors[i];
        
        var newButtons = new ColorButton[buttons.length - 1];
        for (int i = 0; i < buttons.length - 1; i++)
            newButtons[i] = buttons[i];
        
        remove(buttons[buttons.length - 1]);
        
        colors = newColors;
        buttons = newButtons;
        
        revalidate(); // maybe useless
        repaint();
        
        updateButtons();
    }
    
    private void addColor() {
        if (colors.length >= ColorPrecedence.nColors()) {
            System.err.println("you shouldn't have gotten here 2");
            return;
        }
        
        var newColors = new Color[colors.length + 1];
        for (int i = 0; i < colors.length; i++)
            newColors[i] = colors[i];
        
        var newButtons = new ColorButton[buttons.length + 1];
        for (int i = 0; i < buttons.length; i++)
            newButtons[i] = buttons[i];
        
        var newColor = ColorPrecedence.colors[newColors.length - 1];
        newColors[newColors.length - 1] = newColor;
        colors = newColors;
        
        var newButton = ColorButton.getNew(newColor, this);
        newButtons[newButtons.length - 1] = newButton;
        buttons = newButtons;
        
        remove(buttonSubPanel);
        add(newButton);
        add(buttonSubPanel);
        
        revalidate();
        repaint();
        
        updateButtons();
    }
    
    private void updateButtons() {
        boolean any = false;
        for (ColorButton cb : buttons) if (cb.isSelected()) {
            any = true;
            break;
        }
        
        if (!any) pickColor(buttons[buttons.length - 1].color);
        
        if (colors.length >= ColorPrecedence.nColors()) plusButton.setEnabled(false);
        else plusButton.setEnabled(true);
        
        if (colors.length == 1) minusButton.setEnabled(false);
        else minusButton.setEnabled(true);
    }
    
    // public void gameEnd() {
        // System.out.println("timed gameEnd called");
        // int timeTaken;
        // if (listener.inOvertime) timeTaken = listener.overtime + totalSeconds;
        // else timeTaken = listener.elapsed;
        // timer.stop();
        // REPLACE true WITH WIN/LOSE STATE!!! ####################
        // DoneWindow.start(true, totalSeconds, timeTaken, board.manager);
    // }
    
    public int stopTimer() {
        int timeTaken;
        if (listener.inOvertime) timeTaken = listener.overtime + totalSeconds;
        else timeTaken = listener.elapsed;
        timer.stop();
        return timeTaken;
    }
}