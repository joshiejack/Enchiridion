package joshie.enchiridion.lib;

import net.minecraftforge.fml.ModList;

public class EInfo {
    public static final String JAVAPATH = "joshie.enchiridion.";
    public static final String MODID = "enchiridion";
    public static final String MODNAME = "Enchiridion";
    public static final String TEXPATH = "enchiridion:textures/books/";

    public static final boolean IS_GUIDEAPI_LOADED = ModList.get().isLoaded("guideapi");
}