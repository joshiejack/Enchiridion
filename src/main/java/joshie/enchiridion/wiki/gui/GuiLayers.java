package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static joshie.enchiridion.wiki.WikiHelper.mouseY;

import java.util.ArrayList;

import joshie.enchiridion.lib.ETranslate;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.elements.Element;

public class GuiLayers extends GuiExtension {
    private static boolean SHOW_LAYERS = false;
    private static ArrayList<Element> visibleElementLayers;
    private static int layerPosition = 0;

    public static void clear() {
        GuiLayers.SHOW_LAYERS = false;
    }
    
    public static void setActive(boolean isEditMode) {
        GuiLayers.SHOW_LAYERS = isEditMode;
    }
    
    @Override
    public void draw() {
        if (SHOW_LAYERS) {
            if (getPage().getSelected() == null) {
                verticalGradient(5, 44, 270, 75, 0xFF1A2738, 0xFF255174);
                horizontalGradient(5, 75, 270, 78, 0xFFC2C29C, 0xFFC2C29C);
                verticalGradient(5, 78, 270, 80, 0xFF172A39, 0xFF091D28);
                drawScaledText(2F, ETranslate.translate("layers"), 15, 53, 0xFFC2C29C);
                int pageY = -40;

                ArrayList<Element> elements = getPage().getData().getComponents();
                for (int i = layerPosition; i < Math.min(elements.size(), layerPosition + ((WikiHelper.height - 220) / 40)); i++) {
                    int[] colors = getContentBGColors(pageY);
                    drawContentBox(elements.get(i).getTitle(), pageY, colors[0], colors[1]);
                    pageY += 40;
                }

                pageY += 40;
            }
        }
    }

    @Override
    public void clicked(int button) {
        if (SHOW_LAYERS) {
            if (mouseX >= 5 && mouseX <= 245) {
                if (getPage().getSelected() == null) {
                    int pageY = -40;
                    ArrayList<Element> elements = getPage().getData().getComponents();
                    for (int i = layerPosition; i < Math.min(elements.size(), layerPosition + ((WikiHelper.height - 220) / 40)); i++) {
                        if (mouseY >= pageY + 80 + 38 && mouseY <= 80 + pageY + 80) {
                            getPage().getData().moveUp(elements.get(i));
                            break;
                        }

                        pageY += 40;
                    }

                    pageY += 40;
                }
            }
        }
    }

    @Override
    public void scroll(boolean scrolledDown) {
        if (SHOW_LAYERS) {
            if (mouseX >= 5 && mouseX <= 245) {
                if (scrolledDown) {
                    this.layerPosition++;
                    this.layerPosition = Math.min(layerPosition, getPage().getData().getComponents().size() - 1);
                } else {
                    this.layerPosition--;
                    this.layerPosition = Math.max(layerPosition, 0);
                }
            }
        }
    }
}
