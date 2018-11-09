package game.graph;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

public class Chooser {
    public static String chooseFile() {
        // A warning is printed on MacOS, IGNORE

        System.out.println("PLEASE IGNORE THE FOLLOWING WARNING IF ON MACOS");
        
        FileDialog dialog = new FileDialog((Frame) null, "Please choose a graph file", FileDialog.LOAD);
        dialog.setFilenameFilter((File d, String name) -> name.endsWith(".txt"));
        dialog.setVisible(true);
        
        String directory = dialog.getDirectory();
        String fileName = dialog.getFile();
        
        System.out.println("path precheck");
        System.out.println("isNull: " + (directory == null) + " | " + (fileName == null));
        
        if (directory == null || fileName == null)
            return null;
        
        System.out.println("returning " + directory + fileName);
        
        return directory + fileName;
    }
}