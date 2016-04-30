package joshie.enchiridion.gui.book.buttons;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.helpers.FileCopier;
import joshie.enchiridion.helpers.FileHelper;
import joshie.enchiridion.helpers.GsonHelper;
import joshie.enchiridion.json.BookIconTemplate;
import joshie.enchiridion.json.BookIconTemplate.Icons;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ButtonChangeIcon extends ButtonAbstract {
    public ButtonChangeIcon() {
        super("book");
    }
    
    public static void refreshResources() {
        if (EConfig.resourceReload) Minecraft.getMinecraft().scheduleResourcesRefresh();
    }

    @Override
    public void performAction() {
        File file = FileCopier.copyFileFromUser(FileHelper.getIconsDirectory());
        if (file != null) {
            try {
                File iconJson = FileHelper.getIconsJSONForBook(EnchiridionAPI.book.getBook());
                BookIconTemplate template = new BookIconTemplate();
                template.parent = "enchiridion:item/book";
                template.textures = new Icons();
                template.textures.layer0 = "enchiridion:items/" + FilenameUtils.removeExtension(file.getName());
                Writer writer = new OutputStreamWriter(new FileOutputStream(iconJson), "UTF-8");
                writer.write(GsonHelper.getModifiedGson().toJson(template));
                writer.close();
                
                //Reload the icons
                refreshResources();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isLeftAligned() {
        return false;
    }
}
