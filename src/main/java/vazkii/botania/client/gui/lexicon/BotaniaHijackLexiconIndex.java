package vazkii.botania.client.gui.lexicon;

import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.library.mods.BotaniaCommon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.handler.ClientTickHandler;

/** A bunch of this code below is that of Vazkii, this is just to stop the
 * lexicon from Crashing. */
public class BotaniaHijackLexiconIndex extends GuiLexiconIndex {
    public BotaniaHijackLexiconIndex(LexiconCategory category) {
        super(category);
    }

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
    void openEntry(int index) {
        if(index >= entriesToDisplay.size())
            return;

        LexiconEntry entry = entriesToDisplay.get(index);
        mc.displayGuiScreen(new BotaniaHijackLexiconEntry(entry, this));
        ClientTickHandler.notifyPageChange();
    }
}
