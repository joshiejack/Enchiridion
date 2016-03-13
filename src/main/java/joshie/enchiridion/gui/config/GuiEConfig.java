package joshie.enchiridion.gui.config;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.lib.EInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class GuiEConfig extends GuiConfig {
    public GuiEConfig(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), EInfo.MODID, false, false, ".minecraft/config/enchiridion/enchiridion.cfg");
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        List<IConfigElement> settings = new ConfigElement(EConfig.config.getCategory(EConfig.CATEGORY_SETTINGS.toLowerCase())).getChildElements();
        List<IConfigElement> modSupport = new ConfigElement(EConfig.config.getCategory(EConfig.CATEGORY_MOD_SUPPORT.toLowerCase())).getChildElements();

        list.add(new DummyConfigElement.DummyCategoryElement(EConfig.CATEGORY_SETTINGS, EInfo.MODID + ".config.category.settings", settings));
        list.add(new DummyConfigElement.DummyCategoryElement(EConfig.CATEGORY_MOD_SUPPORT, EInfo.MODID + ".config.category.modsupport", modSupport));

        return list;
    }
}