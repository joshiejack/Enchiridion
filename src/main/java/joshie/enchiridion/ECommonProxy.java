package joshie.enchiridion;

import static joshie.enchiridion.Enchiridion.instance;
import joshie.enchiridion.handlers.GuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ECommonProxy {
    public void preInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }
    
    public void init() {}
    public void postInit() {
        setupClient();
    }

    public void setupClient() {}
}
