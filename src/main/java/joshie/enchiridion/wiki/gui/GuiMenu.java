package joshie.enchiridion.wiki.gui;

import static joshie.enchiridion.wiki.WikiHelper.mouseX;
import static joshie.enchiridion.wiki.WikiHelper.mouseY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import joshie.enchiridion.api.ITextEditable;
import joshie.enchiridion.wiki.WikiCategory;
import joshie.enchiridion.wiki.WikiPage;

public class GuiMenu extends GuiExtension implements ITextEditable {
    private static Collection<WikiCategory> visibleCategories;
    private static int categoryPosition = 0;

    protected static boolean isEditing;
    private static WikiCategory edit_category;
    private static WikiPage edit_page;
    private static boolean init = false;

    public static void clear() {
        isEditing = false;
    }

    public static void setEditing() {
        isEditing = true;
    }

    public static boolean isEditing() {
        return isEditing;
    }

    @Override
    public void draw() {
        if (!init) {
            getMaxPosition();
            init = true;
        }

        if (!isEditMode() || isEditing) {
            verticalGradient(5, 44, 270, 75, 0xFF10202F, 0xFF10202F);
            horizontalGradient(5, 75, 270, 78, 0xFF354755, 0XFF192B39);
            verticalGradient(5, 78, 270, 80, 0xFF172A39, 0xFF091D28);
            drawScaledText(2F, getTab().getTitle(), 15, 53, 0xFFC2C29C);
            int pageY = 0;
            int element_id = -1;
            int pages_in_category = 0;
            for (WikiCategory category : getTab().getCategories()) {
                pages_in_category += category.isVisible() ? category.getPages().size() : 1;
                if (categoryPosition < pages_in_category) {
                    int[] color = getCategoryBGColors(pageY);
                    drawCategoryBox(category.isVisible(), GuiTextEdit.getText(this, category.getTitle(), category), pageY, color[0], color[1]);
                    if (category.isVisible()) {
                        for (WikiPage page : category.getPages()) {
                            element_id++;
                            if (element_id < categoryPosition) continue;
                            int[] colors = getContentBGColors(pageY);
                            drawContentBox(GuiTextEdit.getText(this, page.getTitle(), page), pageY, colors[0], colors[1]);
                            pageY += 40;
                        }
                    }

                    pageY += 40;
                } else {
                    element_id += pages_in_category;
                }
            }
        }
    }

    @Override
    public void clicked(int button) {
        if (!isEditMode() || isEditing) {
            if (mouseX >= 5 && mouseX <= 270) {
                int pageY = 0;
                int element_id = -1;
                int pages_in_category = 0;
                for (WikiCategory category : getTab().getCategories()) {
                    pages_in_category += category.isVisible() ? category.getPages().size() : 1;
                    if (categoryPosition < pages_in_category) {
                        if (mouseY >= pageY + 79 && mouseY <= pageY + 119) {
                            if (!isEditMode() || button > 0) {
                                if (category.isVisible()) {
                                    category.setHidden();
                                } else category.setVisible();
                                break;
                            } else {
                                edit_page = null;
                                edit_category = category;

                                GuiTextEdit.select(this);
                            }
                        }

                        if (category.isVisible()) {
                            for (WikiPage page : category.getPages()) {
                                element_id++;
                                if (element_id < categoryPosition) continue;
                                if (mouseY >= pageY + 80 + 38 && mouseY <= 80 + pageY + 80) {
                                    if (!isEditMode()) {
                                        setPage(category.getTab().getMod().getKey(), category.getTab().getKey(), category.getKey(), page.getKey());
                                    } else {
                                        edit_category = null;
                                        edit_page = page;
                                        GuiTextEdit.select(this);
                                    }
                                }

                                pageY += 40;
                            }
                        }

                        pageY += 40;
                    } else {
                        element_id += pages_in_category;
                    }
                }
            }
        }

    }

    private int getMaxPosition() {
        int ret = 0;
        for (WikiCategory category : getTab().getCategories()) {
            ret += category.getPages().size();
        }

        return ret - 1;
    }

    @Override
    public void scroll(boolean scrolledDown) {
        if (!isEditMode() || isEditing) {
            if (mouseX >= 5 && mouseX <= 270) {
                if (scrolledDown) {
                    this.categoryPosition++;
                    this.categoryPosition = Math.min(categoryPosition, getMaxPosition());
                } else {
                    this.categoryPosition--;
                    this.categoryPosition = Math.max(categoryPosition, 0);
                }
            } else {
                if (!isEditMode()) {
                    this.categoryPosition--;
                    this.categoryPosition = Math.max(categoryPosition, 0);
                }
            }
        }
    }

    @Override
    public void setText(String text) {
        if (edit_category != null) {
            edit_category.setTranslation(text);
        } else if (edit_page != null) {
            edit_page.setTranslation(text);
        }
    }

    @Override
    public String getText() {
        if (edit_category != null) {
            return edit_category.getTitle();
        } else if (edit_page != null) {
            return edit_page.getTitle();
        } else return "";
    }

    @Override
    public boolean canEdit(Object... objects) {
        if (isEditing) {
            if (objects[0] instanceof WikiPage) {
                return objects[0] == edit_page;
            } else if (objects[0] instanceof WikiCategory) {
                return objects[0] == edit_category;
            } else return false;
        } else return false;
    }
}
