package joshie.enchiridion.books.buttons;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.features.FeatureImage;
import joshie.enchiridion.lib.EInfo;

public class ButtonInsertImage extends AbstractButton {
	public ButtonInsertImage() {
		super("picture");
	}
	
	private File last_directory = null;

	@Override
	public void performAction() {
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
            String foldername = EnchiridionAPI.draw.getBookSaveName();
            File directory = new File(new File(new File(Enchiridion.root, "books"), "images"), foldername);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new IllegalStateException("Couldn't create dir: " + directory);
            }
            
            File new_location = new File(directory, selectedFile.getName());
            try {
                FileUtils.copyFile(selectedFile, new_location);
                IPage current = EnchiridionAPI.draw.getPage();
                FeatureImage feature = new FeatureImage(EInfo.MODID + ":images/" + foldername + "/" + selectedFile.getName());
                BufferedImage buffered = ImageIO.read(new_location);
                int width = buffered.getWidth();
                int height = buffered.getHeight();
                EnchiridionAPI.draw.getPage().addFeature(feature, 0, 0, width, height, false, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            last_directory = selectedFile.getParentFile();
        }
	}
}
