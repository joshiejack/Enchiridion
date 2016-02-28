package joshie.enchiridion.books.buttons;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.books.BookResourcePack;
import joshie.enchiridion.helpers.GsonHelper;
import joshie.enchiridion.json.BookIconTemplate;
import joshie.enchiridion.json.BookIconTemplate.Icons;
import joshie.lib.helpers.ResourceHelper;
import net.minecraft.client.Minecraft;

public class ButtonChangeIcon extends AbstractButton {
	public ButtonChangeIcon() {
		super("book");
	}
	
	private File last_directory = null;

	@Override
	public void performAction() {
		//PNG ONLY
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png");
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
            File directory = new File(new File(Enchiridion.root, "books"), "icons");
            if (!directory.exists() && !directory.mkdirs()) {
                throw new IllegalStateException("Couldn't create dir: " + directory);
            }
            
            File new_location = new File(directory, selectedFile.getName());
            try {
            	File iconJson = new File(directory, EnchiridionAPI.draw.getBookSaveName() + ".json");
                FileUtils.copyFile(selectedFile, new_location);
                BookIconTemplate template = new BookIconTemplate();
                template.parent = "enchiridion:item/book";
                template.textures = new Icons();
     		   	template.textures.layer0 = "enchiridion:items/" + FilenameUtils.removeExtension(selectedFile.getName());
     		   	Writer writer = new OutputStreamWriter(new FileOutputStream(iconJson), "UTF-8");
                writer.write(GsonHelper.getModifiedGson().toJson(template));;
                writer.close();
                ResourceHelper.refreshDomain(BookResourcePack.INSTANCE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            last_directory = selectedFile.getParentFile();
        }
	}
	
	@Override
	public boolean isLeftAligned() {
		return false;
	}
}
