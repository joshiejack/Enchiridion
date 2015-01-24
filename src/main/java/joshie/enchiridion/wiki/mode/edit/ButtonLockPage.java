package joshie.enchiridion.wiki.mode.edit;

import joshie.enchiridion.wiki.WikiHelper;
import joshie.enchiridion.wiki.mode.ButtonBase;
import net.minecraft.client.Minecraft;

public class ButtonLockPage extends ButtonBase {
    public ButtonLockPage(int id, int x, int y, int width, int height, float scale) {
        super(id, x, y, width, height, "lock", 2F);
    }

    @Override
    public void onClicked() {
        WikiHelper.setVisibility(ConfirmLocking.class, true);
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        super.drawButton(mc, x, y);
    }
}
