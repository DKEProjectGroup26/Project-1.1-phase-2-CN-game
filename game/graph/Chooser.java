package game.graph;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

public class Chooser {
    public static String chooseFile() {
        // A warning is printed on MacOS, IGNORE

        FileDialog dialog = new FileDialog((Frame) null, "Please choose a graph file", FileDialog.LOAD);
        dialog.setFilenameFilter((File d, String name) -> name.endsWith(".txt"));
        dialog.setVisible(true);
        
        String directory = dialog.getDirectory();
        String fileName = dialog.getFile();
        
        if (directory == null || fileName == null)
            return null;
        
        return directory + fileName;
    }
}