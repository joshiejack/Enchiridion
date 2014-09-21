package joshie.enchiridion.wiki.mode;

import static joshie.enchiridion.wiki.WikiHelper.gui;
import static joshie.enchiridion.wiki.gui.GuiMain.button_id;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiTitles;
import joshie.enchiridion.wiki.mode.edit.EditMode;

public class SaveMode extends WikiMode {
    private static final SaveMode instance = new SaveMode();
    private static boolean isDirty;

    public static SaveMode getInstance() {
        return instance;
    }

    public static void markDirty() {
        isDirty = true;
    }

    @Override
    public List addButtons(List list) {
        list.add(new ButtonSwitchMode(button_id++, 900, 14, 1, 1, "edit", 2F, EditMode.getInstance()));
        return list;
    }

    @Override
    public void onSwitch() {
        gui.page.setEditMode(false);
        if (isDirty) {
            try {
                gui.page.getContent().refreshY();

                {
                    String path = gui.page.getPath();
                    File parent = new File(path).getParentFile();
                    if (!parent.exists() && !parent.mkdirs()) {
                        throw new IllegalStateException("Couldn't create dir: " + parent);
                    }
                    
                    Writer writer = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
                    writer.write(WikiHelper.getGson().toJson(gui.page.getContent()));
                    writer.close();
                }

                {
                    String path = Enchiridion.root + "/translations.json";
                    Writer writer = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
                    writer.write(WikiHelper.getGson().toJson(WikiTitles.instance()));
                    writer.close();
                }

                isDirty = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
