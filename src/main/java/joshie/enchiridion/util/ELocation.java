package joshie.enchiridion.util;

import joshie.enchiridion.lib.EInfo;
import net.minecraft.util.ResourceLocation;

public class ELocation extends ResourceLocation {
    public ELocation(String resource) {
        super(EInfo.TEXPATH + resource + ".png");
    }
}