package joshie.enchiridion;

import static joshie.enchiridion.wiki.WikiHandler.wiki;
import joshie.enchiridion.helpers.ClientHelper;
import joshie.enchiridion.wiki.WikiFont;
import joshie.enchiridion.wiki.WikiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;

public class EClientProxy extends ECommonProxy {
    public static WikiFont font;

    @Override
    public void setupClient() {
        MinecraftForge.EVENT_BUS.register(new WikiHandler());
        FMLCommonHandler.instance().bus().register(new WikiHandler());
        wiki = new KeyBinding("enchiridion.key.wiki", Keyboard.KEY_F8, "key.categories.misc");
        Minecraft mc = ClientHelper.getMinecraft();
        font = new WikiFont(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
        if (mc.getLanguageManager() != null) {
            font.setUnicodeFlag(mc.func_152349_b());
            font.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
        }
        
        ((IReloadableResourceManager)mc.getResourceManager()).registerReloadListener(font);
    }
}
