package joshie.enchiridion.designer.features;

import static joshie.enchiridion.designer.DesignerHelper.drawRect;
import static joshie.enchiridion.designer.DesignerHelper.getGui;
import joshie.enchiridion.designer.DesignerHelper;
import joshie.enchiridion.helpers.ClientHelper;

import com.google.gson.annotations.Expose;

public class FeatureJump extends FeatureWithText {
    @Expose
    public String jumpTo = "2";
    @Expose
    public String texture = "";

    private FeatureImage image = null;

    @Override
    public String getTextField() {
        return jumpTo;
    }

    @Override
    public void setTextField(String str) {
        this.jumpTo = str;
    }

    @Override
    public void drawFeature() {
        if (getGui().canEdit) {
            drawRect(left - 4, top - 4, right, top, 0xFF000000);
            drawRect(right, top - 4, right + 4, bottom, 0xFF000000);
            drawRect(left - 4, top, left, bottom + 4, 0xFF000000);
            drawRect(left, bottom, right + 4, bottom + 4, 0xFF000000);
        }

        if (!texture.equals("")) {
            if (isOverFeature(DesignerHelper.getGui().mouseX, DesignerHelper.getGui().mouseY)) {
                if (image == null) image = new FeatureImage(this).setPath(texture);
                image.drawFeature();
            }
        }
    }

    @Override
    public void click(int x, int y, boolean isEditMode) {
        if ((isEditMode && ClientHelper.isShiftPressed()) || !isEditMode) {
            if (isOverFeature(x, y)) {
                isSelected = false;
                clearSelected();

                if (jumpTo != null) {
                    try {
                        int jump = Integer.parseInt(jumpTo) - 1;
                        DesignerHelper.getGui().setPage(jump);
                    } catch (Exception e) {
                        DesignerHelper.getGui().setPage(jumpTo);
                        clearSelected();
                    }
                }
            }

            return;
        }

        super.click(x, y, isEditMode);

    }

    @Override
    public void loadEditor() {
        // TODO Auto-generated method stub

    }
}
