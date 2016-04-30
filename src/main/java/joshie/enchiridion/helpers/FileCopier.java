package joshie.enchiridion.helpers;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class FileCopier {
    private static File last_directory = null;

    public static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }

    public static File getFileFromUser(File default_directory) {
        //Only allow pngs to be selected, force the window on top.
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg", "gif");
        JFileChooser fileChooser = new JFileChooser() {
            @Override
            protected JDialog createDialog(Component parent) throws HeadlessException {
                JDialog dialog = super.createDialog(parent);
                dialog.setAlwaysOnTop(true);
                return dialog;
            }
        };

        if (last_directory == null) {
            fileChooser.setCurrentDirectory(default_directory);
        } else {
            fileChooser.setCurrentDirectory(last_directory);
        }
        
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            last_directory = selectedFile.getParentFile();
            return selectedFile;
        }
        
        return null;
    }

    public static File copyFileFromUser(File directory) {
        return copyFileFromUser(directory, directory);
    }

    public static File copyFileFromUser(File directory, File default_directory) {
        File userFile = getFileFromUser(default_directory);
        if (userFile == null) return null;
        File newFile = new File(directory, userFile.getName());
        try {
            if (newFile.exists()) return newFile;
            FileUtils.copyFile(userFile, newFile); //Copy the file over
            return newFile;
        } catch (Exception e) { return null; }
    }
}
