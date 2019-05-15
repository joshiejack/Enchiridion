package joshie.enchiridion.gui.book;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.api.book.ITemplate;
import joshie.enchiridion.data.book.Page;
import joshie.enchiridion.helpers.MCClientHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class GuiSimpleEditorTemplate extends GuiSimpleEditorAbstract {
    public static final GuiSimpleEditorTemplate INSTANCE = new GuiSimpleEditorTemplate();

    private final HashMap<String, ITemplate> templates = new HashMap<>();
    private ArrayList<ITemplate> sorted = new ArrayList<>();
    private int position = 0;

    protected GuiSimpleEditorTemplate() {
    }


    public void registerTemplate(ITemplate template) {
        templates.put(template.getUniqueName(), template);

        for (IFeatureProvider provider : template.getFeatures()) {
            provider.update(new Page(0));
        }
    }

    public List<IFeatureProvider> getFeaturesFromString(String unique) {
        return templates.get(unique).getFeatures();
    }

    private boolean isOverPosition(int x1, int y1, int x2, int y2, int mouseX, int mouseY) {
        if (mouseX >= EConfig.editorXPos + x1 && mouseX <= EConfig.editorXPos + x2) {
            return mouseY >= EConfig.toolbarYPos + y1 && mouseY <= EConfig.toolbarYPos + y2;
        }
        return false;
    }

    //CONVERT IN TO BUTTONS

    private static final int X_POS_START = 4;

    private void drawBoxLabel(String name, int yPos) {
        drawBorderedRectangle(X_POS_START - 2, yPos, 83, yPos + 10, 0xFFB0A483, 0xFF48453C);
        drawSplitScaledString("[b]" + name + "[/b]", X_POS_START, yPos + 3, 0xFF48453C, 0.5F);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int count = 0;
        int yPlus = 0;
        int xPlus = 0;
        for (ITemplate template : sorted) {
            if (count < position || count > position + 17) {
                count++;
                continue;
            }

            if (isOverPosition(2 + xPlus, 11 + yPlus, 42 + xPlus, 34 + yPlus, mouseX, mouseY)) {
                for (IFeatureProvider provider : template.getFeatures()) {
                    provider.draw(mouseX, mouseY);
                }
            }

            drawImage(template.getIcon(), 2 + xPlus, 11 + yPlus, 42 + xPlus, 34 + yPlus);
            if (GuiBook.INSTANCE.getBook().getDefaultFeatures().contains(template.getUniqueName())) {
                drawBorderedRectangle(2 + xPlus, 11 + yPlus, 42 + xPlus, 34 + yPlus, 0x00000000, 0xFF8C0000);
            }

            xPlus += 41;

            if (xPlus >= 42) {
                xPlus = 0;
                yPlus += 25;
            }

            count++;
        }
    }

    @Override
    public void scroll(boolean down, int mouseX, int mouseY) {
        if (mouseX >= EConfig.editorXPos + 7 && mouseX <= EConfig.editorXPos + 91) {
            if (mouseY >= EConfig.toolbarYPos + 14 && mouseY <= EConfig.toolbarYPos + 302) {
                if (down) {
                    position = Math.min(((sorted.size() + 1) / 2), position + 2);
                } else {
                    position = Math.max(0, position - 2);
                }
            }
        }
    }

    @Override
    public void addToolTip(List<String> tooltip, int mouseX, int mouseY) {
        int count = 0;
        int yPlus = 0;
        int xPlus = 0;
        for (ITemplate template : sorted) {
            if (count < position || count > position + 17) {
                count++;
                continue;
            }

            if (isOverPosition(2 + xPlus, 11 + yPlus, 42 + xPlus, 34 + yPlus, mouseX, mouseY)) {
                tooltip.add(template.getTemplateName());
                tooltip.add("");
                tooltip.add(Enchiridion.translate("template.click"));
                return;
            }

            xPlus += 41;

            if (xPlus >= 42) {
                xPlus = 0;
                yPlus += 25;
            }

            count++;
        }
    }

    private void switchDefaulthood(ITemplate template) {
        HashSet<String> set = new HashSet<>(GuiBook.INSTANCE.getBook().getDefaultFeatures());
        if (set.contains(template.getUniqueName())) {
            set.remove(template.getUniqueName());
        } else set.add(template.getUniqueName());

        GuiBook.INSTANCE.getBook().setDefaultFeatures(set);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        int count = 0;
        int yPlus = 0;
        int xPlus = 0;
        for (ITemplate template : sorted) {
            for (IFeatureProvider provider : template.getFeatures()) {
                provider.setFromTemplate(true);
            }
            if (count < position || count > position + 17) {
                count++;
                continue;
            }

            if (isOverPosition(2 + xPlus, 11 + yPlus, 42 + xPlus, 34 + yPlus, mouseX, mouseY)) {
                if (MCClientHelper.isShiftPressed()) {
                    switchDefaulthood(template);
                } else {
                    for (IFeatureProvider provider : template.getFeatures()) {
                        GuiBook.INSTANCE.getPage().addFeature(provider.getFeature(), provider.getLeft(), provider.getTop(), provider.getWidth(), provider.getHeight(), provider.isLocked(), !provider.isVisible(), provider.isFromTemplate());
                    }
                }
                return true;
            }
            xPlus += 41;

            if (xPlus >= 42) {
                xPlus = 0;
                yPlus += 25;
            }
            count++;
        }
        return false;
    }

    @Override
    public void updateSearch(String search) {
        sorted = new ArrayList<>();
        if (search == null || search.equals("")) {
            sorted.addAll(templates.values());
        } else {
            sorted.addAll(templates.values().stream().filter(template -> template.getTemplateName().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toList()));
        }
    }
}