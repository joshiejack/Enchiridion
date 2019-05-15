package joshie.enchiridion.gui.book;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.Enchiridion;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IFeatureProvider;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class GuiLayers extends AbstractGuiOverlay {
    public static final GuiLayers INSTANCE = new GuiLayers();
    private static final ResourceLocation LOCK_DFLT = new ResourceLocation(EInfo.MODID, "textures/books/lock_dftl.png");
    private static final ResourceLocation LOCK_HOVER = new ResourceLocation(EInfo.MODID, "textures/books/lock_hover.png");
    private static final ResourceLocation VISIBLE_DFLT = new ResourceLocation(EInfo.MODID, "textures/books/layer_dftl.png");
    private static final ResourceLocation VISIBLE_HOVER = new ResourceLocation(EInfo.MODID, "textures/books/layer_hover.png");
    private IFeatureProvider dragged = null;
    private int held = 0;
    private int yStart = 0;
    private int layerPosition = 0;

    private GuiLayers() {
    }

    public boolean isDragging() {
        return held != 0;
    }

    private boolean isOverLayer(int layerY, int mouseX, int mouseY) {
        if (mouseX > EConfig.layersXPos + 20 && mouseX <= EConfig.layersXPos + 83) {
            return mouseY >= EConfig.toolbarYPos - 3 + layerY && mouseY <= EConfig.toolbarYPos + 8 + layerY;
        }
        return false;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        EnchiridionAPI.draw.drawImage(SIDEBAR, EConfig.layersXPos - 3, EConfig.toolbarYPos - 7, EConfig.layersXPos + 87, EConfig.timelineYPos + 13);
        EnchiridionAPI.draw.drawBorderedRectangle(EConfig.layersXPos, EConfig.toolbarYPos + 7, EConfig.layersXPos + 85, EConfig.timelineYPos + 11, 0xFF312921, 0xFF191511);
        EnchiridionAPI.draw.drawSplitScaledString("[b]" + Enchiridion.translate("layers") + "[/b]", EConfig.layersXPos + 20, EConfig.toolbarYPos - 2, 250, 0xFFFFFFFF, 1F);
        int layerY = 0;
        int hoverY = 0;
        ArrayList<IFeatureProvider> features = EnchiridionAPI.book.getPage().getFeatures();
        for (int i = layerPosition; i < Math.min(features.size(), layerPosition + 24); i++) {
            layerY += 12;
            IFeatureProvider feature = features.get(i);
            /* LOCK ICON */
            //Setup the defaults for the lock icon
            ResourceLocation resource = LOCK_DFLT;
            int color1 = 0xFFE4D6AE;
            int color2 = 0x5579725A;

            if (feature.isFromTemplate()) {
                color1 = 0xFFDEAE3B;
                color2 = 0xFF785820;
            }

            //Switch over to the hover if it applies
            if (mouseX >= EConfig.layersXPos + 4 && mouseX <= EConfig.layersXPos + 9 && mouseY >= EConfig.toolbarYPos - 1 + layerY && mouseY <= EConfig.toolbarYPos + layerY + 5) {
                resource = LOCK_HOVER;
                if (feature.isFromTemplate()) {
                    color1 = 0xFFA5812C;
                    color2 = 0xFF543D16;
                } else {
                    color1 = 0xFFB0A483;
                    color2 = 0xFF48453C;
                }
            }

            //Draw the lock icons
            EnchiridionAPI.draw.drawBorderedRectangle(EConfig.layersXPos + 2, EConfig.toolbarYPos - 3 + layerY, EConfig.layersXPos + 11, EConfig.toolbarYPos + 7 + layerY, color1, color2);
            if (feature.isLocked()) {
                EnchiridionAPI.draw.drawImage(resource, EConfig.layersXPos + 4, EConfig.toolbarYPos - 1 + layerY, EConfig.layersXPos + 9, EConfig.toolbarYPos + layerY + 5);
            }
            /* END LOCK ICON */

            /* VISIBILITY ICON */
            //Reset
            resource = VISIBLE_DFLT;
            if (feature.isFromTemplate()) {
                color1 = 0xFFDEAE3B;
                color2 = 0xFF785820;
            } else {
                color1 = 0xFFE4D6AE;
                color2 = 0x5579725A;
            }

            if (mouseX > EConfig.layersXPos + 9 && mouseX < EConfig.layersXPos + 20 && mouseY >= EConfig.toolbarYPos - 1 + layerY && mouseY <= EConfig.toolbarYPos + layerY + 5) {
                resource = VISIBLE_HOVER;
                if (feature.isFromTemplate()) {
                    color1 = 0xFFA5812C;
                    color2 = 0xFF543D16;
                } else {
                    color1 = 0xFFB0A483;
                    color2 = 0xFF48453C;
                }
            }

            EnchiridionAPI.draw.drawBorderedRectangle(EConfig.layersXPos + 11, EConfig.toolbarYPos - 3 + layerY, EConfig.layersXPos + 20, EConfig.toolbarYPos + 7 + layerY, color1, color2);
            if (feature.isVisible()) {
                EnchiridionAPI.draw.drawImage(resource, EConfig.layersXPos + 12, EConfig.toolbarYPos - 1 + layerY, EConfig.layersXPos + 19, EConfig.toolbarYPos + layerY + 5);
            }
            /* END VISIBILITY ICON */

            /* Layer itself */
            EnchiridionAPI.draw.drawBorderedRectangle(EConfig.layersXPos + 20, EConfig.toolbarYPos - 3 + layerY, EConfig.layersXPos + 83, EConfig.toolbarYPos + 7 + layerY, color1, color2);

            if (isOverLayer(layerY, mouseX, mouseY) || EnchiridionAPI.book.isGroupSelected(feature)) {
                hoverY = layerY;
                if (feature.isFromTemplate()) {
                    color1 = 0xFFA5812C;
                    color2 = 0xFF543D16;
                } else {
                    color1 = 0xFFB0A483;
                    color2 = 0xFF48453C;
                }
                EnchiridionAPI.draw.drawBorderedRectangle(EConfig.layersXPos + 20, EConfig.toolbarYPos - 3 + layerY, EConfig.layersXPos + 83, EConfig.toolbarYPos + 7 + layerY, color1, color2);
            }
            /* End Layer */

            String name = feature.getFeature().getName();
            String truncated = (name.substring(0, Math.min(name.length(), 20))).replace("\n", " ");
            EnchiridionAPI.draw.drawSplitScaledString(truncated, EConfig.layersXPos + 25, EConfig.toolbarYPos + layerY, 250, 0xFF000000, 0.5F);
        }


        //Dragging!
        if (dragged != null && hoverY != 0) {
            if (held < 20) {
                held++;
            } else {
                String name = dragged.getFeature().getName();
                String truncated = name.substring(0, Math.min(name.length(), 20));
                EnchiridionAPI.draw.drawBorderedRectangle(EConfig.layersXPos + 20, EConfig.toolbarYPos - 3 + hoverY, EConfig.layersXPos + 83, EConfig.toolbarYPos + 7 + hoverY, 0xFFEEEEEE, 0xFF48453C);
                EnchiridionAPI.draw.drawSplitScaledString(truncated, EConfig.layersXPos + 25, EConfig.toolbarYPos + hoverY, 250, 0xFF000000, 0.5F);
            }
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        int layerY = 0;
        ArrayList<IFeatureProvider> features = EnchiridionAPI.book.getPage().getFeatures();
        for (int i = layerPosition; i < Math.min(features.size(), layerPosition + 20); i++) {
            IFeatureProvider provider = features.get(i);
            layerY += 12;
            if (!provider.isLocked() && isOverLayer(layerY, mouseX, mouseY)) {
                yStart = mouseY;
                dragged = features.get(i);
                GuiBook.INSTANCE.selectLayer(dragged);
                return true;
            }
        }

        dragged = null;
        return false;
    }

    private void insertLayerAt(int mouseY, int layerNumber) {
        int change = 0;
        int difference = mouseY - yStart;
        if (difference > 0) change = 0;
        else if (difference < 0) change = -1;

        if (dragged.getLayerIndex() != layerNumber) {
            dragged.setLayerIndex(layerNumber + change);
            //Resort
            EnchiridionAPI.book.getPage().sort();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        boolean placing = held >= 20;
        int layerY = 0;
        ArrayList<IFeatureProvider> features = EnchiridionAPI.book.getPage().getFeatures();
        for (int i = layerPosition; i < Math.min(features.size(), layerPosition + 24); i++) {
            layerY += 12;
            if (isOverLayer(layerY, mouseX, mouseY)) {
                if (placing) {
                    insertLayerAt(mouseY, features.get(i).getLayerIndex());
                } else {
                    IFeatureProvider selected = EnchiridionAPI.book.getSelected();
                    if (selected != null) selected.deselect();
                    EnchiridionAPI.book.setSelected(features.get(i));
                    selected = EnchiridionAPI.book.getSelected();
                    selected.select(mouseX, mouseY);
                    selected.select(mouseX, mouseY);
                    selected.mouseReleased(mouseX, mouseY, 0);
                }
            }
            
            /* LOCK */
            if (mouseX >= EConfig.layersXPos + 4 && mouseX <= EConfig.layersXPos + 9 && mouseY >= EConfig.toolbarYPos - 1 + layerY && mouseY <= EConfig.toolbarYPos + layerY + 5) {
                IFeatureProvider feature = features.get(i);
                feature.setLocked(!feature.isLocked());
            }
            /* END LOCK */
            /* VISIBLE */
            if (mouseX > EConfig.layersXPos + 9 && mouseX < EConfig.layersXPos + 20 && mouseY >= EConfig.toolbarYPos - 1 + layerY && mouseY <= EConfig.toolbarYPos + layerY + 5) {
                IFeatureProvider feature = features.get(i);
                feature.setVisible(!feature.isVisible());
            }
            /* END VISIBLE */
        }


        //Reset
        dragged = null;
        yStart = 0;
        held = 0;
    }

    @Override
    public void scroll(boolean down, int mouseX, int mouseY) {
        if (mouseX >= EConfig.layersXPos) {
            if (down) {
                this.layerPosition++;
            } else {
                this.layerPosition--;
                this.layerPosition = Math.max(layerPosition, 0);
            }
        }
    }
}