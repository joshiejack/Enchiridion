package vazkii.botania.client.gui.lexicon;

import joshie.enchiridion.helpers.ClientHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.lexicon.LexiconEntry;

/** A bunch of this code below is that of Vazkii, this is just to stop the
 * lexicon from Crashing. */
public class BotaniaHijackLexiconEntry extends GuiLexiconEntry {
    public BotaniaHijackLexiconEntry(LexiconEntry entry, GuiScreen parent) {
        super(entry, parent);
    }
    
    @Override
    public void initGui() {
        EntityPlayer player = ClientHelper.getPlayer();
        ItemStack previous = player.getCurrentEquippedItem();
        if (previous != null) previous = previous.copy();
        player.setCurrentItemOrArmor(0, new ItemStack(Items.stick));
        super.initGui();
        player.setCurrentItemOrArmor(0, previous);
    }
}
