package game.menus;

import game.visual.*;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

public class GraphChooser {
    public static void main(String[] args) {
        
        // WORK IN PROGRESS
        
        // JFileChooser chooser = new JFileChooser();
        // FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt files", "txt");
        // chooser.setFileFilter(filter);
        // int returnVal = chooser.showOpenDialog(null);
        // if(returnVal == JFileChooser.APPROVE_OPTION)
        //     System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
        //
        // chooser.setVisible(true);
        
        FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
        dialog.setFilenameFilter((File d, String name) -> name.endsWith(".txt"));
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String file = dialog.getFile();
        System.out.println(file + " chosen.");
    }
}