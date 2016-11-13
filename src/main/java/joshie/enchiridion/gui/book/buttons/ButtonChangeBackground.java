package joshie.enchiridion.gui.book.buttons;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.helpers.FileCopier;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.lib.EInfo;

import java.io.File;

public class ButtonChangeBackground extends ButtonAbstract {
    public ButtonChangeBackground() {
        super("background");
    }

    @Override
    public void performAction() {
        File file = FileCopier.copyFileFromUser(FileHelper.getImageSaveDirectory());
        if (file != null) {
            EnchiridionAPI.book.getBook().setBackgroundResource(EInfo.MODID + ":images/" + EnchiridionAPI.book.getBook().getSaveName() + "/" + file.getName());
        }
    }

    @Override
    public boolean isLeftAligned() {
        return false;
    }
}