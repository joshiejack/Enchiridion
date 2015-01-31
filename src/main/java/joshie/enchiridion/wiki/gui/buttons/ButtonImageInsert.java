package joshie.enchiridion.wiki.gui.buttons;

import static joshie.enchiridion.wiki.WikiHelper.gui;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.elements.ElementImage;
import net.minecraft.client.resources.I18n;

import org.apache.commons.io.FileUtils;

public class ButtonImageInsert extends ButtonWikiEdit {
    public static File last_directory;

    public ButtonImageInsert(int id, int x, int y, Class clazz, List list) {
        super(id, x, y, clazz, list);
        this.displayString = I18n.format("enchiridion.insert", new Object[0], 2F);
    }

    @Override
    public void onClicked() {
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
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        } else {
            fileChooser.setCurrentDirectory(last_directory);
        }

        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            File directory = new File(WikiHelper.getPage().getPath()).getParentFile();
            File new_location = new File(directory, selectedFile.getName());
            try {
                FileUtils.copyFile(selectedFile, new_location);
                ElementImage img = new ElementImage().setPath(selectedFile.getName(), false);
                gui.page.add(img);
            } catch (Exception e) {
                e.printStackTrace();
            }

            last_directory = selectedFile.getParentFile();
        }
    }
}
