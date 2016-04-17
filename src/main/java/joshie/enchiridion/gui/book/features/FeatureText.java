package joshie.enchiridion.gui.book.features;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.helpers.MCClientHelper;
import joshie.enchiridion.util.ITextEditable;
import joshie.enchiridion.util.TextEditor;
import org.apache.commons.compress.utils.IOUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static joshie.enchiridion.util.ECreativeTab.enchiridion;

public class FeatureText extends FeatureAbstract implements ITextEditable {
    protected transient boolean readTemp = false;
    protected transient double cachedWidth = 0;
    public transient int wrap = 100; //Default wrap to 100 to avoid errors
    public String text = "";
    public float size = 1F;

    public FeatureText(){}
    public FeatureText(String text) {
        this.text = text;
    }

    @Override
    public FeatureText copy() {
        FeatureText text = new FeatureText(this.text);
        text.size = size;
        return text;
    }

    @Override
    public String getName() {
        return text;
    }

    @Override
    public void update(IFeatureProvider position) {
        super.update(position);
        cachedWidth = position.getWidth();
        wrap = Math.max(50, (int) (cachedWidth / size) + 4);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (text != null) {
            EnchiridionAPI.draw.drawSplitScaledString(TextEditor.INSTANCE.getText(this), position.getLeft(), position.getTop(), wrap, 0x555555, size);
        }
    }
    
    @Override
    public boolean getAndSetEditMode() {
        if (MCClientHelper.isShiftPressed()) {
            try {
                readTemp = true;

                File file = new File("enchiridion.temp.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }

                IOUtils.readFully(new FileInputStream("enchiridion.temp.txt"), text.getBytes());
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        } else {
            TextEditor.INSTANCE.setEditable(this);
            return true;
        }
    }
        
    @Override
    public void onDeselected() {
        if (readTemp) {
            byte[] encoded;
            try {
                encoded = IOUtils.toByteArray(new FileInputStream("enchiridion.temp.txt"));
                text = (new String(encoded, Charset.defaultCharset()));
                File file = new File("enchiridion.temp.txt");
                //file.delete();
                readTemp = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        TextEditor.INSTANCE.setEditable(null);
    }
    
    @Override
    public void keyTyped(char character, int key) {
        if (MCClientHelper.isShiftPressed()) {
            if (key == 78) {
                size = Math.min(15F, Math.max(0.5F, size + 0.1F));
                wrap = Math.max(50, (int) ((cachedWidth) / size) + 4);
                return;
            } else if (key == 74) {
                size = Math.min(15F, Math.max(0.5F, size - 0.1F));
                wrap = Math.max(50, (int) ((cachedWidth) / size) + 4);
                return;
            }
        }
    }
    
    @Override
    public String getTextField() {
        return text;
    }

    @Override
    public void setTextField(String text) {
        this.text = text;
    }
}
