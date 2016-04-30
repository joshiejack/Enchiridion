package joshie.enchiridion.gui.book;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.gui.IToolbarButton;

import java.util.ArrayList;
import java.util.List;

public class GuiToolbar extends AbstractGuiOverlay {
    public static final GuiToolbar INSTANCE = new GuiToolbar();
    private List<IToolbarButton> leftButtons = new ArrayList();
    private List<IToolbarButton> rightButtons = new ArrayList();

    private GuiToolbar() {}

    public void registerButton(IToolbarButton button) {
        if (button.isLeftAligned()) leftButtons.add(button);
        else rightButtons.add(button);
    }

    private boolean isOverButton(int xPos, int mouseX, int mouseY) {
        return mouseX >= xPos && mouseX <= xPos + 8 && mouseY >= EConfig.toolbarYPos + 2 && mouseY <= EConfig.toolbarYPos + 10;
    }

    private static final int xStart = -3;
    private static final int xEnd = 426;

    @Override
    public void draw(int mouseX, int mouseY) {
        EnchiridionAPI.draw.drawImage(toolbar, -10, EConfig.toolbarYPos - 5, 441, EConfig.toolbarYPos + 17);
        EnchiridionAPI.draw.drawBorderedRectangle(-6, EConfig.toolbarYPos, 437, EConfig.toolbarYPos + 12, 0xFFE4D6AE, 0x5579725A);

        //Draw the left hand buttons first
        int x = xStart;
        for (IToolbarButton button: leftButtons) {
            if (!isOverButton(x, mouseX, mouseY)) EnchiridionAPI.draw.drawImage(button.getResource(), x, EConfig.toolbarYPos + 2, x + 8, EConfig.toolbarYPos + 10);
            else EnchiridionAPI.draw.drawImage(button.getHoverResource(), x, EConfig.toolbarYPos + 2, x + 8, EConfig.toolbarYPos + 10);

            x+= 12;
        }

        //Now draw the right hand buttons
        x = xEnd;
        for (IToolbarButton button: rightButtons) {
            if (!isOverButton(x, mouseX, mouseY)) EnchiridionAPI.draw.drawImage(button.getResource(), x, EConfig.toolbarYPos + 2, x + 8, EConfig.toolbarYPos + 10);
            else EnchiridionAPI.draw.drawImage(button.getHoverResource(), x, EConfig.toolbarYPos + 2, x + 8, EConfig.toolbarYPos + 10);

            x-= 12;
        }
    }

    @Override
    public void addToolTip(List<String> tooltip, int mouseX, int mouseY) {
        int x = xStart;
        for (IToolbarButton button: leftButtons) {
            if (isOverButton(x, mouseX, mouseY)) {
                tooltip.add(button.getTooltip());
            }

            x+= 12;
        }

        //Right side of buttons
        x = xEnd;
        for (IToolbarButton button: rightButtons) {
            if (isOverButton(x, mouseX, mouseY)) {
                tooltip.add(button.getTooltip());
            }

            x-= 12;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        int x = xStart;
        for (IToolbarButton button: leftButtons) {
            if (isOverButton(x, mouseX, mouseY)) {
                button.performAction();
            }

            x+= 12;
        }

        //Right side of buttons
        x = xEnd;
        for (IToolbarButton button: rightButtons) {
            if (isOverButton(x, mouseX, mouseY)) {
                button.performAction();
            }

            x-= 12;
        }
    }
}
