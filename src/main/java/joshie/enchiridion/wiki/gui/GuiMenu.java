package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static joshie.enchiridion.wiki.WikiHelper.mouseY;

import java.util.ArrayList;
import java.util.Collection;

import joshie.enchiridion.lib.ETranslate;
import joshie.enchiridion.wiki.WikiCategory;
import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.WikiPage;
import joshie.enchiridion.wiki.elements.Element;

public class GuiMenu extends GuiExtension {
    private int[] getCategoryBGColors(int pageY) {
        int bg_color_1 = 0xFF1A2738;
        int bg_color_2 = 0xFF255174;
        if (mouseX >= 5 && mouseX <= 245) {
            if (mouseY >= pageY + 79 && mouseY <= pageY + 119) {
                bg_color_1 = 0xCC67C3D2;
                bg_color_2 = 0xCC5498CA;
            }
        }

        return new int[] { bg_color_1, bg_color_2 };
    }

    private void drawCategoryBox(boolean isVisible, String title, int pageY, int bg_color_1, int bg_color_2) {
        drawRect(5, pageY + 114, 245, pageY + 117, 0xFFC2C29C);
        verticalGradient(5, pageY + 79, 245, pageY + 82, 0xFF3B3C2C, 0xFFC2C29C);
        drawRect(5, pageY + 82, 245, pageY + 83, 0xFF31443E);
        verticalGradient(5, pageY + 83, 245, pageY + 114, bg_color_1, bg_color_2);
        verticalGradient(5, pageY + 115, 245, pageY + 118, 0xFFC2C29C, 0xFF3B3C2C);
        drawRect(5, pageY + 118, 245, pageY + 119, 0xFF071015);
        drawScaledText(2F, (isVisible ? " - " : " + ") + title, 15, 92 + pageY, 0xFFC2C29C);
    }

    private int[] getContentBGColors(int pageY) {
        int page_bg_1 = 0x4410202F;
        int page_bg_2 = 0x4410202F;
        if (mouseX >= 5 && mouseX <= 245) {
            if (mouseY > pageY + 80 + 39 && mouseY < 80 + pageY + 79) {
                page_bg_1 = 0xCC366374;
                page_bg_2 = 0xCC47849A;
            }
        }

        return new int[] { page_bg_1, page_bg_2 };
    }

    private void drawContentBox(String title, int pageY, int page_bg_1, int page_bg_2) {
        horizontalGradient(5, pageY + 80 + 38, 245, 80 + pageY + 40, 0x44354755, 0X44192B39);
        verticalGradient(5, pageY + 80 + 40, 245, 80 + pageY + 42, 0x44172A39, 0x44091D28);
        horizontalGradient(5, pageY + 80 + 42, 245, 80 + pageY + 77, page_bg_1, page_bg_2);
        horizontalGradient(5, pageY + 80 + 77, 245, 80 + pageY + 78, 0x44354755, 0X44192B39);
        verticalGradient(5, pageY + 80 + 78, 245, 80 + pageY + 80, 0x44172A39, 0x44091D28);
        drawScaledText(2F, title, 15, 53 + 80 + pageY, 0xFFC2C29C);
    }

    private static ArrayList<Element> visibleElementLayers;
    private static Collection<WikiCategory> visibleCategories;

    private static int layerPosition = 0;
    private static int categoryPosition = 0;

    public void scrollMenu() {

    }

    @Override
    public void draw() {
        if (!isEditMode()) {
            verticalGradient(5, 44, 245, 75, 0xFF10202F, 0xFF10202F);
            horizontalGradient(5, 75, 245, 78, 0xFF354755, 0XFF192B39);
            verticalGradient(5, 78, 245, 80, 0xFF172A39, 0xFF091D28);
            drawScaledText(2F, getTab().getTitle(), 15, 53, 0xFFC2C29C);
            int pageY = 0;
            for (WikiCategory category : getTab().getCategories()) {
                int[] color = getCategoryBGColors(pageY);
                drawCategoryBox(category.isVisible(), category.getTitle(), pageY, color[0], color[1]);

                if (category.isVisible()) {
                    for (WikiPage page : category.getPages()) {
                        int[] colors = getContentBGColors(pageY);
                        drawContentBox(page.getTitle(), pageY, colors[0], colors[1]);
                        pageY += 40;
                    }
                }

                pageY += 40;
            }
        } else if (getPage().getSelected() == null) {
            verticalGradient(5, 44, 245, 75, 0xFF1A2738, 0xFF255174);
            horizontalGradient(5, 75, 245, 78, 0xFFC2C29C, 0xFFC2C29C);
            verticalGradient(5, 78, 245, 80, 0xFF172A39, 0xFF091D28);
            drawScaledText(2F, ETranslate.translate("layers"), 15, 53, 0xFFC2C29C);
            int pageY = -40;

            ArrayList<Element> elements = getPage().getContent().getComponents();
            for (int i = layerPosition; i <  Math.min(elements.size(), layerPosition + ((WikiHelper.height - 220) / 40)); i++) {
                int[] colors = getContentBGColors(pageY);
                drawContentBox(elements.get(i).getTitle(), pageY, colors[0], colors[1]);
                pageY += 40;
            }

            pageY += 40;
        }
    }

    @Override
    public void clicked(int button) {
        if (mouseX >= 5 && mouseX <= 245) {
            if (!isEditMode()) {
                int pageY = 0;
                for (WikiCategory category : getTab().getCategories()) {
                    if (mouseY >= pageY + 79 && mouseY <= pageY + 119) {
                        if (category.isVisible()) {
                            category.setHidden();
                        } else category.setVisible();
                        break;
                    }

                    if (category.isVisible()) {
                        for (WikiPage page : category.getPages()) {
                            if (mouseY >= pageY + 80 + 38 && mouseY <= 80 + pageY + 80) {
                                setPage(category.getTab().getMod().getKey(), category.getTab().getKey(), category.getKey(), page.getKey());
                            }

                            pageY += 40;
                        }
                    }

                    pageY += 40;
                }
            } else if (getPage().getSelected() == null) {
                int pageY = -40;
                ArrayList<Element> elements = getPage().getContent().getComponents();
                for (int i = layerPosition; i < Math.min(elements.size(), layerPosition + ((WikiHelper.height - 220) / 40)); i++) {
                    if (mouseY >= pageY + 80 + 38 && mouseY <= 80 + pageY + 80) {
                        getPage().getContent().moveUp(elements.get(i));
                        break;
                    }

                    pageY += 40;
                }

                pageY += 40;
            }
        }
    }

    @Override
    public void scroll(boolean scrolledDown) {
        if (mouseX >= 5 && mouseX <= 245) {
            if(scrolledDown) {
                if(isEditMode()) {
                    this.layerPosition++;
                    this.layerPosition = Math.min(layerPosition, getPage().getContent().getComponents().size() - 1);
                } else {
                    this.categoryPosition++;
                    this.categoryPosition = Math.min(categoryPosition, getTab().getCategories().size());
                }
            } else {
                if(isEditMode()) {
                    this.layerPosition--;
                    this.layerPosition = Math.max(layerPosition, 0);
                } else {
                    this.categoryPosition--;
                    this.categoryPosition = Math.max(categoryPosition, 0);
                }
            }
        }
    }
}
