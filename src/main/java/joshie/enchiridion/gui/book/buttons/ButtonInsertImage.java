package joshie.enchiridion.gui.book.buttons;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IPage;
import joshie.enchiridion.gui.book.features.FeatureImage;
import joshie.enchiridion.helpers.FileCopier;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.lib.EInfo;

public class ButtonInsertImage extends ButtonAbstract {
    public ButtonInsertImage() {
        super("picture");
    }

    @Override
    public void performAction() {
        File file = FileCopier.copyFileFromUser(FileHelper.getImageSaveDirectory());
        if (file != null) {
            try {
                String foldername = EnchiridionAPI.book.getBook().getSaveName();
                String modid = EnchiridionAPI.book.getBook().getModID();
                IPage current = EnchiridionAPI.book.getPage();
                String path = "";
                if (modid == null || modid.equals("")) {
                    path = EInfo.MODID + ":images/" + foldername + "/" + file.getName();
                } else path = modid + ":textures/books/images/" + file.getName();

                FeatureImage feature = new FeatureImage(EInfo.MODID + ":images/" + foldername + "/" + file.getName());
                BufferedImage buffered = ImageIO.read(file);
                int width = buffered.getWidth();
                int height = buffered.getHeight();
                EnchiridionAPI.book.getPage().addFeature(feature, 0, 0, width, height, false, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
