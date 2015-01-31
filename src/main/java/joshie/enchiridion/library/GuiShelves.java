package joshie.enchiridion.library;

import static joshie.enchiridion.library.GuiShelves.ShelfType.BLANK;
import static joshie.enchiridion.library.GuiShelves.ShelfType.LINES;
import static joshie.enchiridion.library.GuiShelves.ShelfType.WOOD;
import static joshie.enchiridion.wiki.WikiHelper.drawRect;
import static joshie.enchiridion.wiki.WikiHelper.drawScaledTexture;
import static joshie.enchiridion.wiki.gui.GuiMain.texture;
import joshie.enchiridion.wiki.gui.GuiExtension;

public class GuiShelves extends GuiExtension {
    public static ShelfType type = ShelfType.WOOD;

    public static enum ShelfType {
        WOOD, LINES, BLANK;
    }

    @Override
    public void draw() {
        if (type == WOOD) {
            for (int i = 0; i < 10; i++) {
                int y = (i * 74) + 116;
                for (int w = 0; w < 24; w++) {
                    drawScaledTexture(texture, 30 + (int) (w * (16 * 2.5)), y, 240, 0, 16, 5, 2.5F);
                }
            }

            for (int i = 0; i < 28; i++) {
                drawScaledTexture(texture, 22, 106 + (int) (i * (16 * 2)) - 50, 252, 5, 4, 16, 2F);
                drawScaledTexture(texture, 989, 106 + (int) (i * (16 * 2)) - 50, 252, 5, 4, 16, 2F);
            }
        } else if (type == LINES) {
            for (int i = 0; i < 10; i++) {
                int y = (i * 74) + 120;
                drawRect(20, y + 3, 1000, y + 5, 0xFFC2C29C);
                drawRect(20, y + 1, 1000, y + 3, 0xCC2A535E);
                drawScaledTexture(texture, 120, y, 240, 0, 16, 5, 1F);
            }
        } else if (type == BLANK) {
            return;
        }
    }
}
