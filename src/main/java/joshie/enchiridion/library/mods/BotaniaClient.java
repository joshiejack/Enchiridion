package joshie.enchiridion.library.mods;

import joshie.enchiridion.library.handlers.BotaniaBookHandler;
import net.minecraftforge.common.MinecraftForge;

public class BotaniaClient extends BotaniaCommon {
    public static final BotaniaClient INSTANCE = new BotaniaClient();

    @Override
    public void init() {
        super.init();

        MinecraftForge.EVENT_BUS.register(new BotaniaBookHandler());
    }
}
