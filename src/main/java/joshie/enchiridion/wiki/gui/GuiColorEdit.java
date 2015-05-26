package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.drawRect;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledText;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.WikiHelper.getIntFromMouse;
import static joshie.enchiridion.wiki.WikiHelper.isEditMode;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;
import joshie.enchiridion.EConfig;
import joshie.enchiridion.util.IColorSelectable;
import joshie.enchiridion.util.IGuiDisablesMenu;
import joshie.enchiridion.util.ITextEditable;
import joshie.enchiridion.wiki.WikiHelper;

public class GuiColorEdit extends GuiExtension implements ITextEditable, IGuiDisablesMenu {
    public static IColorSelectable selectable = null;
    private static String hex = "";

    public static void select(IColorSelectable selectable) {
        /** Clear all history when selecting this gui **/
        WikiHelper.clearEditGUIs();
        GuiColorEdit.selectable = selectable;
    }

    public static void clear() {
        GuiColorEdit.selectable = null;
        GuiColorEdit.hex = "";
    }

    @Override
    public void draw() {
        if (selectable != null) {
            //Draw the Search box
            drawScaledTexture(texture, 10, 1, 0, 147, 254, 39, 1F);
            drawScaledTexture(texture, 126, 1, 100, 147, 154, 39, 1F);
            drawScaledText(2F, GuiTextEdit.getText(this, hex), 19, 13, 0xFFFFFFFF);

            int index = 0;
            for (int y = 0; y < 20; y++) {
                for (int x = 0; x < 7; x++) {
                    String color = EConfig.getColor(index);
                    if (color == null) continue;
                    drawRect(20 + (x * 34), 50 + (y * 34), 50 + (x * 34), 80 + (y * 34), (int) Long.parseLong(color, 16));

                    index++;
                }
            }
        }
    }

    @Override
    public void clicked(int button) {
        if (selectable != null) {
            if (button == 0) {
                //Draw Colour selection boxes
                int index = 0;
                for (int y = 0; y < 20; y++) {
                    for (int x = 0; x < 7; x++) {
                        String color = EConfig.getColor(index);
                        if (color == null) continue;
                        if (getIntFromMouse(20 + (x * 34), 50 + (x * 34), 50 + (y * 34), 80 + (y * 34), 0, 1) == 1) {
                            setText(color + "\n");
                        }

                        index++;
                    }
                }

                if (isEditMode()) {
                    if (getIntFromMouse(0, 299, 0, 45, 0, 1) == 1) {
                        GuiTextEdit.select(this);
                    }
                }
            }

        }
    }

    @Override
    public void setText(String text) {
        if (text.contains("\n")) {
            //If enter has been pressed let's try and update the colour
            this.hex = text.replace("\n", "");
            if (this.hex.length() == 6) {
                this.hex = "FF" + this.hex;
            }

            if (this.selectable != null) {
                try {
                    this.selectable.setColor((int) Long.parseLong(hex, 16));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else this.hex = text;
    }

    @Override
    public String getText() {
        return this.hex;
    }

    @Override
    public boolean canEdit(Object... objects) {
        return true;
    }
}
