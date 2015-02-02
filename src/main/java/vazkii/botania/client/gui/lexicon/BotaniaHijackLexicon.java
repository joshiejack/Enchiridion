package vazkii.botania.client.gui.lexicon;

import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.library.mods.BotaniaCommon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.gui.lexicon.button.GuiButtonCategory;

/** A bunch of this code below is that of Vazkii, this is just to stop the
 * lexicon from Crashing. */
public class BotaniaHijackLexicon extends GuiLexicon {
    @Override
    public void initGui() {
        EntityPlayer player = ClientHelper.getPlayer();
        ItemStack previous = player.getCurrentEquippedItem();
        if (previous != null) previous = previous.copy();
        player.setCurrentItemOrArmor(0, BotaniaCommon.lexicon);
        super.initGui();
        player.setCurrentItemOrArmor(0, previous);
    }
    
    @Override
    protected void actionPerformed(GuiButton par1GuiButton) {
        if(par1GuiButton.id >= BOOKMARK_START)
            handleBookmark(par1GuiButton);
        else if(par1GuiButton instanceof GuiButtonCategory) {
            LexiconCategory category = ((GuiButtonCategory) par1GuiButton).getCategory();

            mc.displayGuiScreen(new BotaniaHijackLexiconIndex(category));
            ClientTickHandler.notifyPageChange();
        }
    }
}
