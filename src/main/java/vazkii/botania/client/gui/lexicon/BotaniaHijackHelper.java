package vazkii.botania.client.gui.lexicon;

import net.minecraft.client.gui.GuiScreen;

public class BotaniaHijackHelper {
    public static GuiScreen getGui() {
        return new BotaniaHijackLexicon();
    }
}
