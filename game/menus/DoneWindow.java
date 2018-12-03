package game.menus;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DoneWindow {
    public static void start(boolean win, int hadSeconds, int tookSeconds, WindowManager manager) {
        var window = new Selection(win ? "You won!" : "You lost", manager);
        
        System.out.println(hadSeconds + " - " + tookSeconds);
        
        if (win) {
            window.addLabel("Congratulations! You solved the graph in " + tookSeconds + "s");
            window.addSpace(5);
            if (tookSeconds > hadSeconds)
                window.addLabel("You took " + (tookSeconds - hadSeconds) + "s longer than you had");
            else if (tookSeconds < hadSeconds)
                window.addLabel("You managed to solve the graph with " + (hadSeconds - tookSeconds) + "s left");
            else window.addLabel("You finished just in time!");
            // add hints used, etc.
        }
        
        window.addMainMenuButton();
        window.addExitButton();
        
        manager.addWindow(window, false);
    }
}